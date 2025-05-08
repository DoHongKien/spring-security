package com.kiendh.springsecurity.service;

public interface IdempotentService {

    boolean markExecuted(String key);
}
