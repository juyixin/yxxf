function showRunOn(runOn) {
	//alert(runOn);
	if (runOn == 'On') {
		$("#jobRunAt").removeAttr("disabled", "disabled");
	}
	else {
		
		$("#jobRunAt").val("");
		$("#jobRunAt").attr("disabled", "true");
	}
}


function deleteTriggers(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var triggerNames=new Array();
	var jobNames = new Array();
	var i = 0;
	rowid.forEach(function(item) {
		var triggerName = gridObj.jqGrid('getCell', item, 'Task_Name_Hidden');
		var jobName = gridObj.jqGrid('getCell', item, 'Job_Name');
		triggerNames[i] = triggerName;
		jobNames[i]=jobName;
		i++; 
	});
	
	if(rowid.length == 0){
		validateMessageBox(form.title.deleteTrigger, form.msg.selectSingleTimingTask, false);
		
	}
	if(rowid.length !=0 ){
	deleteAllTriggerConfirm(triggerNames,jobNames);
	//deleteAllTriggerConfirm(triggerNames);
	}
}

function deleteAllTriggerConfirm(triggerNames,jobNames){
	var status=false;
	var message="";
	var title="";
	var type="";
	var tempResult=true;
	var viewId="TIMING_TASK";
	var viewName="Timing Task";
	
	
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteTask,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    
	        	success: function (result) {
	    	        if (result == "Yes") {
	    	        	$.ajax({
	    	        		url: 'bpm/admin/timingtaskdelete',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'triggerNames='+triggerNames+'&jobNames='+jobNames,
	    	        		success : function(response) {
	    	        			
		    	        			if(response.successMessage){
		    	        				title = form.title.success;
	        							type="success";
	        							message=response.successMessage;
	        							showTimingTaskListViews(viewId,viewName);
		    	        			}else{
		    	        				title = form.title.error;
	        							type="error";
	        							message=response.errorMessage;
	        							timingTaskMessageDisplay(title, form.msg.errorInDeleteTask, type, viewId, viewName, tempResult);
		    	        			}
	    	        			}
	    	        	});
	    	        }else{
	    	        	tempResult= false;
	    	  	  }
	    	    },
	    	   
	});
}

function _showEditTrigger(cellValue, options, rawObject){
	  return '<a id="editTrigger" href="#bpm/admin/timingTask" data-role="button" data-icon="" data-theme="b" onclick="_execute(\'target\',\'triggerName='+rawObject.triggerName+'\');"><b>'+rawObject.name+'</b></a>';
	
}

function timingTaskMessageDisplay(title,content,type,viewId,viewName,tempResult){
	if(tempResult){
	validateMessageBox(title, content, type);
		
	}
}

function deleteWidgets(){
	
	 var rowid =  gridObj.getGridParam('selarrrow');
		var widgetNames=new Array();
		var i = 0;
		rowid.forEach(function(item) {
			var widgetName = gridObj.jqGrid('getCell', item, 'id');
			widgetNames[i] = widgetName;
			i++; 
		});
		
		if(rowid.length == 0){
			validateMessageBox(form.title.deleteWidget, form.msg.selectAtleastSingleWidget, false);
			
		}
		if(rowid.length !=0 ){
			deleteAllWidgetConfirm(widgetNames);
		} 
}
function deleteAllWidgetConfirm(widgetNames){
		var status=false;
		var message="";
		var title="";
		var type="";
		var tempResult=true;
		var viewId="WIDGET";
		var viewName="Widget";
		
		
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteWidget,
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    
		        	success: function (result) {
		    	        if (result == "Yes") {
		    	        	$.ajax({
		    	        		url: 'bpm/admin/widgetdelete',
		    	                type: 'GET',
		    	                dataType: 'json',
		    	                async: false,
		    	                data: 'widgetNames='+widgetNames,
		    	        		success : function(response) {
		    	        		//	alert(response.successMessage);
			    	        			if(response.successMessage){
			    	        				title=form.title.success;
		        							type="success";
		        							message=response.successMessage;
			    	        				showWidgetListView(viewId,viewName);
		        							widgetMessageDisplay(title, form.msg.successInDeleteWidget, type,viewId,viewName,tempResult);

			    	        			}else{
			    	        				
			    	        				title = form.title.error;
		        							type = "error";
		        							message=response.errorMessage;
		        							showWidgetListView(viewId,viewName);
		        							widgetMessageDisplay(title, form.msg.errorInDeleteWidget, type,viewId,viewName,tempResult);
			    	        			}
		    	        			}
		    	        	});
		    	        }else{
		    	        	tempResult= false;
		    	  	  }
		    	    },
		    	   
		});
	}
