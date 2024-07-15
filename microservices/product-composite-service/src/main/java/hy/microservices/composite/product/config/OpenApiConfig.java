package hy.microservices.composite.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SecurityScheme(
  name = "security_auth",
  type = SecuritySchemeType.OAUTH2,
  flows = @OAuthFlows(
    authorizationCode = @OAuthFlow(
      authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
      tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
      scopes = {
        @OAuthScope(name = "product:read", description = "read scope"),
        @OAuthScope(name = "product:write", description = "write scope")})))
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

  @Bean
  OpenAPI getOpenApi() {
    return new OpenAPI()
    .info(new Info().title(apiTitle)
      .description(apiDescription)
      .version(apiVersion)
      .contact(new Contact().name(apiContactName)
        .url(apiContactUrl)
        .email(apiContactEmail))
      .termsOfService(apiTermsOfService)
      .license(new License().name(apiLicense)
        .url(apiLicenseUrl)))
      .externalDocs(new ExternalDocumentation().description(apiExternalDocDesc)
        .url(apiExternalDocUrl));
  }
}
