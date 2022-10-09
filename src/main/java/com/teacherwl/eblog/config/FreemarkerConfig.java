package com.teacherwl.eblog.config;


import com.jagregory.shiro.freemarker.ShiroTags;
import com.teacherwl.eblog.template.PostsTemplate;
import com.teacherwl.eblog.template.RankTemplate;
import com.teacherwl.eblog.template.TimeAgoMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class FreemarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;
    @Autowired
    PostsTemplate postsTemplate;

    @Autowired
    RankTemplate rankTemplate;



//前端使用的freemarker标签
    @PostConstruct
    public void setUp() {
        configuration.setSharedVariable("timeAgo", new TimeAgoMethod());
        configuration.setSharedVariable("posts",postsTemplate);//前端得到
        configuration.setSharedVariable("hots",rankTemplate);
        configuration.setSharedVariable("shiro",new ShiroTags());
    }

}
