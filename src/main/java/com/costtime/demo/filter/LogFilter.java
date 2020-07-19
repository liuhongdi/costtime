package com.costtime.demo.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

@Component
public class LogFilter implements Filter {
    private Set<String> excludesPattern;
    protected URLPatternMatcher pathMatcher = new URLPatternMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----------------log filter init");
        String param = filterConfig.getInitParameter("exclusions");
        if (param != null && param.trim().length() != 0) {
            this.excludesPattern = new HashSet(Arrays.asList(param.split("\\s*,\\s*")));
        }
    }
    //过滤功能,打印出request的info
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("----------------log filter doFilter begin");
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (this.isExclusion(request.getRequestURI())) {
            //属于排除的url
            System.out.println("===不执行过滤器功能");
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            //不属于排除，继续处理
            System.out.println("===执行过滤器功能");
            logRequest(request);
            filterChain.doFilter(servletRequest,servletResponse);
        }
        System.out.println("----------------log filter doFilter end");
    }

    @Override
    public void destroy() {
        System.out.println("----------------log filter destroy");
    }

    //打印request的信息
    private void logRequest(HttpServletRequest request) {
        System.out.println("log filter URL : " + request.getRequestURL().toString());
        System.out.println("log filter PARAM : " +  request.getQueryString());
        System.out.println("log filter HTTP_METHOD : " + request.getMethod());
        System.out.println("log filter IP : " + request.getRemoteAddr());
    }

    //判断是否被排除的url,
    //true,被排除,false:非被排除
    public boolean isExclusion(String requestURI) {
        if (this.excludesPattern == null) {
            return false;
        } else {
            Iterator i$ = this.excludesPattern.iterator();
            String pattern;
            do {
                if (!i$.hasNext()) {
                    return false;
                }
                pattern = (String)i$.next();
            } while(!this.pathMatcher.matches(pattern, requestURI));
            return true;
        }
    }
}
