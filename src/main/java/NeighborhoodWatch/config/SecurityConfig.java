package NeighborhoodWatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Allow authentication endpoints
                .requestMatchers("/users/**").permitAll()

                // Allow static pages
                .requestMatchers(
    "/",
    "/index.html",
    "/login.html",
    "/register.html",
    "/verify-otp.html",
    "/incidents.html",
    "/report.html",
    "/css/**",
    "/js/**"
).permitAll()

                // ADMIN ONLY
                .requestMatchers(
                    HttpMethod.PUT,
                    "/incidents/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/incidents/**"
                ).hasRole("ADMIN")

                // Logged-in users
                .requestMatchers(
                    HttpMethod.POST,
                    "/incidents/report"
                ).authenticated()

                .requestMatchers(
                    HttpMethod.GET,
                    "/incidents"
                ).authenticated()

                .anyRequest().authenticated()
            )

            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}