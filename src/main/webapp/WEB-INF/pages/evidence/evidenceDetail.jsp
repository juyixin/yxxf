<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
<title><fmt:message key="详情信息页" /></title>
<meta name="heading" content="<fmt:message key='详情'/>" />
</head>

<spring:bind path="evidence.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<script type="text/javascript">
 function backList3(){
		document.location.href = "#bpm/admin/evidenceList";
	    _execute('target','');
	} 
 
 
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
			    	var phone = document.getElementById('handlerPhone').value;
			    	if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){ 
			    		openMessageBox(form.title.error,"手机号错误","error", true);
						event.preventDefault();
				    }else{
				    	var uploadedFiles = document.getElementById('files');
				    	
				 
				    	if(uploadedFiles.files.length>0){
				    		var fileLength = document.getElementsByName('files');
				    		var s = 0;
				    		var total = 0;
				    		for(var i=0;i<fileLength.length;i++){
				              /*   //获取元素的value值
				                alert(fileLength[i].value);
				                //获取元素的type值
				                alert(fileLength[i].type);
				                alert(fileLength[i].files); */
				                var filePath = fileLength[i].value;
				                if(filePath!=null&&filePath!=""&&filePath!=undefined){
				    			 s = fileLength[i].files[0].size;
					    	     total = total+s;
				                }
				         
				            }
				    		//alert(total);
				    	
				    		if(total<='314572800'){
				    			var fileValue = "1";
					    		document.getElementById("imgUrl").value=fileValue;
				    		}else{
								openMessageBox(form.title.error,"上传文件不能超过300M","error", true);
								event.preventDefault();
				    		}
				    		
 					 /*    var regex= /^.*\.(mp4|mkv|avi|jpg|png|gif|mp3|wma|mav)$/i;
						for (var i = 0; i < uploadedFiles.files.length; ++i) {
							 var uploadedFileName = uploadedFiles.files.item(i).name;
							if(regex.test(uploadedFileName) != true) {
								openMessageBox(form.title.msgBoxTitleError,form.error.invalidFileFormat,"error", true);
								event.preventDefault();
							}
						} */
				    		
				    	}else{
				    		var fileValue = "2";
				    	
				    		document.getElementById("imgUrl").value=fileValue;
				    	}
				    }
			    }
		}
		
	});
 
 function deleteEvidence(id){
	 var evidenceId = $("#id").val();
	 var params = "id="+id+"&evidenceId="+evidenceId;
	 document.location.href = "#bpm/admin/deleteEvidence";
	    _execute('target',params);
 }
 
 function addDiv(divId){
		var count = $('div[id^='+divId+']').length;
		var last = $('div[id^='+divId+']').last();
		var $clone = last.clone(true);
		var next_id = count + 1;
		$clone.attr('id', divId+"_"+next_id);
		$clone.attr('class', 'clone');
		$clone.show().insertAfter(last);
		$('div[id^='+divId+']').last().find("*[id]").each(function() { 
			//if( $(this).attr("type").indexOf("file") < 0 ) {
				$(this).attr("id",  +next_id);
			//}
		});
		
		$('div[id^='+divId+']').last().find("*").each(function() { 
			$(this).attr("value",  "");
			$(this).attr("class",  'clone');
			 var topPosition =  $(this).css("top");
			 var leftPosition =  $(this).css("left");
			 var top = topPosition.split("px");
			 var left = leftPosition.split("px");
				if($(this).text() == "Add"){
					$(this).css({"top":parseInt(top[0])+35});
				}else if($(this).text() == "Remove"){
					$(this).css({"top":parseInt(top[0])+35});
				} else {
					$(this).css({"top":parseInt(top[0])+35});
				}
			});		
	} 
 
 
 $('.deleteDiv').on('click', function() {
		var id = $(this).attr("id");
		var divId = $("#" + id).closest("div").attr("id");
		if (id.indexOf('delete') != 0) {
			$('#' + divId).remove();
		} else {
			$('#'+divId).find("*").each(function() { 
				$(this).attr("value",  "");
			});	
		}
	});
</script> 

