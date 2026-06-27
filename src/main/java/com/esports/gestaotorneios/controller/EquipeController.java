package com.esports.gestaotorneios.controller;

import com.esports.gestaotorneios.model.Equipe;
import com.esports.gestaotorneios.service.EquipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/equipes")
public class EquipeController {

    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("equipes", equipeService.findAll());
        return "equipes/listar";
    }

    @GetMapping("/nova")
    public String novaEquipe(Model model) {
        model.addAttribute("equipe", new Equipe());
        return "equipes/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Equipe equipe, RedirectAttributes attributes, Model model) {
        try {
            equipeService.save(equipe);
            attributes.addFlashAttribute("mensagemSucesso", "Equipe salva com sucesso!");
            return "redirect:/equipes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("equipe", equipe);
            return "equipes/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("equipe", equipeService.findById(id));
        return "equipes/form";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        equipeService.deleteById(id);
        attributes.addFlashAttribute("mensagemSucesso", "Equipe excluída com sucesso!");
        return "redirect:/equipes";
    }
}
