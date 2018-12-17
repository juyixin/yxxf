package com.eazytec.webapp.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cmd.NeedsActiveExecutionCmd;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eazytec.Constants;
import com.eazytec.bpm.common.util.ProcessDefinitionUtil;
import com.eazytec.bpm.common.util.TaskUtil;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.TemplateUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.User;
import com.eazytec.service.LookupManager;
import com.eazytec.util.DateUtil;
import com.eazytec.util.JSTreeUtil;
import com.eazytec.model.Process;

/**
 * <p>The controller for process related operations like its CRUD, list, grid view etc on screen</p>
 *
 * @author Karthick
 * @author madan
 * @author Revathi
 * @author nkumar
 */

@Controller
public class FileController extends BaseFormController {

    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass())
     * from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    

    @RequestMapping(value="bpm/manageFile/downloadFile" , method = RequestMethod.GET )
    public void bulkProcessXmlDownload(@RequestParam("filePath") String filePath,HttpServletResponse response,ModelMap model,HttpServletRequest request) throws IOException{
    	InputStream fStream = null;
        Locale locale = request.getLocale();
        try{     
        	ServletContext context = request.getSession().getServletContext();
        			String mimeType = context.getMimeType(filePath);
        			File downloadFile = new File(filePath);
        	if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
        	response.setContentType(mimeType);
             response.setHeader("Content-Disposition", "attachment; filename="+downloadFile.getName());
             fStream = new FileInputStream(filePath);           
             
             FileCopyUtils.copy(fStream, response.getOutputStream());
 			 log.info("file downloaded uccessfully");
              }catch(Exception e){
            	  log.error(e.getMessage(),e);
                  saveError(request, "Problem downloading file ");
              }finally{
                  if(fStream != null){
                      fStream.close();
                  }
              }
  }
}