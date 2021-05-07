package com.roger.spider.spider_questionbank.service.serviceImpl;

import com.roger.spider.spider_questionbank.dao.QuestionDao;
import com.roger.spider.spider_questionbank.entity.Question;
import com.roger.spider.spider_questionbank.exception.SpiderException;
import com.roger.spider.spider_questionbank.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger LOGGER=LoggerFactory.getLogger(QuestionServiceImpl.class);
    @Autowired
    private QuestionDao questionDao;
    /**
     * 查询所有的题目
     *
     * @return
     */
    @Override
    public List<Question> queryAll() {
        return queryAll(0,Integer.MAX_VALUE);
    }

    /**
     * 根据偏移量查询题目
     *
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Question> queryAll(int offset, int limit) {
        try{
            return questionDao.queryAll(offset,limit);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new SpiderException("database occurs error"+e.getMessage());
        }
    }

    /**
     * 根据id查询题目
     *
     * @param id
     * @return
     */
    @Override
    public Question queryById(Long id) {
        try{
            return questionDao.queryById(id);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new SpiderException("database occurs error"+e.getMessage());
        }
    }
}
