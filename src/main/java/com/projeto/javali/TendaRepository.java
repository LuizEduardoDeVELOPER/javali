package com.projeto.javali;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TendaRepository extends JpaRepository<Tenda, Long> {
}