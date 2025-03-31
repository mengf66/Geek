package com.feng.geek.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.Post;
import com.feng.geek.model.request.post.PostAddRequest;
import com.feng.geek.model.request.post.PostQueryRequest;
import com.feng.geek.model.request.post.PostUpdateRequest;
import com.feng.geek.model.response.PostShowReponse;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.PostFavourService;
import com.feng.geek.service.PostService;
import com.feng.geek.service.PostThumbService;
import com.feng.geek.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private PostFavourService postFavourService;

    @Resource
    private PostThumbService postThumbService;
    /**
     * 帖子添加
     *
     * @param postAddRequest
     * @return
     */
    @PostMapping("/add")
    public R<Long> addPost(@RequestBody PostAddRequest postAddRequest) {
        if(postAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = BeanUtil.copyProperties(postAddRequest, Post.class);
        boolean save = postService.save(post);
        if(!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return R.ok(post.getId());
    }

    /**
     * 帖子更新
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public R<Long> updatePost(@RequestBody PostUpdateRequest postUpdateRequest) {
        if(postUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = BeanUtil.copyProperties(postUpdateRequest, Post.class);
        boolean b = postService.updateById(post);
        if(!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return R.ok(post.getId());
    }

    /**
     * 删除帖子
     *
     * @param postId
     * @param request
     * @return
     */
    @GetMapping("/delete/{postId}")
    public R<String> deletePost(@PathVariable long postId, HttpServletRequest request) {
        if(Long.valueOf(postId) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //只有管理人员和本人可以删帖子
        boolean auth = userService.isAuth(request);
        SafetyUser loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Post post = postService.getById(postId);
        Long createId = post.getCreateId();
        if((auth && (createId.longValue() == loginUserId))) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return R.ok("删除成功");
    }

    /**
     * 根据帖子Id查询帖子
     *
     * @param postId
     * @return
     */
    @GetMapping("/show/{postId}")
    public R<PostShowReponse> getPost(@PathVariable long postId) {
        if(Long.valueOf(postId) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = postService.getById(postId);
        PostShowReponse postShowReponse = BeanUtil.copyProperties(post, PostShowReponse.class);
        return R.ok(postShowReponse);
    }

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    public R<Page<PostShowReponse>> searchPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                         HttpServletRequest request) {
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        if(size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        return R.ok(postService.getPostVOPage(postPage, request));
    }

    @GetMapping("/collect")
    public R<String> collectPost(long postId, HttpServletRequest request) {
        if(Long.valueOf(postId) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SafetyUser loginUser = userService.getLoginUser(request);
        boolean success = postFavourService.addFavour(postId, loginUser);
        if(!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return R.ok("收藏成功");
    }
}
