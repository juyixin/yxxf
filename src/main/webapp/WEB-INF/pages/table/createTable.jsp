<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>


<head>
    <title><fmt:message key="createTable.title"/></title>
    <meta name="heading" content="<fmt:message key='createTable.title'/>"/>
</head>

    <script language="javascript">
    var orderNum = 1;
    var dbSource=$("#dbSource").val();
   
    var fieldTypes= [];
    if(dbSource == "oracle" ){
    	fieldTypes=["VARCHAR2","NVARCHAR2","CHAR","NCHAR","NUMBER","BINARY_FLOAT","BINARY_DOUBLE","DATE","TIMESTAMP","CLOB","BLOB"];
    }else if( dbSource == "mysql"){
    	fieldTypes=["VARCHAR","INT","TIMESTAMP","BOOLEAN","TINYINT","SMALLINT","MEDIUMINT","FLOAT","DOUBLE","DECIMAL","NUMERIC","DATE","DATETIME","TIME","YEAR","CHAR","TINYBLOB","TINYTEXT","BLOB","TEXT","MEDIUMBLOB","MEDIUMTEXT","LONGBLOB","LONGTEXT"];	
    }else if(dbSource == "mssql"){
    	fieldTypes=["VARCHAR","INT","TINYINT","SMALLINT","FLOAT","DECIMAL","NUMERIC","DATE","DATETIME","TIME","CHAR","TEXT","NVARCHAR"];
    }
   
    var isAutoFormCreate = '${metaTable.isAutoFormCreate}';
    if(isAutoFormCreate == true || isAutoFormCreate == 'true'){
    	 document.getElementById("isCreateAutomaticForm").checked == true;
    	$('#isCreateAutomaticForm').attr('checked', true);  
    } else {
    	document.getElementById("isCreateAutomaticForm").checked == false;
    	$('#isCreateAutomaticForm').attr('checked', false); 
    }
    var defaultFieldNamesArray = new Array();
    $("#deleteParentTableList").val("[]");
  	$("#deleteSubTableList").val("[]");
  	$("#deleteExistingTableColumns").val("[]");
    changeWizardByTab();
    var isParentTableList=false;
	var isSubTableList=false;
	var parentTableList=[];
	var subTableList=[];
	var deleteRows=[];
	<c:if test="${empty enableRelationTab}" >
    	var tabContent = document.getElementById('wizardTab-content-2');
		$(tabContent).addClass('displayBlock');
		$(tabContent).removeClass('displayNone');
		$(tabContent).siblings().removeClass('displayBlock');
		$(tabContent).siblings().addClass('displayNone');
		$("#wizardTab-2").parent().addClass('active-step');
	    $("#wizardTab-2").parent().siblings().removeClass('active-step');
	    $("#wizardTab-2").parent().siblings().addClass('completed-step');
	</c:if>;

	var isNewTable=false;
    <c:if test="${empty metaTable.metaTableColumns}" >
  		isNewTable=true;
    </c:if>;
	constructTableTree(emptyParentJsonTable(),"parent_table_names");
	constructTableTree(emptySubJsonTable(),"sub_table_names");
    var jsonData = defaultJsonTable();
	constructTableTree(jsonData,"all_table_names");
	
	function constructTableTree(jsonData,treeId){
		var json_data = eval(jsonData);
		$("#"+treeId).jstree({
			"dnd" : {
	            "drop_finish" : function () { 
	                alert("DROP"); 
	            },
	            "drag_check" : function (data) {
	            	if(data.r.attr("id") == "phtml_1") {
	                    return false;
	                }
	                return { 
	                    after : false, 
	                    before : false, 
	                    inside : true 
	                };

	            },
	            "drag_finish" : function () { 
	                alert("DRAG OK"); 
	            }
	        },"crrm" : {
	            "move" : {
	                "check_move" : function (m) {
	                	var p = m.o.attr("name");
	                    var parentId = m.np.attr("id");
	                  	var sourceTree=m.op.attr("id");
	                 
						 if((parentId != 'parent_table_names') && (parentId != "sub_table_names") && (parentId != "all_table_names")){
	                    	var parentDivId = "ul li#"+parentId;
	      					parentId = $(parentDivId).parents('div').attr("id");
	                    }
	                  	
		                    if(p=="Table List" || p=="Parent Table" || p=="Child Table"){
		                    	 return false;
		                    }else if(parentId != "all_table_names"){
		                           var tableId = "";
		                           var subTableId = "";
		                           var treeName="";
		                           if(parentId == 'parent_table_names'){
		                                  tableId = m.o.attr("id");
		                           }else if(parentId == "sub_table_names"){
		                                  tableId = "${metaTable.id}";
		                                  subTableId = m.o.attr("id");
		                           }
		                           var sourceTableName="${metaTable.tableName}";
		                           setTableRelation(tableId, parentId, subTableId,sourceTree,m,sourceTableName);
		                     }else{
		                       return true;   
		                    }
		            }
	            }
	        },
	        
	        "json_data" : {
	        	"data" : json_data
	        },

	        "plugins" : [ "core", "themes", "json_data", "ui" , "dnd", "crrm" ],
	        
	        "ui" : {
	            "initially_select" : [ "json_1" ]
	        },

	        "core" : { "initially_open" : [ "json_1" ] },

	        "themes" : {
	                "theme" : "default"
	        },
	        
	        "types" : {
	            "valid_children" : [ "root" ],
	            "types" : {
	                "root" : {
	                    "icon" : { 
	                        "image" : "../images/drive.png" 
	                    },
	                    "valid_children" : [ "folder" ],
	                    "draggable" : false
	                },
	                "default" : {
	                    "deletable" : false,
	                    "renameable" : false
	                },

	                "folder" : {
	                    "valid_children" : [ "file" ],
	                    "max_children" : 3
	                },
	                "file" : {
	                    // the following three rules basically do the same
	                    "valid_children" : "none",
	                    "max_children" : 0,
	                    "max_depth" : 0,
	                    "icon" : {
	                        "image" : "../images/file.png"
	                    }
	                }

	            }
	        }
	    }).bind("select_node.jstree", function (e, data) {
	    	if(treeId=="all_table_names"){
	    		//window[getTalbleListGrid](e, data);
	    		var currentNode = data.rslt.obj.attr("id");
		    	var rootNode = data.inst.get_path()[0];
		    	var level = data.inst.get_path().length;
		    	var selected_nodes = $(this).jstree("get_selected", null, true); 
		    	var tree = $.jstree._reference(this); 
		    	var children = tree._get_children(selected_nodes); 
		    	if(children[0] != undefined){
		    		children.remove();
		    	}
		    	var parentNode = "";
		    	parentNode = currentNode;
	    	}
		   });/* .bind("move_node.jstree", function(e, data) {
			 	   var parentId = data.rslt.np.attr("id");
				  if((parentId != 'parent_table_names') && (parentId != "sub_table_names") && (parentId != "all_table_names")){
					  var parentDivId = "ul li#"+parentId;
					  parentId = $(parentDivId).parents('div').attr("id");
				  }
				  
				  if(parentId != "all_table_names"){
                     var tableId = "";
                     var subTableId = "";
                     if(parentId == 'parent_table_names'){
                            tableId = data.rslt.o.attr("id");
                     }else if(parentId == "sub_table_names"){
                            tableId = "${metaTable.id}";
                            subTableId = data.rslt.o.attr("id");
                     }
                        selectForeignKey(tableId, parentId, subTableId);
                  }

			 }) */
	}

	function defaultJsonTable(){
		var jsonData =''; 
		var currentTableId=$("#id").val();
			jsonData = '[{ "data" : "Table List", "attr" : { "id" : "tables", "name" : "Table List","foreignKeyColumnId":"Column_ID","foreignKeyColumnName":"Column_NAME","childKeyColumnid":"childColumnid","childKeyColumnName": "childColumnName" },"metadata": {"id" : "Table List", "name" : "Table List"}},';
			
			<c:forEach items="${tableList}" var="tables" varStatus="status">
				if("${tables.id}" != "" && "${tables.id}"!=currentTableId){	
					jsonData +='{ "data" : "${tables.tableName}", "attr" : { "id" : "${tables.id}", "name" : "${tables.tableName}","foreignKeyColumnId":"Column_ID","foreignKeyColumnName":"Column_NAME","childKeyColumnid":"childColumnid","childKeyColumnName": "childColumnName" },"metadata": {"id" : "tables", "name" : "${tables.tableName}"}},';
				}
			</c:forEach>;
			jsonData+=']';
			
		
		return jsonData;
	} 
	/* function defaultJsonTable(){
		var jsonData = '[{ "data" : "Tables",  "attr" : { "id" : "tables", "name" : "Tables","parent" : "Tables" },"metadata": {"id" : "tables", "name" : "Tables"}}]';
		return jsonData;
	} */
	
	function emptyParentJsonTable(){
	   var jsonData =''; 
		if(isNewTable){
			jsonData = '[{ "data" : "Parent Table",  "attr" : { "id" : "parentTable", "name" : "Parent Table","foreignKeyColumnId":"Column_ID","foreignKeyColumnName":"Column_NAME","childKeyColumnid":"childColumnid","childKeyColumnName": "childColumnName" },"metadata": {"id" : "parentTable", "name" : "Parent Table"}}]';
		}else{
			jsonData = '[{ "data" : "Parent Table", "attr" : { "id" : "parentTable", "name" : "Parent Table","foreignKeyColumnId":"Column_ID","foreignKeyColumnName":"Column_NAME","childKeyColumnid":"childColumnid","childKeyColumnName": "childColumnName" },"metadata": {"id" : "ParentTable", "name" : "Parent Table"}},';
		
			<c:forEach items="${metaTable.metaTableRelation}" var="tables" varStatus="status">
				<c:if test="${not empty tables.parentTable}" >
				isParentTableList=true;
				parentTableList[parentTableList.length]="${tables.parentTable.id}";
					jsonData +='{ "data" : "${tables.parentTable.tableName}(${tables.foreignKeyColumnName}->${tables.childKeyColumnName})", "attr" : { "id" : "${tables.parentTable.id}", "name" : "${tables.parentTable.tableName}","foreignKeyColumnId":"${tables.foreignKeyColumnId}","foreignKeyColumnName":"${tables.foreignKeyColumnName}","childKeyColumnid":"${tables.childKeyColumnId}","childKeyColumnName": "${tables.childKeyColumnName}" },"metadata": {"id" : "${tables.parentTable.id}", "name" : "${tables.parentTable.tableName}"}},';
				 </c:if>;
			</c:forEach>;
		jsonData+=']';
		}   
		
		return jsonData;
	}
	
	function emptySubJsonTable(){
		var jsonData =''; 
		if(isNewTable){
			jsonData = '[{ "data" : "Child Table",  "attr" : { "id" : "childTable", "name" : "Child Table","foreignKeyColumnId":"Column_ID","foreignKeyColumnName":"Column_NAME","childKeyColumnid":"childColumnid","childKeyColumnName": "childColumnName" },"metadata": {"id" : "childTable", "name" : "Child Table"}}]';	
		}else{ 
			jsonData = '[{ "data" : "Child Table", "attr" : { "id" : "childTable", "name" : "Child Table","foreignKeyColumnId":"Column_ID","foreignKeyColumnName":"Column_NAME","childKeyColumnid":"childColumnid","childKeyColumnName": "childColumnName" },"metadata": {"id" : "childTable", "name" : "Child Table"}},';
			
			<c:forEach items="${metaTable.metaTableRelation}" var="tables" varStatus="status">
				<c:if test="${not empty tables.childTable}" >
				isSubTableList=true;
				subTableList[subTableList.length]="${tables.childTable.id}";
					jsonData +='{ "data" : "${tables.childTable.tableName}(${tables.foreignKeyColumnName}->${tables.childKeyColumnName})", "attr" : { "id" : "${tables.childTable.id}", "name" : "${tables.childTable.tableName}","foreignKeyColumnId":"${tables.foreignKeyColumnId}","foreignKeyColumnName":"${tables.foreignKeyColumnName}","childKeyColumnid":"${tables.childKeyColumnId}","childKeyColumnName": "${tables.childKeyColumnName}" },"metadata": {"id" : "${tables.childTable.id}", "name" : "${tables.childTable.tableName}"}},';
				 </c:if>;
			</c:forEach>;
			jsonData+=']'; 
		}
		return jsonData;
	}

    $(function () {
		$("#module_title").html("");
		var module_title = "";
		if(isNewTable){
			//新增操作，初始化固定字段
			var htmlContent = "<TABLE id='defaultValueTable' cellpadding='1' cellspacing='1'  width='100%'>";
	   		var defaultList = $("#tableDefaultValues_"+dbSource).val();
	   		var defaultFields = defaultList.split('-');
			for(var index = 0; index < defaultFields.length; index++){
				var columnField=defaultFields[index].split(',');
				defaultFieldNamesArray[defaultFieldNamesArray.length]=columnField[0].toUpperCase();
				htmlContent = htmlContent + "<tr><td style='padding: 3px;'> <input style='width:30px;'  type='text' readonly='true'  value='"+columnField[0]+"' /></td>"
				+"<td style='padding: 3px;'> <input style='width:125px;'  type='text' readonly='true'  value='"+columnField[1]+"' /></td>"
				+"<td style='padding: 3px;'> <input style='width:125px;' readonly='true' type='text' value='"+columnField[2]+"' /></td>"
				+"<td style='padding: 3px;'> <select style='width:135px;margin-bottom:0px;'  readonly='true' value='"+columnField[3]+"'><option value='"+columnField[3]+"'>"+columnField[3]+"</option></select></td>"
				+"<td style='padding: 3px;'> <input style='width:30px;' readonly='true' type='text' value='"+columnField[4]+"' /></td>"
				+"<td style='padding: 3px;'> <input style='width:75px;' readonly='true' type='text' value='"+columnField[5]+"' /></td>"
				+"<td style='padding: 3px;'> <input style='width:125px;' readonly='true' type='text' /></td>"
				+"<td style='padding: 3px;'> <input style='width:15px;' disabled='disabled' type='checkbox' "+columnField[6];
				if(columnField[6]=='true'){
					htmlContent=htmlContent+" checked='checked'";
				}
				htmlContent=htmlContent+"/></td>"
				+"<td style='padding: 3px;'> <input  style='width:15px;' disabled='disabled' type='checkbox' "+columnField[7];
				if(columnField[7]=='true'){
					htmlContent=htmlContent+" checked='checked'";
				}
				htmlContent=htmlContent+"/></td>"
				+"<td style='width:20px;'></td><td style='width:25px;'></td></tr>";
			}
			htmlContent =htmlContent+"</table>";
			$("#defaultValueTableDiv").html(htmlContent);
			module_title = $("#createTableHeader").html();
			$("#module_title").html(module_title);
	   }else{
		   
		   if("${isFieldEdit}"){
			    $("#tableNameRow").hide();
			    $("#chineseNameRow").hide();
			    $("#descriptionRow").hide();
			    $("#fieldPropertiesLableId").hide();
			    $("#moduleRow").hide();
			    module_title = $("#alterTableColumns").html();
				$("#module_title").html(module_title);
		   }else{
			   module_title = $("#alterTableHeader").html();
			   $("#module_title").html(module_title);
		   }
			alterTableRow('alterTableValues');
		}
		addRow('dataTable');
    });
    
    
 
	$(function() {
		$("#popupDialog").dialog("option", "position", "center");
	});
	
	$(window).resize(function() {
	    $("#popupDialog").dialog("option", "position", "center");
	});

    function addRow(tableID) {
       	var table = document.getElementById(tableID);
        var rowCount = table.rows.length;
    		  
    	var row = table.insertRow(rowCount);
    	
    	var cell0 = row.insertCell(0);
        var element0 = document.createElement("input");
        cell0.style.padding="3px";
        element0.type = "text";
        element0.style.width = "30px";
        element0.value=orderNum++;
        cell0.appendChild(element0);

        var cell1 = row.insertCell(1);
        var element1 = document.createElement("input");
        cell1.style.padding="3px";
        element1.type = "text";
        element1.style.width = "125px";
        cell1.appendChild(element1);
        
        var cell2 = row.insertCell(2);
        var element2 = document.createElement("input");
        cell2.style.padding="3px";
        element2.type = "text";
        element2.style.width = "125px";
        cell2.appendChild(element2);
        
        var cell3 = row.insertCell(3);
        var element3 = document.createElement("select");
        cell3.style.padding="3px";
        element3.style.width = "135px";
        element3.style.marginBottom = "0px";
        
        for (var i=0;i<fieldTypes.length;i++){
			var option;
			option = document.createElement("option");
			option.setAttribute("value", fieldTypes[i]);
			option.innerHTML = fieldTypes[i];
			element3.appendChild(option);
	    }
        element3.setAttribute("onChange", "setDefaultValueInTable(this,"+rowCount+",'"+tableID+"');activateAutoId(this,"+rowCount+",'"+tableID+"');");
        cell3.appendChild(element3);
        
        
        var cell4 = row.insertCell(4);
        var element4 = document.createElement("input");
        cell4.style.padding="3px";
        element4.style.width = "30px";
        element4.type = "text";
        element4.value="100";
       	cell4.appendChild(element4);
       	
        var cell5 = row.insertCell(5);
        var element5 = document.createElement("input");
        cell5.style.padding="3px";
        element5.style.width = "75px";
        element5.type = "text";
       	cell5.appendChild(element5);
       	
       	var autoId = row.insertCell(6);
        var autoIdElement = document.createElement("input");
        autoId.style.padding="3px";
        autoIdElement.type = "text";
        autoIdElement.style.width = "125px";
        autoId.appendChild(autoIdElement);
        
    	var isUniqueKey = row.insertCell(7);
        var uniqueKeyElement = document.createElement("input");
        isUniqueKey.style.padding="3px";
        uniqueKeyElement.type = "checkbox";
        uniqueKeyElement.style.width = "15px";
        uniqueKeyElement.setAttribute("isUnique", "false");
        uniqueKeyElement.setAttribute("onClick", "handleClick(this,'isUnique',"+rowCount+",'"+tableID+"');");
        isUniqueKey.appendChild(uniqueKeyElement);
        
        var isCompositeKey = row.insertCell(8);
        var compositeKeyElement = document.createElement("input");
        isCompositeKey.style.padding="3px";
        compositeKeyElement.type = "checkbox";
        compositeKeyElement.style.width = "15px";
        compositeKeyElement.setAttribute("isComposite", "false");
        compositeKeyElement.setAttribute("onClick", "handleClick(this,'isComposite',"+rowCount+",'"+tableID+"');");
        isCompositeKey.appendChild(compositeKeyElement);
        
        var cell7 = row.insertCell(9);
        var element7 = document.createElement("img");
        cell7.style.padding="3px";
        element7.setAttribute("src", "/images/add.gif");
        element7.setAttribute("onClick", "addRow('dataTable')");
        cell7.appendChild(element7);
       
        var cell8 = row.insertCell(10);
        var element8 = document.createElement("img");
        cell8.style.padding="3px";
        element8.setAttribute("src", "/images/delete.gif");
        element8.setAttribute("onClick", "deleteTableRow(this)");
        cell8.appendChild(element8);
        
 	}
    
    function activateAutoId(typeCombo,rowCount,tableID){
    	var table = document.getElementById(tableID);
    	var row = table.rows[rowCount];
        if(typeCombo.value=="VARCHAR" || typeCombo.value=="VARCHAR2" || typeCombo.value=="NVARCHAR2" ){
        	 row.cells[6].childNodes[0].removeAttribute("readonly");
    	}else{
    		 row.cells[6].childNodes[0].setAttribute("readonly", true);
    		 row.cells[6].childNodes[0].value="";
    	}
    }
    
    function alterTableRow(tableID) {
    	var editableList = new Array();
       	var nonEditableList=new Array();
       	var tempEditCount=0;
       	var tempNONEditCount=0;
       	sourceTableColumns={};
   	 	<c:forEach items="${metaTable.metaTableColumns}" var="columnsField" varStatus="status">
   			var isReadOnly=false;
			var defaultList = $("#tableDefaultValues_"+dbSource).val();
			var defaultFields = defaultList.split('-');
			 for(var index = 0; index < defaultFields.length; index++){
				 var columnField=defaultFields[index].split(',');
				 if(columnField[1]=="${columnsField.name}"){
					 isReadOnly=true;
				 }
			}
			 
			 var columnsFieldLength="";
			 if(${columnsField.length}!=0){
				 columnsFieldLength	= "${columnsField.length}";
             }
			  
			 if(isReadOnly){
				 var mapObj = {};
				    mapObj['orderNum'] = "${columnsField.orderNum}";
				 	mapObj['id'] = "${columnsField.id}";
	                mapObj['columnName'] = "${columnsField.name}";
	                mapObj['columnType'] = "${columnsField.type}";
	                mapObj['columnSize'] = columnsFieldLength;
	                mapObj['columnChineseName'] = "${columnsField.chineseName}";
	                mapObj['defaultValue'] = "${columnsField.defaultValue}";
	                mapObj['autoGenerationId'] = "";
	                mapObj['isReadOnly'] = true;
	                mapObj['isUniquekey'] = ${columnsField.isUniquekey};
	                mapObj['isCompositeKey'] = ${columnsField.isCompositeKey};
	             	nonEditableList[tempNONEditCount]=mapObj;
				 	tempNONEditCount++;
				 	sourceTableColumns["${columnsField.id}"]="${columnsField.name}";
			 }else{
				 var mapObj = {};
				    mapObj['orderNum'] = "${columnsField.orderNum}";
				 	mapObj['id'] = "${columnsField.id}";
	                mapObj['columnName'] = "${columnsField.name}";
	                mapObj['columnType'] = "${columnsField.type}";
	                mapObj['columnSize'] = columnsFieldLength;
	                mapObj['columnChineseName'] = "${columnsField.chineseName}";
	                mapObj['defaultValue'] = "${columnsField.defaultValue}";
	                mapObj['autoGenerationId'] = "${columnsField.autoGenerationId}";
	                mapObj['isReadOnly'] = false;
	                mapObj['isUniquekey'] = ${columnsField.isUniquekey};
	                mapObj['isCompositeKey'] = ${columnsField.isCompositeKey};
		            editableList[tempEditCount]=mapObj;
				 	tempEditCount++;
				 	orderNum++;
				 	sourceTableColumns["${columnsField.id}"]="${columnsField.name}";
			 }
		</c:forEach>;
		
		constructTableForAlterTable(editableList,tableID);
		constructTableForAlterTable(nonEditableList,tableID);
		
	}
    
    function constructTableForAlterTable(editableList,tableID){
    	var table = null;
        var rowCount = 0;
    	for(var index=0;index<editableList.length;index++){
			table=document.getElementById(tableID);
		 	rowCount = table.rows.length;
		 	
	        var row = table.insertRow(rowCount);
			
	        
	        var cell0 = row.insertCell(0);
	        var element0 = document.createElement("input");
	        cell0.style.padding="3px";
	        element0.type = "text";
	        element0.value=editableList[index].orderNum;
	        element0.setAttribute("orderNum", editableList[index].orderNum);
	        if(editableList[index].isReadOnly){
	        	element0.setAttribute("readonly", true);
	        }
	        element0.style.width = "30px";
	        cell0.appendChild(element0);
	        
	        var cell1 = row.insertCell(1);
	        var element1 = document.createElement("input");
	        cell1.style.padding="3px";
	        element1.type = "text";
	        element1.value=editableList[index].columnName;
	        element1.setAttribute("id", editableList[index].id);
	        if(editableList[index].isReadOnly){
	        	element1.setAttribute("readonly", true);
	        }
	        element1.style.width = "125px";
	        cell1.appendChild(element1);
	        
	        var cell2 = row.insertCell(2);
	        var element2 = document.createElement("input");
	        cell2.style.padding="3px";
	        element2.value=editableList[index].columnChineseName;
	        element2.type = "text";
	        if(editableList[index].isReadOnly){
	        	element2.setAttribute("readonly", true);
	        }
	        element2.style.width = "125px";
	        cell2.appendChild(element2);
	        
	        var cell3 = row.insertCell(3);
	        var element3 = document.createElement("select");
	        cell3.style.padding="3px";
	        element3.style.width = "135px";
	        element3.style.marginBottom = "0px";
	        if(!editableList[index].isReadOnly){
		        for (var i=0;i<fieldTypes.length;i++){
					var option;
					option = document.createElement("option");
					option.setAttribute("value", fieldTypes[i]);
					option.innerHTML = fieldTypes[i];
					element3.appendChild(option);
			    }
	        }else{
	        	element3.setAttribute("readonly", true);
	        	var option = document.createElement("option");
				option.setAttribute("value", editableList[index].columnType);
				option.innerHTML = editableList[index].columnType;
				element3.appendChild(option);
	        }
	        element3.setAttribute("onChange", "setDefaultValueInTable(this,"+rowCount+",'"+tableID+"');activateAutoId(this,"+rowCount+",'"+tableID+"')");
	        element3.value=editableList[index].columnType;
	        cell3.appendChild(element3);
	        
	        
	        var cell4 = row.insertCell(4);
	        var element4 = document.createElement("input");
	        cell4.style.padding="3px";
	        element4.style.width = "30px";
	        element4.value=editableList[index].columnSize;
	        element4.type = "text";
	        if(editableList[index].isReadOnly || editableList[index].columnType == "LONGTEXT" || editableList[index].columnType == "DATE"
	        		|| editableList[index].columnType == "DATETIME"){
	        	element4.setAttribute("readonly", true);
	        }
	       	cell4.appendChild(element4);
	       	
	        var cell5 = row.insertCell(5);
	        var element5 = document.createElement("input");
	        cell5.style.padding="3px";
	        if(editableList[index].defaultValue == "NULL"){
	        	element5.value = "";
	        } else {
	        	 element5.value = editableList[index].defaultValue;
	        }
	        element5.style.width = "75px";
	        element5.type = "text";
	        if(editableList[index].isReadOnly){
	        	element5.setAttribute("readonly", true);
	        }
	       	cell5.appendChild(element5);
	       	
	       	var autoId = row.insertCell(6);
	        var autoIdElement = document.createElement("input");
	        autoId.style.padding="3px";
	        autoIdElement.value=editableList[index].autoGenerationId;
	        autoIdElement.type = "text";
	        autoIdElement.style.width = "125px";
	        if(editableList[index].isReadOnly || editableList[index].columnType!="VARCHAR"){
	        	autoIdElement.setAttribute("readonly", true);
	        }
	        autoId.appendChild(autoIdElement);
	        
	        var isUniqueKey = row.insertCell(7);
	        var uniqueKeyElement = document.createElement("input");
	        isUniqueKey.style.padding="3px";
	        uniqueKeyElement.type = "checkbox";
	        uniqueKeyElement.style.width = "15px";
	        if(editableList[index].isReadOnly){
	        	uniqueKeyElement.setAttribute("disabled", "disabled");
	        }
	        if(editableList[index].isUniquekey){
	        	uniqueKeyElement.setAttribute("checked", "checked");
	        	uniqueKeyElement.setAttribute("isUnique", "true");
	        }else{
	        	uniqueKeyElement.setAttribute("isUnique", "false");	
	        }
	        
	        uniqueKeyElement.setAttribute("onClick", "handleClick(this,'isUnique',"+rowCount+",'"+tableID+"');");
	        isUniqueKey.appendChild(uniqueKeyElement);
	        
	        var isCompositeKey = row.insertCell(8);
	        var compositeKeyElement = document.createElement("input");
	        isCompositeKey.style.padding="3px";
	        compositeKeyElement.type = "checkbox";
	        compositeKeyElement.style.width = "15px";
	        if(editableList[index].isReadOnly){
	        	compositeKeyElement.setAttribute("disabled", "disabled");
	        }
	        
	        if(editableList[index].isCompositeKey){
	        	compositeKeyElement.setAttribute("checked", "checked");
	        	compositeKeyElement.setAttribute("isComposite", "true");
	        }else{
	        	compositeKeyElement.setAttribute("isComposite", "false");
	        }
	        compositeKeyElement.setAttribute("onClick", "handleClick(this,'isComposite',"+rowCount+",'"+tableID+"');");
	        isCompositeKey.appendChild(compositeKeyElement);
	        
	        var cell7 = row.insertCell(9);
	        var element7 = document.createElement("img");
	        cell7.style.padding="3px";
	        if(editableList[index].isReadOnly){
	        	cell7.style.width="20px";
	        }else{
	        	element7.setAttribute("src", "/images/delete.gif");
		        element7.setAttribute("onClick", "deleteExistingRow(this)");
		        cell7.appendChild(element7);
	        }
	        
	       
	        var cell8 = row.insertCell(10);
	        //var element7 = document.createElement("img");
	        cell8.style.padding="3px";
	        cell8.style.width="20px";
	        //cell7.appendChild(element7);
		} 
    }
    
    function handleClick(cp,attrKey,rowCount,tableID){
    	$(cp).attr(attrKey,cp.checked);
    	var table = document.getElementById(tableID);
    	var row = table.rows[rowCount];
    	
    	if(attrKey=="isUnique"){
    		var isCompo = row.cells[8].childNodes[0].getAttribute("isComposite");
    		if(isCompo=="true"){
    			row.cells[8].childNodes[0].setAttribute("isComposite", "false");
    			$(row.cells[8].childNodes[0]).prop("checked", false);
    		}
    	}else{
    		var isUniq = row.cells[7].childNodes[0].getAttribute("isUnique");
    		if(isUniq=="true"){
    			row.cells[7].childNodes[0].setAttribute("isUnique", "false");
    			$(row.cells[7].childNodes[0]).prop("checked", false);
    		}
    	}
    }
    
    function deleteTableRow(tabObj) {
    	var rowsIndex=$(tabObj).closest('td').parent()[0].sectionRowIndex;
    	var table = document.getElementById('dataTable');
    	var tableRow=table.rows.length;
    	try {
        	if(tableRow>=2){
        		table.deleteRow(rowsIndex);	
        	}
        }catch(e) {
            alert(e);
        }
    }
    
    
    function deleteExistingRow(tabObj) {
    	var rowsIndex=$(tabObj).closest('td').parent()[0].sectionRowIndex;
    	var table = document.getElementById('alterTableValues');
    	try {
        	var defaultList = $("#tableDefaultValues_"+dbSource).val();
        	var defaultFields = defaultList.split('-');
        	if(table.rows.length>=(defaultFields.length+1)){
            	var row=table.rows[rowsIndex];
           		var columnName = row.cells[1].childNodes[0];
           		deleteRows[deleteRows.length]=columnName.getAttribute('id');
           		table.deleteRow(rowsIndex);	
        	}else{
        		console.log("i cant "+rowsIndex);
        	}	
        }catch(e) {
            alert(e);
        }
    }
    
		 function checkTableName(tableID){
			 var isTableChecked=false;
			
			 if(isNewTable){
				 var tableName = $("#tableName").val() ;
				 var isCreateAutomaticForm = $('#isCreateAutomaticForm').is(":checked");
					if (tableName!="") {
						 if(/^[a-zA-Z][a-zA-Z0-9_]*$/.test(tableName) != false) {
							 var table_name = tableName.replace(/[" "]/g,"_");
							if(table_name.length<=56){
								$.ajax({	
									type: 'GET',
									async: false,
									url : '/table/checkTableName',
									data: 'tableName=' + table_name + '&isCreateAutomaticForm=' + isCreateAutomaticForm,
									success : function(response) {
										var tableObj=response.tableOperationObj;
										if(tableObj=="true"){
											$.msgBox({
											    title: form.title.tablename,
											    content: form.error.tableNameExist,
											    buttons: [{ value: "Ok" }],
									      	    success: function (result, values) {
									      	    	resetWizardDiv();
									      	    	$("#tableName").val("") ;
									      	    	$("#tableName").focus() ;
									      	    	
									      	    }
									   		});
										}else{
											if(checkTableModuleName()){
												isTableChecked=checkFieldValue(tableID);
											 	if(isTableChecked){
													getData('dataTable');
												}	
											}
										}
									}
								});
							}else{
								$.msgBox({
			             		    title:form.title.check,
			             		    content:form.error.tableNameValidation,
	                   		    	afterClose:function (){
	                   		    	resetWizardDiv();
	                   		    	$("#tableName").focus() ;
	                   		    }
			             		});
			                }
							
						}else{
							$.msgBox({
		             		    title:form.title.check,
		             		    content:form.error.tableNameSpecialCharacterValidation,
                   		    afterClose:function (){
                   		    	resetWizardDiv();
                   		    	$("#tableName").focus() ;
                   		    }
		             		});
		                }
					}else{
						if(tableName==""){
							$.msgBox({
							    title: form.title.tablename,
							    content: form.error.tableNameEmptyValidation,
							    afterClose:function (){
							    	resetWizardDiv();
							    	$("#tableName").focus() ;
							    }
							});
						}
						
					} 
			 }else{
				 if(checkTableModuleName()){
					 isTableChecked=checkFieldValue(tableID);
						isAlterTable=checkFieldValue('alterTableValues');
						if(isTableChecked && isAlterTable ){
							getData('dataTable');
							return true;
						}else{
							return false;
						}
				 }else{
					 return false;
				 }
			 }
			 
			 return isTableChecked;
		}
		 
		 function checkTableModuleName(){
			 var modulename = $("#modules").val() ;
			 if(modulename==""){
				 $.msgBox({
					    title: form.title.moduleName,
					    content: form.error.moduleNameEmptyValidation,
					    afterClose:function (){
					    	resetWizardDiv();
					    	$("#modules").focus() ;
            		    }
					});
				 return false;
			 }else{
				 return true;
			 }
		 }
		 
        function getData(tableID) {
        	try {
        		 var table = document.getElementById(tableID);
                 var rowCount = table.rows.length;
            	 var jsonArray = new Array();
            	 
            
	            for(var index=0; index < rowCount; index++) {
	            	var mapObj = {};
	            	var row = table.rows[index];
	            	var orderNum = row.cells[0].childNodes[0];
	                var columnName = row.cells[1].childNodes[0];
	                var columnChineseName = row.cells[2].childNodes[0];
	                var columnType = row.cells[3].childNodes[0];
	                var columnSize = row.cells[4].childNodes[0];
	                var defaultValue = row.cells[5].childNodes[0];
	                var autoGenerationId = row.cells[6].childNodes[0];
	                var isUniqueKey = row.cells[7].childNodes[0];
	                var isCompositeKey = row.cells[8].childNodes[0];
	                var assignDefaultValue=""
	              
	                if(defaultValue.value==""){
	                	// form is not saving if default value is NULL ( for INTEGER ). so changed to 0 if default value is empty
	                	if(columnType.value == "INT" || columnType.value == "DECIMAL" || columnType.value == "DOUBLE" ||  columnType.value == "YEAR" ||  columnType.value == "MEDIUMINT" || columnType.value == "FLOAT" || columnType.value == "NUMERIC" || columnType.value == "BOOLEAN" ) {
	                			assignDefaultValue= 0;	
	                   } else {
		                	assignDefaultValue="NULL";
	                	}
	                }else{
	                	assignDefaultValue=defaultValue.value;
	                }
	                
	                if(columnName.value != null && columnName.value != ""){
	                	mapObj['id'] = null;
	                	mapObj['orderNum'] = orderNum.value;
		                mapObj['columnName'] = (columnName.value == null || columnName.value == "") ? "" : columnName.value;
		                mapObj['columnType'] = columnType.value;
		                mapObj['columnSize'] = columnSize.value;
		                mapObj['columnChineseName'] = (columnChineseName.value == null || columnChineseName.value == "") ? "" : columnChineseName.value;
		                mapObj['defaultValue'] = assignDefaultValue;
		                mapObj['autoGenerationId'] = (autoGenerationId.value == null || autoGenerationId.value == "") ? "" : autoGenerationId.value;
		                mapObj['isUniqueKey'] = isUniqueKey.checked;
		                mapObj['isCompositeKey'] = isCompositeKey.checked;
		                jsonArray[jsonArray.length] = mapObj;
	
	                }
	            }
	            jsonArray=addDefaultColumnValues(rowCount,jsonArray);
	            
	            $("#fieldPropertiesList").val(JSON.stringify(jsonArray));	
	            
	        }catch(e) {
                alert(e);
            }
        }
        
        function addDefaultColumnValues(rowCount,jsonArray){
        	var selectedParentTableList=[];
        	var selectedSubTableList=[];
        	if(isNewTable){
        		 var defaultList = $("#tableDefaultValues_"+dbSource).val();
     			var defaultFields = defaultList.split('-');
     			 for(var temp = 0; temp < defaultFields.length; temp++){
     				 var mapObj = {};
     				 var columnField=defaultFields[temp].split(',');
     				    mapObj['orderNum'] = columnField[0];
     				    mapObj['columnName'] = columnField[1];
     				    mapObj['columnChineseName'] = columnField[2];
     	                mapObj['columnType'] = columnField[3];
     	                mapObj['columnSize'] = columnField[4];
     	                mapObj['defaultValue'] = columnField[5];
     	                mapObj['autoGenerationId'] = "";
     	              	mapObj['isUniqueKey'] = columnField[6];
     	              	mapObj['isCompositeKey'] = columnField[7];
     	                jsonArray[jsonArray.length]=mapObj;
     	                rowCount++;
     			}
     			 selectedParentTableList=getSelectedTableList('parent_table_names','parentTable');
 	        	 selectedSubTableList=getSelectedTableList('sub_table_names','childTable');
 	        	$("#selectedParentTableList").val(JSON.stringify(selectedParentTableList));	
 	        	$("#selectedSubTableList").val(JSON.stringify(selectedSubTableList));	 
     			return jsonArray;
        	 }else{
        		 var table = document.getElementById('alterTableValues');
                 var alterTableRowCount = table.rows.length;
            	for(var index=0; index < alterTableRowCount; index++) {
	            	var mapObj = {};
	            	var row = table.rows[index];
	            	var orderNum = row.cells[0].childNodes[0];
	            	var columnName = row.cells[1].childNodes[0];
	            	var columnChineseName = row.cells[2].childNodes[0];
	                var columnType = row.cells[3].childNodes[0];
	                var columnSize = row.cells[4].childNodes[0];
	                var defaultValue = row.cells[5].childNodes[0];
	                var autoGenerationId = row.cells[6].childNodes[0];
	                var assignDefaultValue=""
                	var isUniqueKey = row.cells[7].childNodes[0];
	                var isCompositeKey = row.cells[8].childNodes[0];
	                if(defaultValue.value==""){
	                	// form is not saving if default value is NULL ( for INTEGER ). so changed to 0 if default value is empty
	                	if(columnType.value == "INT" || columnType.value == "DECIMAL" || columnType.value == "DOUBLE" || columnType.value == "TINYINT" || columnType.value == "YEAR"
	                			|| columnType.value == "SMALLINT" || columnType.value == "MEDIUMINT" || columnType.value == "FLOAT" || columnType.value == "NUMERIC" || 
	                				columnType.value == "BOOLEAN" ) {
	                		assignDefaultValue= 0;
	                	} else {
		                	assignDefaultValue="NULL";
	                	}	    
	                }else{
	                	assignDefaultValue=defaultValue.value;
	                }
	                if(checkTableIsAlter(columnName.getAttribute('id'),columnName.value,columnChineseName.value,columnType.value,columnSize.value,assignDefaultValue,autoGenerationId.value,isUniqueKey.checked,isCompositeKey.checked)){
	                	mapObj['id'] = columnName.getAttribute('id');
	                	mapObj['orderNum'] = orderNum.value;
						mapObj['columnName'] = columnName.value;
		                mapObj['columnType'] = columnType.value;
		                mapObj['columnSize'] = columnSize.value;
		                mapObj['columnChineseName'] = columnChineseName.value;
		                mapObj['defaultValue'] = assignDefaultValue;
		                mapObj['autoGenerationId'] = autoGenerationId.value;
		                mapObj['isUniqueKey'] = isUniqueKey.checked;
		                mapObj['isCompositeKey'] = isCompositeKey.checked;
		                jsonArray[jsonArray.length]=mapObj;
		                rowCount++;
	                }
 	            }
            	
            	selectedParentTableList=getSelectedTableList('parent_table_names','parentTable')
	        	selectedSubTableList=getSelectedTableList('sub_table_names','childTable');
            	
            	if(deleteRows!=""){
            		$("#deleteExistingTableColumns").val(JSON.stringify(deleteRows));
            	}
            	
            	
            	if(isRelationshipAltered(selectedParentTableList,parentTableList)){
            		$("#selectedParentTableList").val(JSON.stringify(selectedParentTableList));	
            	}else{
            		$("#selectedParentTableList").val('[]');
            	}
            	
            	if(isRelationshipAltered(selectedSubTableList,subTableList)){
            		$("#selectedSubTableList").val(JSON.stringify(selectedSubTableList));	
            	}else{
            		$("#selectedSubTableList").val('[]');
            	}
            	
 	         
            	return jsonArray;
    	 	}
        	 
        }
        
	function isRelationshipAltered(selectedTableList,currentTableList){
        	
        	var status=false;
        	if(currentTableList.length==selectedTableList.length){
        		for(var index=0;index<currentTableList.length;index++){
        			if(selectedTableList[index]["tableId"]!=[currentTableList[index]]){
        				status=true;
        				break;
        			}
        		}
        	}else{
        		return true;
        	}
        	return status;
        	
        }
        
        
        function checkTableIsAlter(id,fieldName,ChineseName,type,size,defaultValue,autoGenerationId,isUnique,isComposite){
        	
        	<c:forEach items="${metaTable.metaTableColumns}" var="columnsField" varStatus="status">
        		if(id=="${columnsField.id}"){
        			if(dbSource == "mssql" && (type=="INT" || type=="TINYINT" || type=="SMALLINT" )){
        				if("${columnsField.name}"!=fieldName || "${columnsField.chineseName}"!=ChineseName || "${columnsField.type}"!=type || "${columnsField.autoGenerationId}"!=autoGenerationId || ${columnsField.isUniquekey}!=isUnique || ${columnsField.isCompositeKey}!=isComposite){
    	    				return true;
    	    			}else{
    	        			return false;
    	        		}
        			}
        			
	        		if("${columnsField.name}"!=fieldName || "${columnsField.chineseName}"!=ChineseName || "${columnsField.type}"!=type || "${columnsField.length}"!=size || "${columnsField.defaultValue}"!=defaultValue || "${columnsField.autoGenerationId}"!=autoGenerationId || ${columnsField.isUniquekey}!=isUnique || ${columnsField.isCompositeKey}!=isComposite){
	    				return true;
	    			}else{
	        			return false;
	        		}	
	    		}
	    	</c:forEach>; 
        }
        
        function checkAllFieldsSaved(){
        	 var tempTable = document.getElementById('dataTable');
    		 var tempRowCount = tempTable.rows.length;
    		 var columnNames = [];
    		 for(var index=0; index < tempRowCount; index++) {
                 var row = tempTable.rows[index];
                 var columnName = row.cells[0].childNodes[0];
                 if(columnName.value != ""){
                	 columnNames.push(columnName.value);
                 }
    		 }
    		 
    		 if(columnNames.length !=0){
    			 validateMessageBox(form.title.message,form.error.relationPropertiesValidation,"info",false);
    			setTimeout(function() {
    				var tabContent = document.getElementById('wizardTab-content-1');
     				$(tabContent).addClass('displayBlock');
     				$(tabContent).removeClass('displayNone');
     				$(tabContent).siblings().removeClass('displayBlock');
     				$(tabContent).siblings().addClass('displayNone');
     				$("#wizardTab-1").parent().addClass('active-step');
     			    $("#wizardTab-1").parent().siblings().removeClass('active-step');
     			    $("#wizardTab-1").parent().siblings().addClass('completed-step');
	    		}, 800);
             }
        }
        
        function checkFieldValue(tableID){
        	var checkRowIndex=1;
        	var alterTableCount=0;
        	if(tableID=='alterTableValues'){
        		 var tempTable = document.getElementById('dataTable');
        		 var tempRowCount = tempTable.rows.length;
        		 checkRowIndex=checkRowIndex+tempRowCount;
        	}
        	 
        	var status=true;
        	 var table = document.getElementById(tableID);
             var rowCount = table.rows.length;
            
             if(rowCount==0 && tableID!='alterTableValues'){
            	 $.msgBox({
           		    title:form.title.check,
           		    content:form.error.fieldPropertiesValidation
           		});
            	 status=false;
             }
             
             if(tableID!='alterTableValues'){
        		var checkTable = document.getElementById('alterTableValues');
        		var getRowCount = checkTable.rows.length;
        		alterTableCount=getRowCount;
        	}
           	 
             for(var index=0; index < rowCount; index++) {
      			   
                 var row = table.rows[index];
                 var orderNum = row.cells[0].childNodes[0];
                 var columnName = row.cells[1].childNodes[0];
                 var columnChineseName = row.cells[2].childNodes[0];
               	 var columnType = row.cells[3].childNodes[0];
                 var columnSize = row.cells[4].childNodes[0];
                 var defaultValue = row.cells[5].childNodes[0];
                 if(!isNewTable && tableID=='dataTable' && index==0 && rowCount==1 && columnName.value=="" && columnChineseName.value=="" && columnSize.value=="" && defaultValue.value==""){
                	 status=true;
                 	 break;
            	 }
              
                 if(columnName.value=="" || columnChineseName.value==""){
               		 if(alterTableCount!=0){
               			if(columnName.value=="" && columnChineseName.value==""){
               				status = true;
               				break;
               			}
               		 }
                	 var contentText="Chinese Name ";
                	 if(columnName.value==""){
                		 contentText="Field Name ";
                	 }
                	 $.msgBox({
              		    title:form.title.check,
              		    content:contentText +""+form.msg.atRowNo+""+(checkRowIndex+index)+" "+form.error.notNull,
	              		  afterClose:function (){
	              			  if(contentText=="Chinese Name "){
	              				resetWizardDiv();
	              				columnChineseName.focus();
	              			  }else{
	              				resetWizardDiv();
	              				columnName.focus();
	              			  }
	              		}
              		});
                 	 status=false;
                 	 break;
                 }else{
                	 var columnNameValue = columnName.value;
					 if (!columnNameValue.replace(/\s/g, '').length) {
						$.msgBox({
		    				title:form.title.check,
		    				content:""+form.msg.fieldNo+""+(checkRowIndex+index)+" "+form.error.notBeEmpty,
		   					afterClose:function (){
			    				resetWizardDiv();
			    				row.cells[1].childNodes[0].focus();
	    						row.cells[1].childNodes[0].value="";
			    			}
						});
						status=false;
						break;
					 }
					 
					var columnChineseNameValue = columnChineseName.value;
					if (!columnChineseNameValue.replace(/\s/g, '').length) {
						$.msgBox({
						    title:form.title.check,
						    content:""+form.msg.chineseNameAtRowNo+""+(checkRowIndex+index)+" "+form.error.notBeEmpty,
				 		    afterClose:function (){
				 		    	resetWizardDiv();
				 		    	row.cells[2].childNodes[0].focus();
				        		row.cells[2].childNodes[0].value="";
				 		    }
						});
						status=false;
				 		break;
					}
					
                	if(!isNaN(columnName.value)){
                		 $.msgBox({
                  		    title:form.title.check,
                  		    content:""+form.msg.fieldNo+""+(checkRowIndex+index)+" "+form.error.notANumber,
                		    afterClose:function (){
                		    	resetWizardDiv();
                		    	row.cells[1].childNodes[0].focus();
                       			row.cells[1].childNodes[0].value="";
                		    }
                  		});
                    	status=false;
                    	break;
                	 }
                	 
                	 if(!isNaN(columnChineseName.value)){
                		 $.msgBox({
                  		    title:form.title.check,
                  		    content:""+form.msg.chineseNameAtRowNo+""+(checkRowIndex+index)+" "+form.error.notANumber,
	                		    afterClose:function (){
	                		    	resetWizardDiv();
	                		    	row.cells[2].childNodes[0].focus();
	                       			row.cells[2].childNodes[0].value="";
	                		    }
                  			});
                    		status=false;
                    		break;
                	 }
                	 
                	 if(/^[a-zA-Z][a-zA-Z0-9_]*$/.test(columnName.value) != false) {
                		  if(!isFieldNameDuplicate(columnName.value)){
                			 $.msgBox({
       	                 		    title:form.title.check,
       	                 		    content:form.msg.duplicateFieldName+" "+columnName.value+" "+form.msg.atRowNo+""+(checkRowIndex+index),
       	                		    afterClose:function (){
       	                		    	resetWizardDiv();
       	                		    	columnName.focus();
       	                       		}
       	                 		});
       	               		 	status=false;
       	                  		break;
       	                   }
                      }else{
                		 $.msgBox({
                  		    title:form.title.check,
                  		    content:form.msg.fieldNo+""+(checkRowIndex+index)+" "+form.error.illegalChar,
                  		  	afterClose:function (){
                  		  		resetWizardDiv();
                  		  		columnName.focus();
                     		}
                  		});
                     	 status=false;
                     	 break;
                	 }
                 }
                 
                 var columnTypeValue=columnType.value;
            	 var columnSizeValue=columnSize.value;
                  
                 if(!isNaN(columnSizeValue) && columnTypeValue!="DECIMAL" && columnTypeValue!="NUMERIC" && columnTypeValue!="DOUBLE"){
                	
                	 if(columnSizeValue==""){
                		 columnSizeValue=0;
                	 }
                	 
                	 if(defaultValue.value!="" && defaultValue.value!="NULL" && (columnTypeValue=="YEAR" || columnTypeValue=="BOOLEAN" || columnTypeValue=="VARCHAR" || columnTypeValue=="INT" || columnTypeValue=="CHAR" || columnTypeValue=="TINYINT" || columnTypeValue=="SMALLINT" || columnTypeValue=="MEDIUMINT" || columnTypeValue=="TIME" ||  columnTypeValue=="DATE" || columnTypeValue=="DATETIME")){
                		 if(columnTypeValue=="INT"){
                			 if(isNaN(defaultValue.value)){
                				 $.msgBox({
                            		    title:form.title.check,
                            		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.shouldNumber+" "+(checkRowIndex+index),
                            		    afterClose:function (){
                            		    	resetWizardDiv();
                            		    	row.cells[5].childNodes[0].focus();
                                   			row.cells[5].childNodes[0].value="";
                            		    } 
                            		});
                				status=false;
                          	 	break;
                			 }
                		 }
                		 
                		 if(columnTypeValue=="YEAR"){
                			 if(isNaN(defaultValue.value)){
                				 $.msgBox({
                            		    title:form.title.check,
                            		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.shouldNumber+" "+(checkRowIndex+index),
                            		    afterClose:function (){
                            		    	resetWizardDiv();
                            		    	row.cells[5].childNodes[0].focus();
                                   			row.cells[5].childNodes[0].value="";
                            		    } 
                            		});
                				status=false;
                          	 	break;
                			 }
                		 }
                		 
                		 if(columnTypeValue=='TIME'){
                			 var tempDefaultValue=defaultValue.value;
                			 var tempDefaultValueArray=tempDefaultValue.split(":");
                			 if(tempDefaultValueArray.length==3){
                				 var validTime=true;
                				 for(var timeIndex=0;timeIndex<3;timeIndex++){
                					 if(isNaN(tempDefaultValueArray[timeIndex])){
	                						 $.msgBox({
	                                 		    title:form.title.check,
	                                 		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.shouldNumber+" "+(checkRowIndex+index),
	                                 		    afterClose:function (){
	                                 		    	resetWizardDiv();
	                                 		    	row.cells[5].childNodes[0].focus();
	                                        		row.cells[5].childNodes[0].value="";
	                                 		    } 
	                                 		});
	                						 validTime=false;
	                     			 }else if(tempDefaultValueArray[timeIndex]>59 && timeIndex!=0){
                						 $.msgBox({
	                                 		    title:form.title.check,
	                                 		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validTime+" "+(checkRowIndex+index),
	                                 		    afterClose:function (){
	                                 		    	resetWizardDiv();
	                                 		    	row.cells[5].childNodes[0].focus();
	                                        		row.cells[5].childNodes[0].value="";
	                                 		    } 
	                                 	 });
                						 validTime=false;
	                     		 	}else if(tempDefaultValueArray[timeIndex]>23 && timeIndex==0){	
	               						 $.msgBox({
	                              		    title:form.title.check,
	                              		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validTime+" "+(checkRowIndex+index),
	                              		    afterClose:function (){
	                              		    	resetWizardDiv();
	                              		    	row.cells[5].childNodes[0].focus();
	                                     		row.cells[5].childNodes[0].value="";
	                              		    } 
	                              		 });
	               						 validTime=false;
	                  				 }
                				 }
                				
                				 if(!validTime){
                					 status=false;
                                	 break;	 
                				 }
                			 }else{
	                				 $.msgBox({
	                         		    title:form.title.check,
	                         		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validTimeFormat+" "+(checkRowIndex+index),
	                         		    afterClose:function (){
	                         		    	resetWizardDiv();
	                         		    	row.cells[5].childNodes[0].focus();
	                                		row.cells[5].childNodes[0].value="";
	                         		    } 
	                         		});
	             				status=false;
	                       	 	break;
                			 }
                			 
                		 }
                		 if(columnTypeValue=='DATE'){
                			 var tempDefaultValue=defaultValue.value;
                			 var tempDefaultValueArray=tempDefaultValue.split("-");
                			 if(tempDefaultValueArray.length==3){
                				 var validTime=true;
                				 for(var timeIndex=0;timeIndex<3;timeIndex++){
                				if(isNaN(tempDefaultValueArray[timeIndex])){
	                						 $.msgBox({
	                                 		    title:form.title.check,
	                                 		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.shouldNumber+" "+(checkRowIndex+index),
	                                 		    afterClose:function (){
	                                 		    	resetWizardDiv();
	                                 		    	row.cells[5].childNodes[0].focus();
	                                        		row.cells[5].childNodes[0].value="";
	                                 		    } 
	                                 		});
	                						 validTime=false;
	                     			 }/*else if(tempDefaultValueArray[timeIndex]>0 && timeIndex!=0){
                						 $.msgBox({
	                                 		    title:form.title.check,
	                                 		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validTime+" "+(checkRowIndex+index),
	                                 		    afterClose:function (){
	                                 		    	resetWizardDiv();
	                                 		    	row.cells[4].childNodes[0].focus();
	                                        			row.cells[4].childNodes[0].value="";
	                                 		    } 
	                                 		});
                						 validTime=false;
	                     		 	}else if(tempDefaultValueArray[timeIndex]>0 && timeIndex==0){	
	               						 $.msgBox({
	                              		    title:form.title.check,
	                              		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validTime+" "+(checkRowIndex+index),
	                              		    afterClose:function (){
	                              		    	resetWizardDiv();
	                              		    	row.cells[4].childNodes[0].focus();
	                                     			row.cells[4].childNodes[0].value="";
	                              		    } 
	                              		});
	               						 validTime=false;
	                  				 }*/
                				 }
                				
                				 if(!validTime){
                					 status=false;
                                	 break;	 
                				 }
                			 }else{
	                				 $.msgBox({
	                         		    title:form.title.check,
	                         		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validateDateFormat +" "+(checkRowIndex+index),
	                         		    afterClose:function (){
	                         		    	resetWizardDiv();
	                         		    	row.cells[5].childNodes[0].focus();
	                                		row.cells[5].childNodes[0].value="";
	                         		    } 
	                         		});
	             				status=false;
	                       	 	break;
                			 }
                			 
                		 }
                		 if(columnTypeValue=='DATETIME'){
                			 var tempDefaultValue=defaultValue.value;
                			 var tempDefaultValueArray=tempDefaultValue.split(" ");
                			                			 
                			 if(tempDefaultValueArray.length==2 && tempDefaultValueArray[0].split("-").length==3 && tempDefaultValueArray[1].split(":").length==3){
                				 var validTime=true;
                				/* for(var timeIndex=0;timeIndex<3;timeIndex++){
                				if(isNaN(tempDefaultValueArray[timeIndex])){
	                						 $.msgBox({
	                                 		    title:form.title.check,
	                                 		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.shouldNumber+" "+(checkRowIndex+index),
	                                 		    afterClose:function (){
	                                 		    	resetWizardDiv();
	                                 		    	row.cells[4].childNodes[0].focus();
	                                        		row.cells[4].childNodes[0].value="";
	                                 		    } 
	                                 		});
	                						 validTime=false;
	                     			 }else if(tempDefaultValueArray[timeIndex]>0 && timeIndex!=0){
                						 $.msgBox({
	                                 		    title:form.title.check,
	                                 		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validTime+" "+(checkRowIndex+index),
	                                 		    afterClose:function (){
	                                 		    	resetWizardDiv();
	                                 		    	row.cells[4].childNodes[0].focus();
	                                        			row.cells[4].childNodes[0].value="";
	                                 		    } 
	                                 		});
                						 validTime=false;
	                     		 	}else if(tempDefaultValueArray[timeIndex]>0 && timeIndex==0){	
	               						 $.msgBox({
	                              		    title:form.title.check,
	                              		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validTime+" "+(checkRowIndex+index),
	                              		    afterClose:function (){
	                              		    	resetWizardDiv();
	                              		    	row.cells[4].childNodes[0].focus();
	                                     			row.cells[4].childNodes[0].value="";
	                              		    } 
	                              		});
	               						 validTime=false;
	                  				 }
                				 }*/
                				
                				 if(!validTime){
                					 status=false;
                                	 break;	 
                				 }
                			 }else{
	                				 $.msgBox({
	                         		    title:form.title.check,
	                         		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.validDateTimeFormat+" "+(checkRowIndex+index),
	                         		    afterClose:function (){
	                         		    	resetWizardDiv();
	                         		    	row.cells[5].childNodes[0].focus();
	                                		row.cells[5].childNodes[0].value="";
	                         		    } 
	                         		});
	             				status=false;
	                       	 	break;
                			 }
                			 
                		 }
                		 if(columnSizeValue<defaultValue.value.length && columnTypeValue!="BOOLEAN" && columnTypeValue!="TIME"){
                			 if(dbSource != "mssql" && (columnTypeValue!="INT" || columnTypeValue!="TINYINT" || columnTypeValue!="SMALLINT" )){
	                  		  $.msgBox({
	                       		    title:form.title.check,
	                       		    content:form.msg.defaultValue+" "+defaultValue.value +" "+form.msg.atRowNo+""+(checkRowIndex+index)+" "+form.error.greaterFieldLength+""+columnSizeValue,
	                    		    afterClose:function (){
	                    		    	resetWizardDiv();
	                    		    	row.cells[5].childNodes[0].focus();
	                           			row.cells[5].childNodes[0].value="";
	                    		    }
	                       		});
	                 			status=false;
	                     	 	break;
                			 }
                   	  }
                     }else if(defaultValue.value == "" && columnTypeValue=="TIME"){
                    	 row.cells[5].childNodes[0].value="00:00:00";
                     }
                	
             		 if(columnTypeValue=="VARCHAR" || columnTypeValue=="INT" || columnTypeValue=="CHAR" || columnTypeValue=="TINYINT" || columnTypeValue=="SMALLINT" || columnTypeValue=="MEDIUMINT"){
             			 var noCheck =true;
             			 if(dbSource == "mssql" && (columnTypeValue=="INT" || columnTypeValue=="TINYINT" || columnTypeValue=="SMALLINT" )){
             				noCheck =false ;
             			 }
             			 
                 		 if(noCheck && (columnSizeValue<=0 || columnSizeValue>255)){
                    			 $.msgBox({
                          		    title:form.title.check,
                          		    content:columnTypeValue +" "+form.msg.atRowNo+""+(checkRowIndex+index)+" "+form.error.characterRange,
                        		    afterClose:function (){
                        		    	resetWizardDiv();
                        		    	row.cells[4].childNodes[0].focus();
                               			row.cells[4].childNodes[0].value="";
                        		    }
                          		});
                    			status=false;
                        	 	break;
                    		 }
                      }else if(noCheck && columnTypeValue=="YEAR"){
                     	 if(columnSizeValue!=4){
                    			 $.msgBox({
                          		    title:form.title.check,
                          		    content:columnTypeValue +" "+form.msg.atRowNo+""+(checkRowIndex+index)+" "+form.error.yearRange,
                        		    afterClose:function (){
                        		    	resetWizardDiv();
                        		    	row.cells[4].childNodes[0].focus();
                               			row.cells[4].childNodes[0].value="";
                        		    }
                          		});
                    			status=false;
                        	 	break;
                    		 }
                      }else{
                      	 if(row.cells[4].childNodes[0].value == ""){
                      		row.cells[4].childNodes[0].value=0;
                    	 }
                      }
                     
                 }else if(columnTypeValue=="DECIMAL" || columnTypeValue=="NUMERIC" || columnTypeValue=="DOUBLE"){
                	
                	 if(/^[0-9,]*$/.test(columnSizeValue) == false){
                		 $.msgBox({
                   		    title:form.title.check,
                   		    content:form.msg.lengthAtRowNo+""+(checkRowIndex+index)+" "+form.error.illegalChar,
                   		  	afterClose:function (){
                   		  		resetWizardDiv();
	                   		  	row.cells[4].childNodes[0].focus();
	                   			row.cells[4].childNodes[0].value="";
                      		}
                   		});
                		 status=false;
                 	 	break;
                	 }
                	var tempColumnSizeValue=columnSizeValue.split(',');
                		 if((tempColumnSizeValue[0]<=0 || tempColumnSizeValue[0]>65)){
	                    	$.msgBox({
	                     		    title:form.title.check,
	                      		    content:columnTypeValue +" "+form.msg.atRowNo+(checkRowIndex+index)+" "+form.msg.tempColumnSizeRange,
	                    		    afterClose:function (){
	                    		    	resetWizardDiv();
	                    		    	row.cells[4].childNodes[0].focus();
	                           			row.cells[4].childNodes[0].value="";
	                    		    }
	                      		});
	                			status=false;
	                    	 	break;
                		 }
                   		 
                		 if(defaultValue.value!="" && defaultValue.value!="NULL"){
                			 	if(isNaN(defaultValue.value)){
                   				 $.msgBox({
                               		    title:form.title.check,
                               		    content:form.msg.defaultValueOf+" "+columnTypeValue +" "+form.error.shouldNumber+" "+(checkRowIndex+index),
                               		    afterClose:function (){
                               		    	resetWizardDiv();
                               		    	row.cells[5].childNodes[0].focus();
                                      		row.cells[5].childNodes[0].value="";
                               		    } 
                               		});
                   					status=false;
                             	 	break;
                   			 }
                           		var defaultValueArray=defaultValue.value.split('.');
                           	 if(tempColumnSizeValue[0]<defaultValueArray[0].length && columnTypeValue!="BOOLEAN"){
                           		 if(dbSource != "mysql" && (columnTypeValue!="INT" || columnTypeValue!="TINYINT" || columnTypeValue!="SMALLINT" )){
                           			$.msgBox({
                              		    title:form.title.check,
                              		    content:form.msg.defaultValue+" "+defaultValueArray[0] +" "+form.msg.atRowNo+""+(checkRowIndex+index)+" "+form.error.greaterFieldLength+" "+tempColumnSizeValue[0],
                           		    afterClose:function (){
                           		    	resetWizardDiv();
                           		    	row.cells[5].childNodes[0].focus();
                                  		row.cells[5].childNodes[0].value="";
                           		    }
                              		});
                        			status=false;
                            	 	break;
                           		} 
                          	  }	
                           	 
                           	 if(tempColumnSizeValue.length==2 && defaultValueArray.length==2 && tempColumnSizeValue[1]<defaultValueArray[1].length && columnTypeValue!="BOOLEAN" && columnTypeValue!="TIME"){
                           		if(dbSource != "mysql" && (columnTypeValue!="INT" || columnTypeValue!="TINYINT" || columnTypeValue!="SMALLINT" )){
                           			$.msgBox({
                               		    title:form.title.check,
                               		    content:form.msg.defaultValue+" "+defaultValueArray[1] +" "+form.msg.atRowNo+""+(checkRowIndex+index)+" "+form.error.greaterFieldLength+""+tempColumnSizeValue[1],
                            		    afterClose:function (){
                            		    	resetWizardDiv();
                            		    	row.cells[5].childNodes[0].focus();
                                   			row.cells[5].childNodes[0].value="";
                            		    }
                               		});
                         			status=false;
                             	 	break;	
                           		}                           		
                           	 }
                    	 }
                   		 
                  }else{
               		$.msgBox({
             		    title:form.title.check,
             		    content:form.msg.lengthAtRowNo+""+(checkRowIndex+index)+" "+form.error.numberValidation,
            		    afterClose:function (){
            		    	resetWizardDiv();
            		    	row.cells[4].childNodes[0].focus();
                   			row.cells[4].childNodes[0].value="";
            		    }
             		});
               		status=false;
               		break;
               	 }
             }
             return status;
        }
        
        function qryPreview(tableID){

        	if(isNewTable){
        		if(checkTableName('dataTable')){
        			previewQuery();
        		}
        	}else if(checkTableName('dataTable') && checkTableName('alterTableValues')){
        		previewQuery();
        	}
        }
        
        function getSelectedTableList(treeId,treeRootId){
        	var selectedTableList=[];
        	 var tree = $.jstree._reference(treeId).get_container();  
  	    	var children = tree.find("li");
  	    	for(var treeIndex=0;treeIndex<children.length;treeIndex++){
  	    		if(children[treeIndex].getAttribute("id")!=treeRootId){
  	    			var selectedTableMap={};
         			var tableId=children[treeIndex].getAttribute("id");
         			var tableName=children[treeIndex].getAttribute("name");
         			var foreignKeyColumnId = children[treeIndex].getAttribute("foreignKeyColumnId");
         			var foreignKeyColumnName = children[treeIndex].getAttribute("foreignKeyColumnName"); 
         			var childKeyColumnid = children[treeIndex].getAttribute("childKeyColumnid");
         			var childKeyColumnName = children[treeIndex].getAttribute("childKeyColumnName"); 
         			selectedTableMap["tableId"] = tableId;
         			selectedTableMap["tableName"] = tableName;
         			selectedTableMap["foreignKeyColumnId"] = foreignKeyColumnId;
         			selectedTableMap["foreignKeyColumnName"] = foreignKeyColumnName; 
         			selectedTableMap["childKeyColumnid"] = childKeyColumnid;
         			selectedTableMap["childKeyColumnName"] = childKeyColumnName; 
         			selectedTableList[selectedTableList.length]=selectedTableMap;
  	    		}
  	    	}
  	    	
  	    	if(selectedTableList=="" || selectedTableList==null ){
  	    		if(treeId=="parent_table_names" && isParentTableList){
  		    		$("#deleteParentTableList").val(JSON.stringify(parentTableList));	
  	 	        }
  	    		
  	    		if(treeId=="sub_table_names" && isSubTableList){
  	    			$("#deleteSubTableList").val(JSON.stringify(subTableList));
  	    		}
  	    	}	
  	    	
  	    	return selectedTableList;
        }
        
        function previewQuery(){
        	getData('dataTable');
        	var isUpdate=false;
        	if(!isNewTable){
        		isUpdate=true;
        	}
        
        	var param={ fieldPropertiesList: $("#fieldPropertiesList").val(), tableName: $("#tableName").val(), parentTableList: $("#selectedParentTableList").val(), subTableList: $("#selectedSubTableList").val(),deleteExistingTableColumns:$("#deleteExistingTableColumns").val(),isUpdate: isUpdate }
	    		 $.ajax({	
					type: 'POST',
					async: false,
					url : '/table/previewQuery',
					data: param,
					success : function(response) {
						var previewQuery=response.previewQuery;
						if(previewQuery!="" && previewQuery!="No Changes has been done for Query Preview"){
							clearPreviousPopup();
							$("#popupDialog").html(previewQuery);
							$myDialog = $("#popupDialog");
							$myDialog.dialog({
						                       width: '50%',
						                       height: 'auto',
						                       modal: true,
						                       title: form.title.msgBoxTitleQueryPreview
							             });
							 $myDialog.dialog("open");
						}else{
							$.msgBox({
		             		    title:form.title.check,
		             		    content:"No Changes has been done for Query Preview",
		            		});
						}
					}
				});
        }
        
        function isFieldNameDuplicate(checkColumnName){
        	var fieldNamesArray= new Array();
        	checkColumnName=checkColumnName.toUpperCase();
            //date table
        	var tempDataTable = document.getElementById('dataTable');
        	var tempDataTableRowCount = tempDataTable.rows.length;
        	//altered tables
        	var tempAlterTableValues = document.getElementById('alterTableValues');
        	var tempAlterTableValuesCount = tempAlterTableValues.rows.length;
        	
        	for(var index=0; index < tempDataTableRowCount; index++) {
        	    var row = tempDataTable.rows[index];
                var columnName = row.cells[0].childNodes[0];
                fieldNamesArray[fieldNamesArray.length]=columnName.value.toUpperCase();
        	 }
        	 
        	 for(var index=0; index < tempAlterTableValuesCount; index++) {
        		 var row = tempAlterTableValues.rows[index];
                var columnName = row.cells[0].childNodes[0];
                fieldNamesArray[fieldNamesArray.length]=columnName.value.toUpperCase();
         	  }
        	 
        	 if(isNewTable){
        		 for(var index=0; index < defaultFieldNamesArray.length; index++){
        			 fieldNamesArray[fieldNamesArray.length]=defaultFieldNamesArray[index];
        		 }
        	 }

        	var count=0;
        	for(var temp=0; temp<fieldNamesArray.length; temp++){
        		if(fieldNamesArray[temp]==checkColumnName){
        			count++;
        		}
        	}
        	
        	if(count>=2){
        		return false;	
        	}else{
        		return true;
        	}
        	 
        }
        
        function resetWizardDiv(){
        	 var tableName = $("#tableName").val() ;
        	 var moduleName = $("#module").val() ;
        
       		if(tableName==""){
       			$("#tableNameLable").addClass("error");
       		}else{
       			$("#tableNameLable").removeClass("error");
       		}
       		
       		if(moduleName==""){
       			$("#moduleNameLable").addClass("error");
       		}else{
       			$("#moduleNameLable").removeClass("error");
       		}
        	
        	$("#wizardTab_div-1").addClass("active-step");
  	    	$("#wizardTab_div-2").removeClass("active-step");
			$("#wizardTab-content-1").addClass("displayBlock");
			$("#wizardTab-content-2").removeClass("displayBlock");
			$("#wizardTab-content-2").addClass("displayNone");
        	
        }
        
        /* function selectForeignKey(tableId, parentId, subTableId){
        	console.log(" tableId , parentId, subTableId =============== "+tableId+" parentId "+parentId+" subTableId "+subTableId);
        	var params = "";
            params = 'tableId='+tableId+'&parentDivId='+parentId+'&subTableId='+subTableId;
        	openSelectDialogForFixedPosition("tableRelationForeignKey",params,"450","170","Select Foreign Key");
        	document.location.href = "#bpm/table/getAllColumnIncludeDefaultByTableId";
        } */
        
       
        
        $(document).ready(function(){
        	setModuleNamesList('${moduleId}');
        });
        
        function redirectDiv(){
        	var isModuleChanged = true ;
        	var isAutoFormCreate = false;
        	 if(document.getElementById("isCreateAutomaticForm").checked == true){
        		isAutoFormCreate = true;
        	}else {
        		isAutoFormCreate = false;
        	} 
        	
        	var params = "isModuleChanged="+isModuleChanged+"&isAutoFormCreate="+isAutoFormCreate;
        	if('${metaTable.id}'){
        		_execute('rightPanel', params);
        		}else{
        		_execute('rightPanel', params);
        	}
        	sourceTableColumns={};
        }
        
        function setDefaultValueInTable(typeCombo,rowCount,tableID){
        	var defaultValueMap={"VARCHAR":"100","NVARCHAR":"100","NVARCHAR2":"100","NCHAR":"100","DECIMAL":"19,2","CHAR":"10","INT":"10","YEAR":"4","VARCHAR2":"100","NUMERIC":"10"};
        	var table = document.getElementById(tableID);
        	var row = table.rows[rowCount];
        	
        	for(key in defaultValueMap ){
        		if(typeCombo.value==key){
        			row.cells[4].childNodes[0].value=defaultValueMap[key];
        			break;
        		}else{
        			if(typeCombo.value == "LONGTEXT" || typeCombo.value == "DATE" || typeCombo.value == "DATETIME" ) {
               		 	row.cells[4].childNodes[0].setAttribute("readonly", true);
        			}else{
        				row.cells[4].childNodes[0].removeAttribute("readonly");
        			}
        			row.cells[4].childNodes[0].value="";
        		}
        	}
        }
        
        $(document).ready(function(){
        	//$('#modules').val('${moduleName}');
        	 if('${isTableRelation}' == 'true'){
        		 var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
        		 $("#module_relation_tree").jstree("deselect_node", selected_nodes);

        		$("#module_relation_tree").jstree("create", $("#"+'${moduleId}'+"Tables"), false, {attr : {id: '${metaTable.id}_${moduleId}', name: "${metaTable.tableName}",nodeid:'${metaTable.id}',moduleid:'${moduleId}',isedit:'true',isSystemModule:"${isSystemModule}",nodetype:'table'}, data: "${metaTable.tableName}",metadata: {id: '${metaTable.id}_${moduleId}',name:"${metaTable.tableName}",moduleid:"${moduleId}",nodeid:'${metaTable.id}'}}, false, true);
    			$("#module_relation_tree").jstree("select_node", '#'+'${metaTable.id}_${moduleId}');
        	} 
        	 changeWizardByTab();
        });
 		</script>
		 <%@ include file="/common/messages.jsp" %>
