package com.feng.geek.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.Activity;
import com.feng.geek.model.domain.Post;
import com.feng.geek.model.request.activity.ActivityAddRequest;
import com.feng.geek.model.request.activity.ActivityUpdateRequest;
import com.feng.geek.model.request.post.PostUpdateRequest;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.ActivityService;
import com.feng.geek.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @Resource
    private UserService userService;

    /**
     * 任务添加
     *
     * @param activityAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public R<Long> addActivity(@RequestBody ActivityAddRequest activityAddRequest, HttpServletRequest request) {
        if(activityAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SafetyUser loginUser = userService.getLoginUser(request);
        if(!userService.isAuth(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Activity activity = BeanUtil.copyProperties(activityAddRequest, Activity.class);
        activity.setUserId(loginUser.getId());
        boolean save = activityService.save(activity);
        if(!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return R.ok(activity.getId());
    }

    /**
     * 任务修改
     *
     * @param activityUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public R<Long> updatePost(@RequestBody ActivityUpdateRequest activityUpdateRequest) {
        if(activityUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Activity activity = BeanUtil.copyProperties(activityUpdateRequest, Activity.class);
        boolean b = activityService.updateById(activity);
        if(!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return R.ok(activity.getId());
    }

    /**
     * 删除帖子
     *
     * @param activityId
     * @param request
     * @return
     */
    @GetMapping("/delete/{activityId}")
    public R<String> deletePost(@PathVariable long activityId, HttpServletRequest request) {
        if(Long.valueOf(activityId) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //只有管理人员和本人可以删帖子
        boolean auth = userService.isAuth(request);
        if(!auth) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        SafetyUser loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Activity activity = activityService.getById(activityId);
        return R.ok("删除成功");
    }


}
