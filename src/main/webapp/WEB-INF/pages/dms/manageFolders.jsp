<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="folder.heading"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<script type="text/javascript">

function constructMenuTree(jsonData){
	var json_data = eval(jsonData);
	$("#folder_js_tree").jstree({
        "json_data" : {
        	"data" : json_data
        },
        "plugins" : [ "core", "themes", "json_data", "ui" , "crrm" ],
       
    }).bind("loaded.jstree", function(e, data) {
    	$("#folder_js_tree").jstree("open_all");
    }).bind("select_node.jstree", function (e, data) {
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
  		var params = 'id='+currentNode;
      	document.location.href = "#bpm/dms/showFolderPage";
       	_execute('folder_form',params);
    });

}

function defaultJsonFolderData(){
	
	var jsonData = '[';
	<c:forEach items="${allFolders}" var="folder" varStatus="status">
		if("${folder.id}" != ""){	
			jsonData +='{ "data" : "${folder.name}", "attr" : { "id" : "${folder.id}", "name" : "${folder.name}","parent" : "${folder.name}" },"metadata": {"id" : "folder", "name" : "${folder.name}"}},';
		}
	</c:forEach>;
	jsonData+=']';
	return jsonData;
}

$(document).ready(function(){
	var height = $("#target").height();
	$("#folder_form").css('height',parseInt(height)- 37);
	$("#folder_tree").css('height',parseInt(height)- 37);
	var jsonData = defaultJsonFolderData();
	constructMenuTree(jsonData);
}); 
</script>


<div class="span12">
	<div class="container-fluid" id="parent_container">
		<div class="row-fluid">
			<div class="span12">
				<div class="titleBG">
					<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="folder.heading"/></span>
					<span class="floatRight fontSize14 pad-R10 pad-T10"><a title="Create New" data-icon="" onclick="_execute('folder_form','');refreshJSTree('folder_js_tree');" data-theme="b" data-role="button" href="#bpm/dms/showFolderPage" id="createNew">Create New</a></span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
		    <div class="span12">
				<div id="folder_tree" class="span3 manage_folder_js_tree">
					<div id="folder_js_tree" style="background-image: url('/styles/easybpm/images/palette_line.jpg');"></div>
				</div>
				<div id="folder_form" class="span9 manage_folder_form">
					<%@ include file="/WEB-INF/pages/dms/folderForm.jsp" %>
				</div>
			</div>
		</div>
	</div>
</div>
