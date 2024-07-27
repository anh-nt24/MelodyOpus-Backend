package org.anhnt24.melodyopus.config;

import org.anhnt24.melodyopus.strategy.GoogleLoginStrategy;
import org.anhnt24.melodyopus.strategy.LoginStrategy;
import org.anhnt24.melodyopus.strategy.UsernameLoginStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class LoginStrategyConfig {

    @Bean
    public Map<String, LoginStrategy> loginStrategies(
            UsernameLoginStrategy usernameLoginStrategy,
            GoogleLoginStrategy googleLoginStrategy
    ) {
        return Map.of(
                "usernameLoginStrategy", usernameLoginStrategy,
                "googleLoginStrategy", googleLoginStrategy
        );
    }
}
