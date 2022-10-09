package com.teacherwl.eblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.entity.MComment;
import com.teacherwl.eblog.mapper.MCommentMapper;
import com.teacherwl.eblog.service.MCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teacherwl.eblog.vo.MCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Service
public class MCommentServiceImpl extends ServiceImpl<MCommentMapper, MComment> implements MCommentService {

    @Autowired
    private  MCommentMapper mCommentMapper;
    @Override
    public IPage<MCommentVo> paging(Page page, Long postId, Long userId, String created) {
        return mCommentMapper.selectComments(page,new QueryWrapper<MComment>().eq("post_id",postId).eq(userId!=null,"user_id",userId).orderByDesc("created"));
    }
}
