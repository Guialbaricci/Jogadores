package com.palmeiras.cadastro_jogadores.controller;

import com.palmeiras.cadastro_jogadores.business.JogadorService;
import com.palmeiras.cadastro_jogadores.infrastructure.entities.Jogador;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jogador")
@RequiredArgsConstructor

public class JogadorController {
    private final JogadorService jogadorService;

    @PostMapping
    public ResponseEntity<Void> salvarJogador(@RequestBody Jogador jogador){
        jogadorService.salvarJogador(jogador);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Jogador> buscarJogadorPorNome(@RequestParam String nome){
        return ResponseEntity.ok(jogadorService.buscarJogadorPorNome(nome));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarJogadorPorNome(@RequestParam String nome){
        jogadorService.deletarJogadorPorNome(nome);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarJogadorPorId(@RequestBody Jogador jogador,
                                                     @RequestParam Integer id) {
        jogadorService.atualizarJogadorPorId(id, jogador);
        return ResponseEntity.ok().build();
    }
}
