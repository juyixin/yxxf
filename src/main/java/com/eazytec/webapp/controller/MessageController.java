package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.oa.message.service.MessageService;
import com.eazytec.bpm.admin.sysConfig.service.SysConfigManager;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.model.Message;
import com.eazytec.model.MessageFile;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;
import com.eazytec.util.DateUtil;
import com.eazytec.util.PageBean;
import com.eazytec.util.PageResultBean;

@Controller
public class MessageController extends BaseFormController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private UserManager userManager;
	@Autowired
	private SysConfigManager sysConfigManager;

	@RequestMapping(value = "bpm/messages/showSendMessage", method = RequestMethod.GET)
	public ModelAndView showSendMessages(ModelMap model) {
		Message message = new Message();
		model.addAttribute("message", message);
		return new ModelAndView("messages/send_message", model);
	}
	
	// 发件箱
	@RequestMapping(value = "bpm/messages/showSendBox", method = RequestMethod.GET)
	public ModelAndView showSengdBox(ModelMap model) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','主题','接收人','日期']";
		context.put("title", "发件箱");
		context.put("needCheckbox", true);
		context.put("gridId", "SBOX_LIST");
		context.put("reurl", "bpm/messages/showSendBoxScortP");
		context.put("noOfRecords", "20");
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "messageName", "200", "center", "_showEditSendM", "false");
		CommonUtil.createFieldNameList(fieldNameList, "toUser", "200", "center", "_showToUser", "false");
		CommonUtil.createFieldNameList(fieldNameList, "messageDateByString", "100", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		String script = GridUtil.generateScriptDy(velocityEngine, context);
		model.addAttribute("script", script);
		return new ModelAndView("messages/send_box");
	}

	@RequestMapping(value = "bpm/messages/showSendBoxScortP", method = RequestMethod.GET)
	public @ResponseBody String manualHandleToString(HttpServletRequest request, Integer page, Integer rows, ModelMap model) throws UnsupportedEncodingException {
		User loggInUser = CommonUtil.getLoggedInUser(); // 发送人
		String ordername = request.getParameter("ordername"); // 排序名称
		String ordertype = request.getParameter("ordertype"); // 排序类型

		PageBean<Message> pageBean = new PageBean<Message>(page, rows);
		pageBean = messageService.getAllSendBox(pageBean, ordername, ordertype, loggInUser.getId());
		String[] fieldNames = { "id", "messageName", "toUser", "messageDateByString" };
		PageResultBean pageResultBean = CommonUtil.convertPageBeanToPageResultBean(pageBean, fieldNames, null);
		return CommonUtil.convertBeanToJsonString(pageResultBean);
	}

	// 草稿箱
	@RequestMapping(value = "bpm/messages/showDraft", method = RequestMethod.GET)
	public ModelAndView showDraft(ModelMap model) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','主题','接收人','日期']";
		context.put("title", "草稿箱");
		context.put("needCheckbox", true);
		context.put("gridId", "SBOX_LIST");
		context.put("reurl", "bpm/messages/showDraftScortP");
		context.put("noOfRecords", "20");
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "messageName", "200", "center", "_showEditDraft", "false");
		CommonUtil.createFieldNameList(fieldNameList, "toUser", "200", "center", "_showToUser", "false");
		CommonUtil.createFieldNameList(fieldNameList, "messageDateByString", "100", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		String script = GridUtil.generateScriptDy(velocityEngine, context);
		model.addAttribute("script", script);
		return new ModelAndView("messages/draft");
	}

	@RequestMapping(value = "bpm/messages/showDraftScortP", method = RequestMethod.GET)
	public @ResponseBody String showDraftScortP(HttpServletRequest request, Integer page, Integer rows, ModelMap model) throws UnsupportedEncodingException {
		User loggInUser = CommonUtil.getLoggedInUser(); // 发送人
		String ordername = request.getParameter("ordername"); // 排序名称
		String ordertype = request.getParameter("ordertype"); // 排序类型

		PageBean<Message> pageBean = new PageBean<Message>(page, rows);
		pageBean = messageService.getAllDraft(pageBean, ordername, ordertype, loggInUser.getId());
		String[] fieldNames = { "id", "messageName", "toUser", "messageDateByString" };
		PageResultBean pageResultBean = CommonUtil.convertPageBeanToPageResultBean(pageBean, fieldNames, null);
		return CommonUtil.convertBeanToJsonString(pageResultBean);
	}

	// 收件箱
	@RequestMapping(value = "bpm/messages/showInbox", method = RequestMethod.GET)
	public ModelAndView showInBox(ModelMap model) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','主题','发送人','日期','状态']";
		context.put("title", "收件箱");
		context.put("needCheckbox", true);
		context.put("gridId", "SBOX_LIST");
		context.put("reurl", "bpm/messages/showInBoxScortP");
		context.put("noOfRecords", "20");
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "messageName", "200", "center", "_showEditInBox", "false");
		CommonUtil.createFieldNameList(fieldNameList, "fromUser", "200", "center", "_showToUser", "false");
		CommonUtil.createFieldNameList(fieldNameList, "messageDateByString", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "isRead", "100", "center", "_showReadStatus", "false");
		context.put("fieldNameList", fieldNameList);
		String script = GridUtil.generateScriptDy(velocityEngine, context);
		model.addAttribute("script", script);
		return new ModelAndView("messages/inBox");
	}

	@RequestMapping(value = "bpm/messages/showInBoxScortP", method = RequestMethod.GET)
	public @ResponseBody String showInBoxScortP(HttpServletRequest request, Integer page, Integer rows, ModelMap model) {
		String data = "";
		User loggInUser = CommonUtil.getLoggedInUser(); // 接收人
		String ordername = request.getParameter("ordername"); // 排序名称
		String ordertype = request.getParameter("ordertype"); // 排序类型

		PageBean<Message> pageBean = new PageBean<Message>(page, rows);
		pageBean = messageService.getAllInbox(pageBean, ordername, ordertype, loggInUser.getId());
		String[] fieldNames = { "id", "messageName", "fromUser", "messageDateByString", "isRead" };
		PageResultBean pageResultBean = CommonUtil.convertPageBeanToPageResultBean(pageBean, fieldNames, null);
		data = CommonUtil.convertBeanToJsonString(pageResultBean);
		return data;
	}

	/**
	 * 发送消息或者保存草稿
	 * 
	 * @param message
	 * @param files
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/message/saveMessages", method = RequestMethod.POST)
	public ModelAndView saveMessage(Message message, @RequestParam("files") List<MultipartFile> files, ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		uploadMessageFile(message, files);

		message.init();
		message.setFromUser(CommonUtil.getLoggedInUser().getId());// 发送人
		messageService.saveMessage(message);// 保存消息
	
		if (message.getIsDraft()) {
			return new ModelAndView("redirect:/bpm/messages/showDraft");
		} else {
			return new ModelAndView("redirect:/bpm/messages/showSendBox");
		}
	}

	private void uploadMessageFile(Message message, List<MultipartFile> files) {
		
		try{
		
			String uploadDir = getServletContext().getRealPath("/resources") + "/message/";
	
			File dirPath = new File(uploadDir);
	
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
	
			String fileName = null;
			String filePath = null;
			if (null != files && files.size() > 0) {
				for (MultipartFile multipartFile : files) {
					InputStream stream = multipartFile.getInputStream();
					fileName = multipartFile.getOriginalFilename();
					filePath = uploadDir + addTimeStamp(fileName);
					
					OutputStream bos = new FileOutputStream(filePath, true);
	
					int bytesRead;
					byte[] buffer = new byte[8192];
					while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
						bos.write(buffer, 0, bytesRead);
					}
					bos.close();// close the stream
	
					stream.close();
	
					MessageFile file = new MessageFile();
					file.setFileName(fileName);
					file.setFilePath(filePath);
					file.setMessage(message);
					message.addToFiles(file);
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String addTimeStamp(String fileName) {
		String name = "";// 文件名
		String ext = ""; // 后缀
		char point = '.';
		int index = fileName.lastIndexOf(point);

		if (index != -1) {// 如果有后缀
			name = fileName.substring(0, index);
			ext = fileName.substring(index + 1);
		} else {
			name = fileName;
		}
		
		String timeStamp = DateUtil.convertDateToDefalutDateTimeString(new Date());

		return name.trim() + "_" + timeStamp + point + ext;
	}

	/**
	 * 草稿箱编辑
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/message/editMessage", method = RequestMethod.GET)
	public ModelAndView editMessage(String id, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Message message = messageService.getMessageById(id);
		String fullnames = getFullNameByUserIds(message.getToUser());
		model.addAttribute("message", message);
		model.addAttribute("fullnames", fullnames);
		return new ModelAndView("messages/send_message", model);
	}

	/**
	 * 删除发件箱的消息
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/message/deleteSelectedFromM", method = RequestMethod.GET)
	public ModelAndView deleteSendBox(@RequestParam("ids") String ids, ModelMap model, HttpServletRequest request) {
		String[] idsarray = null;
		if (!StringUtil.isEmptyString(ids)) {
			ids = ids.substring(1, ids.length());
			idsarray = ids.split(",");
			if (idsarray != null && idsarray.length > 0) {
				for (int i = 0; i < idsarray.length; i++) {
					messageService.deleteSendM(idsarray[i]);
				}
			}
		}
		return new ModelAndView("redirect:/bpm/messages/showSendBox");
	}

	/**
	 * 消息详情
	 * 
	 * @param id
	 * @param type
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/message/showSendMRead", method = RequestMethod.GET)
	public ModelAndView showSendMRead(@RequestParam("id") String id, @RequestParam("type") String type, ModelMap model, HttpServletRequest request) {
		Message m = messageService.getMessageById(id);

		String fullnames = getFullNameByUserIds(m.getToUser());
		String sendFullnames = getFullNameByUserIds(m.getFromUser());

		if (type.equalsIgnoreCase("Inbox")) {
			messageService.updateIsRead(id);// 修改为已读
		}
		model.addAttribute("message", m);
		model.addAttribute("sendFullnames", sendFullnames);
		model.addAttribute("fullnames", fullnames);
		model.addAttribute("type", type);
		return new ModelAndView("messages/read");
	}

	private String getFullNameByUserIds(String userId) {
		String fullnames = "";

		if (!StringUtil.isEmptyString(userId)) {
			List<String> userIdList = new ArrayList<String>();
			if (userId.contains(",")) {
				String[] ids = userId.split(",");
				for (String uid : ids) {
					userIdList.add(uid);
				}
			} else {
				userIdList.add(userId);
			}

			List<User> list = userManager.getUserByUserIds(userIdList);
			if (list != null) {
				for (User user : list) {
					fullnames = fullnames + "," + user.getFullName();
				}
			}

			if (StringUtils.isNotBlank(fullnames)) {
				fullnames = fullnames.substring(1, fullnames.length());
			}
		}
		return fullnames;
	}

	/**
	 * 下载附件
	 * 
	 * @param response
	 * @param request
	 * @param mid
	 */
	@RequestMapping(value = "bpm/messages/downloadMF", method = RequestMethod.GET)
	public void downloadDocument(HttpServletResponse response, HttpServletRequest request, @RequestParam("mid") String mid) {
		try {
			if (!StringUtil.isEmptyString(mid)) {
				MessageFile f = messageService.getMessageFileById(mid);
				String fileName = f.getFileName();
				fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-disposition", "attachment; filename=" + fileName);
				OutputStream o = response.getOutputStream();
				InputStream is = new FileInputStream(f.getFilePath());
				IOUtils.copy(is, o);
				o.flush();
				o.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 草稿箱删除
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/message/deleteDraft", method = RequestMethod.GET)
	public ModelAndView deleteDraft(@RequestParam("ids") String ids, ModelMap model, HttpServletRequest request) {
		String[] idsarray = null;
		if (!StringUtil.isEmptyString(ids)) {
			ids = ids.substring(1, ids.length());
			idsarray = ids.split(",");
			if (idsarray != null && idsarray.length > 0) {
				for (int i = 0; i < idsarray.length; i++) {
					messageService.deleteDraft(idsarray[i]);
				}
			}
		}
		return new ModelAndView("redirect:/bpm/messages/showDraft");
	}

	/**
	 * 删除附件
	 * 
	 * @param id
	 * @param messageid
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/message/deleteFileM", method = RequestMethod.GET)
	public @ResponseBody ModelAndView deleteFileM(String id, String mid, ModelMap model, HttpServletRequest request) {
		messageService.deleteMessageFileById(mid);
		return new ModelAndView("redirect:editMessage?id=" + id, model);
	}

	/**
	 * 收件箱删除消息
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/message/deleteSelectedInBox", method = RequestMethod.GET)
	public ModelAndView deleteInBox(@RequestParam("ids") String ids, ModelMap model, HttpServletRequest request) {
		String[] idsarray = null;
		if (!StringUtil.isEmptyString(ids)) {
			ids = ids.substring(1, ids.length());
			idsarray = ids.split(",");
			if (idsarray != null && idsarray.length > 0) {
				for (int i = 0; i < idsarray.length; i++) {
					messageService.deleteInBox(idsarray[i]);
				}
			}
		}
		return new ModelAndView("redirect:/bpm/messages/showInbox");
	}

	@RequestMapping(value = "bpm/messages/getUserByUserid", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getUserByUserid(@RequestParam("userid") String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String usernames = "";
		String userid = "";
		if (!StringUtil.isEmptyString(userId)) {
			List<String> userIdList = new ArrayList<String>();
			if (userId.contains(",")) {
				String[] ids = userId.split(",");
				for (String uid : ids) {
					userIdList.add(uid);
				}
			} else {
				userIdList.add(userId);
			}
			List<User> listu = userManager.getUserByUserIds(userIdList);
			if (listu != null) {
				for (int i = 0; i < listu.size(); i++) {
					usernames = usernames + "," + listu.get(i).getFirstName();
					userid = userid + "," + listu.get(i).getId();
				}
			}
			if (usernames != null)
				usernames = usernames.substring(1, usernames.length());

			if (userid != null)
				userid = userid.substring(1, userid.length());
		}

		map.put("usernames", usernames);
		map.put("userids", userid);
		return map;
	}

	@RequestMapping(value = "bpm/messages/getUnReadMessageCount", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getUnReadMessageCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		User loggInUser = CommonUtil.getLoggedInUser(); // 发送人
		int r = messageService.getAllUnReadMessageCount(loggInUser.getId());
		map.put("unreadmc", r);
		return map;
	}
}
