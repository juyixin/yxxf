<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="roleList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<%@ include file="/common/messages.jsp" %>

<div class="row-fluid">
    	<input type="hidden" name="noOfWidget"  id="noOfWidget" value = '' />
    	<input type="hidden" name="id"  id="id"  value = '${id}'/>
		<input type="hidden" name="widgetNames"  id="widgetNames" />
		<input type="hidden" name="departments"  id="departments"  value = ''/>
		<input type="hidden" name="roles"  id="roles"  value = ''/>
		<input type="hidden" name="assignedTo"  id="assignedTo"  value = ''/>
		<input type="hidden" name="widgetNamesHidden"  id="widgetNamesHidden"  value = ""/>
		<input type="hidden" name="deleteValidationFlag"  id="deleteValidationFlag"  value = ""/>
		<div class="page-header">
			<div class="pull-left">
				<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="layout.manageLayout"/></h2>
			</div>
			<div id="layout_header_links" align="right">
            		<a  id="widget" href="javascript:void(0);" onClick="showWidgetSelection('请选择部件', 'multi' , 'widgetNames', document.getElementById('widgetNames'), '');" class="floatRight createEvent pad-R20 pad-T10 text-underline"  ><fmt:message key="widget.select"/></a> 
			  		<a  id="createLayout" href="javascript:void(0);"   onClick="showLayoutSelection('Select Layout', 'single' , 'noOfWidget', document.getElementById('noOfWidget'), '');" class="floatRight createEvent pad-R20 pad-T10 text-underline" ><fmt:message key="layout.select"/></a>
	        </div>
		</div>
		<div class="span12">
			<div class="span3">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key="layout.organizations"/>

						</div>
					</div>
					<div class="widget-body">
						<div id="layout_tree" class="menuForm_border" >
							<div id="layoutTree" ><%= request.getAttribute("orgTreeScript") %></div>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
			<div class="span9">
				 <div class="widget">
				 	<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key="layout.layoutWidget"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="manage_layout" class="manageMenu_boder" >
						 	<div id="layout" class="span12">
								<div id="divsample" class="span12 row-fluid"> 
									<div id="divManageLayout" class="span12"> <%= request.getAttribute("result") %>  </div>
			 					</div>
			 					<div class="span12 row-fluid padding10" id="LayoutButtonDiv">
			 	
			 						<!-- <div class="span6"> </div> --> <div class="span12"><div class="form-actions no-margin browse_button"><button class="btn btn-info pull-center "  onClick="saveHomePage();"><fmt:message key="save.homePage"/></button></div></div>
			 					</div>
		 					</div>
	 					</div>
	 					<div class="clearfix"></div>
	 				</div>
				</div>
			</div>
		</div>
</div>
<script type="text/javascript">

$(document).ready(function(){
	var userRole=$("#currentUserRoles").val().split(",");
	var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
	if(isAdmin){
		$("#createLayout").hide();
	}else{
		$("#createLayout").show();
	}
	var height = $("#target").height();
	$("#manage_layout").css('height',parseInt(height)- 130);
	$("#layout_tree").css('height',parseInt(height)- 130);
	setTimeout(function() {
		$('a', '#public').click();
		//$.jstree._focused().select_node("#public"); 
    },300);
});

function reorder(orderArray, elementContainer) {
    $.each(orderArray, function(key, val){
        elementContainer.append($("#"+val));
    });
}


$(function(){
	var height = $("#target").height();
	$("#divsample").css('min-height',parseInt(height) - 67);
	$('#header_links').html($('#layout_header_links').html());
	$('a.maxmin').click(
	function(){
		$(this).parent().siblings('.dragbox-content').toggle();
	});

	$('a.delete').click(
	function(){
		//var sel = confirm('do you want to delete the widget?');
		if(sel)
		{
			//_execute('target',listViewParam);
			//del code here
			
		}
	});

	
});
		
		

</script>
  
