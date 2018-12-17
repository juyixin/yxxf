 <%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">

function formRequiredTree(e,data){
	var currentNode = data.rslt.obj.data("id");
	var params = 'id='+currentNode;
   	document.location.href = "#bpm/module/createModule";
   	_execute('module_tree',params);
		
}
constructJsTree("module_js_tree",'${parentModules}',"formRequiredTree");
$("#module_js_tree").bind('before.jstree', function(event, data) {
	if (data.func == 'check_move') {
		return false;
	}
});
$(document).ready(function(){
	var height = $("#target").height();
	$("#module_form").css('height',parseInt(height)- 37);
	$("#module_tree").css('height',parseInt(height)- 37);
}); 

function deleteModule(){
	var selected_nodes = $("#module_js_tree").jstree("get_selected", null, true);
	
	var moduleName=selected_nodes.data("name");
	
	if(moduleName && moduleName!=undefined){
		if(moduleName == 'Default Module'){
			$.msgBox({
			    title: form.title.message,
			    content: "You cannot be delete Default Module",
			});
		}else{
		var moduleId=document.getElementById("id").value;
			var params="moduleId="+moduleId+"&screenName=managModules";
			var content="Are you sure you want to delete the Module: "+moduleName+" ?";
			
			$.msgBox({
			    title: form.title.confirm,
			    content: content,
			    type: "confirm",
			    buttons: [{ value: "Yes" }, { value: "No" }],
			    success: function (result) {
			        if (result == "Yes") {
			        	document.location.href = "#bpm/module/deleteModule";
			    		_execute('target',params);
			    		//refreshTableJSTree('table_tree');
			        }else{
			        	return false;
			        }
			    }
			});	
		}
	}else{
		$.msgBox({
		    title: form.title.message,
		    content: "Select a Module to delete",
		});	
	}
}
function refreshModuleTree(){
	 $("#module_js_tree").jstree("deselect_all");
}

function moduleExportToCSV(){
	var moduleId=document.getElementById("id").value;
	if(moduleId){
	 document.location.href = "bpm/module/moduelCSVExport?moduleId="+moduleId;
	}else{
		$.msgBox({
		    title: form.title.message,
		    content: "Select a Module to Export",
		});
	}
}

function importModule(){
	openSelectDialogForFixedPosition("popupDialog","","400","200","Import Module");
	  document.location.href = "#bpm/module/showModuleImport";
}
</script>


<div class="span12">
<div class="container-fluid" id="parent_container">
	<div class="row-fluid">
		<div class="span12">
			<div class="titleBG">
				<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="admin.module.list"/></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a title="Import" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="importModule()"  data-icon=""><button class="btn"><fmt:message key="button.import"/></button></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a title="Export" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="moduleExportToCSV()"  data-icon=""><button class="btn"><fmt:message key="button.export"/></button></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="deleteTable" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteModule();"  data-icon=""><button class="btn"><fmt:message key="table.button.delete"/></button></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a title="Create New" data-icon="" onclick="refreshModuleTree();_execute('module_tree','');" data-theme="b" data-role="button" href="#bpm/module/createModule"><button class="btn"><fmt:message key="button.createNew"/></button></a></span>
			</div>
		</div>
	</div>
	<div class="row-fluid">
	    <div class="span12">
			<div id="module_form" class="span3 manage_module_form" >
				<div id="module_js_tree" style="background-image: url('/styles/easybpm/images/palette_line.jpg');"></div>
			</div>
			<div id="module_tree" class="span9 manage_module_tree">
				<%@ include file="/WEB-INF/pages/module/defaultModule.jsp" %>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="screenName" value="managModules"/>
</div>
 