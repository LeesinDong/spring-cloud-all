package com.leesin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@SpringBootApplication //具备当前所在包的扫描功能

// @EnableBinding({Source.class, PersonSource.class,PersonSink.class})
public class EchoServiceClientBootstrap {

    private final CsrfTokenRepository csrfTokenRepository;

    public EchoServiceClientBootstrap(CsrfTokenRepository csrfTokenRepository) {

        this.csrfTokenRepository = csrfTokenRepository;
    }

    @GetMapping("/csrf/token")
    public CsrfToken csrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
        if (csrfToken == null) {
            csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken,request,response);
        }
        return csrfToken;
    }

  // kafka相关，除了main方法其他的都注释了

    /*private final Source source;

    private final PersonSource personSource;
    private final PersonSink personSink;

    // @Qualifier("echoServiceClient")
    @Autowired
    private final EchoServiceClient echoServiceClient;

    // @LoadBalanced
    // private final RestTemplate restTemplate;

    private KafkaTemplate<String, Object> kafkaTemplate;

    public EchoServiceClientBootstrap(Source source, PersonSource personSource, com.leesin.PersonSink personSink, EchoServiceClient echoServiceClient,
                                      // RestTemplate restTemplate,
                                      KafkaTemplate<String, Object> kafkaTemplate) {
        this.source = source;
        this.personSource = personSource;
        this.personSink = personSink;
        this.echoServiceClient = echoServiceClient;
        // this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }


    //    发送kafka消息
    @GetMapping("/person")
    public Person person(String name) {
        Person person = createPerson(name);
        kafkaTemplate.send("gupao", person);
        return person;
    }

    @GetMapping("/stream/person")
    public Person streamperson(String name) {
        Person person = createPerson(name);
        MessageChannel messageChannel = source.output();
        messageChannel.send(MessageBuilder.withPayload(person).build());
        return person;
    }

    @GetMapping("/stream/person/source")
    public Person streampersonSource(String name) {
        Person person = createPerson(name);
        MessageChannel messageChannel = personSource.output();
        MessageBuilder messageBuilder = MessageBuilder.withPayload(person).setHeader("Content-Type", "java/pojo");
        messageChannel.send(messageBuilder.build());
        return person;
    }





    public Person createPerson(String name) {
        Person person = new Person();
        person.setId(System.currentTimeMillis());
        person.setName(name);
        return person;
    }


    *//*
     * 读kafka消息
     * *//*
    @KafkaListener(topics = "gupao")
    public void Listen(Person person) {
        System.out.println(person);
    }
    //重要注意事项：
    // 1 尽管springcloud strean binder中存在kafka的整合，然而，spring kafka和spring cloud stream kafka在
    //处理数据生产和消费是存在差异，因此不要混用
    //2 当spring cloud stream 发送消息办函头信息是，kafka deSerializer实现方法回调时不予以处理
    //3 通常业务逻辑可以使用@Streamlistener来监听数据（主体、载体），如果需要更多头信息，需要subscriableChannel
    *//*
    * 通过注解方式监听数据
    * *//*
    @StreamListener("person-source")  //指定channel名称
    public void listenFromStream(Person person) {
        System.out.println(person);
    }


    //通过spring message API的方式监听数据
    //启动完之后会有这样的回调，记住就好了
    @Bean
    public ApplicationRunner runner() {
        return args -> {
            personSink.channel().subscribe(new MessageHandler() { //通过spring Message API实现的
                @Override
                //订阅一个数据
                public void handleMessage(Message<?> message) throws MessagingException {
                    MessageHeaders headers = message.getHeaders();
                    String contentType = headers.get("Content-Type", String.class);
                    Object object = message.getPayload();
                    System.out.printf("说到消息[主体：%s ,消息头：%s \n]", object, headers);
                }
            });
        };
    }


    @GetMapping("/call/echo/{message}")
    public String echo(@PathVariable String message) {
        return echoServiceClient.echo(message);
    }


    // @LoadBalanced //负载均衡的策略
    // @Bean
    // public RestTemplate restTemplate() {
    //     return new RestTemplate();
    // }*/

    public static void main(String[] args) {
        SpringApplication.run(EchoServiceClientBootstrap.class, args);
    }
}
