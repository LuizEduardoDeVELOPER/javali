package com.projeto.javali;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HelloController {

    @Autowired
    private TendaRepository repository;

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping("/admin")
public String admin(Model model) {

    model.addAttribute("listaDeTendas", repository.findAll());

    List<Venda> vendas = vendaRepository.findAll();

    Double totalFaturado = vendas.stream()
            .mapToDouble(Venda::getValorVenda)
            .sum();

    Long totalVendas = vendaRepository.count();
    Long totalTendas = repository.count();

    model.addAttribute("vendas", vendas);
    model.addAttribute("totalFaturado", totalFaturado);
    model.addAttribute("totalVendas", totalVendas);
    model.addAttribute("totalTendas", totalTendas);

    return "admin/dashboard";
}

    @PostMapping("/cadastrar")
    public String cadastrar(Tenda novaTenda) {
        repository.save(novaTenda);
        return "redirect:/admin";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/vender/{id}")
    public String vender(@PathVariable Long id) {
        Tenda tenda = repository.findById(id).orElse(null);
        if (tenda != null && tenda.getQuantidade() > 0) {
            tenda.setQuantidade(tenda.getQuantidade() - 1);
            repository.save(tenda);

            Venda novaVenda = new Venda();
            novaVenda.setNomeTenda(tenda.getNome());
            novaVenda.setValorVenda(tenda.getPreco());
            novaVenda.setDataHora(LocalDateTime.now());
            vendaRepository.save(novaVenda);
        }
        return "redirect:/admin";
    }

@GetMapping("/admin/produtos")
public String produtos(Model model){
    model.addAttribute("listaDeTendas", repository.findAll());
    return "admin/produtos";
}


@GetMapping("/admin/vendas")
public String vendas(Model model){
    model.addAttribute("vendas", vendaRepository.findAll());
    return "admin/vendas";
}



@GetMapping("/admin/relatorios")
public String relatorios(){
    return "admin/relatorios";
}


@Autowired
private GastoService gastoService; // Agora o Java vai reconhecer este serviço!

@GetMapping("/admin/despesas")
public String despesas(Model model) {
    model.addAttribute("gastos", gastoService.listarTodos());
    model.addAttribute("totalGastos", gastoService.calcularTotalGastos());
    return "admin/despesas";
}

}