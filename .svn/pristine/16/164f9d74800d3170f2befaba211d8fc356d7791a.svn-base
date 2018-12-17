<script>
getDoToList();
function getDoToList(){
	$.ajax({
		type : 'GET',
		async : false,
		url : 'bpm/todo/getToDOList',
		success : function(response) {
			var todoList = eval(response);
			var htmlContent = "";
			if(todoList.length > 0){
				for(var idx = 0; idx < todoList.length; idx++){
					htmlContent = htmlContent + '<div style="padding:5px 10px 10px 10px; background-color:#f9f9f9;">';
					htmlContent = htmlContent + '<div class="style15">'+todoList[idx].name+' - '+
					'<a href="javascript:void(0);" onclick="showTaskFormDetail(\''+todoList[idx].id + '\',\''+ todoList[idx].suspensionState + '\',\''+ todoList[idx].lastOperationPerformed + '\',\''+ todoList[idx].executionId + '\',\''
					+ todoList[idx].resourceName + '\',\''+ todoList[idx].processDefinitionId + '\',\''+ todoList[idx].owner + '\');"><u>'+todoList[idx].processName+'</u></a></div>';
					htmlContent = htmlContent + '<div class="style16 style17" style="padding-top:5px; padding-bottom:8px; border-bottom:solid #CCCCCC 1px; ">';	
					htmlContent = htmlContent + todoList[idx].createTime;
		
					htmlContent = htmlContent + '</div>';	
					htmlContent = htmlContent + '</div>';	
					if(idx == 3){
						break;
					}
				}
			$("#todlist_container").html(htmlContent);
			}
		}
	});
}
</script>
<div id="todlist_container"></div>
</div>

<div class="small small style3 readmore-home">
<a href="#bpm/manageTasks/mybucket" onClick="_execute('target','');">更多</a>
</div>
								
