package edu.opengroup.crc.controller;

import edu.opengroup.crc.entity.*;
import edu.opengroup.crc.entity.dto.MoradorRequest;
import edu.opengroup.crc.entity.dto.MoradorRequestUpdate;
import edu.opengroup.crc.repository.AuthRepository;
import edu.opengroup.crc.repository.CondominioRepository;
import edu.opengroup.crc.repository.MoradorRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

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
//    @Autowired
//    private OpenAiChatClient openAiChatClient;

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

        System.out.println("Morador Request: " + moradorRequest);
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
                .authUser(Auth.builder()
                        .email(moradorRequest.email())
                        .hashSenha(passwordEncoder.encode(moradorRequest.senha()))
                        .role(role)
                        .build())
                .build();

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

    @PostMapping("/{id}/desativar-conta")
    public String desativarConta(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            var morador = moradorRepository.findById(id).orElse(null);
            if (morador != null) {
                morador.setStatus(Status.INATIVO);
                moradorRepository.save(morador);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorDesable", "Erro ao desativar conta. "+e.getMessage());
            return "/home";
        }
        return "redirect:/logout";
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
                                       BindingResult bindingResult){
        Morador morador = moradorRepository.findById(id).orElse(null);

        if (bindingResult.hasErrors()) {
            return new ModelAndView("profile_update")
                    .addObject("morador", morador)
                    .addObject("condominios", condominioRepository.findAll())
                    .addObject("moradorRequest", new MoradorRequest("","","","",null,"",null));
        }
        assert morador != null;
        Auth auth = morador.getAuthUser();
        auth.setHashSenha(passwordEncoder.encode(moradorUpdate.senha()));

        morador.setNome(moradorUpdate.nome());
        morador.setQtdMoradores(moradorUpdate.qtdMoradores());
        morador.setAuthUser(auth);

        try {
            moradorRepository.save(morador);
        }catch (DataIntegrityViolationException e){
            bindingResult.reject("error.morador", ""+e.getMostSpecificCause().getMessage());
        }
        return new ModelAndView("redirect:/home")
                .addObject("sucessMessage", "Atualizado com sucesso.");
    }
}
