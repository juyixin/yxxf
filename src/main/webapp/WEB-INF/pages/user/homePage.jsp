<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%@ include file="/common/messages.jsp" %>
<div class="row-fluid">
	<div class="span12">
		<c:if test="${result != 'null'}">	
			<%= request.getAttribute("result") %>   
		</c:if>
	</div>
</div>
<!--  <div id="divHomePage" class="span12"></div>
<div id="divManageLayout" class = "span12"></div>
<div id="div1" ></div>
<div id="div3" ></div> -->

 <script>

function reorder(orderArray, elementContainer)
{
    $.each(orderArray, function(key, val){
        elementContainer.append($("#"+val));
    });
}

$(document).ready(function(){
    var order = '${firstColumnWidget}';
    reorder(order.split(','), $("#column1"));
    var orderfirst = '${secondColumnWidget}';
    reorder(orderfirst.split(','), $("#column2"));
    var ordersecond = '${thirdColumnWidget}';
    reorder(ordersecond.split(','), $("#column3"));
   <%--   $('div#divHomePage').html('<%@ include page="/WEB-INF/pages/user/todoList.jsp"%>');  --%>
    $('#grid_header_links').html($('#header_links').html());
    
    $('#NavigationNewTab a').click(function(event) {
    
		event.preventDefault();
		var location = $(this).attr('name');
		var newTabUrl = $(this).attr('href');
		if(location == "newTab") {
		//	alert(newTabUrl);
			if(newTabUrl.charAt(0) == '/') {
				window.open("#bpm"+newTabUrl);
			    _execute('target', '');	
			}else if(newTabUrl.toLowerCase().substring(0, 3) == 'www') {
				window.open("http://"+newTabUrl);
			    _execute('target', '');				
			}else if( (newTabUrl.toLowerCase().indexOf("http") > -1 ) || (newTabUrl.toLowerCase().indexOf("https") > -1 ) ) {
				window.open(newTabUrl);
			    _execute('target', '');				
			}/* else if(newTabUrl.toLowerCase().substring(0, 4) == '#bpm') {
				window.open("http://"+newTabUrl.substring(4));
			    _execute('target', '');				
			} */else {
				window.open("http://"+newTabUrl);
			    _execute('target', '');					
			}
		}else {
			document.location.href = "#bpm/user/homePage";
		    _execute('target', '');  			
		}
	});
    
   <%--  $('div#divHomePage').html('<%@ include page="/WEB-INF/pages/user/todoList.jsp"%>');  --%>
  <%--   var languageCodes = {
			  div1: '/resources/root/jsp/timingtasks.jsp',
			  div3: '/resources/root/jsp/timingtasks.jsp'
			};

			var keys = [];
			for(var k in languageCodes) {
				 
			       //alert("languageCodes   "+url);
			        var url="<%@ include file='"+languageCodes[k]+"'%>"
			   $("#"+k).html("/WEB-INF/pages/user/todoList.jsp");
				
			} --%>
    //setGeneralPropertiesWidth();
   
});
$(function(){
			$('a.maxmin').click(
			function(){
				$(this).parent().siblings('.dragbox-content').toggle();
			});

			/* $('a.delete').click(
			function(mainContainerId){
				var sel = confirm('do you want to delete the widget?');
				if(sel)
				{
					  $("#"+mainContainerId).remove();
					//_execute('target',listViewParam);
					//del code here
					
				}
			}
			); */

			$('#column').sortable({
			
			connectWith: "#column",
			//connectWith: '.column',
			//handle: 'h2',
			//cursor: 'move',
			//placeholder: 'placeholder',
			//forcePlaceholderSize: true,
			opacity: 1,
			//update: function (event, ui) {
           // var list =  $(this).sortable("toArray").join("|");
            
              //  alert(list);
                /* $.ajax({   url: "/persistListOrder.php",
                           data: { 
                                       'section':this.id,              
                                       'components': list
                                 }
                       }); */
      // },
			stop: function(event, ui)
				{
					$(ui.item).find('h2').click();
					var sortorder='';
					var widgetIdOrderList = "";
					var noOfColumns = '${noOfColumns}';

				/*	$('.column').each(function(){
						var itemorder=$(this).sortable('toArray');
						var columnId=$(this).attr('id');
						sortorder+=columnId+'='+itemorder.toString()+'&';
					});*/
					$('.style8').each(function(){
						var id = $(this).attr('id');
						var widgetId = id.split("_");
						if(widgetIdOrderList.length == 0){
							widgetIdOrderList = widgetId[0];
						}else{
							widgetIdOrderList += "," + widgetId[0];
						}
				
						
					});
					sortorder = sortorder.substring(0, sortorder.length - 1)
					 $.ajax({
						url: 'bpm/admin/saveWidgetOrder?widgetIdList='+widgetIdOrderList+'&noOfColumns='+noOfColumns,
					    type: 'GET',
					    dataType: 'json',
						async: false,
						success: function(data) {
				        }
				    });	 
				}
			})
			$('#column').disableSelection();
		});
</script>
  
