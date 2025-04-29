package com.example.MiniEvent.adapter.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
