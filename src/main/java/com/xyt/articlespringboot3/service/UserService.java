package com.xyt.articlespringboot3.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xyt.articlespringboot3.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyt.articlespringboot3.entity.dto.user.UserQueryRequest;
import com.xyt.articlespringboot3.entity.dto.user.UserUpdateRequest;
import com.xyt.articlespringboot3.entity.vo.LoginUserVO;
import com.xyt.articlespringboot3.entity.vo.UserVO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/**
* @author 16048
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-02-11 17:42:14
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword,String email);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    Map<String,String> userLogin(String userAccount, String userPassword);

    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     */
    User getLoginUser();



    UserVO getUserVOById(long id);
    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);


    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    void updateUser(UserUpdateRequest userUpdateRequest);
}
