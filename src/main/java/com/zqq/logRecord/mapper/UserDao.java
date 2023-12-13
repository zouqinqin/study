package com.zqq.logRecord.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zqq.logRecord.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
