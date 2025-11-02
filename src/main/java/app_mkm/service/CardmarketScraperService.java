package app_mkm.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardmarketScraperService {

    private static final String BASE_URL = "https://www.cardmarket.com/en/Magic/Products/Singles/";

    /**
     * Busca vendedores de una carta dada y devuelve los resultados básicos
     */
    public List<CardListing> scrapeCard(String setName, String cardName) throws IOException {
        String url = BASE_URL + setName.replace(" ", "-") + "/" + cardName.replace(" ", "-");
        System.out.println("Scraping URL: " + url);


        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/120.0.0.0 Safari/537.36")
                .timeout(10000)
                .get();


        List<CardListing> listings = new ArrayList<>();

        // Cada oferta de vendedor suele estar dentro de un <div class="article-row">
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

    /**
     * DTO interno con la información de cada vendedor
     */
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
