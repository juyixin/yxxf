package com.eazytec.webapp.filter;


   /*
    * JCaptcha, the open source java framework for captcha definition and integration
    * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
    * See the LICENSE.txt file distributed with this package.
    */
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eazytec.util.PropertyReader;
import com.octo.captcha.service.CaptchaServiceException;
  
  public class SimpleImageCaptchaServlet extends HttpServlet
 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sImgType = null;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

		// For this servlet, supported image types are PNG and JPG.
		sImgType = servletConfig.getInitParameter("ImageType");
		sImgType = sImgType == null ? "png" : sImgType.trim().toLowerCase();
		if (!sImgType.equalsIgnoreCase("png")
				&& !sImgType.equalsIgnoreCase("jpg")
				&& !sImgType.equalsIgnoreCase("jpeg")) {
			sImgType = "png";
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		Boolean isCaptchaNeeded = Boolean.valueOf(PropertyReader.getInstance()
		.getPropertyFromFile("Boolean", "system.captcha.needed"));
		
		if(isCaptchaNeeded){
			ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
			byte[] captchaBytes;
			try {
				// Session ID is used to identify the particular captcha.
				String captchaId = request.getSession().getId();
				request.getSession().setAttribute("captchaId", captchaId);
				// Generate the captcha image.
				BufferedImage challengeImage = CaptchaService.getInstance()
						.getImageChallengeForID(captchaId, request.getLocale());
				ImageIO.write(challengeImage, sImgType, imgOutputStream);
				captchaBytes = imgOutputStream.toByteArray();
	
				// Clear any existing flag.
				request.getSession().removeAttribute("PassedCaptcha");
			} catch (CaptchaServiceException cse) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Problem generating captcha image.");
				return;
			} catch (IOException ioe) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Problem generating captcha image.");
				return;
			}
	
			// Set appropriate http headers.
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/"
					+ (sImgType.equalsIgnoreCase("png") ? "png" : "jpeg"));
	
			// Write the image to the client.
			ServletOutputStream outStream = response.getOutputStream();
			outStream.write(captchaBytes);
			outStream.flush();
			outStream.close();
		}
	}

	
	public static boolean validateCaptcha(String captchaId, String inputChars) {
		boolean bValidated = false;
		try {
			bValidated = CaptchaService.getInstance().validateResponseForID(
					captchaId, inputChars);
		} catch (CaptchaServiceException cse) {
		}
		return bValidated;
	}
}