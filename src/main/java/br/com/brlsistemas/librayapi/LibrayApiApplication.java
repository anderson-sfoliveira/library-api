package br.com.brlsistemas.librayapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class LibrayApiApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

//	@Scheduled(cron = "0 33 19 1/1 * ? *") // http://www.cronmaker.com/
	@Scheduled(cron = "0 41 19 * * *")
	public void testeAgendamentoTarefas(){
		System.out.println("Agendamento de tarefas.");
	}

	public static void main(String[] args) {
		SpringApplication.run(LibrayApiApplication.class, args);
	}

}
