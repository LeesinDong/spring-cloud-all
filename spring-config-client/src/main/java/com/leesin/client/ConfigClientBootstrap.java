package com.leesin.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/16 0016 16:11
 * @modified By:
 */
@EnableAutoConfiguration
//激活服务发现客户端
@EnableDiscoveryClient

@RestController
public class ConfigClientBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientBootstrap.class, args);
    }
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/services")
    //因为服务的名称是不能重复的，所以这里用set
    public Set<String> getServices() {
        // 面试题 ： 为什么用这个？ 保证顺序，且去重 输入的是123 还是123 返回
        return new LinkedHashSet<>(discoveryClient.getServices());
    }

    /*
     * @PathParam JAX-RS 标砖Java REST注解
     * @PathVariable spring web mvc的
     * */
    @GetMapping("/services/{serviceName}")
    public List<ServiceInstance> getServiceInstances(@PathVariable String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }

    @GetMapping("/services/{serviceName}/{instanceId}")
    public ServiceInstance getServiceInstance(@PathVariable String serviceName, @PathVariable String instanceId) {
        return getServiceInstances(serviceName)
                .stream()
                .filter(serviceInstance -> instanceId.equals(serviceInstance.getInstanceId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no such service instance"));
    }
}
