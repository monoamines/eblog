package com.teacherwl.eblog.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teacherwl.eblog.entity.MCategory;
import com.teacherwl.eblog.service.MCategoryService;
import com.teacherwl.eblog.service.MPostService;
import com.teacherwl.eblog.service.impl.MCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Locale;

/**
 * 每次启动项目的时候预加载
 */
@Component
public class ContextStartup implements ApplicationRunner, ServletContextAware {
    @Autowired
    private  MCategoryService mCategoryService;

    @Autowired
    private MPostService mPostService;
 ServletContext servletContext;
    @Override
    public void run(ApplicationArguments args) throws Exception {
      List<MCategory> categories= mCategoryService.list(new QueryWrapper<MCategory>().eq("status",0));
      mPostService.initWeekRank();
        servletContext.setAttribute("categorys",categories);


    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }
}
