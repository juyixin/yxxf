<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%-- Error Messages --%>
<c:if test="${not empty errors}">
    <script type="text/javascript">
    var error = "";
    <c:forEach var="error" items="${errors}">
    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.error, error, "error");
	showListViews("PROCESS_LIST", "Process List");
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
	showListViews("PROCESS_LIST", "Process List");
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
	showListViews("PROCESS_LIST", "Process List");
    </script>
</c:if>

<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
<script type="text/javascript">
var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
setIndexPageByRedirect(path);
</script>



	<c:if test="${type != 'import'}"> 
	 <%= request.getAttribute("script") %>
	</c:if>
		<c:if test="${type == 'import'}"> 
		<div class="span7 scroll">
		<div class="table-bpm-import">
	   	 <form:form commandName="fileUpload" name="fileUpload" method="post" action="bpm/showProcess/deployBpmnFile" onSubmit="closeImportFileDialog();" enctype="multipart/form-data"
	        cssClass="form-horizontal form-horizontal-popup">
	        <spring:bind path="fileUpload.classificationId">
				<fieldset
					class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
						
					<label class="control-label">分类</label>
					<div class="controls">
					<form:select path="classificationId" id="classificationId" style="height: 30px" class="medium"
							maxlength="500" ><c:forEach
								items="${processClassifications}" var="processClassification">
								<option value="${processClassification.value}">${processClassification.label}</option>
							</c:forEach>
						&nbsp;&nbsp;</form:select>
						&nbsp;&nbsp;<a href="javascript:showAddClassification(0)">添加</a>
					</div>
				</fieldset>
				</spring:bind>
				<spring:bind path="fileUpload.file">
	        <fieldset class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
	        </spring:bind>
	            <eazytec:label key="importBpmn.title" styleClass="control-label style3 style18"/>
	            <div class="controls">
	                <input type="file" name="file" id="file" multiple="multiple" class="bpm_import_file" size="23"/>
	                <br><span class="style16" >(只允许上传 .bpmn20.xml 文件)</span>
	                <form:errors path="file" cssClass="help-inline"/>
	            </div>
	        </fieldset>
	        <div class="control-group">
	        <div class="form-actions no-margin">
	            <button type="submit" name="upload" class="btn btn-primary" onclick="return validateBpmnFileUpload(this.form);">
	                 <fmt:message key="button.upload"/>
	            </button>
	            <span class="empty_space"></span>
	            <button name="cancel" class="btn btn-primary" onclick="bCancel=true;closeSelectDialog('importPopupDialog');" type="reset">
	                <fmt:message key="button.cancel"/>
	            </button>
	        </div>
	       </div>
	    </form:form>		 
	 </div>
</div>  
 
	 
	 <c:set var="scripts" scope="request">
		<v:javascript formName="fileUpload" staticJavascript="false"/>
		<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
	</c:set> 
    </c:if>
