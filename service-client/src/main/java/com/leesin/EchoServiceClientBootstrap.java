package com.leesin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/17 0017 18:01
 * @modified By:
 */
@RestController
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients
public class EchoServiceClientBootstrap {

    @Qualifier("echoServiceClient")
    @Autowired
    private final EchoServiceClient echoServiceClient;

    @LoadBalanced
    private final RestTemplate restTemplate;

    public EchoServiceClientBootstrap(EchoServiceClient echoServiceClient, RestTemplate restTemplate) {
        this.echoServiceClient = echoServiceClient;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/call/echo/{message}")
    public String echo(@PathVariable String message) {
        return echoServiceClient.echo(message);
    }

    @LoadBalanced //负载均衡的策略
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(EchoServiceClientBootstrap.class, args);
    }
}
