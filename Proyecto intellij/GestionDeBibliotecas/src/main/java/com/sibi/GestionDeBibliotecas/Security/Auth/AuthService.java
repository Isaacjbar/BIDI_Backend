package com.sibi.GestionDeBibliotecas.Security.Auth;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final Set<String> invalidTokens = Collections.synchronizedSet(new HashSet<>());

    public void invalidateToken(String token) {
        invalidTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidTokens.contains(token);
    }
}