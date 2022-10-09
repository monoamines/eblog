package com.teacherwl.eblog.controller;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.code.kaptcha.Producer;
import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.entity.MUser;
import com.teacherwl.eblog.service.MUserService;
import com.teacherwl.eblog.shiro.AccountProfile;
import com.teacherwl.eblog.util.ValidationUtil;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class AuthController  extends BaseController {

    private  static  final String KAPTHCA_KEY_NAME="KAPTHCA_KEY_NAME";
    @Autowired
    Producer producer;
    @Autowired
    MUserService mUserService;
    @GetMapping("/register")
    public String register()
    {
            return "/auth/reg";
    }


    @GetMapping ("/login")
    public String login()
    {
        return "/auth/login";
    }

    @ResponseBody
    @RequestMapping ("/dologin")
    public Result dologin(String email,String password)
    {
        UsernamePasswordToken token=new UsernamePasswordToken(email,password);
        if(StrUtil.isEmpty(email)||StrUtil.isBlank(password))
        {
            return  Result.fail("邮箱或者密码不能为空");
        }


        try {
            SecurityUtils.getSubject().login(token);

        }
        catch (AuthenticationException e)
        {
            if(e instanceof UnknownAccountException)
            {
                return Result.fail("email不存在");
            }
            else if (e instanceof LockedAccountException )
            {
                return Result.fail("用户被禁用");
            }
            else if(e instanceof IncorrectCredentialsException )
            {
                return Result.fail("密码错误");
            }
            else
            {
                return Result.fail("用户认证失败");
            }


        }
        return Result.success().action("/");
    }

    @GetMapping("/capthca.jpg")
    public void kaptcha(HttpServletResponse response) throws IOException {
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control","no-store,no-cache");
        System.out.println( text);
        httpServletRequest.getSession().setAttribute(KAPTHCA_KEY_NAME,text);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image,"jpg",outputStream);
    }

    @ResponseBody
    @RequestMapping("/doregister")
    public Result doRegister(MUser user,String repass,String vercode)
    {
      String kapthca= (String)httpServletRequest.getSession().getAttribute(KAPTHCA_KEY_NAME);
      //后端对前端传回的数据进行校验
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(user);
        if (validResult.hasErrors())
        {
            return  Result.fail(validResult.getErrors());
        }
        if(!user.getPassword().equals(repass))
        {
            return Result.fail("repass error");
        }

        if(!vercode.equals(kapthca))
        {
            return  Result.fail("vercode error");
        }
        System.out.println(kapthca);
        Result result=  mUserService.register(user);
        return  result.action("/login");
    }

    @RequestMapping ("/user/logout")
    public String loginout()
    {
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
}
