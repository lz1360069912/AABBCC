package com.company.server.service;

import com.company.server.domain.Chapter;
import com.company.server.domain.ChapterExample;
import com.company.server.dto.ChapterDto;
import com.company.server.mapper.ChapterMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService {

    @Autowired
    private ChapterMapper ChapterMapper;

    public List<ChapterDto> list(){
        PageHelper.startPage(1,5);
        ChapterExample example = new ChapterExample();
        List<Chapter> chapterList = ChapterMapper.selectByExample(example);
        List<ChapterDto> chapterDtoList = new ArrayList<>();
        for (int i = 0, l = chapterList.size(); i < l; i++){
            Chapter chapter = chapterList.get(i);
            ChapterDto chapterDto = new ChapterDto();
            BeanUtils.copyProperties(chapter,chapterDto);
            chapterDtoList.add(chapterDto);
        }
        return chapterDtoList;
    }
}
