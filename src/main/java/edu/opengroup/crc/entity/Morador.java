package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "T_OP_CRC_MORADOR")
public class Morador {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_MORADOR")
    @SequenceGenerator(name = "SQ_CRC_MORADOR", sequenceName = "SQ_CRC_MORADOR", allocationSize = 1)
    @Column(name = "ID_MORADOR")
    private Long id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "CPF")
    private String cpf;
    @Column(name = "pontos")
    private Integer pontos;
    @Column(name = "qtd_moradores")
    private Integer qtdMoradores;
    @Column(name = "identificadorRes")
    private String identificadorRes;
}
// TODO: quando o morador enviar o a fatura menor que do mes anterior ganha 100 pontos, maior ganha 50 
