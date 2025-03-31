package com.feng.geek.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.model.domain.Comment;
import com.feng.geek.service.CommentService;
import com.feng.geek.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2025-03-30 21:25:57
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




