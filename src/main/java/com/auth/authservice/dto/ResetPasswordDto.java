package com.auth.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    @NotBlank(message = "{validation.password.old.required}")
    public String oldPassword;

    @NotBlank(message = "{validation.password.new.required}")
    @Size(min = 8, message = "{validation.password.size}")
    public String newPassword;
}
