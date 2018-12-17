<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
<title><fmt:message key="newcompanylist.title" /></title>
<meta name="heading" content="<fmt:message key='友情链接详情页'/>" />
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

$(document).ready(function(){
	var read='${readOnly}';
	if(read == "readOnly"){
		
		/* $("*").attr("readOnly","readOnly"); */
		/* $("#btnBack").css("display","none");
		$("#btndiv1").css("display","none"); */
		$("#date1").show();
		$("#date2").show();
	}
});


$("#saveButton").click(function(event) { 
	var uploadedFiles = document.getElementById('file');
	//Uploaded Document Validation
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
	
});

	
	
	function backToList(){
		document.location.href="#bpm/admin/linkList";
		_execute('target', '');
	}
	
	 
</script>

<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png" style="float:left; margin:9px 6px 5px 5px" /><h2 style="font-size:16px; margin:2px;/* padding-left: 380px; */">友情链接详情页</h2>
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
<input type="hidden" id = "imgUrl" name="imgUrl" value="${url}"/>
<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main" style="padding-top: 10px">
				<form:form id="linkList" method="post"
					class="form-horizontal no-margin" action="bpm/Link/saveLinkList"
					autocomplete="off" modelAttribute="link"
					onSubmit="_execute('target','')">
					<form:hidden path="id" />

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
				
				
				<form:input type="hidden" path="modifyFileImg"  id="modifyFileImg" name="modifyFileImg"/>
				<form:input type="hidden" path="modifyFileUrl"  id="modifyFileUrl" name="modifyFileUrl"/>
				
				
					
		        <tr>
					<td style="width:500px">
					<label style= "text-align:right;line-height:22px;" >标题</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="title" id="title" class="span6" required="required"  style="width:300px;"/>
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
					<td  style="width:500px">
					 <label style= "text-align:right;line-height:22px; " >地址</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="url" id="url" class="span6"  style="width:300px;"  required="required" />
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
				</tr><tr>
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
				</tr><tr>
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
				</tr> <tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				
				  <tr>
					<td style="width:500px">
					<label style= "text-align:right;line-height:22px;" >状态</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="isActive" name="isActive"
								id="isActive" class="span6"  style="width:300px;">
							
								<option value="1"
									${fn:contains(link.isActive, '1') ?
									'selected' : ''}>
									<fmt:message key="启用" />
								</option>
								<option value="2"
									${fn:contains(link.isActive, '2')
									? 'selected' : ''}>
									<fmt:message key="停用" />
								</option>
								
							</form:select>

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
				</tr><tr>
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
				</tr><tr>
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
			
		
				
		          <tr id ="date1"  style="display:none">
					<td style="width:500px">
					<label style= "text-align:right;line-height:22px;" >创建时间</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="createDate" id="createDate" class="span6" style="width:300px;"  readonly="true"/>

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
				</tr><tr>
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
				</tr><tr>
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
					
					
				
				
		        <tr id ="date2"  style="display:none">
					<td style="width:500px">
					<label style= "text-align:right;line-height:22px;" >修改时间</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="modifyDate" id="modifyDate" class="span6" style="width:300px;"  readonly="true"/>

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
				</tr><tr>
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
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr> <tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
			    	
		        <tr >
					<td style="width:500px">
					<label style= "text-align:right;line-height:22px;" >已上传图片名</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="fileName" id="fileName" class="span6" style="width:300px;"  readonly="true"/>

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
				</tr><tr>
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
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr> <tr>
				<td >
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				<td>
				<label style= "text-align:right;line-height:22px; " ></label>
				</td>
				</tr>
				
				
			       <tr>
					<td style="width:500px">
					<label style= "text-align:right;line-height:22px;" >地址图片</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
						<input type="file" name="file" id="file" size="20" accept="image/*" style="width:200px"/>
					</td>
				
			</tr>
			
			
		     		  <tr>
					<td style="width:500px">
					<label style= "text-align:right;line-height:22px;" ></label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<em style="color:red;margin-left:5px;font-size:18px">(支持jpg/png/gif格式上传)</em>

					</td>
			</tr>
				
				
               <tr>
               <td style="width:500px"></td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <c:if test="${not empty link.pictureUrl}">
		  
		            <c:if test="${link.pictureUrl != '/images/profile/default.png'}">
                    <img id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="${link.pictureUrl}"  /> </br>
		            </c:if>
		            </c:if>

		            </td>
               </tr>
			    	
			    
			    
				</table>

			
<!--             <table  width="99%" height ="110px" border="0" align="center" cellpadding="0" cellspacing="0" id="p2" >		
			            <div class="control-group"    align="center">
			            </div>
			          	<div class="control-group"    align="center">
			            </div>
			            <div class="control-group"    align="center">
			            </div>
			            <div class="control-group"    align="center">
			            </div>
			             <div class="control-group"    align="center">
			            </div>
			             <div class="control-group"    align="center">
			            </div>
			             -->
			            
					<div  class="control-group"  align="center">
					<div class="form-actions no-margin pad-L0">	
					<div  id="btndiv1">
			        <button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton"   align="left">
								<i class="icon-ok icon-white"></i>
								<fmt:message key="button.save" />
							</button>
							 &nbsp;
							 <button type="button" class="btn btn-primary " name="next"
								onclick="backToList();" id="cancelButton"
								style="cursor: pointer;margin:auto;"  align="left">
								<i class="icon-remove icon-white"></i>
								<fmt:message key="button.cancel" />
							</button> 
							<div class="clearfix"></div>
					</div>
					</div>
					</div>
				<!-- 	</table> -->
				</form:form>
			</div>
		</div>
	</div>
</div>