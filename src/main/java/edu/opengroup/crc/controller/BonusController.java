package edu.opengroup.crc.controller;

import edu.opengroup.crc.entity.Bonus;
import edu.opengroup.crc.entity.Condominio;
import edu.opengroup.crc.entity.Morador;
import edu.opengroup.crc.entity.MoradorBonus;
import edu.opengroup.crc.entity.dto.BonusRequest;
import edu.opengroup.crc.rabbitmq.RabbitMQConfig;
import edu.opengroup.crc.repository.BonusRepository;
import edu.opengroup.crc.repository.CondominioRepository;
import edu.opengroup.crc.repository.MoradorBonusRepository;
import edu.opengroup.crc.repository.MoradorRepository;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class BonusController {

    @Autowired
    BonusRepository bonusRepository;
    @Autowired
    CondominioRepository condominioRepository;
    @Autowired
    MoradorRepository moradorRepository;
    @Autowired
    MoradorBonusRepository moradorBonusRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/{idMorador}/{idCondominio}/bonus-disponiveis")
    public ModelAndView bonusDisponiveis(@PathVariable("idCondominio") Long idCondominio,
                                         @PathVariable("idMorador") Long idMorador) {
        Morador morador = moradorRepository.findById(idMorador)
                .orElseThrow(() -> new IllegalArgumentException("Morador não encontrado com o ID: " + idMorador));

        Condominio condominio = condominioRepository.findById(idCondominio)
                .orElseThrow(() -> new IllegalArgumentException("Condomínio não encontrado com o ID: " + idCondominio));

        List<Bonus> bonus = bonusRepository.findAllByCondominio(condominio);

        ModelAndView modelAndView = new ModelAndView("bonus");
        modelAndView.addObject("listbonus", bonus);
        modelAndView.addObject("morador", morador);
        modelAndView.addObject("condominio", condominio);
        return modelAndView;
    }

    @PostMapping("/{idMorador}/{idCondominio}/{idBonus}/resgatar-bonus")
    public ModelAndView resgatarBonus(@PathVariable("idMorador") Long idMorador,
                                      @PathVariable("idCondominio") Long idCondominio,
                                      @PathVariable("idBonus") Long idBonus,
                                      RedirectAttributes redirectAttributes) {

        Morador morador = moradorRepository.findById(idMorador)
                .orElseThrow(() -> new IllegalArgumentException("Morador não encontrado com o ID: " + idMorador));

        Condominio condominio = condominioRepository.findById(idCondominio)
                .orElseThrow(() -> new IllegalArgumentException("Condomínio não encontrado com o ID: " + idCondominio));

        Bonus bonus = bonusRepository.findById(idBonus)
                .orElseThrow(() -> new IllegalArgumentException("Bônus não encontrado com o ID: " + idBonus));

        if (!bonus.getCondominio().getId().equals(condominio.getId())) {
            throw new IllegalArgumentException("O bônus não pertence ao condomínio informado.");
        }

        int quantidadeDisponivel = bonus.getQuantidade();
        if (quantidadeDisponivel <= 0) {
            throw new IllegalArgumentException("Bônus indisponível para resgate.");
        }


        ModelAndView modelAndView = new ModelAndView("bonus-resgatado");
        modelAndView.addObject("bonus", bonus);
        modelAndView.addObject("morador", morador);
        modelAndView.addObject("condominio", condominio);

        if (morador.getPontos() >= bonus.getCusto()){

//            morador.setPontos(morador.getPontos() - bonus.getCusto());
//            moradorRepository.save(morador);
//
//            MoradorBonus moradorBonus = new MoradorBonus();
//            moradorBonus.setMorador(morador);
//            moradorBonus.setBonus(bonus);
//            moradorBonusRepository.save(moradorBonus);
//
//            bonus.setQuantidade(quantidadeDisponivel - 1);
//            bonusRepository.save(bonus);

            rabbitTemplate.convertAndSend(RabbitMQConfig.roteador,
                    RabbitMQConfig.chave_rota,
                    bonus.getNome());
        }
        else {
            redirectAttributes.addFlashAttribute("bonusInsuficiente", "Bonus insuficiente");
            modelAndView.setView(new RedirectView("/"+idMorador+"/"+idCondominio+"/bonus-disponiveis"));
        }

        return modelAndView;
    }

    @GetMapping("/{idMorador}/{idCondominio}/adicionar-bonus")
    public ModelAndView adicionarBonus(@PathVariable("idMorador") Long idMorador,
                                       @PathVariable("idCondominio") Long idCondominio) {
        Condominio condominio = condominioRepository.findById(idCondominio).get();
        Morador morador = moradorRepository.findById(idMorador).get();
        return new ModelAndView("register-bonus")
                .addObject("bonusRequest", BonusRequest.builder().build())
                .addObject("condominio", condominio)
                .addObject("morador", morador);
    }

    @PostMapping("/{idMorador}/{idCondominio}/adicionar-bonus")
    public ModelAndView adicionarBonus(@PathVariable("idMorador") Long idMorador,
                                       @PathVariable("idCondominio") Long idCondominio,
                                       @Valid BonusRequest bonusRequest,
                                       BindingResult bindingResult) {

        Condominio condominio = condominioRepository.findById(idCondominio).get();
        Morador morador = moradorRepository.findById(idMorador).get();

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-bonus")
                    .addObject("bonusRequest", bonusRequest)
                    .addObject("condominio", condominio)
                    .addObject("morador", morador);
        }

        Bonus bonus = Bonus.builder()
                .nome(bonusRequest.nome())
                .descricao(bonusRequest.descricao())
                .custo(bonusRequest.custo())
                .quantidade(bonusRequest.quantidade())
                .condominio(condominio)
                .build();

        bonusRepository.save(bonus);

        return new ModelAndView(new RedirectView("/"+idMorador+"/"+idCondominio+"/bonus-disponiveis"));
//                .addObject("listbonus", bonus)
//                .addObject("morador", morador)
//                .addObject("condominio", condominio);
    }


}
