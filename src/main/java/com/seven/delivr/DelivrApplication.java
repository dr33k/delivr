package com.seven.delivr;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableTransactionManagement
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "jwtAuth", scheme="bearer", bearerFormat = "JWT")
public class DelivrApplication {
	public static void main(String[] args) {
		SpringApplication.run(DelivrApplication.class, args);
	}

}
