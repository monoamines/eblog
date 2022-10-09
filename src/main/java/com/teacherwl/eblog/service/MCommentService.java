package com.teacherwl.eblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.entity.MComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teacherwl.eblog.vo.MCommentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
public interface MCommentService extends IService<MComment> {

    IPage<MCommentVo> paging(Page page, Long postId, Long userId, String created);
}
