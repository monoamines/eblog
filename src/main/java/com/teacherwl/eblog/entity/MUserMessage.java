package com.teacherwl.eblog.entity;

import com.teacherwl.eblog.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MUserMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 发送消息的用户ID
     */
    private Long fromUserId;

    /**
     * 接收消息的用户ID
     */
    private Long toUserId;

    /**
     * 消息可能关联的帖子
     */
    private Long postId;

    /**
     * 消息可能关联的评论
     */
    private Long commentId;

    private String content;

    /**
     * 消息类型 0是系统消息 1 是评论了你的帖子，2是评论了你的评论，3是楼主回复了你的评论
     */
    private Integer type;


}
