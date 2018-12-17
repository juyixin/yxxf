<!doctype html>
<%@ include file="/common/taglibs.jsp"%>

<table class="tree-selector-table treeBoxShadow" style="background-image: url('/styles/easybpm/images/palette_line.jpg');vertical-align:top;">
	<tr>
		<td style="vertical-align:top;">
			<div id="selector_left" class="tree-left" style="background: none !important;"></div>
		</td>
		<td style="vertical-align:top;">
			<div id="selector_right" class="tree-right mar-L0"></div>
		</td>
	</tr>
</table>

<script>
$(window).resize(function() {
    $("#userPopupDialog").dialog("option", "position", "center");
});

$(document).ready(function(){
    $("#userPopupDialog").dialog("option", "position", "center");
});

//construct a jsTree
var selectedVal="";
var selectedArray=new Array();
function getTreeData(selector){
	var topMenuArray = new Array();
	var menuDetails = new Array();
	var rootDepartmentArray = new Array();
	var parentDepartmentArray = new Array();
	var childDepartmentArray = new Array();
	var i=0;
	var k=0;
	var l=0;
	var m =0;
	var n=0;
	var menuData; 
	var parentMenuData;
	var childMenuData;
	var parentMenuItems = new Array();
	var departmentData="";
	var parentDepartmentData=""; 
	var childDepartmentData="";
	parentMenuItems = ['Roles','Groups','Departments'];
	if(selector == "selector_left"){
		for (var j = 0; j < parentMenuItems.length; j++) {
			if(parentMenuItems[j] == "Roles"){
				parentMenuData = '{ "data" : "Roles",  "attr" : { id : "roles", "name" : "roles" }, "children" : [';
				   <c:forEach items="${roles}" var="role" varStatus="status">
				    	menuData =  '{"data":"${role}","metadata": {id : "${role.id}", "name" : "${role.name}"}}';
				       	topMenuArray[i] = menuData;
				       	i++;
				   </c:forEach>; 
				   menuDetails[j] = parentMenuData+topMenuArray+']}';
			}
			if(parentMenuItems[j] == "Groups"){
				parentMenuData = '{ "data" : "Groups",  "attr" : { id : "groups", "name" : "groups" }, "children" : [';
				   <c:forEach items="${groups}" var="group" varStatus="status">
				    	menuData =  '{"data":"${group}","metadata": {id : "${group.id}", "name" : "${group.name}"}}';
				       	topMenuArray[k] = menuData;
				        k++;
				   </c:forEach>;
				   menuDetails[j] = parentMenuData+topMenuArray+']}';
			}
			if(parentMenuItems[j] == "Departments"){
				parentMenuData = '{ "data" : "Departments",  "attr" : { id : "departments", "name" : "departments" }, "children" : [';
				   <c:forEach items="${departments}" var="rootDepartment" varStatus="status">
				    	/* menuData =  '{"data":"${department}","metadata": {id : "${department.id}", "name" : "${department.name}"}}';
				       	topMenuArray[n] = menuData;
				        n++; */
					   if("${rootDepartment.isParent}" == "true" && "${rootDepartment.departmentType}" == "root"){
							departmentData = '{"data":"${rootDepartment.name}","attr":{id : "departments"},"metadata": {id : "${rootDepartment.id}", "name" : "${rootDepartment.name}"}';
					        <c:forEach items="${departments}" var="parentDepartment" varStatus="status">
					       		parentDepartmentData = ', "children" : [';
					       		if("${parentDepartment.superDepartment}" == "${rootDepartment.id}"){
					       			<c:forEach items="${departments}" var="childDepartment" varStatus="status">
					       				childDepartmentData = ', "children" : [';
						       			if("${childDepartment.isParent}" == "false" && "${childDepartment.departmentType}" != "root" && "${childDepartment.superDepartment}" == "${parentDepartment.id}"){
						       				childDepartmentArray[n] = '{"data":"${childDepartment.name}","attr":{id : "${childDepartment.id}"},"metadata": {id : "${childDepartment.id}", "name" : "${childDepartment.name}"}}';
						       				n++;
						       			}
						       		</c:forEach>;
						       		parentDepartmentArray[m] = '{"data":"${parentDepartment.name}","attr":{id : "departments"},"metadata": {id : "${parentDepartment.id}", "name" : "${parentDepartment.name}"}'+childDepartmentData+childDepartmentArray+']}';
						       		childDepartmentArray = new Array();
					       			m++;
					       		}
					       	</c:forEach>;
					       	parentDepartmentData=parentDepartmentData+parentDepartmentArray;
					       	rootDepartmentArray[l] = departmentData+parentDepartmentData+']}';
					       	parentDepartmentArray=new Array();
					       	l++;
						}
				   </c:forEach>;
				   menuDetails[j] = parentMenuData+rootDepartmentArray+']}';
			}
			parentMenuData = "";
			topMenuArray=new Array();
		}
	}
	var jsonData = "["+menuDetails+"]";
	menuData = "";
	return jsonData;
}

