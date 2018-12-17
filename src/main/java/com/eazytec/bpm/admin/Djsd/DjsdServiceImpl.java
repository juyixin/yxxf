package com.eazytec.bpm.admin.Djsd;

import com.eazytec.bpm.admin.NoticeDocument.NoticeDocumentDao;
import com.eazytec.bpm.admin.NoticeUserPlat.NoticeUserPlatDao;
import com.eazytec.bpm.common.util.PathUtils;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.Djsd;
import com.eazytec.model.NoticeUserPlat;
import com.eazytec.model.User;
import com.eazytec.service.impl.GenericManagerImpl;
import com.eazytec.util.PageBean;
import com.eazytec.webapp.socket.ClientSend;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("djsdService")
public class DjsdServiceImpl extends GenericManagerImpl<Djsd,java.lang.String> implements DjsdService{

	private DjsdDao djsdDao;
	
    private NoticeDocumentDao noticedocumentDao;
    
    private NoticeUserPlatDao  noticeuserplatDao;

	@Autowired
	private ClientSend clientSend;

	@Autowired
	public DjsdServiceImpl(DjsdDao djsdDao) {
		super(djsdDao);
		this.djsdDao = djsdDao;
	}

	@Autowired
	public void setDjsdDao(DjsdDao djsdDao) {
		this.dao = djsdDao;
		this.djsdDao = djsdDao;
	}
	
	@Autowired
	public void setNoticeUserPlatDao(NoticeUserPlatDao noticeuserplatDao) {
		this.noticeuserplatDao = noticeuserplatDao;
	}
	
	@Autowired
	public void setNoticeDocumentDao(NoticeDocumentDao noticedocumentDao) {
		this.noticedocumentDao = noticedocumentDao;
	}

	public List<Djsd> getDjsds(Djsd djsd) {
		return dao.getAll();
	}

	public Djsd getDjsdByName(String name) {
		return djsdDao.getDjsdByName(name);
	}

	
	public Djsd saveDjsd(Djsd djsd) throws Exception {

		return djsdDao.saveDjsd(djsd);
		
	}

	public Djsd getDjsdById(String id) {
        return djsdDao.getDjsdById(id);
    }
	
	
	@Override
	public List<Djsd> getNoticeListByUserid() {
		// TODO Auto-generated method stub
		 return djsdDao.getNoticeListByUserid();
	}
	
	@Override
	public List<Djsd> getNoticeListByUserid1(String isActive) {
		// TODO Auto-generated method stub
		 return djsdDao.getNoticeListByUserid1(isActive);
	}
	