function widgetMessageDisplay(title,content,type,viewId,viewName,tempResult){
		if(tempResult){
			validateMessageBox(title, content, type);
			
		}
	}
function showWidgetListView(listViewName,title){
	if(checkListViewName(listViewName, false)){
		var listViewParam="listViewName="+listViewName+"&title="+title+"&container=target&listViewParams=[{}]";
		document.location.href = "#bpm/listView/showListViewGrid";
		_execute('target',listViewParam);
		validateMessageBox(form.title.message, form.msg.successInDeleteWidget , "success");
	}else{
		validateMessageBox(form.title.message, form.msg.listViewNotPresent , "info");
	}
}

function newsListDisplay(listViewName,title,container,listViewParams){
	
	if(listViewParams == ''){
		listViewParams = "[{}]";
	}
	
	//showListViewsWithContainerAndParam(listViewName,title, container,listViewParams);
	//alert(container);
	$.ajax({
		url: '/bpm/listView/showListViewGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'listViewName=' + listViewName + "&title=" + title + "&container=" + container + "&listViewParams=" + listViewParams +"&needHeader=" + false,
		success : function(response) {
			
			//console.log(response);
			if(response.length > 0){
				setTimeout(function(){$("#_"+container).html(response);},10);
				//alert(response);
			}
			},
			failure: function(response){
				alert("Failure  "+response);
			}
	});
}

function getHomepageHeight(container){
	return parseInt($("#_"+container).height()) - 5;
}

function getHomepageWidth(container){
	return parseInt($("#_"+container).width()) - 5;
}

function getReport(reportName,container){
	if(container!= undefined){
		var reportHeight = getHomepageHeight(container);
		var reportWidth = getHomepageWidth(container);
		var reportStyle =reportHeight+ ","+reportWidth;

		$.ajax({
				url: '/bpm/report/showHomepageReport',
			type: 'GET',
			dataType: 'html',
				async: false,
				data: "reportName=" + reportName+"&container="+reportStyle,
				success : function(response) {
					if(response.length > 0){
						setTimeout(function(){$("#_"+container).html(response);},10);
						if(document.getElementById("homePageReport") != undefined){
							document.getElementById("homePageReport").width = ""+getHomepageWidth(container)+"";
							$("#homePageReport").height(getHomepageHeight(container));
						}
					}
				},
				failure: function(response){
					validateMessageBox(form.title.error,form.error.homePageReport,"error");
				}
		});

	}
}

function quickNavigationDisplay(container){
	
	//img src="images/${imagename}"
	$.ajax({
		url: '/bpm/admin/showQuickNavigation',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: "container=" + container,
		success : function(response) {
			
			//console.log(response);
			if(response.length > 0){
				setTimeout(function(){$("#_"+container).html(response);},10);
				//alert(response);
			}
			},
			failure: function(response){
				alert("Failure  "+response);
			}
	});
}



function setSubContainerWidth(widgetName,noOfColumns){
if(noOfColumns == 2){	
	var w = parseInt(getWindowWidth());
	var newsWidth = w - 910;
	$("#"+widgetName).width(parseInt($("#generalProperties").width()));
}else{
	var w = parseInt(getWindowWidth());
	var newsWidth = w - 1205;
	$("#"+widgetName).width(parseInt($("#generalProperties").width()));
	
}

}

function setSubContainerHeight(widgetName,noOfColumns,linkType){
	var h = parseInt(getWindowHeight());
	var newsHeight =  h / 2.5 - 57;
	if(linkType == 'listView'){
		newsHeight =  newsHeight - 26;
	}
	$("#"+widgetName).height(parseInt(newsHeight));
	$(".column").css("min-height",parseInt(newsHeight) + 78);
	$(".dragbox").css("min-height",parseInt(newsHeight) + 78);
}

