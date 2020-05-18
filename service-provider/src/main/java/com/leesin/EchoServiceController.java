package com.leesin;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/17 0017 16:22
 * @modified By:
 */
@RestController
public class EchoServiceController {

    //结论：外部化配置其实有点不靠谱，它并非完全静态，也不一定即时返回
    private final Environment environment;

    public EchoServiceController(Environment environment) {
        this.environment = environment;
    }

    private String getPort() {
        return environment.getProperty("local.server.port");
    }

    @GetMapping(value="/echo/{message}")
    public String echo(@PathVariable String message) {
        return "[ECHO"+getPort()+"]" + message;
    }
}
