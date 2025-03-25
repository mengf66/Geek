package com.feng.geek.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.enums.UserRoleEnum;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.User;
import com.feng.geek.model.request.user.*;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.UserService;
import com.feng.geek.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, Object> redisTemplate;

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
        //利用redis分布式锁，防止同一个用户添加过多次
        try {
            if(lock.tryLock(0, -1, TimeUnit.MINUTES)){
                user = lambdaQuery().eq(User::getEmail, email).one();
                if (user != null) {
                    throw new BusinessException(ErrorCode.ALREADY_REGISTER, "用户已存在");
                }
                user = BeanUtil.copyProperties(userRegister, User.class);
                passWord = DigestUtils.md5DigestAsHex((SALT + passWord).getBytes());
                user.setUserPassword(passWord);
                user.setIntroduction("这里什么都没有哦~");
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

    /**
     * 修改个人信息实现
     *
     * @param userUpdateRequest
     * @return
     */
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserUpdateRequest userUpdateRequest, User loginUser) {
        Long userId = loginUser.getId();
        //校验，若是没有更改的字段，就不进行插入
        UserUpdateRequest login = BeanUtil.copyProperties(loginUser, UserUpdateRequest.class);
        UserUpdateRequest updateUser = isUpdate(userUpdateRequest, login);
        if(updateUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "并没有更改字段");
        }
        User user = BeanUtil.copyProperties(updateUser, User.class);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, userId);
        boolean update = update(user, lambdaQueryWrapper);
        return update;
    }

    /**
     * 修改密码实现
     *
     * @param userSecretRequest
     * @param loginUser
     * @return
     */
    @Override
    public User updateUserSecret(UserSecretRequest userSecretRequest, User loginUser) {
        String userAgainPassword = userSecretRequest.getUserAgainPassword();
        String userPassword = userSecretRequest.getUserPassword();
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码过短");
        }
        Long userId = loginUser.getId();
        loginUser = getById(userId);
        userPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        userAgainPassword = DigestUtils.md5DigestAsHex((SALT + userAgainPassword).getBytes());
        if(!loginUser.getUserPassword().equals(userAgainPassword)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if(userPassword.equals(loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能使用上次的密码");
        }
        loginUser.setUserPassword(userPassword);
        boolean b = updateById(loginUser);
        if(b) {
            return loginUser;
        } else {
            return null;
        }
    }

    @Override
    public SafetyUser getLoginUser(HttpServletRequest request) {
        SafetyUser loginUser = (SafetyUser) request.getSession().getAttribute(USER_LOGIN_STATE);
        return loginUser;
    }

    /**
     * 是否有权限
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAuth(HttpServletRequest request) {
        // 仅管理员可查询
        SafetyUser loginUser = (SafetyUser) request.getSession().getAttribute(USER_LOGIN_STATE);
        return isAuth(loginUser);
    }

    @Override
    public boolean isAuth(SafetyUser user) {
        return user != null && (UserRoleEnum.ADMIN.getValue() == user.getUserRole() || UserRoleEnum.BOSS.getValue() == user.getUserRole());
    }

    /**
     * 是否是管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        SafetyUser loginUser = (SafetyUser) request.getSession().getAttribute(USER_LOGIN_STATE);
        return isAdmin(loginUser);
    }

    @Override
    public boolean isAdmin(SafetyUser user) {
        return user != null && (UserRoleEnum.ADMIN.getValue() == user.getUserRole());
    }

    /**
     * 是否是社长
     *
     * @param request
     * @return
     */
    @Override
    public boolean isBoss(HttpServletRequest request) {
        // 仅管理员可查询
        SafetyUser loginUser = (SafetyUser) request.getSession().getAttribute(USER_LOGIN_STATE);
        return isBoss(loginUser);
    }

    @Override
    public boolean isBoss(SafetyUser user) {
        return user != null && (UserRoleEnum.BOSS.getValue() == user.getUserRole());
    }

    @Override
    public Boolean updateRole(UserRoleRequest userRoleRequest) {
        long userId = userRoleRequest.getId();
        int role = userRoleRequest.getRole();
        User user = getById(userId);
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        if(role < 0 || role > 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "权限不存在");
        }
        if(role == 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无权设置他人为社长");
        }
        user.setUserRole(role);
        boolean b = updateById(user);
        if(!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return b;
    }

    /**
     * 更改校验，是否有改变
     *
     * @param updateUser
     * @param loginUser
     * @return
     */
    private UserUpdateRequest isUpdate(UserUpdateRequest updateUser, UserUpdateRequest loginUser) {
        //如果没有更改，flag始终为false
        boolean flag = false;
        String username = updateUser.getUsername();
        String avatarUrl = updateUser.getAvatarUrl();
        Integer gender = updateUser.getGender();
        String introduction = updateUser.getIntroduction();
        String tags = updateUser.getTags();
//        if(username == null && avatarUrl == null && gender == null && introduction == null && tags == null) {
//            return null;
//        }

        //将更改的地方在loginUser的内部进行替换，这样就不会影响未更改的字段
        if(StringUtils.isNotBlank(username) && !username.equals(loginUser.getUsername())) {
            flag = true;
            loginUser.setUsername(username);
        }
        if(StringUtils.isNotBlank(avatarUrl) && !avatarUrl.equals(loginUser.getAvatarUrl())) {
            flag = true;
            loginUser.setAvatarUrl(avatarUrl);
        }
        if(gender != null && gender.intValue() != loginUser.getGender().intValue()) {
            flag = true;
            loginUser.setGender(gender);
        }
        if(StringUtils.isNotBlank(introduction) && !introduction.equals(loginUser.getIntroduction())) {
            flag = true;
            loginUser.setIntroduction(introduction);
        }
        if(StringUtils.isNotBlank(tags) && !tags.equals(loginUser.getTags())) {
            flag = true;
            loginUser.setTags(tags);
        }
        if(!flag) {
            return null;
        }
        return loginUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public SafetyUser getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        SafetyUser safetyUser = BeanUtil.copyProperties(originUser, SafetyUser.class);
        return safetyUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        SafetyUser currentUser = (SafetyUser) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }
}




