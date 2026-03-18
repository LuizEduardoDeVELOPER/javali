package com.projeto.javali;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dados do Produto
    private String nomeTenda;
    private Double valorVenda;
    private LocalDateTime dataHora;

    // Dados do Comprador (Novos Parâmetros)
    private String nomeComprador;
    private String cpfComprador;
    private String telefoneComprador;

    // Controle Interno
    private String nomeVendedor;

    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeTenda() { return nomeTenda; }
    public void setNomeTenda(String nomeTenda) { this.nomeTenda = nomeTenda; }

    public Double getValorVenda() { return valorVenda; }
    public void setValorVenda(Double valorVenda) { this.valorVenda = valorVenda; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getNomeComprador() { return nomeComprador; }
    public void setNomeComprador(String nomeComprador) { this.nomeComprador = nomeComprador; }

    public String getCpfComprador() { return cpfComprador; }
    public void setCpfComprador(String cpfComprador) { this.cpfComprador = cpfComprador; }

    public String getTelefoneComprador() { return telefoneComprador; }
    public void setTelefoneComprador(String telefoneComprador) { this.telefoneComprador = telefoneComprador; }

    public String getNomeVendedor() { return nomeVendedor; }
    public void setNomeVendedor(String nomeVendedor) { this.nomeVendedor = nomeVendedor; }
}