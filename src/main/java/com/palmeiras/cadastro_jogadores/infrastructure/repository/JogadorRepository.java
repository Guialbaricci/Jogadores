package com.palmeiras.cadastro_jogadores.infrastructure.repository;

import com.palmeiras.cadastro_jogadores.infrastructure.entities.Jogador;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JogadorRepository extends JpaRepository<Jogador,Integer> {

    Optional<Jogador> findByNome(String nome);

    @Transactional
    void deleteByName(String name);
}
