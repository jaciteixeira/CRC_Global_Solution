package edu.opengroup.crc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "T_OP_CRC_MORADOR", uniqueConstraints = {
        @UniqueConstraint(name = "UK_OP_CRC_CPF", columnNames = "CPF")
})
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
    private Integer qtdMoradores = 1;
    @Column(name = "identificadorRes")
    private String identificadorRes;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(
            name = "ID_AUTH",
            referencedColumnName = "ID_AUTH",
            foreignKey = @ForeignKey(name = "FK_AUTH_MORADOR")
    )
    private Auth authUser;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(
            name = "ID_CONDOMINIO",
            referencedColumnName = "ID_CONDOMINIO",
            foreignKey = @ForeignKey(name = "FK_MORADOR_CONDOMINIO")
    )
    private Condominio condominio;
    @OneToMany(mappedBy = "morador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MoradorBonus> bonusAssociations = new HashSet<>();

}