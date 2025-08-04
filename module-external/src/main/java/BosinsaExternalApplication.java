import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
public class BosinsaExternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BosinsaExternalApplication.class, args);
	}
}
