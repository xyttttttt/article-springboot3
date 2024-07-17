package com.xyt.articlespringboot3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyt.articlespringboot3.common.ErrorCode;
import com.xyt.articlespringboot3.entity.LoginUser;
import com.xyt.articlespringboot3.entity.User;
import com.xyt.articlespringboot3.entity.dto.user.UserQueryRequest;
import com.xyt.articlespringboot3.entity.dto.user.UserUpdateRequest;
import com.xyt.articlespringboot3.entity.vo.LoginUserVO;
import com.xyt.articlespringboot3.entity.vo.UserVO;
import com.xyt.articlespringboot3.enums.UserRoleEnum;
import com.xyt.articlespringboot3.exception.BusinessException;
import com.xyt.articlespringboot3.service.UserService;
import com.xyt.articlespringboot3.mapper.UserMapper;
import com.xyt.articlespringboot3.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 16048
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-02-11 17:42:14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Override
    public long userRegister(String username, String password, String checkPassword, String email) {
        // 1. 校验
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (username.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (username.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(password);
            // 3. 插入数据
            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setEmail(email);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getUserId();
        }
    }

    @Override
    public Map<String, String> userLogin(String userAccount, String userPassword) {

        // 传入用户名和密码 将是否认证标记设置为false
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userAccount, userPassword);
// 实现登录逻辑，此时就会去调用 loadUserByUsername方法
// 返回的 Authentication 其实就是 UserDetails
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            log.error("用户名或密码错误！");
            throw new BusinessException(ErrorCode.USER_LOGIN_ERROR);
        }
        //认证没通过，给出信息
        if (authenticate == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户名或密码错误");
        }
        //生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", loginUser.getUser().getUsername());
        claims.put("role", loginUser.getUser().getUserRole());
        String token = JwtUtils.createToken(claims, String.valueOf(loginUser.getUser().getUserId()));

        String refreshToken = JwtUtils.createRefreshToken(claims, String.valueOf(loginUser.getUser().getUserId()));
        //将完整的用户信息存入redis
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("refresh-token",refreshToken);
        return map;
    }


    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     */
    @Override
    public User getLoginUser() {
        // 如果用户已登录，则会返回一个非空的Authentication对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 假设UserDetails实现了getUsername()方法
            String username = userDetails.getUsername();
            // 在这里调用你的服务来获取完整的User实体
            return myUserDetailsService.findUserByUsername(username);
        }
        return null;
    }

    @Override
    public UserVO getUserVOById(long id) {
        User user = baseMapper.selectById(id);
        return getUserVO(user);
    }


    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     */


    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();

        String userName = userQueryRequest.getUsername();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userName), "username", userName);

        return queryWrapper;
    }

    @Override
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest,user);
        baseMapper.updateById(user);
    }
}




