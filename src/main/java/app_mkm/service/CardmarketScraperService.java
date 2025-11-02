package app_mkm.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardmarketScraperService {

    @Value("${SCRAPER_API_KEY}")
    private String scraperApiKey;

    private static final String BASE_URL = "https://www.cardmarket.com/en/Magic/Products/Singles/";

    public List<CardListing> scrapeCard(String setName, String cardName) throws IOException {
        String targetUrl = BASE_URL + setName.replace(" ", "-") + "/" + cardName.replace(" ", "-");

        // 🔄 Pasamos la URL por ScraperAPI
        String proxyUrl = "https://api.scraperapi.com?api_key=" + scraperApiKey + "&render=true&url=" + targetUrl;

        System.out.println("Scraping URL via ScraperAPI: " + proxyUrl);


        Document doc = Jsoup.connect(proxyUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .timeout(20000) // 20 segundos
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .get();



        List<CardListing> listings = new ArrayList<>();
        Elements offers = doc.select("div.article-row");

        for (Element offer : offers) {
            try {
                String seller = offer.select("a.seller-name").text();
                String country = offer.select("span.flag-icon").attr("title");
                String condition = offer.select("div.product-attributes span.condition").text();
                String price = offer.select("div.price-container span.font-weight-bold").text();

                if (seller.isEmpty()) continue;

                listings.add(new CardListing(seller, country, condition, price));
            } catch (Exception ignored) {}
        }

        return listings;
    }

    public static class CardListing {
        private String seller;
        private String country;
        private String condition;
        private String price;

        public CardListing(String seller, String country, String condition, String price) {
            this.seller = seller;
            this.country = country;
            this.condition = condition;
            this.price = price;
        }

        public String getSeller() { return seller; }
        public String getCountry() { return country; }
        public String getCondition() { return condition; }
        public String getPrice() { return price; }

        @Override
        public String toString() {
            return seller + " (" + country + ") - " + condition + " - " + price;
        }
    }
}
