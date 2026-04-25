package com.example.profile.service.service;

public interface SecurityService {

    boolean verifyPassword(String raw, String hashed);
}
