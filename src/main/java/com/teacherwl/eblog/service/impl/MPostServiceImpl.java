package com.teacherwl.eblog.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.entity.MPost;
import com.teacherwl.eblog.mapper.MPostMapper;
import com.teacherwl.eblog.service.MPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teacherwl.eblog.util.RedisUtil;
import com.teacherwl.eblog.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2022-09-19
 */
@Service
public class MPostServiceImpl extends ServiceImpl<MPostMapper, MPost> implements MPostService {

    @Autowired
    MPostMapper mPostMapper;

    @Autowired
    RedisUtil redisUtil;
    @Override
    public IPage<PostVo> paging(Page pages, Long categoryId, Long userId, Integer level, Boolean recommend, String order) {

        if(level==null) level=-1;
        QueryWrapper queryWrapper=new QueryWrapper<MPost>().eq(categoryId!=null,"category_id",categoryId)
                .eq(userId!=null,"user_id",userId).eq(level==0,"level",0).gt(level>0,"level",0)
                .orderByDesc(order!=null,order);
        return mPostMapper.selectPosts(pages,queryWrapper);
    }

    @Override
    public PostVo selectOnePost(QueryWrapper<MPost> wrapper) {
       PostVo postVo= mPostMapper.selectOne(wrapper);
        return  postVo;
    }

    @Override
    public void initWeekRank() {
        List<MPost> mPosts=this.list(new QueryWrapper<MPost>().ge("created", DateUtil.offsetDay(new Date(),-15)).select("id","title","user_id","comment_count","view_count","created"));
        for (MPost post:mPosts
             ) {
            String key="day:rank:"+DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_PATTERN);
            redisUtil.zSet(key,post.getId(),post.getCommentCount());
            //7天后自动过期
            long expiredDays = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime=(7-expiredDays)*24*60*60;
            redisUtil.expire(key,expireTime);
            this.hashCachePostIdAndTitle(post,expireTime);
        }
        this.zunionAndStoreLast7DayForWeekRank();
    }

    /**
     * 可能是第一条评论，可能不是第一条(是第一条的话就要把post信息存储到redis中)
     * @param postId
     * @param isIncr
     */
    @Override
    public void incrCommentCountAndUnionForWeekRank(Long postId,boolean isIncr) {
        String key="day:rank:"+DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        redisUtil.zIncrementScore(key,postId,isIncr?1:-1);
        this.zunionAndStoreLast7DayForWeekRank();
        MPost byId = this.getById(postId);
        long expiredDays = DateUtil.between(new Date(),byId.getCreated(), DateUnit.DAY);
        long expireTime=(7-expiredDays)*24*60*60;
        //缓存这篇文章的信息
        this.hashCachePostIdAndTitle(byId,expireTime);
    }

    @Override
    public void updateViewCount(PostVo postVo) {
        String key="rank:post:"+postVo.getId();
        Integer hget = (Integer) redisUtil.hget(key, "post:viewCount");
        if(hget!=null)
        {
            postVo.setViewCount(hget+1);

        }
        else
        {
            postVo.setViewCount(postVo.getViewCount()+1);
        }

        redisUtil.hset(key,"post:viewCount",postVo.getViewCount());
    }

    private void zunionAndStoreLast7DayForWeekRank() {

        String key="day:rank:"+DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        List<String> otherKeys=new ArrayList<>();
        String desKeys="week:rank";
        for (int i = -14; i < 0; i++) {
            String temp="day:rank:"+DateUtil.format(DateUtil.offsetDay(new Date(),i), DatePattern.PURE_DATE_PATTERN);
            otherKeys.add(temp);
        }
        redisUtil.zUnionAndStore(key,otherKeys,desKeys);
    }

    private void hashCachePostIdAndTitle(MPost mPost,long expiredTime) {
        String key="rank:post:"+mPost.getId();
        boolean hasKey = redisUtil.hasKey(key);
        if(!hasKey)
        {
            redisUtil.hset(key,"post:id",mPost.getId(),expiredTime);
            redisUtil.hset(key,"post:title",mPost.getTitle(),expiredTime);
            redisUtil.hset(key,"post:commentCount",mPost.getCommentCount(),expiredTime);
        }
    }
}
