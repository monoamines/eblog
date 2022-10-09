package com.teacherwl.eblog.common.exception;

import cn.hutool.json.JSONUtil;
import com.teacherwl.eblog.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GloablExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handler(HttpServletRequest request, HttpServletResponse response,Exception e) throws IOException {
        //ajax处理
        String header=  request.getHeader("X-Requested-With");
        if(header!=null&&"XMLHttpRequest".equals(header))
        {
            boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
            if(!authenticated)
            {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print(JSONUtil.toJsonStr(Result.fail("请先登录")));
            }
        }


            //web处理
            ModelAndView modelAndView=new ModelAndView("error");
            modelAndView.addObject("message",e.getMessage());
            return modelAndView;

    }
}
