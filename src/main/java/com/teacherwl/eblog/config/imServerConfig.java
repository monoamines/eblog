package com.teacherwl.eblog.config;

import com.teacherwl.eblog.im.handler.MsgHandlerFactory;
import com.teacherwl.eblog.im.ImServerStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class imServerConfig {
    @Value("${im.server.port}")
    private  int  imPort;
    @Bean
    ImServerStarter imServerStarter()  {
        try {
            ImServerStarter starter=new ImServerStarter(imPort);
            starter.start();
            MsgHandlerFactory.init();
            return starter;
        } catch (IOException e) {
            log.error("tio server 启动失败");
        }

        return null;
    }


}
