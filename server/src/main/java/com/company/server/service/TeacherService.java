package com.company.server.service;

import com.company.server.domain.Teacher;
import com.company.server.domain.TeacherExample;
import com.company.server.dto.TeacherDto;
import com.company.server.dto.PageDto;
import com.company.server.mapper.TeacherMapper;
import com.company.server.util.CopyUtil;
import com.company.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 列表查询
     * @param pageDto
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        TeacherExample teacherExample = new TeacherExample();
        List<Teacher> teacherList = teacherMapper.selectByExample(teacherExample);
        PageInfo<Teacher> pageInfo = new PageInfo<>(teacherList);
        pageDto.setTotal(pageInfo.getTotal());
        List<TeacherDto> teacherDtoList = CopyUtil.copyList(teacherList, TeacherDto.class);
        pageDto.setList(teacherDtoList);
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param teacherDto
     */
    public void save(TeacherDto teacherDto) {
        Teacher teacher = CopyUtil.copy(teacherDto, Teacher.class);
        if (StringUtils.isEmpty(teacher.getId())) {
            this.insert(teacher);
        } else {
            this.update(teacher);
        }
    }

    /**
     * 新增
     * @param teacher
     */
    private void insert(Teacher teacher) {
        teacher.setId(UuidUtil.getShortUuid());
        teacherMapper.insert(teacher);
    }

    /**
     * 更新
     * @param teacher
     */
    private void update(Teacher teacher) {
        teacherMapper.updateByPrimaryKey(teacher);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        teacherMapper.deleteByPrimaryKey(id);
    }
}