function setQuartzContainerHeight(widgetName,noOfColumns){
	var h = parseInt(getWindowHeight());
	var newsHeight =  h / 2.5 - 57;
	$("#"+widgetName).height(parseInt(newsHeight));
	$(".column").css("min-height",parseInt(newsHeight) + 68);
	$(".dragbox").css("min-height",parseInt(newsHeight) + 68);
}

function setQuartzMainContainerHeight(widgetName,noOfColumns){/*
	if(noOfColumns == 2){
	var h = parseInt(getWindowHeight());
	var newsHeight = h - 425;
	$("#"+widgetName).height(parseInt(newsHeight));
}else if(noOfColumns == 3){
	
	var h = parseInt(getWindowHeight());
	var newsHeight = h - 443;
	
	$("#"+widgetName).height(parseInt(newsHeight));
} else{
	
	var h = parseInt(getWindowHeight());
	var newsHeight = h - 650;
	$("#"+widgetName).height(parseInt(newsHeight));
}
*/}

function setMainContainerHeight(widgetName,noOfColumns){/*
	
	if(noOfColumns == 2){
	var w = parseInt(getWindowHeight());
	var newsHeight = w - 425;
	$("#"+widgetName).height(newsHeight);
}else if(noOfColumns == 3){
		var w = parseInt(getWindowHeight());
		var newsHeight = w - 443;
	//	alert(newsHeight);
		$("#"+widgetName).height(newsHeight);
}
else{
	var w = parseInt(getWindowHeight());
	var newsHeight = w - 650;
	$("#"+widgetName).height(newsHeight);
}
*/}


function setMainContainerWidth(widgetName,noOfColumns){
	/*if(noOfColumns == 2){
	var w = parseInt(getWindowWidth());
	var newsWidth = w - 970;
	$("#"+widgetName).width(newsWidth);
}else if(noOfColumns == 3){
	var w = parseInt(getWindowWidth());
	var newsWidth = w - 1161;
	$("#"+widgetName).width(parseInt($("#generalProperties").width())-23);
}
else{
	var w = parseInt(getWindowWidth());
	var newsWidth = w - 1830;
	$("#"+widgetName).width(newsWidth);

	}*/
}

function clearWidgetContents(mainContainerId,widgetId){
	
    var deleteValidationFlag=$("#deleteValidationFlag").val();
    if(deleteValidationFlag=='false'){
		validateMessageBox(form.title.validation, form.msg.loginUserNotAllowedToDeleteThisLayout , "info" ,"columnsOrder");
		return false;
	}
	
	$.msgBox({
		title : form.title.confirm,
		content : form.msg.wantToDeleteWidget,
		type : "confirm",
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
			var selected_nodes = $("#layoutTree").jstree("get_selected", null, true); 
			var assignedTo = selected_nodes.attr("name");
			var widgetNames = $("#widgetNames").val();
			$("#"+mainContainerId).remove();
			if(widgetNames == undefined){
				$.ajax({
				url: '/bpm/admin/deleteSelectedWidgets',
				type: 'GET',
				async: false,
				data: 'widgetIdDelete=' + widgetId + '&widgetDeleteType=Home&assignedTo=User',
				success : function(response) {
					if(response.length > 0){
						
					}
				}
			});
			}else{
					if(assignedTo == undefined){
						$.ajax({
						url: '/bpm/admin/deleteSelectedWidgets',
						type: 'GET',
						async: false,
						data: 'widgetIdDelete=' + widgetId + '&widgetDeleteType=Home&assignedTo=User',
						success : function(response) {
							if(response.length > 0){
								$("#widgetNames").val(response);
							}else{
								$("#widgetNames").val('');
							}
							}
						});
						
						
					}else{
						$.ajax({
						url: '/bpm/admin/deleteSelectedWidgets',
						type: 'GET',
						async: false,
						data: 'widgetIdDelete=' + widgetId + '&widgetDeleteType=Admin&assignedTo='+assignedTo,
						success : function(response) {
							if(response.length > 0){
								$("#widgetNames").val(response);
							}else{
								$("#widgetNames").val('');
							}
							}
						});
					}
					
				}
				
				
			} else {
				return false;
			}
		}
	});
}

