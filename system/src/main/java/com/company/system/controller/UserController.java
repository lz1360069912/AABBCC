package com.company.system.controller;

import com.company.system.domain.User;
import com.company.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

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
}
