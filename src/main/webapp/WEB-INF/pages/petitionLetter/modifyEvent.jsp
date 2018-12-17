<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
<title><fmt:message key="newcompanylist.title" /></title>
<meta name="heading" content="<fmt:message key='信访事件详情页'/>" />
<style type="text/css">
.jstree-default a .jstree-icon {
	background-position: display:none;
}
</style>
</head>

<script type="text/javascript">

	
	function backToList(){
		var params = "personId="+$("#personId").val()+"&first="+$("#first").val();	
		document.location.href="#bpm/admin/modifyPerson";
		_execute('target', params);
	}

</script>
<input type="hidden" id="first" name="first" value="${first}"/>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png" style="float:left; margin:9px 6px 5px 5px" /><h2 style="font-size:16px; margin:2px;/* padding-left: 380px; */">信访事件详情页</h2>
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
				<form:form id="eventList" method="post"
					class="form-horizontal no-margin" action="bpm/admin/saveEvent"
					autocomplete="off" modelAttribute="event"
					onSubmit="_execute('target','')">
					<form:hidden path="id" />

               <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" id="p1">	
               <input type="hidden" id="personId"  name="personId"  value="${event.personId}"/>
				<input type="hidden" id="isActive"  name="isActive"  value="${event.isActive}"/>
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
					<td style="width:515px">
					<label style= "text-align:right;line-height:22px;" >事件名称</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="eventName" id="eventName" class="span6" required="required"  style="width:260px;"/>
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
					<td style="width:515px">
					<label style= "text-align:right;line-height:22px;" >事件内容</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:textarea path="eventDetail" id="eventDetail" class="span6" required="required"  style="height:100px;width:260px;"/>
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
					<td style="width:515px">
					<label style= "text-align:right;line-height:22px;" >备注</label>
					 
					</td>	
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:textarea path="bz" id="bz" class="span6" style="height:100px;width:260px;"/>
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