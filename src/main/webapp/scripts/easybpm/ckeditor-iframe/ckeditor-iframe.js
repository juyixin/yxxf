
function userFormButtonsControl(count){
	   if(count > 1){
		   $('#previousButton').removeAttr("disabled", "disabled");
	   }else{
		   $('#previousButton').attr("disabled", "disabled");
	   }
	   if(count > 4){	
		   $('#nextButton').attr("disabled", "disabled");
	   }else{
		   $('#nextButton').removeAttr("disabled", "disabled");
	   }
}

setTimeout(function(){
	changeWizardByTabForDynamicForm();
},1000);

function changeWizardByTabForDynamicForm(){
	var tabCount = 0;
	$("#wizardTabs a").each(function(){
		tabCount = tabCount + 1;
		$(this).click(function(){
			var tabId = $(this).attr('id');
			tabId = tabId.split('-');
			for(var idx = 1; idx <= tabCount; idx++){
				$("#wizardTab-content-" + idx).css('display', 'none');
				$("#wizardTabSpan-" + idx).removeClass('active-step').addClass('completed-step');
			}
			$("#wizardTab-content-" + tabId[1]).css('display', '');
			$("#wizardTabSpan-" + tabId[1]).removeClass('completed-step').addClass('active-step');
		    userFormButtonsControl(tabId[1]);
		});
	});
}