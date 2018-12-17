/* ---------------------------- JsTree ----------------------------------------*/
var selectedVal="";
var selectedArray=new Array();
var selectedNodeInJSTree = "";
var multiOrganizerCount = 1;
//construct a jsTree
function constructJsTree(selector,jsonData,functionName,isNeedToConstructTree){

	var json_data = eval(jsonData);
	$("#"+selector).jstree({

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
    		window[functionName](e, data);
	    	if(isNeedToConstructTree){
		    	var currentNode = data.rslt.obj.attr("id");
		    	var rootNode = data.inst.get_path(data.node,true)[0];
		    	var level = data.inst.get_path().length;
		    	var selected_nodes = $(this).jstree("get_selected", null, true); 
		    	var tree = $.jstree._reference(this); 
		    	var children = tree._get_children(selected_nodes); 
		    	var children_length = data.inst._get_children(data.rslt.obj).length;
		    	/*if(selectedNodeInJSTree == currentNode){
		    		if(children[0] != undefined){
			    		children.remove();
			    	}
		    	}*/

		    	var parentNode = "";
		    	if(level > 1){
		    		parentNode = data.rslt.obj.parents("li").attr("id");
		    	}else{
		    		parentNode = currentNode;
		    	}
		    	if(currentNode != "ALL" && children_length == 0){

		    		getActiveChildData(currentNode, parentNode, level, rootNode);
		    		$("#Organization > a").removeClass('jstree-hovered');
		    		$("#Group > a").removeClass('jstree-hovered');
		    		$("#Role > a").removeClass('jstree-hovered');
		    		$("li[id~='"+currentNode+"']> ins.jstree-icon").trigger('click');
		    		$("#"+currentNode+ "> a").addClass('jstree-clicked jstree-hovered');
		    		
	    		}	
    	}else{
    		var currentNode = data.rslt.obj.attr("id");
    		if(selector == "module_relation_tree"){
    			var level = data.inst.get_path().length;
    			if(level == 3){
            		$("#"+currentNode).removeClass('jstree-open');
                	$("#"+currentNode).removeClass('jstree-closed');
                	$("#"+currentNode).addClass('jstree-leaf');
    			}else if(level == 2){
    				var currentNodeName = data.rslt.obj.attr("name");
    				if(currentNodeName == "Forms"){
    					$("#"+currentNode).removeClass('jstree-open');
                    	$("#"+currentNode).removeClass('jstree-closed');
                    	$("#"+currentNode).addClass('jstree-leaf');
    				}
    			}else if(level==1){
    				allowMovingModuleOrder();
    			}
    		}else{

        		$("#"+currentNode).removeClass('jstree-open');
            		$("#"+currentNode).removeClass('jstree-closed');
            		$("#"+currentNode).addClass('jstree-leaf');
    		}
    	}
    	var currentNode = data.rslt.obj.attr("id");
   		$("li[id~='"+currentNode+"'] > ins.jstree-icon").trigger('click');
    }).bind("open_node.jstree", function (e, data) { // this binding will collapse all nodes whenever user expands any single node
    	var currentNode = data.rslt.obj.attr("id");
    	$('#'+selector).jstree('select_node', '#'+currentNode);
    	if(selectedNodeInJSTree != ""){
    		$("#"+selectedNodeInJSTree+ "> a").removeClass('jstree-clicked');
    	}else{
    		$("#All > a").removeClass('jstree-clicked');
    	}
    	selectedNodeInJSTree = currentNode;
    });
}

function defaultJsonData(){
	var jsonData = '[{ "data" : "所有用户",  "attr" : { "id" : "All", "name" : "All","parent" : "All" },"metadata": {"id" : "All", "name" : "All"}}, '
	   	+' {"data":"按部门","attr":{"id" : "Organization","name" : "Organization","parent" : "Organization"},"metadata": {"id" : "Organization", "name" : "Organization"}},'
	    +'{"data":"按用户组","attr":{"id" : "Group","name" : "Group","parent" : "Group"},"metadata": {"id" : "Group", "name" : "Group"}},'
	    +'{"data":"按角色","attr":{"id" : "Role","name" : "Role","parent" : "Role"},"metadata": {"id" : "Role", "name" : "Role"}}]';
	return jsonData;
}


function defaultDeptJsonData(){
	var jsonData = '[ {"data":"组织架构树","attr":{"id" : "Organization","name" : "Organization","parent" : "Organization"},"metadata": {"id" : "Organization", "name" : "Organization"}}]'
	return jsonData;
}

/* ---------------------------- User form----------------------------------------*/

   function changeUserWizardByButton(buttonType){
	   var from = $('#from').val();
	   if(from != 'profile'){
		   changeWizardByButton(buttonType);
	   } else {
		   var count = 0;
		   var currentTabId = $('.active-step a').attr("id");
		   currentTabId = currentTabId.split('-');
		   if(buttonType == 'next') {
			   count = ++currentTabId[1];
			   ++count;
		   } else if(buttonType == 'prev') {
			   count = --currentTabId[1];
			   --count;
		   }
		   var nextTabTitle = '#' + currentTabId[0] + '-' + count;
		   var nextTab = '#wizardTab-content-' + count;
		   if($(nextTab).hasClass('displayNone')){
			   $(nextTab).addClass('displayBlock');
			   $(nextTab).removeClass('displayNone');
			   $(nextTab).siblings().removeClass('displayBlock');
			   $(nextTab).siblings().addClass('displayNone');
		   }
		   $(nextTabTitle).parent().addClass('active-step');
		   $(nextTabTitle).parent().siblings().removeClass('active-step');
		   $(nextTabTitle).parent().siblings().addClass('completed-step');
		   userTabButtonsControl(count);
	   }
   }
   
   
   function changeUserWizardByTab(){
	   var from = $('#from').val();
	   if(from != 'profile'){
		   changeWizardByTab();
	   } else {
		   $("#wizardTabs a").each(function(){
				$(this).click(function(){
					var tabId = $(this).attr('id');
					tabId = tabId.split('-');
					var tabContent = document.getElementById('wizardTab-content-' + tabId[1]);
					$(tabContent).addClass('displayBlock');
					$(tabContent).removeClass('displayNone');
				    $(this).parent().addClass('active-step');
				    $(this).parent().siblings().removeClass('active-step');
				    $(this).parent().siblings().addClass('completed-step');
				    $(tabContent).siblings().addClass('displayNone');
				    $(tabContent).siblings().removeClass('displayBlock');
				    userTabButtonsControl(tabId[1]);
				});
			});
	   }
   }
   
   function userTabButtonsControl(count){
	   if(count > 1){
		   $('#previousButton').removeAttr("disabled", "disabled");
	   }else{
		   $('#previousButton').attr("disabled", "disabled");
	   }
	   if(count >= 3){
		   $('#nextButton').attr("disabled", "disabled");
	   }else{
		   $('#nextButton').removeAttr("disabled", "disabled");
	   }
   }
   
   function changeWizardByButton(buttonType){
	   var currentTabId = $('.active-step a').attr("id");
	   currentTabId = currentTabId.split('-');
	   if(buttonType == 'next')
	   var count = ++currentTabId[1];
	   else if(buttonType == 'prev')
	   var count = --currentTabId[1];
	   var nextTabTitle = '#' + currentTabId[0] + '-' + count;
	   var nextTab = '#wizardTab-content-' + count;
	   if($(nextTab).hasClass('displayNone')){
		   $(nextTab).addClass('displayBlock');
		   $(nextTab).removeClass('displayNone');
		   $(nextTab).siblings().removeClass('displayBlock');
		   $(nextTab).siblings().addClass('displayNone');
	   }
	   $(nextTabTitle).parent().addClass('active-step');
	   $(nextTabTitle).parent().siblings().removeClass('active-step');
	   $(nextTabTitle).parent().siblings().addClass('completed-step');
	   userFormButtonsControl(count);
   }
   
   function changeWizardByTab(){
		$("#wizardTabs a").each(function(){
			$(this).click(function(){
				var tabId = $(this).attr('id');
				tabId = tabId.split('-');
				var tabContent = document.getElementById('wizardTab-content-' + tabId[1]);
				$(tabContent).addClass('displayBlock');
				$(tabContent).removeClass('displayNone');
			    $(this).parent().addClass('active-step');
			    $(this).parent().siblings().removeClass('active-step');
			    $(this).parent().siblings().addClass('completed-step');
			    $(tabContent).siblings().addClass('displayNone');
			    $(tabContent).siblings().removeClass('displayBlock');
			    userFormButtonsControl(tabId[1]);
			});
		});
   }
   
   function changeFormWizardByTab(){
		$("#formWizardTabs li").each(function(){
			$(this).click(function(){
				var tabId = $(this).attr('id');
				tabId = tabId.split('-');
				var tabContent = document.getElementById('formWizardTab-content-' + tabId[1]);
				$(tabContent).addClass('displayBlock');
				$(tabContent).removeClass('displayNone');
			    $(this).addClass('active');
			    $($(this).find(".label")).addClass("label badge-inverse");
			    $(this).siblings().removeClass('active');
			    $($(this).siblings().find(".label")).removeClass("badge-inverse");
			    $(tabContent).siblings().addClass('displayNone');
			    $(tabContent).siblings().removeClass('displayBlock');
			    userFormButtonsControl(tabId[1]);
			});
		});
  }
   
   function userFormButtonsControl(count){
	   	   if(count > 1){
			   $('#previousButton').removeAttr("disabled", "disabled");
		   }else{
			   $('#previousButton').attr("disabled", "disabled");
		   }
		   if(count > 4){
		
			   $('#nextButton').attr("disabled", "disabled");
			   /*$('#saveButton').removeAttr('disabled');*/
		   }else{
			   $('#nextButton').removeAttr("disabled", "disabled");
			   /*$('#saveButton').attr("disabled", "disabled");*/
		   }
	}
   
   function passwordChanged(passwordField) {
	    if (passwordField.id == "password") {
	        var origPassword = "${user.password}";
	    } else if (passwordField.id == "confirmPassword") {
	        var origPassword = "${user.confirmPassword}";
	    }
	
	    if (passwordField.value != origPassword) {
	        createFormElement("input", "hidden",  "encryptPass", "encryptPass",
	                          "true", passwordField.form);
	    }
   }

/* ---------------------------- Role form----------------------------------------*/


/* ---------------------------- Group form----------------------------------------*/

function showGroupTree(title, id, selectType){
	$("div#userPopupDialog").empty();
	$("div#departmentPopupDialog").empty();
	$("div#groupPopupDialog").empty();
	$("#popupDialog").dialog("destroy");
	$("#popupDialog1").dialog("destroy");
	$("#popupDialog2").dialog("destroy");
	$("#popupDialog3").dialog("destroy");
	$("#popupDialog4").dialog("destroy");
	$("#userPopupDialog").dialog("destroy");
	$("#userPopupDialogManager").dialog("destroy");
	$("#userPopupDialogSecretary").dialog("destroy");
	$("#departmentPopupDialog").dialog("destroy");
	$("#partTimeDepartmentPopupDialog").dialog("destroy");
	$("#groupPopupDialog").dialog("destroy");
	var params = 'title='+title+'&id='+id+'&selectType='+selectType;
	document.location.href = "#bpm/admin/showGroupTree";
	_execute('groupPopupDialog',params);
	$myDialog = $("#groupPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       modal: true,
                       title: title
	             });
	 $myDialog.dialog("open");
}

/* ---------------------------- Department form----------------------------------------*/
function createDepartmentUser(){
	var a = $.jstree._focused().get_selected();
	var id = a.attr("name");
	
	var parentId = $.jstree._focused().get_path()[0];
	if(parentId == id && id != 'Organization' && id != 'all' && id!= undefined){
		validateMessageBox(form.title.message, form.msg.selectValidRoleOrGroup, false);
		
		return false;
	}
	if(parentId == 'all' || parentId == undefined){
		parentId = 'Organization';
		id = 'Organization';
		_execute('target','method=add&from=department&departmentId='+id+'&parentId='+parentId);
		return true;
	}else if(parentId == 'Organization'){
		parentId = 'Organization';
		_execute('target','method=add&from=department&departmentId='+id+'&parentId='+parentId);
		return true;
	}else if(parentId == 'Group' && parentId!=id){
		_execute('target','method=add&from=group&groupId='+id+'&parentId='+parentId);
		return true;
	}else if(parentId == 'Role' && parentId!=id){
		_execute('target','method=add&from=role&roleId='+id+'&parentId='+parentId);
		return true;
	}else{
		_execute('target','method=add&from=department&departmentId=Organization&parentId=Organization');
		return true;
	}
}

