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
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.model.Txl;

@Controller("exttxlController")
public class TxlController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(TxlController.class);
	
	@Autowired
	private TxlService txlService;



	@RequestMapping(value = "txls", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> list(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Txl> bmdm = txlService.getTxlId(id);
			List<Txl> record = txlService.getTxljkById(bmdm.get(0).getBmdm());
			// 此处不要返回对象所有属性，手工指定需要返回的属性，方便移动端调试
			
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (int i=0;i<record.size();i++) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", record.get(i).getId());
				m.put("bm", record.get(i).getBm());//部门
				m.put("bmdm", record.get(i).getBmdm());//部门代码
				m.put("bz", null==record.get(i).getBz()?"":record.get(i).getBz());//备注
				datas.add(m);
			}
			map.put("datas", datas);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取部门列表出错");
		}
		return ExternalUtils.success(map);
	}

}
