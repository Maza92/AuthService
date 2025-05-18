package com.auth.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecoverPasswordDto {
    @NotBlank
    public String resetToken;

    @NotBlank(message = "{validation.password.new.required")
    @Size(min = 8, max = 255, message = "{validations.password.size}")
    public String newPassword;
}
