package com.teacherwl.eblog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.entity.MUserMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Component
public interface MUserMessageMapper extends BaseMapper<MUserMessage> {

    IPage selectMessages(Page page, @Param(Constants.WRAPPER) QueryWrapper<MUserMessage> to_user_id);

    @Transactional
    @Update("update m_user_message set status=1  ${ew.customsqlSegment}")
    void updateToReaded( @Param(Constants.WRAPPER) QueryWrapper<MUserMessage> id);
}
