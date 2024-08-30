package Microservices.Enrollment_Service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

// Swagger Configuration class for Documenting APIs

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI enrollmentServiceApi() {
		return new OpenAPI()
				.info(new Info().title("Enrollment Service API")
						.description("This Service is Dedicated for Subscriber Enrollment").version("v0.0.1")
						.license(new License().name("Apache 2.0")))
				.externalDocs(new ExternalDocumentation().description("Refer this Docs for More Information about APIs")
						.url("http://enrollment-service-api.docs")) // Dummy URL given
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth",
						new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT").in(In.HEADER)
								.name("Authorization")));
	}
}
