package com.company.system.controller.admin;

import com.company.server.dto.RoleResourceDto;
import com.company.server.dto.PageDto;
import com.company.server.dto.ResponseDto;
import com.company.server.service.RoleResourceService;
import com.company.server.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/roleResource")
@Slf4j
public class RoleResourceController {
    public static final String BUSINESS_NAME = "角色资源关联";

    @Autowired
    private RoleResourceService roleResourceService;

    /**
     * 列表查询
     * @param pageDto
     * @return
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        roleResourceService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param roleResourceDto
     * @return
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleResourceDto roleResourceDto) {
        // 保存校验
        ValidatorUtil.require(roleResourceDto.getRoleId(), "角色");
        ValidatorUtil.require(roleResourceDto.getResourceId(), "资源");

        ResponseDto responseDto = new ResponseDto();
        roleResourceService.save(roleResourceDto);
        responseDto.setContent(roleResourceDto);
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
        roleResourceService.delete(id);
        return responseDto;
    }
}
