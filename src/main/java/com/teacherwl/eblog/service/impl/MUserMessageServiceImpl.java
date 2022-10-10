package com.teacherwl.eblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teacherwl.eblog.entity.MUserMessage;
import com.teacherwl.eblog.mapper.MUserMessageMapper;
import com.teacherwl.eblog.service.MUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Service
public class MUserMessageServiceImpl extends ServiceImpl<MUserMessageMapper, MUserMessage> implements MUserMessageService {

    @Autowired
    MUserMessageMapper mUserMessageMapper;
    @Override
    public IPage paging(Page page, QueryWrapper<MUserMessage> to_user_id) {
    return mUserMessageMapper.selectMessages(page,to_user_id);
    }

    @Override
    public void updateToReaded(List<Long> ids) {
    if(ids.isEmpty())
    {
        return;
    }
    mUserMessageMapper.updateToReaded(new QueryWrapper<MUserMessage>().in("id",ids));
    }
}
