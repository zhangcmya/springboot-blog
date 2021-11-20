package com.zcm.blog.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger属性
 *
 * @author zhangcm
 * @date 2021-10-27 18:16:15
 */
@ConfigurationProperties(prefix = "spring.swagger")
@Data
public class SwaggerProperties {

	/**
	 * 后端接口配置
	 */
	private SwaggerEntity back;

	@Data
	public static class SwaggerEntity {
		private String groupName;
		private String basePackage;
		private String title;
		private String description;
		private String contactName;
		private String contactEmail;
		private String contactUrl;
		private String version;
		private Boolean enable;
	}
}
