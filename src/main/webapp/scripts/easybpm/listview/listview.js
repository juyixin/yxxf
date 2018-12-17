/**
 * @author Mahesh
 * 
 */
/*var createColumnsProperty = [];
var deleteColumnsProperty = [];

var createButtonsProperty = [];
var deleteButtonsProperty = [];
var createGroupSettingProperty = [];
var deleteGroupSettingProperty = [];*/
var _advanceSearchParams = "";
var _advancedSearchViewName = "";
var whereParamMap=[];
var listViewParams=[];
var advanceSearchParams = [];

var _row_id = 0;
var _button_row_id = 0;
var __gs_row_id = 0;
function getColumsPropety(listViewId){
	$.ajax({
		url: '/bpm/listView/columnsProperty?listViewId='+listViewId,
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		success : function(response) {
			if(response.length > 0){
				var gridData = eval(response);
				$('#_LIST_VIEW_COLUMNS_PROPERTY').jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
			}
		}
	});
}

function columnsPropertyGrid(){
	 	$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid({
	  		data: [],
			datatype: "local",
			width: listViewColumnsPropertyGridWidth(), 
			height: listViewColumnsPropertyGridHeight(), 
		    colNames:['Id', '列标题', '数据域', '上移','下移', 'Column Width','排序','Replace Words','Is Sort','Is Advanced Search','Is Simple Search','Text Align','Is Hidden','Render Event','Render Event Name','Comment','Is Group By','Field Type', 'Data Dictionary', 'Is Range','Column Type'],
		    colModel :[{
				    name: 'id'
				 	,index: 'id'
				 	,width: 100
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
				    name: 'columnTitle'
				 	,index: 'columnTitle'
				 	,width: 100
				 	,align: 'left'
				 	,hidden: false  				 	
			 	},{
				    name: 'dataFields'
				 	,index: 'dataFields'
				 	,width: 100
				 	,align: 'left'
				 	,hidden: false  				 	
			 	},{
			 		
				    name: 'orderUpImg'
				 	,index: 'orderUpImg'
				 	,width: 40
				 	,align: 'center'
				 	,hidden: false  	
					,formatter: _moveListViewColumnGridRowUpImage			 	
			 	},{
				    name: 'orderDownImg'
				 	,index: 'orderDownImg'
				 	,width: 40
				 	,align: 'center'
				 	,hidden: false
					,formatter: _moveListViewColumnGridDownImage  				 	
			 	},{
				    name: 'width'
				 	,index: 'width'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
				    name: 'orderBy'
				 	,index: 'orderBy'
				 	,width: 35
				 	,align: 'center'
				 	,hidden: false  				 	
				},{
				    name: 'replaceWords'
				 	,index: 'replaceWords'
				 	,width: 50
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
				    name: 'isSort'
				 	,index: 'isSort'
				 	,width: 80
				 	,align: 'center'
				 	,hidden: true  				 	
			 	},{
				    name: 'isAdvancedSearch'
				 	,index: 'isAdvancedSearch'
				 	,width: 80
				 	,align: 'center'
				 	,hidden: true  				 	
			 	},{
				    name: 'isSimpleSearch'
				 	,index: 'isSimpleSearch'
				 	,width: 80
				 	,align: 'center'
				 	,hidden: true  				 	
			 	},{
				    name: 'textAlign'
				 	,index: 'textAlign'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
				    name: 'isHidden'
				 	,index: 'isHidden'
				 	,width: 80
				 	,align: 'center'
				 	,hidden: true  				 	
			 	},{
				    name: 'onRenderEvent'
				 	,index: 'onRenderEvent'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
				    name: 'onRenderEventName'
				 	,index: 'onRenderEventName'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
				    name: 'comment'
				 	,index: 'comment'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
			 		name: 'isGroupBy'
				 	,index: 'isGroupBy'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
			 		name: 'dataFieldType'
				 	,index: 'dataFieldType'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
			 	},{
			 		name: 'dataDictionary'
				 	,index: 'dataDictionary'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
				 },{
			 		name: 'isRange'
				 	,index: 'isRange'
				 	,width: 80
				 	,align: 'left'
				 	,hidden: true  				 	
				 },{
				 	name: 'columnType'
					,index: 'columnType'
					,width: 80
					,align: 'left'
					,hidden: true  				 	
				}],
		    pager: '#_LIST_VIEW_COLUMNS_PROPERTY_PAGER',
		    viewrecords: true,
		    gridview: true,
		    scroll:1,
		   	hidegrid: false,
				//sortname: 'orderBy'
				//sortorder: 'asc',
		    loadComplete: function() {
	    		$("#_LIST_VIEW_COLUMNS_PROPERTY tr.jqgrow:odd").css("background", "#f0f0f0");
	    	},
	    	ondblClickRow: function(row_id,iRow) {
				
	    		_row_id = row_id;
	    		var data = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getRowData',row_id);  
	    		$("#columnPropertyId").val(data.id);
    			$("#columnTitle").val(data.columnTitle);
    			$("#dataFields").val(data.dataFields);
    			$("#columnWidth").val(data.width);
    			$("#dataDictionary").val(data.dataDictionary);
    			if(data.dataDictionary != "null"){
    				setDataDictionaryName(data.dataDictionary);
    			}else{
    				$("#select_data_dictionary").val("");
    			}
    			$("#columnsOrder").val(data.orderBy);
    			$("#currentColumnsOrder").val(data.orderBy);
    			$("#otherName").val(data.otherName);
    			$("#replaceWords").val(data.replaceWords);
    			$("#isSort").prop('checked', eval(data.isSort));
    			$("#isAdvancedSearch").prop('checked', eval(data.isAdvancedSearch));
    			$("#isSimpleSearch").prop('checked', eval(data.isSimpleSearch));
    			$("#isRange").prop('checked', eval(data.isRange));
    			$("#textAlign"+data.textAlign).prop('checked', true);
    			$("#isHidden").prop('checked', eval(data.isHidden));
    			$("#onRenderEvent").val(data.onRenderEvent);
    			$("#onRenderEventName").val(data.onRenderEventName);
    			$("#comment").val(data.comment);
    			$("#isGroupBy").prop('checked', eval(data.isGroupBy));
    			$("#dataFieldType").val(data.dataFieldType);
    			$("#columnType").val(data.columnType);
    			if($("#isAdvancedSearch").is(':checked')){
    				$("#search_field_type").slideDown(1000);
    				if(data.dataFieldType == "select" || data.dataFieldType == "radio" || data.dataFieldType == "checkbox" || data.dataFieldType == "autocomplete"){
    					$("#data_dictionary_field").show();
    					$("#is_range_field").hide();
    				}else{
    					$("#data_dictionary_field").hide();
    					$("#is_range_field").show();
    				}
    			}else{
    				$("#search_field_type").slideUp(1000);
    				$("#data_dictionary_field").hide();
    				$("#is_range_field").hide();
    			}
	    	}
	 	});
		$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('navGrid','#_LIST_VIEW_COLUMNS_PROPERTY_PAGER',{del:false,edit:false,add:false, search:false, refresh:false},{},{},{});	

}

function _moveListViewColumnGridRowUpImage(cellValue, options, rawObject) {
	return '<img src="/images/go_up_small.png" onclick="moveRow(\'up\',\''
			+rawObject.orderBy+ '\',\'LIST_VIEW_COLUMNS_PROPERTY\',this)"/>';
}

function _moveListViewColumnGridDownImage(cellValue, options, rawObject) {
	return '<img src="/images/move_down_small.png" onclick="moveRow(\'down\',\''
			+rawObject.orderBy+ '\',\'LIST_VIEW_COLUMNS_PROPERTY\',this)"/>';
}

function moveRow(direction,orderId, gridName, currObj) {
   
	var currRow = parseInt($(currObj).closest('tr').index());

	//get the record count of the grid.
    var recordCount = $('#_'+gridName).getGridParam("records");
    var dataObj = $('#_LIST_VIEW_COLUMNS_PROPERTY')[0].p.data;
	//currRow = $('#_LIST_VIEW_COLUMNS_PROPERTY').jqGrid ('getCell', currRowId, 'orderBy');
   
    //get the selected row id. by default id will be numbered from 1,2,3
    // var currRow = $('#_DICTIONARY_LIST').getGridParam('selrow');
    var rowNoFrom = 0;
    var rowNoTo = 0;
	var nextRowId = "";
    var gridArrayList = new Array();
    //alert("currRow======="+currRow);
    // move up only if it is not a first row
    if (currRow > 1 && direction === "up") {
    	rowNoFrom = currRow;
        rowNoTo = currRow - 1;
		currRowId = document.getElementById("_LIST_VIEW_COLUMNS_PROPERTY").rows[currRow].id; // dataObj[currRow - 1].id;
		nextRowId = document.getElementById("_LIST_VIEW_COLUMNS_PROPERTY").rows[currRow - 1].id; //dataObj[currRow - 2].id;
	}else if(currRow < recordCount && direction === "down"){
		rowNoFrom = currRow;
		rowNoTo = parseInt(currRow) + 1;
		currRowId = document.getElementById("_LIST_VIEW_COLUMNS_PROPERTY").rows[currRow].id;//dataObj[currRow - 1].id;
		nextRowId = document.getElementById("_LIST_VIEW_COLUMNS_PROPERTY").rows[parseInt(currRow) + 1].id;//dataObj[parseInt(currRow)].id;
	}
	//alert("currRowId==="+currRowId+"===nexrRowId=="+nextRowId);
		
    var rowObjFrom = $('#_'+gridName).jqGrid('getRowData', currRowId);
    var rowObjTo = $('#_'+gridName).jqGrid('getRowData', nextRowId);
		
    var jsonRowObjFrom = eval(rowObjFrom);
    var jsonRowObjTo = eval(rowObjTo);
    /*var temp = 0;
    temp = jsonRowObjFrom.orderBy;		
    jsonRowObjFrom.orderBy = jsonRowObjTo.orderBy;
    jsonRowObjTo.orderBy = temp;*/
    
    var datarowFrom = "";
    var datarowTo =  "";
    var datarowFrom = {
            id: jsonRowObjFrom.id,
    		// columnId: jsonRowObjFrom.id,
            columnTitle	: jsonRowObjFrom.columnTitle,
            dataFields	: jsonRowObjFrom.dataFields,
    		orderBy: jsonRowObjTo.orderBy,
    		width : jsonRowObjFrom.width,
    		textAlign : jsonRowObjFrom.textAlign,
    		onRenderEvent : jsonRowObjFrom.onRenderEvent,
    		onRenderEventName : jsonRowObjFrom.onRenderEventName,
    		isHidden : jsonRowObjFrom.isHidden,
    		otherName : jsonRowObjFrom.otherName,
    		replaceWords : jsonRowObjFrom.replaceWords, 
    		isSort : jsonRowObjFrom.isSort,
    		isAdvancedSearch : jsonRowObjFrom.isAdvancedSearch,
    		isSimpleSearch : jsonRowObjFrom.isSimpleSearch,
    		comment : jsonRowObjFrom.comment,
    		isGroupBy : jsonRowObjFrom.isGroupBy,
    		dataFieldType : jsonRowObjFrom.dataFieldType,
    		version : jsonRowObjFrom.version,
    		active : jsonRowObjFrom.active,
    		dataDictionary : jsonRowObjFrom.dataDictionary,
    		isRange : jsonRowObjFrom.isRange,
    		columnType : jsonRowObjFrom.columnType

        };
        var datarowTo = {
            id: jsonRowObjTo.id,
    		//  columnId: jsonRowObjTo.id,
            columnTitle	: jsonRowObjTo.columnTitle,
            dataFields	: jsonRowObjTo.dataFields,
    		orderBy: jsonRowObjFrom.orderBy,
    		width : jsonRowObjTo.width,
    		textAlign : jsonRowObjTo.textAlign,
    		onRenderEvent : jsonRowObjTo.onRenderEvent,
    		onRenderEventName : jsonRowObjTo.onRenderEventName,
    		isHidden : jsonRowObjTo.isHidden,
    		otherName : jsonRowObjTo.otherName,
    		replaceWords : jsonRowObjTo.replaceWords, 
    		isSort : jsonRowObjTo.isSort,
    		isAdvancedSearch : jsonRowObjTo.isAdvancedSearch,
    		isSimpleSearch : jsonRowObjTo.isSimpleSearch,
    		comment : jsonRowObjTo.comment,
    		isGroupBy : jsonRowObjTo.isGroupBy,
    		dataFieldType : jsonRowObjTo.dataFieldType,
    		version : jsonRowObjTo.version,
    		active : jsonRowObjTo.active,
    		dataDictionary : jsonRowObjTo.dataDictionary,
    		isRange : jsonRowObjTo.isRange,
    		columnType : jsonRowObjTo.columnType

        };

	  $('#_'+gridName).jqGrid('setRowData', currRowId, datarowTo);
      $('#_'+gridName).jqGrid('setRowData', nextRowId, datarowFrom);

	  gridArrayList.push(datarowTo);
   	  gridArrayList.push(datarowFrom);
	  if(jsonRowObjTo.orderBy != undefined && jsonRowObjFrom.orderBy != undefined){
		  updateListViewColumnOrderNo(gridArrayList);   
  	  }

    
}

