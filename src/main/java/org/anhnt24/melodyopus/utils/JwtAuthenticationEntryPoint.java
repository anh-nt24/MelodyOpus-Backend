package org.anhnt24.melodyopus.utils;

import java.io.IOException;
import java.io.Serializable;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// this component handles cases where authentication fails.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // send an error response with status 401 (Unauthorized).
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }
}