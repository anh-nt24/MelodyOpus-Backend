package org.anhnt24.melodyopus.strategy;

import org.anhnt24.melodyopus.dto.AuthResponseDTO;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface LoginStrategy {
    AuthResponseDTO login(String credentials) throws GeneralSecurityException, IOException;
}