package com.teacherwl.eblog.shiro;

import cn.hutool.json.JSONUtil;
import com.teacherwl.eblog.common.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthFilter  extends UserFilter {

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {

        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
      String header=  httpServletRequest.getHeader("X-Requested-With");
    if(header!=null&&"XMLHttpRequest".equals(header))
    {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        if(!authenticated)
        {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JSONUtil.toJsonStr(Result.fail("请先登录")));
        }

    }
        super.redirectToLogin(request,response);
    }
}
