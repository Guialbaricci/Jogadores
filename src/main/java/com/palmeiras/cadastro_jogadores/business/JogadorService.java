package com.palmeiras.cadastro_jogadores.business;

import com.palmeiras.cadastro_jogadores.infrastructure.entities.Jogador;
import com.palmeiras.cadastro_jogadores.infrastructure.repository.JogadorRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JogadorService {

    private final JogadorRepository repository;

    public Jogador salvarJogador(Jogador jogador) {
        return repository.saveAndFlush(jogador);
    }

    public List<Jogador> listarTodosJogadores() {
        return repository.findAll();
    }

    public Jogador buscarJogadorPorNome(String nome) {
        return repository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Nome não encontrado!"));
    }

    public void deletarJogadorPorNome(String nome) {
        repository.deleteByNome(nome);
    }

    public Jogador atualizarJogadorPorId(Integer id, Jogador jogador) {

        Jogador jogadorExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado!"));

        if (jogador.getNome() != null) {
            jogadorExistente.setNome(jogador.getNome());
        }

        if (jogador.getIdade() != null) {
            jogadorExistente.setIdade(jogador.getIdade());
        }

        if (jogador.getNacionalidade() != null) {
            jogadorExistente.setNacionalidade(jogador.getNacionalidade());
        }

        if (jogador.getGols() != null) {
            jogadorExistente.setGols(jogador.getGols());
        }

        return repository.saveAndFlush(jogadorExistente);
    }
}