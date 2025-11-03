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

    // Ejecuta la comprobación de alertas cada hora
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void checkAlerts() {
        System.out.println("🔁 Ejecutando AlertChecker...");

        List<Alert> alerts = alertService.getAll();
        if (alerts.isEmpty()) {
            System.out.println("⚠️ No hay alertas registradas. Saltando ejecución.");
            return;
        }

        for (Alert alert : alerts) {
            try {
                // Saltar si la alerta ya saltó hace menos de 7 días
                if (alert.getLastTriggeredAt() != null &&
                        alert.getLastTriggeredAt().isAfter(LocalDateTime.now().minusDays(7))) {
                    continue;
                }

                List<CardListing> listings = scraperService.scrapeCard(alert.getSetName(), alert.getCardName());

                listings.stream()
                        .filter(l -> l.getCountry().equalsIgnoreCase(alert.getCountry()))
                        .filter(l -> parsePrice(l.getPrice()) <= alert.getMaxPrice())
                        .filter(l -> conditionMatches(l.getCondition(), alert.getCondition()))
                        .findFirst()
                        .ifPresent(match -> {
                            double price = parsePrice(match.getPrice());
                            System.out.printf("✅ Match encontrado: %s - %s (%s) %s%n",
                                    alert.getCardName(), match.getSeller(), match.getCountry(), match.getPrice());

                            // Guardar en historial
                            AlertHistory history = new AlertHistory();
                            history.setAlert(alert);
                            history.setCardName(alert.getCardName());
                            history.setSetName(alert.getSetName());
                            history.setPrice(price);
                            history.setMatchDate(LocalDateTime.now());

                            // 👇 Guardamos snapshot
                            history.setAlertCardNameSnapshot(alert.getCardName());
                            history.setAlertSetNameSnapshot(alert.getSetName());
                            history.setAlertConditionSnapshot(alert.getCondition());
                            history.setAlertCountrySnapshot(alert.getCountry());
                            history.setAlertMaxPriceSnapshot(alert.getMaxPrice());

                            // 👇 ahora sí guardamos
                            historyRepository.save(history);



                            // Enviar correo
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

                            emailService.sendAlertEmail("victorarsen@gmail.com", subject, text);

                            // Actualizar fecha de última activación
                            alert.setLastTriggeredAt(LocalDateTime.now());
                            alertService.save(alert);
                        });

            } catch (Exception e) {
                System.err.println("❌ Error procesando alerta para " + alert.getCardName());
                e.printStackTrace();
            }
        }
    }

    //METODO AUXILIAR
    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("€", "").replace(",", ".").trim());
    }


    //METODO AUXILIAR
    private boolean conditionMatches(String scrapedCondition, String alertCondition) {
        String c = scrapedCondition.trim().toLowerCase();
        String a = alertCondition.trim().toLowerCase();

        // Diccionario básico de equivalencias
        return switch (a) {
            case "mint", "m" -> c.contains("m");
            case "near mint", "nm" -> c.contains("nm");
            case "excellent", "ex" -> c.contains("ex");
            case "good", "gd" -> c.contains("gd");
            case "light played", "lp" -> c.contains("lp");
            case "played", "pl" -> c.contains("pl");
            case "poor", "po" -> c.contains("po");
            default -> c.contains(a);
        };
    }




}
