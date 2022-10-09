package com.teacherwl.eblog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.entity.MPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teacherwl.eblog.vo.PostVo;
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
public interface MPostMapper extends BaseMapper<MPost> {

    IPage<PostVo> selectPosts(Page pages,@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    PostVo selectOne(@Param(Constants.WRAPPER) QueryWrapper<MPost> wrapper);
}
