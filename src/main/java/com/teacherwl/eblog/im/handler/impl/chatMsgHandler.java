package com.teacherwl.eblog.im.handler.impl;

import cn.hutool.json.JSONUtil;
import com.teacherwl.eblog.common.lang.Consts;
import com.teacherwl.eblog.im.handler.MsgHandler;
import com.teacherwl.eblog.im.handler.MyDefChannelContextFilter;
import com.teacherwl.eblog.im.message.ChatImMess;
import com.teacherwl.eblog.im.message.ChatOutMess;
import com.teacherwl.eblog.im.vo.ImTo;
import com.teacherwl.eblog.im.vo.ImUser;
import com.teacherwl.eblog.service.ChatService;
import com.teacherwl.eblog.util.SpringUtil;
import com.teacherwl.eblog.vo.ImMess;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.Date;

@Slf4j
public class chatMsgHandler implements MsgHandler {
    @Override
    public void handler(String s, WsRequest wsRequest, ChannelContext channelContext) {
        ChatImMess chatImMess = JSONUtil.toBean(s, ChatImMess.class);
        ImTo to = chatImMess.getTo();
        ImUser mine = chatImMess.getMine();
        //处理消息
        ChatOutMess chat=new ChatOutMess();
        chat.setEmit("chatMessage");
        ImMess imMess=new ImMess();
        imMess.setContent(mine.getContent());
        imMess.setAvatar(mine.getAvatar());
        imMess.setId(Consts.IM_GROUP_ID);
        imMess.setMine(false);
        imMess.setFromid(mine.getId());
        imMess.setUsername(mine.getUsername());
        imMess.setTimestamp(new Date());
        imMess.setType(to.getType());
        chat.setData(imMess);
        String s1 = JSONUtil.toJsonStr(chat);
        log.info("群聊消息" +s1);

        WsResponse wsResponse=WsResponse.fromText(s1,"utf-8");
        MyDefChannelContextFilter myDefChannelContextFilter=new MyDefChannelContextFilter();
        myDefChannelContextFilter.setCurrentContext(channelContext);

        Tio.sendToGroup(channelContext.getGroupContext(), Consts.IM_GROUP_NAME,wsResponse,myDefChannelContextFilter);
        ChatService chatService = (ChatService) SpringUtil.getBean("chatService");
        chatService.setGroupHistoryMsg(imMess);
    }
}
