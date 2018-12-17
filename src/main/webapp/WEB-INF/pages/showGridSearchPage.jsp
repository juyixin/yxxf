<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>

<%@ include file="/common/taglibs.jsp"%>
<style>
<!--
tr.row-height{
    height:5px;
}
-->
</style>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="row-fluid">
				<div class="span2">Process</div>
				<div class="span4">
					<select class="large" id="processName" datadictionary="" name="processName" >
					</select>
				</div>
				<div class="span2">Department</div>
				<div class="span4">
					<select class="large" id="departmentId" name="departmentId" >
					</select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">Start Date</div>
				<div class="span4">
					<input type="text" class="large datetimepicker" id="startDate" value="" name="startDate" />
				</div>
				<div class="span2">End Date</div>
				<div class="span4">
					<input type="text" class="large datetimepicker" id="finishDate" value="" name="finishDate" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12"></div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<table width="100%">
					 	<tr>
					        <td colspan="4" align="center">
					            <button type="button" onClick="_advancedSearch('${listViewColumns[0].listView.viewName}');" class="btn btn-primary normalButton padding0 line-height0">
					                 <fmt:message key="button.search"/>
					             </button>
					            <button type="button" onClick="closeSelectDialog('popupDialog');" class="btn btn-primary normalButton padding0 line-height0">
					                 <fmt:message key="button.cancel"/>
					             </button>
					        </td>
					    </tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
function _advancedSearch(){
    var advanceSearchParamsMap = "";
    var startDate="";
    var finishDate = "";
    if($("#startDate").val().contains("PM") ||  $("#startDate").val().contains("pm") || $("#startDate").val().contains("AM") ||$("#startDate").val().contains("am")){
    	startDate = $("#startDate").val().slice(0,-3);
    }else {
    	startDate = $("#startDate").val();
    }
    if($("#finishDate").val().contains("PM") ||  $("#finishDate").val().contains("pm") || $("#finishDate").val().contains("AM") || $("#finishDate").val().contains("am")){
      	finishDate = $("#finishDate").val().slice(0,-3);
    } else {
    	finishDate = $("#finishDate").val();
    }
    advanceSearchParamsMap = advanceSearchParamsMap + "{'startDate':'"+startDate+"',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'finishDate':'"+finishDate+"',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'departmentId':'"+$("#departmentId").val()+"',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'processName':'"+$("#processName").val()+"',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'isAdvancedSearch':'true'}";
    var searchParams = "[" + advanceSearchParamsMap + "]";  
    reloadProcessFlowStatiticsGrid(searchParams);
}

$(function(){
    $(".datepicker").datepicker({
        showOn: 'button',
        //buttonText: 'Show Date',
        buttonImageOnly: true,
        buttonImage: '/images/easybpm/form/rbl/_cal.gif',
        dateFormat: 'yy-mm-dd',
        maxDate: 0,
        constrainInput: true
    });
     $(".datetimepicker").datetimepicker({
        showOn: 'button',
        //buttonText: 'Show Date',
        buttonImageOnly: true,
        buttonImage: '/images/easybpm/form/rbl/_cal.gif',
        dateFormat: 'yy-mm-dd',
        timeFormat: 'hh:mm TT',
        maxDate: 0,
        constrainInput: true
    });
    
    loadDepartmentCombo();
    loadProcessCombo();
});


function loadDepartmentCombo(){
	var element_id = "departmentId";
	$.ajax({
		url: 'bpm/admin/getAllDepartmentNames',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success: function(data) {
			$( "#"+element_id ).empty();
			if(data){
	            $.each(data, function(index, item) {
	                $("#"+element_id).get(0).options[$("#"+element_id).get(0).options.length] = new Option(item.name, item.id);
	            });
	           // $("#"+element_id).prepend("<option value='' selected='selected'></option>");
			}
	    }
	});
}

function loadProcessCombo(){
	var element_id = "processName";
	$.ajax({
		url: 'bpm/admin/getAllProcess',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success: function(data) {
			$( "#"+element_id ).empty();
			if(data){
	            $.each(data, function(index, item) {
	                $("#"+element_id).get(0).options[$("#"+element_id).get(0).options.length] = new Option(item.label, item.label);
	            });
	           // $("#"+element_id).prepend("<option value='' selected='selected'></option>");
			}
	    }
	});
}
</script>