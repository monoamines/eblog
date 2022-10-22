package com.teacherwl.eblog.service;

import com.teacherwl.eblog.im.vo.ImUser;
import com.teacherwl.eblog.vo.ImMess;

import java.util.List;

public interface ChatService {
    ImUser getCurrentUser();
    void setGroupHistoryMsg(ImMess msg);
    List<Object> getGroupHistoryMsg(int count);
}
