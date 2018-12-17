<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="roleList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<%@ include file="/common/messages.jsp" %>
<!--value = '${widgetId}'-->
<div class="row-fluid">
    	<input type="hidden" name="noOfWidget"  id="noOfWidget" value = '${noOfColumns}' />
    	<input type="hidden" name="id"  id="id"  value = '${id}'/>
		<input type="hidden" name="userwidgetNames"  id="userwidgetNames" />
		<input type="hidden" name="departments"  id="departments"  value = '${departments}'/>
		<input type="hidden" name="roles"  id="roles"  value = '${roles}'/>
		<input type="hidden" name="assignedTo"  id="assignedTo"  value = '${assignedTo}'/>
		<input type="hidden" name="widgetNamesHidden"  id="widgetNamesHidden"  value = "${widgetNames}"/>
		<div class="page-header">
			<div class="pull-left">
				<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="layout.manageLayout"/></h2>
			</div>
			<div id="layout_header_links" align="right">
            	<a id="widget" href="javascript:void(0);" onClick="showUserWidgetSelection('请选择部件', 'multi' , 'userwidgetNames', document.getElementById('widgetNamesHidden'), '');" class="padding:3px;"><button class="btn"><fmt:message key="widget.select"/></button></a> 
				<a id="createLayout" href="javascript:void(0);" onClick="showUserLayoutSelection('请选择布局', 'single' , 'noOfWidget', document.getElementById('noOfWidget'), '');" class="padding:3px;" ><button class="btn"><fmt:message key="layout.select"/></button></a>
	        </div>
		</div>
		<div class="span12">
			<div class="widget">
				<div class="widget-body">
					<div id="manage_layout" class="manageMenu_boder" >
					 	<div id="layout" class="span12">
							<div id="divsample" class="span12 row-fluid"> 
								<div id="divManageLayout" class="span12"> <%= request.getAttribute("result") %>  </div> 
		 					</div>
		 					<div class="span12 row-fluid form-actions no-margin" id="LayoutButtonDiv">
		 						<center><button class="btn btn-primary pull-center"  onClick="saveUserHomePage();" ><i class="icon-ok icon-white"></i> <fmt:message key="button.save"/></button></center>
		 						<div class="clearfix"></div>
		 					</div>
						</div>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>
</div>
<script>

$(document).ready(function(){
	var height = $("#target").height();
	$("#manage_layout").css('height',parseInt(height)- 130);
	$('#header_links').html($('#layout_header_links').html());
});
 
function reorder(orderArray, elementContainer) {
    $.each(orderArray, function(key, val){
        elementContainer.append($("#"+val));
    });
}
/*$(document).ready(function(){
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
			} else {
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
   
});*/
$(function(){
			$('a.maxmin').click(
			function(){
				$(this).parent().siblings('.dragbox-content').toggle();
			});

			/*$('.column').sortable({
			connectWith: '.column',
			handle: 'h2',
			cursor: 'move',
			placeholder: 'placeholder',
			forcePlaceholderSize: true,
			opacity: 0.4,
			update: function (event, ui) {
            var list =  $(this).sortable("toArray").join("|");
            
              //  alert(list);
                /* $.ajax({   url: "/persistListOrder.php",
                           data: { 
                                       'section':this.id,              
                                       'components': list
                                 }
                       }); */
    /*   },
			stop: function(event, ui)
				{
					$(ui.item).find('h2').click();
					var sortorder='';

					$('.column').each(function(){
						
						var itemorder=$(this).sortable('toArray');
						var columnId=$(this).attr('id');
						sortorder+=columnId+'='+itemorder.toString()+'&';
					});
					sortorder = sortorder.substring(0, sortorder.length - 1)
					if(sortorder != ''){
					 $.ajax({
						url: 'bpm/admin/saveWidgetOrder?'+sortorder,
					    type: 'GET',
					    dataType: 'json',
						async: false,
						success: function(data) {
				        }
				    });	 
				    }
				}
			}).disableSelection();*/
			$('#column').sortable({
			connectWith: "#column",
			opacity: 1,
			stop: function(event, ui)
				{
					$(ui.item).find('h2').click();
					var sortorder='';
					var widgetIdOrderList = "";
					var noOfColumns = '${noOfColumns}';

					$('.style8').each(function(){
						var id = $(this).attr('id');
						var widgetId = id.split("_");
						if(widgetIdOrderList.length == 0){
							widgetIdOrderList = widgetId[0];
						}else{
							widgetIdOrderList += "," + widgetId[0];
						}
						document.getElementById("userwidgetNames").value = widgetIdOrderList;				
						
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
  
