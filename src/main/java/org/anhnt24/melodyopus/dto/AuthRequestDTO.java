package org.anhnt24.melodyopus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {
    private String username;
    private String password;
    private String accessToken;
}
