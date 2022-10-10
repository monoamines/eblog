package com.teacherwl.eblog.service;

import org.springframework.stereotype.Service;

@Service
public interface WsService {
    void sendMessCountToUser(Long toUserId);
}
