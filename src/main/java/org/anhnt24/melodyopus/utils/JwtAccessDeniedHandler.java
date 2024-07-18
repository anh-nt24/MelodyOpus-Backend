package org.anhnt24.melodyopus.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

// this component handles cases where
// access is denied due to insufficient permissions.
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    // convert objects to JSON.
    private final ObjectMapper objectMapper;

    // constructor to initialize the objectMapper
    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // create a response header
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType("application/json");

        // create a response body
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Route not found");

        // write the response body to the response
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(responseBody));
        writer.flush();
    }
}