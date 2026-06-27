package com.esports.gestaotorneios.controller;

import com.esports.gestaotorneios.model.Torneio;
import com.esports.gestaotorneios.service.TorneioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/torneios")
public class TorneioController {

    private final TorneioService torneioService;

    public TorneioController(TorneioService torneioService) {
        this.torneioService = torneioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("torneios", torneioService.findAll());
        return "torneios/listar";
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @GetMapping("/novo")
    public String novoTorneio(Model model) {
        model.addAttribute("torneio", new Torneio());
        return "torneios/form";
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Torneio torneio, RedirectAttributes attributes, Model model) {
        try {
            torneioService.save(torneio);
            attributes.addFlashAttribute("mensagemSucesso", "Torneio salvo com sucesso!");
            return "redirect:/torneios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("torneio", torneio);
            return "torneios/form";
        }
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("torneio", torneioService.findById(id));
        return "torneios/form";
    }

    @PreAuthorize("hasRole('ORGANIZADOR')")
    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        torneioService.deleteById(id);
        attributes.addFlashAttribute("mensagemSucesso", "Torneio excluído com sucesso!");
        return "redirect:/torneios";
    }
}
