package com.projeto.javali;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.ui.Model; // Serve para levar dados para o HTML
@Controller
public class HelloController {

    @Autowired
    private TendaRepository repository; // Isso conecta o mensageiro ao cérebro

   @GetMapping("/")
public String index(Model model) {
    // repository.findAll() vai no MySQL e pega tudo o que tem lá
    model.addAttribute("listaDeTendas", repository.findAll());
    return "index";
}

    @PostMapping("/cadastrar")
    public String cadastrar(Tenda novaTenda) {
        // Pega os dados do formulário e salva no banco de dados real
        repository.save(novaTenda);
        
        // Depois de salvar, recarrega a página para você ver o formulário limpo
        return "redirect:/";
    }
}