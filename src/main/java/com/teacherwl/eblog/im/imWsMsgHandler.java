package com.teacherwl.eblog.im;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.teacherwl.eblog.common.lang.Consts;
import com.teacherwl.eblog.im.handler.MsgHandler;
import com.teacherwl.eblog.im.handler.MsgHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Map;

@Slf4j
public class imWsMsgHandler implements IWsMsgHandler {
    /**
     * 握手时候走的方法
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //绑定个人通道
        String userId = httpRequest.getParam("userId");
        Tio.bindUser(channelContext,userId);
        return httpResponse;
    }

    /**
     * 握手完成后
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @throws Exception
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //绑定群聊通道
        Tio.bindGroup(channelContext, Consts.IM_GROUP_NAME);
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     *
     * 链接关闭方法
     * @param wsRequest
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 接受字符类型消息
     * @param wsRequest
     * @param s
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onText(WsRequest wsRequest, String s, ChannelContext channelContext) throws Exception {
    log.info("接收到消息"+s);
        Map map = JSONUtil.toBean(s, Map.class);
        String type= MapUtil.getStr(map,"type");
        String data= MapUtil.getStr(map,"data");
        MsgHandler msgHandler = MsgHandlerFactory.getMsgHandler(type);//得到消息处理器
        msgHandler.handler(data,wsRequest,channelContext);
        //处理消息

        return null;
    }
}
