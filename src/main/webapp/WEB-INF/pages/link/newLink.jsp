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
	}
});

	
	function backToList(){
		document.location.href="#bpm/admin/linkList";
		_execute('target', '');
	}

/*    //上传图片
function ajaxFileUpload() {
       //var form = new FormData(document.getElementById("tf"))
	   var fileValue = $("#file").val();
	   alert(fileValue);
	   if(fileValue !=null && fileValue != ""){
		   $.msgBox({
			    title: "确认",
			    content: "是否上传",
			    type: "confirm",
			    buttons: [{ value: "是" }, { value: "否" }],
			        	success: function (result) {
			    	        if (result == "是") {
			    	    		jQuery.ajax({
			    	    			url:'bpm/admin/uploadImg',
			    	    			type:"get",
			    	    			data: {id:JSON.stringify(fileValue)},
			    	    			dataType:"json",
			    	    		    success:function(data){
			    	    				if(data.code == "1"){
			    	    					document.getElementById("profileImage").src =  data.url; //显示图片
			    	    					document.getElementById("profileImage").style.display = "";
			    	    				} else {
			    	    					alert(data.msg);
			    	    				}
			    	    			},
			    	    			error : function(ex) { alert(ex.status); }
			    	    			});	
			    	        }
			    	    }
			});
    	
	   }else{
		   alert("请选择图片");
	   }
	
}
	 */
	 
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
			 
			                        
			    
			    
				</table>

			
            <table  width="99%" height ="110px" border="0" align="center" cellpadding="0" cellspacing="0" id="p2" >		
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
			            
			            
					<div  class="control-group"  align="center">
					<div  id="btndiv1">
			        <button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton"  >
								<i class="icon-ok icon-white"></i>
								<fmt:message key="button.save" />
							</button>
							 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
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