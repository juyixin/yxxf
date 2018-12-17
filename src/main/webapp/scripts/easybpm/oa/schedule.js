/**
 * script for schedule and reschedule events
 * 
 * @auther mathi
 */

//method to clear the previous popup windows
function clearPreviousPopup(){
	$("div#userPopupDialog").empty();
	$("div#departmentPopupDialog").empty();
	$("div#groupPopupDialog").empty();
	$("div#popupDialog").empty();
	$("#popupDialog").dialog("destroy");
	$("#involveUsersPopupDialog").dialog("destroy");
	$("#userPopupDialog").dialog("destroy");
	$("#userPopupDialogManager").dialog("destroy");
	$("#userPopupDialogSecretary").dialog("destroy");
	$("#departmentPopupDialog").dialog("destroy");
	$("#partTimeDepartmentPopupDialog").dialog("destroy");
	$("#groupPopupDialog").dialog("destroy");
	$("div#doc_view_dialog").empty();
	$("#doc_view_dialog").dialog("destroy");
}

//method to show calendar
function showCalender(calenderId,data){
	//var jsonData = eval(data);
	var date = new Date();
	var calendar = $('#'+calenderId).fullCalendar({
		style: 'margin:10px',
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'agendaDay,agendaWeek,month'
		},
		selectable: true,
		selectHelper: true,
		select: function(start, end, allDay) {
			if ( (start < date && start.getDate() != date.getDate()) 
					|| (start < date && start.getMonth() < date.getMonth() && start.getDate() == date.getDate())
					|| (start < date && start.getYear() < date.getYear() && start.getDate() == date.getDate()) ) {
				$.msgBox({
		   		    title:form.title.message,
		   		    content:"不能在过去的时间创建事件",
		   		});
			} else{
				var startDate = dateFormat(start, 'YYYY-MM-DD');
				var endDate = dateFormat(end, 'YYYY-MM-DD');
				//startDate = startDate+' 00:00';
				//endDate = endDate+' 23:59';
				createEvent(startDate, endDate, 'calendar'); //create new event
			}
			calendar.fullCalendar('unselect');
		},
		editable: true,
		eventRender: function(event, element, view)
        {
			/*if(event.start.getMonth() !== view.start.getMonth()) { 
				return false; 
			}*/
			element.bind('click', function(){
	        			editEvent(event.id,'calendar'); //edit event when click the event
			});
        },
      //select already created events in calendar
        events:function(start, end, callback) {
			var startDate = dateFormat(start, 'YYYY-MM-DD');
			var endDate = dateFormat(end, 'YYYY-MM-DD');
			var jsonData = getEventOwnerCalendarDatas(startDate,endDate);
			callback(jsonData)
        },
		buttonText: {
			today: '今天',
			month: '月视图',
			week: '周视图',
			day: '天视图'
	    }
	});
}

//method to create event
function createEvent(startDate, endDate, from){
	var eventOwner = $('#eventOwner').val();
	clearPreviousPopup();
	document.location.href = "#bpm/oa/eventForm";
	_execute('popupDialog','eventOwner='+eventOwner+'&startDate='+startDate+'&endDate='+endDate+'&from='+from);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
	   width: 750,
	   height: 580,
	   modal: true,
	   position: [($(window).width() / 4), 50],
	   title: '新建事件'
     });
	 $myDialog.dialog("open");
}

//method to edit event
function editEvent(eventId,from){
	clearPreviousPopup();
	var params = 'eventId='+eventId+'&from='+from;
	document.location.href = "#bpm/oa/editEvent";
	_execute('popupDialog',params);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
       width: 750,
       height: 580,
       modal: true,
       position: [($(window).width() / 4), 10],
       title: '修改事件'
     });
	 $myDialog.dialog("open");
}

//method to add event to calendar after save the event
function addEventToCalandar (eventId, title, start, end, allDayValue){
	$('#calendar').fullCalendar('removeEvents', eventId);
	$('#calendar').fullCalendar('renderEvent',
		{
			id : eventId,
			title  : title,
	        start  : start,
	        end    : end,
			allDay: allDayValue
		},
		true 
	);
}

//method to set calendar height 
function getFCHeight(){
	 var targetHeight = $('#target').height();
	 height = parseInt(targetHeight)-91;
	 return height;
}

//method for edit grid data
function _showEditSchedule(cellValue, options, rawObject){
	  return '<a id="editEvent" href="javascript:void(0);" data-role="button" data-icon="" data-theme="b" onclick="editEvent(\''+rawObject.id+'\',\'grid\');"><b>'+rawObject.eventName+'</b></a';
}

//method to change the start date format in grid
function _changeStartDateFormat(cellValue, options, rawObject){
	var startDate = cellValue.split(" ");
	return startDate[0];
}

