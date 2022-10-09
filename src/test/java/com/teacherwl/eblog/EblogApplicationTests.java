package com.teacherwl.eblog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.teacherwl.eblog.entity.MCategory;
import com.teacherwl.eblog.service.MCategoryService;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EblogApplicationTests {

    @Autowired
    private MCategoryService mCategoryService;
  @Test
    public void test()
    {
        List<MCategory> categories= mCategoryService.list(new QueryWrapper<MCategory>().eq("status",0));
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(categories.get(i).getName());

        }
    }

}
