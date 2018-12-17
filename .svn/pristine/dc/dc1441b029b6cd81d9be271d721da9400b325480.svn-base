<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
<title><fmt:message key="newcompanylist.title" /></title>
<meta name="heading" content="<fmt:message key='信访人信息详情页'/>" />
<style type="text/css">
.jstree-default a .jstree-icon {
	background-position: display:none;
}
</style>
</head>
<c:set var="delObject" scope="request">
	<fmt:message key="companyinformationlist.list" />
</c:set>
<script type="text/javascript">


$("#wizard").bwizard();	
var newHeight = $("#target").height();

var wizardHeight = parseInt(newHeight) - 190;

$('#wizard').css({height: wizardHeight});
$(".bwizard-buttons").css({display: 'none'});
var personalHeight = parseInt(newHeight) - 257;

$('#personal').css({height: personalHeight,overflow: 'auto'});

var jobHeight = parseInt(newHeight) - 257;
$('#job').css({height: jobHeight,overflow:'auto'});


$(document).ready(function(){
	   changeWizardByTab();
	   $( '#csrq' ).datepicker({
			dateFormat: 'yy-mm-dd',
			changeYear: true,
			changeMonth: true,
			yearRange: "-120:+0",
			showOn: "button",
			buttonImage:'/images/easybpm/form/rbl/_cal.gif',
			maxDate: new Date(),
		});
	   
		 var tabId = $("#first").val();
		 if(tabId=="1"){
			 var twoLi=document.getElementById("two").value;
			//alert(twoLi);
			 $("#two").click(); 
		 }

});
   
$("#saveButton").click(function(event) { 
	
	
	var uploadedFiles = document.getElementById('file');
	if(uploadedFiles.files.length>0){
		var fileValue = "1";
	    document.getElementById("modifyFileImg").value=fileValue;
		var regex= /^.*\.(jpg|png|gif)$/i;
		for (var i = 0; i < uploadedFiles.files.length; ++i) {
		  var uploadedFileName = uploadedFiles.files.item(i).name;
			if(regex.test(uploadedFileName) != true) {
				openMessageBox(form.title.error,"图片格式不正确","error", true);
				event.preventDefault();
			}
		}	
	   }else{
		   var fileValue = "2";
		   var fileUrl = $("#imgUrl").val();
		   document.getElementById("modifyFileImg").value=fileValue;
		   document.getElementById("modifyFileUrl").value=fileUrl;
	   }
	
	    var sfzTrue = true;
	    var sfz = document.getElementById('sfzhm').value;
	    if(!(/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/.test(sfz))){
	    	sfzTrue = false;
	    }
	   
		var status = true;
		var phone = document.getElementById('dh').value;
		if(phone!=null&&phone!=""&&phone!=undefined){	
		if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){ 
			status = false;
	    }
		if(status==false){
			status = true;
		if(!(/^0\d{2,3}-?\d{7,8}$/.test(phone))){
	    	status = false;
	    }
	    	
	    }
	}else{
		status = false;
	}   
		var xb=$("#xb option:selected").val();
		if(xb=="200"){
			openMessageBox(form.title.error,"请选择性别","error", true);
			event.preventDefault();
		}
		if(status == false){	
		openMessageBox(form.title.error,"电话格式不正确","error", true);
		event.preventDefault();
		}
		 if(sfzTrue == false){	
				openMessageBox(form.title.error,"身份证格式不正确","error", true);
				event.preventDefault();
			}
});

	
function backToList(){
	document.location.href="#bpm/admin/petitionLetterPersonList";
	_execute('target', '');
}

function getEventForm(){
	//alert($("#id").val());
	var params = "personId="+$("#id").val();
	document.location.href="#bpm/admin/newEvent";
	_execute('target', params);
}

//删除
function deleteEventInfo(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var defpIds = new Array();
	rowid.forEach(function(item) {
		defpIds[defpIds.length] = item;
	});
    var id = new Array();
    for(var i=0;i<defpIds.length;i++){
    	var rowData = $("#_EVENT_LIST").jqGrid('getRowData',rowid[i]);
    	var j = rowData["id"];
    	id.push(j);
    }
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		deleteEvent(id);		
	}
}

