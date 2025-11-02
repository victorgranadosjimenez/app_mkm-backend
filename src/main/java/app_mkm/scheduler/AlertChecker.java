package app_mkm.scheduler;

import app_mkm.entity.Alert;
import app_mkm.service.AlertService;
import app_mkm.service.CardmarketScraperService;
import app_mkm.service.CardmarketScraperService.CardListing;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertChecker {

    private final AlertService alertService;
    private final CardmarketScraperService scraperService;

    public AlertChecker(AlertService alertService, CardmarketScraperService scraperService) {
        this.alertService = alertService;
        this.scraperService = scraperService;
    }

    // Ejecutar cada 15 minutos
    //@Scheduled(fixedRate = 15 * 60 * 1000)
    public void checkAlerts() {
        System.out.println("🔍 Checking alerts...");

        List<Alert> alerts = alertService.getAll();

        for (Alert alert : alerts) {
            try {
                List<CardListing> listings = scraperService.scrapeCard(alert.getSetName(), alert.getCardName());

                listings.stream()
                        .filter(l -> l.getCountry().equalsIgnoreCase(alert.getCountry()))
                        .filter(l -> parsePrice(l.getPrice()) <= alert.getMaxPrice())
                        .filter(l -> l.getCondition().toLowerCase().contains(alert.getCondition().toLowerCase()))
                        .findFirst()
                        .ifPresent(match -> {
                            System.out.printf("✅ Match found: %s - %s (%s) %s%n",
                                    alert.getCardName(),
                                    match.getSeller(),
                                    match.getCountry(),
                                    match.getPrice());
                            // TODO: enviar correo + guardar en historial
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("€", "").replace(",", ".").trim());
    }
}
