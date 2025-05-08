package com.kiendh.springsecurity.service;

import com.kiendh.springsecurity.dto.response.AlertDto;

public interface TelegramAlertService {

    void sendMessage(AlertDto alert);

    void reloadReceivers();
}
