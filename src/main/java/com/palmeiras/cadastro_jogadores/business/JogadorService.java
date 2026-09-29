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

    public void deletarJogadorPorNome(String nome){
        repository.deleteByName(nome);
    }

    public void atualizarJogadorPorId(Integer id, Jogador jogador){
        Jogador jogadorEntity = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Jogador não encontrado"));
        Jogador jogadorAtualizado = Jogador.builder()
                .nome(jogador.getNome() != null ? jogador.getNome() :
                        jogadorEntity.getNome())
                .idade(jogador.getIdade() != null ? jogador.getIdade() :
                        jogadorEntity.getIdade())
                .nacionalidade(jogador.getNacionalidade() != null ? jogador.getNacionalidade() :
                        jogadorEntity.getNacionalidade())
                .gols(jogador.getGols() != null ? jogador.getGols() :
                        jogadorEntity.getGols())
                .id(jogadorEntity.getId())
                .build();
    }
}
