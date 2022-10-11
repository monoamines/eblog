package com.teacherwl.eblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teacherwl.eblog.common.Result;
import com.teacherwl.eblog.entity.*;
import com.teacherwl.eblog.service.*;
import com.teacherwl.eblog.util.ValidationUtil;
import com.teacherwl.eblog.vo.MCommentVo;
import com.teacherwl.eblog.vo.PostVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class PostController extends  BaseController {

    @Autowired
    MPostService mPostService;
    @Autowired
    MUserService mUserService;

    @Autowired
    WsService wsService;

    @Autowired
    MCategoryService mCategoryService;

    @Autowired
    MUserMessageService mUserMessageService;

    @Autowired
    MUserCollectionService mUserCollectionService;

    @GetMapping("/category/{id:\\d*}")//将接收的参数指定为数字类型
    public String category(@PathVariable( name = "id") Long id)
    {
        int pn= ServletRequestUtils.getIntParameter(httpServletRequest,"pn",1);
        httpServletRequest.setAttribute("currentCategoryId",id);
        httpServletRequest.setAttribute("pn",pn);
        return "post/category";
    }


    @GetMapping("/post/{id}")//将接收的参数指定为数字类型
    public String detail(@PathVariable( name = "id") String id)
    {
        httpServletRequest.setAttribute("currentCategoryId",0);
        String id2=id.replace(",","");

        long l = Long.parseLong(id2);

        PostVo postVo=   mPostService.selectOnePost(new QueryWrapper<MPost>().eq("p.id",id2) );//这里注意 Id不知道是谁的ID，需要声明
        //分页、文章ID、用户ID、排序
       IPage<MCommentVo> commentResults= mCommentService.paging(getPage(),postVo.getId(),null,"created");
        httpServletRequest.setAttribute("onePost",postVo);
        httpServletRequest.setAttribute("comments",commentResults);
        Assert.notNull(postVo,"文章被删除");
        mPostService.updateViewCount(postVo);
        return "post/detail";
    }

    @ResponseBody
    @PostMapping("/collection/find")
    public Result collectionFind(Long cid)
    {
      int count=  mUserCollectionService.count(new QueryWrapper<MUserCollection>().eq("user_id",getProfileId()).eq("post_id",cid));
        return  Result.success(MapUtil.of("collection",count>0));
    }


    @ResponseBody
    @PostMapping("/collection/add/")
    public Result collectionAdd(Long cid)
    {
        MPost byId = mPostService.getById(cid);
        Assert.isTrue(byId!=null,"该帖子已经被删除");
        int count=  mUserCollectionService.count(new QueryWrapper<MUserCollection>().eq("post_id",cid).eq("user_id",getProfileId()));
     if (count>0)
     {
         return  Result.fail();
     }
     MUserCollection userCollection=new MUserCollection();
     userCollection.setPostId(cid);
     userCollection.setUserId(getProfileId());
     userCollection.setCreated(new Date());
     userCollection.setModified(new Date());
     userCollection.setPostUserId(byId.getUserId());
     mUserCollectionService.save(userCollection);
        return  Result.success("收藏成功");
    }

    @ResponseBody
    @PostMapping("/collection/remove/")
    public Result collectionRemove(Long cid) {

        MPost byId = mPostService.getById(cid);
        Assert.isTrue(byId!=null,"该帖子已经被删除");
      Boolean isDelete=  mUserCollectionService.remove(new QueryWrapper<MUserCollection>().eq("post_id",cid).eq("user_id",getProfileId()));
      if(isDelete)
      {
          return Result.success();
      }
      return  Result.fail("取消收藏失败");

    }

    @GetMapping("/post/edit")
    public  String edit()
    {
        String id=httpServletRequest.getParameter("id");
        if (!StringUtils.isEmpty(id))
        {
            MPost byId = mPostService.getById(id);
            Assert.isTrue(byId!=null,"该帖子已经被删除");
            Assert.isTrue(byId.getUserId().equals(getProfileId()),"您没有权限修改此文章");
            httpServletRequest.setAttribute("post",byId);



        }
        List<MCategory> list = mCategoryService.list();
        httpServletRequest.setAttribute("categories",list);

        return "/post/edit";
    }
    @ResponseBody
    @PostMapping("/post/submit")
    public Result submit(MPost mPost)
    {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(mPost);
        if(validResult.hasErrors())
        {
            Result.fail(validResult.getErrors());
        }

        if(mPost.getId()==null)
        {


            mPost.setUserId(getProfileId());
            mPost.setViewCount(0);
            mPost.setCreated(new Date());
            mPost.setLevel(0);
            mPost.setVoteUp(0);
            mPost.setVoteDown(0);
            mPost.setEditMode(null);
            mPost.setRecommend(false);
            mPostService.save(mPost);
        }
        else
        {
            MPost byId = mPostService.getById(mPost.getId());
            byId.setContent(mPost.getContent());
            byId.setTitle(mPost.getTitle());
            byId.setCategoryId(mPost.getCategoryId());
            mPostService.updateById(byId);
        }
        return Result.success().action("/post/"+mPost.getId());
    }

    @Transactional
    @PostMapping("/post/delete")
    @ResponseBody
    public Result delete(Long id)
    {
        MPost byId = mPostService.getById(id);
     Assert.notNull(byId,"此文章不存在");
     Assert.isTrue(byId.getUserId()==getProfileId(),"没有权限删除文章");
         mPostService.removeById(id);
        //删除相关消息以及用户收藏
        mUserMessageService.removeByMap(MapUtil.of("post_id",id));
        mUserCollectionService.removeById(MapUtil.of("post_id",id));
        return Result.success().action("/user/index");
    }



    @Transactional
    @PostMapping("/post/reply")
    @ResponseBody
    public Result reply(Long jid,String content)
    {
        MPost post = mPostService.getById(jid);
        Assert.notNull(jid,"此文章不存在");
        Assert.hasLength(content,"评论内容不能为空");
        Assert.isTrue(post!=null,"此文章已被删除");

        MComment mComment=new MComment();
        mComment.setPostId(jid);
        mComment.setContent(content);
        mComment.setUserId(getProfileId());
        mComment.setCreated(new Date());
        mComment.setModified(new Date());
        mComment.setLevel(0);
        mComment.setVoteDown(0);
        mComment.setVoteUp(0);
        mCommentService.save(mComment);

        post.setCommentCount(post.getCommentCount()+1);
        mPostService.updateById(post);
        //本周热评加一
        mPostService.incrCommentCountAndUnionForWeekRank(post.getId(),true);
        //通知作者 有人评论了你的文章 判断是否是自己评论了自己文章(不需要通知)
        if(!post.getId().equals(getProfileId())) {
            MUserMessage mUserMessage = new MUserMessage();
            mUserMessage.setPostId(jid);
            mUserMessage.setCommentId(mComment.getId());
            mUserMessage.setFromUserId(getProfileId());
            mUserMessage.setToUserId(post.getUserId());
            mUserMessage.setType(1);
            mUserMessage.setContent(content);
            mUserMessage.setCreated(new Date());
            mUserMessage.setStatus(0);//未读消息
            mUserMessageService.save(mUserMessage);
            wsService.sendMessCountToUser(mUserMessage.getToUserId());

        }

        //通知被at的人，有人at了你
        if(content.startsWith("@"))
        {
            String username=content.substring(1,content.indexOf(" "));
            System.out.println(username);
         MUser user=   mUserService.getOne(new QueryWrapper<MUser>().eq("username",username));
         if(!ObjectUtil.isEmpty(user))
         {
             MUserMessage mUserMessage1 = new MUserMessage();
             mUserMessage1.setPostId(jid);
             mUserMessage1.setCommentId(mComment.getId());
             mUserMessage1.setFromUserId(getProfileId());
             mUserMessage1.setToUserId(user.getId());
             mUserMessage1.setType(2);//评论了你的评论
             mUserMessage1.setContent(content);
             mUserMessage1.setCreated(new Date());
             mUserMessage1.setStatus(0);//未读消息
             mUserMessageService.save(mUserMessage1);
         }

        }
        return Result.success().action("/post/"+post.getId());



    }

}
