package com.company.server.service;

import com.company.server.domain.RoleUser;
import com.company.server.domain.RoleUserExample;
import com.company.server.dto.RoleUserDto;
import com.company.server.dto.PageDto;
import com.company.server.mapper.RoleUserMapper;
import com.company.server.util.CopyUtil;
import com.company.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RoleUserService {

    @Autowired
    private RoleUserMapper roleUserMapper;

    /**
     * 列表查询
     * @param pageDto
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleUserExample roleUserExample = new RoleUserExample();
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(roleUserExample);
        PageInfo<RoleUser> pageInfo = new PageInfo<>(roleUserList);
        pageDto.setTotal(pageInfo.getTotal());
        List<RoleUserDto> roleUserDtoList = CopyUtil.copyList(roleUserList, RoleUserDto.class);
        pageDto.setList(roleUserDtoList);
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param roleUserDto
     */
    public void save(RoleUserDto roleUserDto) {
        RoleUser roleUser = CopyUtil.copy(roleUserDto, RoleUser.class);
        if (StringUtils.isEmpty(roleUser.getId())) {
            this.insert(roleUser);
        } else {
            this.update(roleUser);
        }
    }

    /**
     * 新增
     * @param roleUser
     */
    private void insert(RoleUser roleUser) {
        roleUser.setId(UuidUtil.getShortUuid());
        roleUserMapper.insert(roleUser);
    }

    /**
     * 更新
     * @param roleUser
     */
    private void update(RoleUser roleUser) {
        roleUserMapper.updateByPrimaryKey(roleUser);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        roleUserMapper.deleteByPrimaryKey(id);
    }
}