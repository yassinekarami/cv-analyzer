package org.cvanalyzer.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.cvanalyzer", "org.cvanalyzer.backoffice.service.impl"})
public class BackofficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackofficeApplication.class, args);
    }

}
