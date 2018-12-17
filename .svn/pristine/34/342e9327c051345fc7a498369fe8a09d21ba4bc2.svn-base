package com.eazytec.bpm.admin.Zcfg;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eazytec.crm.model.Record;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.Zcfg;
import com.eazytec.util.PageBean;

public interface ZcfgService {
	
	public List<Zcfg> getZcfgListByUserid();
	
	public Zcfg  mysaveOrUpdateZcfgForm(Zcfg zcfgForm, List<MultipartFile> files,List<MultipartFile> myfiles, String userName,String path, List<String[]> deleteArray);
	
	public void removeZcfg(List<String> jyxIds);
	
	public Zcfg saveZcfg(Zcfg zcfg) throws Exception;
	
	public Zcfg getZcfgById(String id);
	
	PageBean<Zcfg> getRecords(PageBean<Zcfg> pageBean,String searchtext);
	
	public List<Zcfg> getZcfgListByserach(String name);

}
