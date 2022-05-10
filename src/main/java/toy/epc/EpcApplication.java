package toy.epc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpcApplication.class, args);
	}

}
