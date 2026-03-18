package com.projeto.javali;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HelloController {

    @Autowired
    private TendaRepository repository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private GastoService gastoService;

    // --- MÓDULO DE LOGIN E ACESSO ---
    @GetMapping("/login")
    public String login() { return "login"; }

    @PostMapping("/logar")
    public String logar(String usuario, String senha, HttpSession session, Model model) {
        if ("admin".equals(usuario) && "123".equals(senha)) {
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/admin"; 
        }
        model.addAttribute("erro", "Usuário ou senha inválidos!");
        return "login"; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- MÓDULO CLIENTE (INTEGRAÇÃO REAL) ---
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("produtos", repository.findAll());
        return "index";
    }

    @PostMapping("/api/vendas/finalizar")
    public String finalizarVendaCliente(
            @RequestParam String nomeTenda,
            @RequestParam Double valorVenda,
            @RequestParam String nomeComprador,
            @RequestParam String cpfComprador,
            @RequestParam String telefoneComprador) {

        // 1. Registra a venda com os parâmetros completos solicitados
        Venda novaVenda = new Venda();
        novaVenda.setNomeTenda(nomeTenda);
        novaVenda.setValorVenda(valorVenda);
        novaVenda.setNomeComprador(nomeComprador);
        novaVenda.setCpfComprador(cpfComprador);
        novaVenda.setTelefoneComprador(telefoneComprador);
        novaVenda.setDataHora(LocalDateTime.now());
        novaVenda.setNomeVendedor("Site Online"); // Identifica a origem

        vendaRepository.save(novaVenda);

        // 2. Busca o produto para baixar estoque
        List<Tenda> tendas = repository.findAll();
        for (Tenda t : tendas) {
            if (t.getNome().equals(nomeTenda) && t.getQuantidade() > 0) {
                t.setQuantidade(t.getQuantidade() - 1);
                repository.save(t);
                break;
            }
        }
        return "redirect:/?sucesso=true";
    }

    // --- MÓDULO ADMIN (DASHBOARD) ---
    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        List<Venda> vendas = vendaRepository.findAll();
        Double totalFaturado = vendas.stream().mapToDouble(Venda::getValorVenda).sum();
        Double totalGastos = gastoService.calcularTotalGastos(); 
        
        model.addAttribute("listaDeTendas", repository.findAll());
        model.addAttribute("vendas", vendas);
        model.addAttribute("totalFaturado", totalFaturado);
        model.addAttribute("totalGastos", totalGastos);
        model.addAttribute("lucroLiquido", totalFaturado - totalGastos);
        model.addAttribute("totalVendas", (long) vendas.size());
        model.addAttribute("totalTendas", repository.count());

        return "Admin/dashboard";
    }

    // --- MÓDULO DE PRODUTOS ---
    @GetMapping("/admin/produtos")
    public String produtos(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";
        model.addAttribute("listaDeTendas", repository.findAll());
        return "Admin/produtos";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Tenda novaTenda) {
        repository.save(novaTenda);
        return "redirect:/admin/produtos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin/produtos";
    }

    // --- MÓDULO DE VENDAS ---
    @GetMapping("/admin/vendas")
    public String vendas(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";
        model.addAttribute("vendas", vendaRepository.findAll());
        return "Admin/vendas";
    }

    // --- RELATÓRIOS E EXCEL ---
    @GetMapping("/admin/relatorios")
    public String relatorios(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";
        Double faturamento = vendaRepository.findAll().stream().mapToDouble(Venda::getValorVenda).sum();
        Double gastos = gastoService.calcularTotalGastos();
        model.addAttribute("totalFaturado", faturamento);
        model.addAttribute("totalGastos", gastos);
        model.addAttribute("lucroLiquido", faturamento - gastos);
        return "Admin/relatorios";
    }

    @GetMapping("/admin/relatorios/excel")
    public void baixarExcel(HttpServletResponse response, HttpSession session) throws IOException {
        if (session.getAttribute("usuarioLogado") == null) return;
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_vendas.csv");
        List<Venda> vendas = vendaRepository.findAll();
        PrintWriter writer = response.getWriter();
        writer.println("ID;Produto;Valor;Comprador;CPF;Data");
        for (Venda v : vendas) {
            writer.println(v.getId() + ";" + v.getNomeTenda() + ";" + v.getValorVenda() + ";" + v.getNomeComprador() + ";" + v.getCpfComprador() + ";" + v.getDataHora());
        }
        writer.flush();
        writer.close();
    }
}