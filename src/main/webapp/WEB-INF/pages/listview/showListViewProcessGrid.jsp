<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/messages.jsp" %>



<c:if test="${success == true}">
<div class="span12">
	<div class="row-fluid">
		<div class="span12">
			<div class="page-header">
				<div class="pull-left">
					<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>${gridTitle}</h2>
				</div>
				<div id="header_links" align="right"></div>
			</div>
		</div>
	</div>
	
	<div class="row-fluid">
    	<div class="span12">
        	<c:if test="${gridType != 'documentToBeDone' && gridType != 'readDocumentList' && gridType != 'toReadDocument' && gridType != 'doneDocumentList' }">
               <div class="span3">
               		<div class="widget">
						<div class="widget-header">
							<div class="title">
								<span class="fs1" aria-hidden="true"></span> <fmt:message key="listView.classifications" />
							</div>
						</div>
						<div class="widget-body">
							<div id="tree_structure" ></div>
							<div class="clearfix"></div>
						</div>
					</div>
               </div>
            </c:if>
            <div class="span9">
				<div class="widget">
					<div class="widget-header">
						<div class="title text-capitalize">
							<span class="fs1" aria-hidden="true"></span> ${gridTitle}
						</div>
					</div>
					<div class="widget-body">
						<div id="data-details" class="floatLeft department-list-users">
                           	<div id="user-grid" >
                               <%= request.getAttribute("script") %> 	
							<div id="gridRecordDetails"></div>	
                           	</div>
                       	</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	 var targetWidth = $('#target').width();
	 var targetHeight = $('#target').height();
	 var _width = parseInt(targetWidth) / 5;
	 $('#tree_structure').height(parseInt(targetHeight)-130);
	 $('#tree_structure').width("100%");
	 $('#data-details').width('100%');
	 $('#user-grid').width('100%');
	 $('#user-grid').height(parseInt(targetHeight)-130);
	 var jsonData ='${jsonTree}' ;
	 constructJsTree("tree_structure",jsonData,"reloadProcessGridByClassification",false);
	 //$('#header_links').html($('#grid_header_links').html());
	 $('#headerTag').hide();
});
</script>
</c:if>
<c:if test="${success == false}">
<input type="hidden" id="error_msg" value="<%=request.getAttribute("script")%>" />
<script type="text/javascript">
$(function(){
	$.msgBox({
		title : form.title.error,
		content : $("#error_msg").val(),
		type : "error"
	});
});
</script>
</c:if>
<div id="print_preview" class="displayNone"></div>
