package com.teacherwl.eblog.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.teacherwl.eblog.common.lang.Consts;
import com.teacherwl.eblog.im.vo.ImUser;
import com.teacherwl.eblog.service.ChatService;
import com.teacherwl.eblog.shiro.AccountProfile;
import com.teacherwl.eblog.util.RedisUtil;
import com.teacherwl.eblog.vo.ImMess;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("chatService")
public class ChatServiceImpl implements ChatService {

    @Autowired
    private  RedisUtil redisUtil;
    @Override
    public ImUser getCurrentUser() {
      AccountProfile accountProfile=(AccountProfile)SecurityUtils.getSubject().getPrincipal();
      ImUser user=new ImUser();
      if(accountProfile!=null)
      {
          user.setId(accountProfile.getId());
        user.setAvatar(accountProfile.getAvatar());
        user.setUsername(accountProfile.getUsername());
        user.setStatus(ImUser.ONLINE_STATUS);
      }
      else
      {
          Long imUserId = (Long) SecurityUtils.getSubject().getSession().getAttribute("imUserId");
          user.setId(imUserId!=null? imUserId: RandomUtil.randomLong());
          SecurityUtils.getSubject().getSession().setAttribute("imUserId",imUserId);
          user.setSign("fuck you bitch");
          user.setStatus(ImUser.ONLINE_STATUS);
          user.setUsername("匿名用户");
      }
      return user;
    }

    @Override
    public void setGroupHistoryMsg(ImMess msg) {
        redisUtil.lSet(Consts.IM_GROUP_HISTROY_MSG_KEY,msg,24*60*60);
    }

    @Override
    public List<Object> getGroupHistoryMsg(int count) {
        long l = redisUtil.lGetListSize(Consts.IM_GROUP_HISTROY_MSG_KEY);
        return redisUtil.lGet(Consts.IM_GROUP_HISTROY_MSG_KEY,l<count? 0:l-count,l);
    }
}
