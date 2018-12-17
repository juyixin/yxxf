package com.eazytec.webapp.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.sysConfig.service.SysConfigManager;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.SysConfig;
import com.eazytec.service.UserManager;
import com.eazytec.util.PropertyReader;

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 * 
 * <p>
 * <a href="UserFormController.java.html"><i>View Source</i></a>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
public class SysConfigController extends BaseFormController {

	private SysConfigManager sysConfigManager;

	public VelocityEngine velocityEngine;

	@Autowired
	public void setsysConfigManager(SysConfigManager sysConfigManager) {
		this.sysConfigManager = sysConfigManager;
	}

	public SysConfigManager getsysConfigManager() {
		return sysConfigManager;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@RequestMapping(value = "bpm/admin/sysConfigForm", method = RequestMethod.GET)
	public ModelAndView showEventForm(SysConfig sysConfig,
			BindingResult errors, Model model, HttpServletRequest request) {
		log.info("show event form");
		model.addAttribute("sysConfig", sysConfig);
		((HashMap<String, Object>) model).put("mode", "create");
		return new ModelAndView("admin/sysConfigForm", model.asMap());
	}

	@RequestMapping(value = "bpm/admin/saveSysConfig", method = RequestMethod.POST)
	public ModelAndView saveSysConfig(SysConfig sysConfig,
			BindingResult errors, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		String id = sysConfig.getId();
		try {
			Locale locale = request.getLocale();
			if (sysConfig.getId().equals("")) {
				if (sysConfigManager
						.checkSysConfigKey(sysConfig.getSelectKey())) {
					// sysConfig.setId(id);

					saveError(request,
							getText("sysconfig.name.duplicate", locale));
					model.addAttribute("sysConfig", sysConfig);

					return new ModelAndView("admin/sysConfigForm", model);
				}
			}

			if (validator != null) {
				validator.validate(sysConfig, errors);
				if (errors.hasErrors()) {

					return new ModelAndView("admin/sysConfigForm", model);

				}
			}
			if (null == sysConfig.getId() || sysConfig.getId().isEmpty()) {
				sysConfig.setId(null);
			}
			getsysConfigManager().saveSysConfig(sysConfig);
			loadSystemConfigurationVariable(sysConfig.getSelectType(),
					sysConfig.getSelectKey(), sysConfig.getSelectValue());
			if(StringUtil.isEmptyString(id)){
				saveMessage(request, getText("success.sysconfig.save",locale));
				log.info("SysConfig saved Successfully");
			} else {
				saveMessage(request, getText("success.sysconfig.update",locale));
				log.info("SysConfig updated Successfully");
			}
		} catch (Exception e) {
			log.error(e);
			errors.rejectValue("error.sysconfig.save", e.getMessage());
			return new ModelAndView("admin/sysConfigForm", model);
		}
		return showSysConfigGrid(request, response);
	}

	@RequestMapping(value = "bpm/admin/editSysConfig", method = RequestMethod.GET)
	public ModelAndView editSysConfig(@RequestParam("id") String id,
			ModelMap model) throws Exception {
		SysConfig sysconfig = sysConfigManager.getId(id);
		model.put("sysConfig", sysconfig);
		model.put("mode", "edit");
		log.info("SysConfig edited successfully");
		return new ModelAndView("admin/sysConfigForm", model);
	}

	@RequestMapping(value = "bpm/admin/deleteSysConfig", method = RequestMethod.GET)
	public ModelAndView deleteSysConfig(
			@RequestParam("sysConfigIds") String sysConfigIds,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		Locale locale = request.getLocale();
		String[] ids = sysConfigIds.split(",");

		try {
			sysConfigManager.deleteSelectedSysConfig(Arrays.asList(ids));
			saveMessage(request, getText("success.sysconfig.delete", locale));
			log.info("SysConfig deleted successfully");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request,
					getText("error.sysconfig.delete", e.getMessage(), locale));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request,
					getText("error.sysconfig.delete", e.getMessage(), locale));

		}
		return showSysConfigGrid(request, response);
	}

	@RequestMapping(value = "bpm/admin/showSysConfigGrid", method = RequestMethod.GET)
	public ModelAndView showSysConfigGrid(HttpServletRequest request,
			HttpServletResponse response) {
		Model model = new ExtendedModelMap();
		Locale locale = request.getLocale();
		
		try {
			List<SysConfig> sysconfig = sysConfigManager.getAllSysConfig();
			String script = GridUtil.generateScriptForSysConfigGrid(
					CommonUtil.getMapListFromObjectList(sysconfig),
					velocityEngine);
			model.addAttribute("script", script);
	
			log.info("SysConfig Grid Displayed Successfully");
		} catch (Exception e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.sysconfigs.getEvents", locale));
		}
		return new ModelAndView("admin/SysConfigGrid", model.asMap());
	}

	public void loadSystemConfigurationVariable(final String fileKey,
			final String propertyKey, final String value) throws BpmException {
		try {
			PropertyReader.getInstance().setProperty(fileKey, propertyKey,
					value);
		} catch (Exception e) {
			throw new BpmException(e.getMessage(), e);
		}
	}
	
	/**
   	 * get license save page
   	 * @param model
   	 * @param request
   	 * @return
   	 */
   	@RequestMapping(value = "/bpm/license/saveLicense", method = RequestMethod.GET)
	public ModelAndView saveLicense(ModelMap model,HttpServletRequest request) {
   		try{
   			String licenseKey = request.getParameter("licenseKey");
   			if(!licenseKey.isEmpty() && licenseKey.length() > 0 && licenseKey != ""){
   			licenseKey = licenseKey.replace('`', '&');
   			SysConfig sysConfig = getsysConfigManager().getSysConfigByKey("license.public.key");
   			sysConfig.setSelectValue(licenseKey);
   			getsysConfigManager().saveSysConfig(sysConfig);
   			loadSystemConfigurationVariable(sysConfig.getSelectType(),
					sysConfig.getSelectKey(), sysConfig.getSelectValue());
   			} else {
   				throw new Exception ("Invalid license key !!!");
   				
   			}
   		} catch(Exception e){
   			log.error(e.getMessage());
   		}
		return new ModelAndView("redirect:/logout");
	}
}