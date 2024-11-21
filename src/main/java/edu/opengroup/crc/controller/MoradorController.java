package edu.opengroup.crc.controller;

import edu.opengroup.crc.entity.*;
import edu.opengroup.crc.entity.dto.MoradorRequest;
import edu.opengroup.crc.entity.dto.MoradorRequestUpdate;
import edu.opengroup.crc.exception.ResourceNotFoundException;
import edu.opengroup.crc.repository.AuthRepository;
import edu.opengroup.crc.repository.CondominioRepository;
import edu.opengroup.crc.repository.MoradorRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Iterator;
import java.util.List;

@Controller
public class MoradorController {

    @Autowired
    AuthRepository authRepository;
    @Autowired
    MoradorRepository moradorRepository;
    @Autowired
    CondominioRepository condominioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/new-user")
    public ModelAndView viewNewUser(HttpSession session) {
        if (session.getAttribute("moradorRequest") != null ) {
            return new ModelAndView("home");
        }

        List<Condominio> condominios = condominioRepository.findAll();

        return new ModelAndView("register-user")
                .addObject("moradorRequest", new MoradorRequest("","","","",null,"",null))
                .addObject("condominios", condominios);
    }

    @PostMapping("/new-user")
    public ModelAndView newUser(
            @Valid MoradorRequest moradorRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Verifica se o e-mail já existe
        var auth = authRepository.findByEmail(moradorRequest.email());
        if (auth.isPresent()) {
            bindingResult.rejectValue("email", "error.email", "E-mail já cadastrado.");
        }

        // Validação do Condomínio
        if (moradorRequest.condominio() == null) {
            bindingResult.rejectValue("condominio", "error.condominio", "Condomínio deve ser selecionado.");
        }

        // Verifica erros de validação
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-user")
                    .addObject("moradorRequest", moradorRequest)
                    .addObject("condominios", condominioRepository.findAll());
        }
        // Define a Role com base no e-mail
        Role role = (moradorRequest.email().contains("@opengroup")) ? Role.ADMIN : Role.USER;


        // Cria o objeto Morador
        var morador = Morador.builder()
                .nome(moradorRequest.nome())
                .condominio(moradorRequest.condominio())
                .cpf(moradorRequest.cpf())
                .qtdMoradores(moradorRequest.qtdMoradores())
                .identificadorRes(moradorRequest.identificadorRes())
                .status(Status.ATIVO)
                .pontos(0)
                .authUser(Auth.builder()
                        .email(moradorRequest.email())
                        .hashSenha(passwordEncoder.encode(moradorRequest.senha()))
                        .role(role)
                        .build())
                .build();

        if (moradorRequest.identificadorRes() == null){
            morador.setIdentificadorRes("N/A");
        }

        // Tenta salvar o Morador
        try {
            moradorRepository.save(morador);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("email", "error.email", "Erro ao salvar: " + e.getMessage());
            return new ModelAndView("register-user")
                    .addObject("moradorRequest", moradorRequest)
                    .addObject("condominios", condominioRepository.findAll());
        }

        // Redireciona com mensagem de sucesso
        redirectAttributes.addFlashAttribute("successMessage", "Usuário cadastrado com sucesso.");
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/{id}/atualizar-perfil")
    public ModelAndView viewAtualizaPerfil(@PathVariable Long id) {
        Morador morador = moradorRepository.findById(id).get();
        return new ModelAndView("profile_update")
                .addObject("morador", morador)
                .addObject("condominios", condominioRepository.findAll())
                .addObject("moradorRequest", new MoradorRequestUpdate("",null,""));
    }

    @PostMapping("/{id}/atualizar-perfil")
    public ModelAndView atualizaPerfil(@PathVariable Long id,
                                       @Valid MoradorRequestUpdate moradorUpdate,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes){
        Morador morador = moradorRepository.findById(id).orElse(null);
        assert morador != null;

        if (bindingResult.hasErrors()) {
            return new ModelAndView("profile_update")
                    .addObject("morador", morador)
                    .addObject("moradorRequest", moradorUpdate);
        }

        Auth auth = morador.getAuthUser();
        auth.setHashSenha(passwordEncoder.encode(moradorUpdate.senha()));

        morador.setNome(moradorUpdate.nome());
        morador.setQtdMoradores(moradorUpdate.qtdMoradores());
        morador.setAuthUser(auth);

        try {
            moradorRepository.save(morador);
        }catch (DataIntegrityViolationException e){
            bindingResult.reject("nome", ""+e.getMostSpecificCause().getMessage());
        }
        redirectAttributes.addFlashAttribute("successUpdate", "Atualizado com sucesso.");
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/{id}/details-perfil")
    public ModelAndView viewDetailsPerfil(@PathVariable Long id) {
        Morador morador = moradorRepository.findById(id).orElse(null);
        return new ModelAndView("details-profile")
                .addObject("morador", morador);

    }

    @GetMapping("/{id}/moradores")
    public ModelAndView listarMoradores(@PathVariable("id") Long id) {
        // Buscar o condomínio pelo ID
        var condominio = condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condomínio não encontrado"));

        // Buscar os moradores associados ao condomínio
        var moradores = moradorRepository.findByCondominioId(id);
        Iterator<Morador> iterator = moradores.iterator();
        while (iterator.hasNext()) {
            Morador morador = iterator.next();
            if(morador.getAuthUser().getRole() == Role.ADMIN) {
                iterator.remove();
            }
        }

        // Retornar a view com os moradores
        return new ModelAndView("moradores")
                .addObject("condominio", condominio)
                .addObject("moradores", moradores);
    }

    @PostMapping("/{id}/remover-morador")
    public ModelAndView removeMorador(@PathVariable("id")Long id){
        Morador morador = moradorRepository.findById(id).orElse(null);
        if (morador != null) {
            moradorRepository.delete(morador);
        }
        return new ModelAndView("redirect:/"+id+"/moradores");
    }

    @PostMapping("/{id}/desativar-conta")
    public ModelAndView desativarConta(@PathVariable Long id,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes){
        Morador morador = moradorRepository.findById(id).orElse(null);
        try {
            if (morador != null) {
                morador.setStatus(Status.INATIVO);
                moradorRepository.save(morador);
                SecurityContextHolder.clearContext(); // Limpa o contexto de segurança
                session.invalidate(); // Invalida a sessão
                redirectAttributes.addFlashAttribute("message_desativo", "Conta desativada com sucesso.");

            }
        }catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message_desativo", "Erro ao excluir conta. "+e.getMessage());
            return new ModelAndView("/home");
        }
        return new ModelAndView("redirect:/logout");
    }

    @GetMapping("/{id}/ativar-conta")
    public ModelAndView ativarConta(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        var authUser = authRepository.findByEmail(auth.getName());
        var moradorAutenticado = moradorRepository.findByAuthUser(authUser.get());

        Morador morador = moradorRepository.findById(id).orElse(null);
        if (morador != null) {
            morador.setStatus(Status.ATIVO);
            moradorRepository.save(morador);
        }
        if (moradorAutenticado != null && moradorAutenticado.getAuthUser().getRole() == Role.USER) {
            redirectAttributes.addFlashAttribute("messageAtiva", "Conta ativada com sucesso");
            return new ModelAndView("home")
                    .addObject("user", morador);
        }
        return new ModelAndView( "redirect:/"+id+"/moradores");
    }


}
