package com.zcm.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt Token 工具类
 *
 * @author zhangcm
 * @date 2021-11-19 18:24:25
 */
public class JwtTokenUtil {

	/**
	 * 请求头
	 */
	public static final String TOKEN_HEADER = "Authorization";

	/**
	 * 请求前缀
	 */
	public static final String TOKEN_PREFIX = "Bearer ";

	/**
	 * token 过期时间
	 */
	private static final long TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;

	/**
	 * 秘钥
	 */
	private static final String TOKEN_SIGN_KEY = "123456";

	/**
	 * 声明role属性名
	 */
	private static final String USER_ROLE_KEY = "role";

	/**
	 * 声明username属性名
	 */
	private static final String USER_NAME_KEY = "sub";

	/**
	 * 创建token
	 * @param username 用户名
	 * @param role 权限
	 * @return token
	 */
	public static String createToken(String username, String role) {
		// 设置参数
		Map<String,Object> map = new HashMap<>(1);
		map.put(USER_ROLE_KEY, role);
		String token = Jwts.builder().setSubject(username)
			.setClaims(map)
			.claim(USER_NAME_KEY, username)
			.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
			.signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY).compressWith(CompressionCodecs.GZIP).compact();
		return token;
	}

	/**
	 * 校验Token
	 */
	public static Claims checkToken(String token) {
		try {
			final Claims claims = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token).getBody();
			return claims;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从Token中获取username
	 * @param token token
	 * @return username
	 */
	public static String getUsername(String token){
		Claims claims = checkToken(token);
		assert claims != null;
		return claims.get(USER_NAME_KEY).toString();
	}

	/**
	 * 从Token中获取用户角色
	 */
	public static String getUserRole(String token){
		Claims claims = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token).getBody();
		return claims.get(USER_ROLE_KEY).toString();
	}

	/**
	 * 校验Token是否过期
	 */
	public static boolean isExpiration(String token){
		Claims claims = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token).getBody();
		return claims.getExpiration().before(new Date());
	}
}