function showDepartmentTree(title, id, selectType){
	
	$("div#userPopupDialog").empty();
	$("div#departmentPopupDialog").empty();
	$("div#groupPopupDialog").empty();
	$("#popupDialog").dialog("destroy");
	$("#popupDialog1").dialog("destroy");
	$("#popupDialog2").dialog("destroy");
	$("#popupDialog3").dialog("destroy");
	$("#popupDialog4").dialog("destroy");
	$("#userPopupDialog").dialog("destroy");
	$("#userPopupDialogManager").dialog("destroy");
	$("#userPopupDialogSecretary").dialog("destroy");
	$("#departmentPopupDialog").dialog("destroy");
	$("#partTimeDepartmentPopupDialog").dialog("destroy");
	$("#groupPopupDialog").dialog("destroy");
	var params = 'title='+title+'&id='+id+'&selectType='+selectType;
	document.location.href = "#bpm/admin/showDepartmentTree";
	_execute('departmentPopupDialog',params);
	$myDialog = $("#departmentPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       modal: true,
                       title: title
	             });
	 $myDialog.dialog("open");
}

function showPartTimeDepartmentTree(title, id){
	$("#popupDialog").dialog("destroy");
	$("#popupDialog1").dialog("destroy");
	$("#popupDialog2").dialog("destroy");
	$("#popupDialog3").dialog("destroy");
	$("#popupDialog4").dialog("destroy");
	$("#userPopupDialog").dialog("destroy");
	$("#userPopupDialogManager").dialog("destroy");
	$("#userPopupDialogSecretary").dialog("destroy");
	$("#departmentPopupDialog").dialog("destroy");
	$("#partTimeDepartmentPopupDialog").dialog("destroy");
	$("#groupPopupDialog").dialog("destroy");
	var params = 'title='+title+'&id='+id;
	document.location.href = "#bpm/admin/showPartTimeDepartmentTree";
	_execute('partTimeDepartmentPopupDialog',params);
	$myDialog = $("#partTimeDepartmentPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       modal: true,
                       title: title
	             });
	 $myDialog.dialog("open");
}

function getPartTimeDepartmentList(e,data){
	document.getElementById("partTimeDepartment_tree_right").innerHTML = "";
	var departmentId = data.rslt.obj.data("id");
	$.ajax({
		url: 'bpm/admin/getDepartmentsByLabelValue',
        type: 'GET',
        dataType: 'json',
		async: false,
		data: 'id=' + departmentId ,
		success : function(response) {
			var partTimeDepartmentData = ""; 
			if(response.length > 0){
				partTimeDepartmentData = "<div class='padding5' id='partTimeDepartmentList'>";
    			for(var i=0; i<response.length; i++){
    				var departmentName = response[i].departmentName;
    				var departmentId = response[i].departmentId;
    		       		partTimeDepartmentData += "<input type='radio' name='partTimeDepartmentDetail' id='"+departmentId+"' value='"+departmentId+"'><span class='pad-L5'>"+departmentName+"</span></input><br>";
    			}
				partTimeDepartmentData += '</div>';
				partTimeDepartmentData += '<div class="button padding20" id="saveButtonDiv">'+
				 '<button type="submit" class="btn btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="loadUserPartTimeDepartment();">'+
	        	 'Submit' +
	    		 '</button></div>';
				} 
			document.getElementById("partTimeDepartment_tree_right").innerHTML = partTimeDepartmentData;
			} 
	});
}


function loadUserPartTimeDepartment(){
	var selectedVal = "";
	var selected = "";
	selected = $("#partTimeDepartmentList input[type='radio'][name='partTimeDepartmentDetail']:checked");
	if (selected.length > 0){
		selectedVal = selected.val(); 
		document.getElementById("partTimeDepartment").value = selectedVal;
	}
	
	closeSelectDialog("partTimeDepartmentPopupDialog");
}

function refreshJSTree(treeId){
	//$('#'+treeId).jstree('refresh');
	var jsonData = defaultJsonMenuData();
	constructMenuTree(jsonData);
}



function showMailUserSelection(){
	var selectedValue=$('#to').val();
	showUserSelection('User', 'multi' , 'to', selectedValue, '');
}

//method to show organization tree selection in popup for select user
function showUserSelection(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showOrganizationTree";
	if(appendTo == "to" || appendTo == "ccto" || appendTo == "bccto"){
		_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues+'&selection=user&rootNodeURL=bpm/admin/getUserRootNodes&childNodeURL=bpm/admin/getUserSelectionChildNodes&callAfter='+callAfter+'&from=&parentId=&currentUser=');
	}else if(appendTo == 'manager'|| appendTo == 'secretary'){
		var currentUser = document.getElementById('username').value;
		selectedValues = $("#"+appendTo).val();
		_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues+'&selection=user&rootNodeURL=bpm/admin/getUserRootNodes&childNodeURL=bpm/admin/getUserSelectionChildNodes&callAfter='+callAfter+'&from=&parentId=&currentUser='+currentUser);
		
	}else{
		_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues.value+'&selection=user&rootNodeURL=bpm/admin/getUserRootNodes&childNodeURL=bpm/admin/getUserSelectionChildNodes&callAfter='+callAfter+'&from=&parentId=&currentUser=');
	}
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择用户(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择用户(多选)";
	}
	
	$myDialog.dialog({
		width: 'auto',
		height: 'auto',
		top: '10px',
		modal: true,
		position: [($(window).width() / 3), 60],
		title: title
     });
	 $myDialog.dialog("open");
}

//method to show organization tree selection in popup for select group
function showRoleSelection(title, selectionType, appendTo, selectedValues, callAfter,isGraduation){
	//clearPreviousPopup();
	var isGraduationTree=false;
	if(isGraduation){
		var userRole=$("#currentUserRoles").val().split(",");
		var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
		if(!isAdmin){
			isGraduationTree=true;
		}
	}
	
	selectedValues = $("#"+appendTo).val();
	
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&isGraduationTree='+isGraduationTree+'&selection=role&rootNodeURL=bpm/admin/getRoleNodes&childNodeURL=&selectedValues='+selectedValues+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择角色(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择角色(多选)";
	}
	
	$myDialog.dialog({
		width: 'auto',
		height: 'auto',
		top: '10px',
		modal: true,
		position: [($(window).width() / 3), 70],
		title: title
	 });
	 $myDialog.dialog("open");
}

//method to show organization tree selection in popup for select role
function showGroupSelection(title, selectionType, appendTo, selectedValues, callAfter,isGraduation){
	//clearPreviousPopup();
	var isGraduationTree=false;
	if(isGraduation){
		isGraduationTree=true;
	}
	selectedValues = $("#"+appendTo).val();
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&isGraduationTree='+isGraduationTree+'&selection=group&rootNodeURL=bpm/admin/getGroupNodes&childNodeURL=bpm/admin/getGroupNodes&selectedValues='+selectedValues+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择用户组(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择用户组(多选)";
	}
	
	$myDialog.dialog({
		width: 'auto',
		height: 'auto',
		top: '10px',
		modal: true,
		position: [($(window).width() / 3), 70],
		title: title
	});
	$myDialog.dialog("open");
}

//method to show organization tree selection in popup for select department
function showDepartmentSelection(title, selectionType, appendTo, selectedValues, callAfter,isGraduation){
	//clearPreviousPopup();
	var isGraduationTree=false;
	if(isGraduation){
		isGraduationTree=true;
	}
	
	selectedValues = $("#"+appendTo).val();
	
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&isGraduationTree='+isGraduationTree+'&selection=department&rootNodeURL=bpm/admin/getDepartmentNodes&childNodeURL=bpm/admin/getDepartmentChildNodes&selectedValues='+selectedValues+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择部门(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择部门(多选)";
	}
	
	$myDialog.dialog({
		width: 'auto',
		height: 'auto',
		top: '10px',
		modal: true,
		position: [($(window).width() / 3), 70],
		title: title
	});
	$myDialog.dialog("open");
}

function showModuleSelection(title, selectionType, appendTo, selectedValues, callAfter){
	document.location.href = "#bpm/admin/moduleTree";
	var moduleId = document.getElementById("id").value;
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&moduleId='+moduleId);
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

function showFormSelection(title, selectionType, appendTo, selectedValues, callAfter){
	document.location.href = "#bpm/admin/formTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues.value+'&callAfter='+callAfter);
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

function showProcessSelection(title, selectionType, appendTo, selectedValues, callAfter){
	document.location.href = "#bpm/admin/processTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues.value+'&callAfter='+callAfter);
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

function showWidgetSelection(title, selectionType, appendTo, selectedValues, callAfter){
	
	document.location.href = "#bpm/admin/showWidgetSelection";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/admin/getWidgetRootNodes&selectedValues='+selectedValues.value+'&callAfter='+callAfter);
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
		width: 'auto',
		height: 'auto',
		top: '10px',
		modal: true,
		position: [($(window).width() / 3), 70],
		title: title
	});
	$myDialog.dialog("open");
}

function  showUserWidgetSelection(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	//alert("selectedValues  ++++ "+selectedValues);
	document.location.href = "#bpm/admin/showWidgetSelection";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/admin/getUserWidgetRootNodes&selectedValues='+selectedValues.value+'&callAfter='+callAfter);
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

function showLayoutSelection(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	//alert(selectedValues);
	var selected_nodes = $("#layoutTree").jstree("get_selected", null, true); 
	var assignedTo = selected_nodes.attr("name");
	var rootNode = selected_nodes.attr("root");
	var layoutDepartments = "";
	var layoutRoles = "";
	if(id == 'Roles'){
		validateMessageBox(form.title.error, form.msg.selectValidRole , "error");
		return false;
	}
	
	if(id == 'Departments'){
		validateMessageBox(form.title.error, form.msg.selectValiddepartment , "error");
		return false;
	}

	if(rootNode == "Role"){
		$("#roles").val(assignedTo);
		layoutRoles = assignedTo;
	}
	if(rootNode == "Department"){
		$("#departments").val(assignedTo);
		layoutDepartments = assignedTo;
	}
	
	document.location.href = "#bpm/user/showLayoutSelection";
	_execute('popupDialog','selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&appendTo='+appendTo+'&assignedTo='+assignedTo+'&layoutDepartments='+layoutDepartments+'&layoutRoles='+layoutRoles+'&layoutType=adminLayout');
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: '50%',
                       height: 'auto',
                       minHeight:300,
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

function showUserLayoutSelection(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	//alert(selectedValues);
	var selected_nodes = $("#layoutTree").jstree("get_selected", null, true); 
	var assignedTo = selected_nodes.attr("name");
	var rootNode = selected_nodes.attr("root");
	var layoutDepartments = "";
	var layoutRoles = "";
	if(id == 'Roles'){
		validateMessageBox(form.title.error, form.msg.selectValidRole , "error");
		return false;
	}
	
	if(id == 'Departments'){
		validateMessageBox(form.title.error, form.msg.selectValidDepartment , "error");
		return false;
	}

	if(rootNode == "Role"){
		$("#roles").val(assignedTo);
		layoutRoles = assignedTo;
	}
	if(rootNode == "Department"){
		$("#departments").val(assignedTo);
		layoutDepartments = assignedTo;
	}
	document.location.href = "#bpm/user/showLayoutSelection";
	_execute('popupDialog','selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&appendTo='+appendTo+'&assignedTo='+assignedTo+'&layoutDepartments='+layoutDepartments+'&layoutRoles='+layoutRoles+'&layoutType=userLayout');
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: '50%',
                       height: 'auto',
                       minHeight:300,
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}


function showQuickNaviURLSelection(title, selectionType, appendTo, selectedValues, callAfter){
/*	//clearPreviousPopup();
	//alert("showQuickNaviURLSelection "+selectedValues.value);
	//document.location.href = 	"#bpm/admin/menuTree";
//	  openSelectDialogForFixedPosition("popupDialog","","515","210","Import CSV");
//	  document.location.href = "#bpm/admin/menuTree?title='Menu'&id=null&selectType='single'"; 

	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/admin/getTreeNode&childNodeURL=bpm/admin/getTreeNode&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&selection='+'menu'+'&from=&parentId=');
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: "Select " +selectionType.charAt(0).toUpperCase() + selectionType.slice(1) + " " + title
	             });
	 $myDialog.dialog("open");*/
	
	
}

function showQuickNaviURLnew(title, selectionType, appendTo, selectedValues, callAfter){
	
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selection=menuQuick&rootNodeURL=bpm/admin/getTreeNode&childNodeURL=bpm/admin/getTreeNode&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	
	title = "请选择菜单";
	
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

function homePagePreview(noOfColumn,selectedWidgets,isRender,isSave){
	//alert(" homePagePreview  =======   "+selectedWidgets+ "  noOfColumn    "+noOfColumn);
	
	document.location.href = "#bpm/user/homePagePreviewandSave";
	_execute('divsample','noOfColumn='+noOfColumn+'&selectedWidgets='+selectedWidgets+'&isRender='+isRender+'&isSave='+isSave);
}
function userHomePagePreview(noOfColumn,selectedWidgets,isRender,isSave){
//	alert(" homePagePreview  =======   "+selectedWidgets+ "  noOfColumn    "+noOfColumn);
	
	document.location.href = "#bpm/user/userHomePagePreviewandSave";
	_execute('divsample','noOfColumn='+noOfColumn+'&selectedWidgets='+selectedWidgets+'&isRender='+isRender+'&isSave='+isSave);
}

//method to render child nodes of current node
function renderChildNodes(e, data, selection) {
	if(selection == 'user'){
		renderUserChildNodes(e, data);
	} else if(selection == 'department') {
		renderDepartmentChildNodes(e, data);
	} else if(selection == 'group') {
		renderGroupChildNodes(e, data);
	} else if(selection == 'dataDictionary') {
		renderDataDictionaryChildNodes(e, data);
	}
}

//method to render user child nodes of current node
function renderUserChildNodes(e, data) {
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var rootNode = data.inst.get_path()[0];
	var nodeLevel = data.inst.get_path().length;
	var selected_nodes = $("#organizationTreeLeft").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#organizationTreeLeft")); 
	var children = tree._get_children(selected_nodes); 
	var children_length = data.inst._get_children(data.rslt.obj).length;
	if ( children_length == 0 ) {
		var childNodes = getUserchildNodes(currentNodeName,rootNode,nodeLevel);
		if( childNodes != null && childNodes.length > 0 ){
			for(var i=0; i<childNodes.length; i++) {
				$("#organizationTreeLeft").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);
				if(childNodes[i].attr.isUser != true && childNodes[i].attr.isUser != 'true'){
					$('#'+childNodes[i].attr.id).find('> a > .jstree-checkbox').remove();
				}
			}
		}else{
			$("#"+currentNode).removeClass('jstree-open');
	    	$("#"+currentNode).removeClass('jstree-closed');
	    	$("#"+currentNode).addClass('jstree-leaf');
		}
		removeChildNodeCheckBox(data,childNodes);
		$("#organizationTreeRight span").each(function(index, elem){
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+$(this).text());
		});
	}
}

//method to render group child nodes of current node
function renderGroupChildNodes(e, data) {
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var rootNode = data.inst.get_path()[0];
	var nodeLevel = data.inst.get_path().length;
	var selected_nodes = $("#organizationTreeLeft").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#organizationTreeLeft")); 
	var children = tree._get_children(selected_nodes); 
	var children_length = data.inst._get_children(data.rslt.obj).length;
	if ( children_length == 0 ) {
		var childNodes = getGroupchildNodes(currentNodeName,rootNode,nodeLevel);
		if( childNodes != null && childNodes.length > 0 ){
			for(var i=0; i<childNodes.length; i++) {
				$("#organizationTreeLeft").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);
			}
		}else{
			$("#"+currentNode).removeClass('jstree-open');
	    	$("#"+currentNode).removeClass('jstree-closed');
	    	$("#"+currentNode).addClass('jstree-leaf');
		}
		$("#organizationTreeRight span").each(function(index, elem){
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+$(this).text());
		});
	}
}

