package net.contal.spring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import net.contal.spring.datahandler.SqlUpdateRunner;
import net.contal.spring.datahandler.SqliteDbHandler;

@SpringBootApplication
public class SpringBootJpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpsApplication.class, args);
	}
	
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
	        System.out.println("DB checker started.....");
			SqliteDbHandler.checkDb();
			SqlUpdateRunner runner = new SqlUpdateRunner();
			runner.run();

	}
	
}
