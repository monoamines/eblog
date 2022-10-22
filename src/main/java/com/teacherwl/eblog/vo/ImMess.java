package com.teacherwl.eblog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ImMess {
    private String username;
    private String avatar;
    private String type;
    private String content;
    private Long cid;
    private Boolean mine;
    private Long fromid;
    private Date timestamp;
    private Long id;

}
