
package app_mkm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class App_mkmApplication {
    public static void main(String[] args) {
        SpringApplication.run(App_mkmApplication.class, args);
    }
}
