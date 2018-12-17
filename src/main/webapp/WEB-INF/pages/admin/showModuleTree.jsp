<%@ include file="/common/taglibs.jsp" %>

<%@ include file="/common/messages.jsp" %>
<script>
$(document).ready(function(){
	var nodeValues = eval('${nodes}');
	treeConstruction(nodeValues);
});
var selectedVal ="";
function treeConstruction(nodeValues){
	$("#organizationTreeLeft").jstree({
        "json_data": {
	        "data" : nodeValues, 
	    },
	    "checkbox": {
              real_checkboxes: false,
              two_state: true
           },
        "plugins" : [ "themes", "json_data", "ui", "checkbox", "crrm"]
	}).bind("loaded.jstree", function(e, data) {
		checkAlreadySelectedValues();
	}).bind("select_node.jstree", function (e, data) { 
	}).bind('check_node.jstree', function(e, data) {
		appendCheckedOptionForTee(e,data); 
	}).bind('uncheck_node.jstree', function(e, data) {
		removeUnCheckedOption(e,data);	
	});
}
function checkAlreadySelectedValues(){
	<c:forEach items="${nodeDetail}" var="node">
	$("#"+'${appendTo}'+" option").each(function(){
		if($(this).val() == '${node.value}'){
			$('#organizationTreeRight:not(:has("#'+'${node.value}'+'"))').append("<span id='"+'${node.value}'+"' class='style3 style18 displayBlock'><input type='checkbox' name='detail'  checked='checked' id='"+'${node.value}'+"' value='"+'${node.label}'+"' onclick='removeClosedOption(\""+'${node.value}'+"\");'/>&nbsp;&nbsp;"+'${node.label}'+"</span>");
		}
	});
	</c:forEach> 
	
	$("#organizationTreeRight span").each(function(index, elem){
		$("#organizationTreeRight input[type='checkbox'][name='detail']:checked").each(function(){
			var $this = $(this);      
			var id=$this.attr('id');
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+id);
		});
		
	});
	
	
}

function appendCheckedOptionForTee(e,data){
		var currentNode = data.rslt.obj.attr("id");
		var currentNodeName = data.rslt.obj.attr("name");
		$('#organizationTreeRight:not(:has("#'+currentNode+'"))').append("<span id='"+currentNode+"' class='style3 style18 displayBlock'><input type='checkbox' name='detail'  checked='checked' id='"+currentNode+"' value='"+currentNodeName+"' onclick='removeClosedOption(\""+currentNode+"\");'/>&nbsp;&nbsp;"+currentNodeName+"</span>");
}

function addSelectedOptionForTree(){
	$("#organizationTreeRight input[type='checkbox'][name='detail']:checked").each(function(){
		var $this = $(this);      
		var id=$this.attr('id');
		var value = $this.attr('value');
		selectedVal += '<option value='+id+' selected>'+value+'</option>';
	});
	var requestorId = "${appendTo}";
	document.getElementById(requestorId).innerHTML = selectedVal;
	closeSelectDialog("userPopupDialog");
}
//This Will call when user close the popup
$('#userPopupDialog').bind('dialogclose', function(event) {
	 $("#"+'${appendTo}'+" option").prop('selected', 'selected');
});
//This will call when user cancel the popup
function closeDialogue(){
	closeSelectDialog('userPopupDialog');
		 $("#"+'${appendTo}'+" option").prop('selected', 'selected');
}
</script>
<div class="container-fluid form-background">
	<div class="row-fluid">
		<div class="span6">
			<label class="style3 style18"><fmt:message key="organizationTree.available"/></label>
		</div>
		<div class="span3">
			<label class="style3 style18"><fmt:message key="organizationTree.selected"/></label>
		</div>
		<div class="span3">
			<label class="style3 style18 fontBold"><fmt:message key="userSelection.selectionType"/> : ${fn:toUpperCase(fn:substring(selectionType, 0, 1))}${fn:toLowerCase(fn:substring(selectionType, 1,fn:length(selectionType)))} </label>
		</div>
	</div>
	<div class="row-fluid organization-tree">
		<div class="span6 organization-tree-left" id="organizationTreeLeft"></div>
		<div class="span6 organization-tree-right pad-L20" id="organizationTreeRight"></div>
	</div>
	<div class="row-fluid pad-T6">
		<div class="span4">
	 	</div>
    	<div class="span3">
			<div class="button" id="saveButtonDiv">
				<button type="button" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onClick="addSelectedOptionForTree()";>
	                 <fmt:message key="button.save"/>
	    		</button>
			</div>
		</div>
		<div class="span3">
			<div class="button" id="cancelButtonDiv">
				<button type="button" id="cancelEvent" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onClick="closeDialogue()";>
                  	<fmt:message key="button.cancel"/>
	    		</button>
			</div>
	 	</div>
	 	<div class="span2">
	 	</div>
	</div>
</div>    
