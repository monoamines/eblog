package com.teacherwl.eblog.config;

import cn.hutool.core.map.MapUtil;

import com.teacherwl.eblog.shiro.AccountRealm;
import com.teacherwl.eblog.shiro.AuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ShiroConfig {

    @Bean
    public SecurityManager securityManager(AccountRealm accountRealm){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(accountRealm);

        log.info("------------------>securityManager注入成功");

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url和登录成功的url
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/user/center");
        // 配置未授权跳转页面
        filterFactoryBean.setUnauthorizedUrl("/error/403");
        //配置自定义认证拦截器
            filterFactoryBean.setFilters(MapUtil.of("authc",new AuthFilter()));

        Map<String, String> hashMap = new LinkedHashMap<>();

//        hashMap.put("/res/**", "anon");
//
//        hashMap.put("/user/home", "auth");
//        hashMap.put("/user/set", "auth");
//        hashMap.put("/user/upload", "auth");
//        hashMap.put("/user/index", "auth");
//        hashMap.put("/user/public", "auth");
//        hashMap.put("/user/collection", "auth");
//        hashMap.put("/user/mess", "auth");
//        hashMap.put("/msg/remove/", "auth");
//        hashMap.put("/message/nums/", "auth");
//
//        hashMap.put("/collection/remove/", "auth");
//        hashMap.put("/collection/find/", "auth");
//        hashMap.put("/collection/add/", "auth");
//
//        hashMap.put("/post/edit", "auth");
//        hashMap.put("/post/submit", "auth");
//        hashMap.put("/post/delete", "auth");
//        hashMap.put("/post/reply/", "auth");
//
//        hashMap.put("/websocket", "anon");
        hashMap.put("/login", "anon");
        hashMap.put("/user/home","authc");//告诉shiro需要登录
        hashMap.put("/user/set","authc");//告诉shiro需要登录
        hashMap.put("/user/public","authc");
        hashMap.put("/user/index","authc");
        hashMap.put("/user/upload","authc");
        hashMap.put("/collection/add/","authc");
        hashMap.put("/collection/remove/","authc");
        hashMap.put("/collection/find/","authc");
        hashMap.put("/post/edit","authc");
        hashMap.put("/post/submit","authc");
        filterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return filterFactoryBean;

    }

    @Bean
    public AuthFilter authFilter()
    {
        return new AuthFilter();
    }


}
