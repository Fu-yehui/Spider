package com.roger.spider.web.controller;

import com.roger.spider.web.dto.Result;
import com.roger.spider.web.entity.Question;
import com.roger.spider.web.service.QuestionService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
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
            return "list";
        }
        Question question = questionService.queryById(id);
        if(question==null){
            return "list";
        }
        model.addAttribute("question",question);
        return "detail";
    }

    @RequestMapping(value = "/{id}/delete",
                    method=RequestMethod.GET)
    public String deleteById(@PathVariable("id") long id,Model model){
        int update=questionService.deleteById(id);
        if(update>0) {
            model.addAttribute("result", new Result<Boolean>(true, "Question deleted successfully！"));
        }else{
            model.addAttribute("result", new Result<Boolean>(false, "Failed to delete question！"));
        }
        return "list";
    }

    @RequestMapping(value = "/{id}/update",
                method=RequestMethod.GET)
    public String updateById(@PathVariable("id")Long id,Model model){
        if(id==null){
            return "list";
        }
        Question question = questionService.queryById(id);
        if(question==null){
            model.addAttribute("result",new Result<Boolean>(false,"Cannot find the specified question！"));
            return "list";
        }
        model.addAttribute("question",question);
        return "update";
    }

    @RequestMapping(value = "/{id}/update",
            method=RequestMethod.POST)
    public String updateById(@RequestBody @Valid Question question, Errors errors,@PathVariable("id")Long id, Model model){
        if(question==null || id==null){
            return "forward:/id/update";
        }
        if(errors.hasErrors()){
            List<ObjectError> objectErrors=errors.getAllErrors();
            StringBuilder sb=new StringBuilder();
            objectErrors.stream()
                    .forEach( objectError -> sb.append(objectError.getDefaultMessage()+";"));
            model.addAttribute("result",new Result<Boolean>(false,sb.toString()));
            return "forward:/id/update";
        }
        int update=questionService.updateById(id,question);
        if(update>0) {
            model.addAttribute("result", new Result<Boolean>(true, "Question updated successfully！"));
        }else{
            model.addAttribute("result", new Result<Boolean>(false, "Failed to update question！"));
        }
        Question newQuestion = questionService.queryById(id);
        model.addAttribute("question",newQuestion);
        return "detail";
    }

    @RequestMapping(value = "/insert",
            method=RequestMethod.GET)
    public String insert(){
        return "insert";
    }
    @RequestMapping(value = "/insert",
            method=RequestMethod.POST)
    public String insert(@RequestBody @Valid Question question,Errors errors,Model model){
        if(question==null){
            return "insert";
        }
        if(errors.hasErrors()){
            List<ObjectError> objectErrors=errors.getAllErrors();
            StringBuilder sb=new StringBuilder();
            objectErrors.stream()
                    .forEach( objectError -> sb.append(objectError.getDefaultMessage()+";"));
            model.addAttribute("result",new Result<Boolean>(false,sb.toString()));
            return "insert";
        }
        int update=questionService.insert(question);
        if(update>0) {
            model.addAttribute("result", new Result<Boolean>(true, "Question inserted successfully！"));
        }else{
            model.addAttribute("result", new Result<Boolean>(false, "Failed to insert question！"));
        }
        return "list";
    }

}
