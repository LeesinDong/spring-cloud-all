package com.leesin;

import com.leesin.annotation.Limited;
import com.leesin.annotation.Timeouted;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.*;

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

    // private ExecutorService executorService = Executors.newFixedThreadPool(2);


    @Limited(10)           // 限流
    @GetMapping("/world")
    public String world() throws InterruptedException {
        await();
        return "world";
    }


    //L long类型
    @Timeouted(value = 50L, fallback = "fallbackHello")
    @GetMapping("/hello")
    public String hello() throws InterruptedException, ExecutionException, TimeoutException {
        // Future<String> future = executorService.submit(this::hello);
        // //固定50ms，把hello执行完
        // return future.get(50,TimeUnit.MILLISECONDS);
        return doHello();
    }

    private String doHello() throws InterruptedException {
        await();
        return "Hello";
    }

    public String fallbackHello() {
        return "fallback";
    }

    @Limited(1)
    //                  数组
    @HystrixCommand(
            fallbackMethod = "fallback",
            //这个必须配置
            threadPoolKey = "test",
            commandProperties = {
                    //    不配置的话，默认是timeout线程池的方式
                    //    还有SEMAPHORE 信号量 策略
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    //                                                                                  默认1s ，这里50ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50"),
                    //    还有补偿方法，fallback，什么是补偿方法，通常有两种方式，一种是补偿，一种是抛异常， 抛异常说明错了，补偿是兜底
                    //                比如宝贝收藏、广告业、需要关闭怎么做?如果qps压力很大，就可以替换一张图片，
            })
    @GetMapping(value = "/echo/{message}")
    public String echo(@PathVariable String message) throws InterruptedException {
        //等若干秒再请求
        await();
        return "[ECHO" + getPort() + "]" + message;
    }

    //服务降级
    public String fallback(String abc) {
        return "fallback - " + abc;
    }


    private final Random random = new Random();

    private void await() throws InterruptedException {
        long wait = random.nextInt(100);
        //这里用printf 不然报错
        System.out.printf("[当前线程： %s ] 当前方法执行（模型）消耗 %d 毫秒 \n", Thread.currentThread().getName(), wait);
        Thread.sleep(wait);
    }

}
