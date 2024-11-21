package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_OP_CRC_BONUS")
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
    @Column(name = "CUSTO")
    private Integer custo;
    @Column(name = "QTD")
    private Integer quantidade;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(
            name = "ID_CONDOMINIO",
            referencedColumnName = "ID_CONDOMINIO",
            foreignKey = @ForeignKey(name = "FK_BONUS_CONDOMINIO")
    )
    private Condominio condominio;
    @OneToMany(mappedBy = "bonus", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MoradorBonus> moradorAssociations = new HashSet<>();
}
