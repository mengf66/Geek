package com.feng.geek.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.constant.CommonConstant;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.Question;
import com.feng.geek.model.domain.User;
import com.feng.geek.model.request.question.*;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.model.vo.QuestionVO;
import com.feng.geek.service.QuestionService;
import com.feng.geek.mapper.QuestionMapper;
import com.feng.geek.service.UserService;
import com.feng.geek.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Lenovo
* @description 针对表【question】的数据库操作Service实现
* @createDate 2025-03-23 22:18:12
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private UserService userService;

    @Override
    public R<Long> saveQuestion(QuestionAddRequest questionAddRequest, SafetyUser loginUser) {
        Question question = BeanUtil.copyProperties(questionAddRequest, Question.class);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if(judgeCase != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeCase));
        }
        if(judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        validQuestion(question, true);
        question.setUserId(loginUser.getId());
        question.setFavourNum(0);
        question.setThumbNum(0);
        boolean result = save(question);
        if(!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        long newQuestionId = question.getId();
        return R.ok(newQuestionId);
    }

    /**
     * 重写
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            if(StringUtils.isAnyBlank(title, content, tags)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    @Override
    public boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        Question question = new Question();
        BeanUtil.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if(judgeCase != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeCase));
        }
        if(judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        // 参数校验
        validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = getById(id);
        if(oldQuestion == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = updateById(question);
        return result;
    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        long questionId = question.getId();
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        SafetyUser safetyUser = userService.getSafetyUser(user);
        questionVO.setSafetyUser(safetyUser);
        return questionVO;
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
//        LambdaQueryWrapper<Question> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Question question = BeanUtil.copyProperties(questionQueryRequest, Question.class);
        List<String> tagList = questionQueryRequest.getTags();
        question.setTags(JSONUtil.toJsonStr(tagList));
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
//        lambdaQueryWrapper.like(StringUtils.isNotBlank(title), Question::getTitle, title);
//        lambdaQueryWrapper.like(StringUtils.isNotBlank(content), Question::getContent, content);
//        lambdaQueryWrapper.like(StringUtils.isNotBlank(content), Question::getContent, answer);
//        if (CollUtil.isNotEmpty(tags)) {
//            for (String tag : tags) {
//                lambdaQueryWrapper.like(Question::getTags, "\"" + tag + "\"");
//            }
//        }
//        lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(id), Question::getId, id);
//        lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(userId), Question::getUserId, userId);
//        // 获取 SFunction（Lambda 表达式）
//        SFunction<User, ?> column = SqlUtils.getLambdaExpression(User.class, sortField);
//        lambdaQueryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC));
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(content), "answer", answer);
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> questionIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> questionIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setSafetyUser(userService.getSafetyUser(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

}




