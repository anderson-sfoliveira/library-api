package br.com.brlsistemas.librayapi;

import br.com.brlsistemas.librayapi.model.entity.Loan;
import br.com.brlsistemas.librayapi.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
public class LibrayApiApplication {

//	@Autowired
//	private EmailService emailService;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

//	@Bean // métodos do tipo CommandLineRunner são executados assim o projeto é executado
//	public CommandLineRunner commandLineRunner(){
//		return args -> {
//			sendMailTest();
//		};
//	}
//
//	@Scheduled(cron = "0 20 23 1/1 * *") // http://www.cronmaker.com/
//	public void mailTestSchedule() {
//		sendMailTest();
//	}
//
//	private void sendMailTest() {
//		List<String> emails = Arrays.asList("91178ce32b-0d1201@inbox.mailtrap.io");
//		emailService.sendEmails("Testando serviço de e-mails.", emails);
//		System.out.println("Email enviado!");
//	}

	public static void main(String[] args) {
		SpringApplication.run(LibrayApiApplication.class, args);
	}

}
