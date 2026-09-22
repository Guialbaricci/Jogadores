package com.palmeiras.cadastro_jogadores.business;

import com.palmeiras.cadastro_jogadores.infrastructure.entities.Jogador;
import com.palmeiras.cadastro_jogadores.infrastructure.repository.JogadorRepository;
import org.springframework.stereotype.Service;

@Service
public class JogadorService {

    private final JogadorRepository repository;

    public JogadorService(JogadorRepository repository) {
        this.repository = repository;
    }

    public void salvarJogador(Jogador jogador){
        repository.saveAndFlush(jogador);
    }

    public Jogador buscarJogadorPorNome(String nome){
        return repository.findByNome(nome).orElseThrow(
                () -> new RuntimeException("Nome não encontrado!")
        );
    }
}
