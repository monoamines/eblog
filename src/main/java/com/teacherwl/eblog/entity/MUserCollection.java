package com.teacherwl.eblog.entity;

import com.teacherwl.eblog.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Data
public class MUserCollection {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long postId;

    private Long postUserId;

    private Date Created;
    private  Date modified;


}
