package com.zcm.blog.config;

import com.zcm.blog.properties.SwaggerProperties;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger配置
 *
 * @author zhangcm
 * @date 2021-10-27 18:14:14
 */
@EnableOpenApi
@Configuration
@EnableConfigurationProperties(value = {SwaggerProperties.class})
public class SwaggerConfig {
	/**
	 * 配置属性
	 */
	@Autowired
	private SwaggerProperties properties;

	@Bean
	public Docket backApi() {
		return new Docket(DocumentationType.OAS_30)
			// 是否开启
			.enable(properties.getBack().getEnable())
			.groupName(properties.getBack().getGroupName())
			.apiInfo(frontApiInfo())
			.select()
			// 指定扫描的包
			.apis(RequestHandlerSelectors.basePackage(properties.getBack().getBasePackage()))
			.paths(PathSelectors.any())
			.build()
			.securityContexts(securityContexts())
			.securitySchemes(securitySchemes());
	}

	/**
	 * 前台api信息
	 * @return ApiInfo
	 */
	@Bean
	public ApiInfo frontApiInfo() {
		return new ApiInfoBuilder()
			.title(properties.getBack().getTitle())
			.description(properties.getBack().getDescription())
			.version(properties.getBack().getVersion())
			.contact(
				// 添加开发者的一些信息
				new Contact(properties.getBack().getContactName(),
					properties.getBack().getContactUrl(),
					properties.getBack().getContactEmail())
			)
			.build();
	}

	private List<SecurityScheme> securitySchemes() {
		List<SecurityScheme> apiKeyList = new ArrayList<>();
		apiKeyList.add(new ApiKey("Authorization", "user-token", "header"));
		return apiKeyList;
	}

	private List<SecurityContext> securityContexts() {
		List<SecurityContext> securityContexts = new ArrayList<>();
		securityContexts.add(
			SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("^(?!auth).*$"))
				.build());
		return securityContexts;
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		List<SecurityReference> securityReferences = new ArrayList<>();
		securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
		return securityReferences;
	}

}
