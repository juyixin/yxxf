package com.eazytec.webapp.springmvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class HandlerExceptionResolver implements org.springframework.web.servlet.HandlerExceptionResolver{

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
		
		e.printStackTrace();
		
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("../../error", model);
	}
}
