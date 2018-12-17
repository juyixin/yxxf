 <%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <div class="span12 box">
	<h2>
		<fmt:message key="widget.heading" />
		<a id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showListViews('Widget','Widget');" data-icon=""><fmt:message key="Back"/></a> 	
		
	</h2> --%>
 
	<spring:bind path="widget.*">
		 	<!-- Error Messages -->
<c:if test="${not empty errors}">
  
    <script type="text/javascript">
    var error = "";
    <c:forEach var="error" items="${errors}">
    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.error, error, "error");
    </script>
     <c:remove var="errors"/>
</c:if>

<!-- Success Messages -->
<c:if test="${not empty successMessages}">
    <script type="text/javascript">
    var msg = "";
    <c:forEach var="error" items="${successMessages}">
    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.success, msg, "success");
	showListViews("Widget","Widget");
    </script>
     <c:remove var="successMessages" scope="session"/> 
</c:if>

<c:if test="${not empty status.errorMessages}">
   
   <script type="text/javascript">
    var error = "";
    <c:forEach var="error" items="${status.errorMessages}">
    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.error, error , "error");
	
    </script>
   
</c:if>
<script type="text/javascript">
	$( "#classify").each(function( index ) {
		var element_id = this.getAttribute("id");
		getClassification(element_id,'${widget.id}');
 	});
 	if('${widget.id}' != null && '${widget.id}'.length >0){
		$("#classify option").each(function(){
			if($(this).val() === '${widget.classify}'){
					$(this).attr('selected', true);
			}
		});
	}else{
		$("#classify").prepend("<option value='' selected='selected'></option>");
	}
	function getClassification(id,widget_id){
		if(id.length != 0 && id != null){
			$.ajax({
			url:'bpm/admin/getAllClassification',
			type:'GET',
			dataType: 'json',
				async: false,
				success: function(data) {
				$("#"+id ).empty();
        			if(data){
	        	     		$.each(data, function(index, item) {
	        	     			$("#"+id).get(0).options[$("#"+id).get(0).options.length] = new Option(item.classificationName, item.classificationName);
	             			});
	               	}
	             }
        	});
        }
	}
</script>
<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
<script type="text/javascript">
	var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
	setIndexPageByRedirect(path);

</script>
	</spring:bind>
<div class="page-header">
	<h2><fmt:message key="widget.title" /></h2>
</div>

<div align="right">
	<strong><a class="padding10"
		style="text-decoration: underline;" id="backToPreviousPage"
		href="javascript:void(0);" data-role="button" data-theme="b"
		onClick="showListViews('WIDGET','部件');" data-icon=""><button class="btn"><fmt:message
				key="button.back" /></button></a></strong>

</div>
<div class="height10"></div>
	<div class="span12">
	<div class="row-fluid">
				<div class="widget-body">
					<div class="box_main">
				<form:form  id="widget" commandName="widget" method="post" class="form-horizontal form-actions no-margin" action="bpm/admin/saveWidget" autocomplete="off" modelAttribute="widget" enctype="multipart/form-data" onSubmit="closeSelectDialog('popupDialog');_execute('target','')" >
					<form:hidden path="id" id="id"/>
					<form:hidden path="widgetUrl" />
					<form:hidden path="documentId" id="documentId" name="documentId"/>
					<form:hidden path="selectedListView" id="selectedListView" name="selectedListView"/>
					<input type="hidden" id="isDepartmentAdmin" name="isDepartmentAdmin" value="${isDepartmentAdmin}">
					
					<div class="control-group">
				 		<eazytec:label styleClass="control-label" key="widget.name"/>
				 	 	<div class="controls">
				 			 <form:input path="name" id="name" class="span6"/>
				 		</div>
				 	</div>
				 	
			 		<div class=" control-group">
			 			<eazytec:label styleClass="control-label" key="widget.classify"/>
					 	 <div class="controls">
					 		 <form:select path="classify" name="classify" id="classify" class="span6"  >
					 		   <!-- <option value="News Widget">News Widget</option>
		  						<option value="Report Widget">Report Widget</option>
		  						<option value="OA Widget">OA Widget</option>
		  						<option value="Quick Widget">Quick Widget</option>-->
		  					</form:select>
					 	</div>
					</div>
			 
					<div class="control-group">
					   <eazytec:label styleClass="control-label" key="widget.linkType" />
						<div class="controls">
							<label class="radio inline"> 
								<form:radiobutton path="linkType" name="linkType" id="widgetUrl" value="JSP" checked="true" onclick="clickLinkType(this.value)" />
								<eazytec:label key="widget.jsp" />
							</label>
							<label class="radio inline"> 
								<form:radiobutton path="linkType" name="linkType" id="viewList" value="listView"   onchange="clickLinkType(this.value)" /> 
								<eazytec:label key="widget.viewList" />
							</label> 
							<label class="radio inline"> 
								<form:radiobutton path="linkType" name="linkType" id="reportList" value="report" onclick="clickLinkType(this.value)" />
								<eazytec:label key="widget.reportList" />
							</label>			
						</div>
					</div>
					
					<div id="listViewDiv" class="control-group displayNone">
						<div id="listViewLabel" >
							<eazytec:label styleClass="control-label" key="widget.listView" />
						</div> 
						<div id="listViewValue"  class="controls">
							<form:input path="listView" id="listView"  name="listView" class="span6"  onclick="showListViewSelection('ListView', 'single' , 'selectedListView',  this, '');"/>
						</div> 
					</div>
					<div id="reportListDiv" class="control-group displayNone">
						<div id="reportListLabel" >
							<eazytec:label styleClass="control-label" key="widget.report" />
						</div> 
						<div id="reportListValue"  class="controls">
							<form:input path="report" class="span6"  onclick="showReportSelection('Report', 'single' , 'report',  this, '');"/>
						</div> 
					</div>
			 
					<div id="contentLinkDiv" class="control-group displayNone" >
					 	<div id="contentLinkLabel">
							<eazytec:label styleClass="control-label" key="widget.contentLink"  />
						</div>
						<div id="contentLinkValue" class="controls">
							<input type="file" name="file" id="file"/>
						</div>
						
						<div class="control-group" >
							<div id="filePath" class="controls">
								<label> ${widget.widgetUrl}</label>
							</div>
						</div>
					</div>
					<div class="form-actions">
						<c:if test="${isDepartmentAdmin}">
							<button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="save"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${mode != 'CREATE'}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
					</c:if>
						<button type="button" class="btn btn-primary" name="next"
							onclick="showListViews('Widget','部件');" id="cancelButton"
							class="cursor_pointer"><i class="icon-remove icon-white"></i>
							<fmt:message key="button.cancel" />
						</button>

					</div>
				</form:form>
			</div>
		</div>
	</div>