function updateListViewColumnOrderNo(gridArrayList) {
	
	$.ajax({
		url: 'bpm/listView/updateListViewColumnOrderNos',
        type: 'POST',
        data: 'listViewColumnJson='+JSON.stringify(gridArrayList),
        dataType: 'json',
        async : false,
        success : function(response) {
				data=response;
        },
		error : function(response){
				data=response;
				validateMessageBox(form.title.error, form.msg.cannotUpdateColumnOrder, false);
		}
	});
}

function gridJsonMapForListViewColumns(rowNo, jsonRowObj){
	
	return {
		id: rowNo,
		columnId: jsonRowObj.id,
      //  description: jsonRowObj.description,
       // name: jsonRowObj.name,
       // roleType: jsonRowObj.roleType,
        
        columnTitle	: jsonRowObj.columnTitle,
        dataFields	: jsonRowObj.dataFields,
				orderBy: jsonRowObj.orderBy
				
	}
}
function setDataDictionaryName(dataDictionaryId){
	$.ajax({
		url: 'bpm/admin/getDataDictById?dictionaryId='+dataDictionaryId,
	    type: 'GET',
		async: false,
		success : function(data) {
			var res = eval(data);
			$("#select_data_dictionary").val(res[0].name);
		}
	});
}

function _clearRecord(){
	$("#columnPropertyId").val("");
	$("#dataDictionary").val("");
	$("#columnTitle").val("");
	$("#dataFields").val("");
	$("#columnWidth").val(0);
	$("#columnsOrder").val(0);
	$("#otherName").val("");
	$("#replaceWords").val("");
	$("#isSort").prop('checked', true);
	$("#isAdvancedSearch").prop('checked', false);
	$("#isRange").prop('checked', false);
	$("#isSimpleSearch").prop('checked', true);
	$("#textAlignLeft").prop('checked', true);
	$("#isHidden").prop('checked', false);
	$("#onRenderEvent").val("");
	$("#onRenderEventName").val("");
	$("#comment").val("");
	$("#isGroupBy").prop('checked', false);
	$("#dataFieldType").val("");
	$("#columnType").val("");
	$("#search_field_type").hide();
	$("#data_dictionary_field").hide();
	$("#is_range_field").hide();
	$("#select_data_dictionary").val("");
	_row_id = 0;
	$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('resetSelection');
	$("#columnTitle").focus();
}

function _addRecord(){
	var colId = $("#columnPropertyId").val();
	var columnsOrder = $("#columnsOrder").val();	
	var currentColumnsOrder = $("#currentColumnsOrder").val();	
	var idVal = 0;
	var columnTitle = $("#columnTitle").val().trim();
    var rowCount = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getGridParam', 'records');
    var alreadyExist = false;
	if(columnTitle != ""){
		if(!isSpecialCharacterFound(columnTitle)){
			if(columnsOrder == "") {
				alreadyExist = true;
			} else {
				if(colId == "" && columnsOrder == 0) {
				 	//columnsOrder = parseInt(rowCount) + 1;
				 	columnsOrder = parseInt(findHighestOrder())+1;
				}else if(currentColumnsOrder != columnsOrder ) {
					alreadyExist = checkExistOrderNO(columnsOrder);
				}else {
						alreadyExist = false;
				}
			}
			if(!alreadyExist){
				//$("#columnsOrder").val();
				//alert("columnsOrder============="+columnsOrder);
				if(!isNameExist(columnTitle,'_LIST_VIEW_COLUMNS_PROPERTY')){
					 var selected = $("input[type='radio'][name='textAlign']:checked");
					 var datarow = { id: $("#columnPropertyId").val(), columnTitle: columnTitle, dataFields: $("#dataFields").val(), width: $("#columnWidth").val(), orderBy: columnsOrder, otherName: $("#otherName").val(), replaceWords: $("#replaceWords").val(),
							 isSort: $("#isSort").is(':checked'), isAdvancedSearch: $("#isAdvancedSearch").is(':checked'), isSimpleSearch: $("#isSimpleSearch").is(':checked'), textAlign: selected.val(), isHidden: $("#isHidden").is(':checked'), 
							 onRenderEvent: $("#onRenderEvent").val(), onRenderEventName: $("#onRenderEventName").val(),comment: $("#comment").val(), isGroupBy : $("#isGroupBy").is(':checked'), dataFieldType: $("#dataFieldType").val(), dataDictionary:$("#dataDictionary").val(), isRange: $("#isRange").is(':checked'),columnType: $("#columnType").val()};
					
					 if(_row_id == 0){
						 $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('addRowData', ++rowCount, datarow);
					 }else{
						 if(!isNaN(_row_id)){
							 $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('setRowData', _row_id, datarow);
						 }else{
							 var _sel_row_index = $('#' + _row_id)[0].rowIndex;
							 $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('setRowData', _row_id, datarow);
						 }
					 }
				     _clearRecord();
				    $("#columnTitle").focus();

						validateMessageBox(form.title.info, form.msg.columnAddedSuccessfully , false);
				}else{
					validateMessageBox(form.title.validation, form.msg.listViewColumnTitleErrorNameExist , false,"columnTitle");
				}
			}else{
				validateMessageBox(form.title.validation, "Order number is empty or exists already" , false,"columnTitle");
				$("#columnsOrder").focus();
			}
			
		}else{
			validateMessageBox(form.title.validation, form.msg.listViewColumnTitleErrorRestrictedCharacters , false,"columnTitle");
		}
	}else{
		validateMessageBox(form.title.validation, form.msg.listViewColumnTitleErrorNotEmpty , false,"columnTitle");
	}
	$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('resetSelection');
}

function findHighestOrder(){
	var recordProperty = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getGridParam','data');
	var listOfValues = [];
	if(recordProperty.length == 0) {
		return recordProperty.length ;
	}
	for(var i = 0; i<recordProperty.length; i++){
		var rowId = recordProperty[i].id;
		var rowData = $('#_LIST_VIEW_COLUMNS_PROPERTY').jqGrid('getRowData', rowId);
		var rowDataInt = parseInt(rowData.orderBy);
		listOfValues.push( rowDataInt );
	}	
	//alert("listOfValues==b="+listOfValues);
	listOfValues.sort(function(a, b){return a-b});
	//listOfValues.sort();
	//alert("listOfValues==a="+listOfValues);
	//alert("listOfValues.length;=="+listOfValues.length+"===listOfValues[listOfValues.length-1]=="+listOfValues[listOfValues.length-1]);
	return listOfValues[listOfValues.length-1];
}

function checkExistOrderNO(columnsOrder){
	var recordProperty = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getGridParam','data');
	var containsFlag = false;
	var listOfValues = [];
	
	for(var i = 0; i<recordProperty.length; i++){
		var rowId = recordProperty[i].id;
		var rowData = $('#_LIST_VIEW_COLUMNS_PROPERTY').jqGrid('getRowData', rowId);
		if(rowData.orderBy == columnsOrder){
		//	alert("exist..."+rowData.orderBy);
			containsFlag = true;
		}
	}	
	return containsFlag;
}

function _deleteRecord(){
	var sel_id = $('#_LIST_VIEW_COLUMNS_PROPERTY').jqGrid('getGridParam', 'selrow');	
	var datadetails = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getGridParam', 'data');	
	var rowCount = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getGridParam', 'records');	
	var colOrder = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid ('getCell', sel_id, 'orderBy');
	if(sel_id != null){
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteSelectedColumns,
		    type: form.title.msgBoxTitleconfirm,
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	var data = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getRowData', sel_id); 
					if(data.id && isNaN(data.id)){													
						var is = false;
						$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('delRowData', sel_id);	
						setTimeout(function() {
								validateMessageBox(form.title.success, form.msg.selectedColumnDeletedSuccessfully , "success");
						},500);								
						$("#columnTitle").focus();
					}else{
						$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('delRowData', sel_id);
						$("#columnTitle").focus();
					}
				}else {
							
					$("#columnTitle").focus();
					return false;
				}					
		     }
	       
		});	
		

	//$("#_LIST_VIEW_COLUMNS_PROPERTY").setGridParam({sortname:'orderBy', sortorder: 'asc'}).trigger('reloadGrid');

	/*var data = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getRowData', sel_id); 
		alert("=====data====="+data.id);
		if(data.id && isNaN(data.id)){
			$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('delRowData', sel_id);
		}else{
			$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('delRowData', sel_id);
			var selectRowId = --sel_id;
		}*/
	}else if(rowCount == 0){
		validateMessageBox(form.title.message, form.msg.listViewNoRecords , false);
	}else{
		validateMessageBox(form.title.message, form.msg.listViewSelectRow , false);
		$("#columnTitle").focus();
	}
	_clearRecord();

}



function _deleteAllRecord(){
	var rowCount = $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getGridParam', 'records');
	if(rowCount > 0){
		
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteAllColumns,
		    type: form.title.msgBoxTitleconfirm,
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        		$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('clearGridData');
								setTimeout(function(){validateMessageBox(form.title.success, form.msg.allColumnDeletedSuccessfully , "success");},500);								

								$("#columnTitle").focus();
		        } else {
								$("#columnTitle").focus();
						}							
						
							
		   }
	        
		});	
		

		//$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('clearGridData');
	}else{
		validateMessageBox(form.title.message, form.msg.listViewNoRecords , false);
		$("#columnTitle").focus();
	}
	_clearRecord();
}

