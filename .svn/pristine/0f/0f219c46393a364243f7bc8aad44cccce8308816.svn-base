<%@ include file="/common/taglibs.jsp" %>

<%@ include file="/common/messages.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
	var height = $("#target").height();
	$("#all_tables").css('height',parseInt(height)- 128);
	//with query
	$("#rightPanel").css('height',parseInt(height)- 362);
	//$("#rightPanel").css('height',parseInt(height)- 40);
});
</script>
<%= request.getAttribute("script") %>
<%-- <div class="span12">
<div class="container-fluid" id="parent_container">
<div class="row-fluid">
		<div class="span12">
			<div class="titleBG">
				<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="table.list"/></span>
			</div>
		</div>
	</div>
<div class="row-fluid">
	    <div class="span12">
			<div id="all_tables" class="span3" style="width:20%;overflow:auto;border:1px solid gray;">
				<div id="all_table_tree" style="background-image: url('/styles/easybpm/images/palette_line.jpg');"></div>
			</div>
	  		<div>
				<%@ include file="/WEB-INF/pages/table/showQueryArea.jsp" %>
			</div>  
			<div id="rightPanel" class="span9" style="width:80%;overflow-y:auto;overflow-x:hidden;">
			</div>
		</div>
	</div>
</div>
</div>	 --%>

<div id="parent_container">
	<div class="page-header">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>
			<fmt:message key="table.list" />
		</h2>
	</div>
	<div class="row-fluid">
		<div class="span12">
			<div class="span3">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key="module.title.tables"/>
						</div>
					</div>
					<div class="widget-body overflow" id="all_tables">
						<div  id="all_table_tree">
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
			<div class="span9">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key="module.title.query"/>
						</div>
					</div>
					<div class="widget-body">
						<%@ include file="/WEB-INF/pages/table/showQueryArea.jsp" %>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
			<div class="span9">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key="module.title.details"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="rightPanel" > </div>
					</div>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>
	</div>
</div>