//method to render department child nodes of current node
function renderDepartmentChildNodes(e, data) {
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var rootNode = data.inst.get_path()[0];
	var nodeLevel = data.inst.get_path().length;
	var selected_nodes = $("#organizationTreeLeft").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#organizationTreeLeft")); 
	var children = tree._get_children(selected_nodes); 
	var children_length = data.inst._get_children(data.rslt.obj).length;
	if ( children_length == 0 ) {
		//$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+currentNode);
		var childNodes = getDepartmentchildNodes(currentNodeName,rootNode,nodeLevel);
		if( childNodes != null && childNodes.length > 0 ){
			for(var i=0; i<childNodes.length; i++) {
				$("#organizationTreeLeft").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);
	        } 
		}else{
			$("#"+currentNode).removeClass('jstree-open');
	    	$("#"+currentNode).removeClass('jstree-closed');
	    	$("#"+currentNode).addClass('jstree-leaf');
		}
		$("#organizationTreeRight span").each(function(index, elem){
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+$(this).text());
		});
	}
}

function isCheckboxCheckedForLabelValue(){
	$("#organizationTreeRight span").each(function(index, elem){
		$.jstree._reference($("#organizationTreeLeft")).check_node('#'+$(this).attr("id"));
	});
}

function isCheckboxChecked(){
	$("#organizationTreeRight span").each(function(index, elem){
		$.jstree._reference($("#organizationTreeLeft")).check_node('#'+$(this).text());
	});
}

//method to render data dictionary child nodes of current node
function renderDataDictionaryChildNodes(e, data) {
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("name");
	var rootNode = data.inst.get_path()[0];
	var nodeLevel = data.inst.get_path().length;
	var selected_nodes = $("#organizationTreeLeft").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#organizationTreeLeft")); 
	var children = tree._get_children(selected_nodes); 
	var children_length = data.inst._get_children(data.rslt.obj).length;
	if ( children_length == 0 ) {
		//$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+currentNode);
		var childNodes = getDataDictionarychildNodes(currentNode,rootNode,nodeLevel);
		if( childNodes != null && childNodes.length > 0 ){
			for(var i=0; i<childNodes.length; i++) {
				$("#organizationTreeLeft").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);
	        } 
		}else{
			$("#"+currentNode).removeClass('jstree-open');
	    	$("#"+currentNode).removeClass('jstree-closed');
	    	$("#"+currentNode).addClass('jstree-leaf');
		}
		$("#organizationTreeRight span").each(function(index, elem){
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+$(this).text());
		});
	}
}

//method to get user child nodes of parent node
function getUserchildNodes(currentNode, rootNode, nodeLevel){ 
	var data = [];
	$.ajax({
		url: 'bpm/admin/getUserSelectionChildNodes',
        type: 'GET',
        data: 'currentNode='+currentNode+'&rootNode='+rootNode+'&nodeLevel='+nodeLevel,
        dataType: 'json',
        async : false,
		success : function(response) {
				data=response;
		},
		error : function(response){
				data=response;
				alert("Problem in getting child nodes");
		}
	});
	return data;
}

//method to get group child nodes of parent node
function getGroupchildNodes(currentNode, rootNode, nodeLevel){ 
	var data = [];
	$.ajax({
		url: 'bpm/admin/getGroupNodes',
        type: 'GET',
        data: 'currentNode='+currentNode,
        dataType: 'json',
        async : false,
		success : function(response) {
				data=response;
		},
		error : function(response){
				data=response;
				alert("Problem in getting child nodes");
		}
	});
	return data;
}

//method to get department child nodes of parent node
function getDepartmentchildNodes(currentNode, rootNode, nodeLevel){ 
	var data = [];
	$.ajax({
		url: 'bpm/admin/getDepartmentNodes',
        type: 'GET',
        data: 'currentNode='+currentNode,
        dataType: 'json',
        async : false,
		success : function(response) {
				data=response;
		},
		error : function(response){
				data=response;
				alert("Problem in getting child nodes");
		}
	});
	return data;
}

//method to get data dictionary child nodes of parent node
function getDataDictionarychildNodes(currentNode, rootNode, nodeLevel){ 
	var data = [];
	$.ajax({
		url: 'bpm/admin/getDictionaryChildNodes',
        type: 'GET',
        data: 'currentNode='+currentNode,
        dataType: 'json',
        async : false,
		success : function(response) {
				data=response;
		},
		error : function(response){
				data=response;
				alert("Problem in getting child nodes");
		}
	});
	return data;
}

//add checked option to right side
function addCheckedOption(e, data, selectionType, selection,validUserNode){
	var currentNode = data.rslt.obj.attr("id");
	if(validUserNode!='${userAdminPrivileges}' && !isValidUserNode(currentNode,validUserNode)){
		return false;
	}
	
	var isUser = data.rslt.obj.attr("isUser");
	//setting display name for the selected item
	var currentNodeName = null;
	if(selection == "dataDictionary"){
		currentNodeName = data.rslt.obj.attr("label");
	}else if(selection == "menu"){
		currentNodeName = data.rslt.obj.attr("nodeName");
		//alert(currentNodeName);
	}else if(selection == "menuQuick"){
		currentNodeName = data.rslt.obj.attr("nodeName");
		
		$("#organizationTreeLeft").jstree("select_node", '#'+currentNode);
		 var aaaaaa = $("#organizationTreeLeft").jstree("get_selected", null, true);
		
		var nodePath = $("#organizationTreeLeft").jstree("get_path", $("#"+aaaaaa.attr("id")), true); 
		var nodeLevel = nodePath.length;
		var rootNode1 = nodePath[1];
		document.getElementById("rootNodeId").value = rootNode1;		
	} 
	else{
		//currentNodeName = data.rslt.obj.attr("name");
		currentNodeName = data.rslt.obj.attr("nodeName");
	}
	if(selection == 'user') {
		if (isUser == true || isUser == 'true') {
			addCheckedOptionToRight(currentNode, currentNodeName, selectionType);
		}
	} else {
		addCheckedOptionToRight(currentNode, currentNodeName, selectionType);
	} 
}

function isValidUserNode(currentNode,validUserNode){
	var isAdminDept=false;
	 if(validUserNode!='[]'){
		 if(validUserNode=='["empty"]'){
			 $('#'+currentNode).find('> a > .jstree-checkbox').remove();
		 }else{
			 var adminData=JSON.parse(validUserNode);
		     	for(var index=0;index<adminData.length;index++){
		     		currentNode=currentNode.replace(/[" "]/g,"");
		     		if(adminData[index].replace(/[" "]/g,"")==currentNode){
		     			isAdminDept=true;
						break;
		     		}
		     	}
		     	
		 		if(!isAdminDept){
				 	$('#'+currentNode).find('> a > .jstree-checkbox').remove();
		 		}
		 }
     		
	 }else{
		 return true;
	 }
     
     return isAdminDept;
}


//add checked option to right side
function addCheckedOptionToRight(currentNode, currentNodeName, selectionType){
	if(selectionType == 'single'){
		$("#organizationTreeRight span").each(function(index, elem){
		    node = this.id;
		    if(currentNode != node){
		    	$("span[id='"+node+"']").remove();
		    	$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+node);
		    }
		});
		appenOptionToRight(currentNode, currentNodeName);
	}else {
		appenOptionToRight(currentNode, currentNodeName);
	}
}

//append the checked option to right side 
function appenOptionToRight(id,text){
	$('#organizationTreeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' name='"+text+"' class='style3 style18 displayBlock pad-T10'><img class='pad-R5' style='width:12px;padding-bottom:3px;' onclick='removeClosedOption(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</span>");
}

//when un_check the check box remove that from right side
function removeUnCheckedOption(e,data){
	removeClosedOption(data.rslt.obj.attr("id"));
}

//when click close icon remove that from right side
function removeClosedOption(nodeId){
	$("span[id='"+nodeId+"']").remove();
	var status = $.jstree._reference($("#organizationTreeLeft")).is_checked('#'+nodeId);
	//for table selection  in listview
	
 	if(status == true){
 		$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+nodeId);
 	}
	
}

//remove root node checkbox while user selection
function removeRootNodeCheckBox(e,data){
	$('#Roles').find('> a > .jstree-checkbox').remove();
	$('#Groups').find('> a > .jstree-checkbox').remove();
	$('#Departments').find('> a > .jstree-checkbox').remove();
}

//remove non user child node checkbox when user selection
function removeChildNodeCheckBox(data,childNodes){
	var currentNode = data.rslt.obj.attr("id");
	var isUser = data.rslt.obj.attr("isUser");
	if(isUser != true && isUser != 'true'){
		$('#'+currentNode).find('> a > .jstree-checkbox').remove();
	}
	for(var i=0; i<childNodes.length; i++){
		if(childNodes[i].attr.isUser != true && childNodes[i].attr.isUser != 'true'){
			$('#'+childNodes[i].attr.id).find('> a > .jstree-checkbox').remove();
		}
	}	
}

