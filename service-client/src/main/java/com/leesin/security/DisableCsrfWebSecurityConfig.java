package com.leesin.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

//@EnableWebSecurity // Spring Boot 场景不需要重新标注
@Configuration
@Order(HIGHEST_PRECEDENCE)
public class DisableCsrfWebSecurityConfig implements WebMvcConfigurer, IWebSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();
        ;
    }
}
