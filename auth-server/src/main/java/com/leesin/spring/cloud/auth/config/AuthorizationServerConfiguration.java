package com.leesin.spring.cloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.concurrent.TimeUnit;

/**
 * 授权配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    //构造器注入
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,
                                            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer configurer) throws Exception {
    }

    // 通过 POST 方法访问 /oauth/token
    // 需要 BASIC AUTH -> clientId 和 secret 作为获取 Access Token 的一部分
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("zsxq") // ClientId = "zsxq"
                .secret(passwordEncoder.encode("123456"))//密钥
                .authorizedGrantTypes("password")  // 授权类型密码 - "password"
                .scopes("read", "write", "trust")
                .accessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(60)); // access token 有效时间
        ;
    }

    @Override
    //所有认证的服务都是对外的一个 外部的服务端点
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //保存
        endpoints.tokenStore(tokenStore())
                //authenticationManager 在别的地方声明的
                .authenticationManager(authenticationManager)
        ;
    }


    @Bean
    public TokenStore tokenStore() {
        //保存到单机
        return new InMemoryTokenStore(); // 内存型（单机） Token存储实现
    }


}