	/**
	 * 前台登录时获取公告列表前5
	 * @param userid
	 * @return
	 */
	@Override
	public List<NoticeUserPlat> showNoticeListByUserid(String userid) {
		// TODO Auto-generated method stub
		List<NoticeUserPlat> noticeUserList= djsdDao.showNoticeListByUserid(userid);
		List<NoticeUserPlat> newnoticeUserList=new ArrayList<NoticeUserPlat>();
		if(noticeUserList!=null){
			for (int i=0;i<noticeUserList.size();i++){
				NoticeUserPlat newUserPlat=new NoticeUserPlat();
				try {
					BeanUtils.copyProperties(newUserPlat, noticeUserList.get(i));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(newUserPlat.getTitle().length()>9){
					String title=newUserPlat.getTitle().substring(0, 9)+"...";
					newUserPlat.setTitle(title);
					newnoticeUserList.add(newUserPlat);
				}else{
					newnoticeUserList.add(newUserPlat);
				}
			}
		}
		return newnoticeUserList;
	}
	

	@Override
	public Djsd saveOrUpdateDocumentForm(Djsd noticeForm, List<MultipartFile> files, String userId,
			String path,List<String[]> deleteArray) {
		// TODO Auto-generated method stub
		Date date = new Date();
		Calendar calendar=Calendar.getInstance();
		List<NoticeDocument> noticeDocuments=new ArrayList<NoticeDocument>();
		String fileName = "";
		try {
			if(StringUtil.isEmptyString(noticeForm.getId())) {
				noticeForm.setId(null);
			}
				noticeForm.setCreateperson(userId);
				noticeForm.setCreatetime(date);
				noticeForm.setDataYear((long) calendar.get(Calendar.YEAR));
				if(!deleteArray.isEmpty()){
					for(int i=0;i<deleteArray.size();i++){
						noticedocumentDao.deleteDocumentsById(deleteArray.get(i)[0]);
					}
					
				}
				noticeForm =  djsdDao.save(noticeForm);
				//解析路劲保存上传文件
			String parentDir = path;
			if(files != null  && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						NoticeDocument document = new NoticeDocument();
						 fileName = multipartFile.getOriginalFilename();
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
						String parentPath = parentDir+fileName;
						//String name = PathUtils.escape(fileName);
						document.setId(null);
						document.setCreatedBy(noticeForm.getCreateperson());
						document.setPath(parentPath);
						document.setName(multipartFile.getOriginalFilename().replace(' ','_'));
						document.setCreatedTime(date);
						document.setMimeType(multipartFile.getContentType());
						if(noticeForm.getId()!=null){
							document.setParentid(noticeForm.getId());
						}
						noticedocumentDao.save(document);
					}
				} 
			}
			
			List<User> userList=new ArrayList<User>();

			userList=djsdDao.getUserList();
			if(userList!=null){
				noticeuserplatDao.deleteNoticeUserPlatById(noticeForm.getId());
				for (User user : userList){
					NoticeUserPlat noticeUser=new NoticeUserPlat();
					noticeUser.setId(null);
					noticeUser.setIsRead("N");
					if(noticeForm.getId()!=null){
						noticeUser.setParentid(noticeForm.getId());
						noticeUser.setTitle(noticeForm.getTitle());
						noticeUser.setCreatetime(noticeForm.getCreatetime());
					}else{
						noticeUser.setParentid("");
						noticeUser.setTitle("");
						noticeUser.setCreatetime(noticeForm.getCreatetime());
					}
					noticeUser.setUserid(user.getId());
					noticeuserplatDao.save(noticeUser);
				}
			}
			
			

			 
			String uploadDir = path;
	         // Create the directory if it doesn't exist
	         File dirPath = new File(uploadDir);
	         if (!dirPath.exists()) {
	             dirPath.mkdirs();
	         }
	         
			if(null != files && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){


						// retrieve the file data
		                InputStream stream = multipartFile.getInputStream();
		                // write the file to the file specified
		                OutputStream bos = new FileOutputStream(uploadDir + fileName);
		                int bytesRead;
		                byte[] buffer = new byte[8192];
		                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		                    bos.write(buffer, 0, bytesRead);
		                }
		                bos.close();
		                // close the stream
		                stream.close();



						clientSend.sendFile(new File(uploadDir + fileName));
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new BpmException("problem in saving document"+e);
		}
		return noticeForm;
	}

	@Override
	public Djsd mysaveOrUpdateDocumentForm(Djsd noticeForm, List<MultipartFile> files,List<MultipartFile> myfiles, String userId,
											   String path,List<String[]> deleteArray) {
		// TODO Auto-generated method stub
		Date date = new Date();
		Calendar calendar=Calendar.getInstance();
		List<NoticeDocument> noticeDocuments=new ArrayList<NoticeDocument>();
		String fileName  = "";
		try {
			if(StringUtil.isEmptyString(noticeForm.getId())) {
				noticeForm.setId(null);
			}
			noticeForm.setCreateperson(userId);
			noticeForm.setCreatetime(date);
			noticeForm.setDataYear((long) calendar.get(Calendar.YEAR));
			if(!deleteArray.isEmpty()){
				for(int i=0;i<deleteArray.size();i++){
					noticedocumentDao.deleteDocumentsById(deleteArray.get(i)[0]);
				}

			}
			noticeForm =  djsdDao.saveDjsd(noticeForm);

			String parentDir = path;

			//保存上传文件进入数据库
			if(files != null  && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						NoticeDocument document = new NoticeDocument();
						 fileName = multipartFile.getOriginalFilename();
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
						String parentPath = parentDir+fileName;
						//String name = PathUtils.escape(fileName);
						document.setId(null);
						document.setCreatedBy(noticeForm.getCreateperson());
						document.setPath(parentPath);
						document.setName(multipartFile.getOriginalFilename().replace(' ','_'));
						document.setCreatedTime(date);
						document.setMimeType(multipartFile.getContentType());
						if(noticeForm.getId()!=null){
							document.setParentid(noticeForm.getId());
						}
						noticedocumentDao.saveNoticeDocument(document);
					}
				}
			}
			List<User> userList=new ArrayList<User>();
			String uploadDir = path;
			// Create the directory if it doesn't exist
			File dirPath = new File(uploadDir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}

			/*//我自己的上传文件到服务器
			if(null != myfiles && !myfiles.isEmpty()){
				List<MbInfo>  list = new ArrayList<MbInfo>();
				for (MultipartFile multipartFile : myfiles) {

						if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
							InputStream stream = multipartFile.getInputStream();
							OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename().replace(' ','_'));
							int bytesRead;
							byte[] buffer = new byte[8192];
							while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
								bos.write(buffer, 0, bytesRead);
								}
							bos.close();
							stream.close();
							clientSend.sendFile(new File(uploadDir + multipartFile.getOriginalFilename().replace(' ','_')));

							//解析excel上传的excel文件获得excel内容
							//获取上传文件名称
							String fileName = multipartFile.getOriginalFilename();
							fileName = fileName.replace(' ', '_');
							//获取上传地址路劲
							String mypath = uploadDir;
							//新建一个上传文件
							File myfile = new File(mypath + fileName);

							String fName = myfile.getName();
							String extension = fName.lastIndexOf(".") == -1 ? "" : fName
									.substring(fName.lastIndexOf(".") + 1);
							if (extension.equals("xls")) {
								HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(myfile));//创建一个excel
								HSSFSheet sheetDept = hwb.getSheetAt(0);//创建一个sheet
								for(int i=0;i<sheetDept.getLastRowNum();i++){
									MbInfo m = new MbInfo();
									m.setName(sheetDept.getRow(i).getCell(0).getStringCellValue());
									m.setCode(sheetDept.getRow(i).getCell(1).getStringCellValue());
									list.add(m);

								}
							}else if (extension.equals("xlsx")) {
								XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(myfile));
								XSSFSheet sheetDept = xwb.getSheetAt(0);
								for(int i=0;i<sheetDept.getLastRowNum()+1;i++){
									MbInfo mb = new MbInfo();
									mb.setName(sheetDept.getRow(i).getCell(0).getStringCellValue());
									mb.setCode(sheetDept.getRow(i).getCell(1).getStringCellValue());
									list.add(mb);
								}
							}else{
								return null;
							}
							int num=0;
							if(!list.isEmpty()){

								List<List<StringBuffer>>  mylist = new ArrayList<List<StringBuffer>>();
									double a=list.size();
									double b=500;
									num= (int) Math.ceil(a/b);//读取次数（一共几页），小数 进 1
								for (int i = 0; i <num; i++) {
									List<StringBuffer> list2 = new ArrayList<>();

									int n =i*500;//每次开始读取的下标
									if(i+1<num){  //i+1 代表
										StringBuffer contextIds = new StringBuffer();
										List<MbInfo> list3 = list.subList(n, n+500);
										contextIds.append("(");
										for(int j = 0;j<list3.size()-1;j++){
											contextIds.append("'"
												+ list3.get(j).getCode() + "'"
												+ ",");
										}
										contextIds.append("'"
											+  list3.get(list3.size() - 1).getCode()
											+ "'"); // 拼装一个字符串是存放主键的，用逗号隔开
										contextIds.append(")");
										list2.add(contextIds);
								}else if(i+1==num){//当位于最后一页时，计算要读多少个数（ aa.size()-i*500）
										StringBuffer contextIds = new StringBuffer();
										List<MbInfo> list3 = list.subList(n, n+list.size()-i*500);
										if (list3.size() >= 2) {
										contextIds.append("(");
										for (int j = 0; j < list3.size() - 1; j++) {
											contextIds.append("'"
												+ list3.get(j).getCode() + "'"
												+ ",");
											}
											contextIds.append("'"
												+  list3.get(list3.size() - 1).getCode()
												+ "'"); // 拼装一个字符串是存放主键的，用逗号隔开
											contextIds.append(")");
										} else {
											contextIds.append("(");
											contextIds.append("'"
												+list3.get(0).getCode() + "'");
											contextIds.append(")");
										}
										list2.add(contextIds);
									}
									mylist.add(list2);
								}
								userList= djsdDao.getallUserList(mylist);
							}
						}
				}
			}
			//没有上传文件 名单
			else{
				//如果是企业端，那就给全部企业发送
				if(noticeForm.getToperson().equals("企业端")){
					userList = djsdDao.getallQyList();
				}
				//如果是平台端那就给平台端所有人员发送
				else{
					userList=djsdDao.getUserList();
				}
			}
//发送公告
			if(userList!=null){
				noticeuserplatDao.deleteNoticeUserPlatById(noticeForm.getId());
				for (User user : userList){
					NoticeUserPlat noticeUser=new NoticeUserPlat();
					noticeUser.setId(null);
					noticeUser.setIsRead("N");
					if(noticeForm.getId()!=null){
						noticeUser.setParentid(noticeForm.getId());
						noticeUser.setTitle(noticeForm.getTitle());
						noticeUser.setCreatetime(noticeForm.getCreatetime());
					}else{
						noticeUser.setParentid("");
						noticeUser.setTitle("");
						noticeUser.setCreatetime(noticeForm.getCreatetime());
					}
					noticeUser.setUserid(user.getId());
					noticeuserplatDao.save(noticeUser);
				}
			}*/

			//上传文件到服务器
			if(null != files && !files.isEmpty()){
				for (MultipartFile multipartFile : files) {
					if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()){
						// retrieve the file data
						InputStream stream = multipartFile.getInputStream();
						// write the file to the file specified
						OutputStream bos = new FileOutputStream(uploadDir + fileName);
						int bytesRead;
						byte[] buffer = new byte[8192];
						while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
							bos.write(buffer, 0, bytesRead);
						}
						bos.close();
						// close the stream
						stream.close();
						clientSend.sendFile(new File(uploadDir + fileName));
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			System.out.println(e.toString());
			throw new BpmException("problem in saving document"+e);
		}
		return noticeForm;
	}
	@Override
	public List<Djsd> getReadNoticeListByUserid(String userid) {
		// TODO Auto-generated method stub
		 return djsdDao.getReadNoticeListByUserid(userid);
	}

	@Override
	public void deleteAllNoticeByIds(List<String> noticeIdList) {
		// TODO Auto-generated method stub
		StringBuffer ids = new StringBuffer();
		ids.append("(");
		for (String noticeId : noticeIdList) {
			ids.append("'" + noticeId + "',");
		}

		if (ids.lastIndexOf(",") > 0) {
			ids.deleteCharAt(ids.lastIndexOf(","));
		}
		ids.append(")");
		djsdDao.deleteAllNoticeByIds(ids.toString());
	}

	//删除
			@Override
			public void removeNotice(List<String> jyxIds) {
				// TODO Auto-generated method stub
				for(int i=1;i<jyxIds.size();i++){
					djsdDao.removeNotice(jyxIds.get(i));
				}

			}

			@Override
			public Djsd getDjsdByIdS(String id) {
				return djsdDao.get(id);
			}

			@Override
			public PageBean<Djsd> getDjsd(PageBean<Djsd> pageBean) {
				int totalrecords =  djsdDao.getAllDjsdCount(pageBean);
				pageBean.setTotalrecords(totalrecords);
				List<Djsd> result =  djsdDao.getAllDjsd(pageBean);
				pageBean.setResult(result);
				return pageBean;
			}

			@Override
			public Djsd getDjsdByIdsffb(String id) {
				  return djsdDao.getDjsdByIdsffb(id);
			}

			@Override
			public List<Djsd> showNoticeListBySffb() {
				// TODO Auto-generated method stub
				 return djsdDao.showNoticeListBySffb();
			}


}
