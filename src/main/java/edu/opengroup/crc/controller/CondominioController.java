package edu.opengroup.crc.controller;

import edu.opengroup.crc.entity.Condominio;
import edu.opengroup.crc.entity.Endereco;
import edu.opengroup.crc.entity.Morador;
import edu.opengroup.crc.entity.dto.CondominioRequest;
import edu.opengroup.crc.repository.CondominioRepository;
import edu.opengroup.crc.repository.MoradorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CondominioController {

    @Autowired
    CondominioRepository condominioRepository;
    @Autowired
    MoradorRepository moradorRepository;

    @GetMapping("/condominios")
    public ModelAndView condominios() {
        List<Condominio> condominios = condominioRepository.findAll();
        return new ModelAndView("condominios")
                .addObject("condominios", condominios);
    }

    @GetMapping("/add-condominio")
    public ModelAndView addCondominio() {

        CondominioRequest condominio = CondominioRequest.builder().build();
        ModelAndView mav = new ModelAndView("register-condominio")
                .addObject("condominioRequest",condominio );
        return mav;
    }

    @PostMapping("/add-condominio")
    public ModelAndView addCondominio(@Valid CondominioRequest condominioRequest,
                                      BindingResult bindingResult   ) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-condominio")
                    .addObject("condominioRequest",condominioRequest );
        }

        Condominio condominio = condominioRepository.findByNome(condominioRequest.nome());
        if (condominio == null) {
            var endereco = Endereco.builder()
                    .uf(condominioRequest.uf())
                    .cep(condominioRequest.cep())
                    .bairro(condominioRequest.bairro())
                    .cidade(condominioRequest.cidade())
                    .numero(condominioRequest.numero())
                    .logradouro(condominioRequest.logradouro())
                    .build();
            Condominio con = Condominio.builder()
                    .nome(condominioRequest.nome())
                    .endereco(endereco)
                    .build();

            condominioRepository.save(con);
        }
        return new ModelAndView("redirect:/condominios");
    }

    @GetMapping("/{id}/details-condominio")
    public ModelAndView detailsCondominio(@PathVariable("id") long id) {
        Condominio condominio = condominioRepository.findById(id).get();

        Long quantidadeMoradores = moradorRepository.countByCondominioId(id);

        return new ModelAndView("details-condominio")
                .addObject("condominio", condominio)
                .addObject("quantidadeMoradores", quantidadeMoradores);
    }

}
