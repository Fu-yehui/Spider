package com.roger.spider.web.controller;

import com.roger.spider.web.dto.Counter;
import com.roger.spider.web.dto.Result;
import com.roger.spider.web.dto.StringDto;
import com.roger.spider.web.entity.Question;
import com.roger.spider.web.entity.SpiderConfig;
import com.roger.spider.web.exception.SpiderCloseException;
import com.roger.spider.web.service.QuestionService;
import com.roger.spider.web.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author roger
 */
@Controller
@RequestMapping("/spider")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @RequestMapping(value = "/management",method = RequestMethod.GET)
    public String spider(){
        return "spiderManagement";
    }

    @RequestMapping(value = "/fetch",method = RequestMethod.GET)
    @ResponseBody
    public Result<StringDto> fetch(Model model) {
        try {
            new Thread(() -> spiderService.fetch()).start();
        } catch (Exception e) {
            return new Result<StringDto>(false,"The spider failed to start!");
        }
        return new Result<StringDto>(true,new StringDto("The spider started successfully!"));

    }

    @RequestMapping(value = "/fetchByConfig",method = RequestMethod.POST)
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
