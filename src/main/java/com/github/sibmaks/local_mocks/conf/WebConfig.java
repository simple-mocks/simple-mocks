package com.github.sibmaks.local_mocks.conf;

import com.github.sibmaks.local_mocks.logger.ApiLoggerFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@EnableWebMvc
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<ApiLoggerFilter> apiLoggingFilter(){
        var registrationBean = new FilterRegistrationBean<ApiLoggerFilter>();

        registrationBean.setFilter(new ApiLoggerFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
