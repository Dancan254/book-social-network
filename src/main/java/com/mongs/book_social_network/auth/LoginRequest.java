package com.mongs.book_social_network.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginRequest {
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotNull(message = "Password is mandatory")
    private String password;
}
