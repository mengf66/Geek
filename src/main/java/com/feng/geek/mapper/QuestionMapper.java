package com.feng.geek.mapper;

import com.feng.geek.model.domain.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Lenovo
* @description 针对表【question】的数据库操作Mapper
* @createDate 2025-03-23 22:18:12
* @Entity com.feng.geek.model.domain.Question
*/
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

}




