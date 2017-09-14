package net.contal.spring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@ComponentScan({ "net.contal.spring" , "net.contal.spring.controller" , "net.contal.spring.service" , "net.contal.spring.config" })
public class SpringBootJpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpsApplication.class, args);
	}
	
	
//	@EventListener(ApplicationReadyEvent.class)
//	public void doSomethingAfterStartup() {
//	        System.out.println("DB checker started.....");
//			SqliteDbHandler.checkDb();
//			SqlUpdateRunner runner = new SqlUpdateRunner();
//			runner.run();
//
//	}
	
//	@Bean
//	  public PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer(){
//		        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
//		        ppc.setLocation(new ClassPathResource("application.properties"));
//		        ppc.setIgnoreUnresolvablePlaceholders(true);
//		        return ppc;
//		    }
	
}
