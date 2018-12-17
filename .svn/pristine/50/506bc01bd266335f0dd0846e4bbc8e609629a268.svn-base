package com.eazytec.external.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.Constants;
import com.eazytec.crm.model.Record;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.petitionLetter.model.PetitionLetterEvent;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;
import com.eazytec.petitionLetter.model.PetitionLetterReturn;
import com.eazytec.petitionLetter.service.PetitionLetterEventService;
import com.eazytec.petitionLetter.service.PetitionLetterPersonService;
import com.eazytec.util.PageBean;

@Controller("extPersonInterfaceController")
public class PersonInterfaceController {
	
private static final Logger log = LoggerFactory.getLogger(PersonInterfaceController.class);
	
	@Autowired
	private PetitionLetterPersonService petitionLetterPersonService;
	@Autowired
	private PetitionLetterEventService petitionLetterEventService;
	
	/**
	  * Description:初始化数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-26 下午8:57:35
	 * @throws UnknownHostException 
	  */
	@RequestMapping(value = "letterPerson", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> list(Integer pageNo, Integer pageSize,String name,String sfzCode, HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		/*InetAddress addr = InetAddress.getLocalHost();
		String ip ="";
		ip=addr.getHostAddress().toString();//获得本机IP
*/		try {
	        List<PetitionLetterPerson> list = petitionLetterPersonService.getPerson();
			PageBean<PetitionLetterPerson> pageBean = new PageBean<PetitionLetterPerson>(pageNo, pageSize);
			pageBean = petitionLetterPersonService.getRecords(pageBean, name, sfzCode);
			
			List<PetitionLetterPerson> records = pageBean.getResult();
			int totla=0;
			int current=0;
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (PetitionLetterPerson person : records) {
				Map<String, Object> m = new HashMap<String, Object>();
				/*List<PetitionLetterEvent> eventList1 = new ArrayList<PetitionLetterEvent>();
				List<PetitionLetterReturn> eventList = new ArrayList<PetitionLetterReturn>();
				PetitionLetterReturn eventReturn = new PetitionLetterReturn();*/
				m.put("id", person.getId());
				m.put("name", person.getXm());
				m.put("sex", person.getXb());
				m.put("phone", person.getDh());
				m.put("photo", Constants.IP_CONFIG+":"+request.getLocalPort()+person.getPhoto());
				m.put("sfzCode", person.getSfzhm());
				m.put("address", person.getAddress());
			/*	eventList1 = petitionLetterEventService.getEvent1(person.getId());
				for(PetitionLetterEvent event : eventList1){
					eventReturn.setId(event.getId());
					eventReturn.setEventName(event.getEventName());
					eventReturn.setEventDetail(event.getEventDetail());
					eventList.add(eventReturn);
				}
				m.put("eventList", eventList);*/
				datas.add(m);
			}
			totla=list.size();
			current=pageNo;
			map.put("totla", totla);
			map.put("current", current);
			map.put("datas", datas);
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取信访人列表出错");
		}
		return ExternalUtils.success(map);
	}
	
	
	/**
	  * Description:信访事件
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-26 下午8:57:35
	 * @throws UnknownHostException 
	  */
	@RequestMapping(value = "eventList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> eventList(String personId,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			List<PetitionLetterEvent> list = new ArrayList<PetitionLetterEvent>();
			list = petitionLetterEventService.getEvent(personId);
			if(!list.isEmpty()){
				for(PetitionLetterEvent event :list){
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("id", event.getId());
					m.put("eventName", event.getEventName());
					m.put("eventDetail", event.getEventDetail());
					m.put("remark", event.getBz());
					datas.add(m);
				}
				map.put("datas", datas);
			}else{
				return ExternalUtils.error(map, "返回结果为空");
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取信访事件列表出错");
		}
		return ExternalUtils.success(map);
	}
	
}
