<%@ include file="/common/taglibs.jsp"%>

    <script language="javascript">
    $(document).ready(function(){
    	var filePath="${customOperatingFunction.pictureByteArrayId}";
    	if(filePath!=""){
    		var fileName=filePath.split('/');
        	$("#importCustomPictureName").val(fileName[fileName.length-1]);
    	}
    });
    
    </script>

<div class="page-header">
	<div class="pull-left">
		<c:if test="${empty customOperatingFunction.id}">
			<h2 id="createCustomFunctionHeader"><fmt:message key="createCustomFunction.title"/></h2>
		</c:if>
 
 		<c:if test="${not empty customOperatingFunction.id}">
			<h2 id="updateCustomFunctionHeader"><fmt:message key="updateCustomFunction.title"/></h2> 
 		</c:if>
	</div>
</div>
	<div class="height10"></div>
	<div align="right"><a style="padding:10px;text-decoration: underline;"  id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showListViews('CUSTOM_OPTION_LIST','Custom Operation');" data-icon=""><strong><fmt:message key="Back"/></strong></a> </div>	 
	<%-- Error Messages --%>
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

<%-- Success Messages --%>
<c:if test="${not empty successMessages}">
    <script type="text/javascript">
    var msg = "";
    <c:forEach var="error" items="${successMessages}">
    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.success, msg, "success");
	showListViews("CUSTOM_OPTION_LIST","Custom Operation");
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

<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
<script type="text/javascript">
var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
setIndexPageByRedirect(path);
</script>
  
 
		<div class="row-fluid">
			<div class="widget">
				<div class="widget-body ">
				<form:form name="customOperatingFunction" id="customOperatingFunction" cssClass="form-horizontal no-margin" modelAttribute="customOperatingFunction" method="post" action="bpm/customOperating/saveCustomOperatingFunction" enctype="multipart/form-data" >
		 			<form:hidden path="id"/>
		 			<form:hidden path="pictureByteArrayId"/>
				
					<div class="control-group">
					    <eazytec:label styleClass="control-label"  key="customOperatingFunction.name"/>
						<div class="controls">
			                <c:if test="${empty customOperatingFunction.id}"> 
					        <form:input path="name" id="name" class="span6"/>
					        </c:if>
					            
					        <c:if test="${not empty customOperatingFunction.id}">
					            <form:input path="name" id="name" readonly="true"  class="span6"/> 
					        </c:if>
			    	    </div>
		  			</div>
					
					<div class="control-group">
					    <eazytec:label styleClass="control-label" key="customOperatingFunction.callFunction"/>
						<div class="controls">
			                <form:input path="callFunction" id="callFunction" class="span6"/>
					    </div>
			   		</div>
			    
			    	<div class="control-group">
					    <eazytec:label styleClass="control-label " key="customOperatingFunction.jsFunction"/>
				    	<div class="controls">
			                <form:textarea path="jsFunction" id="jsFunction"  class="span6" rows="5"/>	
					  	</div>
					</div>
				
				    <div class="control-group">
					    	<eazytec:label styleClass="control-label" key="customOperatingFunction.htmlProperty"/>	
				    	<div class="controls">
			                <form:textarea path="htmlProperty" id="htmlProperty"  class="span6" rows="3"/>
					 	</div>
			   		</div>
					
					<div class="control-group">
					    <eazytec:label styleClass="control-label" key="customOperatingFunction.picture"/> 		
				    	<div class="controls">
			                <input type="text" class="medium" id="importCustomPictureName" readonly="readonly"/>
			                <input type="file" name="file" id="importCustomPicture" multiple="multiple" class="bpm_import_file" size="23"/>
					    </div>
			   		</div>
			   		
			   		<div class="control-group" id="descriptionRow">
					    <eazytec:label styleClass="control-label style3 style18 " key="metaTable.description"/>		
				    	<div class="controls">
			                 <form:textarea path="description" id="description"  class="span6" rows="3"/>
					    </div>
			   		</div>
			   		<div class="control-group">
							<div class="form-actions no-margin">
								<button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false;" id="saveButton">
                                 	<c:choose>
                                        <c:when test ="${not empty customOperatingFunction.id}">
                                        	<fmt:message key="button.update"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="button.save"/>
                                        </c:otherwise>
                                 	</c:choose>     	
                            	</button>
                            	<button type="button" class="btn btn-primary " name="next" onclick="showListViews('CUSTOM_OPTION_LIST','Custom Operation');" id="cancelButton" style="cursor: pointer;">
			        				<fmt:message key="button.cancel"/>
			        			</button>
								<div class="clearfix"></div>
							</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
