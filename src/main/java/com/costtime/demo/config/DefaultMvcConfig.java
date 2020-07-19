package com.costtime.demo.config;


import com.costtime.demo.filter.LogFilter;
import com.costtime.demo.filter.TimeFilter;
import com.costtime.demo.interceptor.CostTimeInterceptor;
import com.costtime.demo.interceptor.LogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.Filter;

/**
 * @desc: mvc config
 * @author: liuhongdi
 * @date: 2020-07-01 11:40
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DefaultMvcConfig implements WebMvcConfigurer {

    @Resource
    private LogInterceptor logInterceptor;
    @Resource
    private CostTimeInterceptor costTimeInterceptor;


    //@Resource
    //private LogFilter logFilter;
    //@Resource
    //private TimeFilter timeFilter;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/home/home");
    }

    //添加Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //1.加入的顺序就是拦截器执行的顺序，
        //2.按顺序执行所有拦截器的preHandle
        //3.所有的preHandle 执行完再执行全部postHandle 最后是postHandle


        registry.addInterceptor(costTimeInterceptor)
                .addPathPatterns("/home/home**")
                .excludePathPatterns("/html/*","/js/*");

        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/home/home**")
                .excludePathPatterns("/html/*","/js/*");


    }

    //add filter
     //registration.addUrlPatterns("/icons/*", "/style/*", "/script/*", "/dwr/*", "/icons/*", "/coverArt.view", "/avatar.view");
    //registration.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
    @Bean
    public FilterRegistrationBean addTimeFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new TimeFilter());
        registration.setName("timeFilter");
        registration.setOrder(2);  //请求中过滤器执行的先后顺序，值越小越先执行
        registration.addUrlPatterns("/home/*","/abc/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean addLogFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LogFilter());
        registration.setName("logFilter");
        registration.setOrder(1);  //请求中过滤器执行的先后顺序，值越小越先执行
        registration.addUrlPatterns("/*");
        registration.addInitParameter("exclusions","/js/*,/images/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
        return registration;
    }
}