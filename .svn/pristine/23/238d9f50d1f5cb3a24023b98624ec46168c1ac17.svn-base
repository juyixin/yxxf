<%@ include file="/common/taglibs.jsp" %>
<script type="text/javascript">
$("#module_js_tree").bind("move_node.jstree", function (e, data) {
	  var newParent = data.rslt.np.data("name");
	  var oldParent = data.rslt.op.data("name");
	  var isParent = data.rslt.o.attr("isParent");
	  var nodeId = data.rslt.o.data("id");
	  var newparentNodeId = data.rslt.np.data("id");
	  var newNodeIsParent = data.rslt.np.attr("isParent");
	  var oldNodeIsParent = data.rslt.o.attr("isParent");
	    //Copy Module 
	    if(newNodeIsParent == "false" && oldNodeIsParent == "false"){
		  	$.msgBox({
				    title:form.title.message,
				    content:"Child module can't be moved to another child module",
				});
			document.location.href = "#bpm/module/endUserModule";
			_execute('target','');
		}else if((newParent && oldParent) || (isParent == "false")){
		    if((newParent != oldParent) || (isParent == "false" && !oldParent)){
		    
			    if(nodeId && newparentNodeId){
				    var params = 'nodeId='+nodeId+'&parentNodeId='+newparentNodeId+"&screenName=myModule";;
					document.location.href = "#bpm/module/copyModule";
					_execute('target',params);
			    } 
		    }else if(newParent == oldParent){
		    	//Module migration
		    	var currentNode = data.rslt.o.data("id");
		    	var movedNode = data.rslt.or.data("id");
		    	var params = 'currentModule='+currentNode+'&movedModule='+movedNode+"&screenName=myModule";
				document.location.href = "#bpm/module/migrationModule";
				_execute('target',params);
		    }
	    }else{
	    	
	    	 $.msgBox({
	   		    title:form.title.message,
	   		    content:"Parent module can't able to moved as child",
	   		});  
	    	 document.location.href = "#bpm/module/endUserModule";
				_execute('target','');
	    }
	   });
function refreshClassification(){
	document.location.href = "#bpm/module/endUserModule";
	_execute('target',params);
	
}

constructJsTree("module_js_tree",'${endUserModules}',"formRequiredTree");

//Form the right tree
function formRequiredTree(e,data){
	var level = data.inst.get_path().length;
	editModule(e,data);
	var moduleId ;
	var moduleName;
	if(level == 1){
		moduleId = currentNode = data.rslt.obj.data("id");
		moduleName = data.rslt.obj.data("name");
	}else{
		moduleId = currentNode = data.rslt.obj.attr("id");
		moduleName = data.rslt.obj.attr("name");
	}
	getChildModule(moduleName,moduleId,level);
		
}
function contains(a, obj) {
    var i = a.length;
    while (i--) {
       if (a[i] === obj) {
           return true;
       }
    }
    return false;
}

function editModule(e,data){
	var level = data.inst.get_path().length;
	var currentNode;
	if(level == 1){
		currentNode = data.rslt.obj.data("id");
	}else{
		currentNode = data.rslt.obj.attr("id");
	}
		var params = 'id='+currentNode;
    	document.location.href = "#bpm/module/createModule";
    	_execute('module_tree',params);
	
}
var checked_ids = []; 
function getChildModule(moduleName, moduleId,level){
   if(level == 1){
	<c:forEach items="${availableModules}" var="module">
	if("${module.id}" == moduleId){	
		if(!contains(checked_ids,moduleId)){
			<c:forEach items="${module.childModules}" var="childModule">
				<c:forEach items="${childModule.administrators}" var="user">
			if("${currentUser}" == "${user}" ){	
				$("#module_js_tree").jstree("create", $("#"+moduleId), false, {metadata : {id: "${childModule.id}", name: "${childModule.name}"},attr : {id: "${childModule.id}", name: "${childModule.name}",isParent: "false"}, data: "${childModule.name}"}, false, true);				
			}
			</c:forEach> 
			
			<c:forEach items="${childModule.departments}" var="department">
			if("${department.name}" == "${currentDepartment}" ){	
				$("#module_js_tree").jstree("create", $("#"+moduleId), false, {metadata : {id: "${childModule.id}", name: "${childModule.name}"},attr : {id: "${childModule.id}", name: "${childModule.name}",isParent: "false"}, data: "${childModule.name}"}, false, true);				
				}
			</c:forEach> 
			
			</c:forEach> 	
			checked_ids.push(moduleId);
		}
	}
		</c:forEach> 
}
}

$(document).ready(function(){
	var height = $("#target").height();
	$("#module_form").css('height',parseInt(height)- 37);
	$("#module_tree").css('height',parseInt(height)- 37);
}); 

function deleteModule(){
	var selected_nodes = $("#module_js_tree").jstree("get_selected", null, true);
	
	var moduleName=selected_nodes.data("name");
	
	if(moduleName && moduleName!=undefined){
		var moduleId=document.getElementById("id").value;
			var params="moduleId="+moduleId+"&screenName=myModule";
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
				<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="my.module.list"/></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a title="Import" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="importModule()"  data-icon=""><fmt:message key="button.import"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a title="Export" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="moduleExportToCSV()"  data-icon=""><fmt:message key="button.export"/></a></span>
				<span class="floatRight fontSize14 pad-R10 pad-T10"><a style="padding:10px;" id="deleteTable" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteModule();"  data-icon=""><fmt:message key="table.button.delete"/></a></span>
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
<input type="hidden" id="screenName" value="myModule"/>
</div>
