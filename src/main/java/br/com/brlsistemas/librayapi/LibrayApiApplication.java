package br.com.brlsistemas.librayapi;

import br.com.brlsistemas.librayapi.model.entity.Loan;
import br.com.brlsistemas.librayapi.service.EmailService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
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
@OpenAPIDefinition(
		info = @Info(
				title = "Library API",
				description = "API do projeto de bibliotecas",
				version = "1.0",
				contact = @Contact(name = "Anderson Oliveira", url = "https://github.com/anderson-sfoliveira", email = "anderson.sfoliveira@gmail.com")
		),
		servers = @Server(
				url = "http://localhost:8080/",
				description = "Descrição do Server",
				variables = {
						@ServerVariable(name = "serverUrl", defaultValue = "localhost"),
						@ServerVariable(name = "serverHttpPort", defaultValue = "8080")
				}))
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
