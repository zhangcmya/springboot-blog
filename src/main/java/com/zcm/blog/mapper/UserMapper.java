package com.zcm.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcm.blog.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息
 *
 * @author zhangcm
 * @date 2021-10-27 19:50:00
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
