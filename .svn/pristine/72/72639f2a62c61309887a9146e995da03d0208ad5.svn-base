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
	  
		 var tabId = $("#first").val();
		 if(tabId=="1"){
			 var twoLi=document.getElementById("two").value;
			//alert(twoLi);
			 $("#two").click(); 
		 }

});
   
$("#saveButton").click(function(event) { 
	var concernSex=$("#concernSex option:selected").val();
	if(concernSex=="200"){
		openMessageBox(form.title.error,"请选择性别","error", true);
		event.preventDefault();
	}else{
		var sfzCode = document.getElementById('sfzCode').value;
			var sfz = document.getElementById('sfzCode').value;
		    if(!(/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/.test(sfz))){
		    	openMessageBox(form.title.error,"身份证号格式错误","error", true);
				event.preventDefault();
		    }else{
		    	var phone = document.getElementById('dh').value;
		    	if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){ 
		    		openMessageBox(form.title.error,"手机号错误","error", true);
					event.preventDefault();
			    }
		    }
	}
	
});

	

/* function getEventForm(){
	//alert($("#id").val());
	var params = "fileId="+$("#id").val();
	document.location.href="#bpm/admin/newFile";
	_execute('target', params);
} */

function getEventForm() {

    var id = $("#id").val();
   // var state = 'SHZT3';
	params = 'id='+encodeURI(id);
	$("#evidence").dialog("destroy");
	document.location.href = "#bpm/admin/newFile";
		_execute('evidence', params);
		$myDialog = $("#evidence");
		$myDialog.dialog({
			 width: '600px',
         height:'auto',
         top: '1000px',
         modal: true,
         resizable:false,
         position: [($(window).width()-460)/2, ($(window).height()-300)/2],
			 title : '上传证据'
		});
		$myDialog.dialog("open");

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
    	var rowData = $("#EVIDENCE_LIST").jqGrid('getRowData',rowid[i]);
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


function backList3(){
	document.location.href = "#bpm/admin/evidenceList";
    _execute('target','');
} 




</script>
<%@ include file="/common/messages.jsp"%>

<div class="page-header">
<h2><fmt:message key="证据采集管理"/></h2>
<div align="right">
		<a id="backToPreviousPage" style="padding:3px;" href="javascript:void(0);" data-role="button" data-theme="b" onClick="backList3()" data-icon=""><button class="btn"><fmt:message key="button.back" /></button></a>
	</div>
</div>
<div class="row-fluid">
	<div class="span12">
	<div style="padding-top:10px;">
		<form:form id="evidenceList" method="post"
					class="form-horizontal no-margin" action="bpm/admin/saveEvidence"
					autocomplete="off" modelAttribute="evidence"
					onSubmit="_execute('target','')">
					<form:hidden path="id" id="id" name="id"/>

			<div class="widget" >
				<div class="widget-body">
					<div id="wizard">
						<ol>
							<li id="one" value="11"><fmt:message key="基本信息" /></li>
							<li id="two" value="222"><fmt:message key="证据列表" /></li>
						</ol>
						
						<div class="well">
						<div  id="personal">

					<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" id="p1">	

				
				<tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
		
					
					
		        <tr>
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >事件名称</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="eventName" id="eventName" class="span6" required="required"  style="width:230px;"/>
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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >当事人姓名</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="concernName" id="concernName" class="span6" required="required"  style="width:230px;"/>
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
					<td  style="width:">
					 <label style= "text-align:right;line-height:22px; " >性别</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="concernSex" name="concernSex" 
								id="concernSex" class="span6"   required="required" style="width:230px;">
								<option value="200"
									${fn:contains(evidence.concernSex, '--------请选择--------') ?'selected' : ''}>
									<fmt:message key="--------请选择--------" />
								</option>
								<option value="男"
									${fn:contains(evidence.concernSex, '男') ?'selected' : ''}>
									<fmt:message key="男" />
								</option>
								<option value="女"
									${fn:contains(evidence.concernSex,
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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >当事人身份证号</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="sfzCode" id="sfzCode" class="span6"  required="required"  style="width:230px;"/>
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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >处理人账号</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="handlerId" id="handlerId" class="span6" required="required"  style="width:230px;"/>
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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >处理人账号</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="handlerName" id="handlerName" class="span6"  required="required" style="width:230px;"/>
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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >处理人联系方式</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="handlerPhone" id="handlerPhone" class="span6" required="required"  style="width:230px;"/>
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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >内容</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:textarea path="content" id="content" class="span6"  style="width:230px;"/>

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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >创建时间</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="createDate" id="createDate" class="span6"  required="required"   readonly="true" style="width:230px;"/>
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
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >修改时间</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="modifyDate" id="modifyDate" class="span6" required="required" readonly="true" style="width:230px;"/>

					</td>
			</tr>
			
			
				</table>
					
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
								name="next" onclick="backList3()" id="cancelButton"><i class="icon-remove icon-white"></i>
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