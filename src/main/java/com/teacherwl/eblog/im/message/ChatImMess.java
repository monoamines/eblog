package com.teacherwl.eblog.im.message;

import com.teacherwl.eblog.im.vo.ImTo;
import com.teacherwl.eblog.im.vo.ImUser;
import lombok.Data;

@Data
public class ChatImMess {
    private ImUser mine;
    private ImTo to;
}
