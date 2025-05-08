package com.kiendh.springsecurity.controller;

import com.kiendh.springsecurity.service.CheckBitMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class CheckController {

    private final CheckBitMapService checkService;

    // Check if email exists
    @GetMapping("/email")
    public String checkEmail(@RequestParam String email) {
        boolean emailExists = checkService.isEmailExists(email);

        if (emailExists) {
            return "Email already exists";
        } else {
            checkService.addEmail(email);
            return "Email added successfully";
        }
    }
}
