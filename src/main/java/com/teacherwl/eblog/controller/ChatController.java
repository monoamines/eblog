package com.teacherwl.eblog.controller;

import cn.hutool.core.map.MapUtil;
import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.common.lang.Consts;
import com.teacherwl.eblog.im.vo.ImUser;
import com.teacherwl.eblog.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Autowired
    ChatService chatService;
    @GetMapping("/getMineAndGroupData")
    public Result getMineAndGroupData()
    {
        //默认群
        Map<String, Object> group=new HashMap<>();
        group.put("name","社区群聊");
        group.put("type","group");
        group.put("avatar","");
        group.put("id", Consts.IM_GROUP_ID);
        group.put("members",0);
        ImUser user=chatService.getCurrentUser();
        return Result.success(MapUtil.builder().put("group",group).put("mine",user).map());
    }
    @GetMapping("/getGroupHistoryMsg")
    public Result getgroupHistoryMsg()
    {

        List<Object> groupHistoryMsg = chatService.getGroupHistoryMsg(20);
        return Result.success(groupHistoryMsg);
    }
}
