package com.example.demo;

import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.demo.model.Note;
import com.example.demo.repository.NoteRepository;

@SpringBootApplication
@EnableJpaAuditing
public class SpringAngularPocApplication implements CommandLineRunner{
	@Autowired
	ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(SpringAngularPocApplication.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		
		for(String beanName : applicationContext.getBeanDefinitionNames()) {
			System.out.println("beanName :  "+beanName);
		}
	}
	
	@Bean
	CommandLineRunner runner(NoteRepository noteRepository) {
		return args-> {
			noteRepository.save(new Note(1L, "yes", "My note", "I am happy", new Timestamp(new Date().getTime())));
		};
		
	}
//	@Bean
	public DataSource dataSource() {

		// no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.H2) //.H2 or .DERBY
			.addScript("create-db.sql")
			.addScript("insert-data.sql")
			.build();
		return db;
	}
	
//	@Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
