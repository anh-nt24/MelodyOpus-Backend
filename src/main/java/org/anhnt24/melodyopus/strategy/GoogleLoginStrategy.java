package org.anhnt24.melodyopus.strategy;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import org.anhnt24.melodyopus.dto.AuthResponseDTO;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.UserService;
import org.anhnt24.melodyopus.utils.TokenManager;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service("googleLoginStrategy")
public class GoogleLoginStrategy implements LoginStrategy{
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @Override
    public AuthResponseDTO login(String credentials) throws GeneralSecurityException, IOException {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();
            GoogleIdToken idToken;
            idToken = verifier.verify(credentials);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String avatar = (String) payload.get("picture");

                User user = userService.findOrCreateUser(email, name, avatar);
                String jwtToken = tokenManager.generateJwtToken(user);
                return new AuthResponseDTO(user, jwtToken);
            } else {
                throw new GeneralSecurityException("Invalid Google token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Google token verification failed");
        }
    }
}
