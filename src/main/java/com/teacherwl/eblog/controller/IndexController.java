package com.teacherwl.eblog.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.vo.PostVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController extends  BaseController {

@RequestMapping({"","/","index"})
    public String index()
    {
        //分页信息、置顶、分类、用户、精选

        //1.分页信息、2分类、3用户、4置顶、5精选、6排序
        IPage<PostVo> page= mPostService.paging(getPage(),null,null,null,null,"created");
        httpServletRequest.setAttribute("currentCategoryId",0);
        httpServletRequest.setAttribute("pageData",page);
        return "index";
    }


    @RequestMapping("test")
    @ResponseBody
    public String test()
    {
        return "index";
    }
}
