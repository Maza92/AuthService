package com.auth.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordByAdminDto {
    @NotBlank(message = "{validation.password.new.required}")
    @Size(min = 8, message = "{validation.password.size}")
    public String newPassword;
}
