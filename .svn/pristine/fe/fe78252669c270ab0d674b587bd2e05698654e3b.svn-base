<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="overflow-x: hidden;
          overflow-y:hidden">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>证据详情</title>
<script type="text/javascript">
function d_close(){
	$('#evidence').dialog('close');
}

function changeSa(){
	$('#evidence').dialog('close');
	var fileId=$("#fileId").val();
	var state=$("#state").val();
	var opinion=$("#opinion").val();
	var jc=$("#jc").val();
	var params = 'fileId='+ fileId+'&id='+id+'&opinion='+opinion+'&jc='+jc;
	document.location.href = "#bpm/yxjtzf/changeState";
    _execute('target', params);
}
</script>
</head>
<body>
<%-- <div id="divUI" style="height:330px;overflow-y:hidden">
<input type="hidden" id="fileId" value="${fileId}">

		<table  border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top: 300px;table-layout: fixed; word-wrap: break-word;" id="p1">	
		        
		        <tr>
					<td style="width:45%">
					<label style= "text-align:right;line-height:22px;" >事件名称</label>
					 
					</td>	
					<td>
					
			    </tr>
			
			<div  align="center">
			<span id="saveButtonDiv" align="center">
								<button type="submit" class="btn btn-primary" name="save"
								onclick="changeSa()" id="saveButton">
										提交
							</button>
							&nbsp;
							</span>
							<span id="cancelButtonDiv" class="">
								<button type="button" class="btn btn-primary cursor_pointer"
								name="next" onclick="d_close()" id="cancelButton">
								取消
							</button>
							</span>
							<div class="clearfix"></div>
			</div>
		</table>	
	</div> --%>
	
	        <table border="0" cellspacing="0" cellpadding="0" align="center" bordercolor="#AAAAAA"
				style="width:400px; height: 300px;table-layout: fixed; word-wrap: break-word;">
				<tr>
				    <td style="width:20%">
					<label style= "text-align:left;line-height:22px;" >证据名称</label>
					</td>	
					<td>
					<input type="text" id="eventName" required="required"  style="width:230px;" align="center"/>
					<em style="color:red;margin-left:5px">*</em>
					</td>
				</tr>
</table>

</body>
</html>