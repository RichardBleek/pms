package personal.music.stream.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class PmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmsApplication.class, args);
	}

	@Bean
    String filesFolder(Environment environment) {
	    return environment.getProperty("pms.filesFolder");
    }

	@Bean
    String hostName(Environment environment) {
	   return environment.getProperty("pms.hostName");
    }

	@Bean
    String applicationPath(Environment environment) {
	    return environment.getProperty("pms.applicationPath");
    }

    @Bean
    String pmsAuthor(Environment environment) {
	    return environment.getProperty("pms.author");
    }
}
