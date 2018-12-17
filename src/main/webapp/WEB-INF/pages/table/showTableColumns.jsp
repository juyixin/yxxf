<%@ include file="/common/taglibs.jsp" %>
<script>
$(document).ready(function(){ 
	var closeIcon = $("a.ui-dialog-titlebar-close");
	$(closeIcon).css("display","none");
	var dataList = $("#tableColumnList");
	dataList.empty();
	<c:forEach items="${tableDetails}" var="table" varStatus="status">
		<c:if test="${not empty table.columnName}">
			var selectOption = document.createElement("OPTION");
			selectOption.text = "${table.columnName}";
			selectOption.value = "${table.columnName}";
			selectOption.id = "${table.id}";
			dataList.append(selectOption);
		</c:if>
	</c:forEach>;
	setDefaultForeignKeyDetails();
});

function setDefaultForeignKeyDetails(){

	     var tableDataList = $('#tableColumnList');
	     var tableVal = $(tableDataList).find('option[value="ID"]');
	 
    	var parentDivId = "#"+"${parentDivId}";
    	if(parentDivId == '#sub_table_names'){
    		var parentTableId = "${subTableId}";
    	}else if(parentDivId == '#parent_table_names'){
    		var parentTableId = "${tableId}";
    	}
    	var parentTableVal = $(parentDivId).find('li[id="'+parentTableId+'"]');
    	 if(tableVal.length != 0){
	    	parentTableVal.attr("foreignKeyColumnId", tableVal.attr('id'));
	    	parentTableVal.attr("foreignKeyColumnName", "ID");
	     }
}

function setTableForeignKeyDetails(){
	 var columnName =  document.getElementById("tableColumn").value.trim();
	 if(columnName != ""){
	     var tableDataList = $('#tableColumnList');
	     var tableVal = $(tableDataList).find('option[value="' + columnName + '"]');
	     if(tableVal.length == 0){
	    	 validateMessageBox(form.title.message,"Choose the existing table.","info",false);
	     }else{
	    	var parentDivId = "#"+"${parentDivId}";
	    	if(parentDivId == '#sub_table_names'){
	    		var parentTableId = "${subTableId}";
	    	}else if(parentDivId == '#parent_table_names'){
	    		var parentTableId = "${tableId}";
	    	}
	    	var parentTableVal = $(parentDivId).find('li[id="'+parentTableId+'"]');
	    	 if(tableVal.length != 0){
		    	parentTableVal.attr("foreignKeyColumnId", tableVal.attr('id'));
		    	parentTableVal.attr("foreignKeyColumnName", columnName);
		    	validateMessageBox(form.title.success,"Foreign Key selected successfully.","success",false);
		    	closeSelectDialog('tableRelationForeignKey');
	    	 }else{
	    		 validateMessageBox(form.title.error, "Error in foreign key selection." ,"error",false);
	    	 }
	     }
	 }
}
</script>
<div align="center" class="padding20">
	 <p><span class="pad-R10"><fmt:message key="module.title.selectColumn"/> </span><input type="text" id="tableColumn" list="tableColumnList" autocomplete="off" required="required"/></p>
	 <datalist id="tableColumnList"></datalist>
</div>
<div align="center">
	<input type="submit" value="save" id="save" class="btn btn-primary normalButton padding0 line-height0" onclick="setTableForeignKeyDetails();" />
	<input type="button" value="cancel" id="cancel" class="btn btn-primary normalButton padding0 line-height0"/>
</div>