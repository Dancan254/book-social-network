package com.mongs.book_social_network.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongs.book_social_network.email.EmailService;
import com.mongs.book_social_network.email.EmailTemplateName;
import com.mongs.book_social_network.exceptions.TokenNotFoundException;
import com.mongs.book_social_network.role.RoleRepository;
import com.mongs.book_social_network.security.JwtService;
import com.mongs.book_social_network.user.Token;
import com.mongs.book_social_network.user.TokenRepository;
import com.mongs.book_social_network.user.User;
import com.mongs.book_social_network.user.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    @Value("${application.mailing.activationUrl}")
    private final String activationUrl;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


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

    public AuthenticationResponse login(LoginRequest request) {
        var auth = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullname", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        //get the token
        Token savedToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Token not found"));
        //check if the token is expired
        if (savedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            //send another token
            User user = savedToken.getUser();
            sendValidationEmail(user);
            String userEmail = user.getEmail();
            throw new RuntimeException("Activation token expired. A new token was sent to " + userEmail + " email adress.");
        }

        //fetch and enable the user
        User user = userRepository.findById(savedToken.getUser().getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        //update the token
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        
    }
}