//add selecetd option to appentTo when click save button in popup of organization tree 
function addSelectedOption(appendTo, callAfter, selection){
	if (!Array.prototype.indexOf) {
	    Array.prototype.indexOf = function(val) {
	        return jQuery.inArray(val, this);
	    };
	}
	var from;
	var parentId;
	if(callAfter == "selectUsers"){
		from = document.getElementById("from").value;
		parentId = document.getElementById("parentId").value;
	}
	var selectedValues = [];
	var selectedNames = [];
	var selectedUserIDWithFullName = [];
	if(selection == "widget" && $("#organizationTreeRight span").length > 7) {
		validateMessageBox(form.title.info, form.msg.cannotAddMoreThanSevenWidgets ,"info");		
		return false;
	}
	
	if(selection == "widget" && appendTo == "userwidgetNames" && $("#organizationTreeRight span").length == 0) {
		validateMessageBox(form.title.info, form.msg.selectAtleastOneWidget ,"info");
		return false;
	}
	
	$("#organizationTreeRight span").each(function(index, elem){
		if(selection == "user"){
			selectedValues.push($(this).attr("id"));
			selectedNames.push(decodeURI($(this).text()));
			selectedUserIDWithFullName.push($(this).attr("id")+"_"+decodeURI($(this).text()));  // to show full name in document grid
		}else if(selection == "menuQuick"){
			selectedValues.push($(this).attr("id"));
			selectedNames.push($(this).text());
		}else if(selection == "widgetImportFiles" || selection == "selectedListView" || selection == "selectedReport" || selection == "selectedJsp"){
			//alert("selection                "+selection);
			selectedValues.push($(this).attr("id"));
			selectedNames.push($(this).text());
			
		}else{
			selectedValues.push($(this).attr("id"));
			selectedNames.push($(this).text());
		}
		
	});
	
	if(selection == "user"){
		$('#'+appendTo).val(selectedValues);
		if(appendTo=='to' || appendTo=="ccto" || appendTo=="bccto"){
			var fullNameValues = $('#'+appendTo+'FullName').val();
			fullNameValues = fullNameValues.split(',');
			var newFullName =[];
			for (var i=0;i<fullNameValues.length;i++){
				if(fullNameValues[i].indexOf('@')>-1){
					newFullName.push(fullNameValues[i]); 
				}else{
					if(selectedNames.indexOf(fullNameValues[i])>-1){
						newFullName.push(fullNameValues[i]);
					}
				}
			}
			for(var i=0;i<selectedNames.length;i++){
				if(newFullName.indexOf(selectedNames[i])==-1){
					newFullName.push(selectedNames[i]);
				}
			}
			$('#'+appendTo+'FullName').val(newFullName+',');
		}else{
			$('#'+appendTo+'FullName').val(selectedNames);
		}
		$('#nextTaskCoordinatorsName').val(selectedNames);
		$('#users_FullName').val(selectedUserIDWithFullName);

		// show full name for operating functions
		$('#userFullName').val(selectedNames);
	}else if(selection == "department" || selection == "role" || selection == "group") {
		$('#'+appendTo).val(selectedValues);
		$('#'+appendTo+'FullName').val(selectedNames);
	}else if(selection == "menuQuick") {
		$('#quickNavigationUrl').val(selectedNames);
		$('#menuId').val(selectedValues);
	} else if (selection == "process" ) {
		$('#quickNavigationUrl').val(selectedNames);
		$('#processName').val(selectedValues);
	} else if (selection == "selectedListView"){
		$('#quickNavigationUrl').val(selectedNames);		
		$('#'+appendTo).val(selectedNames);
		$('#listView').val(selectedNames);	
	//	$('#listViewName').val(selectedValues);
	}else if(selection == "widgetImportFiles"){
		$('#contentLink').val(selectedNames);
	}/*else if(selection == "selectedListView"){
		$('#'+appendTo).val(selectedNames);
		$('#listView').val(selectedNames);
	}*/else if(selection == "widget"){
		$('#'+appendTo).val(selectedValues);
		$('#widgetNamesHidden').val(selectedValues);
	}else if(selection == "selectedReport"){
		$('#'+appendTo).val(selectedNames);
		$('#report').val(selectedNames);
	} else if(selection == "selectedJsp"){
		//alert(selectedNames+"==="+selectedValues+"======="+appendTo);
		$('#'+appendTo).val(selectedValues);
		$('#sourcejsp').val(selectedValues);
		$('#jspName').val(selectedValues);
		jspFormSubmit();
		//$('#jspFiles').val(selectedNames);
	} else {
		$('#'+appendTo).val(selectedNames);
	}
	closeSelectDialog('userPopupDialog');
	closeSelectDialog('showDataDictionaryTree');
	if(callAfter != null && callAfter != ''){
		if(callAfter == "selectUsers")
		{
			selectUsers(from,parentId,selectedValues);	
		}else{
			window[callAfter](selectedValues);
	}}
	_execute('target','');
	if(appendTo == "noOfWidget"){
		addSelectedLayout(appendTo,selectedValues);
		
	}else if(appendTo == "widgetNames"){
		addSelectedWidgets(selectedValues);
	} else if(appendTo == "userwidgetNames"){
		addSelectedWidgets(selectedValues,"userwidgetNames");
	}
}

var noOfColumn;
var selectedWidgets;
function addSelectedLayout(appendTo,selectedValues){
	setDivHeight("divsample");
	noOfColumn = document.getElementById("noOfWidget").value;
	if(selectedValues == '1'){
		$("#divsample").html("<div class='span12 div-height'   style='border:1px solid gray;'> </div> ");
	}
	else if(selectedValues == '2'){
		$("#divsample").html("<div class='span6 div-height' style='height: 669px; border:1px solid gray;' > </div><div class='span6 div-height' style='height: 669px; border:1px solid gray;'> </div>");
	}else if(selectedValues == '3'){setDivHeight("span4");
		$("#divsample").html("<div class='span4 div-height' style='border:1px solid gray;'> </div><div class='span4 div-height' style='border:1px solid gray;'> </div><div class='span4 div-height' style='border:1px solid gray;'> </div>");
	}
	setspan12Height();
}


function addSelectedWidgets(selectedValues,appendTO){
	//alert("  addSelectedWidgets ++++   "+noOfColumn+"        "+selectedWidgets);
	
	//alert("noOfColumn bfr"+noOfColumn);
	var noOfColumn = 1;
	if(document.getElementById("noOfWidget").value != 0){
		noOfColumn = document.getElementById("noOfWidget").value;
	}
	
//	alert("noOfColumn afr"+noOfColumn);
	var isRender = true;
	var isSave = true ;
	if(appendTO == "userwidgetNames"){
   	var selectedWidgets = document.getElementById("userwidgetNames").value;
   		isSave = false;
   		userHomePagePreview(noOfColumn,selectedWidgets,isRender,isSave);
   	}else{
   		isSave = false;
   		var selectedWidgets = document.getElementById("widgetNames").value;
		homePagePreview(noOfColumn,selectedWidgets,isRender,isSave);
	}
}

function saveUserHomePage(){
	var noOfColumn = 1;
	if(document.getElementById("noOfWidget").value != 0){
		noOfColumn = document.getElementById("noOfWidget").value;
	}
	var selectedWidgets = document.getElementById("userwidgetNames").value;
	var assignedTo = $('#assignedTo').val();
	var departments = $('#departments').val();
	var roles = $('#roles').val();
	
	document.location.href = "#bpm/user/saveUserHomePage";
	_execute('divsample','noOfColumn='+noOfColumn+'&selectedWidgets='+selectedWidgets+'&assignedTo='+assignedTo+'&departments='+departments+'&roles='+roles);
	
	validateMessageBox(form.title.success, form.msg.homePageSavedSuccess ,"success");
}

function saveHomePage(){
	var noOfColumn = 1;
	if(document.getElementById("noOfWidget").value != 0){
		noOfColumn = document.getElementById("noOfWidget").value;
	}
	var selectedWidgets = document.getElementById("widgetNames").value;
	var assignedTo = $('#assignedTo').val();
	var departments = $('#departments').val();
	var roles = $('#roles').val();
	
	if(assignedTo != 'Public' && departments == '' && roles == ''){
		validateMessageBox(form.title.message, form.msg.selectAtleastOneDeptOrRole, false);
		
		return false;
	}
	document.location.href = "#bpm/user/saveHomePage";
	_execute('divsample','noOfColumn='+noOfColumn+'&selectedWidgets='+selectedWidgets+'&assignedTo='+assignedTo+'&departments='+departments+'&roles='+roles);
	validateMessageBox(form.title.success, form.msg.homePageSavedSuccess , "success");
}
 
function addSelectedUsers(callAfter,from){
	var selectedValues = [];
	$("#updateUsersRight span").each(function(index, elem){
		selectedValues.push($(this).text());
	});
	if(callAfter != null && callAfter != ''){
		window[callAfter](selectedValues);
	}
	_execute('target','');
}

//add already selected values to right
function addSelectedValues(selectionType, selectedValues){
	
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){

		if(selectionType == 'single'){
			//alert(selectedValues);
			appenOptionToRight(selectedValues, selectedValues);
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
		}else {
			if (selectedValues.indexOf(",") !=-1) {
				var values = selectedValues.split(",");
				for(var i=0; i< values.length; i++){
					appenOptionToRight(values[i], values[i]);
					$.jstree._reference($("#organizationTreeLeft")).check_node('#'+values[i]);
				}
			} else {
				appenOptionToRight(selectedValues, selectedValues);
				$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
			}
		}
	}
}

function addSelectedValuesAsLabelValue(selectionType, selectedValues){
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			var selectedValue = eval(selectedValues);
			if(selectedValue != "undefined"){
				appenOptionToRight(selectedValue[0]['id'], selectedValue[0]['name']);
				var id = selectedValue[0]['id'];
				$.jstree._reference($("#organizationTreeLeft")).check_node('#'+id);
			}
		}else {
			//alert("selectedValues  ==="+selectedValues);
			var selectedValue = eval(selectedValues);
			if (selectedValue.length > 0) {
				for(var i=0; i < selectedValue.length; i++){
					if(!((selectedValue[i]['id']).indexOf('@') !== -1)){
						appenOptionToRight(selectedValue[i]['id'], selectedValue[i]['name']);
						var id = selectedValue[0]['id'];
						$.jstree._reference($("#organizationTreeLeft")).check_node('#'+id);
					} else {
						appenOptionToRight(selectedValue[i]['id'], selectedValue[i]['name']);
					}
				}
			}
		}
	}
}


function addFilesSelectedValuesAsLabelValue(selectionType, selectedValues){
	//alert();
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			var selectedValue = eval(selectedValues);
		//	alert(selectedValue);
			appenOptionToRight(selectedValues, $('#contentLink').val());
		//	var id = selectedValue[0]['id'];
			
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
		}
	}
}

function addListViewSelectedValuesAsLabelValue(selectionType, selectedValues){
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			//var selectedValue = eval(selectedValues);
		//	alert(selectedValue);
			appenOptionToRight(selectedValues,selectedValues);
		//	var id = selectedValue[0]['id'];
			
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
		}
	}
}

function addReportSelectedValuesAsLabelValue(selectionType, selectedValues){
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			//var selectedValue = eval(selectedValues);
		
			appenOptionToRight(selectedValues,selectedValues);
		//	var id = selectedValue[0]['id'];
			
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
		}
	}
}

function addQuickNavUrlSelectedValuesAsLabelValue(selectionType, selectedValues){
	
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			//var selectedValues = eval(selectedValues);
		
			appenOptionToRight(selectedValues, $('#quickNavigationUrl').val());
			if($('#quickNavigationUrl').val() == '') {				
				$("#organizationTreeRight").empty();
			}
		//	var id = selectedValue[0]['id'];
			
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
		}
	}
}


function addWidgetsSelectedValuesAsLabelValue(selectionType, selectedValues){
	//alert("WId selectedValues *** "+selectedValues);
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'multi'){
			if (selectedValues.indexOf(",") !=-1) {
				var values = selectedValues.split(",");
				for(var i=0; i< values.length; i++){
					//appenOptionToRight(values[i], values[i]);
		//			alert("selectedValues *** "+values[i]);
					$.jstree._reference($("#organizationTreeLeft")).check_node('#'+values[i]);
				}
			} else {
				//appenOptionToRight(selectedValues, selectedValues);
				$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
			}
		}
	}
}

