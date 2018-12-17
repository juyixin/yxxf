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
import com.eazytec.external.util.ExternalUtils;
import com.eazytec.link.model.Link;
import com.eazytec.link.service.LinkService;

@Controller("extLinkInterfaceController")
public class LinkInterfaceController {
	
	private static final Logger log = LoggerFactory.getLogger(LinkInterfaceController.class);
	
	@Autowired
	private LinkService linkService;
	
	/**
	  * Description:初始化数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-5 下午1:38:35
	 * @throws UnknownHostException 
	  */
	@RequestMapping(value = "linkList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> linkList(HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
		Map<String, Object> map = new HashMap<String, Object>();
		/*InetAddress addr = InetAddress.getLocalHost();
		String ip ="";
		ip=addr.getHostAddress().toString();//获得本机IP
		System.out.println(request.getLocalPort()+"----"+request.getLocalAddr());*/
		try {
			String isActive = "1";
			List<Link> linkList = linkService.getLink1(isActive);
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			if(linkList.isEmpty()){
				return ExternalUtils.error(map, "返回结果为空");
			}else{
				for (Link link : linkList) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("title", link.getTitle());
					m.put("pictureUrl", Constants.IP_CONFIG+":"+request.getLocalPort()+link.getPictureUrl());
					m.put("url", link.getUrl());
					datas.add(m);
				}
				map.put("datas", datas);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ExternalUtils.error(map, "获取友情链接列表出错");
		}
		return ExternalUtils.success(map);
	}

}