</div> 
	
 <script type="text/javascript">
 
 var linkType="${linkType}";
  $(document).ready(function(){
	//  document.getElementById("contentLink").value = document.getElementById("documentId").value;
	//alert("${linkType}");
	//alert("${contentLink}");
	//alert("${listView}");
	 clickLinkType("${linkType}");
	 $('select[name="classify"]').find('option[value="${widget.classify}"]').attr("selected",true);
 }); 
	function clickLinkType(value) {
	 linkType = value;
	 if(linkType == 'JSP'){
		if($("#contentLinkDiv").hasClass("displayNone")) {
			$("#contentLinkDiv").addClass("displayBlock");
			$("#contentLinkDiv").removeClass("displayNone");
			$("#listViewDiv").removeClass("displayBlock"); 
			$("#listViewDiv").addClass("displayNone");
			$("#reportListDiv").removeClass("displayBlock"); 
			$("#reportListDiv").addClass("displayNone");
			$("#listView").val("");
		}
	}else  if(linkType == 'report'){
		if($("#reportListDiv").hasClass("displayNone")) {
			$("#contentLinkDiv").removeClass("displayBlock"); 
			$("#contentLinkDiv").addClass("displayNone");
			$("#listViewDiv").removeClass("displayBlock"); 
			$("#listViewDiv").addClass("displayNone");
			$("#reportListDiv").removeClass("displayNone"); 
			$("#reportListDiv").addClass("displayBlock");
			$("#report").val('${widget.report}');
		} 
	} else {
		if($("#listViewDiv").hasClass("displayNone")) {
			$("#listViewDiv").addClass("displayBlock");
			$("#listViewDiv").removeClass("displayNone");
			$("#contentLinkDiv").removeClass("displayBlock"); 
			$("#contentLinkDiv").addClass("displayNone");
			$("#reportListDiv").removeClass("displayBlock"); 
			$("#reportListDiv").addClass("displayNone");
			$("#contentLink").val("");
		}
	}
	
	}
	
	$("#save").click(function(event) { 
		if(linkType == 'JSP'){
			var jspUrl = '${widget.widgetUrl}';
			
			var idValue = $('#id').val();
			var name  = $('#name').val();
			var classification = $('#classify').val();
			
			
			var uploadedFiles = document.getElementById('file');
			
			if(uploadedFiles.value == ''){
			
				if((idValue == '') || jspUrl == null || jspUrl == "(NULL)" || (idValue != '' && jspUrl == '') ){
				
					openMessageBox(form.title.error,form.msg.jspNotEmpty,"error",true);
					event.preventDefault();
				}	

				if(classification == null || classification == '' || classification.length == 0) {
					openMessageBox(form.title.error,"Classification is a required field","error",true);
						event.preventDefault();
			}
	
				if(name == null || name == '' || name.length == 0) {
					openMessageBox(form.title.error,"Name is a required field","error",true);
					event.preventDefault();
			}
			}
			//Uploaded Document Validation
			var regex= /^.*\.(jsp|html)$/i;
			for (var i = 0; i < uploadedFiles.files.length; ++i) {
				var uploadedFileName = uploadedFiles.files.item(i).name;
				if(regex.test(uploadedFileName) != true) {
					openMessageBox(form.title.titleFileFormat,form.msg.fileFormat,"error", true);
					event.preventDefault();
				}
			}
		} else if (linkType == 'listView'){
			var listViewName = $('#listView').val();
			if(listViewName == null || listViewName == ''){
				openMessageBox(form.title.error,form.msg.listViewNotEmpty,"error",true);
				event.preventDefault();
			}
		} else {
			var reportName = $('#report').val();
			if(reportName == null || reportName == ''){
				openMessageBox(form.title.error,form.msg.reportNotEmpty ,"error",true);
				event.preventDefault();
			}
		}
	});
	
	</script>
<%--	<v:javascript formName="widget" staticJavascript="false"/>
	<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>	 --%>
