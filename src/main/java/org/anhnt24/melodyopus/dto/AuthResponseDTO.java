package org.anhnt24.melodyopus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.anhnt24.melodyopus.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private User user;
    private String jwt;
}
