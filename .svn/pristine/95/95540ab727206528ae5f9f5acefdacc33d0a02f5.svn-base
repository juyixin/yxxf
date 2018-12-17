<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="menuList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>
<script type="text/javascript">

function constructMenuTree(jsonData){
	var json_data = eval(jsonData);
	$("#dms_js_tree").jstree({
        "json_data" : {
        	"data" : json_data
        },
        "plugins" : [ "core", "themes", "json_data", "ui" , "crrm" ],
       
    }).bind("loaded.jstree", function(e, data) {
    	$("#dms_js_tree").jstree("open_all");
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
  		/*var params = 'folderId='+currentNode;
      	document.location.href = "#bpm/dms/showDMS";
       	_execute('dms_grid',params); */
  		$.ajax({
  			url: 'bpm/dms/getFolderDocumentGrid',
  	        type: 'GET',
  	        dataType: 'html',
  	        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
  			async: false,
  			data: 'folderId='+currentNode,
  			success : function(response) {
  				if(response.length > 0){
  					$("#dms_grid").html(response);
  				}
  			}
  		});
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
	$("#dms_grid").css('height',parseInt(height)- 37);
	$("#dms_tree").css('height',parseInt(height)- 37);
	var jsonData = defaultJsonFolderData();
	constructMenuTree(jsonData);
}); 
</script>


<div class="span12">
	<div class="container-fluid" id="parent_container">
		<div class="row-fluid">
			<div class="span12">
				<div class="titleBG">
					<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="documentForm.heading"/></span>
					<span class="floatRight fontSize14 pad-R10 pad-T10"><a title="Create New" data-icon="" onclick="return createFolderDocumentForm();" data-theme="b" data-role="button" href="#bpm/dms/documentForm" >Create New</a></span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
		    <div class="span12">
				<div id="dms_tree" class="span3 manage_folder_js_tree">
					<div id="dms_js_tree" style="background-image: url('/styles/easybpm/images/palette_line.jpg');"></div>
				</div>
				<div id="dms_grid" class="span9 manage_folder_form" >
					 <%= request.getAttribute("script") %>
				</div>
			</div>
		</div>
	</div>
</div>
