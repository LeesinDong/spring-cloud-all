package com.leesin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/17 0017 16:25
 * @modified By:
 */
//加载springfactorys 下面的定义的类
@EnableAutoConfiguration
//激活服务发现客户端
@EnableDiscoveryClient
@ComponentScan

@EnableCircuitBreaker

@EnableAspectJAutoProxy
//先把kafka去掉  ，不然报错
// @EnableBinding(Sink.class)
public class ServiceProviderBoostrap {
    @StreamListener(Sink.INPUT)
    public void     listen(byte[] data) {
        System.out.println(new String(data));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderBoostrap.class, args);
    }

    @Bean
    public RouteLocator customeRouteLocator(RouteLocatorBuilder builder) {
        builder.routes()
                .route("world",
                        r -> r.path("/w").
                                uri("http://127.0.0.1:8080/world"))
                .build();
    }
}
