package app_mkm.controller;

import app_mkm.service.CardmarketScraperService;
import app_mkm.service.CardmarketScraperService.CardListing;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/scraper")
public class ScraperController {

    private final CardmarketScraperService scraperService;

    public ScraperController(CardmarketScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/test")
    public List<CardListing> testScrape(
            @RequestParam String set,
            @RequestParam String card) throws IOException {
        return scraperService.scrapeCard(set, card);
    }
}
