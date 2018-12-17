<%-- <%@ page session="true" import="com.eazytec.model.Report" %> --%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$( "#classification" ).each(function( index ) {
	
	var element_id = this.getAttribute("id");
	var parent_dictId = "ff808181456e16a001456e1c29650000";
	if('${report.id}'!= ''){
		addOptionValToReport(element_id,parent_dictId);
		document.getElementById("classification").value = '${report.classification}';
	}else{
		addOptionValToReport(element_id,parent_dictId);
	}
 
	});
if('${report.classification}' != '' && '${report.classification}'.length > 0){
	document.getElementById("classification").value = '${report.classification}';
}
</script>

<div class="page-header">
	<c:if test="${report.id != null}">	
		<h2><fmt:message key="report.update"/></h2>
	</c:if>
	<c:if test="${report.id == null}">	
		<h2><fmt:message key="report.create"/></h2>
	</c:if>
</div>
<div align="right">
  	
	<strong><a id="backToPreviousPage"
		style="text-decoration: underline; padding: 10px;"
		href="javascript:void(0);" data-role="button" data-theme="b"
		onClick="showListViews('REPORT_NO_ROLE', 'Manage Reports');"><button class="btn"><fmt:message key="button.back" /></button></a> </strong>
  
</div>

<div class="height10"></div>
	<spring:bind path="report.*">
		<%@ include file="/common/messages.jsp"%>
	</spring:bind>

	<div class="span12">
	<div class="row-fluid">
				<div class="widget-body">
					<div class="box_main">
				<!-- <div style="padding-left: 100px;"> -->
				<form:form commandName="report" method="post"
					action="bpm/report/saveReport" id="reportForm"
					cssClass="form-horizontal" onSubmit="_execute('target','')">

					<c:if test="${report.id != null}">
						<form:hidden path="id" />
					</c:if>


					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.name" />
						<div class="controls">
							<form:input path="name" id="name" readonly="false" class="span6" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="report.classification" />
						<div class="controls">
							<form:select path="classification" id="classification"
								class="span6" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.url" />
						<div class="controls">
							<form:input path="reportUrl" name="reportUrl" id="reportUrl" readonly="false" 
								class="span6" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.roles" />
						<div class="controls">
							<form:input path="roles" id="roles" readonly="readonly" 
								class="span6"
								onclick="showRoleSelection('Role', 'multi' , 'roles', this, '');"
								name="roles" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.departments" />
						<div class="controls">
							<form:input path="departments" id="departments"
								readonly="readonly" class="span6"
								onclick="showDepartmentSelection('Department', 'multi' , 'departments', this, '');"
								name="departments" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.groups" />
						<div class="controls">
							<form:input path="groups" id="groups" readonly="readonly"
								class="span6"
								onclick="showGroupSelection('Group', 'multi' , 'groups', this, '');"
								name="groups" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.users" />
						<div class="controls">
							<form:input path="users" id="users" readonly="readonly"
								class="span6"
								onclick="showUserSelection('User', 'multi' , 'users', this, '');"
								name="users" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.status" />
						<div class="controls">
							<label class="radio inline"> <form:radiobutton
									path="status" name="status" id="status" value="active"
									checked="true" onclick="" /><fmt:message key="report.status.active"/>
							</label> <label class="radio inline"> <form:radiobutton
									path="status" name="status" id="status" value="inactive"
									onclick="" /><fmt:message key="report.status.inactive"/>
							</label>
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="report.description" />
						<div class="controls">
							<form:textarea path="description" id="description" class="span6" />
						</div>
					</div>



					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${report.id != null}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
							<button type="button" class="btn btn-primary " name="next"
								onclick="showListViews('REPORT_NO_ROLE', 'Manage Reports');" id="cancelButton"
								style="cursor: pointer;"><i class="icon-remove icon-white"></i>
								<fmt:message key="button.cancel" />
							</button>
							<div class="clearfix"></div>
						</div>
					</div>
				</form:form>
				<!-- </div> -->
			</div>
		</div>
	</div>
</div>
<!-- <script type="text/javascript">

function checkUrlValue() {
	var urlValue = document.getElementById("reportUrl").value;

	var regex = new RegExp("(https?:\/\/(?:www\.|(?!www))[^\s\.]+\.[^\s]{2,}|www\.[^\s]+\.[^\s]{2,})");
        if (regex.test(urlValue)) {
            
            return true;
        } 
           $.msgBox({
                title : form.title.message,
                content : "Please Enter Valid URL",
                buttons : [ {
                    value : "Ok"
                } ],
                success : function(result) {
                    if (result == "Ok") {
                       // $("#cycleValue").val('0');
                        $("#reportUrl").focus();
                    }
                }
            });			
            return false;

}


</script> -->

<%-- </page:applyDecorator> --%>
	<v:javascript formName="report" staticJavascript="false" />
	<script type="text/javascript"
		src="<c:url value="/scripts/validator.jsp"/>"></script>