<div class="page-header">	
<div class="pull-left">
		<img src="/images/listcode.png" style="float:left; margin:9px 6px 5px 5px" /><h2 style="font-size:16px; margin:2px;/* padding-left: 380px; */">信息详情页</h2>
	</div>
	<div class="height10"></div>

	<div align="right" id="btnBack">
		<a class="padding10" style="text-decoration: underline;"
			id="backToPreviousPage" href="javascript:void(0);" data-role="button"
			data-theme="b" onClick="backList3();" data-icon=""><button
				class="btn">
				<strong><fmt:message key="button.back" /></strong>
			</button></a>
	</div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form  id="evidence" commandName="evidence" class="form-horizontal no-margin" method="post" action="bpm/admin/saveEvidence" autocomplete="off" modelAttribute="evidence" enctype="multipart/form-data">
					<form:hidden path="id" id="id"/>
                    <form:hidden path="imgUrl" id="imgUrl"/>
				<%-- 	<form:hidden path="version" /> --%>
					<%-- <form:hidden path="departmentRole" />
					<form:hidden path="departmentType" /> --%>
	         <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" id="p1" >	
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
		       <tr >
		       
		       <td style="width:20%">
					<label style= "text-align:right;line-height:22px;" >事件名称</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="eventName" id="eventName" class="span6"  required="required" style="width:200px"  />
                        <em style="color:red;margin-left:5px">*</em>
					</td>
		
			
					<td  style="width:20%">
					 <label style= "text-align:right;line-height:22px; " >当事人姓名</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="concernName" id="concernName" class="span6" required="required" style="width:200px" />
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
				
				<tr >
					<td style="width:20%">
					<label style= "text-align:right;line-height:22px;" >当事人身份证</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="sfzCode" id="sfzCode" class="span6"  required="required" style="width:200px"  />
                        <em style="color:red;margin-left:5px">*</em>
					</td>
			
					<td  style="width:20%">
					 <label style= "text-align:right;line-height:22px; " >当事人性别</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form:select path="concernSex" name="concernSex" 
								id="concernSex" class="span6"   required="required" style="width:200px;">
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
				
				<tr >
					<td style="width:20%">
					<label style= "text-align:right;line-height:22px;" >处理人账号</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<form:input path="handlerId" id="handlerId" class="span6" required="required" style="width:200px"  />
                    <em style="color:red;margin-left:5px">*</em>
					</td>
					
					<td  style="width:20%">
					 <label style= "text-align:right;line-height:22px; " >处理人姓名</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="handlerName" id="handlerName" class="span6" required="required" style="width:200px"  />
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
				<td  style="width:20%">
					 <label style= "text-align:right;line-height:22px; " >处理人联系方式</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="handlerPhone" id="handlerPhone" class="span6" required="required" style="width:200px"  />
					    <em style="color:red;margin-left:5px">*</em>
					</td>
					
					<td  style="width:20%">
					 <label style= "text-align:right;line-height:22px; " >内容</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="content" id="content" class="span6" style="width:200px"  />
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
				
				<tr >
					<td style="width:20%">
					<label style= "text-align:right;line-height:22px;" >创建时间</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<form:input path="createDate" id="createDate" class="span6"  style="width:200px" readonly="true"/>

					</td>
			
					<td  style="width:20%">
					 <label style= "text-align:right;line-height:22px; " >修改时间</label>
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="modifyDate" id="modifyDate" class="span6" style="width:200px" readonly="true"/>
						
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
				<label style= "text-align:center;line-height:22px; " >已上传证据</label>
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
				
				
		
		
	     <c:forEach var="filePath" items="${filePathList}" varStatus="rowNum">	
				<td style="width:20%">
				<label style= "text-align:right;line-height:22px; " >${filePath.fileName}(${filePath.fileType})</label>
					
					</td>					
					<td>	
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;							
				<button class="btn" style="width:60px;" href="javascript:void(0);" onclick="downloadDocument('${filePath.id}'); return false;"><center><fmt:message key="documentForm.download"/></center></button>
				<button class="btn" style="width:60px;" href="javascript:void(0);" onclick="deleteEvidence('${filePath.id}'); return false;"><center>删除</center></button>
					</td>	
					<c:if test="${rowNum.count%2==0}">
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
                 </c:if>	
			</c:forEach>
	
  
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
				<label style= "text-align:center;line-height:22px; " >上传证据</label>
				</td>
				</tr>
					
					<tr >
					<td style="width:20%">
				<label style= "text-align:center;line-height:22px; " ></label>
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<div id="clonablerow">
					<input type="file" name="files" id="files" class="span6" style="width:200px"/>
					<a href="javascript:;" id="add" onclick="addDiv('clonablerow')"><fmt:message key="form.add"/></a>&nbsp;&nbsp;
					<a href="javascript:;" id="delete" class="deleteDiv" ><fmt:message key="form.remove"/></a>
					</div>
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
				
		</table>
					
			       
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
				</form:form>
			</div>
		</div>
	</div>
</div>

<v:javascript formName="unitUnion" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>