constructTree('selector_left');
function constructTree(selector){
	var	jsonData;
	if (selector == 'selector_left'){
		jsonData = getTreeData(selector);
	} 
	json_data = eval(jsonData);
	$("#"+selector).jstree({
        "json_data" : {
            "data" :  json_data
        },

        "plugins" : [ "core", "themes", "json_data", "ui" , "dnd" ],
        
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
    	constructUsersRightTree(e,data);
    });
}

function constructUsersRightTree(e,data){
	document.getElementById("selector_right").innerHTML = "";
	var parentName = data.inst._get_parent(data.rslt.obj).attr("id");
	var role = data.rslt.obj.data("id");
	$.ajax({
		url: 'admin/getRoleUsers',
        type: 'GET',
        dataType: 'json',
		async: false,
		data: 'id=' + role + "&parentNode=" + parentName,
		success : function(response) {
			var topMenuArray = new Array();
			var i=0;
			var menuData = ""; 
			var childMenuData;
			if(response.length > 0){
				menuData = "<div class='padding5 overflow height-per-76' id='userList'>"
    			for(var i=0; i<response.length; i++){
    				var userName = response[i].userName;
    				var userId = response[i].userId;
    				if("${selectType}" == 'single'){
    		       		menuData += "<input type='radio' onclick='loadManager();' name='userDetail' id='"+userId+"' value='"+userId+"'><span class='pad-L5'>"+userName+"</span></input><br>"
    				}else{
    					menuData += "<input type='checkbox' onclick='loadManager();' name='userDetail' id='"+userId+"' value='"+userId+"'><span class='pad-L5'>"+userName+"</span></input><br>"
    				}
    			}
				menuData += '</div>'
				menuData += '<div class="popup-button id="saveButtonDiv">'+
				 '<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="saveUsersValue();">'+
	        	 'Select' +
	    		 '</button></div>'
				} 
			document.getElementById("selector_right").innerHTML = menuData;
			} 
	});
}

function loadManager(){
	if("${selectType}" == 'single'){
		selected = $("#userList input[type='radio'][name='userDetail']:checked");
		if (selected.length > 0){
			//document.getElementById(requestorId).value = selected.val();
			selectedArray.push(selected.val());
		}
	}else{
		$("#userList input[type='checkbox'][name='userDetail']:checked").each(function(){
			var $this = $(this);      
			var id=$this.attr('id');
			var value = $this.attr('value');
			//selectedVal += '<option value='+id+' selected>'+value+'</option>';
			selectedArray.push(value);
		});
	}
	
}

function saveUsersValue(){
	var requestorId = "${requestorId}";
	if("${selectType}" == 'single'){
		for(var i=0; i<selectedArray.length;i++){
			selectedVal=selectedArray[i];
		}
		document.getElementById(requestorId).value = selectedVal;
		selectedVal="";
		selectedArray=[];
	}else{
		
		var selectArray = [];
		$.each(selectedArray, function(i, el){
		    if($.inArray(el, selectArray) === -1) selectArray.push(el);
		});
		for(var i=0;i<selectArray.length;i++){
			selectedVal += '<option value='+selectArray[i]+' selected>'+selectArray[i]+'</option>';
		}
		selectArray=[];
		document.getElementById(requestorId).innerHTML = selectedVal;
	}
	
	selectedVal="";
	selectedArray=[];

	closeSelectDialog("userPopupDialog");
	
}

</script>