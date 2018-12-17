<script>
getNewsList();
function getNewsList(){
	$.ajax({
		type : 'GET',
		async : false,
		url : 'bpm/news/getNewsList',
		success : function(response) {
			var newsList = eval(response);
			var htmlContent = "";
			if(newsList.length > 0){
				
				for(var idx = 0; idx < newsList.length; idx++){
					htmlContent = htmlContent + '<div style="padding:5px 10px 10px 10px; background-color:#f9f9f9;">';
					htmlContent = htmlContent + '<div class="style15">'+newsList[idx].Title+'</div>';
					htmlContent = htmlContent + '<div class="style16 style17" style="padding-top:8px; padding-bottom:8px; border-bottom:solid #CCCCCC 1px; ">';	
					htmlContent = htmlContent + newsList[idx].CREATEDTIME;
					htmlContent = htmlContent + '</div>';	
					htmlContent = htmlContent + '</div>';
					if(idx == 2){
						break;
					}	
				}
			$("#newsList_container").html(htmlContent);
			}
		}
	});
}
</script>
<div id="newsList_container"></div>
</div>
<div class="small small style3 readmore-home">
<a href="javascript:void(0);" onClick="showListViews('NEWS_LIST','News');">更多</a>
</div>
									
							