function _saveOrUpdateAllRecord(listViewId){
	
	if(listViewId){
		var createColumnsProperty =  $("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('getGridParam','data');
		/*var columnsProperty = new Array();
		for(var i = 0; i<createColumnsProperty.length; i++){
		    if (createColumnsProperty[i]){
		    	var colsPrperty = createColumnsProperty[i];
		    	var onRenderEventString = colsPrperty.onRenderEvent;
		    	onRenderEventString = encodeURLStringComponent(onRenderEventString);
			    colsPrperty.onRenderEvent = onRenderEventString;
			    columnsProperty.push(colsPrperty);
		    }
		}*/
		
		if(createColumnsProperty.length > 0){
			$.ajax({	
				url: '/bpm/listView/saveListViewColumns',
				type: 'POST',
				async: false,
				data: { listViewId: listViewId, listViewColumns: JSON.stringify(createColumnsProperty),moduleId:$("#moduleId").val()},
				dataType: 'json',
				success : function(response) {
					if(response.successMessage){
						$.msgBox({
							title: form.title.success,
							content: response.successMessage,
						    type: form.title.msgBoxTitlesuccess,
						    afterClose : function(){
						    	$("#_LIST_VIEW_COLUMNS_PROPERTY").jqGrid('clearGridData');
						    	getColumsPropety(response.listViewId);
						    	changeListViewIdInManageModuleTree(listViewId, response.listViewId, form.label.column);
									$("#columnTitle").focus();
						    }
						});
					}else{
						validateMessageBox(form.title.error, response.errorMessage , form.title.msgBoxTitleerror);
					}
				}
		  	});
		}else{
			validateMessageBox(form.title.message, form.msg.listViewColumnProperty , false);
			$("#columnTitle").focus();
		}
	}else{
		validateMessageBox(form.title.error, form.msg.listViewEntityError , form.title.msgBoxTitleerror);
	}
}

function _checkListViewQuery(){
	var listViewName=$("#viewName").val();
	var listViewId=$("#viewName").val();
	if(/^[\-a-zA-Z\_\u4e00-\u9eff]*$/.test(listViewName) == false) {
		$.msgBox({
			      title: form.title.error,
			      content: form.msg.listViewErrorListViewName,
       			      type: form.title.msgBoxTitleerror,
                              afterClose:function (){
			    	$("#saveButton").attr("disabled", "disabled");
				}
			});

		return false;
	}

	// if(!checkListViewName(listViewName.toUpperCase(),isUpdate)){
			if(isValidFieldValues()){
				var constructedQuery=constructedViewQuery();
				var param={ constructedQuery: constructedQuery }
				 $.ajax({	
					type: 'POST',
					async: false,
					url : '/bpm/listView/validateListViewQuery',
					data: param,
					success : function(response) {
						
						if(response.success){
							$.msgBox({
								title: form.title.success,
								content: response.success,
							    type: form.title.msgBoxTitlesuccess,
							    afterClose:function (){
							    	$("#saveButton").removeAttr("disabled", "disabled");
									}
							});
						}else{
							$.msgBox({
								title: form.title.error,
								content: response.error,
							    type: form.title.msgBoxTitleerror,
							    afterClose:function (){
							    	$("#selectColumns").focus();
									}
							});
						}
					}
				}); 
			}
	
		/*}else{
			$.msgBox({
				type: "info",
				title: "View Name '"+listViewName+"' already exists!",
				content: "Please give a different View Name.",
				  afterClose:function (){
					  $("#viewName").val("")
					  $("#viewName").focus() ;
				}
			});
		}*/
}

function disableSaveButton(){
	$('#saveButton').attr("disabled", "disabled");
}

function isValidFieldValues(){
	var status=true;
	var selectColumn=$("#selectColumns").val();
	var fromQuery=$("#fromQuery").val();
	
	if(selectColumn==""){
		$.msgBox({
			title: form.title.check,
			content: form.msg.listviewQuerySelectColumn,
			  afterClose:function (){
			  $("#selectColumns").focus() ;
			}
		});
		status=false;
	}
	
	if(fromQuery==""){
	 	$.msgBox({
			title: form.title.check,
			content: form.msg.listviewQueryFromSqlError,
			  afterClose:function (){
			  $("#fromQuery").focus() ;
			}
		});
	 	status=false;
	}	
	
	return status;
}

function constructedViewQuery(){
	var constructedQuery="SELECT ";
	var selectColumn=$("#selectColumns").val();
	var fromQuery=$("#fromQuery").val();
	var whereQuery=$("#whereQuery").val();
	var orderBy=$("#orderBy").val();
	var groupBy=$("#groupBy").val();
	/*whereQuery=whereQuery.replace(':loggedInUser','""');
	whereQuery=whereQuery.replace(':viewName','""');*/
	constructedQuery=constructedQuery+selectColumn;
	constructedQuery=constructedQuery+" FROM "+fromQuery;
	
	if(whereQuery!=""){
		constructedQuery=constructedQuery+" WHERE "+whereQuery;
	}
	
	if(groupBy!=""){
		constructedQuery=constructedQuery+" GROUP BY "+groupBy;
	}
	
	if(orderBy!=""){
		constructedQuery=constructedQuery+" ORDER BY "+orderBy;
	}
	
	return constructedQuery;
}

function checkListViewName(listViewName,isUpdate){
	var status=false;
	var param={ listViewName: listViewName };
	if(!isUpdate){
		$.ajax({	
			type: 'POST',
			async: false,
			url : '/bpm/listView/checkListViewName',
			data: param,
			success : function(response) {
				var isListViewName=response.isListViewName;
				if(isListViewName=="true"){
					status=true;
				}else{
					status=false;
				}
			}
		});
	}else{
		status=false;
	}
	 
	return status;
}


function buttonsPropertyGrid(){
 	$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid({
  		data: [],
		datatype: "local",
		width: listViewButtonsPropertyGridWidth(), 
		height: listViewButtonsPropertyGridHeight(), 
	    colNames:['Id','Display Name', 'Order By','Button Script','Icon Path', 'Table Name', 'Column Name', 'Redirect Value', 'Status','User Name','User','Department','Roles','Group'],
	    colModel :[{
			    name: 'id'
			 	,index: 'id'
			 	,width: 100
			 	,align: 'left'
			 	,hidden: true  				 	
		 	},{
			    name: 'displayName'
			 	,index: 'displayName'
			 	,width: 150
			 	,align: 'left'
			 	,hidden: false  				 	
		 	},{
			    name: 'orderBy'
			 	,index: 'orderBy'
			 	,width: 80
			 	,align: 'center'
			 	,hidden: false  				 	
			},{
			    name: 'buttonMethod'
			 	,index: 'buttonMethod'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: true  				 	
		 	},{
		 		name: 'iconPath'
			 	,index: 'iconPath'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: true  				 	
		 	},{
		 		name: 'tableName'
			 	,index: 'tableName'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: true  				 	
		 	},{
		 		name: 'columnName'
			 	,index: 'columnName'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: true  				 	
		 	},{
		 		name: 'redirectValue'
			 	,index: 'redirectValue'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: true  				 	
			 },{
		 		name: 'active'
			 	,index: 'active'
			 	,width: 80
			 	,align: 'center'
			 	,hidden: false
			 	,formatter:function(cellvalue, option, rawObject){
			 		if(cellvalue == "true")
			 			return "Active";
			 		else
			 			return "InActive";
			 	}
		 	},{
				name: 'listViewButtonUsersFullName'
					,index: 'listViewButtonUsersFullName'
					,width: 100
					 ,align: 'left'
					 ,hidden: true  				 	
			 },{
				name: 'listViewButtonUsers'
					,index: 'listViewButtonUsers'
					,width: 100
					 ,align: 'left'
					 ,hidden: true  				 	
			 },{
			 	name: 'listViewButtonDeps'
				,index: 'listViewButtonDeps'
				,width: 100
				 ,align: 'left'
				 ,hidden: true  				 	
			},{
		 		name: 'listViewButtonRoles'
				,index: 'listViewButtonRoles'
			 	,width: 100
			 	,align: 'left'
			 	,hidden: true  				 	
		 	},{
				name: 'listViewButtonGroups'
				,index: 'listViewButtonGroups'
				,width: 100
				 ,align: 'left'
				 ,hidden: true  				 	
		 	}],
	    pager: '#_LIST_VIEW_BUTTONS_PROPERTY_PAGER',
	    viewrecords: true,
	    gridview: true,
	    scroll:1,
	   	hidegrid: false,
	    loadComplete: function() {
    		$("#_LIST_VIEW_BUTTONS_PROPERTY tr.jqgrow:odd").css("background", "#f0f0f0");
    	},
    	onSelectRow: function(row_id) {
    		_button_row_id = row_id;
    		var data = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getRowData',row_id);  
    		$("#buttonPropertyId").val(data.id);
			$("#buttonDisplayName").val(data.displayName);
			$("#buttonOrder").val(data.orderBy);
			$("#buttonScript").val(data.buttonMethod);
			$("#iconPath").val(data.iconPath);
			
			$("#listViewButtonRoles").val(data.listViewButtonRoles);
			$("#listViewButtonDeps").val(data.listViewButtonDeps);
			$("#listViewButtonGroups").val(data.listViewButtonGroups);
			$("#listViewButtonUsers").val(data.listViewButtonUsers);
			$("#listViewButtonUsersFullName").val(data.listViewButtonUsersFullName);
			
			if(data.redirectValue!=null && data.redirectValue!="null"){
				$("#redirectValue").val(data.redirectValue);
			}else{
				$("#redirectValue").val("");
			}
			
			if(data.active == "Active"){
				$("#isActive").prop("checked", true);
			}else{
				$("#createNewForm").prop("checked", false);
			}			
			$("#choosed_iconPath").attr('src', data.iconPath);
			if(data.displayName == "Delete" || data.displayName == "Restore"){
				$("#create_new_redirect_formorprocess_tr").hide();
				$("#table_name_tr").show();
				$("#column_name_tr").show();
				loadTableCombo();
				$("#tableName").val(data.tableName);
				loadColumnNameForTable(data.tableName)
				$("#columnName").val(data.columnName);
				$("#createNewForm").prop("checked", false);
				$("#createNewProcess").prop("checked", false);
			}else if(data.displayName == "Create New"){
				$("#table_name_tr").hide();
				$("#column_name_tr").hide();
				$("#columnName").empty();
				$("#tableName").empty();
				$("#create_new_redirect_formorprocess_tr").show();
			}else {
				$("#create_new_redirect_formorprocess_tr").hide();
				$("#table_name_tr").hide();
				$("#column_name_tr").hide();
				$("#columnName").empty();
				$("#tableName").empty();
				$("#createNewForm").prop("checked", false);
				$("#createNewProcess").prop("checked", false);
			}
			
			if(data.displayName == "Create New" || data.displayName == "Recycle" || data.displayName == "Print"){
				/*$("#buttonScript").attr('readonly', true);*/
				$("#buttonDisplayName").attr('readonly', true);
				$("#onClickbuttonScript").hide();
				if(data.active == "Active"){
					$("#isActive").prop("checked", true);
				}else{
					$("#isActive").prop("checked", false);
				}
				
			}else{
				/*$("#buttonScript").removeAttr('readonly');*/
				$("#buttonDisplayName").removeAttr('readonly');
				$("#onClickbuttonScript").show();
			}
	    }
 	});
	$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('navGrid','#_LIST_VIEW_BUTTONS_PROPERTY_PAGER',{del:false,edit:false,add:false, search:false, refresh:false},{},{},{});	
}

function _addButtonRecord(){
	if($("#buttonDisplayName").val() != "" ){
		if(!isSpecialCharacterFound($("#buttonDisplayName").val())){
			if($("#buttonScript").val()!="" || $("#buttonDisplayName").val() == "Create New" ){
				if(!isNaN($("#buttonOrder").val()) && !isOrderNumberExist($("#buttonOrder").val(), '_LIST_VIEW_BUTTONS_PROPERTY')){
					if(!isNameExist($("#buttonDisplayName").val(),'_LIST_VIEW_BUTTONS_PROPERTY')){
						 var rowCount = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getGridParam', 'records');
						 var buttonOrder=0;
						 if($("#buttonOrder").val()!=""){
							 if(!isNaN($("#buttonOrder").val())){
								 buttonOrder=$("#buttonOrder").val();
							 }else{
								 $.msgBox({
				          		    title:form.title.check,
				          		    content:form.msg.listViewValidationNumber,
				         		    afterClose:function (){
				         		    	$("#buttonOrder").focus();
				         		    	$("#buttonOrder").val(0);
				                    }
				          		});
							 }
							
						 }
						var btnStatus = "false"; 
						if($("#isActive").is(':checked')){
							btnStatus = "true";
						}
						var datarow = { id: $("#buttonPropertyId").val(), displayName: $("#buttonDisplayName").val(), orderBy: $("#buttonOrder").val(), 
								buttonMethod: $("#buttonScript").val(), iconPath: $("#iconPath").val(), tableName: $("#tableName").val(), 
								columnName: $("#columnName").val(), active: btnStatus, redirectValue:$("#redirectValue").val(),
								listViewButtonUsers : $("#listViewButtonUsers").val(),
								listViewButtonDeps : $("#listViewButtonDeps").val(),
								listViewButtonRoles: $("#listViewButtonRoles").val(),
								listViewButtonUsersFullName:$("#listViewButtonUsersFullName").val(),
								listViewButtonGroups: $("#listViewButtonGroups").val()};
						 if(_button_row_id == 0){
							 $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('addRowData', ++rowCount, datarow);
						 }else{
							 if(!isNaN(_button_row_id)){
								 $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('setRowData', _button_row_id, datarow);
							 }else{
								 var _sel_row_index = $('#' + _button_row_id)[0].rowIndex;
								 $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('setRowData', _button_row_id, datarow);
							 }
						 }
					_clearButtonRecord();
					validateMessageBox(form.title.info, form.msg.buttonAddedSuccessfully , "info");
					 $("#buttonDisplayName").focus();
					}else{
						validateMessageBox(form.title.validation, form.msg.listViewErrorDisplayName , false,"buttonDisplayName");
					}
				}else{
					validateMessageBox(form.title.validation, form.msg.listViewErrorOrderNumber , false,"buttonOrder");
				}
			}else{
				validateMessageBox(form.title.validation, form.msg.listViewErrorButtonScript, false,"buttonScript");
			}
		}else{
			validateMessageBox(form.title.validation, form.msg.listViewErrorRestrictedCharacters , false,"buttonDisplayName");
		}
	}else{
		validateMessageBox(form.title.validation, form.msg.listViewErrorEmptyName , false,"buttonDisplayName");
	}
	$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('resetSelection');
}

function listViewAdvanceSearch(listViewId){
	var params = "listViewId="+listViewId;
	 _execute('popupDialog', params);
	openSelectDialogForFixedPosition("popupDialog","","800","260","高级搜索");//height Changed from 300 to 230
	document.location.href = "#/bpm/listView/advanceSearch";
}
function _simpleSearch(viewName, listViewId, obj){
	if(obj.value != null){
		$.ajax({	
			url: '/bpm/listView/reloadGridWithSimpleSearch?listViewId='+listViewId+'&searchValue='+encodeURI(obj.value)+'&listViewParams='+$("#listViewParamsList").val(),
			type: 'GET',
			async: false,
			dataType: 'json',
			success : function(response) {
				if(response){
					var gridData = eval(response);
			    	$("#_"+viewName).jqGrid('clearGridData');
					$('#_'+viewName).jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
			    }
			}
	  	});
	}
}


function _deleteButtonRecord(){
	var sel_id = $('#_LIST_VIEW_BUTTONS_PROPERTY').jqGrid('getGridParam', 'selrow');
	var rowCount = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getGridParam', 'records');
	if(sel_id != null){
	/*	var data = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getRowData', sel_id);
		if(data.displayName == "Create New" || data.displayName == "Restore" || data.displayName == "Recycle" || data.displayName == "Delete" || data.displayName == "Print"){
			validateMessageBox(form.title.message, form.msg.cannotDeleteDefaultButtons , false);
		}else{
			if(data.id){
				$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('delRowData', sel_id);
			}else{
				$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('delRowData', sel_id);
				var selectRowId = --sel_id;
			}
			_clearButtonRecord();
		}*/
	$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteSelectedButton,
		    type: form.title.msgBoxTitleconfirm,
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	var data = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getRowData', sel_id);
		if(data.displayName == "Create New" || data.displayName == "Restore" || data.displayName == "Recycle" || data.displayName == "Delete" || data.displayName == "Print"){
							setTimeout(function(){validateMessageBox(form.title.message, form.msg.cannotDeleteDefaultButtons , false);},500);
	$("#buttonDisplayName").focus();
		}else{
			if(data.id){
				$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('delRowData', sel_id);
				setTimeout(function(){validateMessageBox(form.title.success, form.msg.selectedButtonDeletedSuccessfully , "success");},500);
				
			}else{
				$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('delRowData', sel_id);
				var selectRowId = --sel_id;
								setTimeout(function(){validateMessageBox(form.title.success, form.msg.selectedButtonDeletedSuccessfully , "success");},500);	

			}
			_clearButtonRecord();
			$("#buttonDisplayName").focus();
		}
						}		else {
							
							$("#buttonDisplayName").focus();
							return false;
						}					
						
							
		     }
	       
		});	
	}else if(rowCount == 0){
		validateMessageBox(form.title.message, form.msg.listViewNoRecords , false);
	}else{
		validateMessageBox(form.title.message, form.msg.listViewSelectRow , false);
		$("#buttonDisplayName").focus();
	}
}

