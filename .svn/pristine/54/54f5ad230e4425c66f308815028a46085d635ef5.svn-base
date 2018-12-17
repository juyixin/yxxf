<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>
	<div id="taskFormDiv" class="floatleft" style="position:relative;">
   		<%= request.getAttribute("script") %>
   	</div>
	<script>
checkFormSubmit();
loadCKEditor(function() {
	  setTimeout(function(){	
			for(key in richTextBoxMap){
				$("#"+key).attr("style",richTextBoxMap[key]);
			}
		},1000);
	});
	
loadListViewsOnForm();
$(function(){
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
//					$(this).css('left', parseInt(leftPos2[0]) + 15);
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
//					$(this).css('left',parseInt(tempElementLeftPos[0])-15);
				
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
	
});
</script>
<div id="print_template_preview" class="displayNone"></div>
