package com.eazytec.webapp.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;
 
/**
 * Filter for capturing Captcha fields.
 * It's purpose is to store these values internally
 */
public class CaptchaCaptureFilter extends OncePerRequestFilter {
  
 protected Logger logger = Logger.getLogger("filter");
 private String captcha_response;
 private String captcha_challenge;
 private String remoteAddr;
 private HttpServletRequest request;
 
 @Override
 public void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
   FilterChain chain) throws IOException, ServletException {
 
  logger.debug("Captcha capture filter");
   
  // Assign values only when user has submitted a Captcha value.
  // Without this condition the values will be reset due to redirection
  // and CaptchaVerifierFilter will enter an infinite loop
		if (req.getParameter("jcaptcha") != null) {
			request = req;
			captcha_response = req.getParameter("jcaptcha");
			String sessId =(String) request.getSession(true).getAttribute("captchaId");
			captcha_challenge = sessId;
			remoteAddr = req.getRemoteAddr();
		}
   
  logger.debug("challenge: " + captcha_challenge);
  logger.debug("response: " + captcha_response);
  logger.debug("remoteAddr: " + remoteAddr);
   
  // Proceed with the remaining filters
  chain.doFilter(req, res);
 }
 
 public String getCaptcha_response() {
  return captcha_response;
 }
 
 public void setCaptcha_response(String recaptchaResponse) {
  captcha_response = recaptchaResponse;
 }
 
 public String getCaptcha_challenge() {
  return captcha_challenge;
 }
 
 public void setCaptcha_challenge(String recaptchaChallenge) {
  captcha_challenge = recaptchaChallenge;
 }
 
 public HttpServletRequest getRequest() {
     return request;
 }

 public void setRequest(HttpServletRequest request) {
     this.request = request;
 }
 
 public String getRemoteAddr() {
  return remoteAddr;
 }
 
 public void setRemoteAddr(String remoteAddr) {
  this.remoteAddr = remoteAddr;
 }
}