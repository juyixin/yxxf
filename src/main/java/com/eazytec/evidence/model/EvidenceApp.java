package com.eazytec.evidence.model;

public class EvidenceApp {
	//事件名称
	private String name;
    //当事人名称
	private String partyname;
	//当事人身份证号
	private String partycert;
	//联系电话
	private String partyphone;
	//当事人性别
	private String partySex;
	//照片信息，多个以;分割
	private String photos;
	//语音信息，多个以;分割
	private String voices;
	//视频信息，多个以;分割
	private String videos;
	//文字内容
	private String content;
	//上传时间
	private String time;
	//处理人姓名
	private String handlerName;
	//处理人ID
	private String handlerId;
	//处理人联系方式
	private String handlerPhone;
	
	
	
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}
	public String getHandlerPhone() {
		return handlerPhone;
	}
	public void setHandlerPhone(String handlerPhone) {
		this.handlerPhone = handlerPhone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPartyname() {
		return partyname;
	}
	public void setPartyname(String partyname) {
		this.partyname = partyname;
	}
	public String getPartycert() {
		return partycert;
	}
	public void setPartycert(String partycert) {
		this.partycert = partycert;
	}
	public String getPartyphone() {
		return partyphone;
	}
	public void setPartyphone(String partyphone) {
		this.partyphone = partyphone;
	}
	public String getPartySex() {
		return partySex;
	}
	public void setPartySex(String partySex) {
		this.partySex = partySex;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getVoices() {
		return voices;
	}
	public void setVoices(String voices) {
		this.voices = voices;
	}
	public String getVideos() {
		return videos;
	}
	public void setVideos(String videos) {
		this.videos = videos;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
