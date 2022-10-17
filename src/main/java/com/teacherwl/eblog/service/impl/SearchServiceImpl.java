package com.teacherwl.eblog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.search.model.PostDocument;
import com.teacherwl.eblog.search.repository.PostRepository;
import com.teacherwl.eblog.service.SearchService;
import com.teacherwl.eblog.vo.PostVo;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    PostRepository postRepository;
    @Override
    public IPage search(Page page, String keyword) {

        //mybatisPage转化为JPA pageable
       Long current= page.getCurrent()-1;
        Long size=page.getSize();
        Pageable pageable= PageRequest.of(current.intValue(),size.intValue());
     MultiMatchQueryBuilder multiMatchQueryBuilder= QueryBuilders.multiMatchQuery(keyword,"title","authorName","categoryName");
 org.springframework.data.domain.Page<PostDocument> documents= postRepository.search(multiMatchQueryBuilder,pageable);
 IPage pageData=new Page(page.getCurrent(),page.getSize(),documents.getTotalElements());
 pageData.setRecords(documents.getContent());
        return pageData;
    }

    @Override
    public int initEsData(List<PostVo> records) {
        if(records==null&&records.isEmpty())
        {
            return 0;
        }
        List<PostDocument> document=new ArrayList<>();
        for (PostVo postVo:records)
        {
            PostDocument postDocument = BeanUtil.copyProperties(postVo, PostDocument.class);
            document.add(postDocument);
        }
         postRepository.saveAll(document);
        return document.size();
    }
}
