package com.teacherwl.eblog.im.handler.impl;

import com.teacherwl.eblog.im.handler.MsgHandler;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

public class pingMsgHandler implements MsgHandler {
    @Override
    public void handler(String s, WsRequest wsRequest, ChannelContext channelContext) {
        System.out.println("ping");
    }
}
