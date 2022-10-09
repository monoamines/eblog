package com.teacherwl.eblog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.entity.MComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teacherwl.eblog.vo.MCommentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Component
public interface MCommentMapper extends BaseMapper<MComment> {
    IPage<MCommentVo> selectComments(Page page, @Param(Constants.WRAPPER) QueryWrapper<MComment> wrapper);


}
