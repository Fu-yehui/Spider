package com.roger.spider.spider_questionbank.dao;
import com.roger.spider.spider_questionbank.entity.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class QuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void insert() {
        String title="通-T1627]最大公约数";
        String description="给出两个正整数 A,B，求它们的最大公约数。\n";
        String input="输入共两行，第一行一个正整数 A，第二行一个正整数 B。\n";
        String output="在第一行输出一个整数，表示 A,B 的最大公约数。\n";
        String sampleInput="18\n" +
                "24";
        String sampleOutput="6";
        String prompt="数据范围与提示： 对于 60% 的数据，1≤A,B≤1018； 对于 100% 的数据，1≤A,B≤103000 。\n";
        String label="数学";
        Question question=new Question(title,description,input,output,sampleInput,sampleOutput,prompt,label);
        System.out.println(questionDao.insert(question));
    }

    @Test
    public void queryAll() {
        List<Question> list=questionDao.queryAll(0,1000);
        System.out.println(list);
    }
}