function _deleteAllButtonRecord(){
	var rowCount = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getGridParam', 'records');
	if(rowCount > 0){
			$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteAllButtons,
		    type: form.title.msgBoxTitleconfirm,
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        
				$("#_LIST_VIEW_BUTTONS_PROPERTY").find('tr').each(function(){
				if(this.id != ""){
					
					var data = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getRowData', this.id);
					if(data.displayName == "Create New" || data.displayName == "Restore" || data.displayName == "Recycle" || data.displayName == "Delete" || data.displayName == "Print"){		
					
					$("#buttonDisplayName").focus();	
					}else{
						$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('delRowData', this.id);
						setTimeout(function(){
						 validateMessageBox(form.title.success, form.msg.nonDefaultButtonsDeletedSuccessfully, "success");
					},500);
						
						$("#buttonDisplayName").focus();
					}
				
			
				}
			});
				
			} else {	
							
				$("#buttonDisplayName").focus();
				return false;
			}					
						
							
		    }
	       
		});	
			/*$("#_LIST_VIEW_BUTTONS_PROPERTY").find('tr').each(function(){
				if(this.id != ""){
					alert("allbutton");
					var data = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getRowData', this.id);
					if(data.displayName == "Create New" || data.displayName == "Restore" || data.displayName == "Recycle" || data.displayName == "Delete" || data.displayName == "Print"){		
					
					$("#buttonDisplayName").focus();	
					}else{
						$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('delRowData', this.id);
						alert("Non default buttons deleted");
						$("#buttonDisplayName").focus();
					}
				
			
				}
			});*/
		//$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('clearGridData');
	}else{
		validateMessageBox(form.title.message, form.msg.listViewNoRecords , false);
	}
	_clearButtonRecord();
}

function _clearButtonRecord(){
	$("#buttonPropertyId").val("");
	$("#buttonDisplayName").val("");
	$("#buttonOrder").val(0);
	$("#buttonScript").val("");
	$("#iconPath").val("");
	$("#tableName").val("");
	$("#columnName").val("");
	$("#isActive").prop("checked", true);
	$("#choosed_iconPath").attr("src", "");
	$("#table_name_tr").hide();
	$("#column_name_tr").hide();
	$("#onClickbuttonScript").show();
	$("#create_new_redirect_formorprocess_tr").hide();
	$("#buttonDisplayName").removeAttr('readonly');
	$("#buttonScript").removeAttr('readonly');
	$("#listViewButtonRoles").val("");
	$("#listViewButtonUsers").val("");
	$("#listViewButtonUsersFullName").val("");
	$("#listViewButtonDeps").val("");
	$("#listViewButtonGroups").val("");
	$("#createNewForm").prop("checked", false);
	$("#createNewProcess").prop("checked", false);
	_button_row_id = 0;
	$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('resetSelection');
}

function _saveOrUpdateButtonRecord(listViewId){
	if(listViewId){
		 var createButtonsProperty =  $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getGridParam','data');
		var buttonsProperty = new Array();
		for(var i = 0; i<createButtonsProperty.length; i++){
		    if (createButtonsProperty[i]){
		    	var colsPrperty = createButtonsProperty[i];
	    		var buttonScriptString = colsPrperty.buttonMethod;
	    		colsPrperty.buttonMethod = (buttonScriptString != null) ? encodeURLStringComponent(buttonScriptString) : buttonScriptString;
	    		//buttonScriptString = encodeURLStringComponent(buttonScriptString);
	    		//colsPrperty.buttonMethod = buttonScriptString;
	    		buttonsProperty.push(colsPrperty);
		   	}
		}	
		
		if(!buttonsProperty.length > 0){
			buttonsProperty=null;
		}
			$.ajax({	
					url: "/bpm/listView/saveListViewButtons",
					type: 'POST',
					async: false,
					data : { listViewId:listViewId,listViewButtons: JSON.stringify(buttonsProperty),moduleId: $("#moduleId").val()},
					dataType: 'json',
					success : function(response) {
						if(response.successMessage){
							$.msgBox({
								title: form.title.success,
								content: response.successMessage,
							        type: form.title.msgBoxTitlesuccess,
							        afterClose : function(){
								    	$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('clearGridData');
								    	getButtonsPropety(response.listViewId);
								    	changeListViewIdInManageModuleTree(listViewId, response.listViewId, 'button');
							    	}
							});
						}else{
							validateMessageBox(form.title.error, response.errorMessage, form.title.msgBoxTitleerror);
						}
					}
			  	});
		
	}else{
		validateMessageBox(form.title.error, form.msg.listViewEntityError , form.title.msgBoxTitleerror);
	}
}


function getButtonsPropety(listViewId){
	$.ajax({
		url: '/bpm/listView/buttonsProperty?listViewId='+listViewId,
		type: 'GET',
		dataType: 'html',
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		success : function(response) {
			if(response.length > 0){
				var gridData = eval(response);
				$('#_LIST_VIEW_BUTTONS_PROPERTY').jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
			}
		}
	});
}

function _groupFilter(viewName, listViewId, filterTblName){
	var searchParamSize = 0;
	$("#"+filterTblName).find("select").each(function(index){searchParamSize = index;});
	var searchParams = "[{"; 
	$("#"+filterTblName).find("select").each(function(index){
		var attrName = $(this).attr("name");
		var attrValue = $(this).val();
		if(attrValue != ""){
			searchParams = searchParams + attrName+":"+attrValue;
			if(searchParamSize > index){
				searchParams = searchParams + ",";
			}
		}
	});
	searchParams = searchParams+ "}]"; 
	var listViewParams=$("#listViewParamsList").val();
	if(searchParams != null){
		$.ajax({	
			url: '/bpm/listView/reloadGridWithSearchParams?listViewId='+listViewId+'&searchParams='+searchParams+'&sortType=asc&listViewParams='+listViewParams+'&searchType=and&sortTypeColumn=',
			type: 'GET',
			async: false,
			dataType: 'json',
			success : function(response) {
				if(response){
					var gridData = eval(response);
			    	$("#_"+viewName).jqGrid('clearGridData');
					$('#_'+viewName).jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
					closeSelectDialog('popupDialog');
			    }
			}
	  	});
	}
}

//function for showing alert message from process designer and calling the appropriate listview.
//It redirects to showListView method based on condition.
function showListViews(listViewName,title){

	if(filterURL.indexOf("processEditor") > -1){
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.listViewExitProcessDesigner,
		    type: form.title.msgBoxTitleconfirm,
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	showListView(listViewName,title);
		        }else{
		        	return false;
		        }
	        }
		});	
	}else{
		showListView(listViewName,title)
	}	
}

//function for calling appropriate list view.
function showListView(listViewName,title){
	if(title == undefined){
		title = "";
	}
	if(checkListViewName(listViewName, false)){
		var listViewParam="listViewName="+listViewName+"&title="+encodeURI(title)+"&container=target&listViewParams=[{}]+&headerTag="+true;
		document.location.href = "#bpm/listView/showListViewGrid";
		_execute('target',listViewParam);
	}else{
		validateMessageBox(form.title.message, form.msg.listViewErrorListView , false);
	}
}

function showTimingTaskListViews(listViewName,title){
	if(checkListViewName(listViewName, false)){
		var listViewParam="listViewName="+listViewName+"&title="+title+"&container=target&listViewParams=[{}]";
		document.location.href = "#bpm/listView/showListViewGrid";
		_execute('target',listViewParam);
		validateMessageBox(form.title.message, form.msg.listViewSuccessTaskDelete , form.title.msgBoxTitlesuccess);
	}else{
		validateMessageBox(form.title.message, form.msg.listViewErrorListView, false);
	}
}

function showListViewsWithContainer(listViewName,title, container){
	var containerEl = document.getElementById(container);
	if(containerEl){
		var listViewParam="listViewName="+listViewName+"&title="+encodeURI(title)+"&container="+container+"&listViewParams=[{}]";
		document.location.href = "#bpm/listView/showListViewGrid";
		_execute(container,listViewParam);
	}else{
		alert(form.msg.listViewErrorContainerLoad+listViewName);
	}
	
}

function showListViewsWithContainerAndParam(listViewName,title, container,param, needHeader){
	var containerEl = document.getElementById(container);
	var pageHeaderNeeded = "";
	if(containerEl){
		if(needHeader != undefined){
			pageHeaderNeeded = "&needHeader="+needHeader;
		}
		if(param==null){
			param="[{}]";
		}else{
			param=JSON.stringify(param);
		}
		var listViewParam="listViewName="+listViewName+"&title="+encodeURI(title)+"&container="+container+"&listViewParams="+param+pageHeaderNeeded;
		document.location.href = "#bpm/listView/showListViewGrid";
		_execute(container,listViewParam);
		return true;
	}else{
		alert(form.msg.listViewErrorContainerLoad+listViewName);
		return false;
	}
}

function showListViewsWithContainerAndParamAndSize(listViewName,title, container,param,height,width){
	var containerEl = document.getElementById(container);
	if(containerEl){
		if(param==null){
			param="[{}]";
		}else{
			param=JSON.stringify(param);
		}
		var listViewParam="listViewName="+listViewName+"&title="+encodeURI(title)+"&height="+height+"&width="+width+"&container="+container+"&listViewParams="+param;
		document.location.href = "#bpm/listView/showListViewGrid";
		_execute(container,listViewParam);
		return true;
	}else{
		alert(form.msg.listViewErrorContainerLoad+listViewName);
		return false;
	}
}

function listViewDisplay(listViewName,title,container,listViewParams,width,height){
	
	if(listViewParams == ''){
		listViewParams = "[{}]";
	}
	$.ajax({
		url: '/bpm/listView/showListViewGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'listViewName=' + listViewName + "&title=" + title + "&container=" + container + "&listViewParams=" + listViewParams+"&height="+height+"&width="+width+"&needHeader=false",
		success : function(response) {
			
			if(response.length > 0){
				setTimeout(function(){$("#"+container).html(response);},10);
			}
			},
			failure: function(response){
				//alert("Failure  "+response);
			}
	});
}

function getGroupSettingPropety(listViewId){
	$.ajax({
		url: '/bpm/listView/groupSettingProperty?listViewId='+listViewId,
        type: 'GET',
        dataType: 'json',
        async: false,
		success : function(response) {
			if(response!=null && response.length > 0){
				var gridData = eval(response);
				$('#_LIST_VIEW_GROUP_SETTING_PROPERTY').jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
			}
		}
	});
}


function groupSettingGrid(){

 	$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid({
  		data: [],
		datatype: "local",
		width: listViewGroupSettingPropertyGridWidth(), 
		height: listViewGroupSettingPropertyGridHeight(), 
	    colNames:['Id','Group Name','Group Fields Name','Parent Group','Sort Type','Group Type','Order By','Comment'],
	    colModel :[{
			    name: 'id'
			 	,index: 'id'
			 	,width: 100
			 	,align: 'left'
			 	,hidden: true  				 	
		 	},{
			    name: 'groupName'
			 	,index: 'groupName'
			 	,width: 150
			 	,align: 'left'
			 	,hidden: false  				 	
		 	},{
			    name: 'groupFieldsName'
			 	,index: 'groupFieldsName'
			 	,width: 150
			 	,align: 'left'
			 	,hidden: false  				 	
		 	},{
			    name: 'parentGroup'
			 	,index: 'parentGroup'
			 	,width: 150
			 	,align: 'left'
			 	,hidden: false  				 	
		 	},{
			    name: 'sortType'
			 	,index: 'sortType'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: false  				 	
			},{
			    name: 'groupType'
			 	,index: 'groupType'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: false  				 	
		 	},{
			    name: 'orderBy'
			 	,index: 'orderBy'
			 	,width: 80
			 	,align: 'center'
			 	,hidden: false  				 	
		 	},{
			    name: 'comment'
			 	,index: 'comment'
			 	,width: 80
			 	,align: 'left'
			 	,hidden: true  				 	
		 	}],
	    pager: '#_LIST_VIEW_GROUP_SETTING_PROPERTY_PAGER',
	    viewrecords: true,
	    gridview: true,
	    scroll:1,
	   	hidegrid: false,
	    loadComplete: function() {
    		$("#_LIST_VIEW_GROUP_SETTING_PROPERTY tr.jqgrow:odd").css("background", "#f0f0f0");
    	},
    	onSelectRow: function(row_id) {
    		__gs_row_id = row_id;
    		var data = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getRowData',row_id);  
    		$("#columnPropertyIdInGS").val(data.id);
			$("#groupName").val(data.groupName);
			if(data.groupName!="" && isGroupHavingChildGroup(data)){
				$("#groupFieldsName").prop('disabled', 'disabled');
			}else{
				$("#groupFieldsName").removeAttr("disabled");
			}
			
			$("#sortType"+data.sortType).prop('checked', true);
			$("#groupFieldsName").val(data.groupFieldsName);
			$("#groupType"+data.groupType).prop('checked', true);
			$("#groupSettingcomment").val(data.comment);
			updateParentGroupSelectBoxList(data.groupFieldsName,data.parentGroup);
			$("#parentGroup").val(data.parentGroup);
			$("#groupOrder").val(data.orderBy);
    	}
 	});
	$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('navGrid','#_LIST_VIEW_GROUP_SETTING_PROPERTY_PAGER',{del:false,edit:false,add:false, search:false, refresh:false},{},{},{});	
}

