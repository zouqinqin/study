package com.zqq.logRecord.service.impl;

import com.zqq.logRecord.pojo.LoginUser;
import com.zqq.logRecord.pojo.User;
import com.zqq.logRecord.service.LoginService;
import com.zqq.logRecord.utils.CommonResult;
import com.zqq.logRecord.utils.JWTUtils;
import com.zqq.logRecord.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/12 14:54
 * ModifyDate: 2023/12/12 14:54
 * Version: 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public CommonResult login(User user) {
        //AuthenticationManager authenticate 进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassWord());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登陆失败");
        }
         //认证通过了，使用userid生成易jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = jwtUtils.createToken(userId);
        Map<String, String> map = new HashMap<>();
        map.put("token",jwt);
        redisUtil.set("login:"+userId,loginUser);
        return CommonResult.success(200,"登陆成功",map);
    }
}
