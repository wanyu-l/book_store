package com.book_store.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Hi this is home page";
    }
    @GetMapping("/home")
    public String hello() {
        return "Hi this is home";
    }
}
