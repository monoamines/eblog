package com.teacherwl.eblog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.service.MCommentService;
import com.teacherwl.eblog.service.MPostService;
import com.teacherwl.eblog.service.MUserMessageService;
import com.teacherwl.eblog.service.SearchService;
import com.teacherwl.eblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BaseController {
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    MPostService mPostService;

    @Autowired
    SearchService searchService;

    @Autowired
    MUserMessageService mUserMessageService;

    @Autowired
    MCommentService mCommentService;

    protected Page getPage()
    {
        int pn= ServletRequestUtils.getIntParameter(httpServletRequest,"pn",1);
        int size= ServletRequestUtils.getIntParameter(httpServletRequest,"size",3);
        Page pages=new Page(pn,size);
        return  pages;
    }
    protected AccountProfile getProfile()
    {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getProfileId()
    {
        return getProfile().getId();
    }
}
