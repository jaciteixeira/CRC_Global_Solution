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
@Table(name = "T_OP_CRC_AUTH", uniqueConstraints = {
        @UniqueConstraint(name = "UK_OP_CRC_EMAIL", columnNames = "EMAIL")
})
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CRC_AUTH")
    @SequenceGenerator(name = "SQ_CRC_AUTH", sequenceName = "SQ_CRC_AUTH", allocationSize = 1)
    @Column(name = "ID_AUTH")
    private Long id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "HASH_SENHA")
    private String hashSenha;
//    @Column(name = "ULTIMO_LOGIN")
//    private LocalDateTime ultimoLogin;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "T_OP_ASSOCIACAO_AUTH_ROLE",
//            joinColumns = @JoinColumn(name = "id_auth"),
//            inverseJoinColumns = @JoinColumn(name = "id_role"))
//    private Set<Role> roles = new HashSet<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;
}