function updateParentGroupSelectBoxList(groupFieldsName,oldParentGroupName){
	var rowCount = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getGridParam', 'records');
	var element_id = $("#parentGroup");
	element_id.empty();
	if(rowCount > 0){
		var gridData = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getRowData');
		var allGroupFieldsName=[];
		var allParentGroup=[];
		for(var index = 0; index < gridData.length; index++){
			if(gridData[index].groupFieldsName!=groupFieldsName && gridData[index].groupType == "Multi"){
				if(gridData[index].parentGroup!=groupFieldsName || gridData[index].parentGroup=="" || gridData[index].parentGroup==null){
					allGroupFieldsName[allGroupFieldsName.length]=gridData[index].groupFieldsName;
				}
			}
			
			if(gridData[index].parentGroup!="" && gridData[index].parentGroup != null){
				allParentGroup[allParentGroup.length]=gridData[index].parentGroup;
			}
		}

		var diffFieldsName=getValidGroupFieldsName(allGroupFieldsName,allParentGroup);
		for(var index = 0; index < diffFieldsName.length; index++){
			$(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(diffFieldsName[index], diffFieldsName[index]);
		}
		
		if(oldParentGroupName!=""){
			$(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(oldParentGroupName, oldParentGroupName);
		}

		$(element_id).prepend("<option value='' selected='selected'></option>");
	}
}

function getValidGroupFieldsName(allGroupFieldsName,allParentGroup){
	var diffFieldsName = [];
	var i = 0;
	$.grep(allGroupFieldsName, function(el) {
		if (jQuery.inArray(el, allParentGroup) == -1) diffFieldsName.push(el);
	    i++;
	});
	return diffFieldsName;
}


function _addGroupSettingRecord(){
	
	if(typeof String.prototype.trim !== 'function') {
		  String.prototype.trim = function() {
		    return this.replace(/^\s+|\s+$/g, ''); 
		  }
	}
	var groupName = $("#groupName").val().trim();
	//groupName = encodeURI(groupName);
	//groupName = decodeURI(groupName);
	if(groupName != "" && $("#groupFieldsName").val() != ""){
		if(!isSpecialCharacterFound(groupName)){
			var groupOrder = $("#groupOrder").val();
			if(!isNaN(groupOrder) && !isOrderNumberExist(groupOrder, '_LIST_VIEW_GROUP_SETTING_PROPERTY')){
					if(!isNameExist(groupName,'_LIST_VIEW_GROUP_SETTING_PROPERTY')){
						 var sortType = $("input[type='radio'][name='sortType']:checked");
						 var groupType = $("input[type='radio'][name='groupType']:checked");
						 var groupFieldsName = $("#groupFieldsName").val();
						if(!validateGroupTypeIsParent(groupFieldsName,groupType.val(),'_LIST_VIEW_GROUP_SETTING_PROPERTY')){
							var parentGroup = $("#parentGroup").val();
							if(groupFieldsName != parentGroup){
								if(parentGroup == null){
									parentGroup = "";
								}
								var rowCount = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getGridParam', 'records');
								
								 var datarow = { id: $("#columnPropertyIdInGS").val(), groupName: groupName, groupFieldsName: groupFieldsName, 
										 parentGroup: parentGroup, sortType: sortType.val(), groupType: groupType.val(), orderBy: $("#groupOrder").val(), comment: $("#groupSettingcomment").val()};
								 if(__gs_row_id == 0){
									 $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('addRowData', ++rowCount, datarow);
								 }else{
									 if(!isNaN(__gs_row_id)){
										 $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('setRowData', __gs_row_id, datarow);
									 }else{
										 var _sel_row_index = $('#' + __gs_row_id)[0].rowIndex;
										 $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('setRowData', __gs_row_id, datarow);
									 }
								 }
								 _clearGroupSettingRecord();
									validateMessageBox(form.title.info, form.msg.groupAddedSuccessfully , "info");
							}else{
								validateMessageBox(form.title.validation, form.msg.listViewErrorSameParentName , false);
							}
						}else{
							validateMessageBox(form.title.validation, "Group name "+groupName+" is a Parent group its type cannot be Single." , false);
						}
					
				}else{
					validateMessageBox(form.title.validation, form.msg.listViewErrorGroupName, false);
				}
			}else{
				validateMessageBox(form.title.validation, form.msg.listViewOrderNumber , false);
				$("#groupOrder").focus();
			}
		}else{
			validateMessageBox(form.title.validation, form.msg.listViewRestrictedCharacters , false);
		}
	}else{
		validateMessageBox(form.title.validation, form.msg.listViewGroupFields , false);
	}
	//updateParentGroupSelectBoxList("");
	$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('resetSelection');
}

function validateGroupTypeIsParent(groupFieldsName,groupType,gridId){
	var isPresentAsParent = false;
	var rowCount = $("#"+gridId).jqGrid('getGridParam', 'records');
	var sel_id = $('#'+gridId).jqGrid('getGridParam', 'selrow');
	
	if(groupType=="Single"){
		if(rowCount > 0){
			var data = $("#"+gridId).jqGrid('getRowData'); 
			for(index = 0; index < data.length; index++){
				if(data[index].parentGroup == groupFieldsName){
					isPresentAsParent = true;
					break;
				}
			}
		}
	}
	
	return isPresentAsParent;
	
}


function isSpecialCharacterFound(_passedValue){
	var isSpecialChar = false;
	var iChars = "~`!#$%^&*+=-[]\\\';,/{}|\":<>?)(";
	if(_passedValue != ""){
		for (var i = 0; i < _passedValue.length; i++) {
		  if (iChars.indexOf(_passedValue.charAt(i)) != -1) {
			  isSpecialChar = true;
		  }
		}
	}
	return isSpecialChar;
}

function _deleteGroupSettingRecord(){
	var sel_id = $('#_LIST_VIEW_GROUP_SETTING_PROPERTY').jqGrid('getGridParam', 'selrow');
	var rowCount = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getGridParam', 'records');
	if(sel_id != null){
		var data = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getRowData', sel_id); 
		if(!isGroupHavingChildGroup(data)){
			/*alert("ggggg");
			if(data.id && isNaN(data.id)){
				$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('delRowData', sel_id);
			}else{
				$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('delRowData', sel_id);
				var selectRowId = --sel_id;
			}*/$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteSelectedGroupSetting,
		    type: form.title.msgBoxTitleconfirm,
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        
				if(data.id && isNaN(data.id)){
				$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('delRowData', sel_id);
				setTimeout(function(){
				validateMessageBox(form.title.success, form.msg.selectedGroupSettingsDeletedSuccessfully, "success");
					},500);
				
			}else{
				$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('delRowData', sel_id);
				var selectRowId = --sel_id;
				setTimeout(function(){
				validateMessageBox(form.title.success, form.msg.selectedGroupSettingsDeletedSuccessfully, "success");
					},500);
				
			}
				
			} else {						
				$("#groupName").focus();
				return false;
			}					
						
							
		    }
	       
		});	
		}else{
			validateMessageBox(form.title.message, form.msg.listViewRemoveChild , false);
		}
	}else if(rowCount == 0){
		validateMessageBox(form.title.message, form.msg.listViewNoRecords , false);
	}else{
		validateMessageBox(form.title.message, form.msg.listViewSelectRow, false);
			
	}
	updateParentGroupSelectBoxList("","");
	_clearGroupSettingRecord();
}

function isGroupHavingChildGroup(data){
	var status=false;
	//Get All Grid Datas
    var allData =  $('#_LIST_VIEW_GROUP_SETTING_PROPERTY').jqGrid('getGridParam','data');
    for(var i = 0; i<allData.length; i++){
    	if (allData[i]){
	    	var colsPrperty = allData[i];
	    	if(colsPrperty.parentGroup!="" && colsPrperty.parentGroup==data.groupFieldsName){
	    		status=true;
	    		break;
	    	}
	    }
	}
    return status;
}

function _deleteGroupSettingAllRecord(){
	var rowCount = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getGridParam', 'records');
	if(rowCount > 0){
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteAllGroupSettings,
		    type: form.title.msgBoxTitleconfirm,
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        
				$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('clearGridData');
								setTimeout(function(){
						 validateMessageBox(form.title.success, form.msg.allGroupSettingsDeletedSuccessfully, "success");
					},500);
				
			} else {						
				
				return false;
			}					
						
							
		    }
	       
		});	
		//$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('clearGridData');
	}else{
		validateMessageBox(form.title.message, form.msg.listViewNoRecords , false);
	}
	updateParentGroupSelectBoxList("","");
	_clearGroupSettingRecord();
}

function _clearGroupSettingRecord(){	
	$("#columnPropertyIdInGS").val("");
	$("#groupName").val("");
	$("#groupFieldsName").val("");
	$("#parentGroup").val("");
	$("#sortTypeAsc").prop('checked', true);
	$("#groupTypeMulti").prop('checked', true);
	$("#groupSettingcomment").val("");
	$("#groupOrder").val(0);
	$("#parentGroup").removeAttr("disabled");
	__gs_row_id = 0;
	updateParentGroupSelectBoxList("","");
	$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('resetSelection');
}



function _saveOrUpdateAllGroupSettingRecord(listViewId){
	
	if(listViewId){
		 var createGroupSettingProperty =  $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getGridParam','data');
		var groupSettingProperty = new Array();
		for(var i = 0; i<createGroupSettingProperty.length; i++){
		    if (createGroupSettingProperty[i]){
		    	//console.log(createGroupSettingProperty[i])
		    	var property = createGroupSettingProperty[i];
		    	var groupName = createGroupSettingProperty[i].groupName;
		    	groupName = encodeURI(groupName);
		    	property.groupName = groupName;
		    	//alert(prop.groupName);
		    	groupSettingProperty.push(property);
		    }
		}	
		if(!groupSettingProperty.length > 0){
			groupSettingProperty=null;
		}
		if(groupSettingProperty.length > 0){
			$.ajax({	
				url: '/bpm/listView/saveListViewGroupSetting?listViewId='+listViewId+'&createGroupSetting='+JSON.stringify(groupSettingProperty)+'&moduleId='+$("#moduleId").val(),
				type: 'POST',
				async: false,
				dataType: 'json',
				success : function(response) {
					if(response.successMessage){
						$.msgBox({
							title: form.title.success,
							content: response.successMessage,
						    type: "success",
						    afterClose : function(){
						    	$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('clearGridData');
						    	getGroupSettingPropety(response.listViewId);
						    	changeListViewIdInManageModuleTree(listViewId, response.listViewId, 'group');
						    }
						});
					}else{
						validateMessageBox(form.title.error, response.errorMessage , form.title.msgBoxTitleerror);
					}
				}
		  	});
		}else{
			validateMessageBox(form.title.message , form.msg.listViewGroupSettingChanges , false);
		}
	}else{
		validateMessageBox(form.title.error , form.msg.listViewEntityError, form.title.msgBoxTitleerror);
	}
}

function renderGroupSettingChildNodesForModule(e, data, listViewId, gridId) {
	var groupByFields = [];
	var whereParams = "";
	var whereParam = "";
	$(data.rslt.obj).parents('li').each(function () {
		if ( groupName != undefined && groupName != null){
		groupByFields.push($(this).attr("groupName"));
		}
		
		var groupName = $(this).attr("groupName");
		var groupValue = $(this).attr("groupValue");
		if ( groupName != undefined && groupValue != undefined) {
			
			groupValue=groupValue.split("(");
		
			var searchName=groupValue[0];
			whereParam = whereParam +"'"+ groupName+"':'"+searchName.trim()+"',";
		}
    });
	var groupType = data.rslt.obj.attr("grouptype");
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var parentGroup = data.rslt.obj.attr("groupName");
	var sortType = data.rslt.obj.attr("sortType");
	groupByFields.push(parentGroup);
	var rootNode = data.inst.get_path()[3];
	var nodeLevel = data.inst.get_path().length;
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#module_relation_tree")); 
	var splitName=data.rslt.obj.attr("name").split("(");
	var searchName=splitName[0];
	whereParam = whereParam + "" + data.rslt.obj.attr("groupName")+":'"+searchName.trim()+"',";
	if(whereParam){
		whereParams = "[{" + whereParam + "}]";
	}else{
		whereParams = "[]";
	}
	loadGridForSeletedTreeNodeModule(listViewId, groupByFields, whereParams, gridId, sortType, parentGroup);
	if(groupType == "Multi"){
		var children = tree._get_children(selected_nodes); 
		if ( children[0] == undefined ) {
			var childNodes = getGroupSettingchildNodes(currentNodeName, rootNode, groupByFields, listViewId, parentGroup, whereParams);
			if( childNodes != null && childNodes.length > 0 ){
				for(var i=0; i<childNodes.length; i++) {
					$("#module_relation_tree").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);
				}
			}
		}
	}
}

