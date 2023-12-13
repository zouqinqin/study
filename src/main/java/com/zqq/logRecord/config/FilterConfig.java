package com.zqq.logRecord.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/11 12:03
 * ModifyDate: 2023/12/11 12:03
 * Version: 1.0
 */
//@Configuration
//public class FilterConfig {
//    @Bean
//    public FilterRegistrationBean registrationBean(){
//        FilterRegistrationBean<Myfilters> bean = new FilterRegistrationBean<>();
//        bean.setOrder(1);
//        bean.setFilter(new Myfilters());
//        bean.addUrlPatterns("/study/user/*");
//        return bean;
//    }
//
//    //@Bean
//    //public FilterRegistrationBean registrationMyFilter(){
//    //    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//    //     bean.setOrder(2);
//    //     bean.setFilter(new MyAnotherfilters());
//    //     bean.addUrlPatterns("/*");
//    //     return bean;
//    //}
//
//}
