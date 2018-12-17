package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.naming.InvalidNameException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.UserExistsException;
import com.eazytec.service.UserManager;
import com.eazytec.webapp.listener.UserCounterListener;
import com.eazytec.webapp.util.ValidationUtil;
import com.google.common.collect.HashMultimap;

/**
 * Implementation of SimpleFormController that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * @author madan
 * @author mathi
 */
@Controller
@RequestMapping("bpm/admin/userform*")
public class UserFormController extends BaseFormController {
	
	protected final transient Log log = LogFactory.getLog(getClass());
    private RoleService roleService;
    private GroupService groupService;
    private DepartmentService departmentService;
    private String from = "";
    
    @Autowired
    public void setRoleService(RoleService roleService) {
  		this.roleService = roleService;
  	}
    
    @Autowired
    public void setGroupService(GroupService groupService) {
  		this.groupService = groupService;
  	}

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
        
    public UserFormController() {
        setCancelView("redirect:/mainMenu");
        setSuccessView("redirect:/bpm/admin/manageOrganization");
    }

    /**
	 * To bind date and string fields from controller to jsp
	 * 
	 * @param request
	 * @param binder
	 * 
	 * @return
	 */
    @Override
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(User user, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
    	log.debug("entering 'onSubmit' method...");
    	Model model = new ExtendedModelMap();
    	Locale locale = request.getLocale();
    	Integer originalVersion = user.getVersion();
    	String remoteUser = request.getRemoteUser();
    	String contextPath = request.getContextPath();
    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
        String pictureValue = request.getParameter("pictureByteArrayId");
        String userRoles = request.getParameter("userRoles");
		String userGroups = request.getParameter("userGroups"); 
		String whereTheWork = request.getParameter("whereTheWork");
		String resposibilities = request.getParameter("resposibilities");
		String comment = request.getParameter("comment");
		String language = request.getParameter("language");
		String preferredSkin = request.getParameter("preferredSkin");
		if(!StringUtil.isEmptyString(request.getParameter("from"))){
			from = request.getParameter("from");
		}
    	try{
	    	
	    	user = setUserFields(request, user);
	    	if(originalVersion == 0){
		        if(getUserManager().isDublicateUser(user.getUsername())){
		        	errors.rejectValue("username", "errors.existing.user",
	                        new Object[]{user.getUsername(), user.getEmail()}, "duplicate user");
		        	user = setUserReturnValues(user, originalVersion);
		        	model = setModelReturnValues(model, request, userRoles, userGroups);
		        	model.addAttribute("comment",comment);
			    	model.addAttribute("resposibilities", resposibilities);
			    	model.addAttribute("whereTheWork", whereTheWork);
			    	model.addAttribute("language", language);
			    	model.addAttribute("preferredSkin", preferredSkin);
		        	model.addAttribute("from",from);
		        	if(from.equalsIgnoreCase("profile")){
		            	return new ModelAndView("userProfile",model.asMap());
		            } else {
		            	return new ModelAndView("userform",model.asMap());
		            }
		        }
		        if(user.getEmail().length() > 0){
		        	if(getUserManager().isDuplicateEmail(user.getEmail(),null)){
			        	errors.rejectValue("email", "errors.existing.email",
		                        new Object[]{user.getEmail()}, "duplicate user");
			        	user = setUserReturnValues(user, originalVersion);
			        	model = setModelReturnValues(model, request, userRoles, userGroups);
			        	model.addAttribute("comment",comment);
				    	model.addAttribute("resposibilities", resposibilities);
				    	model.addAttribute("whereTheWork", whereTheWork);
				    	model.addAttribute("language", language);
				    	model.addAttribute("preferredSkin", preferredSkin);
			        	model.addAttribute("from",from);
			        	if(from.equalsIgnoreCase("profile")){
			            	return new ModelAndView("userProfile",model.asMap());
			            } else {
			            	return new ModelAndView("userform",model.asMap());
			            }
			        }
		        }
	    	} else {
	        	if(user.getEmail().length() > 0){
		        	if(getUserManager().isDuplicateEmail(user.getEmail(),user.getId())){
			        	errors.rejectValue("email", "errors.existing.email",
		                        new Object[]{user.getEmail()}, "duplicate user");
			        	user = setUserReturnValues(user, originalVersion);
			        	model = setModelReturnValues(model, request, userRoles, userGroups);
			        	model.addAttribute("comment",comment);
				    	model.addAttribute("resposibilities", resposibilities);
				    	model.addAttribute("whereTheWork", whereTheWork);
				    	model.addAttribute("language", language);
				    	model.addAttribute("preferredSkin", preferredSkin);
			        	model.addAttribute("from",from);
			        	if(from.equalsIgnoreCase("profile")){
			            	return new ModelAndView("userProfile",model.asMap());
			            } else {
			            	return new ModelAndView("userform",model.asMap());
			            }
			        }
	        	}
	        }
	    	
	    	if(!StringUtil.isEmptyString(user.getEmployeeNumber())){
		        if(getUserManager().isDuplicateEmployeeNumber(user.getEmployeeNumber(),user.getId())){
		        	errors.rejectValue("employeeNumber", "errors.existing.employeeNumber",
	                        new Object[]{user.getUsername(), user.getEmployeeNumber()}, "duplicate Employee Number");
		        	user = setUserReturnValues(user, originalVersion);
		        	model = setModelReturnValues(model, request, userRoles, userGroups);
		        	model.addAttribute("comment",comment);
			    	model.addAttribute("resposibilities", resposibilities);
			    	model.addAttribute("whereTheWork", whereTheWork);
			    	model.addAttribute("language", language);
			    	model.addAttribute("preferredSkin", preferredSkin);
		        	model.addAttribute("from",from);
		        	if(from.equalsIgnoreCase("profile")){
		            	return new ModelAndView("userProfile",model.asMap());
		            } else {
		            	return new ModelAndView("userform",model.asMap());
		            }
		        }
	    	}
	        
	    	if(!isValidUser(user, errors, request.getParameter("delete"))){
	        	user = setUserReturnValues(user, originalVersion);
	        	model = setModelReturnValues(model, request, userRoles, userGroups);
	        	model.addAttribute("comment",comment);
		    	model.addAttribute("resposibilities", resposibilities);
		    	model.addAttribute("whereTheWork", whereTheWork);
		    	model.addAttribute("language", language);
		    	model.addAttribute("preferredSkin", preferredSkin);
	        	model.addAttribute("from",from);
	        	if(from.equalsIgnoreCase("profile")){
	            	return new ModelAndView("userProfile",model.asMap());
	            } else {
	            	return new ModelAndView("userform",model.asMap());
	            }
	        }
	    	
	    	if (user.getDepartment() != null && user.getPartTimeDepartment() !=null){
				if(user.getDepartment().getName().equals(user.getPartTimeDepartment().getName())){
					errors.rejectValue("department","error.user.dep.part");
					user = setUserReturnValues(user, originalVersion);
		        	model = setModelReturnValues(model, request, userRoles, userGroups);
		        	model.addAttribute("comment",comment);
			    	model.addAttribute("resposibilities", resposibilities);
			    	model.addAttribute("whereTheWork", whereTheWork);
			    	model.addAttribute("language", language);
			    	model.addAttribute("preferredSkin", preferredSkin);
		        	model.addAttribute("from",from);
		        	if(from.equalsIgnoreCase("profile")){
		            	return new ModelAndView("userProfile",model.asMap());
		            } else {
		            	return new ModelAndView("userform",model.asMap());
		            }
				}
	    	}
	    	
	    	if (user.getManager() != null && user.getSecretary() != null ) {
	    		if(!user.getManager().isEmpty() && !user.getSecretary().isEmpty()){
	        		if( user.getManager().equals(user.getSecretary())){
	        			errors.rejectValue("manager","error.user.manager.sec");
	        			user = setUserReturnValues(user, originalVersion);
			        	model = setModelReturnValues(model, request, userRoles, userGroups);
			        	model.addAttribute("comment",comment);
				    	model.addAttribute("resposibilities", resposibilities);
				    	model.addAttribute("whereTheWork", whereTheWork);
				    	model.addAttribute("language", language);
				    	model.addAttribute("preferredSkin", preferredSkin);
			        	model.addAttribute("from",from);
			        	if(from.equalsIgnoreCase("profile")){
			            	return new ModelAndView("userProfile",model.asMap());
			            } else {
			            	return new ModelAndView("userform",model.asMap());
			            }
	        		}
	    		}
	    	}
	    //Logic of set role to user is moved to service for API
	    	if(StringUtil.isEmptyString(request.getParameter("userRoles"))){
	    		Set<Role> roles = new HashSet<Role>();
	    		 getUserManager().setDefaultRoleForUser(user,true,roles);
	    		/*if(user.getRoles() == null || user.getUserRole().isEmpty()){
		    		Role userRole = roleService.getRoleByName("ROLE_"+user.getId()+"_USER");
		    		if(userRole == null){
						Role role=new Role("ROLE_"+user.getId()+"_USER","ROLE_"+user.getId()+"_USER","Internal");
						role=roleService.save(role);
						user.setUserRole(role.getName());
						roles.add(role);
						user.setRoles(roles);
		    		} else {
		    			user.setUserRole(userRole.getName());
		    			roles.add(userRole);
		    			user.setRoles(roles);
		    		}
				}else{
					Role role=roleService.getRoleByName("ROLE_"+user.getId()+"_USER");
					user.addRole(role);
				}*/
	    	} else {
	    		getUserManager().setDefaultRoleForUser(user,false,null);
	    		/*if(user.getRoles() == null || user.getUserRole().isEmpty()){
		    		Role userRole = roleService.getRoleByName("ROLE_"+user.getId()+"_USER");
		    		if(userRole == null){
						Role role=new Role("ROLE_"+user.getId()+"_USER","ROLE_"+user.getId()+"_USER","Internal");
						role=roleService.save(role);
						user.setUserRole(role.getName());
						user.addRole(role);
		    		} else {
		    			user.addRole(userRole);
		    		}
				}else{
					Role role=roleService.getRoleByName("ROLE_"+user.getId()+"_USER");
					user.addRole(role);
				}*/
	    	}
	    	if(file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
				user.setPictureByteArrayId(getUserProfileImagePath(user,file, remoteUser, contextPath));
			} else if(pictureValue.equals("/images/profile/default.png")) {
				user.setPictureByteArrayId("/images/profile/default.png");					
			}
	    	//user.setPictureByteArrayId(getUserProfileImagePath(user,file, remoteUser, contextPath));
	    	model.addAttribute("comment",comment);
	    	model.addAttribute("resposibilities", resposibilities);
	    	model.addAttribute("whereTheWork", whereTheWork);
	    	model.addAttribute("language", language);
	    	model.addAttribute("preferredSkin", preferredSkin);
            getUserManager().saveUser(user);
	    	model.addAttribute("password", user.getPassword());
	    	

            reloadContext(request.getSession().getServletContext());
            addUserMap(user, request.getSession().getServletContext());
            user = setUserReturnValues(user, user.getVersion());
        	model = setModelReturnValues(model, request, userRoles, userGroups);
        	
            if(originalVersion == 0){
   				saveMessage(request, getText(user.getFullName()+" user.added",locale));
   				log.info("User Name : "+user.getFirstName()+" "+user.getLastName()+" "+getText("add.success",locale));
   			}else{
   				saveMessage(request, getText("user.saved", user.getFullName(), locale));
   				log.info("User Name : "+user.getFirstName()+" "+user.getLastName()+" "+getText("save.success",locale));
   			}
            model.addAttribute("from",from);
        	if(from.equalsIgnoreCase("profile")){
            	return new ModelAndView("userProfile",model.asMap());
            } else {
            	return new ModelAndView("userform",model.asMap());
            	//return new ModelAndView("redirect:/bpm/admin/manageOrganization");

            }
        } catch (InvalidNameException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(ade.getMessage());
            saveError(request, getText("user.file.maskmsg",locale));
            user = setUserReturnValues(user, originalVersion);
        	model = setModelReturnValues(model, request, userRoles, userGroups);
        	model.addAttribute("comment",comment);
	    	model.addAttribute("resposibilities", resposibilities);
	    	model.addAttribute("whereTheWork", whereTheWork);
	    	model.addAttribute("language", language);
	    	model.addAttribute("preferredSkin", preferredSkin);
        	model.addAttribute("from",from);
        	if(from.equalsIgnoreCase("profile")){
            	return new ModelAndView("userProfile",model.asMap());
            } else {
            	return new ModelAndView("userform",model.asMap());
            }
        }catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(ade.getMessage());
            saveError(request,ade.getMessage());
           // response.sendError(HttpServletResponse.SC_FORBIDDEN);
            errors.rejectValue("email", "errors.existing.user",
                    new Object[]{user.getUsername(), user.getEmail()}, "duplicate user");
            user = setUserReturnValues(user, originalVersion);
        	model = setModelReturnValues(model, request, userRoles, userGroups);
        	model.addAttribute("comment",comment);
	    	model.addAttribute("resposibilities", resposibilities);
	    	model.addAttribute("whereTheWork", whereTheWork);
	    	model.addAttribute("language", language);
	    	model.addAttribute("preferredSkin", preferredSkin);
        	model.addAttribute("from",from);
        	if(from.equalsIgnoreCase("profile")){
            	return new ModelAndView("userProfile",model.asMap());
            } else {
            	return new ModelAndView("userform",model.asMap());
            }
        } catch (UserExistsException e) {
            errors.rejectValue("email", "errors.existing.user",
                    new Object[]{user.getUsername(), user.getEmail()}, "duplicate user");
            user = setUserReturnValues(user, originalVersion);
        	model = setModelReturnValues(model, request, userRoles, userGroups);
        	model.addAttribute("comment",comment);
	    	model.addAttribute("resposibilities", resposibilities);
	    	model.addAttribute("whereTheWork", whereTheWork);
	    	model.addAttribute("language", language);
	    	model.addAttribute("preferredSkin", preferredSkin);
        	model.addAttribute("from",from);
        	if(from.equalsIgnoreCase("profile")){
            	return new ModelAndView("userProfile",model.asMap());
            } else {
            	return new ModelAndView("userform",model.asMap());
            }
        }catch (Exception e) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(e.getMessage());
            saveError(request,"Problem while submitting values");
            errors.rejectValue("email", "errors.existing.user",
                    new Object[]{user.getUsername(), user.getEmail()}, "duplicate user");
            user = setUserReturnValues(user, originalVersion);
        	model = setModelReturnValues(model, request, userRoles, userGroups);
        	model.addAttribute("language", language);
	    	model.addAttribute("preferredSkin", preferredSkin);
        	model.addAttribute("comment",comment);
	    	model.addAttribute("resposibilities", resposibilities);
	    	model.addAttribute("whereTheWork", whereTheWork);
        	model.addAttribute("from",from);
        	if(from.equalsIgnoreCase("profile")){
            	return new ModelAndView("userProfile",model.asMap());
            } else {
            	return new ModelAndView("userform",model.asMap());
            }
        }
    }

	//@ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		Model model = new ExtendedModelMap();
    		User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	if(!StringUtil.isEmptyString(request.getParameter("from"))){
    			from = request.getParameter("from");
    		}
        	String userRoles = "";
        	String userGroups = "";
        	String comment = "";
        	String whereTheWork = "";
        	String rerponsibilities = "";
        	String language = "";
        	String preferredSkin = "";
            String userId = request.getParameter("id");
            //String comment = request.getParameter("comment");
            // If not an administrator, make sure user is not trying to add or edit another user
          //  if (!request.isUserInRole(Constants.ADMIN_ROLE) && !isFormSubmission(request)) {
               // if (isAdd(request) || request.getParameter("id") != null || !setDepartmentAdminPermissionValues(model, loggedInUser)) {
        	if(StringUtils.isBlank(userId)){
    	    	if(!setDepartmentAdminPermissionValues(model, loggedInUser)){
                    log.warn("User '" + request.getRemoteUser() + "' is trying to edit user with id '" +
                            request.getParameter("id") + "'");
                    //saveError(request,"You do not have permission to modify other users.");
        			//return new ModelAndView("redirect:/bpm/admin/manageOrganization");
    	    	}
        	}
           // }
            if (!isFormSubmission(request)) {
                // if user logged in with remember me, display a warning that they can't change passwords
                log.debug("checking for remember me login...");

                AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
                SecurityContext ctx = SecurityContextHolder.getContext();

                if (ctx.getAuthentication() != null) {
                    Authentication auth = ctx.getAuthentication();

                    if (resolver.isRememberMe(auth)) {
                        request.getSession().setAttribute("cookieLogin", "true");

                        // add warning message
                        saveMessage(request, getText("userProfile.cookieLogin", request.getLocale()));
                    }
                }

                User user;
                if (userId == null && !isAdd(request)) {
                    user = getUserManager().getUserByUsername(request.getRemoteUser());
                    user.removeRole(roleService.getRole("ROLE_DEFAULT"));
                    userRoles = CommonUtil.getRolesAsString(user);
                    userGroups = CommonUtil.getGroupsAsString(user);
                }else if (!StringUtils.isBlank(userId) && !"".equals(request.getParameter("version"))) {
    	            user = getUserManager().getUserByUsername(userId);
    	            setDepartmentAdminPermissionForEdit(model,loggedInUser,user.getDepartment(),user.getId());
    	            user.removeRole(roleService.getRole("ROLE_DEFAULT"));
    	            userRoles = CommonUtil.getRolesAsString(user);
    	            userGroups = CommonUtil.getGroupsAsString(user);
    	           
                } else {
                    user = new User();
                    if (from.equalsIgnoreCase("department")){
                    	if(CommonUtil.isUserAdmin(loggedInUser)){
                    		user.setDepartment(new Department(request.getParameter("departmentId")));
                    	}else{
                    		user.setDepartment(loggedInUser.getDepartment());
                    	}
                    } 
                    if (from.equalsIgnoreCase("group")) {
                    	userGroups = request.getParameter("groupId");
                    } 
                    if (from.equalsIgnoreCase("role")) {
                    	userRoles = request.getParameter("roleId");
                    }else {
                    	userRoles = Constants.USER_ROLE;
                    }
                }
                preferredSkin = user.getPreferredSkin();
                language = user.getLanguage();
                comment = user.getComment();
                whereTheWork = user.getWhereTheWork();
                rerponsibilities = user.getResposibilities();
                user.setConfirmPassword(user.getPassword());
                if (request.isUserInRole(Constants.ADMIN_ROLE)) {
            		model.addAttribute("isAdmin",true);
            	}else{
            		model.addAttribute("isAdmin",false);
            	}
                	model.addAttribute("userRoles",userRoles);
                	model.addAttribute("userGroups",userGroups);
               // model.addAttribute("availableRoles",new HashSet<Role>(roleService.getRoles()));
                model.addAttribute("user",user);
                model.addAttribute("comment", comment);
                model.addAttribute("whereTheWork", whereTheWork);
                model.addAttribute("resposibilities", rerponsibilities);
                model.addAttribute("language", language);
    	    	model.addAttribute("preferredSkin", preferredSkin);
                model.addAttribute("from",from);
                
                if(from.equalsIgnoreCase("profile")){
                	return new ModelAndView("userProfile",model.asMap());
                } else {
                	return new ModelAndView("userform",model.asMap());
                }
            } else {
            	if (request.isUserInRole(Constants.ADMIN_ROLE)) {
            		model.addAttribute("isAdmin",true);
            	}else{
            		model.addAttribute("isAdmin",false);
            	}
                // populate user object from database, so all fields don't need to be hidden fields in form
    			model.addAttribute("user", getUserManager().getUser(request.getParameter("id")));
    			model.addAttribute("from",from);
    			if(from.equalsIgnoreCase("profile")){
    				return new ModelAndView("userProfile",model.asMap());
    			} else {
    			 	return new ModelAndView("userform",model.asMap());
    			}
            }
    	} catch(Exception e) {
            saveMessage(request, e.getMessage());
            e.printStackTrace();
    	}
		return null;
	}

    /**
     * set user values to return userform.
     * 
     * @param user
     * @param originalVersion
     * @return
     */
    private User setUserReturnValues(User user, int originalVersion){
    	 user.setVersion(originalVersion);
         user.setPassword(user.getPassword());
         user.setConfirmPassword(user.getPassword());
    	return user;
    }
    
    /**
     * set model values to return userform.
     * 
     * @param model
     * @param request
     * @param userRoles
     * @param userGroups
     * @return
     */
    private Model setModelReturnValues(Model model,HttpServletRequest request, String userRoles, String userGroups){
    	if (request.isUserInRole(Constants.ADMIN_ROLE)) {
     		model.addAttribute("isAdmin",true);
     	}else{
     		model.addAttribute("isAdmin",false);
     	}
         model.addAttribute("userRoles",userRoles);
     	model.addAttribute("userGroups",userGroups);
    	return model;
    }
    
    /**
	 * method to check isFormSubmission
	 *
	 * @param request
	 * @return 
	 */
    private boolean isFormSubmission(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("post");
    }

    /**
	 * method to check it's add user or not
	 *
	 * @param request
	 * @return 
	 */
    protected boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }
    
    /**
	 * method to validate user
	 *
	 * @param user
	 * @param errors
	 * @param delete
	 * @return 
	 */
	private boolean isValidUser(User user, BindingResult errors, String delete){
    	if (validator != null) {
            validator.validate(user, errors);

            if (errors.hasErrors() && delete == null) { // don't validate when deleting
                return false;
            }else{
            	return true;
            }
        }else{
        	return true;
        }
    }
	
	/**
	 * method to set user details
	 *
	 * @param request
	 * @param user
	 */
	private User setUserFields(HttpServletRequest request, User user)throws Exception{
		// only attempt to change roles if user is admin for other users,
    	
		//commented for any user can create user f he is a admin for a deparment
//		if (request.isUserInRole(Constants.ADMIN_ROLE)) {
			Set<Role> roles = new HashSet<Role>();
	    	Set<Group> groups = new HashSet<Group>();
	    	String roleIds = request.getParameter("userRoles");
			String groupIds = request.getParameter("userGroups");
			
			if(user.getVersion() == 0){
				user.setId(user.getUsername());
			}
			
			if (roleIds != null && !roleIds.isEmpty()) {
				if (roleIds.contains(",")) {
					String[] ids = roleIds.split(",");
					for(String roleId :ids){
						roles.add(new Role(roleId, roleId));
					}
				} else {
					roles.add(new Role(roleIds, roleIds));
				}
				user.setRoles(roles);
			}else {
				user.setRoles(null);
			}
			
			if (groupIds != null && !groupIds.isEmpty()) {
				if (groupIds.contains(",")) {
					String[] ids = groupIds.split(",");
					for(String groupId :ids){
						groups.add(new Group(groupId, groupId));
					}
				} else {
					groups.add(new Group(groupIds, groupIds));
				}
				user.setGroups(groups);
			}else {
				user.setGroups(null);
			}
           
            if(user.getManager() != null){
            	user.setManager(user.getManager());
            }
            if(user.getDepartment()!=null){
            	if(user.getDepartment().getName() !=null && !user.getDepartment().getName().isEmpty()){
            		user.setDepartment(departmentService.getDepartment(user.getDepartment().getName()));
            	}else{
            		user.setDepartment(null);
            	}
           }else{
        	   user.setDepartment(null);
           }
           if(user.getPartTimeDepartment()!=null){
            	if(user.getPartTimeDepartment().getName() !=null && !user.getPartTimeDepartment().getName().isEmpty()){
            		user.setPartTimeDepartment(departmentService.getDepartment(user.getPartTimeDepartment().getName()));
            	}else{
            		user.setPartTimeDepartment(null);
            	}
           }else{
        	   user.setPartTimeDepartment(null);
           }
           
           return user;
           
//        } else {
//            // if user is not an admin then load roles from the database
//            // (or any other user properties that should not be editable by users without admin role) 
//            User cleanUser = getUserManager().getUserByUsername(
//                    request.getRemoteUser());
//            
//            user.getRoles().clear();
//            user.setRoles(cleanUser.getRoles());
//            user.getGroups().clear();
//            user.setGroups(cleanUser.getGroups());
//            user.setDepartment(cleanUser.getDepartment());
//            user.setPartTimeDepartment(cleanUser.getPartTimeDepartment());
//            user.setVersion(user.getVersion()+1);
//            return user;
//        }
	}
	
	/**
	 * method to get user profile image path
	 *
	 * @param user
	 * @param file
	 * @param remoteUser
	 * @param contextPath
	 * @return pictureByteArrayId
	 * @throws IOException
	 * @throws InvalidNameException 
	 */
	private String getUserProfileImagePath(User user, CommonsMultipartFile file,String remoteUser, String contextPath)throws IOException, InvalidNameException{
		 if (file!=null){
			 String fileName = file.getOriginalFilename();
			 int pos = fileName.lastIndexOf(".");
			 String extension = fileName.substring(pos+1);
			 fileName = fileName.substring(0, pos-1);
			 fileName = fileName.replace(" ", "_");
    	     byte[] encodedFileName = fileName.getBytes("UTF-8");
    	     fileName= encodedFileName.toString();
    	     if(fileName.startsWith("[")){
    	     fileName = fileName.substring(1);
    	     }
			 fileName = fileName+"."+extension;
    	     if(!ValidationUtil.validateImage(fileName)){
				 throw new InvalidNameException("Invalid FileName");
			 }
			 
             String uploadDir = getServletContext().getRealPath("/images") + "/" + remoteUser + "/";
             File dirPath = new File(uploadDir);

             if (!dirPath.exists()) {
                 dirPath.mkdirs();
             }
             
             InputStream stream = file.getInputStream();
             OutputStream bos = new FileOutputStream(uploadDir + fileName);
             int bytesRead;
             byte[] buffer = new byte[8192];

             while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                 bos.write(buffer, 0, bytesRead);
             }

             bos.close();
             stream.close();
             String link = contextPath + "/images" + "/" + remoteUser + "/";
             return link + fileName;
         }else if (user.getPictureByteArrayId().isEmpty()){
         	 return "/images/profile/default.png";
         }else {
        	 return user.getPictureByteArrayId();
         }
	}
	
	/**
	 * method for delete the selected users from users grid
	 * 
	 * @param userId
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/userformDeleteSelectedUsers", method = RequestMethod.GET)
	public ModelAndView deleteSelectedUsers(@RequestParam("userIds") String userId, HttpServletRequest request) {
		Locale locale = request.getLocale();
		 List<String> userIdList = new ArrayList<String>();
		 if (userId.contains(",")) {
			  String[] ids = userId.split(",");
			  for(String id :ids){
				  userIdList.add(id);
			  }
			} else {
				userIdList.add(userId);
			}
		try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String notDeletedUsers=getUserManager().deleteSelectedUsers(userIdList,user);
			if(notDeletedUsers==null){
				saveMessage(request, getText("success.user.delete",locale));
				log.info("User Names : "+userId+" "+getText("delete.success",locale));					
				userSessionInvalidation(userIdList);
			}else{
				saveError(request, getText("user.error.delete.privilege", new Object[]{notDeletedUsers, user.getFirstName()+" "+user.getLastName()}  , locale));
				log.info(getText("user.error.delete.privilege", new Object[]{notDeletedUsers, user.getFirstName()+" "+user.getLastName()}  , locale));
			}
			
      //	reloadContext(request.getSession().getServletContext());
		}catch(BpmException e){
			log.error(e.getMessage(), e);
			saveError(request, e.getMessage());
			return new ModelAndView("redirect:/bpm/admin/manageOrganization");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.user.delete",e.getMessage(),locale));
			return new ModelAndView("redirect:/bpm/admin/manageOrganization");
		}	
   		return new ModelAndView("redirect:/bpm/admin/manageOrganization");
	}
	
	/**
	 * method for update the user status enable/disable from the user grid
	 * 
	 * @param userId
	 * @param userStatus
	 */
	@RequestMapping(value = "bpm/admin/userformUpdateUserStatus", method = RequestMethod.GET)
	public String updateUserStatus(@RequestParam("userIds") String userId, @RequestParam("userStatus") String userStatus, HttpServletRequest request){
		Locale locale = request.getLocale();
		 List<String> userIdList = new ArrayList<String>();
		 boolean status = true;
		 if (userId.contains(",")) {
			  String[] ids = userId.split(",");
			  for(String id :ids){
				  userIdList.add(id);
			  }
			} else {
				userIdList.add(userId);
		}
		if(userStatus.equalsIgnoreCase("disable")){
			status=false;
		}
		try{
			boolean isLoginUser=false;
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			for(String usersId:userIdList){
				if(usersId.equals(user.getId())){
					isLoginUser=true;
				}
			}
			
			if(isLoginUser){
				saveError(request, getText("logIn.user.disable.error", new Object[]{user.getFullName()}  , locale));
				log.error( getText("logIn.user.disable.error", new Object[]{user.getFullName()}  , locale));
			}else{
				String notDeletedUsers=getUserManager().updateUserStatus(userIdList, status,user);
				if(notDeletedUsers==null){
					saveMessage(request, getText("success.user.disable",locale));
					log.info(getText("success.user.disable",locale));
					userSessionInvalidation(userIdList);
				}else{
					saveError(request, getText("user.error.disable.privilege", new Object[]{notDeletedUsers, user.getFirstName()+" "+user.getLastName()}  , locale));
					log.info( getText("user.error.disable.privilege", new Object[]{notDeletedUsers, user.getFirstName()+" "+user.getLastName()}  , locale));
				}
			}
		}catch(BpmException e){
			log.error(e.getMessage(), e);
			saveError(request, getText("error.user.delete",e.getMessage(),locale));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.user.delete",e.getMessage(),locale));
		}	
		return getSuccessView();
	}
	
	public void userSessionInvalidation(List<String> userIdList) {
		HashMultimap<String,Object> sessionMap = CommonUtil.getUserSessionInfo();
		for(String userid : userIdList) {
			for (Map.Entry<String, Object> e : sessionMap.entries()) {
				if(userid.equals(e.getKey())) {
					HttpSession session = (HttpSession) e.getValue();
					session.invalidate(); 
				}
			}
		}
	}
	
	private void reloadContext(ServletContext context){
		 context.setAttribute(Constants.AVAILABLE_USERS, new HashSet<LabelValue>(getUserManager().getAllUsers()));
//	     log.info("User : Drop-down initialization complete [OK]");
	}
	
	private void addUserMap(User user, ServletContext context){
		Map<String, String> usersMap= (Map<String, String>) context.getAttribute("usersMap");
		usersMap.put(user.getId(), user.getFullName());
		context.setAttribute("usersMap", usersMap);
		context.setAttribute("usersJSON", new JSONObject(usersMap));
	}
	
    private boolean setDepartmentAdminPermissionValues(Model model,User user) throws BpmException {
		boolean isAdmin = CommonUtil.isUserAdmin(user);
		boolean isDepartmentAdmin = false;
		if(!isAdmin){
	    	List<String> departmentIdsList = departmentService.getDepartmentAdminDepartmentIds(user.getId());
	    	if(departmentIdsList != null && !departmentIdsList.isEmpty()){
    			isDepartmentAdmin =true;
				if(departmentIdsList.size() == 1){
		    		for(String departmentId : departmentIdsList){
		    			model.addAttribute("superDepartment",departmentId);
		    			model.addAttribute("superDepartmentLabel",departmentId);
		    		}
				}
	    	}
		}
		if(isAdmin || isDepartmentAdmin){
			model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
			model.addAttribute("isSystemAdmin",isAdmin);
			return true;
		}
		return false;
    }
    
    /**
     * {@inheritDoc}
     */
    private void setDepartmentAdminPermissionForEdit(Model model,User user,Department department,String userIdToEdit) throws BpmException {
		boolean isAdmin = CommonUtil.isUserAdmin(user);
		boolean permitToEdit = false;
		if(!isAdmin){
			if(department != null){
		    	Set<String> departmentIdsSet = new HashSet<String>();
		    	List<String> departmentIdsList = departmentService.getDepartmentAdminDepartmentIds(user.getId());
		    	if(departmentIdsList != null && !departmentIdsList.isEmpty()){
			    	departmentIdsSet.addAll(departmentIdsList);
		    	}
	    		for(String adminDepartmentId : departmentIdsSet){
	    			if(adminDepartmentId.equals(department.getId())){
	    				model.addAttribute("isDepartmentAdmin",true);
	    				permitToEdit = true;
	    			}
	    		}
			}else {
				permitToEdit = false;
			}
		}else {
			model.addAttribute("isSystemAdmin",true);
			permitToEdit = true;
		}
		if(!permitToEdit){
			 String superiorIdStr = getUserManager().getAllRelativeIds(userIdToEdit, false);
			 if(superiorIdStr!= null){
				 for(String superiorId:superiorIdStr.split(",")){
				     if(superiorId.equals(user.getId())){
							permitToEdit = true;
							break;
				     }
			     }
			 }
		}
		model.addAttribute("permitToEdit",permitToEdit);
    }
}