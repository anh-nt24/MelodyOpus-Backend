package org.anhnt24.melodyopus.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl;
        if (authentication.getPrincipal() instanceof OAuth2User || authentication.getPrincipal() != null) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String avatar = oAuth2User.getAttribute("picture");
            redirectUrl = "/api/auth/google/success?email=" + email + "&name=" + name + "&avatar=" + avatar;

        } else {
            redirectUrl = "/api/auth/google/failure?error=Account is not validated";
        }
        response.sendRedirect(redirectUrl);
    }
}
