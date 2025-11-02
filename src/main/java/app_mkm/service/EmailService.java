package app_mkm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

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

            Map<String, Object> body = Map.of(
                    "sender", Map.of("email", "tu_correo@tudominio.com", "name", "MKM Alerts"),
                    "to", new Map[]{ Map.of("email", to) },
                    "subject", subject,
                    "htmlContent", "<p>" + content.replace("\n", "<br>") + "</p>"
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = rest.postForEntity(
                    "https://api.brevo.com/v3/smtp/email", entity, String.class
            );

            System.out.println("📧 Email enviado correctamente: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("❌ Error enviando email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
