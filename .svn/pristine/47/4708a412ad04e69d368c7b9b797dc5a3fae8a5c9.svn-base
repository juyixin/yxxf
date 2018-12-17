package com.eazytec.webapp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.runtime.agency.service.AgencyService;
import com.eazytec.bpm.runtime.holiday.service.HolidayService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.Holiday;
import com.eazytec.model.User;
import com.eazytec.service.LookupManager;

@Controller
public class HolidayController extends BaseFormController{
	
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());
	
	private HolidayService holidayService;
	
	@Autowired
	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	/**
	 * To bind date and string fields from controller to jsp
	 * 
	 * @param request
	 * @param binder
	 * 
	 * @return
	 */
//    @Override
//    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
//        binder.registerCustomEditor(Integer.class, null,
//                new CustomNumberEditor(Integer.class, null, true));
//    }
	
	 @RequestMapping(value = "bpm/holiday/holidayForm",method = RequestMethod.GET)
	public ModelAndView showHolidayForm(HttpServletRequest request,ModelMap model)throws Exception {
		 Calendar calender = Calendar.getInstance();
		Map<String,Object> holidays = new HashMap<String, Object>();
		if(request.getParameter("selectedYear")!=null){
			holidays = holidayService.getAllHolidaysByYear(Integer.parseInt(request.getParameter("selectedYear")));
			model.addAttribute("currentYear", request.getParameter("selectedYear"));
		}else{
			holidays = holidayService.getAllHolidaysByYear(calender.get(Calendar.YEAR));
			model.addAttribute("currentYear", calender.get(Calendar.YEAR));
		}
		
		
		if(holidays.isEmpty()){
			model.addAttribute("isEdit", false);
		}else{
			/*JSONArray jsonArray =CommonUtil.convertListStringToJsonArray(holidays);
			String selectedDateString=CommonUtil.convertListStringToString(holidays);
			model.addAttribute("selectedDateList", jsonArray);
			model.addAttribute("selectedDateString", selectedDateString);
			model.addAttribute("currentYear", calender.get(Calendar.YEAR));*/
			String selectedWeekends=String.valueOf(holidays.get("weekEnds"));
			holidays.remove("weekEnds");
			JSONObject jsonObject  =CommonUtil.convertMapToJsonObj(holidays);
			String selectedDateString=CommonUtil.convertDateMapToString(holidays);
			model.addAttribute("holidaysMapString",selectedDateString);
			model.addAttribute("selectedDateList", jsonObject);
			model.addAttribute("isEdit", true);
			model.addAttribute("selectedWeekEnds",selectedWeekends);
		}
		
		return new ModelAndView("admin/holidayForm",model);
	}
	
	@RequestMapping(value = "bpm/holiday/saveHoliday", method = RequestMethod.POST)
   	public ModelAndView saveHoliday(@ModelAttribute("holiday") Holiday holiday, BindingResult errors, ModelMap model, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		if (validator != null) {
        	validator.validate(holiday, errors);
            if (errors.hasErrors()) {
            	return new ModelAndView("admin/holidayForm", model);
            }
        }
	//	holiday.setHoliday("");
		String selectedDates=request.getParameter("selectedDateJson");
		String currentYear=request.getParameter("currentYear");
		
		StringBuffer selectedWeekends=new StringBuffer();
		
		if(request.getParameter("sunday")!=null && Integer.parseInt(request.getParameter("sunday"))==0){
			selectedWeekends.append("sunday,");
		}
		
		if(request.getParameter("monday")!=null && Integer.parseInt(request.getParameter("monday"))==1){
			selectedWeekends.append("monday,");
		}
		
		if(request.getParameter("tuesday")!=null && Integer.parseInt(request.getParameter("tuesday"))==2){
			selectedWeekends.append("tuesday,");
		}
		
		if(request.getParameter("wednesday")!=null && Integer.parseInt(request.getParameter("wednesday"))==3){
			selectedWeekends.append("wednesday,");
		}
		
		if(request.getParameter("thursday")!=null && Integer.parseInt(request.getParameter("thursday"))==4){
			selectedWeekends.append("thursday,");
		}
		
		if(request.getParameter("friday")!=null && Integer.parseInt(request.getParameter("friday"))==5){
			selectedWeekends.append("friday,");
		}
		
		if(request.getParameter("saturday")!=null && Integer.parseInt(request.getParameter("saturday"))==6){
			selectedWeekends.append("saturday,");
		}
		
		if(selectedWeekends.lastIndexOf(",")>0){
			selectedWeekends.deleteCharAt(selectedWeekends.lastIndexOf(","));
		}
		
		Map<String,Object> selectedDateList=new HashMap<String, Object>();
		if(selectedDates!="" && selectedDates!=null){
			selectedDateList=CommonUtil.convertJsonToMap(selectedDates);	
		}
		
		selectedDateList=holidayService.saveOrUpdate(Integer.parseInt(currentYear),selectedDateList,selectedWeekends.toString());
		
		/*JSONArray jsonArray =CommonUtil.convertListStringToJsonArray(selectedDateList);
		String selectedDateString=CommonUtil.convertListStringToString(selectedDateList);*/
		JSONObject jsonObject  =CommonUtil.convertMapToJsonObj(selectedDateList);
		String selectedDateString=CommonUtil.convertDateMapToString(selectedDateList);
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("selectedDateList", jsonObject);
		model.addAttribute("holidaysMapString",selectedDateString);
		model.addAttribute("selectedWeekEnds",selectedWeekends.toString());
		model.addAttribute("isEdit", true);
/*		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");
		request.getSession().getServletContext().setAttribute(Constants.AGENCY_SETTING,mgr.getAllHoliday());*/
		saveMessage(request, getText("success.holiday.save",locale));
		log.info("Holiday Saved Successfully");
		return new ModelAndView("admin/holidayForm",model);
	}
	
}
