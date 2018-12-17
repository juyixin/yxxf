<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	
</script>

<div class="page-header">
<h2><fmt:message key="emailContact.heading"/></h2>
</div>
	
<div align="right"><strong><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showListViews('CONTACTS','Contacts');" data-icon="">后退</a></strong>
 </div>
 <div class="height10"></div>
	<spring:bind path="emailContact.*">
    <%@ include file="/common/messages.jsp" %>
    </spring:bind>
		<div class="row-fluid">
			<div class="widget">
				<div class="widget-body ">
				<form:form  id="emailContact" commandName="emailContact" method="post" cssClass="form-horizontal no-margin" action="/bpm/mail/jwma/saveMailContact" autocomplete="off" modelAttribute="emailContact" onSubmit="_execute('target','');">
					<form:hidden path="id"/>
					<form:hidden path="createdBy"/>
				
					<div class="control-group">
					    <label class="control-label">姓名<span class="required">*</span></label>
						<div class="controls">
			                <form:input path="name" id="name" class="span6"/>
			    	    </div>
		  			</div>
			    
			    	<div class="control-group">
					    <eazytec:label styleClass="control-label"  key="emailContact.englishName"/>    		
				    	<div class="controls">
			                <form:input path="englishName" id="englishName" class="span6"/>
					  	</div>
					</div>
					
				    <div class="control-group">
					    <eazytec:label styleClass="control-label" key="emailContact.email"/>    		
				    	<div class="controls">
			                <form:input path="email" id="email" class="span6"/>
					 	</div>
			   		</div>
					
					<div class="control-group">
					    <eazytec:label styleClass="control-label" key="emailContact.mobile"/>    		
				    	<div class="controls">
			                <form:input path="mobile" id="mobile" class="span6"/>
					    </div>
			   		</div>
			   		
			   		<div class="control-group">
					    <eazytec:label styleClass="control-label" key="emailContact.workPhone"/>    		
				    	<div class="controls">
			                <form:input path="workPhone" id="workPhone" class="span6"/>
					    </div>
			   		</div>
			   		
			   		<div class="control-group">
					    <eazytec:label styleClass="control-label" key="emailContact.homePhone"/>    		
				    	<div class="controls">
			                <form:input path="homePhone" id="homePhone" class="span6"/>
					   </div>
					</div>
			   		
			   		<div class="control-group">
					    <eazytec:label styleClass="control-label" key="emailContact.fax"/>    		
				    	<div class="controls">
			                <form:input path="fax" id="fax" class="span6"/>
						</div>
					</div>
					
					<div class="control-group">
					    <eazytec:label styleClass="control-label" key="emailContact.website"/>    		
				    	<div class="controls">
			                <form:input path="website" id="website" class="span6"/>
					    </div>
			   		</div>
			   		
					<div class="control-group">	
				    	<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary" name="save" id="saveButton">
			        			<c:choose>
                                    <c:when test ="${emailContact.id != null && emailContact.id!= ''}">
                                    	<fmt:message key="button.update"/>
                                    </c:when>
                                    <c:otherwise>
                                    	<fmt:message key="button.save"/>
                                    </c:otherwise>
                            	</c:choose>
			    			</button>
			    			<button type="button" class="btn btn-primary " name="next"
								 onClick="showListViews('CONTACTS','Contacts');" id="cancelButton"
								style="cursor: pointer;">
								<fmt:message key="button.cancel" />
							</button>
							
			    			<div class="clearfix"></div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>


<%-- </page:applyDecorator> --%>
<v:javascript formName="emailContact" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
