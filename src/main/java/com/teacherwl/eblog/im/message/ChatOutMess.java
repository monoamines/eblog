package com.teacherwl.eblog.im.message;

import com.teacherwl.eblog.vo.ImMess;
import lombok.Data;

@Data
public class ChatOutMess {
    private  String emit;
    private ImMess data;
}