function showUserErrorTab(){
	var from = $('#from').val();
	$('#wizardTab-content-5 label').each(function(){
		  if($(this).hasClass('error')){
			   $('#wizardTab-5').parent().addClass('active-step');
			   $('#wizardTab-5').parent().siblings().removeClass('active-step');
			   $('#wizardTab-5').parent().siblings().addClass('completed-step');
			   $('#wizardTab-content-5').addClass('displayBlock');
			   $('#wizardTab-content-5').removeClass('displayNone');
			   $('#wizardTab-content-5').siblings().removeClass('displayBlock');
			   $('#wizardTab-content-5').siblings().addClass('displayNone');
			   userFormButtonsControl(5);
		  }
	});
	
	$('#wizardTab-content-4 label').each(function(){
		  if($(this).hasClass('error')){
			   $('#wizardTab-4').parent().addClass('active-step');
			   $('#wizardTab-4').parent().siblings().removeClass('active-step');
			   $('#wizardTab-4').parent().siblings().addClass('completed-step');
			   $('#wizardTab-content-4').addClass('displayBlock');
			   $('#wizardTab-content-4').removeClass('displayNone');
			   $('#wizardTab-content-4').siblings().removeClass('displayBlock');
			   $('#wizardTab-content-4').siblings().addClass('displayNone');
			   userFormButtonsControl(4);
		  }
	});
	
	$('#wizardTab-content-3 label').each(function(){
		  if($(this).hasClass('error')){
			   $('#wizardTab-3').parent().addClass('active-step');
			   $('#wizardTab-3').parent().siblings().removeClass('active-step');
			   $('#wizardTab-3').parent().siblings().addClass('completed-step');
			   $('#wizardTab-content-3').addClass('displayBlock');
			   $('#wizardTab-content-3').removeClass('displayNone');
			   $('#wizardTab-content-3').siblings().removeClass('displayBlock');
			   $('#wizardTab-content-3').siblings().addClass('displayNone');
			   if(from == 'profile'){
				   userTabButtonsControl(3);
			   }else {
				   userFormButtonsControl(3);
			   }
			   
		  }
	});
	
	$('#wizardTab-content-2 label').each(function(){
		  if($(this).hasClass('error')){
			   $('#wizardTab-2').parent().addClass('active-step');
			   $('#wizardTab-2').parent().siblings().removeClass('active-step');
			   $('#wizardTab-2').parent().siblings().addClass('completed-step');
			   $('#wizardTab-content-2').addClass('displayBlock');
			   $('#wizardTab-content-2').removeClass('displayNone');
			   $('#wizardTab-content-2').siblings().removeClass('displayBlock');
			   $('#wizardTab-content-2').siblings().addClass('displayNone');
			   userFormButtonsControl(2);
		  }
	});
	
	$('#wizardTab-content-1 label').each(function(){
		  if($(this).hasClass('error')){
			   $('#wizardTab-1').parent().addClass('active-step');
			   $('#wizardTab-1').parent().siblings().removeClass('active-step');
			   $('#wizardTab-1').parent().siblings().addClass('completed-step');
			   $('#wizardTab-content-1').addClass('displayBlock');
			   $('#wizardTab-content-1').removeClass('displayNone');
			   $('#wizardTab-content-1').siblings().removeClass('displayBlock');
			   $('#wizardTab-content-1').siblings().addClass('displayNone');
			   if(from == 'profile'){
				   userTabButtonsControl(1);
			   }else {
				   userFormButtonsControl(1);
			   }
		  }
	});
}

function deleteMenu(){
	var menuId = $('#id').val();
	var isSystemDefined = $('#isSystemDefined').val();
	if(menuId != "" && isSystemDefined == 'false'){
		params = 'menuId='+menuId;
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteMenu,
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	document.location.href = "#bpm/admin/deleteMenu";
		    		_execute('target',params);
		        } else {
					return false;
				}
		    }
		});
	}else{
		if(isSystemDefined == 'true') {
			validateMessageBox(form.title.validation, form.msg.cantDeleteSysDefinedMenu ,"info");
		}else{
		validateMessageBox(form.title.validation, from.msg.selectMenu , "info");
		}
	}
}

//QuartzJob Edit

function _showEditQuartzJob(cellValue, options, rawObject){
	  return '<a id="editquartzjob" href="#bpm/admin/editQuartzJob" data-role="button" data-icon="" data-theme="b" onclick="_execute(\'target\',\'id='+rawObject.id+'\');"><b>'+rawObject.triggerName+'</b></a>';
}

//SysConfig edit
function _showEditSysConfig(cellValue, options, rawObject){
	  return '<a id="editsysconfig" href="#bpm/admin/editSysConfig" data-role="button" data-icon="" data-theme="b" onclick="_execute(\'target\',\'id='+rawObject.id+'\');"><b>'+rawObject.selectKey+'</b></a>';
}

function _showSysconfigValue(cellValue, options, rawObject){
	if(rawObject.selectType == "Password"){
		return '<input type="password"  style="text-align:left;" role="gridcell" readonly="true" value="'+rawObject.selectValue+'"/>';
	} else {
		return rawObject.selectValue;
	}
}

function _showEditSystemLog(cellValue, options, rawObject){
	  return '<a id="editsysconfig" href="#bpm/admin/editSystemLog" data-role="button" data-icon="" data-theme="b" onclick="_execute(\'target\',\'id='+rawObject.id+'\');"><b>'+rawObject.name+'</b></a>';
}

//Sysconfig delete
function deleteSysConfig(){
	var isSystemDefined = false;
	var rowid =  gridObj.getGridParam('selarrrow');
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleEntry, false);
		
	}
	
	var ids=new Array();
	var i = 0;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'id');
		var checkSystemDefined = gridObj.jqGrid('getCell', item, 'systemDefined');
		if(checkSystemDefined == 'true'){
			isSystemDefined = true;
		}else{
			ids[i] = id;
			i++;
		}
	});
	
	
	if(rowid.length !=0 ){	
		
		if(isSystemDefined == true){
			validateMessageBox(form.title.info, form.msg.cantDeleteActiveSysConfig , "info");
		}else{
			deleteAllSysConfigConfirm(ids);
		}
	
	}
	
}

function deleteAllSysConfigConfirm(sysConfigIds){
	params = 'sysConfigIds='+sysConfigIds;
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteSysConfig,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/admin/deleteSysConfig";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}


// Assign users to particular role/group/dept
function showUsers(title, selectionType, callAfter){
	
	var selected_nodes = $("#department_tree").jstree("get_selected", null, true);
	var id = selected_nodes.attr("id");
	var parents = $("#department_tree").jstree("get_path",selected_nodes,true); 
	var parentId="";
	$.each(parents, function(k, v){
		if(k==0){
			parentId = v;
		}
	});

	if((id == 'All') || (id == 'Organization') || (id =="Group") || (id=="Role")){
		validateMessageBox(form.title.message, form.msg.selectValidRoleOrGroup, false);
	}else{

		if( parentId != "All" ){
			document.location.href = "#bpm/admin/showOrganizationTree";
			if(parentId == 'Organization'){
				_execute('userPopupDialog','selectionType='+selectionType+'&appendTo=&selectedValues=&selection=user&rootNodeURL=bpm/admin/getUserRootNodes&childNodeURL=bpm/admin/getUserSelectionChildNodes&callAfter='+callAfter+'&from=department&parentId='+id);
			}else if(parentId == 'Group'){
				_execute('userPopupDialog','selectionType='+selectionType+'&appendTo=&selectedValues=&selection=user&rootNodeURL=bpm/admin/getUserRootNodes&childNodeURL=bpm/admin/getUserSelectionChildNodes&callAfter='+callAfter+'&from=group&parentId='+id);
			}else if(parentId == 'Role'){
				_execute('userPopupDialog','selectionType='+selectionType+'&appendTo=&selectedValues=&selection=user&rootNodeURL=bpm/admin/getUserRootNodes&childNodeURL=bpm/admin/getUserSelectionChildNodes&callAfter='+callAfter+'&from=role&parentId='+id);
			}else{
				_execute('target','selectionType='+selectionType+'&appendTo=&selection=department&rootNodeURL=bpm/admin/getDepartmentNodes&childNodeURL=bpm/admin/getDepartmentChildNodes&selectedValues=&callAfter='+callAfter+'&from=&parentId='+id);
			}
			$myDialog = $("#userPopupDialog");
			
			if(selectionType.toLowerCase() == "single"){
				title = "请选择用户(单选)";
			}
			
			if(selectionType.toLowerCase() == "multi"){
				title = "请选择用户(多选)";
			}
			
			$myDialog.dialog({
            	width: 'auto',
            	height: 'auto',
            	top: '10px',
            	modal: true,
            	position: [($(window).width() / 3), 70],
            	title: title
			});
			$myDialog.dialog("open");
		}else{
			validateMessageBox(form.title.message, form.msg.cannotPullUsersToAllUsers, false);
		}
	}
}

function selectUsers(from,parentId,selectedValues){

	if(selectedValues=='')
		{
		validateMessageBox(form.title.message, form.msg.selectUser, false);
		
		}else{
			var duplicate = false;
			var data = $('#_'+'USER_LIST').jqGrid('getGridParam','data');
		    var selectedUsersArray = selectedValues;
		    if(data.length !=0 ){
		    	data.forEach(function(item){
		    		for(var j=0;j<selectedUsersArray.length;j++){
		    			if(item.id == selectedUsersArray[j]){
		    				duplicate = true;
		    			}
		    		}
		    	});
		    }
		    if(duplicate){
			validateMessageBox(form.title.message, form.msg.someUsersAlreadyAdded, false);
		    	
		    }else{
				$.ajax({
					url: 'bpm/admin/updateUsersDetails',
			        type: 'GET',
			        dataType: 'html',
			        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			        async: false,
			        data: 'from=' +encodeURI(from)+ '&parentId=' +encodeURI(parentId)+ '&selectedValues=' +encodeURI(selectedValues),
					success : function(response) {
						
						if( response.length > 0){
							$("#user-grid").html(response);
							$('#grid_header_links').html($('#header_links').html());
							validateMessageBox(form.title.success, form.msg.successInUpdatingUser, "success");
						}else{
							validateMessageBox(form.title.error, form.msg.errorInUpdatingUser, "error");
						}
						}
				});
			}
		}
	
	}

//method to show organization tree selection in popup for select user
function showOrganizerSelection(title, selectionType, appendTo, selectedValues, callAfter,organizersList,isPotentialOrganizer){
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showOrganizerTree";
	if(appendTo=="to"){
		_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues+'&organizerOrders='+$("#taskOrganizerOrder").val()+'&organizersList='+organizersList+'&rootNodeURL=bpm/admin/getOrganizerRootNodes&childNodeURL=bpm/admin/getOrganizerSelectionChildNodes&callAfter='+callAfter+'&from=&parentId=&isPotentialOrganizer='+isPotentialOrganizer);
	}else{
		_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues.value+'&organizerOrders='+$("#taskOrganizerOrder").val()+'&organizersList='+organizersList+'&rootNodeURL=bpm/admin/getOrganizerRootNodes&childNodeURL=bpm/admin/getOrganizerSelectionChildNodes&callAfter='+callAfter+'&from=&parentId=&isPotentialOrganizer='+isPotentialOrganizer);
	}
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择办理人(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择办理人(多选)";
	}
	
	$myDialog.dialog({
		width: 'auto',
		height: 'auto',
		top: '10px',
		modal: true,
		position: [($(window).width() / 3), 70],
		title: title
     });
	 $myDialog.dialog("open");
}

function addOrganizerSelectedValues(selectionType, selectedValues,organizerOrders,isPotentialOrganizer){
	var orgOrder = [];
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			var selectedValue = eval(selectedValues);
			var organizerOrder = eval(organizerOrders);
			if(selectedValue!="" && selectedValue.length == 1){
				appendOrganizerOptionToRight(selectedValue[0]['id'], selectedValue[0]['name'],organizerOrder[0]['organizerOrder'],organizerOrder[0]['organizerSuperior'],organizerOrder[0]['organizerSubordinate'],isPotentialOrganizer,'single');
				var id = selectedValue[0]['id'];
				$.jstree._reference($("#organizationTreeLeft")).check_node('#'+id);
			}
		}else {
			var selectedValue = eval(selectedValues);
			var organizerOrder = eval(organizerOrders);
			
			if (selectedValue.length > 0) {
				for(var i=0; i < selectedValue.length; i++){
					if(!((selectedValue[i]['id']).indexOf('@') !== -1)){
						appendOrganizerOptionToRight(selectedValue[i]['id'], selectedValue[i]['name'],organizerOrder[i]['organizerOrder'],organizerOrder[i]['organizerSuperior'],organizerOrder[i]['organizerSubordinate'],isPotentialOrganizer,'');
						var id = selectedValue[0]['id'];
						$.jstree._reference($("#organizationTreeLeft")).check_node('#'+id);
					} else {
						appendOrganizerOptionToRight(selectedValue[i]['id'], selectedValue[i]['name'],organizerOrder[i]['organizerOrder'],organizerOrder[i]['organizerSuperior'],organizerOrder[i]['organizerSubordinate'],isPotentialOrganizer,'');
					}
					orgOrder.push(organizerOrder[i]['organizerOrder']);
				}
			}
		}
		var max = 0;
		
		
		if(orgOrder.length > 1){
			max = Math.max.apply(null, orgOrder);
		}else if(orgOrder.length == 1){
			max = orgOrder[0];
		}
		$('#organizationTreeRight').append("<input type='hidden' id='maxOrder' value='"+max+"'/>");
	}
}

