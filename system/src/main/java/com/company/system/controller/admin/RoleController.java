package com.company.system.controller.admin;

import com.company.server.dto.PageDto;
import com.company.server.dto.ResponseDto;
import com.company.server.dto.RoleDto;
import com.company.server.service.RoleService;
import com.company.server.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/role")
@Slf4j
public class RoleController {
    public static final String BUSINESS_NAME = "角色";

    @Autowired
    private RoleService roleService;

    /**
     * 列表查询
     * @param pageDto
     * @return
     */
    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        roleService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param roleDto
     * @return
     */
    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleDto roleDto) {
        // 保存校验
        ValidatorUtil.require(roleDto.getName(), "角色");
        ValidatorUtil.length(roleDto.getName(), "角色", 1, 50);
        ValidatorUtil.require(roleDto.getDesc(), "描述");
        ValidatorUtil.length(roleDto.getDesc(), "描述", 1, 100);

        ResponseDto responseDto = new ResponseDto();
        roleService.save(roleDto);
        responseDto.setContent(roleDto);
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
        roleService.delete(id);
        return responseDto;
    }

    /**
     * 保存资源
     * @param roleDto
     */
    @PostMapping("/save-resource")
    public ResponseDto saveResource(@RequestBody RoleDto roleDto) {
        log.info("保存角色资源关联开始");
        ResponseDto responseDto = new ResponseDto();
        roleService.saveResource(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    /**
     * 加载已关联的资源
     */
    @GetMapping("/list-resource/{roleId}")
    public ResponseDto listResource(@PathVariable String roleId) {
        log.info("加载资源开始");
        ResponseDto responseDto = new ResponseDto<>();
        List<String> resourceIdList = roleService.listResource(roleId);
        responseDto.setContent(resourceIdList);
        return responseDto;
    }
}
