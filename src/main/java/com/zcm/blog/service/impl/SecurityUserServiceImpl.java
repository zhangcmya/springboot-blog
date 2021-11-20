package com.zcm.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcm.blog.entity.RoleEntity;
import com.zcm.blog.entity.UserEntity;
import com.zcm.blog.mapper.RoleMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security的UserDetailsService实现类，具体实现通过db查询用户名密码和对应权限
 *
 * @author zhangcm
 * @date 2021-11-17 19:11:12
 */
@Service
public class SecurityUserServiceImpl extends ServiceImpl<BaseMapper<UserEntity>, UserEntity> implements UserDetailsService {

	@Autowired
	private RoleMapper roleMapper;

	/**
	 * 通过用户名获取用户密码和权限
	 *
	 * @param username 用户名
	 * @return UserDetails class
	 * @throws UsernameNotFoundException e
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("用户名不能问题");
		}
		QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
		userEntityQueryWrapper.eq("username", username);
		UserEntity userEntity = this.getOne(userEntityQueryWrapper);
		if (ObjectUtils.isEmpty(userEntity)) {
			throw new UsernameNotFoundException("用户名密码错误");
		}
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id", userEntity.getId());
		List<RoleEntity> roleEntities = this.roleMapper.selectList(wrapper);
		roleEntities.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		});
		User user = new User(username, userEntity.getPassword(), authorities);
		return user;
	}


}
