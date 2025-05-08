package com.kiendh.springsecurity.service;

import com.kiendh.springsecurity.dto.response.CustomerResponse;
import com.kiendh.springsecurity.entity.Customer;
import com.kiendh.springsecurity.repository.CustomerRepository;
import com.kiendh.springsecurity.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void getCustomerById() {
        Customer customer = customerRepository.findById(1L).orElse(null);

//        assertNotNull(customer);
        assertEquals(1L, customer.getId());

        verify(customerRepository, times(1)).findById(1L);
    }
}
