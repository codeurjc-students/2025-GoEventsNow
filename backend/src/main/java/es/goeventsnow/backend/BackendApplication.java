package es.goeventsnow.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EntityScan("es.goeventsnow.backend.model")
@EnableSpringDataWebSupport(
	pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
