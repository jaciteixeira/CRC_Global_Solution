package edu.opengroup.crc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrcApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrcApplication.class, args);
	}
	// TODO: quando o morador enviar o a fatura(CONSUMO MENSAL) menor que do mes anterior ganha 100 pontos, maior ganha 50

}
