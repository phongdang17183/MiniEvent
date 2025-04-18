package com.example.MiniEvent.web.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 6)
    private String password;
}
