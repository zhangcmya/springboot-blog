package com.zcm.blog.controller;

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
@RestController
public class UserController {

	@GetMapping("/api/user/{userId}")
	@PreAuthorize("hasAnyRole('admin')")
	public String userDetail(@PathVariable String userId) {
		return userId;
	}
}
