package com.eazytec.external.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.external.util.ExternalUtils;
import com.eazytec.util.DateUtil;

@Controller("extFileUploadController")
public class FileUploadController {
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

	@RequestMapping(value = "fileUpload", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> fileUpload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 从请求头中获取文件名
		String fileName = ExternalUtils.getFileName(request);
		// 获取文件名后缀
		String ext = FilenameUtils.getExtension(fileName);

		try {
			
			//上传文件格式定义，可做成配置形式，方便扩展
			String[] allowSuffix = { "jpg", "txt", "png", "pptx" };

			// 不允许上传的文件格式
			if (!Arrays.asList(allowSuffix).contains(ext)) {
				return ExternalUtils.error(map, ext + "格式的文件不允许上传");
			}
			
			

			// 获得字节数组，用来家计算文件大小
			byte[] data = IOUtils.toByteArray(request.getInputStream());

			// 文件大小限制，可做成配置形式，方便扩展，示例为1M
			if (((int) (data.length / 1024 / 1024)) > 1) {
				return ExternalUtils.error(map, "您上传的文件太大了,不能超过1M");
			}

			
			
			// 文件输出路径，文件加上时间重命名，可根据项目自行修改
			String filePath = "E:/" + addTimeStamp(fileName);
			File file = new File(filePath);
			
			//输出到文件
			FileUtils.copyInputStreamToFile(new ByteArrayInputStream(data), file);
			

			map.put("fileName", fileName);
			map.put("filePath", filePath);
			return ExternalUtils.success(map);

		} catch (Exception e) {
			log.error("文件上传出错!", e);
			return ExternalUtils.error(map, "文件上传出错!");
		}
	}

	private String addTimeStamp(String fileName) {
		String name = FilenameUtils.getBaseName(fileName);// 文件名
		String ext = FilenameUtils.getExtension(fileName); // 后缀

		String timeStamp = DateUtil.convertDateToDefalutDateTimeString(new Date());

		return name.trim() + "_" + timeStamp + '.' + ext;
	}
}
