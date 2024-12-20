package edu.opengroup.crc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "/teste", "/new-user", "/h2-console/**", "/public/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/moradores","/remover-morador").hasAuthority("ADMIN")
                        .requestMatchers("/condominios", "/add-condominio").hasAuthority("MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?erro=true")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login")
                        .maximumSessions(1) // Limita a apenas uma sessão por usuário
                        .expiredUrl("/login?expired=true")
                )
                .exceptionHandling((exception) ->
                        exception.accessDeniedHandler((request,response,accessDeniedHandler) ->
                        {response.sendRedirect("/acesso_negado");}));

        return http.build();
    }
}
