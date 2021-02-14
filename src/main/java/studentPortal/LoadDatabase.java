package studentPortal;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
	
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	  @Bean
	  CommandLineRunner initDatabase(CourseRepository courseRepository,ProfessorRepository professorRepository ) {

	    return args -> {
	      log.info("Preloading " + courseRepository.save(new Course("Software Engg", "2020-01-21", "2021-01-21")));
	      log.info("Preloading " + courseRepository.save(new Course("Human Resouse", "2020-01-21", "2020-01-21")));
	      log.info("Preloading " + professorRepository.save(new Professor("John Smith","Director")));
	    };
	  }
}
