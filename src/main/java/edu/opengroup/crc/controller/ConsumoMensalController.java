package edu.opengroup.crc.controller;

import edu.opengroup.crc.entity.ConsumoMensal;
import edu.opengroup.crc.entity.Morador;
import edu.opengroup.crc.entity.dto.ConsumoRequest;
import edu.opengroup.crc.exception.ResourceNotFoundException;
import edu.opengroup.crc.repository.ConsumoMensalRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ConsumoMensalController {

    @Autowired
    MoradorRepository moradorRepository;
    @Autowired
    ConsumoMensalRepository consumoRepository;

    @GetMapping("/{id}/enviar-fatura")
    public ModelAndView viewFatura(@PathVariable("id") Long id) {

        Morador morador = moradorRepository.findById(id).get();
        return new ModelAndView( "register-fatura")
                .addObject("consumoRequest", new ConsumoRequest(null, null))
                .addObject("morador", morador);
    }

    @PostMapping("/{id}/enviar-fatura")
    public ModelAndView viewFatura(@PathVariable("id") Long id,
                                   @Valid ConsumoRequest consumo,
                                   BindingResult bindingResult) {
        // Buscando o morador
        var morador = moradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Morador não encontrado"));

        // Verificando se houve erro na validação do formulário
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-fatura")
                    .addObject("consumoRequest", consumo)
                    .addObject("morador", morador);
        }

        // Buscar o último consumo do morador (se existir)
        var ultimoConsumo = consumoRepository.findTopByMoradorOrderByDataUploadDesc(morador);

        int pontos = 50; // Pontos padrão caso o consumo atual seja maior
        if (ultimoConsumo.isPresent()) {
            var consumoAnterior = ultimoConsumo.get();
            if (consumo.quantidadeConsumida() < consumoAnterior.getQuantidadeConsumida()) {
                pontos = 100; // Caso o consumo atual seja menor que o anterior
            }
        }

        // Atualizar os pontos do morador
        morador.setPontos(morador.getPontos() + pontos);
        moradorRepository.save(morador);

        // Criando e salvando o consumo mensal
        ConsumoMensal consumoMensal = ConsumoMensal.builder()
                .dataUpload(LocalDateTime.now())
                .dataConsumo(consumo.dataConsumo())
                .quantidadeConsumida(consumo.quantidadeConsumida())
                .morador(morador)
                .build();

        consumoRepository.save(consumoMensal);

        // Redirecionando para a página de listar faturas do morador
        return new ModelAndView("redirect:/"+id+"/faturas");
    }

    @GetMapping("/{id}/faturas")
    public ModelAndView viewfaturas(@PathVariable("id")Long id){

        Morador morador = moradorRepository.findById(id).get();

        List<ConsumoMensal> faturas = consumoRepository.findByMorador(morador);
        return new ModelAndView("faturas")
                .addObject("faturas", faturas)
                .addObject("morador", morador);
    }

    @GetMapping("/{id}/details-fatura")
    public ModelAndView detalhesFatura(@PathVariable("id")Long id){
        ConsumoMensal consumo = consumoRepository.findById(id).get();
        if (consumo == null) {
            return new ModelAndView("redirect:/"+id+"/faturas");
        }
        return new ModelAndView("details-fatura")
                .addObject("consumo", consumo);

    }

}