function deleteEvent(id){

	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	    		jQuery.ajax({
	    	    			url:'bpm/admin/deleteEventList',
	    	    			type:"get",
	    	    			data: {id:JSON.stringify(id)},
	    	    			dataType:"json",
	    	    		    success:function(data){
	    	    				if(data.code == "1"){
	    	    					backList(data.first,data.personId);
	    	    				} else {
	    	    					alert(data.msg);
	    	    				}
	    	    			},
	    	    			error : function(ex) { alert(ex.status); }
	    	    			});	
	    	        }
	    	    }
	});
}


function backList(first,personId){
	var params = "first="+first+"&personId="+personId;
	document.location.href="#bpm/admin/modifyPerson";
	_execute('target', params);
}


/**
 * Description:控制内容显示长度
 * 作者 : 蒋晨 
 * 时间 : 2017-2-25 下午2:39:04
 */
function showLongDetail(rowId, tv, rawObject, cm, rdata) {
	var eventDetail = rawObject.eventDetail;
	if(eventDetail.length>0){	
		if(eventDetail.length>10){		
		var s = eventDetail.substring(0,10) + "...";
		return '<a id="changeDetail"  href="javascript:void(0);" title='+ eventDetail + ' data-role="button" data-theme="b"><b>'+ s + '</b></a>';
		}else{
			return '<a id="changeDetail"  href="javascript:void(0);" title='+ eventDetail + ' data-role="button" data-theme="b"><b>'+ eventDetail + '</b></a>';
		}
	}else{
		return '<a id="changeDetail" href="javascript:void(0);" data-role="button" data-theme="b"><b>'+ rawObject.eventDetail + '</b></a>';
	}

}


function onSearch(){
	var searchText=$("#searchInfo").val();
	var params = "searchText="+encodeURI(encodeURI(searchText))+"&personId="+$("#id").val()+"&first="+"1";
	document.location.href="#bpm/admin/modifyPerson";
	_execute('target', params);
}

</script>
<%@ include file="/common/messages.jsp"%>
<input type="hidden" id="oldSfzCode" name="oldSfzCode" value="${oldSfzCode}"/>
<input type="hidden" id = "imgUrl" name="imgUrl" value="${url}"/>
<input type="hidden" id="first" name="first" value="${first}"/>
<div class="page-header">
<h2><fmt:message key="信访人信息"/></h2>
<div align="right">
		<a id="backToPreviousPage" style="padding:3px;" href="javascript:void(0);" data-role="button" data-theme="b" onClick="backToList()" data-icon=""><button class="btn"><fmt:message key="button.back" /></button></a>
	</div>
