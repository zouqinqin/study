package com.zqq.logRecord.service;

import com.zqq.logRecord.pojo.User;
import com.zqq.logRecord.utils.CommonResult;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/12 14:54
 * ModifyDate: 2023/12/12 14:54
 * Version: 1.0
 */
public interface LoginService {

    CommonResult login(User user);
}
