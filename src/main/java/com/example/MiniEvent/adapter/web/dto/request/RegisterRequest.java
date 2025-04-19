package com.example.MiniEvent.adapter.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 6)
    private String password;

    @NotEmpty
    @Pattern(
            regexp = "^0\\d{9}$",
            message = "Must start with 0 and have exactly 10 digits."
    )
    private String phone;
}
