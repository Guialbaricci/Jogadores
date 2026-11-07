package com.palmeiras.cadastro_jogadores.controller;

import com.palmeiras.cadastro_jogadores.business.JogadorService;
import com.palmeiras.cadastro_jogadores.infrastructure.entities.Jogador;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// CROSS: libera o navegador a chamar essa API a partir do frontend.
// 63342 = servidor embutido do IntelliJ | 5500 = extensão "Live Server" do VS Code
@CrossOrigin(origins = {
        "http://localhost:63342",
        "http://127.0.0.1:63342",
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
@RestController
@RequestMapping("/jogador")
@RequiredArgsConstructor
public class JogadorController {

    private final JogadorService service;
    @PostMapping
    public ResponseEntity<Void> salvarJogador(@RequestBody Jogador jogador) {
        service.salvarJogador(jogador);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> buscarJogador(@RequestParam(required = false) String nome) {

        if (nome == null || nome.isBlank()) {
            List<Jogador> jogadores = service.listarTodosJogadores();
            return ResponseEntity.ok(jogadores);
        }

        return ResponseEntity.ok(service.buscarJogadorPorNome(nome));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarJogador(@RequestParam String nome) {
        service.deletarJogadorPorNome(nome);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarJogador(
            @RequestParam Integer id,
            @RequestBody Jogador jogador) {

        service.atualizarJogadorPorId(id, jogador);
        return ResponseEntity.ok().build();
    }
}
