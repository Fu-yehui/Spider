package com.roger.spider.spider_questionbank.controller;

import com.roger.spider.spider_questionbank.dto.Counter;
import com.roger.spider.spider_questionbank.dto.Result;
import com.roger.spider.spider_questionbank.entity.Question;
import com.roger.spider.spider_questionbank.entity.SpiderConfig;
import com.roger.spider.spider_questionbank.exception.SpiderCloseException;
import com.roger.spider.spider_questionbank.service.QuestionService;
import com.roger.spider.spider_questionbank.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author roger
 */
@Controller
@RequestMapping("/")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;
    @Autowired
    private QuestionService questionService;


    @RequestMapping(value = "/management",method = RequestMethod.GET)
    public String spider(){
        return "spider";
    }

    @RequestMapping(value = "/fetch",method = RequestMethod.GET)
    public String fetch(Model model){
        spiderService.fetch();
        List<Question> list=questionService.queryAll();
        model.addAttribute("list",list);
        return "list";
    }

    @RequestMapping(value = "/fetch",method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> fetch(@Valid SpiderConfig spiderConfig, Errors errors){
        if(errors.hasErrors()){
            List<ObjectError> objectErrors=errors.getAllErrors();
            StringBuilder sb=new StringBuilder();
            objectErrors
                    .stream()
                    .forEach( objectError -> sb.append(objectError.getDefaultMessage()+";"));
            return new Result<Boolean>(false,sb.toString());
        }

        spiderService.fetch(spiderConfig.getThreadCount(),spiderConfig.getRetryCount(),spiderConfig.getSleepTime(),spiderConfig.isUseProxy(),spiderConfig.getUrl());

        return new Result<Boolean>(true,true);
    }




    @RequestMapping(value = "/stop",method = RequestMethod.GET)
    @ResponseBody
    public Result<Counter> stop() {
        try {
            Counter counter=spiderService.count();
            spiderService.stop();
            return new Result<Counter>(true,counter);
        }catch (SpiderCloseException e){
            return new Result<Counter>(false,e.getMessage());
        }
    }


}
