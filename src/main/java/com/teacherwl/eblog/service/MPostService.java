package com.teacherwl.eblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.teacherwl.eblog.entity.MPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teacherwl.eblog.vo.PostVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
public interface MPostService extends IService<MPost> {

    IPage<PostVo> paging(Page pages, Long categoryId, Long userId, Integer level, Boolean recommend, String order);

    PostVo selectOnePost(QueryWrapper<MPost> wrapper);

    void initWeekRank();

    void incrCommentCountAndUnionForWeekRank(Long postId,boolean isIncr);

    void updateViewCount(PostVo postVo);
}
