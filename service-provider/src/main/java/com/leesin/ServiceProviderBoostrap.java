package com.leesin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
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

@EnableBinding(Sink.class)
public class ServiceProviderBoostrap {
    @StreamListener(Sink.INPUT)
    public void     listen(byte[] data) {
        System.out.println(new String(data));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderBoostrap.class, args);
    }
}