//method to change the end date format in grid
function _changeEndDateFormat(cellValue, options, rawObject){
	var endDate = cellValue.split(" ");
	return endDate[0];
}

//method to show popup for select event owner
function showEventOwnerTree(title, id, selectType){
	clearPreviousPopup();
	var params = 'title='+title+'&id='+id+'&selectType='+selectType;
	document.location.href = "#bpm/oa/eventOwnerTree";
	_execute('popupDialog',params);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       modal: true,
                       title: title
	             });
	 $myDialog.dialog("open");
}

//method to get events that are already created
function getEventCalendarDatas(){
	var data = [];
	$.ajax({
		url: 'bpm/oa/getEvents',
        type: 'GET',
        data: 'eventOwner='+'',
        dataType: 'json',
        async : false,
		success : function(response) {
			if(response.length > 0){
				data=response;
			}
		},
		error : function(response){
			$.msgBox({
				title:form.title.error,
				content:"Problem in getting old events",
				type: "error"
			});
		}
	});
	return data;
}

//method to get events that are already created
function getEventOwnerCalendarDatas(startDate,endDate){
	var eventOwner = $('#eventOwner').val();
	var data = [];
	$.ajax({
		url: 'bpm/oa/getEvents',
        type: 'GET',
        data: 'eventOwner='+eventOwner+'&startDate='+startDate+'&endDate='+endDate,
        dataType: 'json',
        async : false,
		success : function(response) {
			if(response != null && response.length > 0){
				data=response;
			}
		},
		error : function(response){
			$.msgBox({
				title:form.title.error,
				content:"Problem in getting old events",
				type: "error"
			});
		}
	});
	return data;
}

/**
* Format date as a string
* @param date - a date object (usually "new Date();")
* @param format - a string format, eg. "DD-MM-YYYY"
*/
function dateFormat(date, format) {
    // Calculate date parts and replace instances in format string accordingly
    format = format.replace("DD", (date.getDate() < 10 ? '0' : '') + date.getDate()); // Pad with '0' if needed
    format = format.replace("MM", (date.getMonth() < 9 ? '0' : '') + (date.getMonth() + 1)); // Months are zero-based
    format = format.replace("YYYY", date.getFullYear());
    return format;
}

function updateEventsToCalendar(data){
	var calendarDate = $('#calendar').fullCalendar('getDate');
	if(calendarDate.length != 0){
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar('addEventSource',data);
		$('#calendar').fullCalendar('rerenderEvents',data);
		$('#calendar').fullCalendar( 'gotoDate', calendarDate.getFullYear(), calendarDate.getMonth());
	}
}

function updateCalendarEvents(data){
	var calendarDate = $('#calendar').fullCalendar('getDate');
	if(calendarDate.length != 0){
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar('addEventSource',data);
		$('#calendar').fullCalendar('rerenderEvents',data);
		$('#calendar').fullCalendar( 'gotoDate', calendarDate.getFullYear(), calendarDate.getMonth());
	}
}

function deleteScheduledEvent(eventId,eventOwner){
	var data = [];
	$.ajax({
		url: 'bpm/oa/deleteEvent',
        type: 'GET',
        data: 'eventId=' + eventId + '&eventOwner=' + eventOwner,
        dataType: 'json',
        async : false,
		success : function(response) {
					if(response != null && response.length > 0){
						data=response;
					}
					//updateCalendarEvents(data);
					$('#calendar').fullCalendar('removeEvents');
					$('#calendar').fullCalendar( 'refetchEvents' );
					closeSelectDialog('popupDialog');
					$.msgBox({
						title:form.title.success,
						content:"事件删除成功",
						type: "success"
					});
		},
		error : function(response){
					$.msgBox({
						title:form.title.error,
						content:"Problem in deleting events",
						type: "error"
					});
		}
	});
}

//Schedules Delete
function deleteSchedules(){
	var rowid =  gridObj.getGridParam('selarrrow');
    var deletePermission = true;
	var currentUserId = document.getElementById('currentUserId').value;
	var userRole=$("#currentUserRoles").val().split(",");
    var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
    if(rowid.length !=0 ){
    		if ( !Array.prototype.forEach ) {
    			for(var i=0;i<rowid.length;i++){
    				var createdBy = gridObj.jqGrid ('getCell', rowid[i], 'createdBy');
				if(createdBy != currentUserId && isAdmin == false){
					deletePermission = false;
				}
    			}
    		} else  {
			rowid.forEach(function(item) {
				var createdBy = gridObj.jqGrid ('getCell', item, 'createdBy');
				if(createdBy != currentUserId && isAdmin == false){
					deletePermission = false;
				}
			});
		}
	}
	if(deletePermission){
		deleteAllSchedulesConfirm(rowid,'grid');
	}else{
		$.msgBox({
   		    title:form.title.message,
   		    content:"没有权限，无法删除",
   		});
		return false;
	}
	if(rowid.length ==0){
		$.msgBox({
   		    title:form.title.message,
   		    content:"请选择要删除的记录",
   		});
	}
}

