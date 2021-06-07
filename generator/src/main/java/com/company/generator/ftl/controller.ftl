package com.company.${module}.controller.admin;

import com.company.server.dto.${Domain}Dto;
import com.company.server.dto.PageDto;
import com.company.server.dto.ResponseDto;
import com.company.server.service.${Domain}Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/${domain}")
@Slf4j
public class ${Domain}Controller {
    public static final String BUSINESS_NAME = "${tableNameCn}";

    @Autowired
    private ${Domain}Service ${domain}Service;

    /**
     * 列表查询
     * @param pageDto
     * @return
     */
    @PostMapping("/list")
    //@RequestBody接收流数据
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        ${domain}Service.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param ${domain}Dto
     * @return
     */
    @PostMapping("/save")
    //@RequestBody接收流数据
    public ResponseDto save(@RequestBody ${Domain}Dto ${domain}Dto) {
        ResponseDto responseDto = new ResponseDto();
        ${domain}Service.save(${domain}Dto);
        responseDto.setContent(${domain}Dto);
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
        ${domain}Service.delete(id);
        return responseDto;
    }
}
