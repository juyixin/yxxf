<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %>

<script type="text/javascript">
function checkForm(){
	
	if($("#file").val()=="") {
		validateMessageBox(form.title.error, "请选择要上传的csv文件！" , "error");
		return false;
	}
}

</script>

<div class="page-header">
	<div class="pull-left">
		<h2>导入字典</h2>
	</div>
	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="loadDataDictionaryGrid('UserDefined')" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="row-fluid">
	<div class="span12">
		<div class="widget-body">
			<div class="box_main" style="padding:20px;">
				<form method="post" class="form-horizontal no-margin" action="bpm/admin/importDataDictionaryToCsv" enctype="multipart/form-data">

					<input type="file" name="file" id="file"/>
					<input type="submit" value="上传文件" onclick="return checkForm();"/>
					
				</form>
			</div>
		</div>
	</div>
</div>