package com.zcm.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigInteger;
import java.util.List;
import lombok.Data;

/**
 * 用户实体类
 *
 * @author zhangcm
 * @date 2021-10-27 19:50:25
 */
@Data
@TableName("user")
public class UserEntity {

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@TableField("username")
	private String username;

	@TableField("password")
	private String password;
}
