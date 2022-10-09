package com.teacherwl.eblog.shiro;

import lombok.Data;

import java.util.Date;

/**
 * 写一个通用的用户数据类，来存储返回的用户信息
 */
@Data
public class AccountProfile {
    private  Long id;
    private  String username;
    private  String email;
    private Date created;
    private String avatar;
    private  String sign;
    private String gender;

    public String getSex()
    {
        return gender.equals("0")? "男":"女";
    }
}
