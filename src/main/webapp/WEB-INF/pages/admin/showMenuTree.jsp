<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="menuList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<script type="text/javascript">

//Form the oprion to add in select menu
var selectedVal="";

function constructMenuTreeForPopup(jsonData){
	var json_data = eval(defaultJsonMenuData());
	$("#menu_tree").jstree({

		"json_data": {
	        "data" : json_data, 
	    },
	    "checkbox": {
              real_checkboxes: false,
              two_state: true
           },
        "plugins" : [ "themes", "json_data", "ui", "checkbox", "crrm"]
    }).bind("loaded.jstree", function(e, data) {
    	removeRootNodeCheckBoxForMenu();
    	checkAlreadySelectedMenu('${requestorId}');
	}).bind("select_node.jstree", function (e, data) {
		removeRootNodeCheckBoxForMenu();
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
    	if(level > 1){
    		parentNode = data.rslt.obj.parents("li").attr("id");
    	}else{
    		parentNode = currentNode;
    	}
    	if(currentNode != "all"){ 
    		getActiveChildMenu(currentNode, parentNode, level, rootNode);
    	}
    }).bind('check_node.jstree', function(e, data) {
    	if(data.rslt.obj.attr("id") != 'top' && data.rslt.obj.attr("id") != 'header'){
    	//	checkChildOfParentNode(e,data);
    	}
	    addCheckedOptionForMenu(e,data); 
	}).bind('uncheck_node.jstree', function(e, data) {
		
		/* $("#menus option").each(function(){
			
			if($(this).val() == data.rslt.obj.attr("id")){
				
				$("#menus option[value="+$(this).val()+"]").remove();
			}
		}); */
		
		removeUnCheckedOptionForMenu(e,data);	
	});

}
function checkChildOfParentNode(e,data){
	var parentNodeId = data.inst._get_parent(data.rslt.obj).attr("id");
	if(parentNodeId != 'top' && parentNodeId != 'header'){
		
		$.jstree._reference($("#menu_tree")).check_node('#'+parentNodeId);
	}
	
}
function removeUnCheckedOptionForMenu(e,data){
	removeClosedOptionForMenu(data.rslt.obj.attr("id"));
}

//when click close icon remove that from right side
function removeClosedOptionForMenu(nodeId){
	$("span[id='"+nodeId+"']").remove();
	var status = $.jstree._reference($("#menu_tree")).is_checked('#'+nodeId);
 	if(status == true){
 		$.jstree._reference($("#menu_tree")).uncheck_node('#'+nodeId);
 	}
}
function getActiveChildMenu(currentNode, parentNode, level, rootNode){
	if(level == 1){
		<c:forEach items="${availableMenus}" var="menu">
			var menuType = rootNode.toLowerCase();
			if("${menu.isParent}" == "true" && "${menu.menuType}" == menuType){	
				var menuId =  "${menu.id}";
				var menuId2	= menuId.replace(/ /g,'');
				
				$("#menu_tree").jstree("create", $("#"+currentNode), false, {attr : {id: menuId2, name: "${menu.name}"}, data: "${menu.name}"}, false, true);
				checkAlreadySelectedMenuForLeft();
			}
		</c:forEach> 
	}else{
		<c:forEach items="${availableMenus}" var="menu">
			if("${menu.parentMenu.id}" == currentNode && "${menu.menuType}" == 'side'){		
				var menuId =  "${menu.id}";
				var menuId2	= menuId.replace(/ /g,'');
				$("#menu_tree").jstree("create", $("#"+currentNode), false, {attr : {id: menuId2, name: "${menu.name}"}, data: "${menu.name}"}, false, true);
				checkAlreadySelectedMenuForLeft();
			}
		</c:forEach> 
	}
	
}

