package com.teacherwl.eblog.controller;

import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.entity.MPost;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

}
