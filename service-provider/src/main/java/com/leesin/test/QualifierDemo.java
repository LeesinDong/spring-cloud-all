// package com.leesin.test;
//
// import javafx.application.Application;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.ApplicationRunner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.WebApplicationType;
// import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.builder.SpringApplicationBuilder;
// import org.springframework.context.annotation.Bean;
//
// import java.lang.annotation.*;
// import java.util.Collection;
// import java.util.Collections;
// import java.util.Map;
//
// /**
//  * @description:
//  * @author: Leesin Dong
//  * @date: Created in 2020/5/20 0020 10:49
//  * @modified By:
//  */
// @EnableAutoConfiguration
// public class QualifierDemo {
//
//     @Autowired
//     //把所有的bean拿出来，key是bean的名称，value是值 内容
//     private Map<String,String> allStringBeans = Collections.emptyMap();
//
//     //通过@Qualifier value() 属性来以来依赖查找
//     @Autowired
//     @Qualifier("a")
//     private String aBean;
//
//     @Autowired
//     @Qualifier("b")
//     private String bBean;
//
//     @Autowired
//     @Qualifier("c")
//     private String cBean;
//
//     //不通过@Qualifier value() 属性来以来查找
//     @Autowired
//     // @Qualifier
//     /*
//     * 这里相当于  @Qualifier
//     * b 和c 标注了  @Qualifier
//     * @Qualifier 没有value的话，相当于按照 标注了@Qualifier的进行分组，只取有@Qualifier 的
//     * */
//     @Group
//     private Map<String, String> groupStringBeans = Collections.emptyMap();
//
//     @Bean
//     public ApplicationRunner runner() {
//         return args -> {
//             System.out.println("allStringBeans:"+allStringBeans);
//
//             System.out.println("aBean："+aBean);
//             System.out.println("bBean："+bBean);
//             System.out.println("cBean："+cBean);
//
//             System.out.println("allStringBeans:"+groupStringBeans);
//         };
//     }
//
//     @Bean
//     public String a() {
//         return "String-a";
//     }
//
//     //b and c分组 -> @Qualifer
//     @Bean
//     // @Qualifier
//     @Group
//     public String b() {
//         return "String-b";
//     }
//
//     @Bean
//     // @Qualifier
//     @Group
//     public String c() {
//         return "String-c";
//     }
//
//     public static void main(String[] args) {
//         new SpringApplicationBuilder(QualifierDemo.class).web(WebApplicationType.NONE).run();
//         // SpringApplication.run(QualifierDemo.class, args);
//     }
// }
//
//
// //自定义注解-元注解@Qualifier
// @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
// @Retention(RetentionPolicy.RUNTIME)
// @Inherited
// @Documented
// @Qualifier
// @interface Group {
//
// }
//
