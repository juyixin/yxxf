<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.eazytec.common.util.CommonUtil,org.springframework.security.core.context.SecurityContextHolder,com.eazytec.model.User"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import ="com.eazytec.Constants" %>

<%
User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currentRoles = CommonUtil.getRolesAsString(user);
String userFullName = user.getFirstName()+" "+user.getLastName();
%>

<!DOCTYPE html>

<html>
<head>

	<link rel="icon" type="image/x-icon" href="/images/favicon.ico?v=2">
	<link rel="shortcut icon" type="image/x-icon" href="/images/favicon.ico?v=2">

	<title><decorator:title/> | <fmt:message key="webapp.name"/></title>
	<meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
	<c:set var="prefLang" value="<%=request.getSession(false).getAttribute(Constants.PREFERRED_LOCALE_KEY)%>"/>
	<c:set var="prefSkin" value="<%=request.getSession().getAttribute(Constants.USER_PREFERRED_SKIN)%>"/>
	<c:set var="loggedInUser" value="<%= SecurityContextHolder.getContext().getAuthentication().getPrincipal()%>"/>
	
	<script type="text/javascript" src="<c:url value='/scripts/boostrap/html5-trunk.js'/>"></script>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/bootstrap/icomoon/style.css'/>" />

    <c:choose>
	<c:when test="${fn:contains(header['User-Agent'],'MSIE')}">
		<%@ include file = "/common/includeStyleForIE.jsp" %>
    	<%@ include file = "/common/includeScriptForIE.jsp" %>
	</c:when>
	<c:otherwise>
		<%@ include file = "/common/includeStyle.jsp" %>
    	<%@ include file = "/common/includeScript.jsp" %>
	</c:otherwise>
	</c:choose>
    
    <decorator:head/>
    <style>

	body {
	    background: none repeat scroll 0 0 #FFFFFF;
	    color: #1A1A1A;
	    font: 12px/18px "Open Sans",arial,sans-serif;
	    margin: 0;
	}
	
    </style>

	<script type="text/javascript">
	var _mainContainer = "parent_container";
	var _params = "";
	$(function(){
		var windowHeight = $(window).height();
		$('#parent_container').height(windowHeight);
	});
	function _execute(container, requestedparams) {
    	_mainContainer = container;
    	if(requestedparams != ""){
    		_params = requestedparams;
    	}
    	var currentURL = window.location.href;
        if(currentURL.indexOf("#bpm/admin/getAllDictionariesAsLabelValue") == -1){
                destroyCKEditor();
        }
   	}
	
	function setIndexPage(tempindexPage,menuType){
		$(".submenu li").removeClass('side-menu-active');
	  	$(".submenu li a").removeClass('side-menu-active');
	  	$("#"+tempindexPage).addClass('side-menu-active');
	}
	
	
	function getDefaultValForForm(type) {
			var dayMap = {0:'Sunday', 1:'Monday', 2:'Tuesday', 3:'Wednesday', 4:'Thursday', 5:'Friday', 6:'Saturday'};
			var monthMap = {0:'January', 1:'February', 2:'March', 3:'April', 4:'May', 5:'June', 6:'July', 7:'August', 8:'September', 9:'October', 10:'November', 11:'December'};
			if(type.toLowerCase() == "time"){
				return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="HH:mm" />';
			}else if(type.toLowerCase() == "day"){
				var day = "<%= new java.util.Date().getDay() %>";
				return dayMap[day];
			}else if(type.toLowerCase() == "month"){
				var month = "<%= new java.util.Date().getMonth() %>";
				return monthMap[month];
			}else if(type.toLowerCase() == "year"){
				return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy" />';
			}else if(type.toLowerCase() == "date"){
				return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd" />';
			}else if(type.toLowerCase() == "datetime"){
				return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd HH:mm" />';
			}else if(type.toLowerCase() == "username"){
				return '<%=request.getSession().getAttribute("username")%>';
			}else if(type.toLowerCase() == "userlogin"){
				return '<%=request.getSession().getAttribute("userlogin")%>';
			}else if(type.toLowerCase() == "userid"){
				return '<%=request.getSession().getAttribute("userid")%>';
			}else if(type.toLowerCase() == "deptname"){
				return '<%=request.getSession().getAttribute("deptname")%>';
			}else if(type.toLowerCase() == "deptid"){
				return '<%=request.getSession().getAttribute("deptid")%>';
			}else if(type.toLowerCase() == "telephone"){
				return '<%=request.getSession().getAttribute("telephone")%>';
			}else if(type.toLowerCase() == "roleid"){
				return '<%=request.getSession().getAttribute("roleid")%>';
			}else if(type.toLowerCase() == "groupid"){
				return '<%=request.getSession().getAttribute("groupid")%>';
			}else if(type.toLowerCase() == "apppath"){
				return '<%=request.getRealPath("")%>';
			}
		}	
	
	</script>
