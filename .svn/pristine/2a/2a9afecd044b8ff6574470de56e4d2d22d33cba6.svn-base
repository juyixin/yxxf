/**
 * JSTree 
 * @author mahesh
 */

function showTreeSelectionWithData(title, selectionType, appendTo, selectedValues, callAfter, url,tableList){
	//clearPreviousPopup();
	
	var param='';
	param='selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues+'&callAfter='+callAfter+'&tableList='+JSON.stringify(tableList)+'&treeType='+title;
	_execute('popupDialog',param);
	document.location.href = "#"+url;
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
	       width: 'auto',
	       height: 'auto',
	       top: '10px',
	       modal: true,
	       position: [($(window).width() / 3), 150],
	       title: "Select " +selectionType.charAt(0).toUpperCase() + selectionType.slice(1) + " " + title
	 });
	 $myDialog.dialog("open");
}

function addSelectedOptionInField(appendTo,callAfter){
	
	defaultTreeNodes=[];
	var selectedValues = [];
	$("#treeRight span").each(function(index, elem){
		selectedValues.push($(this).text());
	});
	$('#'+appendTo).val(selectedValues);
	closeSelectDialog('popupDialog');
	if(callAfter != null && callAfter != ''){
		window[callAfter](selectedValues);
	}

	_execute('target','');
}


//remove root node checkbox while user selection
function removeTableRootNodeCheckBox(){
	for(var index=0;index<defaultTreeNodes.length;index++){
		$('#'+defaultTreeNodes[index]).find('> a > .jstree-checkbox').remove();	
	}
}

//method to render user child nodes of current node
function renderChildNodesInJSTree(e, data, childNodeUrl) {
	removeTableRootNodeCheckBox();
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var rootNode = data.inst.get_path()[0];
	var nodeLevel = data.inst.get_path().length;
	var selected_nodes = $("#treeLeft").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#treeLeft")); 
	var children = tree._get_children(selected_nodes);
	
	if ( children[0] == undefined && nodeLevel==1) {
		var childNodes = getChildNodesForSelectedNode(currentNodeName,rootNode,nodeLevel, childNodeUrl);
		if( childNodes != null && childNodes.length > 0 ){
			for(var i=0; i<childNodes.length; i++) {
				$("#treeLeft").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);
				if(childNodes[i].attr.isChild == true && childNodes[i].attr.isChild == 'true'){
					$('#'+childNodes[i].attr.id).find('> a > .jstree-checkbox').remove();
				}
			}
		}
		removeTableChildNodeCheckBox(data,childNodes);
		$("#treeRight span").each(function(index, elem){
			$.jstree._reference($("#treeLeft")).check_node('#'+$(this).text());
		});
	}
}

function getChildNodesForSelectedNode(currentNode, rootNode, nodeLevel, childNodeUrl){ 
	var data = [];
	$.ajax({
		url: childNodeUrl,
        type: 'POST',
        data: 'currentNode='+currentNode+'&rootNode='+rootNode+'&nodeLevel='+nodeLevel,
        dataType: 'json',
        async : false,
		success : function(response) {
				data=response;
		}
	});
	return data;
}


function addCheckedOptionInRightJSTree(currentNode, currentNodeName, selectionType){
	if(selectionType == 'single'){
		$("#treeRight span").each(function(index, elem){
		    node = this.id;
		    if(currentNode != node){
		    	$("span[id='"+node+"']").remove();
		    	$.jstree._reference($("#treeLeft")).uncheck_node('#'+node);
		    }
		});
		appenOptionToRightJSTree(currentNode, currentNodeName);
	}else {
		appenOptionToRightJSTree(currentNode, currentNodeName);
	}
}

function addCheckedOptionInRightJSTree(e, data, selectionType, selection){
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var rootName=data.inst.get_path()[0];
	var isParent=false;
	//to avoid the parent nodes
	for(var index=0;index<defaultTreeNodes.length;index++){
		if(currentNodeName==defaultTreeNodes[index]){
			isParent=true;
			break;
		}
	}
	
	if(!isParent){
		appenOptionToRightJSTree(currentNode, currentNodeName);
	}
}

//append the checked option to right side 
function appenOptionToRightJSTree(id,text){
	//replace for table column keys
	var fieldName=text.replace("-",".");
	$('#treeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' class='style3 style18 displayBlock'><img class='pad-R5' onclick='removeClosedOptionInJSTree(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+fieldName+"</span>");
}

//when click close icon remove that from right side
function removeClosedOptionInJSTree(nodeId){
	$("span[id='"+nodeId+"']").remove();
	var status = $.jstree._reference($("#treeLeft")).is_checked('#'+nodeId.replace(/[" "]/g,""));
 	if(status == true){
 		$.jstree._reference($("#treeLeft")).uncheck_node('#'+nodeId.replace(/[" "]/g,""));
 	}
}

function removeUnCheckedOptionInJSTree(e,data){
	removeClosedOptionInJSTree(data.rslt.obj.attr("id"));
}

//add already selected values to right
function addSelectedTreeValues(selectionType, selectedValues){
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			var selectedValuesId=selectedValues.replace(/[" "]/g,"");
			appenOptionToRightJSTree(selectedValuesId, selectedValues);
			$.jstree._reference($("#treeLeft")).check_node('#'+selectedValuesId);
		}else {
			if (selectedValues.indexOf(",") !=-1) {
				var values = selectedValues.split(",");
				for(var i=0; i< values.length; i++){
					var optionId=values[i].replace(/[" "]/g,"");
					appenOptionToRightJSTree(optionId, values[i]);
					$.jstree._reference($("#treeLeft")).check_node('#'+optionId);
				}
			} else {
				var selectedValuesId=selectedValues.replace(/[" "]/g,"");
				appenOptionToRightJSTree(selectedValuesId, selectedValues);
				$.jstree._reference($("#treeLeft")).check_node('#'+selectedValuesId);
			}
		}
	}
}

//remove non user child node checkbox when user selection
function removeTableChildNodeCheckBox(data,childNodes){
	removeTableRootNodeCheckBox();
	var currentNode = data.rslt.obj.attr("id");
	var isChild = data.rslt.obj.attr("isChild");
	if(isChild = true && isChild == 'true'){
		$('#'+currentNode).find('> a > .jstree-checkbox').remove();
	}
	for(var i=0; i<childNodes.length; i++){
		if(childNodes[i].attr.isChild == true && childNodes[i].attr.isChild == 'true'){
			$('#'+childNodes[i].attr.id).find('> a > .jstree-checkbox').remove();
		}
	}
}