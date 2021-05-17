package com.roger.spider.web.spider;

import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider.web.dao.QuestionDao;
import com.roger.spider.web.entity.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class MyBatisPipeline implements Pipeline {

    private static final Logger LOGGER= LoggerFactory.getLogger(MyBatisPipeline.class);
    @Autowired
    private QuestionDao questionDao;

    private AtomicLong totalCounter=new AtomicLong(0);
    private AtomicLong newDataCounter=new AtomicLong(0);

    public void recover(){
        totalCounter.set(0);
        newDataCounter.set(0);
    }

    public AtomicLong getTotalCounter() {
        return totalCounter;
    }

    public AtomicLong getNewDataCounter() {
        return newDataCounter;
    }



    @Override
    public void process(Result result)  {
        Question question=result.get("question");
        if(question!=null){
            totalCounter.incrementAndGet();
            try {
                int update=questionDao.insert(question);
                if(update>0){
                    newDataCounter.incrementAndGet();
                }
            }catch (Exception e){
                LOGGER.warn("Failed to insert [{}]:{}",question,e.getMessage());
            }
        }
    }
}
