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

/* $(document).ready(function(){
	var read='${readOnly}';
	if(read == "readOnly"){
		
		$("*").attr("readOnly","readOnly"); 
		$("#btnBack").css("display","none");
		$("#btndiv1").css("display","none"); 
		$("#date1").show();
		$("#date2").show();
	}
});
*/

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

});
   
$("#saveButton").click(function(event) { 
	
	var xb=$("#xb option:selected").val();
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

</script>
<input type="hidden" id = "imgUrl" name="imgUrl" value="${url}"/>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png" style="float:left; margin:9px 6px 5px 5px" /><h2 style="font-size:16px; margin:2px;/* padding-left: 380px; */">信访人信息详情页</h2>
	</div>
	<div class="height10"></div>

<%@ include file="/common/messages.jsp"%>

	<div align="right" id="btnBack">
		<a class="padding10" style="text-decoration: underline;"
			id="backToPreviousPage" href="javascript:void(0);" data-role="button"
			data-theme="b" onClick="backToList();" data-icon=""><button
				class="btn">
				<strong><fmt:message key="button.back" /></strong>
			</button></a>
	</div>
</div>
<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main" style="padding-top: 10px">
				<form:form id="personList" method="post"
					class="form-horizontal no-margin" action="bpm/Link/savePersonList"
					autocomplete="off" modelAttribute="person"
					onSubmit="_execute('target','')">
					<form:hidden path="id" />
					
					<div class="span3" style="width:20%">
					                    <c:if test="${empty person.photo}">
										<div class="thumbnail" id="photoId" style="width:200px;height:200px;display:none">
										<img id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="${person.photo}"/> </br>
										</div>
										</c:if>
										<c:if test="${not empty person.photo}">
										<div class="thumbnail" id="photoId" style="width:200px;height:200px">
										<img id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="${person.photo}"/> </br>
										</div>
										</c:if>
							</div>
							<div class="span9" style="width:78%">

               <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" id="p1">	
            <form:input type="hidden" path="modifyFileImg"  id="modifyFileImg" name="modifyFileImg"/>
			 <form:input type="hidden" path="modifyFileUrl"  id="modifyFileUrl" name="modifyFileUrl"/>
				
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
				</tr><tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>			
					
					
		        <tr>
					<td style="width:260px">
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
					<td  style="width:260px">
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
					<td style="width:260px">
					<label style= "text-align:right;line-height:22px;" >个人照片</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="file" name="file" id="file" size="20" accept="image/*" style="width:200px"/>
						
						
					</td>
			    </tr>
			 		        <tr>
					<td style="width:260px">
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
					<td style="width:260px">
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
					<td style="width:260px">
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
					<td style="width:260px">
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
					<td style="width:260px">
					<label style= "text-align:right;line-height:22px;" >地址</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:textarea path="address" id="address" class="span6" required="required"  style="width:230px;"/>
							<em style="color:red;margin-left:5px">*</em>

					</td>
			</tr>
			    
			    
				</table>

			</div>
            <table  width="99%" height ="110px" border="0" align="center" cellpadding="0" cellspacing="0" id="p2" >		
			            <div class="control-group"    align="center">
			            </div>
			          	<div class="control-group"    align="center">
			            </div>
			       	<div class="control-group"    align="center">
			            </div>
			         	
			      
			            
			            
					<div  class="control-group"  align="center">
					<div  id="btndiv1">
			        <button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton"  >
								<i class="icon-ok icon-white"></i>
								<fmt:message key="button.save" />
							</button>
							 &nbsp; 
							 <button type="button" class="btn btn-primary " name="next"
								onclick="backToList();" id="cancelButton"
								style="cursor: pointer;margin:auto;" >
								<i class="icon-remove icon-white"></i>
								<fmt:message key="button.cancel" />
							</button> 
							<div class="clearfix"></div>
					</div>
					</div>
					</table>
				</form:form>
			</div>
		</div>
	</div>
</div>