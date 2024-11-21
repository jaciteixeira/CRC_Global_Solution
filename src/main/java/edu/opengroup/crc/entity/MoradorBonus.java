package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_OP_CRC_MORADOR_BONUS")
public class MoradorBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_MORADOR_BONUS")
    @SequenceGenerator(name = "SQ_CRC_MORADOR_BONUS", sequenceName = "SQ_CRC_MORADOR_BONUS", allocationSize = 1)
    @Column(name = "ID_MORADOR_BONUS")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_MORADOR",
            referencedColumnName = "ID_MORADOR",
            foreignKey = @ForeignKey(name = "FK_MORADOR_BONUS_MORADOR")
    )
    private Morador morador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_BONUS",
            referencedColumnName = "ID_BONUS",
            foreignKey = @ForeignKey(name = "FK_MORADOR_BONUS_BONUS")
    )
    private Bonus bonus;
}
