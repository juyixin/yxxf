<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="menuList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<script type="text/javascript">
var treeLevel=0;
function constructMenuTree(jsonData){
	var json_data = eval(jsonData);
	$("#table_tree").jstree({

		 "json_data" : {
        	"data" : json_data
        },

        "plugins" : [ "core", "themes", "json_data", "ui" , "crrm" ],
        
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
	    	treeLevel=level;
	    	/* if(currentNode == "tables"){
	    		getActiveChildData(currentNode, parentNode, level, rootNode);
	    	} */
	    	if(level == 2){
	    		var params = 'tableId='+currentNode+'&enableRelationTab=false';
	        	document.location.href = "#bpm/table/showFieldEdit";
	        	_execute('table_form',params); 
	    	}
			
		
});

}

function getActiveChildData(currentNode, parentNode, level, rootNode){
	 <c:forEach items="${tableList}" var="tables" varStatus="status">
		var tableType = rootNode.toLowerCase();
		if("${tables.id}" != ""){	
			var tableId =  "${tables.id}";
			var tableId2	= tableId.replace(/ /g,'');
			$("#table_tree").jstree("create", $("#"+currentNode), false, {attr : {id: tableId2, name: "${tables.tableName}"}, data: "${tables.tableName}"}, false, true);
		}
	</c:forEach>;

}

/* function defaultMasterTable(){
	var jsonData = '[';
	
	<c:forEach items="${tableList}" var="tables" varStatus="status">
		if("${tables.id}" != ""){	
			jsonData +='{ "data" : "${tables.tableName}", "attr" : { "id" : "${tables.id}", "name" : "${tables.tableName}","parent" : "${tables.tableName}" },"metadata": {"id" : "tables", "name" : "${tables.tableName}"}},';
		}
	</c:forEach>;
	jsonData+=']';
	return jsonData;
} */

$(function(){
	var height = $("#target").height();
	$("#table_form").css('height',parseInt(height)- 37);
	$("#table_js_tree").css('height',parseInt(height)- 37);
	var jsonData = '${tableTree}';
	constructMenuTree(jsonData);
}); 

function editTableDetails(){
	var selected_nodes = $("#table_tree").jstree("get_selected", null, true);
	if(treeLevel!=1){
		var tableId=selected_nodes.attr("id");
		if(tableId!="tables" && tableId!=undefined){
				var params = "tableId="+tableId+"&enableRelationTab=false";
				document.location.href = "#/bpm/table/showEditTable";
				_execute('table_form', params); 	
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToEdit,
			});	
		}	
	}else{
		$.msgBox({
		    title: form.title.message,
		    content: form.msg.moduleSelectTableToEdit,
		});	
	}
	
}

function exportTableDetails(){
	var selected_nodes = $("#table_tree").jstree("get_selected", null, true);
	if(treeLevel!=1){
		var tableId=selected_nodes.attr("id");
		if(tableId!="tables" && tableId!=undefined){
			  document.location.href = "bpm/table/exportTable?tableId="+tableId;
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToExport,
			});	
		}
	}else{
		$.msgBox({
		    title: form.title.message,
		    content: form.msg.moduleSelectTableToExport,
		});	
	}
}

function exportSQLTableDetails(){
	var selected_nodes = $("#table_tree").jstree("get_selected", null, true);
	if(treeLevel!=1){
		var tableId=selected_nodes.attr("id");
		if(tableId!="tables" && tableId!=undefined){
			  document.location.href = "bpm/table/exportTableSql?tableId="+tableId;
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToExport,
			});	
		}
	}else{
		$.msgBox({
		    title: form.title.message,
		    content: form.msg.moduleSelectTableToExport,
		});	
	}
	
}

function importSQLTableDetails(){
	  openSelectDialogForFixedPosition("importSQLPopupDialog","","450","170","Import SQL");
	  document.location.href = "#bpm/table/importSQLDump";
}

function importCSVTableDetails(){
	 openSelectDialogForFixedPosition("importCSVPopupDialog","","515","210","Import CSV");
	  document.location.href = "#bpm/table/importCSVDump";
}

function refreshTableJSTree(treeId){
	var json_data = eval('${tableTree}');
	constructMenuTree(json_data);
}

function deleteTableDetails(){
	var selected_nodes = $("#table_tree").jstree("get_selected", null, true);
	
	if(treeLevel!=1){
		var tableId=selected_nodes.attr("id");
		var tableName=selected_nodes.attr("name");
		
			if(tableId!="tables" && tableId!=undefined){
				var params="tableId="+tableId;
				var content="Are you sure you want to delete the Table: "+tableName+" ?";
				
				$.msgBox({
				    title: form.title.confirm,
				    content: content,
				    type: "confirm",
				    buttons: [{ value: "Yes" }, { value: "No" }],
				    success: function (result) {
				        if (result == "Yes") {
				        	document.location.href = "#bpm/table/deleteTableById";
				    		_execute('target',params);
				    		refreshTableJSTree('table_tree');
				        }else{
				        	return false;
				        }
				    }
				});	
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToDelete,
			});	
		}
	}else{
		$.msgBox({
		    title: form.title.message,
		    content: form.msg.moduleSelectTableToDelete,
		});	
	}
	
	
}

</script>

<div class="span12">
<div class="container-fluid" id="parent_container">
	<div class="row-fluid">
		<div class="span12">
			<div class="titleBG">
				<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="admin.module.list"/></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="deleteTable" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteTableDetails();"  data-icon=""><fmt:message key="table.button.delete"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="importSql" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="importSQLTableDetails();"  data-icon=""><fmt:message key="table.button.import.sql"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="importCsv" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="importCSVTableDetails();"  data-icon=""><fmt:message key="table.button.import.csv"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="exportSql" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="exportSQLTableDetails();"  data-icon=""><fmt:message key="table.button.export.sql"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="export" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="exportTableDetails();"  data-icon=""><fmt:message key="table.button.export"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="editTable" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="editTableDetails();"  data-icon=""><fmt:message key="table.button.edit"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="createTable" href="#/bpm/table/createTable" data-role="button" data-theme="b"  onClick="_execute('table_form','enableRelationTab=false&isEdit=true');refreshTableJSTree('table_tree');"  data-icon=""><fmt:message key="table.button.new"/></a></span>
			</div>
		</div>
	</div>
	<div class="row-fluid">
	    <div class="span12">
			<div id="table_js_tree" class="span3 manage_table_js_tree">
				<div id="table_tree" style="background-image: url('/styles/easybpm/images/palette_line.jpg');"></div>
			</div>
			<div id="table_form" class="span9 manage_table_folder">
				<%@ include file="/WEB-INF/pages/table/tableEmpty.jsp" %>
			</div>
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
$('#grid_header_links').html($('#header_links').html());
$(function () {

    setTimeout(function() {
        $("#tables a").removeClass("").addClass('jstree-clicked');
    },300);

 });
</script>