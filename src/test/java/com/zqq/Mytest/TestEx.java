package com.zqq.Mytest;

import com.zqq.logRecord.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/12 14:11
 * ModifyDate: 2023/12/12 14:11
 * Version: 1.0
 */
@SpringBootTest
public class TestEx {
    @Autowired
    private JWTUtils jwtUtils;
    @Test
    public void TestBcrypPassWordEnCode(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("1213");
        System.out.println(encode);

    }
    @Test
    public void TestPast(){
        Claims claims = jwtUtils.parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzAyMzY3Mjk2LCJleHAiOjE3MDI5NzIwOTZ9.O-n1MRNBx0uhRYYZsnl6mGqYXzzwivx_M6t-YHIfC_c");
        System.out.println(claims.getSubject());
    }

}