function deleteAllSchedulesConfirm(eventIds){
	params = 'eventIds='+eventIds;
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除选中的记录吗?",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/oa/deleteSelectedSchedules";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function refetchEvents(selectedValues){
	$('#eventOwner').val(selectedValues);
	$('#calendar').fullCalendar('removeEvents');
	$('#calendar').fullCalendar( 'refetchEvents' );
}

//event form validation
function validateEventForm(form){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var remindLength = $("#remindBefore").val().length;
	var eventNameLength = $("#eventName").val().length;
	if(eventNameLength > 255) {
		$.msgBox({
			title : form.title.message,
			content : "Event Name Should not  be exeeced 255 characters in Length",
		});
		return false;
	}
	if(remindLength > 10 || isNaN($("#remindBefore").val())){
		$.msgBox({
   		    title:form.title.message,
   		    content:"Please give valid 'Remind Before'",
   		});
		return false;
	}
	
	if($('#users').val() == null || $('#users').val() == ''){
		$.msgBox({
   		    title:form.title.message,
   		    content:"Please select atleast one user",
   		});
		return false;
	}
	
	if(startDate > endDate){
		$.msgBox({
   		    title:form.title.message,
   		    content:"End Date should be greated than Start Date",
   		});
		return false;
	}else{
		var from = $('#from').val();
		if ( from == 'grid') {
			_execute('target','');
		} else {
			_execute('popupDialog','');
		}
		return true;
	}
}

function showRepeats(eventType) {
	if(eventType == 'once'){
		$('#dailyRepeatDiv').hide();
		$('#weeklyRepeatDiv').hide();
		$('#monthlyRepeatDiv').hide();
		$("#repeatEveryDays").attr("disabled", "disabled");	
		$("#repeatEveryWeekDays").attr("disabled", "disabled");	
		$("#repeatEveryMonths").attr("disabled", "disabled");	
		
	} else if(eventType == 'daily'){
		$('#dailyRepeatDiv').show();
		$('#weeklyRepeatDiv').hide();
		$('#monthlyRepeatDiv').hide();
		$("#repeatEveryWeekDays").attr("disabled", "disabled");	
		$("#repeatEveryMonths").attr("disabled", "disabled");	
		$("#repeatEveryDays").removeAttr("disabled");	
		
	} else if(eventType == 'weekly'){
		$('#dailyRepeatDiv').hide();
		$('#weeklyRepeatDiv').show();
		$('#monthlyRepeatDiv').hide();
		$("#repeatEveryDays").attr("disabled", "disabled");	
		$("#repeatEveryMonths").attr("disabled", "disabled");	
		$("#repeatEveryWeekDays").removeAttr("disabled");	
		
	} else if(eventType == 'monthly'){
		$('#dailyRepeatDiv').hide();
		$('#weeklyRepeatDiv').hide();
		$('#monthlyRepeatDiv').show();
		$("#repeatEveryDays").attr("disabled", "disabled");	
		$("#repeatEveryWeekDays").attr("disabled", "disabled");	
		$("#repeatEveryMonths").removeAttr("disabled");	
		
	}
	
		
}

function loadDateField(id){
	$( '#'+id ).datepicker({
		dateFormat: 'yy-mm-dd',
		minDate: 0
	});
}

function loadDateFieldWithImg(id){
	$( '#'+id ).datepicker({
		dateFormat: 'yy-mm-dd',
		buttonImage:'/images/easybpm/form/rbl/_cal.gif',
		showOn: "button",
		minDate: 0
	});
}

function loadDateMonthYearField(id){
	$( '#'+id ).datepicker({
		dateFormat: 'dd-mm-yy',
		minDate: 0
	});
}

function loadTimeField(id){
	$('#'+id).timepicker({ 
		'timeFormat': 'HH:mm' 
	});
}

function loadDateTimeField(id){
	$( '#'+id ).datetimepicker({
		time: 'HH:mm',
		minDate: 0
	});
}

//method to load date and time to the text field
function loadDateTimeFieldEazytecFormat(id){
	$( '#'+id ).datetimepicker({
		dateFormat: 'yy-mm-dd',
		time : 'HH:mm:ss',
		minDate: 0,
		showOn: "button",
		buttonImage:'/images/easybpm/form/rbl/_cal.gif'
	});
}
