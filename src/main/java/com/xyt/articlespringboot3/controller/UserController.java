package com.xyt.articlespringboot3.controller;

import com.xyt.articlespringboot3.common.BaseResponse;
import com.xyt.articlespringboot3.common.DeleteRequest;
import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.common.ResultUtils;
import com.xyt.articlespringboot3.entity.User;
import com.xyt.articlespringboot3.entity.dto.user.UserLoginRequest;
import com.xyt.articlespringboot3.entity.dto.user.UserRegisterRequest;
import com.xyt.articlespringboot3.entity.dto.user.UserUpdateRequest;
import com.xyt.articlespringboot3.entity.vo.LoginUserVO;
import com.xyt.articlespringboot3.exception.BusinessException;
import com.xyt.articlespringboot3.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        if (StringUtils.isAnyBlank(username, password, checkPassword,email)) {
            return null;
        }
        long result = userService.userRegister(username, password, checkPassword,email);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<Map<String,String>> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Map<String,String> map = userService.userLogin(username, password);
        return ResultUtils.success(map);
    }


    /**
     * 获取当前登录用户
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user','admin')")
    @GetMapping("/me")
    public BaseResponse<LoginUserVO> getLoginUser() {
        User user = userService.getLoginUser();
        return ResultUtils.success(userService.getLoginUserVO(user));
    }


    /**
     * 删除用户
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('user','admin')")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('user','admin')")
    public BaseResponse<String> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
        return ResultUtils.success("更新成功");
    }
}
