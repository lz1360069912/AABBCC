package com.company.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TestMapper testMapper;

    @RequestMapping("/getUserList")
    public List<User> getUserList(){
        List<User> userList = userMapper.getUserList();
        return userList;
    }
    @RequestMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id")int id){
        User userById = userMapper.getUserById(id);
        return userById;
    }

    @RequestMapping("/test")
    public List<Test> test(){
        TestExample example = new TestExample();
        //example.setOrderByClause("id desc");//asc
        example.createCriteria().andIdEqualTo(1);
        return testMapper.selectByExample(example);
    }
}
