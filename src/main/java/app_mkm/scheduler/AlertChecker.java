package app_mkm.scheduler;

import app_mkm.entity.Alert;
import app_mkm.entity.AlertHistory;
import app_mkm.repository.AlertHistoryRepository;
import app_mkm.service.AlertService;
import app_mkm.service.CardmarketScraperService;
import app_mkm.service.CardmarketScraperService.CardListing;
import app_mkm.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AlertChecker {

    private final AlertService alertService;
    private final CardmarketScraperService scraperService;
    private final AlertHistoryRepository historyRepository;
    private final EmailService emailService;

    public AlertChecker(AlertService alertService,
                        CardmarketScraperService scraperService,
                        AlertHistoryRepository historyRepository,
                        EmailService emailService) {
        this.alertService = alertService;
        this.scraperService = scraperService;
        this.historyRepository = historyRepository;
        this.emailService = emailService;
    }

    // ✅ Ejecutar cada 15 minutos
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void checkAlerts() {
        System.out.println("🔁 Ejecutando AlertChecker...");

        List<Alert> alerts = alertService.getAll();
        if (alerts.isEmpty()) {
            System.out.println("⚠️ No hay alertas registradas. Saltando ejecución.");
            return;
        }

        for (Alert alert : alerts) {
            try {
                // ⏳ Saltar si la alerta ya saltó hace menos de 7 días
                if (alert.getLastTriggeredAt() != null &&
                        alert.getLastTriggeredAt().isAfter(LocalDateTime.now().minusDays(7))) {
                    continue;
                }

                List<CardListing> listings = scraperService.scrapeCard(alert.getSetName(), alert.getCardName());

                listings.stream()
                        .filter(l -> l.getCountry().equalsIgnoreCase(alert.getCountry()))
                        .filter(l -> parsePrice(l.getPrice()) <= alert.getMaxPrice())
                        .filter(l -> l.getCondition().toLowerCase().contains(alert.getCondition().toLowerCase()))
                        .findFirst()
                        .ifPresent(match -> {
                            double price = parsePrice(match.getPrice());
                            System.out.printf("✅ Match encontrado: %s - %s (%s) %s%n",
                                    alert.getCardName(), match.getSeller(), match.getCountry(), match.getPrice());

                            // 💾 Guardar en historial
                            AlertHistory history = new AlertHistory();
                            history.setAlert(alert);
                            history.setCardName(alert.getCardName());
                            history.setSetName(alert.getSetName());
                            history.setPrice(price);
                            history.setMatchDate(LocalDateTime.now());
                            historyRepository.save(history);

                            // 📧 Enviar correo
                            String subject = "🔔 Alerta activada: " + alert.getCardName();
                            String text = String.format("""
                                    Se ha encontrado una coincidencia para tu alerta:
                                    
                                    Carta: %s
                                    Set: %s
                                    Condición: %s
                                    País: %s
                                    Precio: %.2f €
                                    
                                    Fecha: %s
                                    """,
                                    alert.getCardName(),
                                    alert.getSetName(),
                                    alert.getCondition(),
                                    alert.getCountry(),
                                    price,
                                    LocalDateTime.now().toString()
                            );

                            // Cambia aquí el correo destino si lo tienes en el usuario logueado
                            emailService.sendAlertEmail("tu_correo@gmail.com", subject, text);

                            // ⏱️ Actualizar fecha de última activación
                            alert.setLastTriggeredAt(LocalDateTime.now());
                            alertService.save(alert);
                        });

            } catch (Exception e) {
                System.err.println("❌ Error procesando alerta para " + alert.getCardName());
                e.printStackTrace();
            }
        }
    }

    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("€", "").replace(",", ".").trim());
    }




    // 🚨 METODO PARA HACER UNA PRUEBA DE ALERT
    public void triggerTestAlert() {
        System.out.println("🧪 Forzando alerta de prueba...");

        // Puedes personalizar estos valores
        Alert testAlert = new Alert();
        testAlert.setCardName("Black Lotus");
        testAlert.setSetName("Limited Edition Alpha");
        testAlert.setCountry("Spain");
        testAlert.setCondition("NM");
        testAlert.setMaxPrice(5000.0);
        testAlert.setLastTriggeredAt(null); // Para que no salte la restricción de 7 días

        // Simular que existe una coincidencia
        double price = 4999.99;
        AlertHistory history = new AlertHistory();
        history.setAlert(testAlert);
        history.setCardName(testAlert.getCardName());
        history.setSetName(testAlert.getSetName());
        history.setPrice(price);
        history.setMatchDate(LocalDateTime.now());
        historyRepository.save(history);

        // Enviar correo
        String subject = "🔔 [TEST] Alerta activada manualmente";
        String text = String.format("""
            Se ha activado una alerta de prueba manualmente:
            
            Carta: %s
            Set: %s
            Condición: %s
            País: %s
            Precio: %.2f €
            
            Fecha: %s
            """,
                testAlert.getCardName(),
                testAlert.getSetName(),
                testAlert.getCondition(),
                testAlert.getCountry(),
                price,
                LocalDateTime.now().toString()
        );

        emailService.sendAlertEmail("tu_correo@gmail.com", subject, text);

        System.out.println("✅ Alerta de prueba ejecutada correctamente.");
    }


}
