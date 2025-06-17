package in.athenaeum.jpmcspringjune2025day2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI getOpenAPIConfiguration() {
        return new OpenAPI()
                .info(new Info().title("Project Name")
                        .description("Spring Boot RESTful Webservice")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://company.com")))
                        .externalDocs(new ExternalDocumentation()
                        .description("Documentation")
                        .url("https://company.com/docs"));
    }
}
