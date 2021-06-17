package com.company.server.mapper.my;

import com.company.server.dto.SortDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCourseMapper {
    int updateTime(@Param("courseId") String courseId);

    int updateSort(SortDto sortDto);

    int moveSortsForward(SortDto sortDto);

    int moveSortsBackward(SortDto sortDto);
}