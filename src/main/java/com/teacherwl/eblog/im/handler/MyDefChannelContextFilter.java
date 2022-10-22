package com.teacherwl.eblog.im.handler;

import lombok.Data;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;

@Data
public class MyDefChannelContextFilter implements ChannelContextFilter {
    private  ChannelContext currentContext;
    @Override
    public boolean filter(ChannelContext channelContext) {
        if(currentContext.userid.equals(channelContext.userid))
        {
            return false;
        }
        return true;
    }
}
