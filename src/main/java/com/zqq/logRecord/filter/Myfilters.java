package com.zqq.logRecord.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.LogRecord;

/**
 * TODO
 * <p>
 * Author: zouqinqin
 * Date: 2023/12/11 11:43
 * ModifyDate: 2023/12/11 11:43
 * Version: 1.0
 */
//@Component
//@Order(1)
//@WebFilter(urlPatterns = "/*")
//public class Myfilters implements Filter {
//
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("myfilter");
//        //放行请求
//        filterChain.doFilter(servletRequest,servletResponse);
//    }
//}