function defaultJsonMenuData(){
	var jsonData = '[{ "data" : "Header",  "attr" : { "id" : "header", "name" : "Header","parent" : "Header" },"metadata": {"id" : "header", "name" : "Header"}}, '
	    			+'{"data":"Top","attr":{"id" : "top","name" : "Top","parent" : "Top"},"metadata": {"id" : "top", "name" : "Top"}}]';
	return jsonData;
}


 $(document).ready(function(){
	 $("#popupDialog").dialog("option", "position", "center");
	var jsonData = defaultJsonMenuData();
	constructMenuTreeForPopup(jsonData);
}); 
 function addCheckedOptionForMenu(e, data){
	// alert("${selectedMenu}");
	 var currentNode = data.rslt.obj.attr("id");
		var currentNodeName = data.rslt.obj.attr("name");
	 $("#selector_right span").each(function(index, elem){
		    node = this.id;
		    if(currentNode != node){
		    	$("span[id='"+node+"']").remove();
		    	$.jstree._reference($("#menu_tree")).uncheck_node('#'+node);
		    }
		});
		
		$('#selector_right:not(:has("#'+currentNode+'"))').append("<span id='"+currentNode+"' class='style3 style18 displayBlock'><input type='checkbox' name='detail'  checked='checked' id='"+currentNode+"' value='"+currentNodeName+"' onclick='removeClosedOptionForMenu(\""+currentNode+"\");'/>&nbsp;&nbsp;"+currentNodeName+"</span>");
	}
 

	//when click close icon remove that from right side
	function removeClosedOptionForMenu(id){
/* $("#menus option").each(function(){
			
			if($(this).val() == id){
				
				$("#menus option[value="+id+"]").remove();
			}
		}); */
		var nodeId = id;
		$("span[id='"+nodeId+"']").remove();
		var status = $.jstree._reference($("#menu_tree")).is_checked('#'+nodeId);
	 	if(status == true){
	 		$.jstree._reference($("#menu_tree")).uncheck_node('#'+nodeId);
	 	}
	}
	function addSelectedOptionForMenu(){
	/* 	var selectedValues = [];
		var selectedNames = [];
		$("#organizationTreeRight span").each(function(index, elem){
			 if(selection == "widgetImportFiles"){
				//alert("selection                "+selection);
				selectedValues.push($(this).attr("id"));
				selectedNames.push($(this).text());
			}else{
				selectedValues.push($(this).text());
			}
			
		});
		$('#'+appendTo).val(selectedValues);
		 if(selection == "widgetImportFiles"){
			$('#contentLink').val(selectedNames);
		}
		closeSelectDialog('popupDialog');
		
		_execute('target','');*/	
		var value="";
		var id = "";
		$("#selector_right input[type='checkbox'][name='detail']:checked").each(function(){
			var $this = $(this);      
			 id=$this.attr('id');
			value += $this.attr('value');
			selectedVal += '<option value='+id+' selected>'+value+'</option>';
		});
		var appendTo = "${appendTo}";
		document.getElementById('menuId').value = id;
		//alert(document.getElementById('menuId').value);
		
		//document.getElementById("quickNavigationUrl").value = value;
		$('#quickNavigationUrl').val(value);
	//	alert(document.getElementById("quickNavigationUrl").value);
		closeSelectDialog("popupDialog");
	}
function checkAlreadySelectedMenu(selectedValues){
	
	<c:forEach items="${availableMenus}" var="menu">
	$("#menus option").each(function(){
		if($(this).val() == '${menu.id}'){
			$('#selector_right:not(:has("#'+'${menu.id}'+'"))').append("<span id='"+'${menu.id}'+"' class='style3 style18 displayBlock'><input type='checkbox' name='detail'  checked='checked' id='"+'${menu.id}'+"' value='"+'${menu.name}'+"' onclick='removeClosedOptionForMenu(\""+'${menu.id}'+"\");'/>&nbsp;&nbsp;"+'${menu.name}'+"</span>");
		}
	});
	
</c:forEach>

if(selectedValues!=null && selectedValues!=""){
	console.log("selectedValues  -------------------->            "+selectedValues);
	$('#selector_right:not(:has("#'+selectedValues+'"))').append("<span id='"+selectedValues+"' name="+$("#quickNavigationUrl").val()+" class='style3 style18 displayBlock'><img class='pad-R5' onclick='removeClosedOption(\""+selectedValues+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+$("#quickNavigationUrl").val()+"</span>");
	$.jstree._reference($("#selector_right")).check_node('#'+selectedValues);	
}


}

function removeRootNodeCheckBoxForMenu(){
	$('#header').find('> a > .jstree-checkbox').remove();
	$('#top').find('> a > .jstree-checkbox').remove();
}

function checkAlreadySelectedMenuForLeft(){
	removeRootNodeCheckBoxForMenu();
	$("#selector_right span").each(function(index, elem){
		$("#selector_right input[type='checkbox'][name='detail']:checked").each(function(){
			var $this = $(this);      
			var id=$this.attr('id');
			$.jstree._reference($("#menu_tree")).check_node('#'+id);
		});
		
	});
	/* $("#menus option").each(function(){
		$.jstree._reference($("#menu_tree")).check_node('#'+$(this).val());
	}); */
}
</script>



<table class="tree-selector-table treeBoxShadow" style="background-image: url('/styles/easybpm/images/palette_line.jpg');vertical-align:top;">
<!-- <input id="quickNavigationUrl" type="hidden"   /> -->
	<tr>
		<td style="vertical-align:top;">
			<div id="menu_tree" class="tree-left" style="background: none !important; "></div>
		</td>
		<td style="vertical-align:top;">
			 <div id="selector_right" class="tree-right mar-L0">
			 
			</div>
		</td>
	</tr>
	<tr><td colspan="2"><div class="row-fluid pad-T6">
		<div class="span4">
	 	</div>
    	<div class="span3">
			<div class="button" id="saveButtonDiv">
				<button type="button" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onClick="addSelectedOptionForMenu();">
	                 <fmt:message key="button.save"/>
	    		</button>
			</div>
		</div>
		<div class="span3">
			<div class="button" id="cancelButtonDiv">
				<button type="button" id="cancelEvent" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onClick="closeSelectDialog('popupDialog');">
                  	<fmt:message key="button.cancel"/>
	    		</button>
			</div>
	 	</div>
	 	<div class="span2">
	 	</div>
	</div></td></tr>
</table>