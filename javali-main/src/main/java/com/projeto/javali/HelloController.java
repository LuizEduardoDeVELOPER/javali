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

    // ORGANIZAÇÃO: Todas as injeções no topo da classe
    @Autowired
    private TendaRepository repository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
private GastoRepository gastoRepository; // ADICIONE ESTA LINHA AQUI

    @Autowired
    private GastoService gastoService; // Agora identificado corretamente no topo

    // 1. MÓDULO HOME (DASHBOARD)
    @GetMapping("/admin")
    public String admin(Model model) {
        // Dados das tendas
        model.addAttribute("listaDeTendas", repository.findAll());

        // Lógica de Vendas e Faturamento
        List<Venda> vendas = vendaRepository.findAll();
        Double totalFaturado = vendas.stream()
                .mapToDouble(Venda::getValorVenda)
                .sum();

        // Lógica de Gastos e Lucro (Usando o GastoService injetado acima)
        Double totalGastos = gastoService.calcularTotalGastos(); 
        Double lucroLiquido = totalFaturado - totalGastos;

        // Envio de atributos para o HTML
        model.addAttribute("vendas", vendas);
        model.addAttribute("totalFaturado", totalFaturado);
        model.addAttribute("totalGastos", totalGastos);
        model.addAttribute("lucroLiquido", lucroLiquido);
        model.addAttribute("totalVendas", (long) vendas.size());
        model.addAttribute("totalTendas", repository.count());

        return "Admin/dashboard"; // Ajustado para "Admin" com A maiúsculo
    }

    // 2. MÓDULO DE PRODUTOS
    @GetMapping("/admin/produtos")
    public String produtos(Model model) {
        model.addAttribute("listaDeTendas", repository.findAll());
        return "Admin/produtos";
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

    // 3. MÓDULO DE VENDAS
    @GetMapping("/admin/vendas")
    public String vendas(Model model) {
        model.addAttribute("vendas", vendaRepository.findAll());
        return "Admin/vendas";
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

    // 4. MÓDULO DE DESPESAS (FINANCEIRO)
    @GetMapping("/admin/despesas")
    public String despesas(Model model) {
        model.addAttribute("gastos", gastoService.listarTodos());
        model.addAttribute("totalGastos", gastoService.calcularTotalGastos());
        return "Admin/despesas";
    }

    // 5. MÓDULO DE RELATÓRIOS
   


    @PostMapping("/admin/despesas/salvar")
public String salvarDespesa(Gasto novoGasto) {
    // 1. Salva o gasto no banco de dados via Repository
    gastoRepository.save(novoGasto);
    
    // 2. Redireciona de volta para a tela de despesas para ver a lista atualizada
    return "redirect:/admin/despesas";
    
}

@GetMapping("/admin/relatorios")
public String relatorios(Model model) {
    // Puxa os totais para o dono da loja ver no relatório
    Double faturamento = vendaRepository.findAll().stream().mapToDouble(Venda::getValorVenda).sum();
    Double gastos = gastoService.calcularTotalGastos();

    model.addAttribute("totalFaturado", faturamento);
    model.addAttribute("totalGastos", gastos);
    model.addAttribute("lucroLiquido", faturamento - gastos);
    
    return "Admin/relatorios";
}





}