/*function setGeneralPropertiesWidth(){
	var w = parseInt(getWindowWidth());
	var newsWidth = (w - 269.5)/3;
	parseInt($("#generalProperties").width(newsWidth));
}
function getGridWidthForHomePage(){
	//alert("gridId    "+$("#"+widgetContainer).width());
	return parseInt($("#"+widgetContainer).width()) - 1;
}
function getGridHeightForHomePage(){
	//alert(widgetContainer);
	//setTimeout(function (){alert("gridId    "+$("#"+widgetContainer).height());},1000);
	
	return parseInt($("#"+widgetContainer).height()) - 50;
}*/


function setDivHeight(divName){
var w = parseInt(getWindowHeight());
var newsHeight = w - 44;
$("#"+divName).height(newsHeight);
}


function setspan12Height(){
	var targetheight = parseInt(getWindowHeight());
	$(".div-height").height(targetheight);
}

function deleteQuickNavigation(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var quickNavigations=new Array();
	var i = 0;
	rowid.forEach(function(item) {
		var quickNavigation = gridObj.jqGrid('getCell', item, 'id');
		quickNavigations[i] = quickNavigation;
		i++; 
	});
	
	if(rowid.length == 0){
					validateMessageBox(form.title.deleteQuickNav, form.msg.selectAtleastSingleQuickNav, false);
		
	}
	if(rowid.length !=0 ){
	deleteAllQuickNavigationConfirm(quickNavigations);
	//deleteAllTriggerConfirm(triggerNames);
	}
}

function deleteAllQuickNavigationConfirm(quickNavigation){
	var status=false;
	var message="";
	var title="";
	var type="";
	var tempResult=true;
	var viewId="QUICK_NAVIGATION";
	var viewName="Quick Navigation";
	
	
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteQuickNav,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    
	        	success: function (result) {
	    	        if (result == "Yes") {
	    	        	$.ajax({
	    	        		url: 'bpm/admin/quickNavigationDelete',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'quickNavigation='+quickNavigation,
	    	        		success : function(response) {
	    	        			//alert(response);
		    	        			if(response.successMessage){
		    	        				title = form.title.success;
	        							type="success";
	        							message=response.successMessage;
	        							showQuickNavListViews(viewId,viewName);
	        							$('#quickNavigation'+id).append('<div id="message">'+response+'</div>');
	        							setTimeout(function () {

	        								  $('#message').fadeOut(function(){

	        									$(this).remove();

	        								  });

	        								}, 1000);
		    	        			}else{
	        							title = form.title.error;
	        							type="error";
	        							message=response.errorMessage;
	        							showQuickNavListViews(viewId,viewName);
		    	        			}
	    	        			}
	    	        	});
	    	        }else{
	    	        	tempResult= false;
	    	  	  }
	    	    },
	    	   
	});
}

function showQuickNavListViews(listViewName,title){
	if(checkListViewName(listViewName, false)){
		var listViewParam="listViewName="+listViewName+"&title="+title+"&container=target&listViewParams=[{}]";
		document.location.href = "#bpm/listView/showListViewGrid";
		_execute('target',listViewParam);
		validateMessageBox(form.title.message, form.msg.successInDeleteQuickNav , "success");
	}else{
		validateMessageBox(form.title.message, form.msg.listViewNotPresent , "info");
	}
}





function showJspContent(contentLink,container){
	var listViewParam="contentLink="+contentLink+"&container="+container;
	document.location.href = "#bpm/homePage/showJspContent";
	_execute('target',listViewParam);
	
}



function hello(menuName, urlType, menuUrl, tableId){
	
	//resetSubSideMenu();
	OpenInNewTab(menuUrl);
	//alert("helloooo "+menuUrl);
	//checkMenu(menuName, urlType, "google.com", tableId);
	
	//document.location.href = menuUrl;
}
function OpenInNewTab(url)
{
	//alert("new "+url);
 var win=window.open(url, '_blank');
 win.focus();
}
