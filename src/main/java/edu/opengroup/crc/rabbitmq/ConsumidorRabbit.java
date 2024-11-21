package edu.opengroup.crc.rabbitmq;

import edu.opengroup.crc.entity.Bonus;
import edu.opengroup.crc.entity.Morador;
import edu.opengroup.crc.entity.MoradorBonus;
import edu.opengroup.crc.repository.MoradorBonusRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumidorRabbit {

    @Autowired
    MoradorBonusRepository moradorBonusRepository;

    @RabbitListener(queues = RabbitMQConfig.fila)
    public void relacionamentoMoradorBonus() {
        System.out.println("Resgate de bonus finalizado!");
    }

}
