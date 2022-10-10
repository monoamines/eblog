package com.teacherwl.eblog.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.entity.MPost;
import com.teacherwl.eblog.entity.MUser;
import com.teacherwl.eblog.entity.MUserMessage;
import com.teacherwl.eblog.service.MPostService;
import com.teacherwl.eblog.service.MUserService;
import com.teacherwl.eblog.shiro.AccountProfile;
import com.teacherwl.eblog.util.UploadUtil;
import com.teacherwl.eblog.vo.UserMessageVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Controller
public class MUserController extends BaseController {

    @Autowired
    private MUserService mUserService;

    @Autowired
    private UploadUtil uploadUtil;
    @Autowired
    private MPostService mPostService;
    @GetMapping("/user/home")
    public String home()
    {
        MUser byId = mUserService.getById(getProfileId());
        //通过用户id获取用户七天内发表过的文章
       List<MPost> posts= mPostService.list(new QueryWrapper<MPost>().eq("user_id",getProfileId()).ge("created", DateUtil.offsetDay(new Date(),-7)).orderByDesc("created"));
        httpServletRequest.setAttribute("user",byId);
        httpServletRequest.setAttribute("posts",posts);
        return "/user/home";
    }


    @GetMapping("/user/set")
    public String set()
    {
        MUser user = mUserService.getById(getProfileId());
        httpServletRequest.setAttribute("user",user);
        return "/user/set";
    }

    @GetMapping("/user/index")
    public String index()
    {

        return "/user/index";
    }

    @GetMapping("/user/mess")
    public String mess()
    {
        System.out.println("hello bitch");
     IPage<UserMessageVo> page=  mUserMessageService.paging(getPage(),new QueryWrapper<MUserMessage>().eq("to_user_id",getProfileId()));
       httpServletRequest.setAttribute("pageData",page);
            List<UserMessageVo> mess=   page.getRecords();
        List<Long> ids = mess.stream().filter((messageVo) -> messageVo.getStatus() == 0).map(UserMessageVo::getId).collect(Collectors.toList());

        //批量修改成已读
        mUserMessageService.updateToReaded(ids);
       //将消息改为已读
        return "/user/mess";

    }

    @PostMapping("/user/set")
    @ResponseBody
    public Result doset(MUser user)
    {

        //修改头像与修改个人信息公用逻辑
        if(StrUtil.isNotBlank(user.getAvatar()))
        {
            MUser temp1 = mUserService.getById(getProfileId());
            temp1.setAvatar(user.getAvatar());
            mUserService.updateById(temp1);
            AccountProfile profile1 = getProfile();
            profile1.setAvatar(user.getAvatar());
            SecurityUtils.getSubject().getSession().setAttribute("profile",profile1);
            return Result.success().action("/user/set#avatar");
        }

        if(StrUtil.isBlank(user.getUsername()))
        {
            return Result.fail("用户名不能为空");
        }
        int count = mUserService.count(new QueryWrapper<MUser>().eq("username", user.getUsername()).ne("id", getProfileId()));
        if(count!=0)
        {
        return Result.fail("用户名已经存在");
        }
        MUser temp = mUserService.getById(getProfileId());

        temp.setUsername(user.getUsername());
        temp.setGender(user.getGender());
        temp.setSign(user.getSign());
        mUserService.updateById(temp);
        //更新shiro中的信息,实时同步页面的用户信息
        AccountProfile profile = getProfile();
        profile.setUsername(temp.getUsername());
        profile.setSign(temp.getSign());
        SecurityUtils.getSubject().getSession().setAttribute("profile",profile);
        return Result.success().action("/user/set#info");
    }

   @ResponseBody
   @RequestMapping("/user/upload")
   public Result upoloadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return uploadUtil.upload(UploadUtil.type_avatar,file);
  }


  @ResponseBody
  @RequestMapping("/user/repass")
  public Result repass(String currPassword,String newPassword,String verifyPassword)
  {
      MUser byId = mUserService.getById(getProfileId());
      if(!SecureUtil.md5(currPassword).equals(byId.getPassword()))
      {
          return Result.fail("密码不正确");
      }

      if(!newPassword.equals(verifyPassword))
      {
          return Result.fail("两次输入新密码不一致");
      }
      byId.setPassword(SecureUtil.md5(newPassword));
      mUserService.updateById(byId);
      SecurityUtils.getSubject().logout();
      return Result.success().action("/user/set#repass");
  }

    /**
     * 用户主页中，已经发布的文章
     * @return
     */
    @ResponseBody
    @GetMapping("/user/public")
    public  Result userPublic()
    {
       IPage<MPost> page= mPostService.page(getPage(),new QueryWrapper<MPost>().eq("user_id",getProfileId()).orderByDesc("created"));
        return Result.success(page);
    }

    @ResponseBody
    @GetMapping("/user/collection")
    public  Result collection()
    {
        IPage<MPost> page= mPostService.page(getPage(),
                new QueryWrapper<MPost>().inSql("id","select post_id from m_user_collection where user_id="+getProfileId()));


        return Result.success(page);
    }

    @ResponseBody
    @PostMapping("/message/remove/")
    public Result remove(Long id,@RequestParam(defaultValue = "false") Boolean all)
    {
     boolean success= mUserMessageService.remove(new QueryWrapper<MUserMessage>().eq("to_user_id",getProfileId()).eq(!all,"id",id));
        return success? Result.success(null):Result.fail("删除失败");
    }

@ResponseBody
@RequestMapping("/message/nums/")
        public Map msgNums()
        {
            //所有未读消息数量
      int count=  mUserMessageService.count(new QueryWrapper<MUserMessage>().eq("to_user_id",getProfileId()).eq("status",0));
    return MapUtil.builder("status",0).put("count",count).build();
        }


}
