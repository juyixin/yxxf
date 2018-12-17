<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<c:choose>
<c:when test="${message eq 'upload success'}">
<script type="text/javascript">
validateMessageBox(form.title.msgBoxTitleMessage,"上传成功","success");
</script>
</c:when>
<c:when test="${message eq 'upload fail'}">
<script type="text/javascript">
validateMessageBox(form.title.msgBoxTitleMessage,"上传失败","error");
</script>
</c:when>
<c:when test="${message eq 'Excle data error'}">
<script type="text/javascript">
validateMessageBox(form.title.msgBoxTitleMessage,"上传数据不正确","error");
</script>
</c:when>
<c:when test="${message eq 'Excle is Empty'}">
<script type="text/javascript">
validateMessageBox(form.title.msgBoxTitleMessage,"上传Excle为空表","error");
</script>
</c:when>
</c:choose>

<script type="text/javascript">
function backList(){

	document.location.href = "#bpm/admin/txlry";
    _execute('target', '');
}
</script>
<input type="hidden" id="code5" value="${unitId}"/>
<div>
<div class="page-header">
	
		<div align="left">
	    <h2 style="font-size:16px; margin:2px;/* padding-left: 380px; */"><fmt:message key="人员信息导入"/></h2>
		</div>
	
		<div align="right">
		<a id="backToPreviousPage" style="padding:3px;" href="javascript:void(0);" data-role="button" data-theme="b" onClick="backList()" data-icon=""><button class="btn"><fmt:message key="button.back" /></button></a>
	</div>
</div>
	<div class="box_main" style="padding-top:10px;padding-bottom:1px">
		<div class="widget-body">
			<form:form  method="post" class="form-horizontal no-margin" action="bpm/admin/uploadData?JC=${unitId}"  enctype="multipart/form-data" id="uploadForm">
					<table align="center">
						<tr>
							<td style="padding-top:15px"><span class="fontSize14"><b>上传文件：</b></span> </td>
							<td style="padding-top:15px"><input type="file" name="file" id="file_choose" class="height25" required="required"/></td>
						</tr>
						<tr>
							<td style="padding-top:15px"><span class="fontSize14"><b>说明：</b></span></td>
							<td style="padding-top:15px">
								<span class="fontSize14" style="display:block">请使用文件格式为xlsx/xls的文件上传</span>
							</td>
						</tr>
						<tr>
							<td style="padding-top:15px"><span class="fontSize14"><b>数据项顺序：</b></span></td>
							<td style="padding-top:15px">
								<span class="fontSize14" style="display:block;color:red">1:姓名</span>
								<span class="fontSize14" style="display:block;color:red">2:手机号码</span>
								<span class="fontSize14" style="display:block;color:red">2:办公电话</span>
								<span class="fontSize14" style="display:block;color:red">3:备注</span>
							</td>
						</tr>
						<tr>
							<td style="padding-top:15px"><span class="fontSize14"><b>EXCLE解析：</b></span></td>
							<td style="padding-top:15px">
							<div style="border:1px solid;width:290px;height:120px;background-color:white;overflow-y:scroll"><c:forEach items="${list}" var="li">${li}<br></c:forEach></div>
							</td>
						</tr>
					</table>
						<div style="padding-top:15px">
							<div class="control-group browse_button">
								<button type="submit" name="upload" class="btn btn-primary" onclick="bCancel=false;">
		               				<i class="icon-upload icon-white"></i> <fmt:message key="上传"/>
		            			</button>
							</div>
						</div>	
						
			</form:form>
				
				
				<div style="padding-top:0px">
							<div class="control-group browse_button">
		            			<a href="bpm/admin/downloadExcel"><button>
							<i class="icon-upload icon-white"></i> <fmt:message key="下载模板文档"/></button></a>
							</div>
		       </div>
		</div>
	</div>
</div>