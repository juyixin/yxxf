<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function backList5(){
	document.location.href = "#bpm/sendDocuments/sendDocumentsList";
    _execute('target', '');
}
</script>

<spring:bind path="sendDocuments.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<h2>详情页</h2>
	</div>

	<div align="right">
		<button type="submit" class="btn btn-primary" name="save"
								onclick="backList5()" id="button3">
										后退
						    </button>
	</div>
</div>

<div class="row-fluid">
	<div class="span12">
		<div class="widget-body">
			<div>
				<form:form id="sendDocuments" commandName="sendDocuments" method="post"
					class="form-horizontal no-margin" action=""
					autocomplete="off" modelAttribute="sendDocuments"
					onSubmit="_execute('target','');">
					
			
					
	
		<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" id="p1">
         
            
            <tr>
				<td height="22" style="width:15%" align="center" >
				<b>基本信息</b></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
			</tr>
			
				<tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>
				
				
				 <tr>
					<td height="22" style="width:15%" align="right">					
					      <label>主题</label>
					</td>
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
                    <form:input path="theme" id="theme"  required="required" readonly="true" style="width:280px;"/>
                    <em style="color:red;margin-left:5px">*</em>
					</td>
				
				 <td height="22" style="width:15%" align="right">
					<label>发送时间</label>
					</td>
					
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<form:input path="sendTime" id="sendTime"   required="required" readonly="true" style="width:280px;"/>
					<em style="color:red;margin-left:5px">*</em>
					</td>
				</tr>
				
				
				<tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>
				
				
				 <tr>
					<td height="22" style="width:15%" align="right">					
					      <label>发件人</label>
					</td>
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
                   <input type="text" id="senderName"  value="${senderName}" required="required" readonly="true" style="width:280px;"/>
                    <em style="color:red;margin-left:5px">*</em>
					</td>
				
				 <td height="22" style="width:15%" align="right">
					<label>收件人</label>
					</td>
					
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="text" id="recipientName"  value="${recipientName}" required="required" readonly="true" style="width:280px;"/>
					<em style="color:red;margin-left:5px">*</em>
					</td>
				</tr>
			
			    	
			    <tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>

				
				<tr>
                   
                   	
					<td height="22" style="width:15%" align="right">
					<label>抄送人</label>					
					</td>
					
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
						<form:input path="copyPeopleName" id="copyPeopleName"  required="required" readonly="true" style="width:280px"/>
						
						<!-- 	<em style="color:red;margin-left:5px">*</em> -->
							
					</td>
			
					<td height="22" style="width:15%" align="right">					
					      <label>内容</label>
					</td>
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
                    <form:textarea path="content" id="content"  required="required" readonly="true" style="width:280px;"/>
                    <em style="color:red;margin-left:5px">*</em>
					</td>
				
				</tr>

				
			<tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>
				
			<tr>
				<td height="22" style="width:15%" align="center" >
				<b>文件列表</b></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
			    </tr>
			    	
			    <tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>
			
			<c:choose>  
            <c:when test="${not empty list}">  
	            <c:forEach var="en" items="${list}" varStatus="rowNum">
	           <tr>	
				 <td height="22" style="width:15%" align="right">
					<label>序号</label>					
					</td>				
				<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
					<%-- <input type="text" name="orderNo" value="${en.orderNo}" readonly="readonly" style="width:280px"/>
					<em style="color:red;margin-left:5px">*</em> --%>	
					<span>${rowNum.index+1}</span>			
					</td>
				
                  </tr>
                  
                  
                  
				<tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>
				
				<tr>
                   
                   	
					<td height="22" style="width:15%" align="right">
					<label>文件名称</label>					
					</td>
					
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
						<input type="text" id="fileName"  value="${en.fileName}" required="required" readonly="true" style="width:280px"/>
						
							<em style="color:red;margin-left:5px">*</em>
							
					</td>
					<td height="22"  align="right">
					<label>文件查看/下载</label>					
					</td>
					
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
							<input type="button" id="fileType"  onclick="downloadDocument('${en.id}'); return false;" value ="下载" style="width:50px"/>
							<em style="color:red;margin-left:5px">*</em>
					</td>
				
				</tr>
                   
				<c:if test="${rowNum.count%1==0}">
				<tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>
                </c:if>	
			</c:forEach>
			</c:when>
			<c:otherwise>
			 <td height="22" style="width:15%" align="right">
					<label>无</label>					
					</td>							
                  </tr>
			</c:otherwise>
			</c:choose>
			
				<tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
				</tr>
			
			 <tr>
				<td height="22" style="width:15%" align="center" >
				<b>系统信息</b></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
			</tr>
	             
	         <tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
		     </tr>
				
				 <tr>		
				 
				 
				 	<td height="22" style="width:15%" align="right">
					   <label>创建人</label>
					</td>
					
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
		
                    <input type="text" value="${senderName}"   required="required" readonly="true" style="width:280px;"/>

							<em style="color:red;margin-left:5px">*</em>
					</td>
					
					  			 
				    <td height="22" align="right">
					 <label>创建时间</label>
					</td>
					
					<td height="22" align="left" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
	
                    <form:input path="sendTime"  required="required" readonly="true"  style="width:280px;"/>
							<em style="color:red;margin-left:5px">*</em>
					</td>
					
				
				</tr>
				
				
				
				<tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
		     </tr>
		     <tr>
				<td height="22" style="width:15%" align="right" ></td>
				<td height="22" align="left" ></td>
				<td height="22" align="right"></td>
				<td height="22" align="left" ></td>
		     </tr>
			
		</table>
		
		
		
						
					
					
				<%-- 	<div class="control-group"  align="center">
						<div>
							<span id="saveButtonDiv" align="center">
								<button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
										<fmt:message key="button.save" />
							</button>
							&nbsp;
							</span>
							<span id="cancelButtonDiv" class="">
								<button type="button" class="btn btn-primary cursor_pointer"
								name="next" onclick="backList5()" id="cancelButton">
								<fmt:message key="button.cancel" />
							</button
							<div class="clearfix"></div>
						</div>
					</div> --%>
				</form:form>
			</div>
		</div>
	</div>
</div>