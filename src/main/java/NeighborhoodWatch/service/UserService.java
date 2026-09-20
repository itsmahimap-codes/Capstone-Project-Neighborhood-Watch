package NeighborhoodWatch.service;

import java.time.LocalDateTime;
import java.util.Optional;

import NeighborhoodWatch.entity.User;
import NeighborhoodWatch.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    public User registerUser(User user) {

        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        String otp = emailService.generateOtp();

        user.setOtp(otp);

        user.setOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        emailService.sendOtp(
                savedUser.getEmail(),
                otp
        );

        return savedUser;
    }


    public boolean verifyOtp(String email, String otp) {

        Optional<User> optionalUser =
                userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        if (user.getOtp() == null ||
            user.getOtpExpiry() == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return false;
        }

        if (!user.getOtp().equals(otp)) {
            return false;
        }

        user.setEmailVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return true;
    }


    public User loginUser(String email, String password) {

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return null;
        }

        User existingUser = user.get();

        if (!existingUser.isEmailVerified()) {
            throw new RuntimeException(
                    "Please verify your email first"
            );
        }

        if (passwordEncoder.matches(
                password,
                existingUser.getPassword())) {

            return existingUser;
        }

        return null;
    }
}