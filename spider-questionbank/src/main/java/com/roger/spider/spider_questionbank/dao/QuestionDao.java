package com.roger.spider.spider_questionbank.dao;

import com.roger.spider.spider_questionbank.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
public interface QuestionDao {

    /**
     *  插入question到数据库中
     * @param question
     */
    int insert(Question question);

    /**
     * 根据偏移量查询题目
     * @return
     */
    List<Question> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据id查询题目
      * @param id
     * @return
     */
    Question queryById(@Param("id")Long id);

}
