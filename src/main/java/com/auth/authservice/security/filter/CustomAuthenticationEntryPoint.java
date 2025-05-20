package com.auth.authservice.security.filter;

import java.io.IOException;
import java.time.Instant;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.auth.authservice.dto.ApiErrorDto;
import com.auth.authservice.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final MessageUtils messageUtils;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ApiErrorDto errorResponse = new ApiErrorDto(
                HttpServletResponse.SC_UNAUTHORIZED,
                this.messageUtils.getMessage("exception.auth.unauthorized"),
                Instant.now(),
                request.getRequestURI(),
                request.getMethod());

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}