package com.leesin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/17 0017 18:03
 * @modified By:
 */

@FeignClient("service-provider")
public interface EchoServiceClient {

    @GetMapping(value = "/echo/{message}")
    String echo(@PathVariable String message);
}
