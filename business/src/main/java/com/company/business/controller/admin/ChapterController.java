package com.company.business.controller.admin;

import com.company.server.dto.ChapterDto;
import com.company.server.dto.PageDto;
import com.company.server.dto.ResponseDto;
import com.company.server.service.ChapterService;
import com.company.server.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/chapter")
@Slf4j
public class ChapterController {
    public static final String BUSINESS_NAME = "大章";

    @Autowired
    private ChapterService chapterService;

    /**
     * 列表查询
     * @param pageDto
     * @return
     */
    @PostMapping("/list")
    //@RequestBody接收流数据
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        chapterService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param chapterDto
     * @return
     */
    @PostMapping("/save")
    //@RequestBody接收流数据
    public ResponseDto save(@RequestBody ChapterDto chapterDto) {

        // 保存校验
        ValidatorUtil.require(chapterDto.getName(), "名称");
        ValidatorUtil.require(chapterDto.getCourseId(), "课程ID");
        ValidatorUtil.length(chapterDto.getCourseId(), "课程ID", 1, 8);


        ResponseDto responseDto = new ResponseDto();
        chapterService.save(chapterDto);
        responseDto.setContent(chapterDto);
        return responseDto;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    //@RequestBody接收流数据
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        chapterService.delete(id);
        return responseDto;
    }
}
