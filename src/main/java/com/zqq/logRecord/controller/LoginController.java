package com.zqq.logRecord.controller;

import com.zqq.logRecord.pojo.User;
import com.zqq.logRecord.service.LoginService;
import com.zqq.logRecord.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/12 14:53
 * ModifyDate: 2023/12/12 14:53
 * Version: 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/info")
    public CommonResult login(@RequestBody User user){
       return loginService.login(user);
    }

}
