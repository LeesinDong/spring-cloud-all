package com.leesin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    private final EchoServiceClient echoServiceClient;

    public EchoServiceClientBootstrap(EchoServiceClient echoServiceClient) {
        this.echoServiceClient = echoServiceClient;
    }

    @GetMapping("/call/echo/{message}")
    public String echo(@PathVariable String message) {
        return echoServiceClient.echo(message);
    }

    public static void main(String[] args) {
        SpringApplication.run(EchoServiceClientBootstrap.class, args);
    }
}