<div class="widget-body"> 
<h2 id="createTableHeader" style="display: none;"><fmt:message key="createTable.title"/></h2>
<h2 id="alterTableHeader" style="display: none;"><fmt:message key="alterTable.title"/></h2>
<h2 id="alterTableColumns" style="display: none;"><fmt:message key="alterTableFields.title"/></h2>
	   <form:form id="metaTable" commandName="metaTable" method="post" action="bpm/table/saveTable" autocomplete="off" modelAttribute="metaTable" onSubmit="redirectDiv()">
	  <form:hidden path="id"/>
	   <input type="hidden" name="fieldPropertiesList" id="fieldPropertiesList"/>
	   <input type="hidden" name="selectedParentTableList" id="selectedParentTableList"/>
	   <input type="hidden" name="selectedSubTableList" id="selectedSubTableList"/>
	   
	   <input type="hidden" name="deleteParentTableList" id="deleteParentTableList"/>
	   <input type="hidden" name="deleteSubTableList" id="deleteSubTableList"/>
	   
	   <input type="hidden" name="deleteExistingTableColumns" id="deleteExistingTableColumns"/>
       <input type="hidden" name="tableDefaultValues_oracle" id="tableDefaultValues_oracle" value="<fmt:message key='oracle.table.default.values'/>"/>
       
       <input type="hidden" name="tableDefaultValues_mysql" id="tableDefaultValues_mysql" value="<fmt:message key='mysql.table.default.values'/>"/>
       <input type="hidden" name="tableDefaultValues_mssql" id="tableDefaultValues_mssql" value="<fmt:message key='mssql.table.default.values'/>"/>
       
	  
	  <div id="wizard">
	  	<c:if test="${not empty metaTable.id}" >
	  	<ol>
			<li><fmt:message key="table.properties"/></li>
			<li><fmt:message key="table.relation.properties"/></li>
		</ol>
		</c:if>
		<div class="well"> 
			<div id="tableProperty">
		<div id="wizardTab-content-1">
			<div class="form-horizontal no-margin">
		 	<div class="control-group" id="tableNameRow">
		      	<%--   <td width="271" height="40"><eazytec:label styleClass="control-label style3 style18 " key="metaTable.tableName"/></td> --%>
		     	<span id="tableNameLable"><label class="control-label"><fmt:message key="table.field.name"/><span class="required">*</span></label></span>
		        
		        <c:if test="${empty metaTable.id}" >
	        	<div class="controls controls-row"><form:input path="tableName" id="tableName" class="span8"/></div>
				</c:if>
				
				<c:if test="${not empty metaTable.id}" >
				
				<div class="controls controls-row"><form:input path="tableName" readonly="true" id="tableName" class="span8"/></div>
				</c:if>
	    	</div>
	    	<div class="control-group" id="chineseNameRow">
		      	<eazytec:label styleClass="control-label" key="metaTable.chineseName"/>
		        <div class="controls controls-row"><form:input path="chineseName" id="chineseName" class="span8"/></div>
        	</div>
       
			<div class="control-group" id="descriptionRow">
     			<eazytec:label styleClass="control-label" key="metaTable.description"/>
	        	<div class="controls controls-row"><form:textarea path="description" id="description" cols="18" class="span8" rows="3"/></div>
	   
		</div>
		 <div class="control-group">
			<label class="control-label" > <fmt:message key="metaTable.isCreateAutomaticForm"/> </label>
			<div class="controls checkbox-label">
				<input type="checkbox" id="isCreateAutomaticForm" name="isCreateAutomaticForm"/>
			</div>
		
	    	</div>
	    	</div>
	    	
	    	<div class="title">
				<span class="fs1" aria-hidden="true"></span>
				<h5><eazytec:label styleClass="control-label" key="table.field.properties"/></h5>
			</div>
			<!-- <div class="well"> 
			<div id="tableProperty"> -->
			<table>
			    <tr>
			        <td colspan="2">
			        		<TABLE cellpadding="1" cellspacing="1"  width="100%">
				        		 <TR>
				        		 	 <th style="padding: 3px; width: 55px;"><label class="table-header-label">排序<span class="required">*</span></label></th>
				        			 <th style="padding: 3px; width: 140px;"><label class="table-header-label"><fmt:message key="table.field.name"/><span class="required">*</span></label></th>
				        			 <th style="padding: 3px; width: 140px;"><label class="table-header-label"><fmt:message key="metaTable.chineseName"/><span class="required">*</span></label></th>
						             <th style="padding: 3px; width: 127px;"><label class="table-header-label"><fmt:message key="table.field.type"/></label></th>
						             <th style="padding: 3px; width: 55px;"><label class="table-header-label"><fmt:message key="table.field.length"/></label></th>
						             <th style="padding: 3px; width: 85px;"><label class="table-header-label"><fmt:message key="table.field.default.value"/></label></th>
							     	 <th style="padding: 3px; width: 125px;"><label class="table-header-label"><fmt:message key="table.field.auto.id"/></label></th>
							     	 <th style="padding: 3px; width: 45px;"><label class="table-header-label"><fmt:message key="table.field.is.unique.key"/></label></th>
							     	 <th style="padding: 3px;"></th>
							     	 <th style="padding: 3px;"></th>
							     </TR>
			        		</Table>
			        		<TABLE cellpadding="1" cellspacing="1"  width="100%">
			        		<TABLE id="dataTable">
			        	   
					   		 </TABLE>
					   		 <TABLE id="alterTableValues">
			        	   
					   		 </TABLE>
					   		 
					   		 <div id="defaultValueTableDiv"></div>
					   		 </TABLE>
				</td>
				</tr>
			</table>
					<c:if test="${isEdit=='true'}" >
		<div class="form-horizontal no-margin">
			<div class="control-group">
				<div class="form-actions no-margin" align="center" style="padding-left:0;">
					<c:choose>
				    	<c:when test="${not empty metaTable.id}">
				       		<input class="btn btn-primary pull-center" style="height:30px;" type="submit" value="<fmt:message key="button.update"/>" onclick="return checkTableName('dataTable');"/>
