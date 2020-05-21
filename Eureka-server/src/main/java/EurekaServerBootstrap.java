import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/16 0016 14:31
 * @modified By:
 */

public class EurekaServerBootstrap {

    @EnableAutoConfiguration
    @EnableEurekaServer
    public static class EurekaServerConfiguration {
    }

    public  static void main(String[] args) {
        SpringApplication.run(EurekaServerConfiguration.class, args);
    }
}
