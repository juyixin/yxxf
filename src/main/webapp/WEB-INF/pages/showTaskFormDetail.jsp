<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>
<script type="text/javascript">
$("#header_menu").hide();
$("#top_menu").hide();
$("#side_menu").hide();
$("#ajax_loader").width("100%");
$("#target").height($("#target").height()+100);
fullScreenMode();
</script>
<div id="processFormDesignValues" class="padding20 padBottom0 floatLeft width-per-97">
<input type="hidden" name="isUpdateOnly" id="isUpdateOnly" value="${isUpdateOnly}" />
<input type="hidden" name="isUpdate" id="isUpdate" value="${isUpdate}" />
<input type="hidden" name="suspendState" id="suspendState" value='${suspendState}' />

			<div>
			
				<div class="row-fluid"> 
					<div class="span12">
			     		<span class="floatRight fontSize14 pad-L10 pad-T2">
			     			<div id="toggle-fullscreen-div" style="cursor: pointer;">
			     				<img src="/images/easybpm/common/min.png" />
			     			</div>
			     		</span>
					</div>
				</div>
	     		
				<div id="taskFormDiv" class="floatLeft" style="position:relative;">
					<%= request.getAttribute("taskFormScript") %> 
				</div>
				<div id="task_target"></div>
				<div class="floatRight margin10 pad-R70 pad-T30 fullHeight">
				<%@ include file="task/operatingfunction/showOperatingFunctions.jsp"%>
					<!-- <img src="images/Buttons/approve copy.jpg">&nbsp;<img src="images/Buttons/complete copy.jpg"> -->
				</div>
			</div>
	<div id="print_template_preview" class="displayNone"></div>
</div>	
<script>
checkFormSubmit();
loadListViewsOnForm();

$(function(){
	$("#taskFormDiv").find("input,select,textarea").each(function(index){
		var attrType = $(this).attr('type');
		if(attrType != 'hidden' && attrType !='button' && attrType != 'submit'){
			var topPos = $(this).css('top');
			var temptopPos = topPos.split('px');
			$(this).css('top',parseInt(temptopPos[0])+10);
			topPos = $(this).css('top');
			var leftPos = $(this).css('left');
			if(attrType == 'date'){
				var topPos2 = topPos.split('px');
				var leftPos2 = leftPos.split('px');
				$(this).siblings("img").css('top', parseInt(topPos2[0]) + 5);
				var eleWidth = $(this).css('width');
				if( eleWidth && eleWidth != 'auto'){
					var newEleWidth = eleWidth.split('px');
					$(this).siblings("img").css('left', parseInt(leftPos2[0]) + 30 +parseInt(newEleWidth[0]));
				}else{
					$(this).siblings("img").css('left', parseInt(leftPos2[0]) + 231);
				}
				$(this).siblings("img").css('position', 'absolute'); 
			}
		}
	});
	var formHeight = $("#"+$("#formId").val()).css("min-height");
    $("#form_div").css({ "min-height": formHeight, "overflow": "auto", "position": "relative"});
});
</script>
<%-- <div id="processFormDesignValues" class="padding20">
	<div class="panel-title-style style3 style18">Process History</div>
		<div class="table-view-task">
			<div id="taskFormDiv">
					<form id="taskForm" name="taskForm">
						<c:if test="${not empty historyDetails}">
						<c:if test="${fn:length(historyDetails) gt 0}">
								<table width="80%" height="80%" class="line-height46">	
									<c:forEach items="${historyDetails}" var="historyDetailsKey">
									<tr>
										<td>
											<div class="collapsed style3 style18"><span class="fontBold">Task Name&nbsp;:&nbsp;</span>${historyDetailsKey.key.name}</div>
											<div class="collapsed style3 style18"><span class="fontBold">Operator&nbsp;:&nbsp;</span>${historyDetailsKey.key.assignee}</div>
											<div class="collapsed style3 style18"><span class="fontBold">Created At&nbsp;:&nbsp;</span><fmt:formatDate pattern="MM/dd/yyyy HH:mm:ss"
value="${historyDetailsKey.key.startTime}" /> </div>
									</td>
									<td>
										<table>
											<c:forEach items="${historyDetailsKey.value}" var="historyDetails">										
											<tr>					
												<td width="60%"><span class="style3 style18">${historyDetails.name}</span></td>
												<td class="pad-L10"><input name="${historyDetails.name}" type="text" value="${historyDetails.value}" readonly ></input></td>						
											</tr>
										</c:forEach>										
											
										</table>
									</td>
								</tr>
								<tr height="40px">
								</tr>
								</c:forEach>
										
								</table>
						</c:if>
						</c:if>
							</form>
					</div>
	</div>
</div> --%>