<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ page session="true" import="dtw.webmail.model.*" %>
<%@ include file="/common/taglibs.jsp"%>
<div class="page-header">
	<div class="pull-left">
		<h2><fmt:message key="jwma.mail.settings"/></h2>
	</div>
</div>
<div class="height10"></div>

	<spring:bind path="user.*">
        <%-- Error Messages --%>
		<c:if test="${not empty errors}">
		    <script type="text/javascript">
			    var error = "";
			    <c:forEach var="error" items="${errors}">
			    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
				</c:forEach>
				validateMessageBox("Email Authentication Error", error , "alert");
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
		    </script>
		    <c:remove var="successMessages" scope="session"/> 
		</c:if>
		
		<c:if test="${not empty status.errorMessages}">
		   <script type="text/javascript">
			    var error = "";
			    <c:forEach var="error" items="${status.errorMessages}">
			    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
				</c:forEach>
				validateMessageBox("Email Authentication Error", error , "error");
		    </script>
		</c:if>
		
		<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
    </spring:bind>

	<script type="text/javascript">
		var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
		setIndexPageByRedirect(path);
		</script>
		
		<script type="text/javascript">
		$(document).ready(function(){
			  	$('#confirmPassword').val('${user.password}');
			  	var status = '${status}';
			  	if(status == '' || status == null || status == '连接失败'){
			  		status = '连接失败';
			  		$('#status').css( "color", "red" );
			  	} else {
			  		$('#status').css( "color", "green" );
			  	}
			  	$('#status').val(status);
			});
	</script>
	<div class="span12">
	<div class="row-fluid">
				<div class="widget-body">
					<div class="box_main">
				<form:form commandName="user" method="post" action="bpm/mail/jwma/saveEmailSetting" id="userMailForm" cssClass="form-horizontal no-margin"  onSubmit="_execute('target','')">
				    <form:hidden path="id"/>
				    <form:hidden path="username"/>
				    <form:hidden path="password"/>
				   	<form:hidden path="confirmPassword"/>
				   	<form:hidden path="englishName"/>
				    <form:hidden path="passwordHint"/>
				   	<form:hidden path="mobile"/>
				   	<form:hidden path="employeeNumber"/>
				   	<form:hidden path="idCardNumber"/>
				   	<form:hidden path="workPhone"/>
				    <form:hidden path="homePhone"/>
				    <form:hidden path="fax"/>
				   	<form:hidden path="website"/>
				    <form:hidden path="address.postalCode"/>
			
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="user.fullName" />
						<div class="controls">
							<form:input path="fullName" id="fullName" maxlength="50"
								class="span8" />
						</div>
					</div>

					<div class="control-group">
					    <eazytec:label styleClass="control-label" key="user.email"/>
		          		<div class="controls">
		              		<form:input path="email" id="email" readonly="true" class="span6"/>
					    </div>
			   		</div>
			   		
			   		<div class="control-group">
					    <eazytec:label styleClass="control-label" key="user.password"/>
			            <div class="controls">
			                <form:password path="emailPassword" id="emailPassword" showPassword="true" class="span6"/>
					   </div>
					</div>
			   		
			   		<div class="control-group">
					    <eazytec:label styleClass="control-label" key="jwma.user.signature"/>
		          		<div class="controls">
		              		<form:textarea path="signature" id="signature" class="span6"/>
						</div>
					</div>
					
					<div class="control-group">
					    <eazytec:label styleClass="control-label" key="jwma.user.status"/>
						<div class="controls checkbox-label">
							<label id="status">${status}</label>
					    </div>
			   		</div>
			   		
					<div class="control-group">	
				    	<div class="form-actions no-margin" id="saveButtonDiv">
							<button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false;" id="saveButton"  style="cursor: pointer;"><i class="icon-ok icon-white"></i>
				        		<c:choose>
									<c:when test ="${user.version !=0 }">
										<fmt:message key="button.update"/>
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save"/>
									</c:otherwise>
								</c:choose>
				    		</button>
				    		<div class="clearfix"></div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

			    