<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="holiday.title" /></title>
</head>
<style>

.booked{
	background-color: #f7c1c1;
}

.specialDay{
	background-color: #33CC66 !important;
}

</style>
<script type="text/javascript">
var holidaysMap = {};
var weekEndList = [];
var isEdit = "${isEdit}";

$(function() {
	loadValueForYears();
	var cur_year=new Date().getFullYear();
	if(isEdit){
		$("#pickerYears").val("${currentYear}");
		cur_year="${currentYear}";
	}
	
	$("#currentYear").val(cur_year);
    
	$( "#datepicker" ).datepicker({
		numberOfMonths: [3,4],
		changeMonth: false,
		changeYear: false,
		//showButtonPanel: true,
		//hideIfNoPrevNext: true,
		//stepMonths: 2,
		direction: false,
		//duration: '' ,
		minDate: new Date(cur_year-1,00,01),
		maxDate: new Date(cur_year+1,00,01),
		defaultDate: new Date(cur_year,00,01),
		//selectMultiple:false,
		//beforeShowDay: showBooked,
		onSelect: function(value, date) {
			var day1 = $("#datepicker").datepicker('getDate').getDate();                 
            var month1 = $("#datepicker").datepicker('getDate').getMonth();             
            var year1 = $("#datepicker").datepicker('getDate').getFullYear();
            var fullDate = year1 + "-" + month1 + "-" + day1;
            
            if (holidaysMap[fullDate]!= undefined) {
            	delete holidaysMap[fullDate];
            }else{
            	holidaysMap[fullDate]="holiday";
            }
          	document.getElementById("selectedDateJson").value= JSON.stringify(holidaysMap);
   			showHolidays();
       }
	});
});

if(isEdit=="true"){
	document.getElementById("selectedDateJson").value= '${selectedDateList}';
	var holidaysMapString='${holidaysMapString}';
	var holidaysString=holidaysMapString.split(',');
	for(var index=0;index<holidaysString.length;index++){
		var holidayMapArray = holidaysString[index].split(' ');
		var tempKey=holidayMapArray[0];
		var tempValue="";
		if(holidayMapArray.length>2){
			for(var valueIndex=1;valueIndex<holidayMapArray.length;valueIndex++){
				tempValue=tempValue+holidayMapArray[valueIndex]+" ";
			}
		}else{
			tempValue=holidayMapArray[1];
		}
		holidaysMap[tempKey]=tempValue.trim();
	}
	var conunt=0;
	for(key in holidaysMap){
		conunt++;
	}
	
	var selectedWeekEnds='${selectedWeekEnds}';
	var selectedWeeks=selectedWeekEnds.split(',');
	for(var index=0;index<selectedWeeks.length;index++){
		$("#"+selectedWeeks[index]).prop("checked", true);
	}
	showHolidays();
}


function loadValueForYears(){
	var curYear=new Date().getFullYear();
	$("#pickerYears").val(curYear);
	$("#pickerYears").empty();
	var minYears=curYear-5;
	var maxYears=curYear+10;
	for(var years=minYears;years<maxYears;years++){
		$("#pickerYears").get(0).options[$("#pickerYears").get(0).options.length] = new Option(years, years);
	}
}

function showBooked(id) {
	var element = document.getElementById(id).value;
	$('#datepicker').datepicker('option', 'beforeShowDay', function(date){
		var currentFullYear = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDate();
		var day = date.getDay();
		if(day == element){
			if(document.getElementById(id).checked){
				if (holidaysMap[currentFullYear]== undefined) {
					holidaysMap[currentFullYear]="Weekend";
				}	
			}else{
				if (holidaysMap[currentFullYear]!= undefined && holidaysMap[currentFullYear]=="Weekend") {
					delete holidaysMap[currentFullYear];
				}
			}
		}
		return [true,''];
	});
	
	document.getElementById("selectedDateJson").value= JSON.stringify(holidaysMap);
	showHolidays();
	return false;
}

function showHolidays() {
	$('#datepicker').datepicker('option', 'beforeShowDay', function(date){
		var currentFullYear = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDate();
		var holidayCss = '';
		if (holidaysMap[currentFullYear]!=undefined) {
			if(holidaysMap[currentFullYear]=="Weekend"){
				holidayCss = "booked";
				return [true, holidayCss, holidaysMap[currentFullYear]];
			}else{
				holidayCss = "specialDay";
				return [true, holidayCss, holidaysMap[currentFullYear]];
			}
		}
		return [true, holidayCss];
	});
	$("#datepicker").datepicker("refresh");
	return false;
}

function loadCurrentYearHolidays(){
	var selectedYear=$("#pickerYears").val();
	var params='selectedYear='+selectedYear;
	document.location.href = "#bpm/holiday/holidayForm";
 	_execute('target',params);
}

