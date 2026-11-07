package com.palmeiras.cadastro_jogadores.business;

import com.palmeiras.cadastro_jogadores.infrastructure.entities.Jogador;
import com.palmeiras.cadastro_jogadores.infrastructure.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JogadorService {

    private final JogadorRepository repository;

    public Jogador salvarJogador(Jogador jogador) {
        if (jogador.getNome() == null || jogador.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome é obrigatório!");
        }

        if (repository.existsByNome(jogador.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um jogador com esse nome!");
        }

        jogador.setId(null);
        return repository.saveAndFlush(jogador);
    }

    public List<Jogador> listarTodosJogadores() {
        return repository.findAll();
    }

    public Jogador buscarJogadorPorNome(String nome) {
        return repository.findByNome(nome)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nome não encontrado!"));
    }

    public void deletarJogadorPorNome(String nome) {
        Jogador jogador = buscarJogadorPorNome(nome);
        repository.delete(jogador);
    }

    public Jogador atualizarJogadorPorId(Integer id, Jogador jogador) {

        Jogador jogadorExistente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado!"));

        if (jogador.getNome() != null) {
            boolean nomeMudou = !jogador.getNome().equals(jogadorExistente.getNome());
            if (nomeMudou && repository.existsByNome(jogador.getNome())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um jogador com esse nome!");
            }
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
