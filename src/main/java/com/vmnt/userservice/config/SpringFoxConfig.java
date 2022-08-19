package com.vmnt.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration for Swagger
 * 
 * @author vmntruong
 *
 */
@Configuration
public class SpringFoxConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.vmnt.userservice"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false)
				.tags(new Tag("API User", "Provides Application to Manage User"));
	}
}