package com.zcm.blog.filter;

import com.alibaba.fastjson.JSON;
import com.zcm.blog.utils.JwtTokenUtil;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 登录过滤器
 *
 * @author zhangcm
 * @date 2021-11-17 18:16:17
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

	public JwtLoginFilter(AuthenticationManager authenticationManager) {
		setAuthenticationManager(authenticationManager);
		// 更改默认登录路径
		super.setFilterProcessesUrl("/api/login");

	}

	/**
	 * 在过滤之前和之后执行的事件
	 *
	 * @param request  请求
	 * @param response 返回值
	 * @param chain    chain
	 * @throws IOException      IOException
	 * @throws ServletException ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		super.doFilter(request, response, chain);
	}

	/**
	 * 验证操作 接收并解析用户凭证
	 *
	 * @param request  请求
	 * @param response 返回
	 * @return Authentication
	 * @throws AuthenticationException e
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		if (!request.getMethod().equals(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		// 通过request param中获取用户名密码
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new UsernameNotFoundException("用户名或密码不能为null");
		}
		// 用户名密码校验
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		this.setDetails(request, authRequest);
		return getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * 验证成功后的处理方法
	 * 若验证成功后生成token并返回
	 *
	 * @param request    请求
	 * @param response   返回
	 * @param chain      chain
	 * @param authResult 认证参数
	 * @throws IOException      e
	 * @throws ServletException e
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		// 从User中获取权限信息
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		// 创建Token
		String token = JwtTokenUtil.createToken(user.getUsername(), authorities.toString());
		// 设置编码 防止乱码问题
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		// 在请求头里返回创建成功的token
		// 设置请求头为带有"Bearer "前缀的token字符串
		response.setHeader("token", JwtTokenUtil.TOKEN_PREFIX + token);

		// 处理编码方式 防止中文乱码
		response.setContentType("text/json;charset=utf-8");
		// 将反馈塞到HttpServletResponse中返回给前台
		response.getWriter().write(JSON.toJSONString("登录成功"));
	}

	/**
	 * 验证失败后处理方法
	 *
	 * @param request  请求
	 * @param response 返回
	 * @param failed   失败异常信息
	 * @throws IOException      e
	 * @throws ServletException e
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											  AuthenticationException failed) throws IOException, ServletException {
		String returnData;
		// 账号过期
		if (failed instanceof AccountExpiredException) {
			returnData = "账号过期";
		}
		// 密码错误
		else if (failed instanceof BadCredentialsException) {
			returnData = "密码错误";
		}
		// 密码过期
		else if (failed instanceof CredentialsExpiredException) {
			returnData = "密码过期";
		}
		// 账号不可用
		else if (failed instanceof DisabledException) {
			returnData = "账号不可用";
		}
		//账号锁定
		else if (failed instanceof LockedException) {
			returnData = "账号锁定";
		}
		// 用户不存在
		else if (failed instanceof InternalAuthenticationServiceException) {
			returnData = "用户不存在";
		}
		// 其他错误
		else {
			returnData = "未知异常";
		}

		// 处理编码方式 防止中文乱码
		response.setContentType("text/json;charset=utf-8");
		// 将反馈塞到HttpServletResponse中返回给前台
		response.getWriter().write(JSON.toJSONString(returnData));
	}
}
