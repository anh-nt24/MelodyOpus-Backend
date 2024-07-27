package org.anhnt24.melodyopus.strategy;

import org.anhnt24.melodyopus.dto.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Component
public class LoginContext {
    private final Map<String, LoginStrategy> loginStrategyMap;

    @Autowired
    private LoginContext(Map<String, LoginStrategy> loginStrategyMap) {
        this.loginStrategyMap = loginStrategyMap;
    }

    public AuthResponseDTO login(String strategyType, String credentials) throws GeneralSecurityException, IOException {
        LoginStrategy strategy = loginStrategyMap.get(strategyType);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy not found");
        }

        return strategy.login(credentials);
    }
}
