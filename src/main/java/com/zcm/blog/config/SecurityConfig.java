package com.zcm.blog.config;

import com.zcm.blog.filter.JwtAuthorizationFilter;
import com.zcm.blog.filter.JwtLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * 定义security配置
 * EnableWebSecurity 配置Web安全过滤器和启用全局认证机制
 * EnableGlobalMethodSecurity 开启基于方法的安全认证机制，也就是说在web层的controller启用注解机制的安全确认
 *
 * @author zhangcm
 * @date 2021-10-27 21:46:42
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 安全配置
	 *
	 * @param http http
	 * @throws Exception e
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 跨域共享
		http.cors()
			.and()
			// 跨域伪造请求限制无效
			.csrf().disable()
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			// 开启登录前认证流程过滤器
			.addFilterBefore(new JwtLoginFilter(authenticationManager()),
				UsernamePasswordAuthenticationFilter.class)
			// 开启访问控制层认证流程过滤器
			.addFilterAfter(new JwtAuthorizationFilter(authenticationManager()), BasicAuthenticationFilter.class)
			// 前后端分离禁用Session
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

	/**
	 * 忽略拦截url或静态资源文件夹
	 * web.ignoring(): 会直接过滤该url - 将不会经过Spring Security过滤器链
	 * http.permitAll(): 不会绕开Spring Security验证，相当于是允许该路径通过
	 *
	 * @param web web
	 * @throws Exception e
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 放行swagger
		web.ignoring().antMatchers(HttpMethod.GET,
			"/swagger-ui.html",
			"/swagger-ui/*",
			"/swagger-resources/**",
			"/v2/api-docs",
			"/v3/api-docs",
			"/webjars/**");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
