package com.roger.spider.web.dao;

import com.roger.spider.web.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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

    /**
     * 根据id删除题目
     * @param id
     * @return
     */
    int deleteById(@Param("id")Long id);

    /**
     * 根据id修改题目
     * @param id
     * @param question
     * @return
     */
    int updateById(@Param("id")Long id,@Param("question")Question question);
}
