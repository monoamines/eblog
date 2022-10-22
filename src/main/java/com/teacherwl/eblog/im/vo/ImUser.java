package com.teacherwl.eblog.im.vo;

import lombok.Data;

@Data
public class ImUser {

    public final static  String ONLINE_STATUS="online";
    public final static  String HIDE_STATUS="hide";
    private  String username;
    private  Long id;
    private  String status;
    private  String sign;
    private  String avatar;
    private  Boolean mine;//是否是我发送的消息
    private  String content;//消息内容
}