function loadGridForSeletedTreeNodeModule(listViewId, groupByFields, whereParams, gridId, sortType, sortTypeColumn){
	 var listViewParams=$("#listViewParamsList").val();
	var listViewParam="listViewId="+listViewId+"&listViewParams="+listViewParams+"&searchParams="+encodeURI(whereParams)+"&sortType="+sortType+"&searchType=and&sortTypeColumn="+sortTypeColumn;
	document.location.href = "#bpm/listView/reloadListViewGridModuleGroups";
	_execute("rightPanel",listViewParam);
}


function renderGroupSettingChildNodes(e, data, listViewId, gridId) {
	var groupByFields = [];
	var whereParams = "";
	var whereParam = "";
	$(data.rslt.obj).parents('li').each(function () {
		groupByFields.push($(this).attr("groupName"));
		var groupName = $(this).attr("groupName");
		var groupValue = $(this).attr("name").split("(");
		var searchName=groupValue[0];
		whereParam = whereParam +"'"+ groupName+"':'"+searchName.trim()+"',";
    });
	var groupType = data.rslt.obj.attr("grouptype");
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var parentGroup = data.rslt.obj.attr("groupName");
	var sortType = data.rslt.obj.attr("sortType");
	groupByFields.push(parentGroup);
	var rootNode = data.inst.get_path()[0];
	var nodeLevel = data.inst.get_path().length;
	var selected_nodes = $("#groupSettingTree").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#groupSettingTree")); 
	var splitName=data.rslt.obj.attr("name").split("(");
	var searchName=splitName[0];
	if(gridId == "NOTICE") {
		whereParam = whereParam + ' NOTICE.is_delete '+':'+' false '+',';
	}
	whereParam = whereParam + "'" + data.rslt.obj.attr("groupName")+"':'"+searchName.trim()+"'";

	if(whereParam){
		whereParams = "[{" + whereParam + "}]";
	}else{
		whereParams = "[]";
	}
	loadGridForSeletedTreeNode(listViewId, groupByFields, whereParams, gridId, sortType, parentGroup);
	if(groupType == "Multi"){
		var children = tree._get_children(selected_nodes); 
		if ( children[0] == undefined ) {
			var childNodes = getGroupSettingchildNodes(currentNodeName, rootNode, groupByFields, listViewId, parentGroup, whereParams);
			if( childNodes != null && childNodes.length > 0 ){
				for(var i=0; i<childNodes.length; i++) {
					$("#groupSettingTree").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);
				}
			}
		}
	}
}

function getGroupSettingchildNodes(currentNode, rootNode, groupByFields, listViewId, parentGroup, whereParams){ 
	var data = [];
	$.ajax({
		url: 'bpm/listView/getGroupSettingSelectionChildNodes?currentNode='+currentNode+'&rootNode='+rootNode+'&groupByFields='+JSON.stringify(groupByFields)+'&listViewId='+listViewId+'&parentGroup='+parentGroup+'&whereParams='+whereParams,
        type: 'GET',
        dataType: 'json',
        async: false,
		success : function(response) {
			data=response;
		},
		error : function(response){
			data=response;
			validateMessageBox(form.title.error, form.msg.listViewError , form.title.msgBoxTitleerror);
		}
	});
	return data;
}

function loadGridForSeletedTreeNode(listViewId, groupByFields, whereParams, gridId, sortType, sortTypeColumn){
	var listViewParams=$("#listViewParamsList").val();
	$.ajax({
		url: '/bpm/listView/reloadGridWithSearchParams?listViewId='+listViewId+'&searchParams='+whereParams+'&sortType='+sortType+'&searchType=and&listViewParams='+listViewParams+'&sortTypeColumn='+sortTypeColumn,
        type: 'GET',
        dataType: 'json',
        async: false,
		success : function(response) {
			if(response){
				var gridData = eval(response);
		    	$("#_"+gridId).jqGrid('clearGridData');
				$('#_'+gridId).jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
		    }
		}
	});
}

function _loadGridWithSearchParams(listViewId, groupByFields, whereParams, gridId, sortType, sortTypeColumn, searchType){
	var listViewParams=$("#listViewParamsList").val();
	$.ajax({
		url: '/bpm/listView/reloadGridWithSearchParams?listViewId='+listViewId+'&searchParams='+whereParams+'&sortType='+sortType+'&listViewParams='+listViewParams+'&searchType='+searchType+'&sortTypeColumn='+sortTypeColumn,
        type: 'GET',
        dataType: 'json',
        async: false,
		success : function(response) {
			if(response){
				var gridData = eval(response);
				$("#_"+gridId).jqGrid('clearGridData');
				$('#_'+gridId).jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
		    }
		}
	});
}

function loadListViewTemplate(element_id, listViewId, viewName){
	$.ajax({
		url: 'bpm/listView/getAllListViewTemplateByModule',
        type: 'GET',
        dataType: 'json',
        async: false,
		success : function(response) {
			if(response){
				var listViewTemplate = eval(response);
				$(element_id).append("<option value='' selected='true'></option>");
				var option = "";
				 $.each(listViewTemplate, function(index, item) {
					
					if(item.length>0){
						option += "<optgroup label="+index+">";
						$.each(item,function(key, data){
							option += "<option value='"+data.listViewId+"'>"+data.listViewName+"</option>";
						});
						option +="</optgroup>";
					}
					
		          	//$(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item.listViewName, item.listViewId);
		        	 });
				$(element_id).append(option);
				 
				 if(listViewId){
					setTimeout(function(){
						 $(element_id).val(listViewId);
					},100);
				 }
		    }
		}
	});
}

function showListViewTemplateData(listViewId){
	var container = $("#rightPanel").length > 0 ? "rightPanel" : "target";
	if(listViewId){
		var params = 'listViewId='+listViewId+'&moduleId='+$('#moduleId').val()+'&isEdit=true&isListViewTemplate=true';
	 	document.location.href = "#bpm/listView/showListViewDesignEdit";
	 	_execute(container, params); 
	}else{
		_execute(container, '');
		location.href="#/bpm/listView/showListView";
	}
}

function isOrderNumberExist(orderNumber, gridId){
	var isPresent = false;
	var rowCount = $("#"+gridId).jqGrid('getGridParam', 'records');
	var sel_id = $('#'+gridId).jqGrid('getGridParam', 'selrow');
	if(rowCount > 0){
		var data = $("#"+gridId).jqGrid('getRowData'); 
		for(index = 0; index < data.length; index++){
			if(data[index].orderBy == orderNumber){
				isPresent = true;
				break;
			}
		}
	}
	if(isPresent){
		var gridData = $("#"+gridId).jqGrid('getRowData', sel_id); 
		if(gridData.orderBy == orderNumber){
			isPresent = false;
		}
	}
	return isPresent;
}

function isNameExist(name,gridId){
	var isPresent = false;
	var rowCount = $("#"+gridId).jqGrid('getGridParam', 'records');
	var sel_id = $('#'+gridId).jqGrid('getGridParam', 'selrow');
	if(rowCount > 0){
		var data = $("#"+gridId).jqGrid('getRowData'); 
		for(index = 0; index < data.length; index++){
			var colTitle = "";
			if(gridId == "_LIST_VIEW_GROUP_SETTING_PROPERTY"){
				colTitle = data[index].groupName;
			}else if(gridId == "_LIST_VIEW_COLUMNS_PROPERTY") {
				colTitle = data[index].columnTitle;
			}else if(gridId == "_LIST_VIEW_BUTTONS_PROPERTY") {
				colTitle = data[index].displayName;
			}
			if(colTitle != undefined){
				if(colTitle.toUpperCase() == name.toUpperCase()){
					isPresent = true;
					break;
				}
			}
		}
	}
	if(isPresent){
		var gridData = $("#"+gridId).jqGrid('getRowData', sel_id); 
		var colTitle = "";
		if(gridId == "_LIST_VIEW_GROUP_SETTING_PROPERTY"){
			colTitle = gridData.groupName;
		}else if(gridId == "_LIST_VIEW_COLUMNS_PROPERTY") {
			colTitle = gridData.columnTitle;
		}else if(gridId == "_LIST_VIEW_BUTTONS_PROPERTY") {
			colTitle = gridData.displayName;
		}
		if(colTitle != undefined){
			if(colTitle.toUpperCase() == name.toUpperCase()){
				isPresent = false;
			}
		}
	}
	return eval(isPresent);
}

function showFromQureyWithData(){
	var selectedFromQuery= $("#fromQuery").val();
	var defaultList=[];
	defaultList[0]="Run_Time_Tables";
	defaultList[1]="Meta_Tables";
	defaultTreeNodes=defaultList;
	if(selectedFromQuery!=""){
		showTreeSelectionWithData('Table', 'multi' , 'fromQuery', selectedFromQuery, '', 'bpm/listView/showTableSelectFrom',defaultList);
	}else{
		showTreeSelectionWithData('Table', 'multi' , 'fromQuery', '' , '', 'bpm/listView/showTableSelectFrom',defaultList);
	}
	
}

function showAllTableTree(){
	var defaultList=[];
	defaultList[0]="Run_Time_Tables";
	defaultList[1]="Meta_Tables";
	document.location.href = "#bpm/listView/showTableSelectFrom";
	var param="selectionType=multi&appendTo=allTables&selectedValues=''&callAfter=''&tableList="+JSON.stringify(defaultList)+"&treeType=Tables";
	_execute('target',param);
}

function selectTableColumnsTree(){
	var selectedFromQuery= $("#fromQuery").val();
	var selectedColumns= $("#selectColumns").val();
	var tableNameList=[];
	if(selectedFromQuery!=""){
		var tablesArray=selectedFromQuery.split(",");
		for(var tableIndex=0;tableIndex<tablesArray.length;tableIndex++){
			var tableNameArray=(tablesArray[tableIndex]).split(" AS ");
			tableNameList[tableNameList.length]=tableNameArray[0].replace(/["`"]/g,"");
			defaultTreeNodes[defaultTreeNodes.length]=tableNameArray[0].replace(/["`"]/g,"");
		}
		showTreeSelectionWithData('Columns', 'multi' , 'selectColumns', selectedColumns, '', 'bpm/listView/showTableSelectFrom',tableNameList);
	}else{
		$.msgBox({
			title: form.title.check,
			content: form.msg.tableSelectFirst,
		    type: false,
			 afterClose:function (){
				  $("#fromQuery").focus() ;
			}
		});
	}
}

function exportGridDataToCSV(gridName){
	var colArray=new Array();
	colArray=$(gridObj).getDataIDs();
	var hiddenColumn=[];
	var mymodel = $(gridObj).getGridParam('colModel') // this get the colModel array
	// loop the array and get the hiden columns
			$.each(mymodel, function(i) {
			if(this.hidden == true ) {
				hiddenColumn[hiddenColumn.length]=this.name;
			}
	});
    //Get grid all column name
    var colName=$(gridObj).getRowData(colArray[0]); 
    var colNames=new Array();
    for (var name in colName){colNames[colNames.length]=name;}
    //Get All Grid Datas
    var data =  $(gridObj).jqGrid('getGridParam','data');
    if(data!=""){
    	//Get selected row ids 
    	var selectedRowId =  $(gridObj).jqGrid ('getGridParam', 'selarrrow');
    	//console.log("selectedRowId "+selectedRowId);
	 	var gridExportDatas=[]
	 	if(selectedRowId!=""){
		 	if ( !Array.prototype.forEach ) {
		 		for(var i=0;i<selectedRowId.length;i++){
		 			if(selectedRowId[i].length>=5){
		 				for(var index=0;index<data.length;index++){
		 					if(data[index].id==selectedRowId[i]){
		 						gridExportDatas[gridExportDatas.length]=data[index];
		 					}
		 				}
		 			} else {
		 				gridExportDatas[gridExportDatas.length]=data[selectedRowId[i]-1];
		 			}
		 		}
		 	} else {
		 		selectedRowId.forEach(function(item) {
		 			if(item.length>=5){
		 				data.forEach(function(valuRow) {
		 					if(valuRow.id==item){
		 						gridExportDatas[gridExportDatas.length]=valuRow;
		 					}
		 				});
		 				
		 			}else{
		 				gridExportDatas[gridExportDatas.length]=data[item-1];
		 			}
		 			
		 		});
		 	}
	 	}else{
	 		//all datas
	 		gridExportDatas=data;
	 	}
	 		$("#gridDatas_form").val(JSON.stringify(gridExportDatas));
		 	$("#gridHeader_form").val(colNames);
		 	$("#gridName_form").val(gridName);
		 	$("#grid_hiddenColumn").val(hiddenColumn);
		 	document.getElementById("exportListViewCsvForm").submit();
    }else{
    	$.msgBox({
			title: form.title.check,
			content: form.msg.listViewExport,
		    type: false
		});
    }
 }


