package com.teacherwl.eblog.vo;

import com.teacherwl.eblog.entity.MUserMessage;
import lombok.Data;
import org.springframework.data.redis.core.index.PathBasedRedisIndexDefinition;

@Data
public class UserMessageVo extends MUserMessage {

    private  String toUserName;//接受消息用户ID
    private  String fromUserName;//消息来源用户ID
    private  String postTitle;
    private  String commentContent;
}
