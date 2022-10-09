package com.teacherwl.eblog.service;

import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.entity.MUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teacherwl.eblog.shiro.AccountProfile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
public interface MUserService extends IService<MUser> {

    Result register(MUser user);

    AccountProfile login(String email, String password);
}