//append the checked option to right side 
function appendOrganizerOptionToRight(id,text,order,isSuperior,isSubordinate,isPotentialOrganizer,selectionType){
	if($('#organizationTreeRight:has("#'+id+'")').length == 0){
		$("#maxOrder").val(order);
	}
	if(isPotentialOrganizer == 'true'){
		// to avoid number box for single sign off
		if(selectionType == 'single') {
			$('#organizationTreeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' value='"+order+"' name='"+text+"'><table><tr><td width='650px'><img class='pad-R5' "+
					"onclick='removeClosedOption(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</td></tr></table></span>");			
		} else {
			$('#organizationTreeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' value='"+order+"' name='"+text+"'><table><tr><td width='650px'><img class='pad-R5' "+
			"onclick='removeClosedOption(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</td><td width='50px' align='center'>" +
			"</td></tr></table></span>");
		}
//<input type='text'style='width:40px; display:none' onkeypress='return onlyNumbers(event)' id='order_"+id+"' size='1' value='"+order+"'/>
		/*if(isSuperior && isSuperior == 'true'){
			$('#superior_'+id).prop('checked', true); 
		}
		if(isSubordinate && isSubordinate == 'true'){
			$('#subordinate_'+id).prop('checked', true);
		}*/
	}else {
		if(selectionType == 'single') {
			$('#organizationTreeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' value='"+order+"' name='"+text+"'><table><tr><td width='650px'><img class='pad-R5' "+
					"onclick='removeClosedOption(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</td></tr></table></span>");			
		} else {
			$('#organizationTreeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' value='"+order+"' name='"+text+"' ><table><tr><td widht='650px'><img class='pad-R5' "+
			"onclick='removeClosedOption(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</td><td widht='50px' align='center'>" +
			"<input type='text' style='width:40px;' onkeypress='return onlyNumbers(event)' id='order_"+id+"' size='1' value='"+order+"'/></td></tr></table></span>");	
		}
		
	}
}



//add checked option to right side
function addOrganizerCheckedOption(data,isPotentialOrganizer, selectionType){
	var currentNodeOrder = parseInt($("#maxOrder").val()) + 1;
	var currentNode = data.rslt.obj.attr("id");
	var currentNodeName = data.rslt.obj.attr("nodename");
	if(selectionType == 'single'){
		$("#organizationTreeRight span").each(function(index, elem){
		    node = this.id;
		    if(currentNode != node){
		    	$("span[id='"+node+"']").remove();
		    	$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+node);
		    }
		});
		currentNodeOrder = 1;
	}
	appendOrganizerOptionToRight(currentNode, currentNodeName,currentNodeOrder,false,false,isPotentialOrganizer,'');
}

//add selecetd option to appentTo when click save button in popup of organization tree 
function addOrganizerSelectedOption(appendTo, callAfter, selection,isPotentialOrganizer){
	var selectedValues = [];
	var selectedNames = [];
	var orderValues = [];
	var isEmpty = false;
	$("#organizationTreeRight span").each(function(index, elem){
		if(!isEmpty){
			selectedValues.push($(this).attr("id"));
			selectedNames.push($(this).attr("name"));
			if($("#order_"+$(this).attr("id")).val() != ''){
				if(isPotentialOrganizer == 'true'){
					orderValues.push({"order": $(this).attr("value"),"superior" : $("#superior_"+$(this).attr("id")).is(":checked") ,"subordinate" : $("#subordinate_"+$(this).attr("id")).is(":checked")});
				}else {
					orderValues.push({"order":$(this).attr("value"),"superior" : false ,"subordinate" : false});
				}
			}else {
				validateMessageBox(form.title.message, form.msg.organizationOrderNotEmpty , "info");
				isEmpty = true;
			}
		}
	});
	multiOrganizerCount = 1;
	if(!isEmpty){
		$('#'+appendTo).val(selectedValues);
		$('#nextTaskOrganizersname').val(selectedNames);
		var orderValuesJson = JSON.stringify(orderValues);
		$('#taskOrganizerOrder').val(orderValuesJson);
		closeSelectDialog('userPopupDialog');
		multiOrganizerCount = 1;
		_execute('target','');
	}
}

//Except only numbers for textbox
function onlyNumbers(event) {
    var charCode = (event.which) ? event.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}



function showListViewSelection(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showListViewSelection";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/admin/getListViewRootNodes&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&selection=selectedListView');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择视图列表(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择视图列表(多选)";
	}
	
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}


//for reporet in menu form
function showReportSelection(title, selectionType, appendTo, selectedValues, callAfter){
	
	//clearPreviousPopup();
	document.location.href = "#bpm/report/showReportSelection";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/report/getReportRootNodes&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&selection=selectedReport');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择报告(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择报告(多选)";
	}
	
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}


function showWidgetContentSelection(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showImportFilesSelection";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/admin/getImportFilesRootNodes&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&selection=widgetImportFiles');
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: "Select " +selectionType.charAt(0).toUpperCase() + selectionType.slice(1) + " " + title
	             });
	 $myDialog.dialog("open");
}



//construct a jsTree
function constructModuleJsTree(selector,jsonData,functionName,isNeedToConstructTree){
	var json_data = eval(jsonData);
	$("#"+selector).jstree({
		"crrm" : { 
			"move" : {
				"check_move" : function (m) { 
					var p = this._get_parent(m.o);
					if(!p) return false;
					p = p == -1 ? this.get_container() : p;
					if(p === m.np) return true;
					if(p[0] && m.np[0] && p[0] === m.np[0]) return true;
					return false;
				}
			}
		},
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
        },
        
        "json_data" : {
        	"data" : json_data
        },

        "plugins" : [ "core", "themes", "json_data", "ui" , "crrm" , "dnd"],
        
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
    		var currentNode = data.rslt.obj.attr("id");
    		if(selector == "module_relation_tree"){
    			var level = data.inst.get_path().length;
    			if(level == 3){
            		$("#"+currentNode).removeClass('jstree-open');
                	$("#"+currentNode).removeClass('jstree-closed');
                	$("#"+currentNode).addClass('jstree-leaf');
    			}else if(level == 2){
    				var currentNodeName = data.rslt.obj.attr("name");
    				if(currentNodeName == "Forms"){
    					$("#"+currentNode).removeClass('jstree-open');
                    	$("#"+currentNode).removeClass('jstree-closed');
                    	$("#"+currentNode).addClass('jstree-leaf');
    				}
    			}else if(level==1){
    				allowMovingModuleOrder();
    			}
    		}else{
        		$("#"+currentNode).removeClass('jstree-open');
            	$("#"+currentNode).removeClass('jstree-closed');
            	$("#"+currentNode).addClass('jstree-leaf');
    		}
    	var currentNode = data.rslt.obj.attr("id");
       	//$("li[id~='"+currentNode+"'] > ins.jstree-icon").trigger('click');
    	$('#'+selector).jstree("open_node", "#"+currentNode);
       	window[functionName](e, data);
    });
}


function getUserNameAndPosition(e,isOrganizers,appendTo,currentUser){
	var enterKey = 13;
    if (e.which == enterKey){
    	checkUserInTree(isOrganizers,appendTo);
    }
    if(e.keyCode == 8 || e.keyCode == 229 || !(e.keyCode < 65 || e.keyCode > 90) || !(e.a < 96|| e.keyCode > 105) ){
    	var searchUserName=$("#searchUserName").val();

    	if(searchUserName!=""){
    		var datalist = [];
    		$.ajax({
    			type : 'GET',
    			async : false,
    		    dataType: 'json',
    			url : '/bpm/admin/getUserNameAndPosition?userName='+encodeURI(searchUserName)+'&currentUser='+currentUser+'&appendTo='+appendTo,
    			success : function(data) {
    				
				if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
					document.getElementById("searchValue").value=searchUserName;
					var dataList = $("#searchUserNameWithPosition");
    					dataList.empty();
					if($("#userSelectionWidget") != undefined){
							$("#userSelectionWidget").remove();				
					}
					var selectOption = "<select id='userSelectionWidget' onchange=appendValue(this.value,'user','"+searchUserName+"');selectUserAndPosition('"+isOrganizers+"','"+appendTo+"');><option value=''></option>";
	    				$.each(data, function(index, item) {
	    					selectOption += "<option id='"+item.id+"' value='"+item.userName+"'><font size='5' color='black'>"+item.userName+"</font></option>";
	    				});
					selectOption += '</select>';
	    				dataList.parent().append(selectOption);
				} else {
					var dataList = $("#searchUserNameWithPosition");
	    				dataList.empty();
					$.each(data, function(index, item) {
	    					datalist.push(item.userName);
	    					var selectOption = document.createElement("OPTION");
	    					selectOption.text = item.userName;
	    					if (!($("input[name=searchUserName]").val())) {
	    						$("input[name=searchUserName]").val(item.id);
	    					}
	    					selectOption.value = item.userName;
	    					selectOption.id = item.id;
	    					dataList.append(selectOption);
	    				});
	    				dataList.append('<option value=""></option>');	
				}
    			}
    		});
    		
    		/*$('#searchUserName').autocomplete({
    		    minLength: 1,
    		    source: datalist,
    		    
            	});*/
    	}
    }
	
}

function appendValue(selectedValue,appendTo,searchValue){
	document.getElementById("searchValue").value = searchValue;
	if(appendTo == 'user'){
		document.getElementById("searchUserName").value = selectedValue;
	} else if(appendTo == 'listView'){
		document.getElementById("searchListViewName").value = selectedValue;
	} else if(appendTo == 'process'){
		document.getElementById("searchProcessName").value = selectedValue;
	} else if(appendTo == 'module') {
		document.getElementById("searchModuleDatas").value = selectedValue;
	}
}

function checkUserInTree(isOrganizers,appendTo){
	var userName = $("#searchUserName").attr('value');
	var searchValue = $("#searchValue").attr('value');
	//alert("aaaaa=="+searchValue+"==="+userName);
	if (userName != "") {
		//var userDataList = $('#searchUserNameWithPosition');
		//console.log(userDataList);
		var listVal = '';
		var nodeId = "";
		if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
			listVal = $('#userSelectionWidget').find('option[value="'+userName+'"]');
		} else {
			listVal = $('#searchUserNameWithPosition').find('option[value="'+userName+'"]');
		}
		nodeId = listVal.attr('id');
		//alert("name==="+userName+"==="+nodeId+"======="+isOrganizers+"==="+appendTo);
		if (nodeId != '' && nodeId != undefined) {
			if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
			} else {
				var status = $.jstree._reference($("#organizationTreeLeft")).is_checked('#'+nodeId);
				if(status == false){
			 		$.jstree._reference($("#organizationTreeLeft")).check_node('#'+nodeId);
			 	}
			}
			//alert(isOrganizers+"==="+nodeId+"======="+userName);
			if(isOrganizers == "single"){
				$("#organizationTreeRight span").each(function(index, elem){
				    node = this.id;
				    if(nodeId != node){
				    	$("span[id='"+node+"']").remove();
				    	$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+node);
				    }
				});
				
				appendOrganizerOptionToRight(nodeId, userName,1,false,false,'true','single');
			}else{
				/*var selectionType = $("#ui-dialog-title-userPopupDialog").html();
				if(selectionType == 'Select Single User'){
					$("#organizationTreeRight span").each(function(index, elem){
					    node = this.id;
					    if(nodeId != node){
					    	$("span[id='"+node+"']").remove();
					    	$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+node);
					    }
					});
					
				}else {*/
				//appenOptionToRight(nodeId, userName);
				if(appendTo == "nextTaskOrganizersSelect" ){//|| appendTo == "ccto" || appendTo == "bccto" || appendTo == "agent"){
					var userOrder = parseInt($("#maxOrder").val()) + 1;
					appendMultiOrganizerOptionToRight(nodeId, userName,userOrder);
					multiOrganizerCount++;
					
				}else{
					appenOptionToRight(nodeId, userName);
				}
			}
			
			$("#searchUserName").val(searchValue);
			$("#searchUserName").focus();
		} else {
			$("#searchUserName").val(searchValue);
			$("#searchUserName").focus();
		}
	}
}

