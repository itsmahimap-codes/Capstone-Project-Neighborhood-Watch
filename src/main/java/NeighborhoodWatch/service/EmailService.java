
package NeighborhoodWatch.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_SENDER_EMAIL}")
    private String senderEmail;

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtp(String email, String otp) {

        Map<String, Object> body = Map.of(
            "sender", Map.of(
                "name", "Neighborhood Watch",
                "email", senderEmail
            ),
            "to", List.of(
                Map.of("email", email)
            ),
            "subject", "Neighborhood Watch - Email Verification OTP",
            "textContent",
                "Hello,\n\n"
                + "Your Neighborhood Watch verification OTP is:\n\n"
                + otp
                + "\n\n"
                + "This OTP is valid for 5 minutes.\n\n"
                + "If you did not request this OTP, please ignore this email.\n\n"
                + "Regards,\n"
                + "Neighborhood Watch Team"
        );

        RestClient.create("https://api.brevo.com/v3")
            .post()
            .uri("/smtp/email")
            .header("api-key", apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }
}