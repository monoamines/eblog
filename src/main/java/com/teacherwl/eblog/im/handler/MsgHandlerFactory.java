package com.teacherwl.eblog.im.handler;

import com.teacherwl.eblog.common.lang.Consts;
import com.teacherwl.eblog.im.handler.impl.chatMsgHandler;
import com.teacherwl.eblog.im.handler.impl.pingMsgHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MsgHandlerFactory {
    private static Map<String,MsgHandler> handlerMap=new HashMap<>();
    public static void init()
    {
    handlerMap.put(Consts.IM_MESS_TYPE_CHAT,new chatMsgHandler());
    handlerMap.put(Consts.IM_MESS_TYPE_PING,new pingMsgHandler());
    log.info("handler factory init");
    }

    //根据类型获取处理器
    public static MsgHandler getMsgHandler(String type)
    {
        return handlerMap.get(type);
    }
}
