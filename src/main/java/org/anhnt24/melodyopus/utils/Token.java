package org.anhnt24.melodyopus.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

// this class represents the JWT token itself
// and provides a getter method to retrieve the token value.
@AllArgsConstructor
@Getter
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String token;
}
