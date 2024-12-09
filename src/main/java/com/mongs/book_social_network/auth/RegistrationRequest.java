package com.mongs.book_social_network.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "firstname is required")
    @NotBlank(message = "firstname is required")
    private String firstname;
    @NotEmpty(message = "lastname is required")
    @NotBlank(message = "lastname is required")
    private String lastname;
    @NotEmpty(message = "email is required")
    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;
    @NotEmpty(message = "password is required")
    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;
}
