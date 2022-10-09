package com.teacherwl.eblog.template;

import com.teacherwl.eblog.common.templates.DirectiveHandler;
import com.teacherwl.eblog.common.templates.TemplateDirective;
import com.teacherwl.eblog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 本周热议
 */
@Component
public class RankTemplate  extends TemplateDirective {

    @Autowired
    RedisUtil redisUtil;
    @Override
    public String getName() {
        return "hosts";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        String key="week:rank";
        List<Map> hostslist=new ArrayList<>();
        Set<ZSetOperations.TypedTuple> zSetRank = redisUtil.getZSetRank(key, 0, 6);
        for (ZSetOperations.TypedTuple typedTuple : zSetRank) {
            Map<String,Object> map=new HashMap<>();
            Object value = typedTuple.getValue();//post Id
            String postkey="rank:post:"+value;
            map.put("id",value);
            map.put("title",redisUtil.hget(postkey,"post:title"));
            map.put("commentCount",redisUtil.hget(postkey,"post:commentCount"));
            hostslist.add(map);
        }
        handler.put(RESULTS,hostslist).render();
    }
}
