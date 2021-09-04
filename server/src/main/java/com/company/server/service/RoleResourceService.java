package com.company.server.service;

import com.company.server.domain.RoleResource;
import com.company.server.domain.RoleResourceExample;
import com.company.server.dto.RoleResourceDto;
import com.company.server.dto.PageDto;
import com.company.server.mapper.RoleResourceMapper;
import com.company.server.util.CopyUtil;
import com.company.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RoleResourceService {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    /**
     * 列表查询
     * @param pageDto
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleResourceExample roleResourceExample = new RoleResourceExample();
        List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(roleResourceExample);
        PageInfo<RoleResource> pageInfo = new PageInfo<>(roleResourceList);
        pageDto.setTotal(pageInfo.getTotal());
        List<RoleResourceDto> roleResourceDtoList = CopyUtil.copyList(roleResourceList, RoleResourceDto.class);
        pageDto.setList(roleResourceDtoList);
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param roleResourceDto
     */
    public void save(RoleResourceDto roleResourceDto) {
        RoleResource roleResource = CopyUtil.copy(roleResourceDto, RoleResource.class);
        if (StringUtils.isEmpty(roleResource.getId())) {
            this.insert(roleResource);
        } else {
            this.update(roleResource);
        }
    }

    /**
     * 新增
     * @param roleResource
     */
    private void insert(RoleResource roleResource) {
        roleResource.setId(UuidUtil.getShortUuid());
        roleResourceMapper.insert(roleResource);
    }

    /**
     * 更新
     * @param roleResource
     */
    private void update(RoleResource roleResource) {
        roleResourceMapper.updateByPrimaryKey(roleResource);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        roleResourceMapper.deleteByPrimaryKey(id);
    }
}