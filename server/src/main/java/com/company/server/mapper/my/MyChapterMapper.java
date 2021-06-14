package com.company.server.mapper.my;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyChapterMapper {
    int updateTime(@Param("chapterId") String courseId);
}
