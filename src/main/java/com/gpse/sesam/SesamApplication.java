package com.gpse.sesam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@ConfigurationPropertiesScan
@EnableMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true)
// TODO: Remove annotation
@SuppressWarnings("HideUtilityClassConstructor")
public class SesamApplication {
    public SesamApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(SesamApplication.class, args);
    }

}
