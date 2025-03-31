package com.feng.geek.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.model.domain.Post;
import com.feng.geek.model.request.post.PostQueryRequest;
import com.feng.geek.model.response.PostShowReponse;
import com.feng.geek.service.PostService;
import com.feng.geek.mapper.PostMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【post】的数据库操作Service实现
* @createDate 2025-03-30 21:25:44
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

    @Override
    public Page<Post> searchFromEs(PostQueryRequest postQueryRequest) {
        return null;
    }

    @Override
    public Page<PostShowReponse> getPostVOPage(Page<Post> postPage, HttpServletRequest request) {
        return null;
    }
}




