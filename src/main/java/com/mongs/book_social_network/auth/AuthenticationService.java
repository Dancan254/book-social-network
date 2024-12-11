package com.mongs.book_social_network.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongs.book_social_network.email.EmailService;
import com.mongs.book_social_network.email.EmailTemplateName;
import com.mongs.book_social_network.role.RoleRepository;
import com.mongs.book_social_network.user.Token;
import com.mongs.book_social_network.user.TokenRepository;
import com.mongs.book_social_network.user.User;
import com.mongs.book_social_network.user.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final String activationUrl;

    public AuthenticationService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            TokenRepository tokenRepository,
            EmailService emailService, RoleRepository roleRepository,
            @Value("${application.mailing.activationUrl}") String activationUrl) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.activationUrl = activationUrl;
    }

    public void register(RegistrationRequest request) throws MessagingException {
        var role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountLocked(false)
            .enabled(false)
            .roles(List.of(role))
            .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    public void sendValidationEmail(User user) throws MessagingException {
        
        var newToken = generateValidationToken(user);
        emailService.sendEmail(user.getEmail(), user.fullName(),EmailTemplateName.ACTIVATEACCOUNT, activationUrl, newToken, "Activate your account");

    }

    public String generateValidationToken(User user) {
        // generate token
        String generatedToken = generateActivationToken(6);
        var token = Token.builder()
            .token(generatedToken)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .user(user)
            .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    public String generateActivationToken(int length) {
        // generate token
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
