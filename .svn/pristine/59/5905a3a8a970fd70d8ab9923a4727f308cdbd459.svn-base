<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%-- <head>
    <title><fmt:message key="folder.heading"/></title>
</head> --%>
<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>
<script type="text/javascript">

$(function(){
	var height = $("#target").height();
	$("#user-grid").css('height',parseInt(height)- 146);
	$("#folderTree").css('height',parseInt(height)- 146);
	$('#user-grid').width('100%');
	getFolderDocument('${folderId}');
	setTimeout(function() {
		$.jstree._focused().select_node("#${folderId}"); 
    },300);
}); 
</script>


<%-- <div class="span12 target-background">
    <div class="container-fluid" id="parent_container">
		<div class="row-fluid">
			<div class="span12">
				<div class="titleBG">
					<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="folder.heading"/></span>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style='background-image: url("/styles/easybpm/images/palette_line.jpg");'>
				<div class="span3" style="width:20%;">
					<div id="folderTree" class="org-tree-left"></div>
				</div>
				<div class="span9" style=";width:80%;">
					 <div class="span12">
						<div id="header_links" align="right" style="display:none">
				              <a style="padding:10px;" href="#bpm/dms/documentForm" onClick="return createFolderDocumentForm();" ><fmt:message key="button.createNew"/></a>
				        </div>
						<div id="user-grid" style=" overflow:auto;" ></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div> --%>

	<div class="page-header">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="folder.heading"/></h2>
	
		<div id="header_links" align="right">
			<a class="padding3" href="#bpm/dms/documentForm" onClick="return createFolderDocumentForm();" ><button class="btn"><fmt:message key="button.createNew"/></button></a>
		</div>
	</div>
  	<div class="span12">
		<div class="span3">
			<div class="widget">
				<div class="widget-header">
					<div class="title">
						<span>文件夹（可右击操作）</span>
					</div>
				</div>
				<div class="widget-body" style="overflow:auto;">
					<div id="folderTree"></div>
				</div>
			</div>
		</div>
		<div class="span9">
		
			<div class="widget">
				<div class="widget-header">
					<div class="title">
						<span>文档</span>
					</div>
				</div>
				<div class="widget-body">    
					<div id="user-grid" class=" overflow " ></div>	
				</div>
			</div>
		</div>
	</div>
	
<script type="text/javascript">
$('#grid_header_links').html($('#header_links').html());
</script>