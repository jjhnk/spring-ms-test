package hy.oltp.core.estate.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
  @Value("${api.common.version}")
  String apiVersion;
  @Value("${api.common.title}")
  String apiTitle;
  @Value("${api.common.description}")
  String apiDescription;
  @Value("${api.common.termsOfService}")
  String apiTermsOfService;
  @Value("${api.common.license}")
  String apiLicense;
  @Value("${api.common.licenseUrl}")
  String apiLicenseUrl;
  @Value("${api.common.externalDocDesc}")
  String apiExternalDocDesc;
  @Value("${api.common.externalDocUrl}")
  String apiExternalDocUrl;
  @Value("${api.common.contact.name}")
  String apiContactName;
  @Value("${api.common.contact.url}")
  String apiContactUrl;
  @Value("${api.common.contact.email}")
  String apiContactEmail;
  @Value("${springdoc.oAuthFlow.authorizationUrl}")
  String oAuthFlowAuthorizationUrl;
  @Value("${springdoc.oAuthFlow.tokenUrl}")
  String oAuthFlowTokenUrl;

  @Bean
  OpenAPI getOpenApi() {
    var components = new Components()
      .addSecuritySchemes("bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT"))
      /* .addSecuritySchemes("oAuth2", new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
        .flows(new OAuthFlows().authorizationCode(new OAuthFlow().authorizationUrl(oAuthFlowAuthorizationUrl)
          .tokenUrl(oAuthFlowTokenUrl)
          .scopes(new Scopes().addString("estate:read", "read scope")
            .addString("estate:write", "write scope"))))) */;
    return new OpenAPI().info(new Info().title(apiTitle)
      .description(apiDescription)
      .version(apiVersion)
      .contact(new Contact().name(apiContactName)
        .url(apiContactUrl)
        .email(apiContactEmail))
      .termsOfService(apiTermsOfService)
      .license(new License().name(apiLicense)
        .url(apiLicenseUrl)))
      .externalDocs(new ExternalDocumentation().description(apiExternalDocDesc)
        .url(apiExternalDocUrl))
      .components(components);
  }

  @Bean
  GroupedOpenApi publicApi() {
    String[] paths = {"/**"};
    return GroupedOpenApi.builder()
      .group("public")
      .pathsToMatch(paths)
      .build();
  }

  @Bean
  GroupedOpenApi buildingApi() {
    String[] packagesToScan = {"hy.oltp.core.estate.building"};
    return GroupedOpenApi.builder()
      .group("building")
      .packagesToScan(packagesToScan)
      .build();
  }

  @Bean
  GroupedOpenApi unitApi() {
    String[] packagesToScan = {"hy.oltp.core.estate.unit"};
    return GroupedOpenApi.builder()
      .group("unit")
      .packagesToScan(packagesToScan)
      .build();
  }

  @Bean
  GroupedOpenApi tenantApi() {
    String[] packagesToScan = {"hy.oltp.core.estate.tenant"};
    return GroupedOpenApi.builder()
      .group("tenant")
      .packagesToScan(packagesToScan)
      .build();
  }

  @Bean
  GroupedOpenApi leaseApi() {
    String[] packagesToScan = {"hy.oltp.core.estate.lease"};
    return GroupedOpenApi.builder()
      .group("lease")
      .packagesToScan(packagesToScan)
      .build();
  }

}
