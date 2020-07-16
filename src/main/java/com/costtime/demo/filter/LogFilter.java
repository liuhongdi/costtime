package com.costtime.demo.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(2)
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----------------log filter init");
    }
    //过滤功能,打印出request的info
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("----------------log filter doFilter begin");
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        System.out.println("log filter URL : " + request.getRequestURL().toString());
        System.out.println("log filter PARAM : " +  request.getQueryString());
        System.out.println("log filter HTTP_METHOD : " + request.getMethod());
        System.out.println("log filter IP : " + request.getRemoteAddr());

        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("----------------log filter doFilter end");
    }

    @Override
    public void destroy() {
        System.out.println("----------------log filter destroy");
    }
}