<!-- 				        	<input type="button" class="btn btn-primary pull-center" style="height:30px;" name="next" onclick="backToPreviousPage()" id="cancelButton" value="Cancel"/>
 -->				        </c:when>
				        <c:otherwise>
				        	<input class="btn btn-primary pull-center" style="height:30px;" type="submit" value="<fmt:message key="button.save"/>" onclick="return checkTableName('dataTable');"/>
						</c:otherwise>
					</c:choose>
	            	<input class="btn btn-primary pull-center" style="height:30px;" type="button" value="<fmt:message key="form.preview"/>" onclick="qryPreview('dataTable')"/>
	    		</div>
	    	</div>
	    </div>
	    </c:if>
	    </div>
	    </div>
	    </div>
		<c:if test="${not empty metaTable.id}" >
			<div id="wizardTab-content-2">
		</c:if>
		<c:if test="${empty metaTable.id}" >
	  		<div id="wizardTab-content-2" class="displayNone">
	  	</c:if>
		<fieldset>
		<div class="well" > 	
		<div id="relationProperty">
		<table width="100%">
		<!--  <tr height="30px"></tr>  -->
		
		<tr>
		
	    <td width="150" id="tableRelationId" height="35"><eazytec:label styleClass="control-label style3 style18 " key="table.relation.properties"/></td>
	        <td colspan="2">
	         
	        		<table cellpadding="1" cellspacing="1" style="border: 1px solid #808080;" align="center" width="100%">
					<tr>        		
					   <td style="border: 1px solid #808080;vertical-align:top;" width="1%" id="allTableListId">
					   		<!-- <div style="height:180px;overflow:auto;"> -->
					   		<div id="tableListHeight">
					   			<div border="1px solid #a1a1a1" id="all_table_names"></div>
					   		</div>
					   </td>
					   <td style="border: 1px solid #808080;vertical-align:top;" id="parentRelationShipListId">
					   		<!-- <div style="height:180px;overflow:auto;"> -->
					   		<div id="tableListHeight">
					   			<div id="parent_table_names"></div>
					   		</div>
					   		
					   	</td>
					   <td style="border: 1px solid #808080;vertical-align:top;" id="subRelationShipListId">
					   		<!-- <div style="height:180px;overflow:auto;"> -->
					   		 <div id="tableListHeight">
					   			<div id="sub_table_names"></div>
					   		</div>
					   	</td> 
					</tr>
					</table>
				
				</td>
				
			</tr>
			
		<tr height="10px"></tr>
		 <c:if test="${isEdit=='true'}" >
			<tr>
				<td colspan="2">

	        		<TABLE cellpadding="1" cellspacing="1" width="100%">
					<tr>
				        <td>
				       		<div class="form-horizontal no-margin">
							<div class="control-group">
								<div class="form-actions no-margin" align="center">	
									<span id="saveButtonDiv">
										
										<input class="btn btn-primary pull-center" style="height:30px;" type="submit" value="Save" onclick="return checkTableName('dataTable');"/>	
										
									</span>
									<span>
										<input class="btn btn-primary pull-center" style="height:30px;" type="button" value="Preview" onclick="qryPreview('dataTable')"/>
									</span>
									<div class="clearfix"></div>
								</div>
							</div>
						</div>						
				        </td>
				    </tr>
					</table>
				</td>
			</tr>
	        </c:if>
	    </table>
	    </div>
	    </div> 
	    </fieldset>
	    </div>
	    </div>
	    <input type="hidden" name="_moduleId" />
	    <input type="hidden" name="_moduleName" value="${moduleName}" />
	   <input type="hidden" id="moduleId" name="module.Id" value="${moduleId}"/>
	    <input type="hidden" id="isTableRelation" name="isTableRelation" value="${isTableRelation}"/>
	   </form:form> 
