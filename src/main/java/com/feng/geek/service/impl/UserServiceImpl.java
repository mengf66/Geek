package com.feng.geek.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.config.RedissonConfig;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.User;
import com.feng.geek.model.request.UserLoginRequest;
import com.feng.geek.model.request.UserRegisterRequest;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.UserService;
import com.feng.geek.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

import static com.feng.geek.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-03-21 21:44:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "fengmeng";

    @Resource
    private RedissonClient redissonClient;

    /**
     * 注册功能实现
     *
     * @param userRegister
     * @return
     */
    @Override
    public R<String> register(UserRegisterRequest userRegister) {
        String passWord = userRegister.getUserPassword();
        String email = userRegister.getEmail();
        if(StringUtils.isAnyBlank(passWord, email)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        if (passWord.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        RLock lock = redissonClient.getLock("geek:userservice:register:lock");
        User user = null;
        try {
            if(lock.tryLock(1, -1, TimeUnit.MINUTES)){
                user = lambdaQuery().eq(User::getEmail, email).one();
                if (user != null) {
                    throw new BusinessException(ErrorCode.ALREADY_REGISTER, "用户已存在");
                }
                user = BeanUtil.copyProperties(userRegister, User.class);
                passWord = DigestUtils.md5DigestAsHex((SALT + passWord).getBytes());
                user.setUserPassword(passWord);
                boolean save = save(user);
                if(!save) {
                    throw new BusinessException(ErrorCode.ADDUSER_FAIL, "添加用户失败");
                }
                return R.ok("注册成功");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return R.failed("注册失败");
    }

    /**
     * 登录功能实现
     *
     * @param userLogin
     * @return
     */
    @Override
    public R<SafetyUser> login(UserLoginRequest userLogin, HttpServletRequest request) {
//        1.校验参数
        String email = userLogin.getEmail();
        String password = userLogin.getUserPassword();
        if(StringUtils.isAnyBlank(email, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        if(password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不足");
        }
//        2.查询数据库获取
        User user = lambdaQuery().eq(User::getEmail, email).one();
        if(user == null) {
            throw new BusinessException(ErrorCode.UNEXIST_REGISTER, "用户不存在");
        }
//        3.密码比对
        String userPassword = user.getUserPassword();
        password = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        if(!password.equals(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        SafetyUser safetyUser = BeanUtil.copyProperties(user, SafetyUser.class);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return R.ok(safetyUser);
    }

    /**
     * 登出功能实现
     *
     * @param request
     * @return
     */
    @Override
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return R.ok("登出成功");
    }

    /**
     * 查看个人信息实现
     *
     * @param request
     * @return
     */
    @Override
    public R<SafetyUser> show(HttpServletRequest request) {
        SafetyUser safetyUser = (SafetyUser) request.getSession().getAttribute(USER_LOGIN_STATE);
        return R.ok(safetyUser);
    }
}




