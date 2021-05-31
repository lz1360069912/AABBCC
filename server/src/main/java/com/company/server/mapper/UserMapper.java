package com.company.server.mapper;

import com.company.server.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    List<User> getUserList();

    User getUserById(int id);

}
