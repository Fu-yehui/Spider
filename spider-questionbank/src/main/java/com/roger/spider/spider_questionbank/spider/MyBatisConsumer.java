package com.roger.spider.spider_questionbank.spider;

import com.roger.spider.spider_questionbank.dao.QuestionDao;
import com.roger.spider.spider_questionbank.entity.Question;
import com.xiepuhuan.reptile.consumer.Consumer;
import com.xiepuhuan.reptile.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class MyBatisConsumer implements Consumer {

    private static final Logger LOGGER= LoggerFactory.getLogger(MyBatisConsumer.class);
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
    public void consume(Result result)  {
        Question question=result.getResult("question");
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
