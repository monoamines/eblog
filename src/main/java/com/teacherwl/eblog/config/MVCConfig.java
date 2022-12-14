package com.teacherwl.eblog.config;

import com.teacherwl.eblog.common.lang.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig  implements WebMvcConfigurer {

    @Autowired
    Consts consts;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/avatar/**").addResourceLocations("file:///"+consts.getUploadDir()+"/avatar/");
    }
}
