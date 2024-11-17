package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_OP_CRC_BONUS", uniqueConstraints = {
        @UniqueConstraint(name = "UK_OP_CRC_EMAIL", columnNames = "EMAIL")
})
public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_BONUS")
    @SequenceGenerator(name = "SQ_CRC_BONUS", sequenceName = "SQ_CRC_BONUS", allocationSize = 1)
    @Column(name = "ID_BONUS")
    private Long id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    //todo: FINALIZAR ATRIBUTOS
}
