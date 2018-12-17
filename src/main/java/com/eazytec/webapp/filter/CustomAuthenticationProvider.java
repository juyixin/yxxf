package com.eazytec.webapp.filter;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.exceptions.BPMAccountStatusException;
import com.eazytec.model.User;
import com.eazytec.service.UserService;
import com.eazytec.util.LicenseToken;
import com.eazytec.util.LicenseValidator;
import com.eazytec.util.PropertyReader;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	@Autowired
	private ReflectionSaltSource saltSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private Logger logger = Logger.getLogger(CustomAuthenticationProvider.class);
	private CaptchaCaptureFilter captchaCaptureFilter;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	    String username = String.valueOf(authentication.getPrincipal()).toLowerCase();
	    String password = String.valueOf(authentication.getCredentials());
	    logger.debug("Checking authentication for user {}"+ username);
	    logger.debug("userResponse: {}"+ captchaCaptureFilter.getCaptcha_response());
	    if (StringUtils.isBlank(username)
	            || StringUtils.isBlank(password)) {
	        throw new BadCredentialsException("No Username and/or Password Provided.");
	    }
	    
    	licensePreCheck();
	    
	    
	    Boolean isCaptchaNeeded = Boolean.valueOf(PropertyReader.getInstance()
	    		.getPropertyFromFile("Boolean", "system.captcha.needed"));  
	    
	   Boolean adEnabled = Boolean.valueOf(PropertyReader.getInstance()
	    		    		.getPropertyFromFile("Boolean", "system.ad.enabled")); 		
	   
	  // if(!adEnabled){
		   
		    if(isCaptchaNeeded && StringUtils.isBlank(captchaCaptureFilter.getCaptcha_response())) {
		        throw new BadCredentialsException("Captcha Response is Empty");
		    }
		    
		    
			if (isCaptchaNeeded) {
				// else {
				// Send HTTP request to validate user's Captcha
				boolean captchaPassed = SimpleImageCaptchaServlet.validateCaptcha(
						captchaCaptureFilter.getCaptcha_challenge(),
						captchaCaptureFilter.getCaptcha_response());
	
				// Check if valid
				if (captchaPassed) {
					logger.debug("Captcha is valid!");
					resetCaptchaFields();
				} else {
					logger.debug("Captcha is invalid!");
					resetCaptchaFields();
	
					throw new BPMAccountStatusException(I18nUtil.getMessageProperty("errors.captcha.mismatch"));
				}
			}
			 User user = null;
			if(!adEnabled){
	         user = userService.getUserById(username);
			}
	        if (user == null && adEnabled) {
	            throw new BadCredentialsException(I18nUtil.getMessageProperty("errors.password.mismatch")); 
	        }
	        if (user == null || !user.isEnabled() && !adEnabled) {
	            throw new BPMAccountStatusException(I18nUtil.getMessageProperty("errors.password.mismatch")); 
	        }
	        if (passwordEncoder.isPasswordValid(user.getPassword(), password, saltSource.getSalt(user))) {
	            Set<GrantedAuthority> authorityList = (Set<GrantedAuthority>) user.getAuthorities();
	            return new UsernamePasswordAuthenticationToken(user, password, authorityList);
	        }
	        else {
	        	if(adEnabled){
	        		throw new BadCredentialsException(I18nUtil.getMessageProperty("errors.password.mismatch"));
	        	}else{
	        		throw new BPMAccountStatusException(I18nUtil.getMessageProperty("errors.password.mismatch")); 
	        	}
	        }
	   }
	      
	  //  }
	//}
	
	
	/**
	 * Before-hand check of license validity of the session and redirect to expired page
	 * if invalid
	 * @throws IOException
	 */
	public void licensePreCheck() throws BPMAccountStatusException{
		try {
			//System.out.println("asfasdfasd before ceck=========");
			if(!LicenseValidator.isLicenseApplicable()){
				logger.info(LicenseToken.LICENSE_CHECK_NOT_APPLICABLE);
				PropertyReader.getInstance().setProperty("Boolean", "license.warn.expiry", "false" );
				return;
			}
			//System.out.println("licebse chekc is needed========");
			logger.info(LicenseToken.LICENSE_CHECK_NEEDED);
			String licenseKey = PropertyReader.getInstance().getPropertyFromFile("String", "license.public.key");
			//String licenseErrorMessage = PropertyReader.getInstance().getPropertyFromFile("LICENSE_PARAM","license.err.message");
			
			boolean isValidLicense =false;
			try{
				isValidLicense = licenseCheck(licenseKey);
			}catch(Exception e){
				logger.info("License Key after Validation failed::: "+licenseKey, e);
				throw new Exception(e.getMessage()); 
			}
						
			if(!isValidLicense){
				logger.info(LicenseToken.LICENSE_KEY_INVALID+licenseKey);
				throw new BPMAccountStatusException(I18nUtil.getMessageProperty("license.expired")); 
			}else{
				try{
					//If user has valid license, mark if he is close to license expiry, and warning needed
					PropertyReader.getInstance().setProperty("String", "license.days.to.expire", String.valueOf(LicenseValidator.getDaysToExpire(licenseKey)) );
					PropertyReader.getInstance().setProperty("String", "license.warn.expiry", String.valueOf(LicenseValidator.warningNeeded(licenseKey)) );
					
				}catch(Exception e){
					logger.info("License Key after Validation failed:::"+licenseKey, e);
					throw new BPMAccountStatusException(I18nUtil.getMessageProperty("license.expired")); 
				}
			}
			
		}catch (BadCredentialsException e) {
			//e.printStackTrace();
			throw new BPMAccountStatusException(I18nUtil.getMessageProperty("license.expired")); 
		} catch (Exception e) {
			throw new BPMAccountStatusException(e.getMessage()); 
		}
	}
	
	public boolean licenseCheck(String licenseKey) throws Exception{
		return LicenseValidator.checkLicenseHandlerForValidity(licenseKey);
	 }


	public boolean supports(Class<?> authentication) {
	    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	/**
	 * Reset Captcha fields
	 */
	public void resetCaptchaFields() {
	    captchaCaptureFilter.setCaptcha_response(null);
	}

	
	 public CaptchaCaptureFilter getCaptchaCaptureFilter() {
		  return captchaCaptureFilter;
		 }
		 
		 public void setCaptchaCaptureFilter(CaptchaCaptureFilter captchaCaptureFilter) {
		  this.captchaCaptureFilter = captchaCaptureFilter;
		 }
}