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
    @Column(name = "CEP")
    private String cep;
    @Column(name = "NUMERO")
    private String numero;
    
}
