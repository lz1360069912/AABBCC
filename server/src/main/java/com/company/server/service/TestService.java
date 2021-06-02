package com.company.server.service;

import com.company.server.domain.Test;
import com.company.server.domain.TestExample;
import com.company.server.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public List<Test> list(){
        TestExample example = new TestExample();
        //example.setOrderByClause("id desc");//asc
        example.createCriteria().andIdEqualTo("1");
        return testMapper.selectByExample(example);
    }
}
