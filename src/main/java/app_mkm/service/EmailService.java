package app_mkm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    private static final String API_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendAlertEmail(String to, String subject, String content) {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "sender", Map.of("email", "victorarsen@gmail.com", "name", "MKM Alerts"),
                "to", java.util.List.of(Map.of("email", to)),
                "subject", subject,
                "htmlContent", "<p>" + content.replace("\n", "<br>") + "</p>"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = rest.postForEntity(API_URL, entity, String.class);
            System.out.println("📧 Email enviado correctamente: " + response.getStatusCode());
            System.out.println("📨 Respuesta de Brevo: " + response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("❌ Error enviando email: " + e.getStatusCode());
            System.err.println("📨 Respuesta de Brevo: " + e.getResponseBodyAsString());

            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                System.err.println("⚠️ Revisa tu API key de Brevo. Parece no estar configurada correctamente.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error inesperado enviando email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
