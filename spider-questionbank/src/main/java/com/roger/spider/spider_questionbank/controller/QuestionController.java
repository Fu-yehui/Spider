package com.roger.spider.spider_questionbank.controller;

import com.roger.spider.spider_questionbank.dto.Result;
import com.roger.spider.spider_questionbank.entity.Question;
import com.roger.spider.spider_questionbank.exception.SpiderException;
import com.roger.spider.spider_questionbank.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        List<Question> list=questionService.queryAll();
        model.addAttribute("list",list);
        return "list";
    }

    @RequestMapping(value = "/{id}/detail",
            method=RequestMethod.GET)
    public String detail(@PathVariable("id")Long id,Model model){
        if(id==null){
            return "redirect:/list";
        }
        Question question = questionService.queryById(id);
        if(question==null){
            return "forward:/list";
        }
        model.addAttribute("question",question);
        return "detail";
    }
}
