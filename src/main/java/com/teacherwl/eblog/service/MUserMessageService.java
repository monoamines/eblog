package com.teacherwl.eblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.entity.MUserMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
public interface MUserMessageService extends IService<MUserMessage> {

    IPage paging(Page page, QueryWrapper<MUserMessage> to_user_id);

    void updateToReaded(List<Long> ids);
}
