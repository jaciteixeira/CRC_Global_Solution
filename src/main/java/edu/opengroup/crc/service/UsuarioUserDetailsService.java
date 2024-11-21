package edu.opengroup.crc.service;

import java.util.Collections;
import java.util.stream.Collectors;

import edu.opengroup.crc.entity.Auth;
import edu.opengroup.crc.entity.Status;
import edu.opengroup.crc.exception.UsernameInactiveException;
import edu.opengroup.crc.repository.AuthRepository;
import edu.opengroup.crc.repository.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UsuarioUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthRepository uRep;
    @Autowired
    private MoradorRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Auth resgatado = uRep.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Usuário " + email + " não encontrado na base de dados"));

//        // Verifica se o usuário está ativo
//        var user = userRepo.findByAuthUser(resgatado);
//        if (user.getStatus().equals(Status.INATIVO)) {
//            throw new UsernameInactiveException("Usuário " + email + " não está ativo");
//        }

        return new User(
                resgatado.getEmail(),
                resgatado.getHashSenha(),
                Collections.singletonList(new SimpleGrantedAuthority(resgatado.getRole().toString()))
        );
    }


}
