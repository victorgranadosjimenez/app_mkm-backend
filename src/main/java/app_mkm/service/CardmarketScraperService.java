package app_mkm.service;

import org.jsoup.Connection;
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

    @Value("${SCRAPER_API_KEY:demo}")
    //@Value("${SCRAPER_API_KEY}")
    private String scraperApiKey;

    private static final String BASE_URL = "https://www.cardmarket.com/en/Magic/Products/Singles/";

    public List<CardListing> scrapeCard(String setName, String cardName) throws IOException {
        String targetUrl = BASE_URL
                + setName.trim().replace(" ", "-")
                + "/"
                + cardName.trim()
                .replace(" ", "-")
                .replace("’", "")
                .replace("'", "")
                .replace(",", "")
                .replace("–", "-");

        // Usar ScraperAPI (para evitar bloqueos de Cloudflare)
        String proxyUrl = "https://api.scraperapi.com?api_key=" + scraperApiKey + "&url=" + targetUrl;

        System.out.println("🌐 Scraping URL via ScraperAPI: " + proxyUrl);

        Connection.Response response = Jsoup.connect(proxyUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118 Safari/537.36")
                .timeout(60000)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .execute();

        System.out.println("HTTP status: " + response.statusCode());

        Document doc = response.parse();
        System.out.println("📄 Page title: " + doc.title());

        // Seleccionamos las filas de las ofertas
        Elements offers = doc.select("div.table-body div.article-row");
        System.out.println("🔍 Ofertas encontradas: " + offers.size());


        if (!offers.isEmpty()) {
            System.out.println("🧾 HTML del primer artículo:");
            System.out.println(offers.first().outerHtml());
        }


        List<CardListing> listings = new ArrayList<>();

        for (Element offer : offers) {
            try {
                // vendedor
                String seller = offer.select("div.col-seller a").text();

                // país
                Element location = offer.selectFirst("div.col-seller span[title*='Item location:']");
                String country = (location != null)
                        ? location.attr("title").replace("Item location:", "").trim()
                        : "Unknown";

                // condición
                String condition = offer.select("div.product-attributes a.article-condition span.badge").text();

                // precio
                String price = offer.select("div.price-container span.color-primary").text();

                if (seller.isEmpty() || price.isEmpty()) continue;

                listings.add(new CardListing(seller, country, condition, price));
            } catch (Exception e) {
                System.err.println("⚠️ Error procesando una oferta: " + e.getMessage());
            }
        }


        if (listings.isEmpty()) {
            System.out.println("❌ No se encontraron listings válidos. Verifica el set o nombre de carta.");
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
