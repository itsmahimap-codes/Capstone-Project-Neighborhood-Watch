package NeighborhoodWatch.controller;

import NeighborhoodWatch.dto.LoginResponse;
import NeighborhoodWatch.dto.RegistrationResponse;
import NeighborhoodWatch.entity.User;
import NeighborhoodWatch.service.EmailService;
import NeighborhoodWatch.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import NeighborhoodWatch.dto.RegistrationResponse;
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();


    @PostMapping("/register")
public RegistrationResponse registerUser(@RequestBody User user) {

    User savedUser = userService.registerUser(user);

    return new RegistrationResponse(
            "Registration successful. OTP sent to your email.",
            savedUser.getEmail()
    );
}


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody User user,
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                    )
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                request,
                response
        );

        User loggedInUser =
                userService.loginUser(
                        user.getEmail(),
                        user.getPassword()
                );

        return new LoginResponse(
                loggedInUser.getUserId(),
                loggedInUser.getFullName(),
                loggedInUser.getEmail(),
                loggedInUser.getPhoneNumber(),
                loggedInUser.getRole()
        );
    }


    @PostMapping("/test-email")
    public String testEmail(@RequestParam String email) {

        String otp = emailService.generateOtp();

        emailService.sendOtp(email, otp);

        return "OTP sent successfully to " + email;
    }


    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        boolean verified =
                userService.verifyOtp(email, otp);

        if (verified) {
            return "Email verified successfully";
        }

        return "Invalid or expired OTP";
    }
}