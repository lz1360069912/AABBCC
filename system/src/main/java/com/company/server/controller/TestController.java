package com.company.server.controller;

import com.company.server.domain.Test;
import com.company.server.domain.TestExample;
import com.company.server.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @RequestMapping("/test")
    public List<Test> test(){
        TestExample example = new TestExample();
        //example.setOrderByClause("id desc");//asc
        example.createCriteria().andIdEqualTo("1");
        return testMapper.selectByExample(example);
    }
}
