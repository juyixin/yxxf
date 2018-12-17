<%@ include file="/common/taglibs.jsp"%>
<style>
.boxshadow{
/* -moz-box-shadow:inset 0px 0px 7px 0px rgba(0, 0, 0, 0.97);
-webkit-box-shadow:inset 0px 0px 7px 0px rgba(0, 0, 0, 0.97);
box-shadow:inset 0px 0px 7px 0px rgba(0, 0, 0, 0.97);
 */
-moz-box-shadow:inset 1px 1px 1px 0px #000000;
-webkit-box-shadow:inset 1px 1px 1px 0px #000000;
box-shadow:inset 1px 1px 1px 0px #000000;

}

.tree-left{
		border-right:1px solid #CCCCCC;
		border-bottom:1px solid #CCCCCC;
		width:250px;
		height:300px;
		overflow-x:hidden;
		overflow-y:auto;
}

.tree-right{
		margin-left:10px;
		border-right:1px solid #CCCCCC;
		border-bottom:1px solid #CCCCCC;
		width:250px;
		height:300px;
		overflow-x:hidden;
		overflow-y:auto;
}

.tree-selector-table{
	margin-top:10px;
}

</style>
<table class="tree-selector-table boxshadow" style="background-image: url('/styles/easybpm/images/palette_line.jpg');vertical-align:top;">
	<tr>
		<td style="vertical-align:top;">
			<div id="department_tree_left" class="tree-left" style="background: none !important;"></div>
		</td>
		<td style="vertical-align:top;">
			<div id="department_tree_right" class="tree-right mar-L0"></div>
		</td>
	</tr>
</table>

<script>

$(window).resize(function() {
    $("#departmentPopupDialog").dialog("option", "position", "center");
});

$(document).ready(function(){
    $("#departmentPopupDialog").dialog("option", "position", "center");
});

var selectedVal="";
var selectedArray=new Array();

//the javascript written here because of using jstl tag for get department list
//construct json data for jsTree
function getDepartmentTreeData(){
	var rootDepartmentArray = new Array();
	var parentDepartmentArray = new Array();
	var childDepartmentArray = new Array();
	var i=0;
	var j=0;
	var k=0;
	var departmentData="";
	var parentDepartmentData=""; 
	var childDepartmentData="";
	 <c:forEach items="${departmentList}" var="rootDepartment" varStatus="status">
		if("${rootDepartment.isParent}" == "true" && "${rootDepartment.departmentType}" == "root"){
			departmentData = '{"data":"${rootDepartment.name}","attr":{id : "${rootDepartment.id}"},"metadata": {id : "${rootDepartment.id}", "name" : "${rootDepartment.name}"}';
	        <c:forEach items="${departmentList}" var="parentDepartment" varStatus="status">
	       		parentDepartmentData = ', "children" : [';
	       		if("${parentDepartment.superDepartment}" == "${rootDepartment.id}"){
	       			<c:forEach items="${departmentList}" var="childDepartment" varStatus="status">
	       				childDepartmentData = ', "children" : [';
		       			if("${childDepartment.isParent}" == "false" && "${childDepartment.departmentType}" != "root" && "${childDepartment.superDepartment}" == "${parentDepartment.id}"){
		       				childDepartmentArray[k] = '{"data":"${childDepartment.name}","attr":{id : "${childDepartment.id}"},"metadata": {id : "${childDepartment.id}", "name" : "${childDepartment.name}"}}';
		       				k++;
		       			}
		       		</c:forEach>;			       		
		       		parentDepartmentArray[j] = '{"data":"${parentDepartment.name}","attr":{id : "${parentDepartment.id}"},"metadata": {id : "${parentDepartment.id}", "name" : "${parentDepartment.name}"}'+childDepartmentData+childDepartmentArray+']}';
		       		childDepartmentArray = new Array();
	       			j++;
	       		}
	       	</c:forEach>;
	       	parentDepartmentData=parentDepartmentData+parentDepartmentArray;
	       	rootDepartmentArray[i] = departmentData+parentDepartmentData+']}';
	       	parentDepartmentArray=new Array();
	       	i++;
		}
	</c:forEach>;
	var jsonData = "["+rootDepartmentArray+"]";
	departmentData = "";
	return jsonData;
}

function getDepartmentList(e,data){
	document.getElementById("department_tree_right").innerHTML = "";
	var departmentId = data.rslt.obj.data("id");
	$.ajax({
		url: 'bpm/admin/getDepartmentsByLabelValue',
        type: 'GET',
        dataType: 'json',
		async: false,
		data: 'id=' + departmentId ,
		success : function(response) {
			var departmentData = ""; 
			if(response.length > 0){
				departmentData = "<div class='padding5 overflow height-per-76' id='departmentList'>";
    			for(var i=0; i<response.length; i++){
    				var departmentName = response[i].departmentName;
    				var departmentId = response[i].departmentId;
    				if("${selectType}" == 'single'){
    					departmentData += "<input type='radio' onclick='loadUserDepartment();' name='departmentDetail' id='"+departmentId+"' value='"+departmentId+"'><span class='pad-L5'>"+departmentName+"</span></input><br>"
    				}else{
    					departmentData += "<input type='checkbox' onclick='loadUserDepartment();' name='departmentDetail' id='"+departmentId+"' value='"+departmentId+"'><span class='pad-L5'>"+departmentName+"</span></input><br>"
    				}
    			}
				departmentData += '</div>';
				departmentData += '<div class="popup-button" id="saveButtonDiv">'+
				 '<button type="submit" class="btn btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="saveDepartmentsValue();">'+
	        	 'Select' +
	    		 '</button></div>';
				} 
			document.getElementById("department_tree_right").innerHTML = departmentData;
			} 
	});
}

function loadUserDepartment(){
	if("${selectType}" == 'single'){
		selected = $("#departmentList input[type='radio'][name='departmentDetail']:checked");
		if (selected.length > 0){
			//document.getElementById(requestorId).value = selected.val();
			selectedArray.push(selected.val());
		}
	}else{
		$("#departmentList input[type='checkbox'][name='departmentDetail']:checked").each(function(){
			var $this = $(this);      
			var id=$this.attr('id');
			var value = $this.attr('value');
			//selectedVal += '<option value='+id+' selected>'+value+'</option>';
			selectedArray.push(value);
		});
	}
}

function saveDepartmentsValue(){
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
	closeSelectDialog("departmentPopupDialog");
	
}
var jsonData = getDepartmentTreeData();
constructDepartmentJsTree("department_tree_left",jsonData,"getDepartmentList");

function constructDepartmentJsTree(selector,jsonData,functionName){
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
    	window[functionName](e, data);
    });
}
</script>