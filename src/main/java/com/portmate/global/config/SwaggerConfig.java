package com.portmate.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Value("${server.servlet.context-path:/api}")
	private String contextPath;

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
			.addServersItem(new Server().url(contextPath))
			.components(components)
			.addSecurityItem(securityRequirement)
			.info(apiInfo());
	}
	@Bean
	public OpenApiCustomizer prependApiPrefix() {
		return openApi -> {
			var newPaths = new io.swagger.v3.oas.models.Paths();
			openApi.getPaths().forEach((p, item) -> {
				if (!p.startsWith(contextPath)) {
					newPaths.addPathItem(contextPath + p, item);
				} else {
					newPaths.addPathItem(p, item);
				}
			});
			openApi.setPaths(newPaths);
		};
	}

	private Info apiInfo() {
		return new Info()
			.title("PortMate API")
			.version("v1.0");
	}
}

