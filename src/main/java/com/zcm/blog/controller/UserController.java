package com.zcm.blog.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 *
 * @author zhangcm
 * @date 2021-11-16 18:40:43
 */
@Api("用户管理接口")
@RestController
public class UserController {

	@Operation(summary = "用户详情接口")
	@GetMapping("/api/user/{userId}")
	@PreAuthorize("hasAnyRole('admin')")
	public String userDetail(@PathVariable String userId) {
		return userId;
	}
}
