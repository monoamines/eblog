package com.teacherwl.eblog.common.schedules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teacherwl.eblog.entity.MPost;
import com.teacherwl.eblog.service.MPostService;
import com.teacherwl.eblog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 定时将redis中的阅读量刷新到数据库中
 */
@Component
public class ViewCountSyncTask {


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MPostService mPostService;
    @Scheduled(cron = "0/5 * * * * *")//每五秒同步一次
    public void task()
    {
        Set<String> keys = redisTemplate.keys("rank:post:*");
        List<String> postIdKeys=new ArrayList<>();
        for (String key : keys) {

            String postId=key.substring("rank:post:".length());
            boolean hasKey = redisUtil.hHasKey(key, "post:viewCount");
            if (hasKey)
            {
                postIdKeys.add(postId);
            }
        }
        if(postIdKeys.isEmpty())return;

        List<MPost> posts = mPostService.list(new QueryWrapper<MPost>().in("id", postIdKeys));
        posts.stream().forEach((post)->{
            Integer viewCount = (Integer)redisUtil.hget("rank:post:" + post.getId(), "post:viewCount");
            post.setViewCount(viewCount);
        });
        if(posts.isEmpty())return;
        boolean b = mPostService.updateBatchById(posts);
        if(b)
        {
            postIdKeys.stream().forEach((id)->{
                redisUtil.hdel("rank:post:"+id,"post:viewCount");
                System.out.println("id-----------------------:"+id+"同步成功");
            });
        }
    }
}
