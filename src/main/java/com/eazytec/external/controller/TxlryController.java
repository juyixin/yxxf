package com.eazytec.external.controller;

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

import com.eazytec.bpm.admin.txl.TxlService;
import com.eazytec.bpm.admin.txlry.TxlryService;
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.model.Txl;
import com.eazytec.model.Txlry;

@Controller("extTxlryController")
public class TxlryController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(TxlryController.class);
	
	@Autowired
	private TxlryService txlryService;
	@Autowired
	private TxlService txlService;
	
	
	@RequestMapping(value = "txlrys", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> txlrylist(String id,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			List<Txl> bmdm = txlService.getTxlId(id);
			List<Txlry> records= txlryService.getTxlryTeam(bmdm.get(0).getBmdm());
	
			//此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i=0;i<records.size() ; i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("xm", records.get(i).getXm());//姓名
				m.put("dh", records.get(i).getDh());//电话
				if(records.get(i).getTele() !=null && records.get(i).getTele()!=""){
					m.put("tele", records.get(i).getTele());//办公电话
				}else{
					m.put("tele", "");
				}
				m.put("bz", records.get(i).getBz());//备注
				m.put("bm", records.get(i).getBm());//部门
				datas.add(m);
			}
			map.put("datas", datas);
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取员工信息列表出错");
		}
		return ExternalUtils.success(map);
	}

}
