package com.feng.geek.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.geek.model.domain.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.geek.model.request.post.PostQueryRequest;
import com.feng.geek.model.response.PostShowReponse;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【post】的数据库操作Service
* @createDate 2025-03-30 21:25:44
*/
public interface PostService extends IService<Post> {

    Page<Post> searchFromEs(PostQueryRequest postQueryRequest);

    Page<PostShowReponse> getPostVOPage(Page<Post> postPage, HttpServletRequest request);
}
