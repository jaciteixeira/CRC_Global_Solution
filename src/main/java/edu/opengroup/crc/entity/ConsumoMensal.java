package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_OP_CRC_CONSUMO")
public class ConsumoMensal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_CONSUMO")
    @SequenceGenerator(name = "SQ_CRC_CONSUMO", sequenceName = "SQ_CRC_CONSUMO", allocationSize = 1)
    @Column(name = "ID_CONSUMO")
    private Long id;
    @Column(name = "QTD_CONSUMIDA")
    private Long quantidadeConsumida;
    @Column(name = "DATA")
    private LocalDateTime data;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(
            name = "ID_MORADR",
            referencedColumnName = "ID_MORADOR",
            foreignKey = @ForeignKey(name = "FK_CONSUMO_MORADOR")
    )
    private Morador morador;


}
