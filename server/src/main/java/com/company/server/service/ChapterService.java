package com.company.server.service;

import com.company.server.domain.Chapter;
import com.company.server.domain.ChapterExample;
import com.company.server.dto.ChapterDto;
import com.company.server.dto.PageDto;
import com.company.server.mapper.ChapterMapper;
import com.company.server.util.CopyUtil;
import com.company.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    /**
     * 列表查询
     * @param pageDto
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ChapterExample example = new ChapterExample();
        List<Chapter> chapterList = chapterMapper.selectByExample(example);
        PageInfo<Chapter> pageInfo = new PageInfo<>(chapterList);
        pageDto.setTotal(pageInfo.getTotal());
        List<ChapterDto> chapterDtoList = CopyUtil.copyList(chapterList, ChapterDto.class);
        pageDto.setList(chapterDtoList);
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param chapterDto
     */
    //即支持新增也支持修改，通过判断有没有ID值，有值认为是修改，没值认为是新增
    public void save(ChapterDto chapterDto) {
        Chapter chapter = CopyUtil.copy(chapterDto, Chapter.class);
        if (StringUtils.isEmpty(chapter.getId())) {
            this.insert(chapter);
        } else {
            this.update(chapter);
        }
    }

    /**
     * 新增
     * @param chapter
     */
    private void insert(Chapter chapter) {
        chapter.setId(UuidUtil.getShortUuid());
        chapterMapper.insert(chapter);
    }

    /**
     * 更新
     * @param chapter
     */
    private void update(Chapter chapter) {
        chapterMapper.updateByPrimaryKey(chapter);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        chapterMapper.deleteByPrimaryKey(id);
    }
}