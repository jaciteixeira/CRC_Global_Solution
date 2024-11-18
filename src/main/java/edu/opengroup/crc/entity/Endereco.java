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
@Table(name = "T_OP_CRC_ENDERECO")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_ENDERECO")
    @SequenceGenerator(name = "SQ_CRC_ENDERECO", sequenceName = "SQ_CRC_ENDERECO", allocationSize = 1)
    @Column(name = "ID_ENDERECO")
    private Long id;
    @Column(name = "LOGRADOURO")
    private String logradouro;
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "BAIRRO")
    private String bairro;
    @Column(name = "CIDADE")
    private String cidade;
    @Column(name = "UF", length = 2)
    private String uf;
    @Column(name = "CEP")
    private String cep;

}
