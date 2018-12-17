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
			<div id="partTimeDepartment_tree_left" class="tree-left" style="background: none !important;"></div>
		</td>
		<td style="vertical-align:top;">
			<div id="partTimeDepartment_tree_right" class="tree-right mar-L0"></div>
		</td>
	</tr>
</table>

<script>
//the javascript written here because of using jstl tag for get partTimeDepartment list
//construct json data for jsTree
function getPartTimeDepartmentTreeData(){
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

var jsonData = getPartTimeDepartmentTreeData();

constructJsTree("partTimeDepartment_tree_left",jsonData,"getPartTimeDepartmentList",true);

</script>