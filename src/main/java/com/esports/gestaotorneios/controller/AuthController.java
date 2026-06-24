package com.esports.gestaotorneios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        // Redireciona para o arquivo src/main/resources/templates/auth/login.html
        return "auth/login";
    }
}