/**
 *
 */
function appendMultiOrganizerOptionToRight(id,text,order){
		if($('#organizationTreeRight:has("#'+id+'")').length == 0){
		$("#maxOrder").val(order);
	}
	/*$('#organizationTreeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' value='"+order+"' name='"+text+"' class='style3 style18 displayBlock'><table width='700px'><tr><td width='650px'><img class='pad-R5' "+
				"onclick='removeClosedOption(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</td><td width='50px' align='center'></td></tr></table></span>");*/
		$('#organizationTreeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' value='"+order+"' name='"+text+"' class='style3 style18 displayBlock'><table width='700px'><tr><td widht='650px'><img class='pad-R5' "+
				"onclick='removeClosedOption(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</td><td width='50px' align='right'>" +
				"<input type='text' style='width:40px;' onkeypress='return onlyNumbers(event)' id='order_"+id+"' size='1' value='"+order+"'/></td></tr></table></span>");					
}

function showSelectedLayout(e, data,validAdminDepment,validDepmentRoles){
	var selected_nodes = $("#layoutTree").jstree("get_selected", null, true); 
	var assignedTo = selected_nodes.attr("name");
	var rootNode = selected_nodes.attr("root");
	var hasLayout=false;
	var userRole=$("#currentUserRoles").val().split(",");
    var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
    
	$('#assignedTo').val(assignedTo);
	$.ajax({
		url: 'bpm/admin/getLayoutScript',
        type: 'GET',
        dataType: 'json',
		async: false,
		data: 'assignedTo=' + assignedTo,
		success : function(response) {
			if(response.length > 0){
				for(var i=0; i<response.length; i++){
					$("#divsample").html(response[i].script);
					$('#widgetNames').val(response[i].widgetNames);
					$('#widgetNamesHidden').val(response[i].widgetNames);
					$('#noOfWidget').val(response[i].noOfColumns);
    			}
				hasLayout=true;
			} else {
				if(isAdmin){
					$("#divsample").html("<center><h5>还没有选择部件</h5></center>");
				}
				var height = $("#target").height();
				$("#divsample").css('min-height',parseInt(height) - 67);
				$('#widgetNames').val('');
				$('#widgetNamesHidden').val('');
			}
			
		 	if(!isAdmin){
		    	setDepAdminLayout(rootNode,assignedTo,validAdminDepment,validDepmentRoles,hasLayout);
		    }
			
			if(rootNode == 'Role'){
				$('#roles').val(assignedTo);
				$('#departments').val("");
			} else if(rootNode == 'Department'){
				$('#departments').val(assignedTo);
				$('#roles').val("");
			} else {
				$('#roles').val("");
				$('#departments').val("");
			}
		}
	});
}

function setDepAdminLayout(rootNode,assignedTo,validAdminDepment,validDepmentRoles,hasLayout){
	var validDataList=validAdminDepment;
	if(rootNode=="Department" || rootNode=="Role"){
		if(rootNode=="Role"){
			validDataList=validDepmentRoles;
		}
		
		if(isValidUserNode(assignedTo,validDataList)){
			if(!hasLayout){
				$("#divsample").html("<center><h5>还没有选择部件</h5></center>");
			}
			$("#header_links").show();
			$("#LayoutButtonDiv").show();
			$("#deleteValidationFlag").val(true);
		}else{
			if(!hasLayout){
				$("#divsample").html("<center><h5>还没有选择部件</h5></center>");
			}
			$("#header_links").hide();
			$("#LayoutButtonDiv").hide();
			$("#deleteValidationFlag").val(false);
		}
	}else if(assignedTo=="Public"){
		$("#header_links").hide();
		$("#LayoutButtonDiv").hide();
		$("#deleteValidationFlag").val(false);
	}else if(!hasLayout){
		$("#divsample").html("<center><h5>还没有选择部件</h5></center>");
	}
}

function selectUserAndPosition(isOrganizers,appendTo){
//	alert(isOrganizers+"===aa=="+appendTo);
	setTimeout(function() {
		checkUserInTree(isOrganizers,appendTo);
	}, 500);
}

function selectRadio(value){
	if(value == 1){
		$('#one').prop('checked',true);
	} else if(value == 2){
		$('#two').prop('checked',true);
	} else if(value == 3){
		$('#three').prop('checked',true);
	}
}

//method for adding fullname of user to field when load the page
function setUserFullNames(fullNameField, userIds, JSONUsers){
	var userFullNames = "";
	//for(var key in JSONUsers){ alert('key name: ' + key + ' value: ' + JSONUsers[key]); }
	if(userIds != null && userIds.length > 0 && JSONUsers!= null){
		if(userIds.indexOf(',') != -1){
			var userIdArray = userIds.split(',');
			for(var i=0; i<userIdArray.length;i++){
				if(userFullNames.length == 0){
					if(JSONUsers[userIdArray[i]] != undefined){
						userFullNames = JSONUsers[userIdArray[i]];
					} else {
						userFullNames = userIdArray[i];
					}
				} else {
					if(JSONUsers[userIdArray[i]] != undefined){
						userFullNames = userFullNames +','+ JSONUsers[userIdArray[i]];
					} else {
						userFullNames = userFullNames +','+ userIdArray[i];
					}
				}
			}
		} else {
			if(JSONUsers[userIds] != undefined){
				userFullNames = JSONUsers[userIds];
			} else {
				userFullNames = userIds;
			}
		}
	}
	if(userFullNames.length > 0){
		$('#'+fullNameField).val(userFullNames);
	}
}

//method to show organization tree selection in popup for select super department(only parents)
function showSuperDepartmentSelection(title, selectionType, appendTo, selectedValues, callAfter,isDepartmentAdmin){
	//clearPreviousPopup();
	var rootNodeURL = "bpm/admin/getDepartmentNodes";
	var childNodeURL = "bpm/admin/getSuperDepartmentChildNodes";
	if(isDepartmentAdmin == 'true'){
		rootNodeURL = "bpm/admin/getUserAdministrationSuperDepartmentNodes";
	}
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selection=department&rootNodeURL='+rootNodeURL+'&childNodeURL='+childNodeURL+'&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

//method to show organization tree selection in popup for select user
function showDepartmentUserSelection(title, selectionType, appendTo, selectedValues, callAfter,isDepartmentAdmin){
	var childNodeURL = "bpm/admin/getUserSelectionChildNodes";
	if(isDepartmentAdmin == 'true'){
		childNodeURL = "bpm/admin/getDepartmentAdminUserSelectionChildNodes";
	}
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues.value+'&selection=user&rootNodeURL=bpm/admin/getUserRootNodes&childNodeURL='+childNodeURL+'&callAfter='+callAfter+'&from=&parentId=&currentUser=');
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: "Select " +selectionType.charAt(0).toUpperCase() + selectionType.slice(1) + " " + title
	             });
	 $myDialog.dialog("open");
}

//method to show organization tree selection in popup for select department
function showDepartmentAdminDepartmentSelection(title, selectionType, appendTo, selectedValues, callAfter,isDepartmentAdmin){
	//clearPreviousPopup();
	var rootNodeURL = "bpm/admin/getDepartmentNodes";
	var childNodeURL = "bpm/admin/getDepartmentChildNodes";
	if(isDepartmentAdmin == 'true'){
		rootNodeURL = "bpm/admin/getUserAdministrationDepartmentNodes";
	}
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selection=department&rootNodeURL='+rootNodeURL+'&childNodeURL='+childNodeURL+'&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}
function showProcessSelectionTree(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selection=process&rootNodeURL=bpm/admin/showProcessTreeList&childNodeURL=&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择流程(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择流程(多选)";
	}
	
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

function showProcessSelectionTreeForQuickNav (title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selection=process&rootNodeURL=bpm/admin/showProcessTreeList&childNodeURL=&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择流程(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择流程(多选)";
	}
	
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: title
	             });
	 $myDialog.dialog("open");
}

/*function showListViewSelection(title, selectionType, appendTo, selectedValues, callAfter){
	//clearPreviousPopup();
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selection=listview&rootNodeURL=bpm/admin/getListViewRootNodes&childNodeURL=&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: "Select " +selectionType.charAt(0).toUpperCase() + selectionType.slice(1) + " " + title
	             });
	 $myDialog.dialog("open");
}*/

function getListViewName(e,isOrganizers,appendTo,currentUser){
	var enterKey = 13;
    if (e.which == enterKey){
    	checkListViewInTree(isOrganizers,appendTo);
    }
    //alert("befo=="+isOrganizers+"=="+appendTo);
    if(e.keyCode == 8 || e.keyCode == 229 || !(e.keyCode < 65 || e.keyCode > 90) || !(e.keyCode < 48 || e.keyCode > 57) || !(e.keyCode < 96|| e.keyCode > 105)){
    	var searchListViewName=$("#searchListViewName").val();
    	if(searchListViewName!=""){
    		var datalist = [];
    		$.ajax({
    			type : 'GET',
    			async : false,
    		        dataType: 'json',
    			url : '/bpm/listView/getListViewNames?listViewName='+encodeURI(searchListViewName)+'&appendTo='+appendTo,
    			success : function(data) {
				if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
					document.getElementById("searchValue").value=searchListViewName;
					var dataList = $("#searchListViewNameList");
    					dataList.empty();
					if($("#listviewSelectionWidget") != undefined){
							$("#listviewSelectionWidget").remove();				
					}
					var selectOption = "<select id='listviewSelectionWidget' onchange=appendValue(this.value,'listView','"+searchListViewName+"');selectListViewName('"+isOrganizers+"','"+appendTo+"');><option value=''></option>";
	    				$.each(data, function(index, item) {
	    					selectOption += "<option id='"+item.id+"' value='"+item.listViewName+"'><font size='5' color='black'>"+item.listViewName+"</font></option>";
	    				});
					selectOption += '</select>';
	    				dataList.parent().append(selectOption);
				} else {
	    				var dataList = $("#searchListViewNameList");
	    				dataList.empty();
	    				$.each(data, function(index, item) {
	    					datalist.push(item.listViewName);
	    					var selectOption = document.createElement("OPTION");
	    					selectOption.text = item.listViewName;
	    					if (!($("input[name=searchListViewName]").val())) {
	    							$("input[name=searchListViewName]").val(item.id);
	    					}
	    					selectOption.value = item.listViewName;
	    					selectOption.id = item.id;
	    					dataList.append(selectOption);
	    				});
	    				dataList.append('<option value=""></option>');
				}
    			}
    		});
    		
    		/*$('#searchUserName').autocomplete({
    		    minLength: 1,
    		    source: datalist,
    		    
            	});*/
    	}
    }	
}

function selectListViewName(isOrganizers,appendTo){
	setTimeout(function() {
		checkListViewInTree(isOrganizers,appendTo);
	}, 500);
}

function checkListViewInTree(isOrganizers,appendTo){
	var listViewName=$("#searchListViewName").attr('value');
	var searchValue = $("#searchValue").attr('value');
	
	if (listViewName != "") {
		//var listViewNameList = $('#searchListViewNameList');
		var listVal = '';
		var nodeId = "";
		if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
			listVal = $('#listviewSelectionWidget').find('option[value="'+listViewName+'"]');
		} else {
			listVal = $('#searchListViewNameList').find('option[value="'+listViewName+'"]');
		}
		nodeId = listVal.attr('id');
		//alert("namess==="+listViewName+"==="+nodeId+"======="+isOrganizers+"==="+appendTo);
		if (nodeId !="" && nodeId != undefined) {
			if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
				
			}else{
			var status = $.jstree._reference($("#organizationTreeLeft")).is_checked('#'+nodeId);
			if(status == false){
		 		$.jstree._reference($("#organizationTreeLeft")).check_node('#'+nodeId);
		 	}
			}
			if(isOrganizers == "single"){
				$("#organizationTreeRight span").each(function(index, elem){
				    node = this.id;
				    if(nodeId != node){
				    	$("span[id='"+node+"']").remove();
				    	$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+node);
				    }
				});
				appendOrganizerOptionToRight(nodeId, listViewName,1,false,false,'true','single');
			}else{
					appenOptionToRight(nodeId, listViewName);
			}
			$("#searchListViewName").val(searchValue);
			$("#searchListViewName").focus();
		} else {
			$("#searchListViewName").val(searchValue);
			$("#searchListViewName").focus();
		}
	}
}

