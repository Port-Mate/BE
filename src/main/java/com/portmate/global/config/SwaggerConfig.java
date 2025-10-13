package com.portmate.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		// Security Scheme 정의 (Bearer JWT)
		SecurityScheme apiKey = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization"); // 헤더 이름

		// Security Requirement (전역 적용)
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("Authorization");

		// Components에 Security Scheme 등록
		Components components = new Components()
			.addSecuritySchemes("Authorization", apiKey);

		return new OpenAPI()
			.addServersItem(new Server().url("/")) // 리버스 프록시 등일 때 베이스 경로
			.components(components)
			.addSecurityItem(securityRequirement)
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("PortMate API")
			.version("v1.0");
	}
}

