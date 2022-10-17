package com.teacherwl.eblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacherwl.eblog.vo.PostVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {

    IPage search(Page page,String keyword);

    int initEsData(List<PostVo> records);
}
