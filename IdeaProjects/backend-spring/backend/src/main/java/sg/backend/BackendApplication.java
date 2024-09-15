package sg.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//
// api_test / hyeon
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//
//

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}

//
// api_test / hyeon
@RestController
class ApiController {
	@GetMapping("/api_test")
	public String api_test() {
		return "test complete";
	}
}
//
//