<script>
getScheduleList();
function getScheduleList(){
	$.ajax({
		type : 'GET',
		async : false,
		url : 'bpm/oa/getScheduleList',
		success : function(response) {
			var scheduleList = eval(response);
			var a = scheduleList.length;
			var htmlContent = "";
			if(scheduleList.length > 0){
				for(var idx = 0; idx < scheduleList.length; idx++){
				
					htmlContent = htmlContent + '<div style="padding:5px 10px 10px 10px; background-color:#f9f9f9;">';
					htmlContent = htmlContent + '<div class="style15">'+scheduleList[idx].name+'</div>';
					htmlContent = htmlContent + '<div style="padding-top:5px;font-weight: normal;">'+scheduleList[idx].description+'</div>';
					htmlContent = htmlContent + '<div style="padding-top:5px;font-weight: normal;">'+scheduleList[idx].location+'</div>';
					htmlContent = htmlContent + '<div class="style16 style17" style="padding-top:10px; padding-bottom:8px; border-bottom:solid #CCCCCC 1px; ">';	
					htmlContent = htmlContent + '开始时间: '+scheduleList[idx].startdate+'&nbsp;&nbsp;&nbsp;&nbsp;结束时间: '+scheduleList[idx].endDate;
					htmlContent = htmlContent + '</div></div>';
					if(idx == 2){
						break;
					}	
				}
			$("#scheduleList_container").html(htmlContent);
			}
		}
	});
}
</script>
<div id="scheduleList_container"></div>
</div>

<div class="small small style3 readmore-home">
<a href="#bpm/oa/schedules" onClick="_execute('target','');">更多</a>

</div>
							 	
