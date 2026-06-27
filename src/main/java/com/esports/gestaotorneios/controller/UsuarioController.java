package com.esports.gestaotorneios.controller;

import com.esports.gestaotorneios.model.UsuarioRole;
import com.esports.gestaotorneios.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ORGANIZADOR')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("roles", UsuarioRole.values());
        return "usuarios/listar";
    }

    @PostMapping("/alterar-role")
    public String alterarRole(@RequestParam Long usuarioId, @RequestParam UsuarioRole novaRole, RedirectAttributes attributes) {
        usuarioService.alterarRole(usuarioId, novaRole);
        attributes.addFlashAttribute("mensagemSucesso", "Permissão alterada com sucesso!");
        return "redirect:/usuarios";
    }
}
