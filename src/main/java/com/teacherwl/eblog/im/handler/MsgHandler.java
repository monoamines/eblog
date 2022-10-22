package com.teacherwl.eblog.im.handler;

import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;

public interface MsgHandler {
    void handler(String s, WsRequest wsRequest, ChannelContext channelContext);
}
