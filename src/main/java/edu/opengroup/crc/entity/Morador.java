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
//    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinColumn(
//            name = "ID_CONSUMO",
//            referencedColumnName = "ID_CONSUMO",
//            foreignKey = @ForeignKey(name = "FK_MORADOR_CONSUMO")
//    )
//    private ConsumoMensal consumoMensal;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(
            name = "ID_AUTH",
            referencedColumnName = "ID_AUTH",
            foreignKey = @ForeignKey(name = "FK_AUTH_MORDADOR")
    )
    private Auth auth;
}
// TODO: quando o morador enviar o a fatura(CONSUMO MENSAL) menor que do mes anterior ganha 100 pontos, maior ganha 50
