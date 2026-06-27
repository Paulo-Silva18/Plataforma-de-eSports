package com.esports.gestaotorneios.controller;

import com.esports.gestaotorneios.model.Jogador;
import com.esports.gestaotorneios.service.EquipeService;
import com.esports.gestaotorneios.service.JogadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jogadores")
public class JogadorController {

    private final JogadorService jogadorService;
    private final EquipeService equipeService;

    public JogadorController(JogadorService jogadorService, EquipeService equipeService) {
        this.jogadorService = jogadorService;
        this.equipeService = equipeService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("jogadores", jogadorService.findAll());
        return "jogadores/listar";
    }

    @GetMapping("/novo")
    public String novoJogador(Model model) {
        model.addAttribute("jogador", new Jogador());
        model.addAttribute("equipes", equipeService.findAll());
        return "jogadores/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Jogador jogador, RedirectAttributes attributes, Model model) {
        try {
            jogadorService.save(jogador);
            attributes.addFlashAttribute("mensagemSucesso", "Jogador salvo com sucesso!");
            return "redirect:/jogadores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("jogador", jogador);
            model.addAttribute("equipes", equipeService.findAll());
            return "jogadores/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("jogador", jogadorService.findById(id));
        model.addAttribute("equipes", equipeService.findAll());
        return "jogadores/form";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        jogadorService.deleteById(id);
        attributes.addFlashAttribute("mensagemSucesso", "Jogador excluído com sucesso!");
        return "redirect:/jogadores";
    }
}
