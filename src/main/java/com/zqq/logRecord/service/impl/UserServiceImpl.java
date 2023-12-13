package com.zqq.logRecord.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqq.logRecord.mapper.UserDao;
import com.zqq.logRecord.pojo.User;
import com.zqq.logRecord.service.UserService;
import com.zqq.logRecord.utils.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    UserDao userDao;
    @Override
    public R addUser(User user) {
        int count = userDao.insert(user);
        return R.ok().put("count",count);
    }

    @Override
    public R getUserById(Integer id) {
        User user = userDao.selectById(id);
        return R.ok().put("user",user);
    }
}
