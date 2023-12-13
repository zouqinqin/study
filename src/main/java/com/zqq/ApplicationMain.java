package com.zqq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2022/9/5 21:31
 * ModifyDate: 2022/9/5 21:31
 * Version: 1.0
 */
@SpringBootApplication
@MapperScan("com.zqq.logRecord.mapper")
//@ServletComponentScan("com.zqq.logRecord.filter")
public class ApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class,args);
    }

}
