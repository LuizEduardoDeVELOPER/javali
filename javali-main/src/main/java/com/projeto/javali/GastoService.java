package com.projeto.javali;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    // Retorna a lista de todos os gastos para o módulo de despesas
    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }

    // Soma todos os valores de gastos para o card do dashboard
    public Double calcularTotalGastos() {
        return gastoRepository.findAll().stream()
                .mapToDouble(Gasto::getValor)
                .sum();
    }
}