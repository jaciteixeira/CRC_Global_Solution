package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "T_OP_CRC_CONDOMINIO")
public class Condominio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_CONDOMINIO")
    @SequenceGenerator(name = "SQ_CRC_CONDOMINIO", sequenceName = "SQ_CRC_CONDOMINIO", allocationSize = 1)
    @Column(name = "ID_CONDOMINIO")
    private Long id;
    @Column(name = "NOME")
    private String nome;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(
            name = "ID_ENDERECO",
            referencedColumnName = "ID_ENDERECO",
            foreignKey = @ForeignKey(name = "FK_ENDERECO_CONDOMINIO")
    )
    private Endereco endereco;
}