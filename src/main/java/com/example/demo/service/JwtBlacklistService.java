package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtBlacklistService {
    private Set<String> blacklistedTokens = new HashSet<>();
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}