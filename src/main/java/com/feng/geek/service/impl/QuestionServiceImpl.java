package com.feng.geek.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.model.domain.Question;
import com.feng.geek.service.QuestionService;
import com.feng.geek.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【question】的数据库操作Service实现
* @createDate 2025-03-23 22:18:12
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




