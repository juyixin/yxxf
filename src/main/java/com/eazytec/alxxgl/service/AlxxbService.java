package com.eazytec.alxxgl.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.model.Alxxb;
import com.eazytec.alxxgl.model.AlxxbDocument;
import com.eazytec.crm.model.Record;
import com.eazytec.util.PageBean;

public interface AlxxbService {
	
	Alxxb saveAlxxb(Alxxb alxxb);

	String removeAlxxb(List<String> alxxbIdList);
	
	Alxxb getAlxxbById(String id);

	Alxxb saveOrUpdateAlxxbForm(Alxxb alxxbForm, List<MultipartFile> files, String path);

	List<Alxxb> getAllxByAllx(String allx);
	
	List<Alxxb> getAllxByAllx1(String allx);
	
	List<Alxxb> getAllxByAllx2(String id,String name);
	
	Alxxb getAllxByIds(String id);
	
	PageBean<Alxxb> getRecords(PageBean<Alxxb> pageBean,String name,String allx,String dsr);

	List<Alxxb> getlistsize(String name,String allx,String dsr);

}
