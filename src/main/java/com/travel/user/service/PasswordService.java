package com.travel.user.service;

public interface PasswordService {
    String hash(String raw);
    boolean matches(String raw, String hashed);
}

