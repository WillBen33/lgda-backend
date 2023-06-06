package com.lgda.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("error", authException.getMessage());

        Map<String, String> error = new HashMap<>();

        if (request.getAttribute("expired_exception") != null) {
            error.put("is_token_expired", "true");
            error.put("error_message", request.getAttribute("expired_exception").toString());
        }
        if (request.getAttribute("malformed_exception") != null) {
            error.put("is_jwt_malformed", "true");
            error.put("error_message", request.getAttribute("malformed_exception").toString());
        }
        if (request.getAttribute("jwt_exception") != null) {
            error.put("is_jwt_exception", "true");
            error.put("error_message", request.getAttribute("jwt_exception").toString());
        }

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
