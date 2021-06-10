package com.company.server.service;

import com.company.server.domain.Section;
import com.company.server.domain.SectionExample;
import com.company.server.dto.SectionDto;
import com.company.server.dto.PageDto;
import com.company.server.enums.SectionChargeEnum;
import com.company.server.mapper.SectionMapper;
import com.company.server.util.CopyUtil;
import com.company.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Date;

@Service
public class SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    /**
     * 列表查询
     * @param pageDto
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        SectionExample sectionExample = new SectionExample();
        sectionExample.setOrderByClause("sort asc");
        List<Section> sectionList = sectionMapper.selectByExample(sectionExample);
        PageInfo<Section> pageInfo = new PageInfo<>(sectionList);
        pageDto.setTotal(pageInfo.getTotal());
        List<SectionDto> sectionDtoList = CopyUtil.copyList(sectionList, SectionDto.class);
        pageDto.setList(sectionDtoList);
    }

    /**
     * 保存，id有值时更新，无值时新增
     * @param sectionDto
     */
    public void save(SectionDto sectionDto) {
        Section section = CopyUtil.copy(sectionDto, Section.class);
        if (StringUtils.isEmpty(section.getId())) {
            this.insert(section);
        } else {
            this.update(section);
        }
    }

    /**
     * 新增
     * @param section
     */
    private void insert(Section section) {
        Date now = new Date();
        section.setCreatedAt(now);
        section.setUpdatedAt(now);
        section.setId(UuidUtil.getShortUuid());
        section.setCharge(SectionChargeEnum.CHARGE.getCode());
        sectionMapper.insert(section);
    }

    /**
     * 更新
     * @param section
     */
    private void update(Section section) {
        section.setUpdatedAt(new Date());
        sectionMapper.updateByPrimaryKey(section);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        sectionMapper.deleteByPrimaryKey(id);
    }
}