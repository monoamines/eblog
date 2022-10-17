package com.teacherwl.eblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.entity.MPost;
import com.teacherwl.eblog.vo.PostVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{

    /**
     *
     * @param id postId
     * @param rank 0表示取消，1表示操作
     * @param field 操作类型
     * @return
     */
    @ResponseBody
    @PostMapping("/jet-set")
    public Result jetSet(Long id,Integer rank,String field)
    {
        MPost post = mPostService.getById(id);
        Assert.notNull(post,"该帖子已经被删除");
        if("delete".equals(field))
        {
            mPostService.removeById(id);
            return Result.success();
        } else if ("status".equals(field)) {
            //rank为1就加精，否则不加精
            post.setRecommend(rank==1);
        }
        else if ("stick".equals(field))
        {
        post.setLevel(rank);
        }
        mPostService.updateById(post);
        return Result.success();
    }
    @ResponseBody
    @RequestMapping("initEsData")
    public Result initEsData()
    {
        int size=1000;
        Page page=new Page();
        page.setSize(size);
        int total=0;
        for (int i = 0; i < 1000; i++) {
                page.setCurrent(1);
            IPage<PostVo> paging = mPostService.paging(page, null, null, null, null, "created");
       int nums= searchService.initEsData(paging.getRecords());
       total+=nums;

        //当查询一页数据小于size时 直接返回
        if(paging.getRecords().size()<size)
        {
            break;
        }
        }
    return Result.success("总共有"+total+"条数据",null);

    }


}
