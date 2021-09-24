package com.mli.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author Haripal.Chauhan
 * Authenticating JWT token if it is expired or invalid or wrong API call.
 */
@Component
public class MliAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -4880092541013831273L;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
