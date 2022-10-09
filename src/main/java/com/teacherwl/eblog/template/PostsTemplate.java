package com.teacherwl.eblog.template;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.common.templates.DirectiveHandler;
import com.teacherwl.eblog.common.templates.TemplateDirective;
import com.teacherwl.eblog.service.MPostService;
import com.teacherwl.eblog.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostsTemplate  extends TemplateDirective {
    @Override
    public String getName() {
        return "posts";
    }
    @Autowired
    MPostService mPostService;
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer level = handler.getInteger("level");
        Integer pn = handler.getInteger("pn",1);
        Integer size = handler.getInteger("size",2);
        Long categoryId = handler.getLong("categoryId");
        IPage<PostVo> page = mPostService.paging(new Page(pn, size), categoryId, null, level, null, "created");
        handler.put(RESULTS,page).render();


    }
}
