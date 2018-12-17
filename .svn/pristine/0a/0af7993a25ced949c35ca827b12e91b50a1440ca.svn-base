package com.eazytec.external.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.eazytec.external.TestUtils;

public class FileUploadControllerTest {

	@Test
	public void fileUploadTest() throws Exception {
		String url = "fileUpload";
		File file = new File("D:/ss.jpg");
		InputStream is = new FileInputStream(file);

		TestUtils.getFilePost(url, is, "ss.jpg", "697c58e2");
	}
}