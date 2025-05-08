package com.kiendh.springsecurity.service;

public interface CheckBitMapService {

    boolean isEmailExists(String email);

    void addEmail(String email);

    boolean isUsernameExists(String username);

    void addUsername(String username);
}