function printGridData(obj, gridName){
	var colArray=new Array();
	colArray=$(gridObj).getDataIDs();
    //Get grid all column name
    var colName=$(gridObj).getRowData(colArray[0]); 
    var colNames=new Array();  
    var ii=0;
    for (var i in colName){colNames[ii++]=i;}
    
    //Get All Grid Datas
    var data =  gridObj.jqGrid('getGridParam','data');
   
    if(data!=""){
    	//Get selected row ids 
	 	var selectedRowId =  gridObj.jqGrid ('getGridParam', 'selarrrow');
	 	var gridExportDatas=[]
	 	if(selectedRowId!=""){
	 		//selected datas
	 		for(var index=0;index<selectedRowId.length;index++){
	 				var getRowId=selectedRowId[index]-1;
				 	gridExportDatas[gridExportDatas.length]=data[getRowId];
			}
	 	}else{
	 		//all datas
	 		gridExportDatas=data;
	 	}
	 	
	 	var htmlContent = "<table width='100%'>";
	 	htmlContent = htmlContent + "<tr style='height:20px;background-color:gray;color:#fff;font-weight:bold;'>";
	 	var keys = [];
	 	$.each(data[0], function(key, val) {
	 		keys.push(key);
	 		htmlContent = htmlContent + "<td align='center'>"+key.replace("_"," ")+"</td>";
	 	});
	 	htmlContent = htmlContent + "</tr>";
	 		
	 	for(var index = 0; index < gridExportDatas.length; index++ ){
 			htmlContent = htmlContent + "<tr>";
 			for(var idx = 0; idx < keys.length; idx++){
 				var columnHeader =  keys[idx];
 				var columnData = gridExportDatas[index][columnHeader];
 				htmlContent = htmlContent + "<td style='padding-left:5px'>"+columnData+"</td>";
 			}
 			htmlContent = htmlContent + "</tr>";
	 	}
	 	var htmlContent = htmlContent + "</table>";
	 	document.getElementById("print_preview").innerHTML = htmlContent;
	 	showPrintPreviewForGrid(obj, "print_preview");
	 	
    }else{
    	$.msgBox({
			title: form.title.check,
			content: form.msg.listViewExport,
		    type: false
		});
    }
}


function showPrintPreviewForGrid(obj, id){
	$(obj).printPreview(id);
}

function showListViewsVersion(listViewName,id){
	
	var tempListViewName=listViewName;
	var viewTitle=tempListViewName.replace(/["_"]/g," ")+" Version";
	var listViewParams=[{"viewName":listViewName}];
	showListViewsWithContainerAndParam("SHOW_LIST_VIEW_VERSION",viewTitle, "target",listViewParams);
	
}

function restoreListViewDialog(viewId,viewName){
	var status=false;
	var message="";
	var title="";
	var type="";
	var tempResult=true;
	params = 'viewId='+viewId+'&viewName='+viewName;
	$.msgBox({
	    title: form.msg.listViewRestoreVersion,
	    content: form.msg.listViewActiveListVersion,
	    type: form.title.msgBoxTitleconfirm,
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	$.ajax({	
					url: '/bpm/listView/restoreListView?&'+params,
					type: 'GET',
					async: false,
					dataType: 'json',
					success : function(response) {
						if(response.successMessage){
							title=form.title.success;
							type="success";
							message=response.successMessage;
						}else{
							title=form.title.error;
							type="error";
							message=response.errorMessage;
						}
					}
			  	});
	          }else{
	        	  tempResult=false;
	        }
	    },
	    afterClose : function(){
	    	restoreVersionGrid(title,message,type,viewId,viewName,tempResult);
	   	}
	});
}

function restoreVersionGrid(title,content,type,viewId,viewName,tempResult){
	if(tempResult){
		$.msgBox({
			title: title,
			content: content,
		   	type:type ,
		   	afterClose : function(){
		   		showListViewsVersion(viewName,viewId);
		   	}
		});
	}
}

function getDefaultBUttonsProperty(listViewId, viewName){
	var printFun = 'function printPreviewGridData(){printGridData(this, "'+viewName+'");}';
	var recycleFun = 'function deletedGridData(){recycleGridData(_current_listview_id, "'+viewName+'", 1);}';
	var defaultBtnsProperty = "[{'id':'1','displayName':'Create New','orderBy':'1','buttonMethod':'','iconPath':'','columnName':'','tableName':'','active':'false'},"
							+ "{'id':'2','displayName':'Delete','orderBy':'2','buttonMethod':'default','iconPath':'','columnName':'','tableName':'','active':'false'},"
							+ "{'id':'3','displayName':'Recycle','orderBy':'3','buttonMethod':'"+recycleFun+"','iconPath':'','columnName':'','tableName':'','active':'false'},"
							+ "{'id':'4','displayName':'Restore','orderBy':'4','buttonMethod':'default','iconPath':'','columnName':'','tableName':'','active':'false'},"
							+ "{'id':'5','displayName':'Print','orderBy':'5','buttonMethod':'"+printFun+"','iconPath':'','columnName':'','tableName':'','active':'false'}]";
	return defaultBtnsProperty;
}


function deleteOrRestoreGridData(listViewId, listViewName, isDelete, tableName, columnName){
	var userRole=$("#currentUserRoles").val().split(",");
	var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
	if(!isAdmin){
		validateMessageBox(form.title.validation, form.msg.permissionRestricted , false);
		return false;
	}
	if(tableName != "" && columnName != ""){
		var columnNameAndTitle = columnName.split(",");
		var column = columnNameAndTitle[0].split(".");
		var sel_id = $('#_'+listViewName).jqGrid('getGridParam', 'selarrrow');
		var deleteTblIds = new Array();
		if(sel_id.length > 0){
			var gridData =  gridObj.jqGrid('getGridParam','data');
			for(var i = 0; i < sel_id.length; i++){
				//var gridData = $("#_"+listViewName).jqGrid('getRowData', sel_id[i]);
				if (gridData){
					if(columnNameAndTitle[1] == "id"){
						var row_index = $('#_'+listViewName).jqGrid('getInd',sel_id[i]);
						deleteTblIds.push(gridData[row_index-1][columnNameAndTitle[1]]);
					} else{
						deleteTblIds.push(gridData[sel_id[i]-1][columnNameAndTitle[1]]);
			    		//deleteTblIds.push(gridData[columnNameAndTitle[1]]);
					}
			    }
			}
			var content="";
			if(isDelete){
				content = form.msg.listViewDeleteRecord;
			}else{
				content = form.msg.listViewRestoreRecord;
			}
			$.msgBox({
			    title: form.title.confirm,
			    content: content,
			    type: form.title.msgBoxTitleconfirm,
			    buttons: [{ value: "Yes" }, { value: "No" }],
			    success: function (result) {
			     if (result == "Yes") {
			        	 $.ajax({	
							type: 'POST',
							async: false,
							url : "/bpm/listView/softDeleteTableDataAndRestore?deleteTblIds="+JSON.stringify(deleteTblIds)+"&tableName="+tableName+"&columnName="+column[1]+"&isDelete="+isDelete,
							success : function(response) {
								if(response.success){
									var isDelete = isDelete?1:0;
									var whereParams = [{is_delete:isDelete}];
									_loadGridWithSearchParams(listViewId, "", JSON.stringify(whereParams), listViewName, "asc", "", "and");
								}
							}
						}); 
			        }
			    }	    
			});
			
		}else{
			validateMessageBox(form.title.validation, form.msg.listViewSelectRow , false);
			var gridData = $("#_"+listViewName).jqGrid('getRowData'); 
			for(var i = 0; i < gridData.length; i++){
			    if (gridData[i]){
			    	deleteTblIds.push(gridData[i][columnNameAndTitle[1]]);
			    }
			}
		}
	}else{
		validateMessageBox(form.title.validation, form.msg.listViewSelectColumn , false);
	}
}



function restoreListViewGrid(listViewId, listViewName, isDelete, tableName, columnName){
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isEdit=selected_nodes.attr("isedit");
	
	if(isEdit=='false'){
		validateMessageBox(form.title.validation, form.msg.listViewRestrictRestorePermission , false);
		return false;
	}
		var sel_id = $('#_'+listViewName).jqGrid('getGridParam', 'selarrrow');
		var restoreListViewIds = new Array();
		if(sel_id.length > 0){
			for(var i = 0; i < sel_id.length; i++){
				var gridData = $("#_"+listViewName).jqGrid('getRowData', sel_id[i]);
				if (gridData){
					restoreListViewIds.push(gridData[columnName]);
			    }
			}
			var content = form.msg.listViewRestoreRecord;
			$.msgBox({
			    title: form.title.confirm,
			    content: content,
			    type: form.title.msgBoxTitleconfirm,
			    buttons: [{ value: "Yes" }, { value: "No" }],
			    success: function (result) {
			     if (result == "Yes") {
			        	 $.ajax({	
							type: 'POST',
							async: false,
							url : "/bpm/listView/softDeleteTableDataAndRestore?deleteTblIds="+JSON.stringify(restoreListViewIds)+"&tableName="+tableName+"&columnName="+columnName+"&isDelete="+isDelete,
							success : function(response) {
								if(response.success){
									//var isDelete = isDelete?0:1;
									//var whereParams = [{is_delete:isDelete}];
									//_loadGridWithSearchParams(listViewId, "", JSON.stringify(whereParams), listViewName, "asc", "", "and");
									var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
									var moduleId=selected_nodes.attr("id"); 
									$.ajax({	
										type: 'GET',
										async: false,
										url : "/bpm/module/getAllModule",
										dataType: 'json',
										success : function(response) {
											constructModuleJsTree("module_relation_tree", response.moduleTree, "rightPanel");
											if(moduleId){
												setTimeout(function(){
													$("#module_relation_tree").jstree("select_node", '#'+moduleId);
											 	},100);
											}
										}
									}); 
								}
							}
						}); 
			        }
			    }	    
			});
			
		}else{
			validateMessageBox(form.title.validation, form.msg.listViewSelectRow , false);
			var gridData = $("#_"+listViewName).jqGrid('getRowData'); 
			for(var i = 0; i < gridData.length; i++){
			    if (gridData[i]){
			    	deleteTblIds.push(gridData[i][columnNameAndTitle[1]]);
			    }
			}
		}
}

function recycleGridData(listViewId, listViewName, isDelete){
	var whereParams = [{is_delete:isDelete}];
	_loadGridWithSearchParams(listViewId, "", JSON.stringify(whereParams), listViewName, "asc", "", "and")
}

function processToExecuteQuery(){
	var query=$("#queryArea").val().toUpperCase();
	if(query!=""){
		if(query.indexOf("DROP")!=-1){
			validateMessageBox(form.title.validation, form.msg.queryDropQuery , false);
			return false;
		}
		
		if(query.indexOf("INSERT")!=-1 || query.indexOf("UPDATE")!=-1 || query.indexOf(";")!=-1 ){
			validateMessageBox(form.title.validation, "Invalid Query " , false);
			return false;
		}
		
		var param={ constructedQuery: query }
		 $.ajax({	
			type: 'POST',
			async: false,
			url : '/bpm/listView/validateListViewQuery',
			data: param,
			success : function(response) {
				if(response.success){
					if(query.indexOf("SELECT")!=-1){
						generateListViewByQuery(query);
					}else{
						validateMessageBox(form.title.validation, form.msg.querySuccess , false);
					}
					
				}else{
					$.msgBox({
						title: form.title.error,
						content: response.error,
					    type: form.title.msgBoxTitleerror
					});
				}
			}
		}); 
	
	}else{
		validateMessageBox(form.title.validation, form.msg.queryValidation , false);
	}
}

function generateListViewByQuery(query){
	var params = 'constructedQuery='+encodeURIComponent(query);            	
	document.location.href = "#bpm/table/generateListViewByQuery";
	_execute('rightPanel',params);
}

function changeListViewIdInManageModuleTree(previousVersionListViewId, currentVersionListViewId, currentTab){	
	//$("#"+previousVersionListViewId+"_"+$("#moduleId").val()).attr("id", currentVersionListViewId+"_"+$("#moduleId").val());
 	getAllModuleAsJSON(currentVersionListViewId, $("#moduleId").val(), currentTab, true);
}

function tabNavication(currentTab){
	$("#wizardTab-content-1").removeClass('displayBlock').addClass('displayNone');
	$("#wizardTab-1").parent().removeClass('active-step').addClass('completed-step');
 	if(currentTab == 'column'){
 		$("#wizardTab-2").parent().removeClass('completed-step').addClass('active-step');
 		$("#wizardTab-content-2").removeClass('displayNone').addClass('displayBlock');
 	}else if(currentTab == 'button'){
 		$("#wizardTab-4").parent().removeClass('completed-step').addClass('active-step');
 		$("#wizardTab-content-4").removeClass('displayNone').addClass('displayBlock');
 	}else if(currentTab == 'group'){
 		$("#wizardTab-3").parent().removeClass('completed-step').addClass('active-step');
 		$("#wizardTab-content-3").removeClass('displayNone').addClass('displayBlock');
 	}
}


