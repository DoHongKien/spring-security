package com.kiendh.springsecurity.service;

import com.kiendh.springsecurity.dto.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse getCustomer(Long id);
}
