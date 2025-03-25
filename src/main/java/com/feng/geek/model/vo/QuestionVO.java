package com.feng.geek.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.feng.geek.model.domain.Question;
import com.feng.geek.model.request.question.JudgeConfig;
import com.feng.geek.model.response.SafetyUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName question
 */
@Data
public class QuestionVO implements Serializable {

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json数组)
     */
    private List<String> tagList;


    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题配置（json对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户Id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date updateTime;

    private SafetyUser safetyUser;

    public static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = BeanUtil.copyProperties(questionVO, Question.class);
        List<String> tagList = questionVO.getTagList();
        question.setTags(JSONUtil.toJsonStr(tagList));
        JudgeConfig questionJudgeConfig = questionVO.getJudgeConfig();
        if(questionJudgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(questionJudgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
//        QuestionVO questionVO = BeanUtil.copyProperties(question, QuestionVO.class);
        QuestionVO questionVO = new QuestionVO();
        questionVO.setId(question.getId());
        questionVO.setTitle(question.getTitle());
        questionVO.setContent(question.getContent());
        questionVO.setSubmitNum(question.getSubmitNum());
        questionVO.setAcceptedNum(question.getAcceptedNum());
        questionVO.setThumbNum(question.getThumbNum());
        questionVO.setFavourNum(question.getFavourNum());
        questionVO.setUserId(question.getUserId());
        questionVO.setCreateTime(question.getCreateTime());
        questionVO.setUpdateTime(question.getUpdateTime());
        questionVO.setTagList(JSONUtil.toList(question.getTags(), String.class));
        String judgeConfig = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));
        return questionVO;
    }
}