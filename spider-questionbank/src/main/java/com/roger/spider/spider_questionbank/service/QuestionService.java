package com.roger.spider.spider_questionbank.service;

import com.roger.spider.spider_questionbank.entity.Question;

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
}
