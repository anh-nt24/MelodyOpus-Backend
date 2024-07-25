package org.anhnt24.melodyopus.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.anhnt24.melodyopus.entity.User;
import org.anhnt24.melodyopus.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

public class UserUtil {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UserService userService;

    public User getUserFromRequest(HttpServletRequest request) throws ServiceException {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        if (token.isEmpty()) {
            throw new ServiceException("JWT requires");
        }
        String username = tokenManager.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new ServiceException("User not found");
        }
        return user;
    }
}
