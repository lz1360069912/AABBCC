package com.company.business.controller.admin;

import com.company.server.dto.SectionDto;
import com.company.server.dto.PageDto;
import com.company.server.dto.ResponseDto;
import com.company.server.service.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/section")
@Slf4j
public class SectionController {
    public static final String BUSINESS_NAME = "小节";

    @Autowired
    private SectionService sectionService;

    /**
     * 列表查询
     * @param pageDto
     * @return
     */
    @PostMapping("/list")
    //@RequestBody接收流数据
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        sectionService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param sectionDto
     * @return
     */
    @PostMapping("/save")
    //@RequestBody接收流数据
    public ResponseDto save(@RequestBody SectionDto sectionDto) {
        ResponseDto responseDto = new ResponseDto();
        sectionService.save(sectionDto);
        responseDto.setContent(sectionDto);
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
        sectionService.delete(id);
        return responseDto;
    }
}