function getProcessName(e,isOrganizers,appendTo,currentUser){
	var enterKey = 13;
    if (e.which == enterKey){
    	checkProcessInTree(isOrganizers,appendTo);
    }
    if(e.keyCode == 8 || e.keyCode == 229 || !(e.keyCode < 65 || e.keyCode > 90) || !(e.keyCode < 48 || e.keyCode > 57) || !(e.keyCode < 96|| e.keyCode > 105)){
    	var searchProcessName=$("#searchProcessName").val();
    	if(searchProcessName!=""){
    		var datalist = [];
    		$.ajax({
    			type : 'GET',
    			async : false,
    		        dataType: 'json',
    			url : 'bpm/admin/searchProcessNameList?processName='+encodeURI(searchProcessName)+'&appendTo='+appendTo,
    			success : function(data) {
				if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
					document.getElementById("searchValue").value=searchProcessName;    	
					var dataList = $("#searchProcessNameList");
    					dataList.empty();
					if($("#processSelectionWidget") != undefined){
							$("#processSelectionWidget").remove();				
					}
					var selectOption = "<select id='processSelectionWidget' onchange=appendValue(this.value,'process','"+searchProcessName+"');selectProcessName('"+isOrganizers+"','"+appendTo+"');><option value=''></option>";
	    				$.each(data, function(index, item) {
	    					selectOption += "<option id='"+item.id+"' value='"+item.processName+"'><font size='5' color='black'>"+item.processName+"</font></option>";
	    				});
					selectOption += '</select>';
	    				dataList.parent().append(selectOption);
				} else {
    				var dataList = $("#searchProcessNameList");
    				dataList.empty();
    				$.each(data, function(index, item) {
    					datalist.push(item.processName);
    					var selectOption = document.createElement("OPTION");
    					selectOption.text = item.processName;
    					if (!($("input[name=searchProcessName]").val())) {
    							$("input[name=searchProcessName]").val(item.id);
    					}
    					selectOption.value = item.processName;
    					selectOption.id = item.id;
    					dataList.append(selectOption);
    				});
    				dataList.append('<option value=""></option>');
				}
    			}
    		});
    	}
    }	
}
function getModuleData(e){
	var enterKey = 13;
    if (e.which == enterKey){
    	selectTreeInModule();
    }
    if(e.keyCode == 8 || e.keyCode == 229 || !(e.keyCode < 65 || e.keyCode > 90) || !(e.keyCode < 48 || e.keyCode > 57) || !(e.keyCode < 96|| e.keyCode > 105)){
    	var searchModuleDatas=$("#searchModuleDatas").val();
    	var dataListCount = 0;
        $("#searchModuleDataList").find("option").each(function(index){
        	dataListCount = index;
        });
        //alert(dataListCount+"===="+searchModuleDatas.length);
    	if(searchModuleDatas.length < 1) {
			var dataList = $("#searchModuleDataList");
			dataListCount = 0;
			dataList.empty();
    	}
    	if(searchModuleDatas!="" && dataListCount == 0){
		var searchUrl ="" ;
		var searchType = $("#searchType").val();
		if(searchType == "Table"){
			searchUrl = 'bpm/table/searchModuleTableNames?tableName='+encodeURI(searchModuleDatas);
		}else{
			searchUrl = 'bpm/form/searchFromNames?fromName='+encodeURI(searchModuleDatas);
		}
    		var datalist = [];
    		$.ajax({
    			type : 'GET',
    			async : false,
    		    dataType: 'json',
    			url : searchUrl,
    			success : function(data) {
    					if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
    						var dataList = $("#searchModuleDataList");
    	    					dataList.empty();
    						if($("#moduleSerach") != undefined){
    								$("#moduleSerach").remove();				
    						}
    						var selectOption = "<select id='moduleSerach' onchange=appendValue(this.value,'module');><option value=''></option>";
    		    				$.each(data, function(index, item) {
    		    					selectOption += "<option id='"+item.id+"' value='"+item.dataName+"'><font size='5' color='black'>"+item.dataName+"</font></option>";
    		    				});
    						selectOption += '</select>';
    		    				dataList.parent().append(selectOption);
    					} else {
    	    				var dataList = $("#searchModuleDataList");
    	    				dataList.empty();
    	    				$.each(data, function(index, item) {
    	    					datalist.push(item.dataName);
    	    					var selectOption = document.createElement("OPTION");
    	    					selectOption.text = item.dataName;
    	    					if (!($("input[name=searchModuleDatas]").val())) {
    	    							$("input[name=searchModuleDatas]").val(item.id);
    	    					}
    	    					selectOption.value = item.dataName;
    	    					selectOption.id = item.id;
    	    					dataList.append(selectOption);
    	    				});
    	    				//dataList.append('<option value=""></option>');
    					}
    				

    			}
    		});
    	}
    }	
}
function selectProcessName(isOrganizers,appendTo){
	setTimeout(function() {
		checkProcessInTree(isOrganizers,appendTo);
	}, 500);
}

function checkProcessInTree(isOrganizers,appendTo){
	var processName=$("#searchProcessName").attr('value');
	var searchValue = $("#searchValue").attr('value');
	
	if (processName != "") {
		//var processNameList = $('#searchProcessNameList');
		var listVal = '';
		var nodeId = "";
		if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
			listVal = $('#listviewSelectionWidget').find('option[value="'+listViewName+'"]');
		} else {
			listVal = $('#searchProcessNameList').find('option[value="'+listViewName+'"]');
		}
		nodeId = listVal.attr('id');
		//alert(processName+"===="+nodeId);
		if (nodeId != ''  && nodeId != undefined) {
			if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){}
			else{
			var status = $.jstree._reference($("#organizationTreeLeft")).is_checked('#'+nodeId);
			if(status == false){
		 		$.jstree._reference($("#organizationTreeLeft")).check_node('#'+nodeId);
		 	}
			}
			if(isOrganizers == "single"){
				$("#organizationTreeRight span").each(function(index, elem){
				    node = this.id;
				    if(nodeId != node){
				    	$("span[id='"+node+"']").remove();
				    	$.jstree._reference($("#organizationTreeLeft")).uncheck_node('#'+node);
				    }
				});
				appendOrganizerOptionToRight(nodeId, processName,1,false,false,'true','single');
			}else{
					appenOptionToRight(nodeId, listViewName);
			}
			$("#searchProcessName").val(searchValue);
			$("#searchProcessName").focus();
		} else {
			$("#searchProcessName").val(searchValue);
			$("#searchProcessName").focus();
		}
	}
}



/*function selectTreeModule(){
	setTimeout(function() {
		selectTreeInModule();
	}, 500);
}*/

function selectTreeInModule(){
	var dataName=$("#searchModuleDatas").attr('value');
	if (dataName != "") {
		var dataNameList = $('#searchModuleDataList');
		//alert("selectTreeInModule===dataNameList=="+dataNameList);
		var listVal = $(dataNameList).find('option[value="'+dataName+'"]');
		//alert("selectTreeInModule==listVal==="+listVal.length);
		if (listVal.length != 0) {
			var searchType = $("#searchType").val();
			//console.log(listVal);
			var nodeId=listVal.attr('id');
			
			if(searchType == "From"){
				var nodeIds = nodeId.split("_");
				nodeId = nodeIds[1];
				var formId = nodeIds[0];
				var nodeIdAfterReplace = nodeId.replace('AllForm', '');
				//id="ff80818147a4d1340147a4e1774d003f_ff80818147a4d1340147a4d91e6b0000AllForm"
				document.location.href = "#bpm/form/showFormDesignerForEdit";
				_showFormDesignerForEdit(formId,nodeIdAfterReplace);
			}else{
				$("#module_relation_tree").jstree("deselect_all");		
				$("#module_relation_tree").jstree("select_node", '#'+nodeId);
				$("#searchModuleDatas").val("");
				$("#searchModuleDatas").focus();
			}
			
			//To Check check box in From grid
			/*setTimeout(function() {
				$("#_FORMS_LIST").setGridParam({rowNum:100});
				$("#_FORMS_LIST").trigger('reloadGrid');				
				$("#_FORMS_LIST").jqGrid('setSelection',formId); 
			}, 1200);*/
		} else {
			$("#searchModuleDatas").val("");
			$("#searchModuleDatas").focus();
		}
	}
	//alert("dataName==listVal==="+dataName);
}

function resetSearchDataValue(){
	$("#searchModuleDatas").val("");
	$("#searchModuleDatas").focus();
}


function getTableName(e,isOrganizers,appendTo,currentUser){
	var enterKey = 13;
    if (e.which == enterKey){
    	checkTableInTree(isOrganizers,appendTo);
    }
    if(e.keyCode == 8 || e.keyCode == 229 || !(e.keyCode < 65 || e.keyCode > 90) || !(e.keyCode < 48 || e.keyCode > 57) || !(e.keyCode < 96|| e.keyCode > 105)){
    	var searchTableName=$("#searchTableName").val();
			
    	if(searchTableName!=""){
    		var datalist = [];
    		$.ajax({
    			type : 'GET',
    			async : false,
    		        dataType: 'json',
    			url : '/bpm/table/getTableNames?tableName='+searchTableName+'&appendTo='+appendTo,
    			success : function(data) {
    				var dataList = $("#searchTableNameList");
    				dataList.empty();
    				$.each(data, function(index, item) {
    					datalist.push(item.tableName);
    					var selectOption = document.createElement("OPTION");
    					selectOption.text = item.tableName;
    					if (!($("input[name=searchTableName]").val())) {
    							$("input[name=searchTableName]").val(item.id);
    					}
    					selectOption.value = item.tableName;
    					selectOption.id = item.id;
    					dataList.append(selectOption);
    				});
    				dataList.append('<option value=""></option>');
    			}
    		});    		
    	}
    }	
}

function selectTableName(isOrganizers,appendTo){	
	//$("#treeLeft").jstree('open_all');
	setTimeout(function() {
		checkTableInTree(isOrganizers,appendTo);
	}, 500);
}

function checkTableInTree(isOrganizers,appendTo){
		
	var tableName=$("#searchTableName").attr('value');
	
	if (tableName != "") {
		var tableNameList = $('#searchTableNameList');
		var listVal = $(tableNameList).find('option[value="'+tableName+'"]');
		
		if (listVal.length != 0) {
			var nodeId=listVal.attr('id');
			nodeId = nodeId.replace(/\s/g, '');			 
			var status = $.jstree._reference($("#treeLeft")).is_checked('#'+nodeId);				
			if(status == false){
		 		$.jstree._reference($("#treeLeft")).check_node('#'+nodeId);
		 	}
			
			if(isOrganizers != "single"){		
				appendMultiTableToRight(nodeId, tableName);				
			}
			$("#searchTableName").val("");
			$("#searchTableName").focus();
		} else {
			$("#searchTableName").val("");
			$("#searchTableName").focus();
		}
	}
}

function appendMultiTableToRight(id,text){		
	$('#treeRight:not(:has("#'+id+'"))').append("<span id='"+id+"' name='"+text+"' class='style3 style18 displayBlock'><table width='700px'><tr><td width='650px'><img class='pad-R5' "+
				"onclick='removeClosedOptionForTable(\""+id+"\");' src='/images/easybpm/form/close_btn.jpg'/>"+text+"</td><td width='50px' align='center'></td></tr></table></span>");
				
}


//when click close icon remove that from right side
function removeClosedOptionForTable(nodeId){
	$("span[id='"+nodeId+"']").remove();
	
	//for table selection  in listview
	var showTableTreeStatus = $.jstree._reference($("#treeLeft")).is_checked('#'+nodeId);
 	
	if(showTableTreeStatus == true) {
		$.jstree._reference($("#treeLeft")).uncheck_node('#'+nodeId);
	}
}
function addJspFromSoruce(selectionType, selectedValues){
	if(selectedValues.length >0 && selectedValues != null && selectedValues != ''){
		if(selectionType == 'single'){
			//var selectedValue = eval(selectedValues);
		
			appenOptionToRight(selectedValues,selectedValues);
		//	var id = selectedValue[0]['id'];
			
			$.jstree._reference($("#organizationTreeLeft")).check_node('#'+selectedValues);
		}
	}
}

function showJspFileSelection(title, selectionType, appendTo, selectedValues, callAfter){
	document.location.href = "#bpm/form/showJspSelection";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/form/getAllJspFromSource='+selectedValues.value+'&callAfter='+callAfter+'&selection=selectedJsp');
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 70],
                       title: "Select " +selectionType.charAt(0).toUpperCase() + selectionType.slice(1) + " " + title
	             });
	 $myDialog.dialog("open");
}