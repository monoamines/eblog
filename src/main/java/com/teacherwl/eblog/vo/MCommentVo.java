package com.teacherwl.eblog.vo;

import com.teacherwl.eblog.entity.MComment;
import lombok.Data;

@Data
public class MCommentVo  extends MComment {
    private  String userAvatar;
    private  String userName;
}
