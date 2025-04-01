package com.feng.geek.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.model.domain.Activity;
import com.feng.geek.service.ActivityService;
import com.feng.geek.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【activity】的数据库操作Service实现
* @createDate 2025-04-01 21:36:05
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
    implements ActivityService{

}




