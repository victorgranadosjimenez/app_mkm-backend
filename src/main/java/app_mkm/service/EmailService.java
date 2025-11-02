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

    public void sendAlertEmail(String to, String subject, String content) {
        try {
            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 💡 Usamos remitente genérico de Brevo
            Map<String, Object> body = Map.of(
                    "sender", Map.of("email", "no-reply@brevo.com", "name", "MKM Alerts"),
                    "to", new Map[]{ Map.of("email", to) },
                    "subject", subject,
                    "htmlContent", "<p>" + content.replace("\n", "<br>") + "</p>"
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = rest.postForEntity(
                    "https://api.brevo.com/v3/smtp/email", request, String.class
            );

            System.out.println("📧 Email enviado correctamente: " + response.getStatusCode());

        } catch (HttpClientErrorException e) {
            System.err.println("❌ Error enviando email: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("❌ Error inesperado enviando email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
