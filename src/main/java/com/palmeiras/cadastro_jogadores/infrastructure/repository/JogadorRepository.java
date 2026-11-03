package com.palmeiras.cadastro_jogadores.infrastructure.repository;

import com.palmeiras.cadastro_jogadores.infrastructure.entities.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Integer> {

    Optional<Jogador> findByNome(String nome);

    @Transactional
    void deleteByNome(String nome);
}
