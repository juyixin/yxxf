<%-- <%@ page session="true" import="com.eazytec.model.Report" %> --%>
<%@ include file="/common/taglibs.jsp"%>
<div class="span12 box">
 	<h2><fmt:message key="report.title"/></h2>
 
<div class="span11">	
	<div style="padding-left:100px;">
		<form:form commandName="report" method="post" action="bpm/report/saveReport" id="reportForm" cssClass="form-horizontal" onSubmit="_execute('target','')">
			    
			    	<table>
								<tr>
									<td>
									    <fieldset class="control-group">
								            <eazytec:label styleClass="control-label style3 style18" key="report.name"/>
								            <div class="controls">
								                <form:input path="reportName" id="reportName" class="normal" readonly="false"/>
								            </div>
								        </fieldset>
								       <fieldset class="control-group">
								            <eazytec:label styleClass="control-label style3 style18 " key="report.classification"/>
								            <div class="controls">
								                <form:input path="classification" id="classification" class="normal" readonly="false"/>
								            </div>
								        </fieldset>
								        <fieldset class="control-group">
							          		<eazytec:label styleClass="control-label style3 style18" key="report.url"/>
							          		<div class="controls">
							              		<form:input path="reportUrl" id="reportUrl" readonly="false" class="normal"/>
							          		</div>
							      		</fieldset>
							      		<fieldset class="control-group">
							          		<eazytec:label styleClass="control-label style3 style18" key="report.roles"/>
							          		<div class="controls">
							              		<form:input path="roles" id="roles" readonly="readonly" class="normal" onclick="showRoleSelection('Role', 'multi' , 'roles', this, '');" name="roles"/>
							          		</div>
							      		</fieldset>
							      		<fieldset class="control-group">
							          		<eazytec:label styleClass="control-label style3 style18" key="report.departments"/>
							          		<div class="controls">
							              		<form:input path="departments" id="departments" readonly="readonly" class="normal" onclick="showDepartmentSelection('Department', 'multi' , 'departments', this, '');" name="departments"/>
							          		</div>
							      		</fieldset>
											<fieldset class="control-group">
							          			<eazytec:label styleClass="control-label style3 style18" key="report.groups"/>
							          			<div class="controls">
							              			<form:input path="groups" id="groups" readonly="readonly" class="normal" onclick="showGroupSelection('Group', 'multi' , 'groups', this, '');" name="groups"/>
							          		</div>
							      		</fieldset>
										<fieldset class="control-group">
							          			<eazytec:label styleClass="control-label style3 style18" key="report.users"/>
							          			<div class="controls">
							              			<form:input path="users" id="users" readonly="readonly" class="normal" onclick="showUserSelection('User', 'multi' , 'users', this, '');" name="users"/>
							          		</div>
							      		</fieldset>
							      		<fieldset class="control-group">
							            	<eazytec:label styleClass="control-label style3 style18 " key="report.status"/>
												<div class="controls">
													<label class="radio inline">
														<form:radiobutton path="status" name="status" id="status" value="active" checked="true"  onclick="" />
														Active
													</label> 
													<label class="radio inline">
														<form:radiobutton path="status" name="status" id="status" value="inactive"  onclick="" />
													Inactive
													</label>
												</div>
										</fieldset>
										
							      		<fieldset class="control-group">
							          		<eazytec:label styleClass="control-label style3 style18" key="report.description"/>
							          		<div class="controls">
							              		<form:textarea path="description" id="description" class="normal"/>
							          		</div>
							      		</fieldset>
							      		
							      	</td>
								</tr>
							</table>
							<fieldset id="buttons" class="form-actions pad-L300">
				<div class="cont">
					<div class="button" id="saveButtonDiv">
						 <button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" onclick="bCancel=false" id="saveButton"  style="cursor: pointer;">
			        			        	
			        		<c:choose>
								<c:when test ="${report.id != null }">
									<fmt:message key="button.update"/>
								</c:when>
								<c:otherwise>
									<fmt:message key="button.save"/>
								</c:otherwise>
							</c:choose>
			    		</button>
			    		<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onclick="#" id="cancelButton"  style="cursor: pointer;">
			        		<fmt:message key="button.cancel"/>
			        	</button>
					</div>
				</div>
			</fieldset>
			    </form:form> 
    </div>
</div>
			    