package com.company.server.mapper.my;

import com.company.server.dto.ResourceDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyUserMapper {

    List<ResourceDto> findResources(@Param("userId") String userId);

}