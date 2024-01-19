package org.example.springbackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushRequestController {

    @GetMapping("/paymentRequest")
    public void paymentRequest() {
        System.out.println("Payment request received");
    }
}
