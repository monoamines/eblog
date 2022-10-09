package com.teacherwl.eblog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.entity.MUser;
import com.teacherwl.eblog.mapper.MUserMapper;
import com.teacherwl.eblog.service.MUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teacherwl.eblog.shiro.AccountProfile;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Service
public class MUserServiceImpl extends ServiceImpl<MUserMapper, MUser> implements MUserService {

    @Override
    public Result register(MUser user) {
      int count=  this.count(new QueryWrapper<MUser>().eq("email",user.getEmail()).
                or().
                eq("username",user.getUsername()));
      if(count>0)
          return Result.fail("用户已经存在");

      MUser temp=new MUser();
      temp.setUsername(user.getUsername());
      temp.setEmail(user.getEmail());
      temp.setPassword(SecureUtil.md5(user.getPassword()));//实际中需要加密
        temp.setCreated(new Date());
        temp.setPoint(0);
        temp.setCommentCount(0);
        temp.setPostCount(0);
        temp.setAvatar("/res/images/avatar/default.png");
        this.save(temp);
        return  Result.success();
    }

    @Override
    public AccountProfile login(String email, String password) {
       MUser user= this.getOne(new QueryWrapper<MUser>().eq("email",email));
       if(user==null)
       {
           throw new UnknownAccountException();
       }
       String securitypass=SecureUtil.md5(password);
       if(!user.getPassword().equals(securitypass))
       {
           throw new IncorrectCredentialsException();
       }
       user.setLasted(new Date());
       this.updateById(user);
        AccountProfile profile=new AccountProfile();
        BeanUtil.copyProperties(user,profile);

        return profile;
    }
}
