package com.teacherwl.eblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teacherwl.eblog.entity.MUserMessage;
import com.teacherwl.eblog.service.MUserMessageService;
import com.teacherwl.eblog.service.WsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WsServiceImpl implements WsService {

    @Autowired
    MUserMessageService mUserMessageService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Async
    @Override
    public void sendMessCountToUser(Long toUserId) {
        int count=  mUserMessageService.count(new QueryWrapper<MUserMessage>().eq("to_user_id",toUserId).eq("status",0));
        simpMessagingTemplate.convertAndSendToUser(toUserId.toString(),"messCount",count);
    }
}
