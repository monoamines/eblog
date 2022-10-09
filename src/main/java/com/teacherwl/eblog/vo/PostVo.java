package com.teacherwl.eblog.vo;

import com.teacherwl.eblog.entity.MPost;
import lombok.Data;

@Data
public class PostVo  extends MPost {
    private  String authorName;
    private  String authorAvatar;
    private  String categoryName;

}
