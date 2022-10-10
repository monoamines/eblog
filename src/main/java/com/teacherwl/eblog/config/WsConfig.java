package com.teacherwl.eblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableAsync
@EnableWebSocketMessageBroker//表示开启使用s'tomp协议来传输基于代理的东西
public class WsConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/websocket")//表示注册一个端点 websocket访问地址
               .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
       registry.enableSimpleBroker("/user/","/topic/");
       registry.setApplicationDestinationPrefixes("/app");
    }
}