</head>


<body<decorator:getProperty property="body.id" writeEntireProperty="true"/> class="eazybpmBody" id="default_body">
	<div class="container-fluid">
		<div class="main-container">
			<div class="row-fluid">
				<div class="span12">
					<div style="margin-top:10px;margin-left:10px;line-height:20px;">
						流程名称：<b>${processName}</b><br/>
						任务名称：<b>${activeTask}</b>
					</div>		
					<div id="processFormDesignValues" class="padding10">

						<input type="hidden" name="isUpdateOnly" id="isUpdateOnly" value="${isUpdateOnly}" />
						<input type="hidden" name="isUpdate" id="isUpdate" value="${isUpdate}" />
						<input type="hidden" name="suspendState" id="suspendState" value='${suspendState}' /> 
						<input type="hidden" name="processInsId" id="processInsId" value='${processInsId}' />
						<input type="hidden" name="type" id="gridType" value='${gridType}'/>
						<input id="userFullName" type="hidden" value="<%=userFullName%>"  />
						
						<div class="widget">
							<div class="widget-body">
						<div style="margin-bottom:10px">
							<ol id="formWizardTabs" class="bwizard-steps clearfix clickable" role="tablist">
							    <li id="formWizardTab-1" role="tab" style="z-index: 3;" aria-selected="true" class="active">
							        <span class="label badge-inverse">1</span>
							        <a class="hidden-phone" href="#">表单内容</a>
							    </li>
								<li id="formWizardTab-2" role="tab" style="z-index: 2;" aria-selected="false" class="">
								    <button class="listview btn btn-mini">2</button>
								    <a class="hidden-phone" href="#" >填写意见</a>
								</li>
								<li id="formWizardTab-3" role="tab" style="z-index: 1;" aria-selected="false" class="">
								    <button class="listview btn btn-mini">3</button>
								    <a class="hidden-phone" href="#" >流程图</a>
								</li>
							</ol>
						</div>
						<div id="formWizardTab-content"class="well">
							<div id="formWizardTab-content-1" class="displayBlock">
								<div class="row-fluid">
									<div class="span10" id="ajax_loader">
										<div id="taskFormDiv" class="floatLeft" style="position:relative;">
											<%= request.getAttribute("taskFormScript") %> 
										</div>
										
										<div class="floatRight pad-T30 fullHeight" id="operatingFunctionDiv" style="margin-right:-45px;">
										<%-- <%= request.getAttribute("customOfScript") %> --%>
										<%-- <%@ include file="../WEB-INF/pages/task/operatingfunction/showOperatingFunctions.jsp"%> --%>
											<!-- <img src="images/Buttons/approve copy.jpg">&nbsp;<img src="images/Buttons/complete copy.jpg"> -->
										</div>
										<div class="hidden" id="operateDiv">
											<%@ include file="../WEB-INF/pages/task/operatingfunction/showOperatingFunctions.jsp"%>
										</div>
									</div>
								</div>
							</div>
							<div id="formWizardTab-content-2" class="displayNone">
								<div class="row-fluid">
									<div class="span10">
										<%@ include file="../WEB-INF/pages/process/opinionForm.jsp"%>
									</div>
								</div>
							</div>
							<div id="formWizardTab-content-3" class="displayNone">
								<div id= "WorkFlowTraceDetail">
									<div class="row-fluid">
										<div>
													<c:if test="${not empty parentTraceJsonArray}">
													<a href="#" class="linkStyle" onclick="showParentTrace();">Show</a>
													</c:if>
													<div id="parentWorkFlowTrace" class="displayNone">
														<%=request.getAttribute("parentWorkFlowTrace")%>
													</div>
													<div id="parentWorkflowTraceTableDiv" class="displayNone">
														<div style="line-height: 33px; font-weight: bold;">Process
															Input Trace :</div>
														<table id="parentWorkflowTraceTable"
															class="table table-condensed table-bordered no-margin">
															<tr>
																<th>No</th>
																<th>Node Name</th>
																<th>Organiser</th>
																<th>Operation Time</th>
																<th>Operate Type</th>
																<th>Process Name</th>
															</tr>
														</table>
													</div>
										</div>

												
										<div id="opinion_div">
											<%=request.getAttribute("workFlowTrace")%>
										</div>

										<div id ="workflowTraceTableDiv">	
											<div style="line-height: 33px; font-weight: bold;">流程明细 :</div>
											<table id="workflowTraceTable" class="table table-condensed table-bordered no-margin">
												<tr>
													<th>序号</th>
													<th>节点名称</th>
													<th>操作人</th>
													<th>操作时间</th>
													<th>操作类型</th>	
													<th>流程名称</th>	
												</tr>
											</table> 
										</div>
										<div id ="formFieldTraceTableDiv"></div>
										<table class="table table-condensed table-bordered no-margin" id="formFieldTraceTable">
						
										</table> 
									</div>
								</div>
							</div>
						</div>
						</div>
						</div>
			
						
						<div style="height: 100%;width: 100%;">
							<div class="span12">
								<div id="popupDialog" class="form-background"></div>	
								<div id="involveUsersPopupDialog"></div>
								<div id="task_target"></div>
								<div id="userPopupDialog" class="form-background"></div>
								<div id="userPopupDialogManager"></div>
								<div id="userPopupDialogSecretary"></div>
								<div id="departmentPopupDialog"></div>
								<div id="partTimeDepartmentPopupDialog"></div>
								<div id="groupPopupDialog"></div>
								<div id="importPopupDialog"></div>
								<div id="importSQLPopupDialog"></div>
								<div id="importCSVPopupDialog"></div>
								<div id="exportMultiTablePopupDialog"></div>
								<div id="classificationPopupDialog"></div>
								<div id="processPreviewPopupDialog" style="text-align: center;vertical-align: middle;"></div>
								<div id="formAutoGenerationId"></div>
								<div id="tableRelationForeignKey"></div>
								<div id="doc_view_dialog" class="form-background"></div>
								<div id="print_preview" class="displayNone"></div>
								<div id="IFrame" class="iFrame">
									<iframe id="iframeTab" src="" width="100%" height="100%" marginwidth=0
									marginheight=0 hspace=0 vspace=0 frameBorder=0 scrolling=auto>
									</iframe>
								</div>
							</div>   
						</div> 	
					</div>
		    	</div>
			</div>
		</div>
	</div>
	<!-- end new ui  -->
	<div class="clear"></div>
	

	<script>
	function showParentTrace() {
		if($('#opinion_div').is(':visible')) {
			$('#opinion_div').hide();
			$('#workflowTraceTableDiv').hide();
			$('#parentWorkFlowTrace').show();
			$('#parentWorkflowTraceTableDiv').show();
			//loadWorkFlowTraceGrid("","",'');
		} else {
			$('#opinion_div').show();
			$('#workflowTraceTableDiv').show();
			$('#parentWorkFlowTrace').hide();
			$('#parentWorkflowTraceTableDiv').hide();
		}
	}
	setTimeout(function(){
		setDateAndMandatoryImgPositon();
	},500);
	function loadWorkFlowTraceGrid(traceValuesStr,fieldAuditValues,parentTraceInfo){
		if(parentTraceInfo != "") {
			var parentTrace = JSON.parse(parentTraceInfo);
			console.log(parentTrace);
			for(var i=0 ; i< parentTrace.length ; i++){
			 	addRow("parentWorkflowTraceTable",parentTrace[i],i+1);
			}
		}
		if(traceValuesStr != "") {
			var traceValues = JSON.parse(traceValuesStr);
			for(var i=0 ; i< traceValues.length ; i++){
			 	addRow("workflowTraceTable",traceValues[i],i+1);
			}
		}
		if(fieldAuditValues != "") {
			var fieldAuditDetails = JSON.parse(fieldAuditValues);
			for(var i=0 ; i< fieldAuditDetails.length ; i++){
				addRowForFormFiledAudit("formFieldTraceTable",fieldAuditDetails[i],i+1);
			}
		}

	}

	function addRow(tableID,values,no) {
	    var table = document.getElementById(tableID);
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	
	    var cell1 = row.insertCell(0);
		cell1.innerHTML = "&nbsp;"+no+"&nbsp;";
		//cell1.setAttribute("class","table_with_border");
		
	    var cell2 = row.insertCell(1);
	    cell2.innerHTML = "&nbsp;"+values.name+"&nbsp;";
		//cell2.setAttribute("class","table_with_border");
		
	    var cell3 = row.insertCell(2);
	  	cell3.innerHTML = "&nbsp;"+values.createdby+"&nbsp;";
		//cell3.setAttribute("class","table_with_border");
		
	  	var cell4 = row.insertCell(3);
	  	cell4.innerHTML = "&nbsp;"+values.createdtime+"&nbsp;";
		//cell4.setAttribute("class","table_with_border");
	  	
	    var cell5 = row.insertCell(4);
	    
	    var opType = values.operationType;
	    
	    if(opType == "submit"){
	    	opType = "提交";
	    }else if(opType == "save"){
	    	opType = "保存";
	    }else if(opType == "Back"){
	    	opType = "退回上一步";
	    }else if(opType == "return"){
	    	opType = "退回";
	    }else if(opType == "forward"){
	    	opType = "重新提交";
	    }else if(opType == "withdraw"){
	    	opType = "撤办";
	    }else if(opType == "recall"){
	    	opType = "拿回";
	    }else if(opType == "complete"){
	    	opType = "完成";
	    }else if(opType == "suspend"){
	    	opType = "暂停";
	    }else if(opType == "activate"){
	    	opType = "恢复";
	    } 
	    
	    
	  	cell5.innerHTML =  "&nbsp;"+opType+"&nbsp;"; 
		//cell5.setAttribute("class","table_with_border"); 	

	    var cell5 = row.insertCell(5);
	  	cell5.innerHTML =  "&nbsp;"+values.processDefinitionName+"&nbsp;"; 
	}

	function addRowForFormFiledAudit(tableID,values,no) {
	    var table = document.getElementById(tableID);
		if(no == 1){
			document.getElementById("formFieldTraceTableDiv").innerHTML = "<div style='line-height: 33px; font-weight: bold;'>表单修改记录 :</div>";
			var header=table.createTHead();
			var row=header.insertRow(0);
			var cell=row.insertCell(0);
			cell.innerHTML="<b>&nbsp;序号&nbsp;</b>";
			//cell.setAttribute("class","table_with_border"); 	
			
			var cell=row.insertCell(1);
			cell.innerHTML="<b>&nbsp;修改时间&nbsp;</b>";
			//cell.setAttribute("class","table_with_border"); 	
			
			var cell=row.insertCell(2);
			cell.innerHTML="<b>&nbsp;修改人&nbsp;</b>";
			//cell.setAttribute("class","table_with_border"); 	
			
			var cell=row.insertCell(3);
			cell.innerHTML="<b>&nbsp;表单域字段&nbsp;</b>";
			//cell.setAttribute("class","table_with_border"); 	
			
			var cell=row.insertCell(4);
			cell.innerHTML="<b>&nbsp;表单域名称&nbsp;</b>";
			//cell.setAttribute("class","table_with_border"); 	
			
			var cell=row.insertCell(5);
			cell.innerHTML="<b>&nbsp;原始内容&nbsp;</b>";
			//cell.setAttribute("class","table_with_border"); 	
			
			var cell=row.insertCell(6);
			cell.innerHTML="<b>&nbsp;修改后的内容&nbsp;</b>";
			//cell.setAttribute("class","table_with_border"); 	
		}
		checkFormSubmit();
		//loadCKEditor();     
		loadListViewsOnForm();
		
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	
	    var cell1 = row.insertCell(0);
		cell1.innerHTML = "&nbsp;"+no+"&nbsp;";
		//cell1.setAttribute("class","table_with_border"); 	
	
	    var cell2 = row.insertCell(1);
	    cell2.innerHTML = "&nbsp;"+values.modifiedTime+"&nbsp;";
		//cell2.setAttribute("class","table_with_border"); 	
		
	    var cell3 = row.insertCell(2);
	  	cell3.innerHTML = "&nbsp;"+values.modifiedBy+"&nbsp;";
		//cell3.setAttribute("class","table_with_border"); 	
		
	  	var cell4 = row.insertCell(3);
	  	cell4.innerHTML = "&nbsp;"+values.fieldName+"&nbsp;";
		//cell4.setAttribute("class","table_with_border"); 	
		
	    var cell5 = row.insertCell(4);
	  	cell5.innerHTML =  "&nbsp;"+values.chineseName+"&nbsp;";  	
		//cell5.setAttribute("class","table_with_border"); 	
		
	  	var cell6 = row.insertCell(5);
	  	cell6.innerHTML = "&nbsp;"+values.oldValue+"&nbsp;";
		//cell6.setAttribute("class","table_with_border"); 	
	  	
	    var cell7 = row.insertCell(6);
	  	cell7.innerHTML =  "&nbsp;"+values.newValue+"&nbsp;";
		//cell7.setAttribute("class","table_with_border"); 	  	
	}
	
	var printPreview = '${printPreivew}';
	if(printPreview == 'true'){
		document.getElementById("operatingFunctionDiv").style.display="none";
	}
	checkFormSubmit();
	//loadCKEditor();     
	loadListViewsOnForm();
	
	function setDateAndMandatoryImgPositon(){
		$("#taskFormDiv").find("input,select,textarea").each(function(index){
			var attrType = $(this).attr('type');
			if(attrType != 'hidden' && attrType !='button' && attrType != 'submit'){
				var topPos = $(this).css('top');
				var temptopPos = topPos.split('px');
				$(this).css('top',parseInt(temptopPos[0]));
				topPos = $(this).css('top');
				
				var leftPos = $(this).css('left');
				if(topPos){
					var topPos2 = topPos.split('px');
					$(this).css('top', parseInt(topPos2[0]));
				}
				if(leftPos){
					var leftPos2 = leftPos.split('px');
// 					$(this).css('left', parseInt(leftPos2[0]) + 15);
				}
				
				if(attrType == 'date'){
					var topPos2 = topPos.split('px');
					var leftPos2 = leftPos.split('px');
					$(this).siblings("img").css('top', parseInt(topPos2[0]));
					//$(this).siblings("img").css('top', parseInt(topPos2[0]));
					var eleWidth = $(this).css('width');
					if( eleWidth && eleWidth != 'auto'){
						var newEleWidth = eleWidth.split('px');
						$(this).siblings("img").css('left', parseInt(leftPos2[0]) +30+ parseInt(newEleWidth[0]));
						//$(this).siblings("img").css('left', parseInt(leftPos2[0]) + 188);
					} else{
						$(this).siblings("img").css('left', parseInt(leftPos2[0]) + 180);
					}
					$(this).siblings("img").css('position', 'absolute'); 
				}
				
				if(attrType == 'radio' || attrType == 'checkbox'){
					var elementTopPos = $(this).css('top');
					var elementLeftPos = $(this).css('left');
					var tempElementTopPos = elementTopPos.split('px');
					var tempElementLeftPos = elementLeftPos.split('px');
					//$(this).css('top',parseInt(tempElementTopPos[0])+10);
// 					$(this).css('left',parseInt(tempElementLeftPos[0])-15);
					
				}
				 if($(this).hasClass('mandatory')){
					var topPos2 = topPos.split('px');
					var leftPos2 = leftPos.split('px');
					if(attrType == 'radio' || attrType == 'checkbox'){
						$(this).siblings("span.requiredImg").css('top', parseInt(topPos2[0]));
					}else{
						$(this).siblings("span.requiredImg").css('top', parseInt(topPos2[0])-8);
					}
					
					var eleWidth = $(this).css('width');
					
					if( eleWidth && eleWidth != 'auto'){
						var newEleWidth = eleWidth.split('px');
						if(attrType == 'date'){
							$(this).siblings("span.requiredImg").css('left', parseInt(leftPos2[0]) + 49 + parseInt(newEleWidth[0]));
						}else{
							if(attrType == 'radio' || attrType == 'checkbox'){
								$(this).siblings("span.requiredImg").css('left', parseInt(leftPos2[0]) - 30 + parseInt(newEleWidth[0]));	
							}else{
								$(this).siblings("span.requiredImg").css('left', parseInt(leftPos2[0]) + 35 + parseInt(newEleWidth[0]));	
							}
						}
					}else{
						if(attrType == 'date'){
							$(this).siblings("span.requiredImg").css('left', parseInt(leftPos2[0]) + 246);
						}else{
							$(this).siblings("span.requiredImg").css('left', parseInt(leftPos2[0]) + 231);	
						}
					}
					$(this).siblings("span.requiredImg").css('position', 'absolute'); 
				}
			}
		});
		 var formHeight = $("#"+$("#formId").val()).css("min-height");
	     $("#form_div").css({ "min-height": formHeight, "overflow": "auto", "position": "relative"});
	}
	
 	window.onunload = refreshParent;
	function refreshParent() {
	   // window.opener.location.reload();
	   window.opener.refreshParentWindow(0,$('#gridType').val());
	}
	$(document).ready(function(){
		var url = window.location.href.split('?')[1];
		var value = url.substring(url.lastIndexOf('&') + 1);

		if(value.indexOf('isDefault') == 0) {
			$('#formWizardTabs').hide();
			//$( "#formSubmitSection" ).remove();
			$('button[name="Submit"]').hide();
			$('button[name="Save"]').hide();
			document.getElementById("operatingFunctionDiv").style.display="none";		
		}
		document.getElementById("operatingFunctionDiv").innerHTML= document.getElementById("operateDiv").innerHTML;
		//remove start node form from process
		if('${isStartNodeTask}' == 'true' || '${lastOperationPerformed}' == 'suspend'  || '${lastOperationPerformed}' == 'complete' || '${lastOperationPerformed}' == 'withdraw'  || '${lastOperationPerformed}' == 'terminate' ){
				document.getElementById("operatingFunctionDiv").style.display="none";		
		}
		if('${isOpinion}' == 'false') {
			$('#saveButton').hide();
		}
		changeFormWizardByTab();
		$("#formValuesDiv").addClass("active-step");
		loadWorkFlowTraceGrid('${traceJsonArray}','${formFieldAuditValues}','${parentTraceJsonArray}');
		highlightTrace('${workFlowTrace}');
		// deleting cloned file upload div in process
		$('.deleteDiv').on('click', function() {
			var id = $(this).attr("id");
			var divId = $("#" + id).closest("div").attr("id");
 			$(this).closest('div.clone').nextAll('.clone').each(function() {
 			//	$(this).css({"top":parseInt(top[0])-35});
 				 var topPosition =  $(this).attr("id");
 				$('div[id^='+topPosition+']').find("*").each(function() { 
 					 var topPosition =  $(this).css("top");
 					 var leftPosition =  $(this).css("left");
 					 var top = topPosition.split("px");
 					 var left = leftPosition.split("px");
 						if($(this).text() == "Add"){
 							$(this).css({"top":parseInt(top[0])-35});
 						}else if($(this).text() == "Remove"){
 							$(this).css({"top":parseInt(top[0])-35});
 						} else {
 							$(this).css({"top":parseInt(top[0])-35});
 						}
 					});	
		    }); 
			//$( 'div.clone' ).nextAll().css({"top":parseInt(top[0])-35});
			if (id.indexOf('delete') != 0) {
				$('#' + divId).remove();
			} else {
				$('#'+divId).find("*").each(function() { 
					$(this).attr("value",  "");
				});					
			}
		});

	});
	// highlight current task name in Instance Trace	
	function highlightTrace(data) {
		var svgId;
		$("#opinion_div > [id]").each(function() {
		    svgId = this.id ;
		});
		var data = document.getElementById(svgId);
		   var allElements = data.getElementsByTagName("text");
		   for(var i = 0; i < allElements.length; i++) {
		       var element = allElements[i];
				var tex = element.getAttribute("id");
				if(document.getElementById(tex).textContent == '${activeTask}') {
					var myrect = document.getElementById(tex);
				    myrect.style.fill = 'red';
				    break;
				}
		   }  
	}
	</script>
</body>
</html>