<!-- </div> -->

<c:if test="${not empty metaTable.id}" >
<script type="text/javascript">
	$("#wizard").bwizard();
	$(".bwizard-buttons").css({display:'none'});
	var newHeight = $("#target").height();
	var wizardHeight = parseInt(newHeight) - 280;
	var wizardPropertyHeight = parseInt(newHeight) - 255;
	
	//var newWidth = $("#target").width
	/* $('#wizard').css({height: wizardHeight}); */
	
	/* var tablePropertyHeight = parseInt(newHeight) - 280; */
	var tablePropertyHeight = parseInt(newHeight) - 215;
	$('#tableProperty').css({height: tablePropertyHeight,overflow:'auto'});
	
	/* var relationPropertyHeight = parseInt(newHeight) - 250; */
	$('#relationProperty').css({height: wizardPropertyHeight});	
	var newTableList = $(".widget-body").height();
	var newTableListWidth = $("#rightPanel").width();
	var ListParentChildHeight = parseInt(newTableList) - 262;
	var ListParentChildWidth = parseInt((parseInt(newTableListWidth) - 220)/3);
	//var ListParentChildWidth = parseInt(newTableList) - 600;
	$('#tableListHeight').css({height : ListParentChildHeight,width : ListParentChildWidth, overflow: 'auto'});
	
	<c:if test="${enableRelationTab}" >
		$("#wizard").bwizard("next");
	</c:if>;
</script>
</c:if>
