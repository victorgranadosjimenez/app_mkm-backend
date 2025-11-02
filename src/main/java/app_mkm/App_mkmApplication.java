
package app_mkm;

import app_mkm.scheduler.AlertChecker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "app_mkm")
@EntityScan(basePackages = "app_mkm.entity")
@EnableJpaRepositories(basePackages = "app_mkm.repository")
public class App_mkmApplication {
    public static void main(String[] args) {

        var context = SpringApplication.run(App_mkmApplication.class, args);
        context.getBean(AlertChecker.class).triggerTestAlert(); // 👈 fuerza la alerta

    }
}
