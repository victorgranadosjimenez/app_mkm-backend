package app_mkm.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ScraperTest implements CommandLineRunner {

    private final CardmarketScraperService scraperService;

    public ScraperTest(CardmarketScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🧪 Iniciando test del scraper...");
        var cards = scraperService.scrapeCard("Urzas-Saga", "Planar Void");
        cards.forEach(System.out::println);
        System.out.println("✅ Test finalizado.");
    }
}
