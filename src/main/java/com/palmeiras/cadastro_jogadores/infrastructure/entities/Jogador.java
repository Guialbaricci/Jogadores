package com.palmeiras.cadastro_jogadores.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "jogador")
@Entity
public class Jogador {
 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

 @Column(name = "nome", unique = true, length = 200)
    private String nome;

 @Column(name = "idade")
    private Integer idade;

 @Column(name = "nacionalidade", length = 100)
    private String nacionalidade;

 @Column(name = "gols")
    private Integer gols;
}
