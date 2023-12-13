package com.zqq.logRecord.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqq.logRecord.pojo.User;
import com.zqq.logRecord.utils.R;

public interface UserService extends IService<User> {
    R addUser(User user);

    R getUserById(Integer id);
}