function getAllModuleAsJSON(listViewId, moduleId, currentTab, isListView){
	$.ajax({	
		type: 'GET',
		async: false,
		url : "/bpm/module/getAllModule",
		dataType: 'json',
		success : function(response) {
			constructModuleJsTree("module_relation_tree", response.moduleTree, "rightPanel");
			if(isListView){
				setTimeout(function(){
					$("#module_relation_tree").jstree("select_node", "#"+listViewId+"_"+moduleId);
					var params = 'listViewId='+listViewId+'&isListViewTemplate=false&isEdit=true&moduleId='+moduleId;
				 	document.location.href = "#bpm/listView/showListViewDesignEdit";
				 	_execute('rightPanel',params);  
				 	setTimeout(function(){
				 		tabNavication(currentTab);
				 	},500);
			 	},100);
			}
		}
	}); 
}

function getListViewObjForParams(listViewName){
	var viewName=listViewName.split("(");
	listViewParams=[];
	var param={ viewName:viewName[0].trim()  }
	 $.ajax({	
			type: 'GET',
			async: false,
			url : '/bpm/listView/getListViewByName',
			data: param,
			success : function(response) {
				if(response!=null && response.length>0){
					var formFieldHtml='<table width="400px;">';
					whereParamMap=response;
					for(var i=0;i<response.length;i++){
						if(response[i]!="listViewObj"){
							formFieldHtml+='<tr><td>'+response[i]+'</td><td>&nbsp;&nbsp;&nbsp;<input type="text" id="param_'+response[i]+'" /></td></tr><tr height="5px;"></tr>';
						}
					}
					formFieldHtml+='<tr align="right"><td></td><td><input type="submit" class="btn btn-primary normalButton padding0 line-height0" value="save" id="saveButton" onClick="getListViewWhereParams();"/></td></tr></table>';
					$myDialog = $("#popupDialog");
					 $myDialog.dialog({
					                            width: 500,
					                            height: 300,
					                            modal: true,
					                            title: form.title.listViewParams,
					                            overflow: 'hidden',
					                            zIndex: 100000
					             });
					 $("#popupDialog").html(formFieldHtml);
					 $myDialog.dialog("open");
					 
				}
			}
	 }); 
 }

function getListViewWhereParams(){
	for(var i=0;i<whereParamMap.length;i++){
		var listViewParamsMap={};
			listViewParamsMap[whereParamMap[i]]=$("#param_"+whereParamMap[i]).val();
			listViewParams[listViewParams.length]=listViewParamsMap;
	}
	$myDialog.dialog("close");
}


function getSingleRowData(listViewName, row_id, columnTitle){
	var gridData = $('#_'+listViewName).jqGrid('getRowData', row_id);
	var gridRowData = [];
	if (gridData){
		if(columnTitle != ""){
			gridRowData.push(gridData[columnTitle]);
		}else{
			gridRowData.push(gridData);
		}
	}
	return JSON.stringify(gridRowData);
}

function getSelectedSingleRowData(listViewName, columnTitle){
	var sel_id = $('#_'+listViewName).jqGrid('getGridParam', 'selrow');
	var gridRowData = [];
	if(sel_id != null){
		var gridData =$('#_'+listViewName).jqGrid('getRowData', sel_id); 
		if (gridData){
			if(columnTitle != ""){
				gridRowData.push(gridData[columnTitle]);
			}else{
				gridRowData.push(gridData);
			}
		}
	}
	return JSON.stringify(gridRowData);
}

function getSelectedMultiRowData(listViewName, columnTitle){
	var sel_id = $('#_'+listViewName).jqGrid('getGridParam', 'selarrrow');
	var gridRowData = [];
	if(sel_id.length > 0){
		for(var i = 0; i < sel_id.length; i++){
		    var gridData = $('#_'+listViewName).jqGrid('getRowData', sel_id[i]); 
		    if (gridData){
		    	if(columnTitle != ""){
					gridRowData.push(gridData[columnTitle]);
				}else{
					gridRowData.push(gridData);
				}
		    }
		}	
	}
	return JSON.stringify(gridRowData);
}

function getListViewColumns(element_id){
	var constructedQuery=constructedViewQuery();
	var param={ constructedQuery:constructedQuery  }
	 $.ajax({	
			type: 'POST',
			async: false,
			url : '/bpm/listView/getAllSelectColumnsWithoutAlice',
			data: param,
			success : function(response) {
				var listViewColumns = eval(response);
				if(listViewColumns!=""){
					$.each(listViewColumns, function(index, item) {
			          	$("#"+element_id).get(0).options[$("#"+element_id).get(0).options.length] = new Option(item.columnName, item.columnId);
			         });
					 $("#"+element_id).prepend("<option value='' selected='selected'></option>");
				}
			}
	 });
}

function getListViewColumnsForSelectedValue(element_id,selectedValue){
	var constructedQuery=constructedViewQuery();
	var param={ constructedQuery:constructedQuery  }
	 $.ajax({	
			type: 'POST',
			async: false,
			url : '/bpm/listView/getAllSelectColumnsWithoutAlice',
			data: param,
			success : function(response) {
				$("#columnName").empty();
				var listViewColumns = eval(response);
				 $.each(listViewColumns, function(index, item) {
				 	/* commanted by ram,because the delete and restore button to show this selected table column in listview button property*/
					// if(item.columnName.indexOf(selectedValue) > -1){
						 $("#"+element_id).get(0).options[$("#"+element_id).get(0).options.length] = new Option(item.columnName, item.columnId);
					// }
		         });
				 $("#"+element_id).prepend("<option  value='' selected='selected'></option>");
			}
	 }); 
}

function refreshListViewGridData(gridId, listViewId, intervalTime){
	var listViewParams=$("#listViewParamsList").val();
	$.ajax({
		url: '/bpm/listView/reloadGridWithSearchParams?listViewId='+listViewId+'&listViewParams='+listViewParams+'&searchParams=[{}]&sortType=asc&searchType=and&sortTypeColumn=',
        type: 'GET',
        dataType: 'json',
        async: false,
		success : function(response) {
			if(response){
				$("#_"+gridId).jqGrid('clearGridData');
				$("#_"+gridId).jqGrid('setGridParam', {page: 1, data: response}).trigger('reloadGrid');
		    }
			if(document.getElementById("_"+gridId) != null){
				setTimeout(function(){
					if($(".grid-refresh").val() > 0){
						refreshListViewGridData(gridId, listViewId, $(".grid-refresh").val());
					}
				}, parseInt($(".grid-refresh").val()));
			}
		}
	});
}

function createNewBtnRedirect(redirectValue){
		$.ajax({
			url: 'bpm/showProcess/getProcessDefinitionsByName?processDefName='+redirectValue,
	        type: 'GET',
	        dataType: 'json',
	        async: false,
			success : function(response) {
				if(response){
					startProcessWithOrganizer(response.id, response.hasStartFormKey.toString(), response.isSystemDefined.toString(),null,false);
				}
			}
		});
}

//For rendering values for auto complete fields in from
function autoCompleteForAllActiveProcess(element_id) {
	var dataList = [];
	$.ajax({
		url: 'bpm/admin/getAllProcess',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(data) {
			$.each(data, function(index, item) {
				dataList.push(item.label);
            });
		}
	});
	
	//Accessing list of values to auto complete
	$('#'+element_id).autocomplete({
	    minLength: 1,
	    source: dataList
	});
	
	//Searching by only start letters
	var escapeRegexp = $.ui.autocomplete.escapeRegex;
	$.extend( $.ui.autocomplete, {
	    escapeRegex: function( pressedLetter ) {
	        return '^' + escapeRegexp(pressedLetter);
	    }
	});
}

//sorting the list view data  
function sortListViewResults(prop, sortOrder) {
	var data =  $(gridObj).jqGrid('getGridParam','data');
	    data = data.sort(function(a, b) {
	    if(a[prop] && b[prop]){
	        if (sortOrder=="asc") return (a[prop].toUpperCase() > b[prop].toUpperCase()) ? 1 : ((a[prop].toUpperCase() < b[prop].toUpperCase()) ? -1 : 0);
	        else return (b[prop].toUpperCase() > a[prop].toUpperCase()) ? 1 : ((b[prop].toUpperCase() < a[prop].toUpperCase()) ? -1 : 0);
	    }
	    });
    }

function showRunTimeTables(){
	var selectedFromQuery= $("#multiTableName").val();
	var defaultList=[];
	defaultList[0]="Run_Time_Tables";
	defaultTreeNodes=defaultList;
	if(selectedFromQuery!=""){
		showTreeSelectionWithData(form.msg.tableExport , 'multi' , 'multiTableName', selectedFromQuery, '', 'bpm/listView/showTableSelectFrom',defaultList);
	}else{
		showTreeSelectionWithData(form.msg.tableExport , 'multi' , 'multiTableName', '' , '', 'bpm/listView/showTableSelectFrom',defaultList);
	}
	
}



function reloadProcessFlowStatiticsGrid(searchParams){
	var srchParams = eval(searchParams);
	if(searchParams != null){
		$.ajax({	
			url: '/bpm/processInstance/reloadFlowStatisticsGridWithSearchParamsAndConstraints?searchParams='+encodeURLStringComponent(searchParams),
			type: 'GET',
			async: false,
			dataType: 'json',
			success : function(response) {
				if(response){
					var departmentId = srchParams[0]["departmentId"];
					var isAdvancedSearch = srchParams[0]["isAdvancedSearch"];
					if(departmentId != "undefined" && departmentId != "" && isAdvancedSearch == "true"){
						var gridData = eval(response);
						$("#user-grid").html(gridData[0]["script"]);
						closeSelectDialog('popupDialog');
						$('#grid_header_links').html($('#header_links').html());
					}else{
						var gridData = eval(response);
				    	$("#_Flow_Statistics").jqGrid('clearGridData');
						$('#_Flow_Statistics').jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
						closeSelectDialog('popupDialog');
					}
			    }
			}
	  	});
	} 
}

function showProcessAudit(processName,processInstId){
	var listViewParams=[{"processInstId":processInstId}];
	showListViewsWithContainerAndParam("JOURNAL_AUDIT",processName, "target",listViewParams);
}

function bulkDeleteListView(){
	
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var listViewIds=[];
	rowid.forEach(function(item) {
		var listViewId = gridObj.jqGrid('getCell', item, 'Id');
		listViewIds[listViewIds.length]=listViewId;
	});
	
	if(rowid.length !=0){
		deleteAllListViewConfirm(listViewIds);
	}else{
		$.msgBox({
   		    title:form.title.message,
   		    content:form.msg.deleteListView
   		});
	}
 
}


function deleteAllListViewConfirm(listViewIds){
	var content = form.msg.listViewVewrsionDelete;
	params = 'listViewIds='+listViewIds;
	var message = "";
	var isSuccess = false;
	var isNeededMsg=true;
	$.msgBox({
	    title: form.title.confirm,
	    content: content,
	    type: form.title.msgBoxTitleconfirm,
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	$.ajax({
	    			type : 'GET',
	    			async : false,
	    			url : '/bpm/listView/deleteBulkListViewDetails?listViewIds='+JSON.stringify(listViewIds),
	    			success : function(response) {
	    				if(response.success){
	    					message = response.success;
	    					isSuccess = true;
	    					 var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	    					 var moduleid=selected_nodes.attr("moduleid");
	    		 			 var listViewParams=[{"moduleid":moduleid}];
	    		 			 showListViewsWithContainerAndParam("LIST_VIEW_RECYCLE", "List View Recycle", "rightPanel",listViewParams);
	    				}else{
	    					message = response.error;
	    				}
	    			}
	    		});
	        }else{
	        	isNeededMsg=false;
	        	return false;
	        }
	    },
	    afterClose : function(){
	    	if(isNeededMsg){
	    		if(isSuccess){
		    		validateMessageBox(form.title.success, message , success);
				}else{
					validateMessageBox(form.title.error, message , form.title.msgBoxTitleerror);
				}
	    	}
	    }
	});
}

function deleteBulkListViewConfirm(listViewIds){
	var content = form.msg.listViewVewrsionDelete;
	var message = "";
	var isSuccess = false;
	var isNeededMsg=true;
	$.msgBox({
	    title: form.title.confirm,
	    content: content,
	    type: form.title.msgBoxTitleconfirm,
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	$.ajax({
	    			type : 'GET',
	    			async : false,
	    			url : '/bpm/listView/deleteBulkListViewDetails?listViewIds='+JSON.stringify(listViewIds),
	    			success : function(response) {
	    				if(response.success){
	    					message = response.success;
	    					isSuccess = true;
	    					showListView("LIST_VIEW", "Manage List View11");
	    				}else{
	    					message = response.error;
	    				}
	    			}
	    		});
	        }else{
	        	isNeededMsg=false;
	        	return false;
	        }
	    },
	    afterClose : function(){
	    	if(isNeededMsg){
	    		if(isSuccess){
		    		validateMessageBox(form.title.success, message , "success");
				}else{
					validateMessageBox(form.title.error, message , form.title.msgBoxTitleerror);
				}
	    	}
	    }
	});
}

