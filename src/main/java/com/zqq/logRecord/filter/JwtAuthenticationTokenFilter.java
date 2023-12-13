package com.zqq.logRecord.filter;

import com.zqq.logRecord.pojo.LoginUser;
import com.zqq.logRecord.utils.JWTUtils;
import com.zqq.logRecord.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/12 16:10
 * ModifyDate: 2023/12/12 16:10
 * Version: 1.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }

        String userId = null;
        try {
            //解析token获取其中的userId
            Claims claims = jwtUtils.parseToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:"+userId;
        LoginUser loginUser = redisUtil.get(redisKey);
        if(Objects.isNull(loginUser)){
           throw new RuntimeException("用户未登陆！");
        }
        //存入SecurityContextHolder中
        //TODO 获取权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行请求
        filterChain.doFilter(request,response);
    }
}
