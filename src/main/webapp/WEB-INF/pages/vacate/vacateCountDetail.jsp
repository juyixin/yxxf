<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">
function backList(){
	/* var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	var params = "startTime="+startTime+"&endTime="+endTime; */
	document.location.href = "#bpm/vacate/vacateCount";
    _execute('target', '');
}
</script>
<%@ include file="/common/messages.jsp" %>

<input type="hidden" id="startTime" value="${startTime}"/>
<input type="hidden" id="endTime" value="${endTime}"/>

<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>请假统计详情</h2>
	</div>

	<div align="right">

		<a style="padding: 3px; ">
		<input type="button" onclick="backList()" value="后退"/>  
		</a>
	</div>
</div>
<div><%= request.getAttribute("script") %></div>

