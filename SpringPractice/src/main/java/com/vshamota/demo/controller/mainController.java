package com.vshamota.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {
    @GetMapping("/mainpage")
    public String getMainPage() {
        return "user/main";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "user/login";
    }

    @GetMapping("main/aboutUs")
    public String getAboutUsPage() {
        return "user/aboutUs";
    }
}
