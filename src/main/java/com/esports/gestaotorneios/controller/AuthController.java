package com.esports.gestaotorneios.controller;

import com.esports.gestaotorneios.model.Usuario;
import com.esports.gestaotorneios.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/salvar-usuario")
    public String salvarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes attributes, Model model) {
        try {
            usuarioService.salvarNovoUsuario(usuario);
            attributes.addFlashAttribute("mensagemSucesso", "Conta criada com sucesso! Faça login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "auth/registro";
        }
    }
}
