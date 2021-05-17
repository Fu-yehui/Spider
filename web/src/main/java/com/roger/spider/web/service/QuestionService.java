package com.roger.spider.web.service;

import com.roger.spider.web.entity.Question;

import java.util.List;

public interface QuestionService {

    /**
     * 查询所有的题目
     * @return
     */
    List<Question> queryAll();

    /**
     * 根据偏移量查询题目
     * @param offset
     * @param limit
     * @return
     */
    List<Question> queryAll(int offset,int limit);

    /**
     * 根据id查询题目
     * @param id
     * @return
     */
    Question queryById(Long id);

    /**
     * 根据id删除题目
     * @param id
     */
    int deleteById(Long id);

    /**
     * 根据id修改题目
     * @param id
     * @param question
     */
    int updateById(Long id,Question question);

    /**
     * 添加题目
     * @param question
     */
    int insert(Question question);
}
