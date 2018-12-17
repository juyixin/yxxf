<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %>
<div id="processFormDesignValues" class="padding20 padBottom0 floatLeft width-per-97">
	<div>
		<div class="row-fluid"> 
			<div class="span12">
	     		<span class="floatRight fontSize14 pad-L10 pad-T2">
	     		</span>
			</div>
		</div>
    		
		<div id="taskFormDiv" class="floatLeft" style="position:relative;">
			<%=request.getAttribute("script") %>
		</div>
		<div id="task_target"></div>
		<div class="floatRight margin10 pad-R70 pad-T30 fullHeight">
		</div>
	</div>
</div>	
<script>
$("#showOlderTaskForm").removeClass('side-menu-active');
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

<div id="print_template_preview" class="displayNone"></div>