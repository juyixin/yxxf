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
			<div id="group_tree_left" class="tree-left" style="background: none !important;"></div>
		</td>
		<td style="vertical-align:top;">
			<div id="group_tree_right" class="tree-right mar-L0"></div>
		</td>
	</tr>
</table>

<script>
$(window).resize(function() {
    $("#groupPopupDialog").dialog("option", "position", "center");
});

$(document).ready(function(){
    $("#groupPopupDialog").dialog("option", "position", "center");
});

var selectedVal="";
var selectedArray=new Array();

//the javascript written here because of using jstl tag for get group list
//construct json data for jsTree
function getGroupTreeData(){
	var parentGroupArray = new Array();
	var childGroupArray = new Array();
	var i=0;
	var j=0;
	var groupData="";
	var childGroupData=""; 
	 <c:forEach items="${groupList}" var="parentGroup" varStatus="status">
		if("${parentGroup.isParent}" == "true"){
			groupData = '{"data":"${parentGroup.name}","attr":{id : "${parentGroup.id}"},"metadata": {id : "${parentGroup.id}", "name" : "${parentGroup.name}"}';
	        <c:forEach items="${groupList}" var="childGroup" varStatus="status">
	       		childGroupData = ', "children" : [';
	       		if("${childGroup.superGroup}" == "${parentGroup.id}"){
		       		childGroupArray[j] = '{"data":"${childGroup.name}","attr":{id : "${childGroup.id}"},"metadata": {id : "${childGroup.id}", "name" : "${childGroup.name}"}}';
	       			j++;
	       		}
	       	</c:forEach>;
	       	childGroupData=childGroupData+childGroupArray;
	       	parentGroupArray[i] = groupData+childGroupData+']}';
	       	childGroupArray=new Array();
	       	i++;
		}
	</c:forEach>;
	var jsonData = "["+parentGroupArray+"]";
	groupData = "";
	return jsonData;
}

function getGroupList(e,data){
	document.getElementById("group_tree_right").innerHTML = "";
	var groupId = data.rslt.obj.data("id");
	$.ajax({
		url: 'bpm/admin/getGroupsByLabelValue',
        type: 'GET',
        dataType: 'json',
		async: false,
		data: 'id=' + groupId ,
		success : function(response) {
			var groupData = ""; 
			if(response.length > 0){
				groupData = "<div class='padding5 overflow height-per-76' id='groupList'>";
    			for(var i=0; i<response.length; i++){
    				var groupName = response[i].groupName;
    				var groupId = response[i].groupId;
    				if("${selectType}" == 'single'){
    					groupData += "<input type='radio' onclick='loadUserGroups();' name='groupDetail' id='"+groupId+"' value='"+groupId+"'><span class='pad-L5'>"+groupName+"</span></input><br>"
    				}else{
    					groupData += "<input type='checkbox' onclick='loadUserGroups();' name='groupDetail' id='"+groupId+"' value='"+groupId+"'><span class='pad-L5'>"+groupName+"</span></input><br>"
    				}
    			}
				groupData += '</div>';
				groupData += '<div class="popup-button" id="saveButtonDiv">'+
				 '<button type="submit" class="btn btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="saveGroupsValue();">'+
	        	 'Select' +
	    		 '</button></div>';
				} 
			document.getElementById("group_tree_right").innerHTML = groupData;
			} 
	});
}

function loadUserGroups(){
	if("${selectType}" == 'single'){
		selected = $("#groupList input[type='radio'][name='groupDetail']:checked");
		if (selected.length > 0){
			//document.getElementById(requestorId).value = selected.val();
			selectedArray.push(selected.val());
		}
	}else{
		$("#groupList input[type='checkbox'][name='groupDetail']:checked").each(function(){
			var $this = $(this);      
			var id=$this.attr('id');
			var value = $this.attr('value');
			//selectedVal += '<option value='+id+' selected>'+value+'</option>';
			selectedArray.push(value);
		});
	}
	
}

function saveGroupsValue(){
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
	closeSelectDialog("groupPopupDialog");
	
}

function constructGroupJsTree(selector,jsonData,functionName){
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

var jsonData = getGroupTreeData();
constructGroupJsTree("group_tree_left",jsonData,"getGroupList");

</script>