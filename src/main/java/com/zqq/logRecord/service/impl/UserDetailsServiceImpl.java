package com.zqq.logRecord.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.logRecord.mapper.UserDao;
import com.zqq.logRecord.pojo.LoginUser;
import com.zqq.logRecord.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/12 13:30
 * ModifyDate: 2023/12/12 13:30
 * Version: 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getName,username);
        User user = userDao.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }
        return new LoginUser(user);
    }
}
