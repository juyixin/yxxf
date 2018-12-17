package com.eazytec.bpm.metadata.process.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.Diagram;
import org.oryxeditor.server.diagram.DiagramBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.process.service.ProcessModelerService;
import com.eazytec.model.Classification;
import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

import de.hpi.bpmn2_0.ExportValidationEventCollector;
import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.model.Definitions;
import de.hpi.bpmn2_0.transformation.BPMNPrefixMapper;
import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;
import de.hpi.util.reflection.ClassFinder;


@Service("processModelerService")
public class ProcessModelerServiceImpl implements ProcessModelerService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService;
	private ProcessDefinitionService processDefinitionService;
	private FormDefinitionService formService;
	
	@Autowired
	public void setFormService(FormDefinitionService formService) {
		this.formService = formService;
	}
	
	@Autowired
	public void setRtProcessService(com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}
	
	@Autowired
	public void setProcessDefinitionService(ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}
	
	
	public List<ProcessDefinition> getAllProcessDefinitionVersions(String processName){
		return processDefinitionService.getAllProcessDefinitionVersions(processName);
	}
	
	
	@Override
	public boolean createProcess(HttpServletRequest request,String processName,String classificationId,Integer orderNo,ServletContext getServletContext) throws Exception {

		String svgDOM = new String(request.getParameter("svg").getBytes("UTF-8"));
		String jsonData = request.getParameter("data");
		String description = request.getParameter("summary");
		
		boolean asXML = request.getParameter("xml") != null;
		
		if(description!=null){
			description = URLDecoder.decode(description, "UTF-8");
		}
		
		if(classificationId != null){
			log.info("Creating Process : "+processName+" in "+classificationId);
			MDC.put("processName",processName);
			boolean isClassificationExist = rtProcessService.isClassificationExist(classificationId);
			if(!isClassificationExist){
				Classification classification = new Classification();
	            classification.setName(classificationId);
	            classification.setDescription(classificationId);
	            classificationId = classificationId.toLowerCase().replaceAll(" ", "_");
				classification.setId(classificationId);
	            rtProcessService.saveClassification(classification,request);		            
			}else{
				classificationId = classificationId.toLowerCase().replaceAll(" ", "_");
			}
		}

		List<Class<? extends AbstractBpmnFactory>> factoryClasses = ClassFinder.getClassesByPackageName(AbstractBpmnFactory.class,"de.hpi.bpmn2_0.factory", request.getSession().getServletContext());

		StringWriter output = performTransformationToDi(jsonData, asXML, factoryClasses,request.getSession().getServletContext());

		String bpmnPrefixReplaceBefore = output.toString().replace("ns2:", "");
		
		String bpmnPrefixReplaceAfter = bpmnPrefixReplaceBefore.replace(":ns2", "");
		
		int beginIndex = bpmnPrefixReplaceAfter.indexOf("<bpmndi:processDiagram");
		int endIndex = bpmnPrefixReplaceAfter.indexOf("</bpmndi:processDiagram>") + 24;
		
		String bpmnXML = bpmnPrefixReplaceAfter.replace(bpmnPrefixReplaceAfter.substring(beginIndex, endIndex), "");
		
		//xml parsing compatible to the acitiviti process defnition creation.
		bpmnXML = formService.parseXmlByDomParser(bpmnXML, processName,svgDOM,jsonData,true);
		
		formService.setProcessPermission(bpmnXML, processName);
		// the directory to upload to
		String uploadDir = getServletContext.getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
		
		File dirPath = new File(uploadDir);

		// Create the directory if it doesn't exist
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		String fileNameWithPath = uploadDir + processName + ".bpmn20.xml";
		FileWriter fr = new FileWriter(new File(fileNameWithPath));
		Writer br = new BufferedWriter(fr);
		br.write(bpmnXML);
		br.close();
		
		boolean isDeployed = processDefinitionService.deployBPMNSourceFileWithJson(
				processName+".bpmn20.xml", fileNameWithPath,description,classificationId,orderNo, jsonData, svgDOM);
		
		return isDeployed;
		
	}	
	
	@Override
	public JSONObject editProcess(HttpServletRequest request) throws JSONException {
		JSONObject result = new JSONObject();
		
		result.put("modelId", "oryx-canvas123");
		
		result.put("parent", "/directory/root-directory");
		
		String modelData = null;
		
//		log.info("params key : "+request.getParameter("key"));
		
		ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(request.getParameter("id").replaceAll(" ", "_"));
		log.info("Editing Process : "+processDefinition.getName());
		List<ProcessDefinition>processDefinitions = new ArrayList<ProcessDefinition>();
		processDefinitions.add(processDefinition);
		List<Map<String, Object>> processDefinitionsMapAsList = processDefinitionService.resolveProcessDefinitions(processDefinitions);
		
		Map<String, Object>processDefinitionMap = processDefinitionsMapAsList.get(0);
		
		String resourceName = (String) processDefinitionMap.get("resourceName");
		ResourceEntity resource = rtProcessService.getResourceAsEntity(processDefinition.getDeploymentId(), resourceName);
		
		modelData = new String(resource.getJsonString());//new String("{'resourceId':'oryx-canvas123','properties':{'name':'','documentation':'','auditing':'','monitoring':'','version':'','author':'','language':'English','namespaces':'','targetnamespace':'http://www.omg.org/bpmn20','expressionlanguage':'http://www.w3.org/1999/XPath','typelanguage':'http://www.w3.org/2001/XMLSchema','creationdate':'','modificationdate':''},'stencil':{'id':'BPMNDiagram'},'childShapes':[{'resourceId':'oryx_2C42161A-97AC-49CE-8868-1150EF37E13B','properties':{'name':'','documentation':'','auditing':'','monitoring':'','eventdefinitionref':'','eventdefinitions':'','dataoutputassociations':'','dataoutput':'','outputset':'','bgcolor':'#ffffff','form':'','trigger':'None'},'stencil':{'id':'StartNoneEvent'},'childShapes':[],'outgoing':[],'bounds':{'lowerRight':{'x':144,'y':162},'upperLeft':{'x':114,'y':132}},'dockers':[]}],'bounds':{'lowerRight':{'x':1485,'y':1050},'upperLeft':{'x':0,'y':0}},'stencilset':{'url':'/oryx//stencilsets/bpmn2.0/bpmn2.0.json','namespace':'http://b3mn.org/stencilset/bpmn2.0#'},'ssextensions':[]}");
		
		JSONObject modelJSON = new JSONObject(modelData);
		
		result.put("model", modelJSON);
		result.put("name", "openExisting");
		result.put("description", "");
		
		result.put("modelHandler", "/editor");
		
		result.put("revision", 0);
		
		result.put("versioning", true);
		result.put("designMode", "edit");
		
		return result;
	}
	
	/**
     * Triggers the transformation from Diagram to BPMN model and writes the
     * resulting BPMN XML on success.
     * 
     * @param json
     *            The diagram in JSON format
     * @param writer
     *            The HTTP-response writer
     * @throws Exception
     *             Exception occurred while processing
     */
    protected StringWriter performTransformationToDi(String json, boolean asXML, List<Class<? extends AbstractBpmnFactory>> factoryClasses,ServletContext servletcontext) throws Exception {
        StringWriter writer = new StringWriter();
        JSONObject result = new JSONObject();

        /* Retrieve diagram model from JSON */

        Diagram diagram = DiagramBuilder.parseJson(json);

        /* Build up BPMN 2.0 model */
        Diagram2BpmnConverter converter = new Diagram2BpmnConverter(diagram, factoryClasses);
        Definitions bpmnDefinitions = converter.getDefinitionsFromDiagram();

        /* Perform XML creation */
        JAXBContext context = JAXBContext.newInstance(Definitions.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        NamespacePrefixMapper nsp = new BPMNPrefixMapper();
        
        marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", nsp);

        /* Set Schema validation properties */
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

        String xsdPath = servletcontext.getRealPath("/oryx/lib/xsd/bpmn20/BPMN20.xsd");
        
        Schema schema = sf.newSchema(new File(xsdPath));
        marshaller.setSchema(schema);
        
        ExportValidationEventCollector vec = new ExportValidationEventCollector();
        marshaller.setEventHandler(vec);

        /* Marshal BPMN 2.0 XML */
        marshaller.marshal(bpmnDefinitions, writer);
        
        if (asXML) {
            return writer;
        }
		
        result.put("xml", writer.toString());

        /* Append XML Schema validation results */
        /*if (vec.hasEvents()) {
            ValidationEvent[] events = vec.getEvents();
            StringBuilder builder = new StringBuilder();
            builder.append("Validation Errors: <br /><br />");

            for (ValidationEvent event : Arrays.asList(events)) {

                builder.append("Line: ");
                builder.append(event.getLocator().getLineNumber());
                builder.append(" Column: ");
                builder.append(event.getLocator().getColumnNumber());

                builder.append("<br />Error: ");
                builder.append(event.getMessage());
                builder.append("<br /><br />");
            }
            result.put("validationEvents", builder.toString());
        }*/

        /* Prepare output */
        /*writer = new StringWriter();
        writer.write(result.toString());*/

        
        return writer;
    }

	@Override
	public List<String> userNameCheck(String strUserList) throws Exception {
		return processDefinitionService.userNameCheck(strUserList);
	}
	
	@Override
	public List<Map<String,String>> userNameCheckFullName(String strUserList) throws Exception {
		return processDefinitionService.userNameCheckFullName(strUserList);
	}

	@Override
	public String showXMLSource(HttpServletRequest request,boolean asXML,String processName,String jsonData,String svgDOM,boolean isProcessCreate) throws Exception {
		String bpmnXML = "";
		
		List<Class<? extends AbstractBpmnFactory>> factoryClasses = ClassFinder
				.getClassesByPackageName(AbstractBpmnFactory.class,
						"de.hpi.bpmn2_0.factory", request.getSession()
								.getServletContext());

		StringWriter output = performTransformationToDi(
				request.getParameter("data"), asXML, factoryClasses,
				request.getSession().getServletContext());

		String prefixReplace = output.toString().replace("ns2:", "");
		prefixReplace = prefixReplace.replace(":ns2", "");
		int beginIndex = prefixReplace.indexOf("<bpmndi:processDiagram");
		int endIndex = prefixReplace.indexOf("</bpmndi:processDiagram>") + 24;
		bpmnXML = prefixReplace.replace(prefixReplace.substring(beginIndex, endIndex), "");
		
		//xml parsing compatible to the acitiviti process defnition creation.
		bpmnXML = formService.parseXmlByDomParser(bpmnXML, processName,svgDOM,jsonData,isProcessCreate);
		
		
		return bpmnXML;
	}

}
