package com.eazytec.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONException;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xalan.xsltc.runtime.Constants;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import com.eazytec.bpm.common.Messages;
import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.EazyBpmException;

public class JdomXmlParserUtil {

	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	private static final Log log = LogFactory.getLog(JdomXmlParserUtil.class);
	
	public static List<String> getSubProcessListFromXml(String xmlString,String processName,boolean isProcessCreate) throws EazyBpmException, JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new ByteArrayInputStream(xmlString.getBytes()));
		Element root = document.getRootElement();

		List process = root.getChildren();
		Iterator processList = process.iterator();
        Set<String> subProcessList = new HashSet<String>();
        while (processList.hasNext()) {
			Element processEl = (Element) processList.next();
			List childElement = processEl.getChildren();
			Iterator childElementList = childElement.iterator();
			while (childElementList.hasNext()) {
				Element childEl = (Element) childElementList.next();
				//For sub Process
				if(childEl.getName().equals("subProcess")){
					String processId = childEl.getAttributeValue("processName");
					if(processId != null && !processId.isEmpty() && processId != ""){
						subProcessList.add(processId);
					} 
				}
			}
		}
        List<String> subProcessNameList = null;
        if(subProcessList != null && !subProcessList.isEmpty()){
        	subProcessNameList = new ArrayList<String>(subProcessList);
        }
        return subProcessNameList;
	}
	
	/**
	 * Return the list for form id from the XML String we have sent
	 * 
	 * @return FormList parsed form the XML string
	 * @throws JDOMException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static Set<String> getNodeListFromXml(String xmlString,String processName,boolean isProcessCreate,Map<String,byte[]> subProcessXml) throws EazyBpmException, JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new ByteArrayInputStream(xmlString.getBytes()));
		Element root = document.getRootElement();
		List process = root.getChildren();
		Iterator processList = process.iterator();
        Set<String> formIdList = new HashSet<String>();
		while (processList.hasNext()) {
			Element processEl = (Element) processList.next();
			List childElement = processEl.getChildren();
			if(isProcessCreate){
				try{
					if(!containsStartEndEvent(childElement,processName,subProcessXml)) {
						throw new EazyBpmException(I18nUtil.getMessageProperty(Messages.START_END_EVENT_MANDATORY));
					}
				} catch(EazyBpmException e){
					throw new EazyBpmException(e.getMessage());
				}
			}
			Iterator childElementList = childElement.iterator();
			while (childElementList.hasNext()) {
				Element childEl = (Element) childElementList.next();
				//For sub Process
				if(childEl.getName().equals("subProcess")){
					String processId = childEl.getAttributeValue("processName");
					List subProcessElement = null;
					if(processId != null && !processId.isEmpty() && processId != "" && subProcessXml != null &&!subProcessXml.isEmpty()){
						Document subProcessDocument = builder.build(new ByteArrayInputStream(subProcessXml.get(processId)));
						Element subProcessRoot = subProcessDocument.getRootElement();
						subProcessElement = subProcessRoot.getChildren();
						Element processElement = subProcessRoot.getChild("process");
						List<Content> childrenContent = processElement.cloneContent();
						//childEl.setContent(childrenContent);
						childEl.addContent(childrenContent);
						if(childEl.removeChildren("laneSet")){
							subProcessElement = childEl.getChildren();
						}
					} else {
					 subProcessElement = childEl.getChildren();
					}
					if(subProcessElement != null && !subProcessElement.isEmpty()){
						Iterator subProcessElementList = subProcessElement.iterator();
						while (subProcessElementList.hasNext()) {
							Element subProcessEl = (Element) subProcessElementList.next();
							String subProcessElFormId=subProcessEl.getAttributeValue("form");
							if (subProcessElFormId!=null && !subProcessElFormId.isEmpty() &&  subProcessElFormId != "") {
								 formIdList.add(subProcessElFormId);
							} 
						}
					}
				}
				String currentFormId=childEl.getAttributeValue("form");
				if (currentFormId!=null && !currentFormId.isEmpty() && currentFormId != "") {
					 formIdList.add(childEl.getAttributeValue("form"));
				}
			}
		}
		return formIdList;
	}
	
	/**
	 * Return the list of form elements from the XML String we have sent
	 * 
	 * @return ElementsList parsed form the XML string
	 * @throws JDOMException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static List<Element> getFormPropertyForBpmn(String fromXMl)throws EazyBpmException, JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new ByteArrayInputStream(fromXMl.getBytes()));
		Element form = document.getRootElement();
		
		List<Element> fromPropertyElList=new ArrayList<Element>();
		
		List extensionElements = form.getChildren();
		Iterator extensionElementsList = extensionElements.iterator();
		
		while (extensionElementsList.hasNext()) {
			
			Element extensionEl = (Element) extensionElementsList.next();
			
			List columnElement = extensionEl.getChildren();
			Iterator columnElementList = columnElement.iterator();
			while (columnElementList.hasNext()) {
				Element columnEl = (Element) columnElementList.next();
				fromPropertyElList.add(columnEl);
				//List formPropertys = columnEl.getChildren();
				//Iterator formPropertyList = formPropertys.iterator();
				//while (formPropertyList.hasNext()) {
					//Element formPropertyEl = (Element) formPropertyList.next();
					//log.info("formPropertyEl --------------------- "+formPropertyEl);
					//fromPropertyElList.add(formPropertyEl);
				//}
			}
		}
        // Create an XMLOutputter object with pretty formatter. Calling
        // the outputString(Document doc) method convert the document
        // into string data.
        //
        /*XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlStrings = outputter.outputString(document);*/
        return fromPropertyElList;
	}

	private static boolean containsStartEndEvent(List<Element> processElements,String processName,Map<String,byte[]> subProcessXml)
			throws EazyBpmException {
		boolean hasStartEndEvent = false;
		boolean hasStart = false;
		boolean hasEnd = false;
		boolean hasTask = false;
		boolean hasSubProcessElements = false;
		List<String> subProcessList = new ArrayList<String>();
		try{
			for (Element element : processElements) {
				if(processName.equalsIgnoreCase("notice") || processName.equalsIgnoreCase("news")){
					if(element.getName().equalsIgnoreCase("startevent")){
						hasStart = true;
						hasTask = true;
						hasSubProcessElements = true;
					}
				}else{
					if (element.getName().equalsIgnoreCase("startevent")) {
						String adminJson=getJsonStringFormElement(element,"admin");
						if(!adminJson.equals("") && !adminJson.isEmpty()){
							String jsonPersmissions = adminJson.replace("$","");
							JSONObject jsonFieldsPersmission = new JSONObject(jsonPersmissions);
							JSONArray jsonFieldsPersmissionArray = jsonFieldsPersmission.getJSONArray("members");
							List<Map<String,Object>> operationItemsList=CommonUtil.convertJsonToList(String.valueOf(jsonFieldsPersmissionArray));
							String persmissionsItemString=(String)operationItemsList.get(0).get("totalCount");
							if(Integer.parseInt(persmissionsItemString) != 0){
								hasStart = true;
							} else{
								throw new EazyBpmException("Start Node Must Need the Workflow Admin");
							}
						} else{
							throw new EazyBpmException("Start Node Must Need the Workflow Admin");
						}
					}
				
					if(element.getName().equalsIgnoreCase("usertask") || element.getName().equalsIgnoreCase("subProcess")) {
						if(element.getName().equalsIgnoreCase("subProcess")){
							
							hasTask = false;
							boolean hasSubProcessStartEvent = false;
							boolean hasSubProcessEndEvent = false;
							boolean hasSubProcessTask = false;
							String processId = element.getAttributeValue("processName");
							List subProcessElement = null;
							if(processId != null && !processId.isEmpty() && processId != "" && subProcessXml != null &&!subProcessXml.isEmpty()){
								if(subProcessList.size() > 0 && !subProcessList.contains(processId)){
									subProcessList.add(processId);
								} else {
									if(subProcessList.size() == 0){
										subProcessList.add(processId);
									} else {
										throw new EazyBpmException("The "+processId.split(":")[0]+" process used before.");
									}
								}
								SAXBuilder builder = new SAXBuilder();
								Document subProcessDocument = builder.build(new ByteArrayInputStream(subProcessXml.get(processId)));
								Element subProcessRoot = subProcessDocument.getRootElement();
								subProcessElement = subProcessRoot.getChildren();
								Element processElement = subProcessRoot.getChild("process");
								List<Content> childrenContent = processElement.cloneContent();
								//childEl.setContent(childrenContent);
								element.addContent(childrenContent);
								if(element.removeChildren("laneSet")){
									subProcessElement = element.getChildren();
								}
							} else {
							 subProcessElement = element.getChildren();
							}
							//List subProcessElement = element.getChildren();
							Iterator subProcessElementList = subProcessElement.iterator();
							while (subProcessElementList.hasNext()) {
								Element subProcessEl = (Element) subProcessElementList.next();
								if(subProcessEl.getName().equalsIgnoreCase("startevent")){
									hasSubProcessStartEvent = true;
								}
								if(subProcessEl.getName().equalsIgnoreCase("usertask")){
									String taskName = subProcessEl.getAttributeValue("name");
									if(taskName != null && !taskName.isEmpty() && taskName != "" && taskName.length() > 0){
										String formId = subProcessEl.getAttributeValue("form");
										if(formId != null && !formId.isEmpty() && formId != "" && formId.length() > 0){
										hasSubProcessTask = true;
										}else{
											throw new EazyBpmException(" Sub Process Need a Task or Form to save process");
										}
									} else {
										throw new EazyBpmException("Subprocess Task must have the Name to save process");
									}
								}
								if(subProcessEl.getName().equalsIgnoreCase("endevent")){
									hasSubProcessEndEvent = true;
								}
							}
							if(hasSubProcessStartEvent && hasSubProcessTask && hasSubProcessEndEvent){
								hasTask = true;
								hasSubProcessElements = true;
							}
						}else{
							String formId = element.getAttributeValue("form");
							String taskName = element.getAttributeValue("name");
							if(taskName != null && !taskName.isEmpty() && taskName != "" && taskName.length() > 0){
								if(formId != null && !formId.isEmpty() && formId != "" && formId.length() > 0){
									hasTask = true;
									hasSubProcessElements = true;
								} else {
									throw new EazyBpmException(" Task and Form must be need for to save process");
								}
							} else {
								throw new EazyBpmException("Task Name must need to save process");
							}
						}
					}
				}
				
				if(element.getName().equalsIgnoreCase("laneCompartment")){
					hasTask = true;
					hasStart = true;
					hasEnd = true;
					hasSubProcessElements = true;
				}
				if(element.getName().equalsIgnoreCase("endevent")) {
					hasEnd = true;
				}
				if(hasStart && hasEnd && hasTask && hasSubProcessElements) {
					hasStartEndEvent = true;
					return hasStartEndEvent;
				}
			}
		} catch(Exception e){
			throw new EazyBpmException(e.getMessage());
		}
		return hasStartEndEvent;
	}
	
	/**
	 * Return the BPNM xml string that is merged with the form elements.
	 * 
	 * @return String parsed form the XML string
	 * @throws JDOMException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static Element mergeBpmnXmlAndFormElements(Element processRoot,List<Element> formProperties,String formId,String formName,String processName,Map<String, byte[]> subProcessXml)throws EazyBpmException, JDOMException, IOException{
	
		
		int taskcount = 1;

		Namespace XSI_NAMESPACE = Namespace.getNamespace("http://schema.omg.org/spec/BPMN/2.0");
		
		// Namespace XSI_NAMESPACE1 =
		// Namespace.getNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL");

		// root1.addNamespaceDeclaration(XSI_NAMESPACE1);
		// root1.setNamespace(XSI_NAMESPACE1);
		// root1.removeNamespaceDeclaration(XSI_NAMESPACE);
		// root1.setNamespace(XSI_NAMESPACE1);

		List<?> process = processRoot.getChildren();
		
		Iterator<?> processList = process.iterator();
		while (processList.hasNext()) {
			Element processEl = (Element) processList.next();
			processEl.setAttribute("isExecutable", "true");
			processEl.setAttribute("name", processName);
			processEl.setAttribute("id", processName.replaceAll(" ", "_"));

			List<?> childElement = processEl.getChildren();
			Iterator<?> childElementList = childElement.iterator();
			while (childElementList.hasNext()) {
				Element childEl = (Element) childElementList.next();
				String currentFormId = childEl.getAttributeValue("form");
								
				Map<String,String> fieldPermissionValueMap = new HashMap<String, String>();
				
				if(childEl.getName().equalsIgnoreCase("subProcess")){
						String processId = childEl.getAttributeValue("processName");
						List subProcessElement = null;
						if(processId != null && !processId.isEmpty() && processId != "" && subProcessXml != null &&!subProcessXml.isEmpty()){
							SAXBuilder builder = new SAXBuilder();
							Document subProcessDocument = builder.build(new ByteArrayInputStream(subProcessXml.get(processId)));
							Element subProcessRoot = subProcessDocument.getRootElement();
							subProcessElement = subProcessRoot.getChildren();
							Element processElement = subProcessRoot.getChild("process");
							List<Content> childrenContent = processElement.cloneContent();
							childEl.setContent(childrenContent);
							if(childEl.removeChildren("laneSet")){
								/*Element userTaskElement = childEl.getChild("userTask");
								if(userTaskElement != null){
									userTaskElement.removeChildren("extensionElements");
								}*/
								subProcessElement = childEl.getChildren();
							}
						} else {
							subProcessElement = childEl.getChildren();
						}
						Iterator subProcessElementList = subProcessElement.iterator();
						while (subProcessElementList.hasNext()) {
							Element subProcessEl = (Element) subProcessElementList.next();
							mergeBpmnXmlAndSubFormElements(subProcessEl,formId,formName,formProperties,taskcount++);
					}
						
				}
				
				if (childEl.getName().toLowerCase().contains("task")) {
					childEl = setTaskProperties(childEl, taskcount++, XSI_NAMESPACE);					
				}
				
				childEl.setNamespace(Namespace.NO_NAMESPACE);
				if (childEl.getName().equalsIgnoreCase("startEvent")) {
					//Namespace activitiNS = Namespace.getNamespace("activiti","http://activiti.org/bpmn");
					//childEl.setAttribute("initiator", "employeeName",activitiNS);
					setStartAndEndEventProperties(childEl,XSI_NAMESPACE,true);
					childEl.removeAttribute("reader");
					childEl.removeAttribute("admin");
					childEl.removeAttribute("process");
					childEl.removeAttribute("urgemessage");
					childEl.removeAttribute("notificationmessage");
				}
				
				if (childEl.getName().equalsIgnoreCase("endEvent")) {
					setStartAndEndEventProperties(childEl,XSI_NAMESPACE,false);
					childEl.removeAttribute("reader");
				}
			
				if (!StringUtil.isEmptyString(currentFormId)) {
					if (currentFormId.equals(formId)) { 
						childEl.setAttribute("formName",formName);
						fieldPermissionValueMap = getFieldPermission(childEl);
						List<?> extensionElement = childEl.getChildren();
						Iterator<?> extensionElementList = extensionElement.iterator();
						Element extensionEl = null;
						if (extensionElement != null && !extensionElement.isEmpty()) {
							Element extensionEltemp = (Element) extensionElementList.next();
							if (extensionEltemp.getName().equals("extensionElements")) {
								extensionEl = extensionEltemp;
							} else {
								extensionEl = new Element("extensionElements");
							}
						} else {
							extensionEl = new Element("extensionElements");
						}
						// log.info("extensionEl --------------------"+extensionEl);
						if (extensionEl != null) {
							setFormAttributesInParentTag(extensionEl,formProperties,fieldPermissionValueMap);
							childEl.addContent(extensionEl);
						}
					}
				}
				if (childEl.getName().equalsIgnoreCase("startEvent")) {
					//childEl.removeAttribute("organizerpermission");
				}
			}
			processEl.setNamespace(Namespace.NO_NAMESPACE);
		}

		/*
		 * XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
		 * String xmlStrings = outputter1.outputString(document1);
		 */
		return processRoot;
	}
	
	private static void mergeBpmnXmlAndSubFormElements(Element childEl,String formId,String formName,List<Element> formProperties,int taskcount){
		 

		Namespace XSI_NAMESPACE = Namespace.getNamespace("http://schema.omg.org/spec/BPMN/2.0");
		String currentFormId = childEl.getAttributeValue("form");
				
		Map<String,String> fieldPermissionValueMap = new HashMap<String, String>();
		
		if (childEl.getName().toLowerCase().contains("task")) {
			childEl = setTaskProperties(childEl, taskcount, XSI_NAMESPACE);					
		}
		
		childEl.setNamespace(Namespace.NO_NAMESPACE);
		if (childEl.getName().equalsIgnoreCase("startEvent")) {
			Namespace activitiNS = Namespace.getNamespace("activiti","http://activiti.org/bpmn");
			childEl.setAttribute("initiator", "employeeName",activitiNS);
			setStartAndEndEventProperties(childEl,XSI_NAMESPACE,true);
			childEl.removeAttribute("reader");
			childEl.removeAttribute("admin");
			childEl.removeAttribute("process");
			childEl.removeAttribute("urgemessage");
			childEl.removeAttribute("notificationmessage");
		}
		
		if (childEl.getName().equalsIgnoreCase("endEvent")) {
			setStartAndEndEventProperties(childEl,XSI_NAMESPACE,false);
			childEl.removeAttribute("reader");
		}
	
		if (!StringUtil.isEmptyString(currentFormId)) {
			if (currentFormId.equals(formId)) { 
				childEl.setAttribute("formName",formName);
				fieldPermissionValueMap = getFieldPermission(childEl);
				List<?> extensionElement = childEl.getChildren();
				Iterator<?> extensionElementList = extensionElement.iterator();
				Element extensionEl = null;
				if (extensionElement != null && !extensionElement.isEmpty()) {
					Element extensionEltemp = (Element) extensionElementList.next();
					if (extensionEltemp.getName().equals("extensionElements")) {
						extensionEl = extensionEltemp;
					} else {
						extensionEl = new Element("extensionElements");
					}
				} else {
					extensionEl = new Element("extensionElements");
				}
			//	log.info("extensionEl --------------------"+extensionEl);
				if (extensionEl != null) {
					setFormAttributesInParentTag(extensionEl,formProperties,fieldPermissionValueMap);
					//String processId = childEl.getAttributeValue("processName");
					//if(processId != null && !processId.isEmpty() && processId != ""){
					//	Element userTaskElement = childEl.getChild("userTask");
						if(childEl != null && childEl.getChild("extensionElements") != null){
							childEl.removeChildren("extensionElements");
						}
					//}
					childEl.addContent(extensionEl);
				}
			}
		}
		
		if (childEl.getName().equalsIgnoreCase("startEvent")) {
			//childEl.removeAttribute("organizerpermission");

		}
	}
	
	private static void setStartAndEndEventProperties(Element startOrEndElement,Namespace namespace,boolean isStartEvent) throws EazyBpmException {
		
		String readerJson=getJsonStringFormElement(startOrEndElement,"reader");
		String adminJson=getJsonStringFormElement(startOrEndElement,"admin");
		//String startPermissionrJson=getJsonStringFormElement(startOrEndElement,"organizerpermission");
		String urgeMessage = getJsonStringFormElement(startOrEndElement,"urgemessage");
		String notificationMessage = getJsonStringFormElement(startOrEndElement,"notificationmessage");
		String processJson = getJsonStringFormElement(startOrEndElement,"process");
		
		String startscriptJSON=getJsonStringFormElement(startOrEndElement,"startscript");
		String endscriptJSON=getJsonStringFormElement(startOrEndElement,"endscript");
		
		if(!startscriptJSON.equals("") && !startscriptJSON.isEmpty()){
			setScriptFunction(startscriptJSON,true,startOrEndElement,namespace);
		}
		
		if(!endscriptJSON.equals("") && !endscriptJSON.isEmpty()){
			setScriptFunction(endscriptJSON,false,startOrEndElement,namespace);
		}
		
		if(startOrEndElement.getAttribute("reader")!=null){
			if(!readerJson.equals("") && !readerJson.isEmpty()){
				readerJson=readerJson.replace("$","");
				getRoleMemberPermission(readerJson,"reader",startOrEndElement,namespace);
			}
		}
		/*if(startOrEndElement.getAttribute("organizerpermission") != null){
			if(!startPermissionrJson.equals("") && !startPermissionrJson.isEmpty()){
				startPermissionrJson=startPermissionrJson.replace("$","");
				getStartNodePermission(startPermissionrJson,"organizerpermission",startOrEndElement,namespace);
			}
			
		}*/
		if(!urgeMessage.equals("") && !urgeMessage.isEmpty()){
			getStartNodeUrgeMessage(urgeMessage,startOrEndElement,namespace);
		}/*else{
			getStartNodeUrgeMessage(com.eazytec.Constants.DEFAULT_URGE_MESSAGE,startOrEndElement,namespace);
		}*/
		
		if(!processJson.equals("") && !processJson.isEmpty()) {
			processJson = processJson.replace("$", "");
			getStartNodeProcessJson(processJson,startOrEndElement,namespace);			
		}
		
		if(!notificationMessage.equals("") && !notificationMessage.isEmpty()){
			getStartNodeNotificationMessage(notificationMessage,startOrEndElement,namespace);
		}/*else{
			getStartNodeNotificationMessage(com.eazytec.Constants.DEFAULT_NOTIFICATION_MESSAGE,startOrEndElement,namespace);
		}*/
		
		if(isStartEvent){
			if(!adminJson.equals("") && !adminJson.isEmpty()){
				adminJson=adminJson.replace("$","");
				getRoleMemberPermission(adminJson,"workflowadministrator",startOrEndElement,namespace);
			}
		}
	}
	
	
	private static Map<String,String> getFieldPermission(Element taskElement) throws EazyBpmException {	
		
		Map<String,String> fieldPermissionValueMap = new HashMap<String, String>(); 
		if(taskElement.getAttribute("creator") != null){
			if(!StringUtil.isEmptyString(taskElement.getAttributeValue("creator"))) {
				fieldPermissionValueMap.put("creator", taskElement.getAttributeValue("creator").replace("$",""));
			}
			taskElement.removeAttribute("creator");
		}
		
		if(!StringUtil.isEmptyString(taskElement.getAttributeValue("coordinator"))) {
			fieldPermissionValueMap.put("coordinator", taskElement.getAttributeValue("coordinator").replace("$","")); 
		}
		taskElement.removeAttribute("coordinator");
		
		if(!StringUtil.isEmptyString(taskElement.getAttributeValue("reader"))) {
			fieldPermissionValueMap.put("reader", taskElement.getAttributeValue("reader").replace("$","")); 
		}
		taskElement.removeAttribute("reader");
		
		if(!StringUtil.isEmptyString(taskElement.getAttributeValue("processeduser"))) {
			fieldPermissionValueMap.put("processeduser", taskElement.getAttributeValue("processeduser").replace("$","")); 
		}
		taskElement.removeAttribute("processeduser");
		
		if(!StringUtil.isEmptyString(taskElement.getAttributeValue("workflowadministrator"))) {
			fieldPermissionValueMap.put("workflowadministrator", taskElement.getAttributeValue("workflowadministrator").replace("$","")); 
		}
		taskElement.removeAttribute("workflowadministrator");
		if(taskElement.getAttribute("organizer") != null){
			if(!StringUtil.isEmptyString(taskElement.getAttributeValue("organizer"))) {
				fieldPermissionValueMap.put("organizer", taskElement.getAttributeValue("organizer").replace("$",""));
			}
			taskElement.removeAttribute("organizer");
		}
		/*if(!StringUtil.isEmptyString(taskElement.getAttributeValue("organizerpermission"))){
			if(!StringUtil.isEmptyString(taskElement.getAttributeValue("organizerpermission"))) {
				fieldPermissionValueMap.put("organizerpermission", taskElement.getAttributeValue("organizerpermission").replace("$",""));
			}
			taskElement.removeAttribute("organizerpermission");
		}*/
		
		return fieldPermissionValueMap;
	}
	
	/*public static void getStartNodePermission(String jsonPersmissions,String role,Element taskElement,Namespace namespace){
		JSONObject jsonFieldsPersmission;	
		JSONArray dynamicTransactorJSONArray = new JSONArray();
		jsonFieldsPersmission = new JSONObject(jsonPersmissions);
		//JSONArray jsonFieldsPersmissionArray = jsonFieldsPersmission.getJSONArray("dynamicTransactor");
		try {
			if(jsonFieldsPersmission.has("dynamicTransactor")){
				dynamicTransactorJSONArray = jsonFieldsPersmission.getJSONArray("dynamicTransactor");
				if(dynamicTransactorJSONArray.length() > 0 && !dynamicTransactorJSONArray.get(0).equals(Constants.EMPTYSTRING)){
					if(role.equals("organizerpermission")){
						taskElement.setAttribute("dynamicOrganizer",CommonUtil.convertJsonToListStrings(String.valueOf(dynamicTransactorJSONArray)).get(1));
						taskElement.setAttribute("dynamicOrganizerType",CommonUtil.convertJsonToListStrings(String.valueOf(dynamicTransactorJSONArray)).get(2));
					}
				}
			}
		}catch(org.json.JSONException e){
			log.info(e.getMessage(),e);
		}
			
	}*/
	
	public static void getStartNodeUrgeMessage(String urgeMessage,Element startOrEndElement,Namespace namespace){
		try{
			if(startOrEndElement.getName().equalsIgnoreCase("startEvent")){
				Element urgeMessageElement = new Element("urgeMessage");
				urgeMessageElement.setNamespace(Namespace.NO_NAMESPACE);
				urgeMessageElement.addContent(urgeMessage);
				startOrEndElement.addContent(urgeMessageElement);
			}
		}catch(Exception e){
			log.info(e.getMessage());
		}
		 
	}
	
	/**
	 * <p> To get the start node process permissions and set it in the start node tag in the xml source</p>
	 * @param processJson
	 * @param startOrEndElement
	 * @param namespace
	 */
	public static void getStartNodeProcessJson(String processJson,Element startOrEndElement,Namespace namespace){		
		try{
			String roleType = "";
			String resourceType = "";
			String userPermission = "";
			String rolePermission = "";
			String departmentPermission = "";
			String groupPermission = "";
			if(startOrEndElement.getName().equalsIgnoreCase("startEvent")){
				JSONObject processJsonObj = new JSONObject(processJson);
				JSONArray processMembersArray = processJsonObj.getJSONArray("processMembers");
				List<Map<String,Object>> operationItemsList=CommonUtil.convertJsonToList(String.valueOf(processMembersArray));
				List<Map<String,Object>> persmissionsList = CommonUtil.convertJsonToList((String)operationItemsList.get(0).get("items"));
				for(Map<String,Object> persmissionsMap:persmissionsList){
					roleType = (String) persmissionsMap.get("roletype");
					resourceType = (String) persmissionsMap.get("resource_type");
					if(resourceType.equals("humanPerformer")){
						userPermission += (String)persmissionsMap.get("resourceassignmentexpr")+",";
					}else if(resourceType.equals("potentialOwner") && roleType.equals("departments")){
						departmentPermission += (String)persmissionsMap.get("resourceassignmentexpr")+",";
					} else if(resourceType.equals("potentialOwner") && roleType.equals("groups")) {
						groupPermission += (String)persmissionsMap.get("resourceassignmentexpr")+",";
					} else if(resourceType.equals("potentialOwner") && roleType.equals("roles")) {
						rolePermission += (String)persmissionsMap.get("resourceassignmentexpr")+",";
					}
				 }
				if(!"".equals(userPermission)) {
					userPermission = userPermission.substring(0,userPermission.length()-1);
	    			startOrEndElement.setAttribute("userPermission",userPermission); 
				}
				if(!"".equals(departmentPermission)) {
	    			departmentPermission = departmentPermission.substring(0,departmentPermission.length()-1);
					startOrEndElement.setAttribute("departmentPermission",departmentPermission);
				}
				if(!"".equals(groupPermission)) {
	    			groupPermission = groupPermission.substring(0,groupPermission.length()-1);
					startOrEndElement.setAttribute("groupPermission",groupPermission);
				}
				if(!"".equals(rolePermission)) {
	    			rolePermission = rolePermission.substring(0,rolePermission.length()-1);
					startOrEndElement.setAttribute("rolePermission",rolePermission);
				}				
			}
		}catch(Exception e){
			log.info(e.getMessage());
		}
	}
	
	public static void getStartNodeNotificationMessage(String notificationMessage,Element startOrEndElement,Namespace namespace){
		try{
			if(startOrEndElement.getName().equalsIgnoreCase("startEvent")){
				Element notificationMessageElement = new Element("notificationMessage");
				notificationMessageElement.setNamespace(Namespace.NO_NAMESPACE);
				notificationMessageElement.addContent(notificationMessage);
				startOrEndElement.addContent(notificationMessageElement);
			}
		}catch(Exception e){
			log.info(e.getMessage());
		}
		 
	}
	
	public static String generateBPMNXmlString(Element rootElem,Document processDoc,String svgString,String jsonString) throws JDOMException {
		
		
		 Element svgExtensionEl = new Element("svgstring");
		 svgExtensionEl.setAttribute("id",rootElem.getAttributeValue("id"));
		 svgExtensionEl.addContent(svgString); 
		 rootElem.addContent(svgExtensionEl);
	
		 
		 Element jsonExtensionEl = new Element("jsonstring");
		 jsonExtensionEl.setAttribute("id",rootElem.getAttributeValue("id"));
		 jsonExtensionEl.addContent(jsonString);
		 
		 rootElem.addContent(jsonExtensionEl);
		 
		 XMLOutputter outputter1 = new XMLOutputter(Format.getPrettyFormat());
		 String xmlString = outputter1.outputString(processDoc);
		 
		 return xmlString;
	}
	
	
	/**
	 * Return the list of form elements from the XML String we have sent
	 * 
	 * @return ElementsList parsed form the XML string
	 * @throws JDOMException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static List<Element> getFromElementPropertyForBpmn(String fromXMl)throws EazyBpmException, JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(new ByteArrayInputStream(fromXMl.getBytes()));
		Element form = document.getRootElement();
		
		List<Element> fromPropertyElList=new ArrayList<Element>();
		List<Element> extensionElements = form.getChildren();
		Iterator<Element> extensionElementsList = extensionElements.iterator();
		
		while (extensionElementsList.hasNext()) {
			Element extensionEl = (Element) extensionElementsList.next();
			List<Element> columnElement = extensionEl.getChildren();
			Iterator<Element> columnElementList = columnElement.iterator();
			while (columnElementList.hasNext()) {
				Element columnEl = (Element) columnElementList.next();
				if(columnEl != null){
					fromPropertyElList.add(columnEl);
				}
			}
		}
		
        // Create an XMLOutputter object with pretty formatter. Calling
        // the outputString(Document doc) method convert the document
        // into string data.
        //
        /*XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlStrings = outputter.outputString(document);*/
        return fromPropertyElList;
	}
	
	private static Element setTaskProperties(Element taskElement,int taskcount,Namespace namespace) throws EazyBpmException {
		
	/*	Element extensionElem = null;*/
		
		
		if(StringUtil.isEmptyString(taskElement.getAttributeValue("name"))) {
			taskElement.setAttribute("name", "DefaultTask "+taskcount);
		}
		
		//validation for resources(user/group) to be assigned for a task that has nodetype 1.
		/*if(taskElement.getAttribute("nodeType").getValue().equals("1") && taskElement.getChildren().size() > 1) {
			throw new EazyBpmException(I18nUtil.getMessageProperty(Messages.NODE_TYPE_1_RESOURCES_ERROR));
		}*/
		
		String organizerJSON=getJsonStringFormElement(taskElement,"organizer");
		String readerJSON=getJsonStringFormElement(taskElement,"reader");
		
		if(!organizerJSON.equals("") && !organizerJSON.isEmpty()){
			organizerJSON=organizerJSON.replace("$","");
			getRoleMemberPermission(organizerJSON,"organizer",taskElement,namespace);
		}
		
		if(!readerJSON.equals("") && !readerJSON.isEmpty()){
			readerJSON=readerJSON.replace("$","");
			getRoleMemberPermission(readerJSON,"reader",taskElement,namespace);
		}
		
		//start script and end script 
		String startscriptJSON=getJsonStringFormElement(taskElement,"startscript");
		String endscriptJSON=getJsonStringFormElement(taskElement,"endscript");
		
		if(!startscriptJSON.equals("") && !startscriptJSON.isEmpty()){
			setScriptFunction(startscriptJSON,true,taskElement,namespace);
		}
		
		if(!endscriptJSON.equals("") && !endscriptJSON.isEmpty()){
			setScriptFunction(endscriptJSON,false,taskElement,namespace);
		}
		
		String formfieldAutomaticJSON = getJsonStringFormElement(taskElement,"formfieldautomatic");
		if(!formfieldAutomaticJSON.equals("") && !formfieldAutomaticJSON.isEmpty()){
			formfieldAutomaticJSON = formfieldAutomaticJSON.replace("$","");
			getFormFieldAutomatic(formfieldAutomaticJSON,"formfieldautomatic",taskElement,namespace);
		}
		
		String processTimeSettingJSON = getJsonStringFormElement(taskElement,"processtimesetting");
		if(!processTimeSettingJSON.equals("") && !processTimeSettingJSON.isEmpty()){
			processTimeSettingJSON = processTimeSettingJSON.replace("$","");
			setProcessTimeSetting(processTimeSettingJSON,"processtimesetting",taskElement,namespace);
		}
		
		String onRead = getJsonStringFormElement(taskElement,"onread");
		String onCreate = getJsonStringFormElement(taskElement,"oncreate");
		String onUpdate = getJsonStringFormElement(taskElement,"onupdate");
		if(taskElement.getAttribute("onread") != null){
			if(!onRead.equals("") && !onRead.isEmpty()){
				setOnRead(onRead,taskElement,namespace);
			}
		}
		
		if(taskElement.getAttribute("oncreate") != null){
			if(!onCreate.equals("") && !onCreate.isEmpty()){
				setOnCreate(onCreate,taskElement,namespace);
			}
		}
		
		if(taskElement.getAttribute("onupdate") != null){	
			if(!onUpdate.equals("") && !onUpdate.isEmpty()){
				setOnUpdate(onUpdate,taskElement,namespace);
			}
		}
		
		
		
		/*
		taskElement.addContent(extensionElem);*/
		
		//Operation function 
		String operationorganizerJSON=getJsonStringFormElement(taskElement,"operationorganizer");
		String operationownerJSON=getJsonStringFormElement(taskElement,"operationcreator");
		String operationcoordinatorJSON=getJsonStringFormElement(taskElement,"operationcoordinator");
		String operationreaderJSON=getJsonStringFormElement(taskElement,"operationreader");
		String operationprocesseduserJSON=getJsonStringFormElement(taskElement,"operationprocesseduser");
		String operationworkflowadministratorJSON=getJsonStringFormElement(taskElement,"operationworkflowadministrator");
		
		if(!operationorganizerJSON.equals("") && !operationorganizerJSON.isEmpty()){
			createOperationDatas(operationorganizerJSON,"organizer",taskElement,namespace);
		}
		if(!operationownerJSON.equals("") && !operationownerJSON.isEmpty()){
			createOperationDatas(operationownerJSON,"creator",taskElement,namespace);
		}
		
		if(!operationcoordinatorJSON.equals("") && !operationcoordinatorJSON.isEmpty()){
			createOperationDatas(operationcoordinatorJSON,"coordinator",taskElement,namespace);
		}
		
		if(!operationreaderJSON.equals("") && !operationreaderJSON.isEmpty()){
			createOperationDatas(operationreaderJSON,"reader",taskElement,namespace);
		}
		
		if(!operationprocesseduserJSON.equals("") && !operationprocesseduserJSON.isEmpty()){
			createOperationDatas(operationprocesseduserJSON,"processeduser",taskElement,namespace);
		}

		if(!operationworkflowadministratorJSON.equals("") && !operationworkflowadministratorJSON.isEmpty()){
			createOperationDatas(operationworkflowadministratorJSON,"workflowadministrator",taskElement,namespace);
		}
		
		/*String notificationJSON=getJsonStringFormElement(taskElement,"notification");
		if(!notificationJSON.equals("") && !notificationJSON.isEmpty()){
			createNotificationDatas(notificationJSON,"Notification",taskElement,namespace);
		}*/
		
		taskElement.removeAttribute("formfieldautomatic");
		taskElement.removeAttribute("startscript");
		taskElement.removeAttribute("endscript");
		taskElement.removeAttribute("operationorganizer");
		taskElement.removeAttribute("operationcreator");
		taskElement.removeAttribute("operationcoordinator");
		taskElement.removeAttribute("operationreader");
		taskElement.removeAttribute("operationprocesseduser");
		taskElement.removeAttribute("operationworkflowadministrator");
		taskElement.removeAttribute("processtimesetting");
		taskElement.removeAttribute("onread");
		taskElement.removeAttribute("oncreate");
		taskElement.removeAttribute("onupdate");
		return taskElement;
	}
	
	private static void setScriptFunction(String scriptJSON,boolean isStartScript,Element taskElement,Namespace namespace){
		try {
			List<Map<String,Object>> scriptFunctionList=CommonUtil.convertJsonToList(scriptJSON);
			for(Map<String,Object> 	scriptFunction:scriptFunctionList){
				Element extensionElem = null;
				if(isStartScript){
					extensionElem = new Element("startScript",namespace);
				}else{
					extensionElem = new Element("endScript",namespace);
				}
				Element element1 =  new Element("functionName");
				Element element2 =  new Element("script");
				element1.setNamespace(Namespace.NO_NAMESPACE);
				element2.setNamespace(Namespace.NO_NAMESPACE);
				
				CDATA cdata1 = new CDATA((String)scriptFunction.get("scriptName"));
				element1.setContent(cdata1);
				CDATA cdata2 = new CDATA(""+(String)scriptFunction.get("scriptFunction")+"");
				element2.setContent(cdata2);
				extensionElem.addContent(element1);
				extensionElem.addContent(element2);
				
				taskElement.addContent(extensionElem);
			}
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void setOnRead(String onRead,Element taskElement, Namespace namespace){
		try{
			Element extensionEl = new Element("extensionElements");
			Element onReadElement=new Element("onRead");
			onReadElement.setAttribute("onRead",onRead);
			Namespace XSI_NAMESPACE = Namespace.getNamespace("activiti","http://activiti.org/bpmn");
			onReadElement.setNamespace(XSI_NAMESPACE);
			extensionEl.addContent(onReadElement);
			taskElement.addContent(extensionEl);
		} catch(Exception e){
			log.info(e.getMessage());
		}
		
	}
	
	private static void setOnCreate(String onCreate,Element taskElement, Namespace namespace){
		try{
			Element extensionEl = new Element("extensionElements");
			Element onCreateElement=new Element("onRead");
			onCreateElement.setAttribute("onCreate",onCreate);
			Namespace XSI_NAMESPACE = Namespace.getNamespace("activiti","http://activiti.org/bpmn");
			onCreateElement.setNamespace(XSI_NAMESPACE);
			extensionEl.addContent(onCreateElement);
			taskElement.addContent(extensionEl);
		} catch(Exception e){
			log.info(e.getMessage());
		}
	}
	
	private static void setOnUpdate(String onUpdate,Element taskElement, Namespace namespace){
		try{
			Element extensionEl = new Element("extensionElements");
			Element onUpdateElement=new Element("onRead");
			onUpdateElement.setAttribute("onUpdate", onUpdate);
			Namespace XSI_NAMESPACE = Namespace.getNamespace("activiti","http://activiti.org/bpmn");
			onUpdateElement.setNamespace(XSI_NAMESPACE);
			extensionEl.addContent(onUpdateElement);
			taskElement.addContent(extensionEl);
		} catch(Exception e){
			log.info(e.getMessage());
		}
	}
	
	private static void getFormFieldAutomatic(String formFieldAutomaticJSON,String type,Element taskElement,Namespace namespace){
		
		JSONObject formFieldAutomaticObject = new JSONObject(formFieldAutomaticJSON);
		JSONArray formFieldAutomaticArray = formFieldAutomaticObject.getJSONArray("formFieldAutomatic");
			if(formFieldAutomaticArray.length()>0){
				Element formFieldAutomaticElement = new Element("formFieldAutomatic");
				List<Element> formFieldAutomaticElementList = new ArrayList<Element>();
				for(int i=0;i<formFieldAutomaticArray.length();i++){
					Element automaticInfoElement = new Element("automaticFieldInfo");
					List<Element> addFormFieldAutomaticList = new ArrayList<Element>();
					JSONObject childOfChildObject = (JSONObject) formFieldAutomaticArray.get(i);
					Element formIdElement = new Element("fillForm");
					formIdElement.setText(childOfChildObject.getString("fillForm"));
					addFormFieldAutomaticList.add(formIdElement);
					Element fieldNameElement = new Element("fillFieldName");
					fieldNameElement.setText(childOfChildObject.getString("fillField"));
					addFormFieldAutomaticList.add(fieldNameElement);
					Element fieldTypeElement = new Element("fieldType");
					fieldTypeElement.setText(childOfChildObject.getString("fillType"));
					addFormFieldAutomaticList.add(fieldTypeElement);
					Element fieldValueElement = new Element("fieldValue");
					fieldValueElement.setText(childOfChildObject.getString("fillFieldValue"));
					addFormFieldAutomaticList.add(fieldValueElement);
					Element isSkipValueElement = new Element("isSkip");
					isSkipValueElement.setText(childOfChildObject.getString("isSkip"));
					addFormFieldAutomaticList.add(isSkipValueElement);
					Element fromFormElement = new Element("fromForm");
					fromFormElement.setText(childOfChildObject.getString("fromForm"));
					addFormFieldAutomaticList.add(fromFormElement);
					Element fromFormFieldElement = new Element("fromFormfield");
					fromFormFieldElement.setText(childOfChildObject.getString("fromFormField"));
					addFormFieldAutomaticList.add(fromFormFieldElement);
					automaticInfoElement.addContent(addFormFieldAutomaticList);
					formFieldAutomaticElementList.add(automaticInfoElement);
				}
				formFieldAutomaticElement.addContent(formFieldAutomaticElementList);
				taskElement.addContent(formFieldAutomaticElement);
				
			}
		
		
	}
	
	private static void  setProcessTimeSetting(String processTimeSettingJSON,String type,Element taskElement,Namespace namespace){
		JSONObject timeSettingObject = new JSONObject(processTimeSettingJSON);
		JSONArray timeSettingArray = timeSettingObject.getJSONArray("processTimeSetting");
		if(timeSettingArray.length() > 0){
			Element timeSetting = new Element("timeSetting");
			List<Element> timeSettingList = new ArrayList<Element>();
			for(int i=0;i<timeSettingArray.length();i++){
				JSONObject childOfChildObject = (JSONObject) timeSettingArray.get(i);
				Element maxDays = new Element("maxDays");
				maxDays.setText(childOfChildObject.getString("maxDays"));
				timeSettingList.add(maxDays);
				Element warningDays = new Element("warningDays");
				warningDays.setText(childOfChildObject.getString("warningDays"));
				timeSettingList.add(warningDays);
				Element dateType = new Element("dateType");
				dateType.setText(childOfChildObject.getString("dateType"));
				timeSettingList.add(dateType);
				Element urgeTimes = new Element("urgeTimes");
				urgeTimes.setText(childOfChildObject.getString("urgeTimes"));
				timeSettingList.add(urgeTimes);
				Element frequence = new Element("frequence");
				frequence.setText(childOfChildObject.getString("frequence"));
				timeSettingList.add(frequence);
				Element dealIfTimeout = new Element("dealIfTimeout");
				dealIfTimeout.setText(childOfChildObject.getString("dealIfTimeout"));
				timeSettingList.add(dealIfTimeout);
				Element notificationType = new Element("notificationType");
				notificationType.setText(childOfChildObject.getString("notificationType"));
				timeSettingList.add(notificationType);
			}
			timeSetting.addContent(timeSettingList);
			taskElement.addContent(timeSetting);
		}
		
	}
	
	private static void getRoleMemberPermission(String jsonPersmissions,String role,Element taskElement,Namespace namespace){
		List<Element> permissionElementList=new ArrayList<Element>();
		
		JSONObject jsonFieldsPersmission;	
		JSONArray dynamicTransactorJSONArray = new JSONArray();
		jsonFieldsPersmission = new JSONObject(jsonPersmissions);
		JSONArray jsonFieldsPersmissionArray = jsonFieldsPersmission.getJSONArray("members");
	
		try {
			if(jsonFieldsPersmission.has("dynamicTransactor")){
				dynamicTransactorJSONArray = jsonFieldsPersmission.getJSONArray("dynamicTransactor");
				if(dynamicTransactorJSONArray.length() > 0 && !dynamicTransactorJSONArray.get(0).equals(Constants.EMPTYSTRING)){
					if(role.equals("organizer")){
						taskElement.setAttribute("dynamicOrganizer",CommonUtil.convertJsonToListStrings(String.valueOf(dynamicTransactorJSONArray)).get(0));
						taskElement.setAttribute("dynamicOrganizerType",CommonUtil.convertJsonToListStrings(String.valueOf(dynamicTransactorJSONArray)).get(1));
					}else if(role.equals("reader")){
						taskElement.setAttribute("dynamicReader",CommonUtil.convertJsonToListStrings(String.valueOf(dynamicTransactorJSONArray)).get(0));
						taskElement.setAttribute("dynamicReaderType",CommonUtil.convertJsonToListStrings(String.valueOf(dynamicTransactorJSONArray)).get(1));
					}
				}
			}
			List<Map<String,Object>> operationItemsList=CommonUtil.convertJsonToList(String.valueOf(jsonFieldsPersmissionArray));
			String persmissionsItemString=(String)operationItemsList.get(0).get("items");
			List<Map<String,Object>> persmissionsList = CommonUtil.convertJsonToList(persmissionsItemString);
			for(Map<String,Object> persmissionsMap:persmissionsList){
				Element extensionElem = null;
			
				String resourceType=(String)persmissionsMap.get("resource_type");
				if(resourceType.equals("humanPerformer")){
					extensionElem = new Element("humanPerformer",namespace);
				}else{
					extensionElem = new Element("potentialOwner",namespace);
					extensionElem.setAttribute("type",(String)persmissionsMap.get("roletype"));
				}
				extensionElem.setAttribute("resourceRef",(String)persmissionsMap.get("resource"));
				extensionElem.setAttribute("role",role);
				
				Element element1 =  new Element("resourceAssignmentExpression");
				element1.setNamespace(Namespace.NO_NAMESPACE);
				Element element2 = new Element("formalExpression");
				element2.setNamespace(Namespace.NO_NAMESPACE);
				if(role.equals("organizer")){
					element2.setAttribute("evaluatesToTypeRef",(String)persmissionsMap.get("evaluatestotype"));
				}else{
					element2.setAttribute("evaluatesToTypeRef","0");
				}
				if(persmissionsMap.containsKey("superior")){
					element2.setAttribute("superior",(String)persmissionsMap.get("superior"));
				}
				if(persmissionsMap.containsKey("subordinate")){
					element2.setAttribute("subordinate", (String)persmissionsMap.get("subordinate"));
				}
	 			extensionElem.addContent(element1);
				element1.addContent(element2);
				element2.setText((String)persmissionsMap.get("resourceassignmentexpr"));
				permissionElementList.add(extensionElem);
			}
			
			for(Element extensionElem : permissionElementList){
				taskElement.addContent(extensionElem);
			}
			
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createNotificationDatas(String notificationDatasJSON, String operationRole,Element taskElement,Namespace namespace){
		Element operationElem = null;
		try {
			List<Map<String,Object>> notificationList=CommonUtil.convertJsonToList(notificationDatasJSON);
			Element automaticRemainder=new Element("automaicreminder");
			
			for(Map<String,Object> notificationMap:notificationList){
				Element automaticReminderMessageEle=new Element("Message");
				Element automaticReminderTypeEle=new Element("Type");
				Element automaticReminderHourEle=new Element("Hour");
				Element automaticReminderMinuteEle=new Element("Minute");
				automaticReminderMessageEle.setText((String) notificationMap.get("automaticReminderMessage"));
				automaticReminderTypeEle.setText((String)notificationMap.get("automaticReminderType"));
				automaticReminderHourEle.setText((String) notificationMap.get("automaticReminderHour"));
				automaticReminderMinuteEle.setText((String) notificationMap.get("automaticReminderMinute"));
				automaticRemainder.addContent(automaticReminderMessageEle);
				automaticRemainder.addContent(automaticReminderTypeEle);
				automaticRemainder.addContent(automaticReminderHourEle);
				automaticRemainder.addContent(automaticReminderMinuteEle);
			}
			
			taskElement.addContent(automaticRemainder);
			
		}catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * To create operation data elements
	 * @param operationDatasJSON
	 * @param operationRole
	 * @param taskElement
	 * @param namespace
	 */
	private static void createOperationDatas(String operationDatasJSON, String operationRole,Element taskElement,Namespace namespace){
		
		Element operationElem = null;
		try {
			List<Map<String,Object>> operationorganizerList=CommonUtil.convertJsonToList(operationDatasJSON);
			
			
			for(Map<String,Object> operationMap:operationorganizerList){
				
				String operationItemsJSON=(String)operationMap.get("items");
				
				if(!operationItemsJSON.equals("") && !operationItemsJSON.isEmpty()){
					List<Map<String,Object>> operationItemsList=CommonUtil.convertJsonToList(operationItemsJSON);
					if(!operationItemsList.isEmpty()){
						
						//Create list of tag names 
						Set<String> operationFunctionTags=createOperationTags();
						
						//Create the element with the tag names
						Map<String,Element> operationElements = createOperationElement(operationFunctionTags);
						
						for(Map<String,Object> operationItemsMap:operationItemsList){
							String opertaionType=String.valueOf(operationItemsMap.get("resourceassignmentexpr"));
							if(opertaionType.equals("Default Operation")){
								String resourceName=String.valueOf(operationItemsMap.get("resource_type")).toLowerCase();
								Element functionTagElement = operationElements.get(resourceName);
								if(functionTagElement!=null){
									functionTagElement.setText("true");
									operationElements.put(resourceName, functionTagElement);
								}
							}else{
								Element customOperation=new Element("custom");
								String customId=String.valueOf(operationItemsMap.get("resource_type"));
								customOperation.setText(customId);
								customOperation.setNamespace(Namespace.NO_NAMESPACE);
								operationElements.put(customId, customOperation);
							}
						}
						operationElem= addElementInOperatingFunction(operationElements,operationRole);
						taskElement.addContent(operationElem);
					}
				}
			}
			
			//adding the operation element  
			
		
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * To add the operation tags with the  operation elements.
	 * @param operationElements
	 * @param operationRole
	 * @return
	 */
	private static Element addElementInOperatingFunction(Map<String,Element> operationElements,String operationRole){
		Element operationElem = new Element("operatingFunction");
		operationElem.setAttribute("role", operationRole);
		operationElem.setNamespace(Namespace.NO_NAMESPACE);
		Set<String> operationKeySet=operationElements.keySet();
		for(String operationKey: operationKeySet){
			operationElem.addContent(operationElements.get(operationKey));
		}
		return operationElem;
	}
	
	/**
	 * List of operation default attributes
	 * @return List<String>
	 */
	private static Set<String> createOperationTags(){
		Set<String> opetarionFunctionTags=new HashSet<String>();
		
		opetarionFunctionTags.add("submit");
		opetarionFunctionTags.add("save");
		opetarionFunctionTags.add("returnoperation");
		opetarionFunctionTags.add("recall");
		opetarionFunctionTags.add("withdraw");
		opetarionFunctionTags.add("transfer");
		opetarionFunctionTags.add("urge");
		opetarionFunctionTags.add("add");
		opetarionFunctionTags.add("collaborativeoperation");
		opetarionFunctionTags.add("circulateperusal");
		opetarionFunctionTags.add("jump");
		opetarionFunctionTags.add("transactorreplacement");
		opetarionFunctionTags.add("terminate");
		opetarionFunctionTags.add("suspend");
		opetarionFunctionTags.add("print");
		
		return opetarionFunctionTags;
	}
	
	/**
	 * To create the default operation elements 
	 * @param opetarionFunctionTags
	 * @return
	 */
	private static Map<String,Element> createOperationElement(Set<String> opetarionFunctionTags){
		
		Map<String,Element> defaultOperationMap=new HashMap<String, Element>();
		for(String opetarionTags : opetarionFunctionTags){
			Element opetarionElementName=new Element(opetarionTags);
			opetarionElementName.setText("false");
			opetarionElementName.setNamespace(Namespace.NO_NAMESPACE);
			defaultOperationMap.put(opetarionTags+"_elm", opetarionElementName);
		}
		return defaultOperationMap;
	}
	
	/**
	 * To set the Attributes list in an element
	 * @param formAttributeList
	 * @param parentTag
	 */
	public static void  setFormAttributesInParentTag(Element extensionElements,List<Element> fromPropertys, Map<String,String> fieldPermissionValueMap){
		for(Element fromProperty:fromPropertys ){
			//To add activiti namespace
			Element tagName=new Element(fromProperty.getName());
			tagName.setText(fromProperty.getText());
			//To get the list of Attributes of Forms
			List formAttributeList=fromProperty.getAttributes();
			//log.info("formAttributeList.toString() -------:-------- "+formAttributeList.toString());
			//log.info("tagName.toString() ---------:---------- "+tagName.toString());
			getAttributeOfForm(formAttributeList,tagName, fieldPermissionValueMap);
			
			//To get the list of sub form 
			List subForms=fromProperty.getChildren();
			Iterator subForm=subForms.iterator();
			while(subForm.hasNext()){
				Element subFormEl = (Element) subForm.next();
				Element subFormElements = new Element(subFormEl.getName());
				
				//To get the list of sub form Attributes
				List subFormAttributeList=subFormEl.getAttributes();
				getAttributeOfForm(subFormAttributeList,subFormElements,fieldPermissionValueMap);
			
				tagName.addContent(subFormElements);
			}
			extensionElements.addContent(tagName);
		}
	}
	
	
	private static Map setFieldsPersmissionOnTaskRole(String fieldIdValue, Map<String,String> fieldPermissionValueMap){
		Map<String, String> permissionMap = new HashMap<String, String>();
					
	/*	String ownerPerms       = "{'fieldsPersmission':[{'FieldPermForm_id':'1-1-0-0-0','FieldPermForm_name':'1-1-0-0-0','FieldPermForm_code':'1-1-0-0-0'}]}";
		String coordinatorPerms = "{'fieldsPersmission':[{'FieldPermForm_id':'0-1-1-0-0','FieldPermForm_name':'0-1-1-0-0','FieldPermForm_code':'0-1-1-0-0'}]}";
		String readerPerms      = "{'fieldsPersmission':[{'FieldPermForm_id':'0-0-1-0-0','FieldPermForm_name':'0-0-1-0-0','FieldPermForm_code':'0-0-1-0-0'}]}";*/
		
		try{
			if(fieldPermissionValueMap.get("creator") != null && !fieldPermissionValueMap.get("creator").isEmpty()){
				permissionMap = setFieldPersmission(permissionMap,fieldIdValue,fieldPermissionValueMap.get("creator"),"creator");			

			}
			if(fieldPermissionValueMap.get("coordinator") != null && !fieldPermissionValueMap.get("coordinator").isEmpty()){
				permissionMap = setFieldPersmission(permissionMap,fieldIdValue,fieldPermissionValueMap.get("coordinator"),"coordinator");

			}
			if(fieldPermissionValueMap.get("reader") != null && !fieldPermissionValueMap.get("reader").isEmpty()){
				permissionMap = setFieldPersmission(permissionMap,fieldIdValue,fieldPermissionValueMap.get("reader"),"reader");
			}
				
			if(fieldPermissionValueMap.get("processeduser") != null && !fieldPermissionValueMap.get("processeduser").isEmpty()){
				permissionMap = setFieldPersmission(permissionMap,fieldIdValue,fieldPermissionValueMap.get("processeduser"),"processeduser");
			}
			
			if(fieldPermissionValueMap.get("workflowadministrator") != null && !fieldPermissionValueMap.get("workflowadministrator").isEmpty()){
				permissionMap = setFieldPersmission(permissionMap,fieldIdValue,fieldPermissionValueMap.get("workflowadministrator"),"workflowadministrator");
			}
			
			if(fieldPermissionValueMap.get("organizer") != null && !fieldPermissionValueMap.get("organizer").isEmpty()){
				permissionMap = setFieldPersmission(permissionMap,fieldIdValue,fieldPermissionValueMap.get("organizer"),"organizer");
			}
			/*if(fieldPermissionValueMap.get("organizerpermission") != null && !fieldPermissionValueMap.get("organizerpermission").isEmpty()){
				permissionMap = setStartFieldPermission(permissionMap,fieldIdValue,fieldPermissionValueMap.get("organizerpermission"),"organizer");
			}*/
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("Error While setting Field Persmission : "+e.getMessage(),e);
		}
		
		return permissionMap;
	}
	
	private static Map<String, String> setFieldPersmission(Map<String, String> permissionMap,String fieldIdValue,String userJsonPersmissions,String permissionFor){
			
			JSONObject jsonFieldsPersmission;			
			jsonFieldsPersmission = new JSONObject(userJsonPersmissions);
			
			JSONArray jsonFieldsPersmissionArray = jsonFieldsPersmission.getJSONArray("fieldsPersmission");
			JSONObject childOfChildObject = (JSONObject) jsonFieldsPersmissionArray.get(0);
	
			Iterator keychilOfChildList = childOfChildObject.keys();
			while(keychilOfChildList.hasNext()){
				String childOfChildKey = keychilOfChildList.next().toString();
				if(childOfChildKey.equalsIgnoreCase(fieldIdValue)){
					String rolePermissions = childOfChildObject.get(childOfChildKey).toString();
					permissionMap.put(permissionFor, rolePermissions);
				}
			}	
			
			
			return permissionMap;
	}
	
	/*private static Map<String,String> setStartFieldPermission(Map<String,String> permissionMap,String fieldIdValue,String userJsonPersmissions,String permissionFor){
		JSONObject jsonFieldsPersmission;	
		jsonFieldsPersmission = new JSONObject(userJsonPersmissions);
		JSONArray jsonFieldsPersmissionArray = jsonFieldsPersmission.getJSONArray("startPermission");
		for(int i=0;i<jsonFieldsPersmissionArray.length();i++){
			JSONObject childOfChildObject = (JSONObject) jsonFieldsPersmissionArray.get(i);
			if(fieldIdValue.equalsIgnoreCase(childOfChildObject.get("resource_type").toString())){
			permissionMap.put(permissionFor, childOfChildObject.get("evaluatestotype").toString());
			}
		}
		return permissionMap;
	}*/
		
	/**
	 * To set the Attributes list in an element
	 * @param formAttributeList
	 * @param parentTag
	 * @throws JSONException 
	 */
	public static void  getAttributeOfForm(List formAttributeList,Element parentTag, Map<String,String> fieldPermissionValueMap){
		Iterator formAttributes=formAttributeList.iterator();
		Namespace XSI_NAMESPACE = Namespace.getNamespace("activiti","http://activiti.org/bpmn");
		parentTag.setNamespace(XSI_NAMESPACE);
		while(formAttributes.hasNext()){
			Object formAttributeObj = formAttributes.next();
			  Attribute formAttribute = (Attribute) formAttributeObj;
			//  log.info("formAttribute.getName() --------------"+formAttribute.getName());
			//  log.info("formAttribute.getValue() --------------"+formAttribute.getValue());
			  if(formAttribute.getName().equalsIgnoreCase("id") && formAttribute.getValue() !=null){
				  parentTag.setAttribute(formAttribute.getName(), formAttribute.getValue());
				  Map permissionMap = setFieldsPersmissionOnTaskRole(formAttribute.getValue(),fieldPermissionValueMap);
				  if(!permissionMap.isEmpty()){
					  Iterator permissionIterator = permissionMap.entrySet().iterator();
					  while(permissionIterator.hasNext()){
						  Map.Entry mapEntry = (Map.Entry) permissionIterator.next();
						 // log.info(mapEntry.getKey().toString()+" -:- "+mapEntry.getValue().toString());
						  parentTag.setAttribute(mapEntry.getKey().toString(), mapEntry.getValue().toString());
					  }
				  }				  
				  
			  }else{
				  parentTag.setAttribute(formAttribute.getName(), formAttribute.getValue());
  			  }
		}
	}
	
	/**
	 * To get the attribute form the element
	*/
	private static String getJsonStringFormElement(Element sendElement,String attributeName){
		String getJSONString="";
		
		if(sendElement.getAttribute(attributeName)!=null){
			getJSONString=sendElement.getAttribute(attributeName).getValue();
		}
		
		return getJSONString;
	}
}

