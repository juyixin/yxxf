package com.eazytec.webapp.controller;

/**
 * @author Rajasekar
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.common.util.TodoUtil;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.model.User;

@Controller
public class ToDoController {
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    public VelocityEngine velocityEngine;
    
    private TaskService rtTaskService;
    
    /**
   	 * To show the compose sms 
   	 * * @param model
   	 * @return
   	 */
    @RequestMapping(value = "bpm/todo/crateTodo",method = RequestMethod.GET)
    public String showCreateTodo(ModelMap model) {
		return "todo/createTodo";
    }
    
    /**
	 * To News List
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/todo/todoList",method = RequestMethod.GET)
	   public ModelAndView newsList(ModelMap model) {
		String script = TodoUtil.generateScriptForTodoList(null, velocityEngine);
		model.addAttribute("script", script);
		model.addAttribute("type", "todoList");
		return new ModelAndView("todo/todoList",model);
		
	   }
	
	/**
	 * To News List
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/todo/getToDOList",method = RequestMethod.GET)
	   public @ResponseBody List<Map<String, Object>> getToDOList(ModelMap model, HttpServletRequest request) {	
		List<Map<String, Object>> taskDetails = new ArrayList<Map<String, Object>>();
		try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Task> tasks = rtTaskService.getToDoListByUser(user);
			taskDetails = rtTaskService.resolveTaskDetails(tasks, user, "mybucket");
			log.info("ToDo List Retrived Successfully");
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}		
		return taskDetails;
	   }
	
    
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

	@Autowired
	public void setRtTaskService(TaskService rtTaskService) {
		this.rtTaskService = rtTaskService;
	}
}
