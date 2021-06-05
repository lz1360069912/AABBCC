package com.company.business.controller.admin;

import com.company.server.dto.ChapterDto;
import com.company.server.dto.PageDto;
import com.company.server.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/chapter")
@Slf4j
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @RequestMapping("/list")
    //@RequestBody接收流数据
    public PageDto list(@RequestBody PageDto pageDto) {
        chapterService.list(pageDto);
        return pageDto;
    }

    @RequestMapping("/save")
    //@RequestBody接收流数据
    public ChapterDto save(@RequestBody ChapterDto chapterDto) {
        log.info("chapterDto:{}", chapterDto);
        chapterService.save(chapterDto);
        return chapterDto;
    }
}