function getSpecialDayReason(){
	var hasDataStatus=false;
	var splLeaveStatus=false;
	var specialDayReasonList=[];
	
	for(key in holidaysMap ){
		hasDataStatus=true;
		if(holidaysMap[key]!="Weekend"){
			splLeaveStatus=true;
			var specialDayReasonMap={};
			var jsDate=key;
			var dateArray=jsDate.split('-');
			var normalDate= dateArray[0]+'-'+(parseInt(dateArray[1])+1)+'-'+dateArray[2];
			specialDayReasonMap["header"]=normalDate;
			specialDayReasonMap["type"]="text";
			specialDayReasonMap["id"]=key;
			specialDayReasonMap["name"]=key;
			specialDayReasonMap["value"]=holidaysMap[key];
			specialDayReasonList.push(specialDayReasonMap);
		}
	}
	
	if(!hasDataStatus){
		validateMessageBox(form.title.validation, form.msg.empty , form.title.info);
	}else{
		if(splLeaveStatus){
			//shorting map
			specialDayReasonList = _.sortBy(specialDayReasonList, function(item){return item.header});
			
			$.msgBox({ 
				type: "prompt",
				title: "Holidays Description",
			    inputs: specialDayReasonList,
			    height:400,
			    buttons: [
			    { value: "Ok" }, {value:"Cancel"}],
			    success: function (result, values) {
			    	if(result == "Ok"){
			    		for(var index=0;index<values.length;index++){
			    			if(values[index].value.trim()!=""){
			    				holidaysMap[values[index].name]=values[index].value.trim();
				    		}
			    		}
			    		document.getElementById("selectedDateJson").value= JSON.stringify(holidaysMap);
			    		//showHolidays();
			    		var ajaxLoaderDiv = new ajaxLoader(document.getElementById('ajax_loader'));
			    		$("#holiday").ajaxSubmit(function(template){
			    			 	 	$("#target").html(template);
							 		$("#target").show(); 
							 });
			    		if (ajaxLoaderDiv) ajaxLoaderDiv.remove();
			    	}
			    }
			});
						
		}else{
				$("#holiday").ajaxSubmit(function(template){ 
				 	$("#target").html(template);
			 		$("#target").show(); 
	 			 });
		}
	}
	
}

</script>
<div class="span12 box">
<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2 id="holidaySetting" style="color:#4D4D4D; margin:2px;"><fmt:message key="holidaySetting.title"/></h2>
	<%@ include file="/common/messages.jsp"%>
		<form:form id="holiday" commandName="holiday" method="post" action="bpm/holiday/saveHoliday" autocomplete="off" modelAttribute="holiday" cssClass="form-horizontal">
			 <fieldset class="control-group">
		  		<eazytec:label styleClass="control-label style3 style18" key="holiday.year"/>
	            <div class="controls">
	            	<div id="selectYears"><select id="pickerYears" onchange="loadCurrentYearHolidays();" ></select></div>
	            </div>
	        </fieldset>	
	        
			<fieldset class="control-group">
		  		<eazytec:label styleClass="control-label style3 style18" key="holiday.weekEnd"/>
	            <div class="controls">
	            <label class="checkbox inline style3 style18">				
					<input type="checkbox" name="sunday" id="sunday" value="0" onclick="showBooked('sunday')"><fmt:message key="schedule.sunday"/>
				</label>
	            <label class="checkbox inline style3 style18">
					<input type="checkbox" name="monday" id="monday" value="1" onclick="showBooked('monday')"><fmt:message key="schedule.monday"/>
				</label>
	            <label class="checkbox inline style3 style18">				
					<input type="checkbox" name="tuesday" id="tuesday" value="2" onclick="showBooked('tuesday')"><fmt:message key="schedule.tuesday"/>
				</label>
	            <label class="checkbox inline style3 style18">
					<input type="checkbox" name="wednesday" id="wednesday" value="3" onclick="showBooked('wednesday')"><fmt:message key="schedule.wednesday"/>
				</label>
	            <label class="checkbox inline style3 style18">				
					<input type="checkbox" name="thursday" id="thursday" value="4" onclick="showBooked('thursday')"><fmt:message key="schedule.thursday"/> 
				</label>
	            <label class="checkbox inline style3 style18">
					<input type="checkbox" name="friday" id="friday" value="5" onclick="showBooked('friday')"><fmt:message key="schedule.friday"/>			</label>
	            <label class="checkbox inline style3 style18">				
					<input type="checkbox" name="saturday" id="saturday" value="6" onclick="showBooked('saturday')"><fmt:message key="schedule.saturday"/> 
				</label>												
	            </div>
	        </fieldset>	     
	        <fieldset class="control-group">
		  		<eazytec:label styleClass="control-label style3 style18" key="holiday.selectHoliday"/>
		  		
	            <div class="controls">
	            <!-- <div style="width:75%;"> -->
	            	<div id="datepicker"></div>
	           <!--  </div> -->
	            </div>
	          
	        </fieldset>	 
	        <input type="hidden" id="selectedDateJson" name="selectedDateJson"/>
	        <input type="hidden" id="currentYear" name="currentYear"/>
	        <!--  <center> -->
	        <div class="form-actions no-margin" align="center">	
	         <button type="button" class="btn btn-primary" name="save" id="saveButton" onclick="return getSpecialDayReason();">
                              <fmt:message key="holiday.save"/>
    		</button>
    		</div>
    		<!-- </center> -->
		</form:form>
</div>

<v:javascript formName="module" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
