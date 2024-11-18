package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_OP_CRC_ASSOCIACAO_MORADOR_BONUS")
public class MoradorBonus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_ASSOCIACAO")
    @SequenceGenerator(name = "SQ_CRC_ASSOCIACAO", sequenceName = "SQ_CRC_ASSOCIACAO", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_MORADOR", nullable = false, foreignKey = @ForeignKey(name = "FK_MORADOR_ASSOCIACAO"))
    private Morador morador;

    @ManyToOne
    @JoinColumn(name = "ID_BONUS", nullable = false, foreignKey = @ForeignKey(name = "FK_BONUS_ASSOCIACAO"))
    private Bonus bonus;

    @Column(name = "QTD", nullable = false)
    private Integer qtd;
}
