package NeighborhoodWatch.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String generateOtp() {

        Random random = new Random();

        int otp = 100000 + random.nextInt(900000);

        return String.valueOf(otp);
    }

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Neighborhood Watch - Email Verification OTP");

        message.setText(
            "Hello,\n\n"
            + "Your Neighborhood Watch verification OTP is:\n\n"
            + otp
            + "\n\n"
            + "This OTP is valid for 5 minutes.\n\n"
            + "If you did not request this OTP, please ignore this email.\n\n"
            + "Regards,\n"
            + "Neighborhood Watch Team"
        );

        mailSender.send(message);
    }
}