</div>
<div class="row-fluid">
	<div class="span12">
	<div class="box_main" style="padding-top:10px;">
		<form:form id="personList" method="post"
					class="form-horizontal no-margin" action="bpm/Link/savePersonList?oldSfzCode=${oldSfzCode}"
					autocomplete="off" modelAttribute="person"
					onSubmit="_execute('target','')">
					<form:hidden path="id" id="id" name="id"/>

			<div class="widget" >
				<div class="widget-body">
					<div id="wizard">
						<ol>
							<li id="one" value="11"><fmt:message key="信访人信息" /></li>
							<li id="two" value="222"><fmt:message key="事件列表" /></li>
						</ol>
						
						<div class="well">
						<div  id="personal">
							<div class="span3" style="width:20%">
										<div class="thumbnail" style="width:200px;height:200px;">
										<img id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="${person.photo}" /> </br>
										</div>
							</div>
							<div class="span9" style="width:78%">
					<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" id="p1">	
                		<form:input type="hidden" path="modifyFileImg"  id="modifyFileImg" name="modifyFileImg"/>
				       <form:input type="hidden" path="modifyFileUrl"  id="modifyFileUrl" name="modifyFileUrl"/>
				       <form:input type="hidden" path="isActive" id="isActive"  name="isActive"/>
				
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
		
					
					
		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >姓名</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="xm" id="xm" class="span6" required="required"  style="width:230px;"/>
							<em style="color:red;margin-left:5px">*</em>
					</td>
			</tr>
			
			
			
			<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>	
				
				
				
			<tr>
					<td  style="width:300px">
					 <label style= "text-align:right;line-height:22px; " >性别</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="xb" name="xb" 
								id="xb" class="span6"   required="required" style="width:230px;">
								<option value="200"
									${fn:contains(person.xb, '--------请选择--------') ?'selected' : ''}>
									<fmt:message key="--------请选择--------" />
								</option>
								<option value="男"
									${fn:contains(person.xb, '男') ?'selected' : ''}>
									<fmt:message key="男" />
								</option>
								<option value="女"
									${fn:contains(person.xb,
									'女') ? 'selected' : ''}>
									<fmt:message key="女" />
								</option>
							</form:select>
							<em style="color:red;margin-left:5px">*</em>
							
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				
				
				
			
			<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
		
				
				  <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >个人照片</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="file" name="file" id="file" size="20" accept="image/*"  style="width:200px"/>
						
						
					</td>
			    </tr>
			 		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" ></label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<em style="color:red;margin-left:5px;font-size:18px">(支持jpg/png/gif格式上传)</em>

					</td>
			</tr>
				
			
			<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
			
				
					
		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >出生日期</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="csrq" id="csrq" class="span6"  style="width:230px;"/>
					</td>
			</tr>
			
			
			
			
			<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
		
				
				
					
		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >电话</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="dh" id="dh" class="span6" required="required"  style="width:230px;"/>
							<em style="color:red;margin-left:5px">*</em>

					</td>
			</tr>
			
		
			<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
		
				
				
					
		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >身份证号码</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="sfzhm" id="sfzhm" class="span6"  required="required" style="width:230px;"/>
						<em style="color:red;margin-left:5px">*</em>
					</td>
			</tr>
			
			
			<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
			
				
					
		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >地址</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:textarea path="address" id="address" class="span6" required="required"  style="width:230px;"/>
							<em style="color:red;margin-left:5px">*</em>

					</td>
			</tr>
			    
			    
			    <tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
			
				
				<c:if test="${not empty person.id}">
					
		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >创建时间</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="createTime" id="createTime" class="span6"  required="required"   readonly="true" style="width:230px;"/>
					</td>
			</tr>
			
			
			
					</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				<tr>
				
					
		        <tr>
					<td style="width:300px">
					<label style= "text-align:right;line-height:22px;" >修改时间</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="lastModifyTime" id="lastModifyTime" class="span6" required="required" readonly="true" style="width:230px;"/>

					</td>
			</tr>
			</c:if>
			
				</table>
					</div>
							</div>
						</div>
					
						<div class="well" >
	
							<div class="form-horizontal no-margin" id="job">
		 					<a style="padding: 3px; ">
		                    <input id="searchInfo" type="text" placeholder="输入事件名称" style="width:180px"></input>
		                    </a>	
	              	        <input type="button" onclick="onSearch()" value="查询"/>
							<a class="padding3" id="createUser" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="getEventForm()"  data-icon=""><input type="button" value="新建"/></a>
                            <a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteEventInfo()"  data-icon=""><input type="button" value="删除"/></a>    
                            <%= request.getAttribute("script") %> 
							</div>
					
						</div>
		
				</div>
					<div class="control-group" align="center">
						<div class="form-actions no-margin">	
							<span id="saveButtonDiv" align="center">
								<button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
										<fmt:message key="button.save" />
							</button>
							&nbsp;
							</span>
							<span id="cancelButtonDiv" class="">
								<button type="button" class="btn btn-primary cursor_pointer"
								name="next" onclick="backToList()" id="cancelButton"><i class="icon-remove icon-white"></i>
								<fmt:message key="button.cancel" />
							</button>
							</span>
							<div class="clearfix"></div>
						</div>
					</div>	
				</div>
			</div>
		</form:form>
	</div>
</div>
</div>

<div id="popupcontent">
</div>

<v:javascript formName="person" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>