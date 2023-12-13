package com.zqq.logRecord.controller;

import com.zqq.logRecord.pojo.User;
import com.zqq.logRecord.service.UserService;
import com.zqq.logRecord.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public R addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    //通过用户id查询用户信息
    @RequestMapping("/get")
    public R getAllUser(@RequestParam Integer id){
       return userService.getUserById(id);
    }

    @GetMapping("/hello")
    public String test(){
       return "hello security";
    }

}
