package com.company.server.service;

import com.company.server.domain.User;
import com.company.server.domain.UserExample;
import com.company.server.dto.LoginUserDto;
import com.company.server.dto.PageDto;
import com.company.server.dto.UserDto;
import com.company.server.exception.BusinessException;
import com.company.server.exception.BusinessExceptionCode;
import com.company.server.mapper.UserMapper;
import com.company.server.util.CopyUtil;
import com.company.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 列表查询
     *
     * @param pageDto
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        UserExample userExample = new UserExample();
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageDto.setTotal(pageInfo.getTotal());
        List<UserDto> userDtoList = CopyUtil.copyList(userList, UserDto.class);
        pageDto.setList(userDtoList);
    }

    /**
     * 保存，id有值时更新，无值时新增
     *
     * @param userDto
     */
    public void save(UserDto userDto) {
        User user = CopyUtil.copy(userDto, User.class);
        if (StringUtils.isEmpty(user.getId())) {
            this.insert(user);
        } else {
            this.update(user);
        }
    }

    /**
     * 新增
     *
     * @param user
     */
    private void insert(User user) {
        user.setId(UuidUtil.getShortUuid());
        // 如果user变量已经存在，则从数据库中查出来的，我们可以约定使用Db结尾，用userDb
        User userDb = this.selectByLoginName(user.getLoginName());
        if (userDb != null) {
            // 业务上的逻辑校验，在校验不通过时，使用业务异常（自定义异常）
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
        userMapper.insert(user);
    }

    /**
     * 更新
     *
     * @param user
     */
    private void update(User user) {
        user.setPassword(null);
        // mybatis-generator生成的方法里，
        // update By PrimaryKeySelective会对字段进行非空判断，
        // 再更新，如果值为空就不更新，
        // 原理就是利用mybatis的if拼成动态sql
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据登录名查询用户信息
     * <p>
     * 我们要的功能是校验用户名是否存在，所以也可以把返回值改成true或false，
     * 而不是返回User对象，但是这种写法不够通用，所以我们选择返回User
     *
     * @param loginName
     * @return
     */
    public User selectByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    /**
     * 重置密码
     *
     * @param userDto
     */
    public void savePassword(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 登录
     * @param userDto
     */
    public LoginUserDto login(UserDto userDto) {
        User user = selectByLoginName(userDto.getLoginName());
        if (user == null) {
            log.info("用户名不存在, {}", userDto.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (user.getPassword().equals(userDto.getPassword())) {
                // 登录成功
                LoginUserDto loginUserDto = CopyUtil.copy(user, LoginUserDto.class);
                return loginUserDto;
            } else {
                log.info("密码不对, 输入密码：{}, 数据库密码：{}", userDto.getPassword(), user.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}