package com.huaiyin.pytorch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huaiyin.pytorch.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{

}
