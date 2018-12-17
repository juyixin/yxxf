<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
    <title><fmt:message key="emailConfig.title"/></title>
    <meta name="heading" content="<fmt:message key='emailConfig.title'/>"/>
</head>
<script type="text/javascript">
$(function() {
	changeWizardByTab();
});
</script>

<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="emailConfig.title"/></h2>
	</div>
	<spring:bind path="emailConfig.*">
        <%@ include file="/common/messages.jsp" %>
    </spring:bind>

	<div class="height10"></div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main" style="padding-top:0px;">
				<form:form id="emailConfig" commandName="emailConfig" method="post"
					action="/bpm/mail/jwma/saveMailConfig" autocomplete="off"
					modelAttribute="emailConfig" onSubmit="_execute('target','')">
					<form:hidden path="id" />
					<div class="row-fluid">
						<div class="span12">
							<div class="widget">
								<div class="widget-body">
									<div id="wizard">
										<ol>
											<li><fmt:message key="emailConfig.mta" /></li>
											<li><fmt:message key="emailConfig.postoffice" /></li>
										</ol>
										<div class="well">
											<div class="form-horizontal no-margin">

												<%-- <div class="control-group">
								<eazytec:label styleClass="control-label" key="emailConfig.mtaName"/>
								<div class="controls">
									<form:input path="mtaName" id="mtaName" class="span6"/>
								</div>
							</div> --%>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.mtaProtocol" />
													<div class="controls">
														<form:input path="mtaProtocol" id="mtaProtocol"
															class="span6" />
													</div>
												</div>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.mtaAddress" />
													<div class="controls">
														<form:input path="mtaAddress" id="mtaAddress"
															class="span6" />
													</div>
												</div>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.mtaPort" />
													<div class="controls">
														<form:input path="mtaPort" id="mtaPort" class="span6" />
													</div>
												</div>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.mtaSecure" />
													<div class="controls checkbox-label">
														<form:checkbox path="mtaSecure" id="mtaSecure" />
													</div>
												</div>

												<%-- <div class="control-group">
								<eazytec:label styleClass="control-label " key="emailConfig.mtaAuthenticated"/>
								<div class="controls">
									 <form:checkbox path="mtaAuthenticated" id="mtaAuthenticated" class="span6"/>
								</div>
							</div> --%>
											</div>
										</div>

										<div class="well">
											<div class="form-horizontal no-margin">

												<%-- <div class="control-group">
								<eazytec:label styleClass="control-label style3 style18" key="emailConfig.poName"/>
								<div class="controls">
									<form:input path="poName" id="poName" class="medium"/>
								</div>
							</div> --%>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.poProtocol" />
													<div class="controls">
														<form:input path="poProtocol" id="poProtocol"
															class="span6" />
													</div>
												</div>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.poAddress" />
													<div class="controls">
														<form:input path="poAddress" id="poAddress" class="span6" />
													</div>
												</div>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.poPort" />
													<div class="controls">
														<form:input path="poPort" id="poPort" class="span6" />
													</div>
												</div>

												<div class="control-group">
													<eazytec:label styleClass="control-label"
														key="emailConfig.poSecure" />
													<div class="controls checkbox-label">
														<form:checkbox path="poSecure" id="poSecure" />
													</div>
												</div>

												<%-- <div class="control-group">
								<eazytec:label styleClass="control-label style3 style18" key="emailConfig.poType"/>
								<div class="controls">
									 <form:input path="poType" id="poType" class="medium"/>
								</div>
							</div>
	
							<div class="control-group">
								<eazytec:label styleClass="control-label style3 style18" key="emailConfig.poRootFolder"/>
								<div class="controls">
									 <form:input path="poRootFolder" id="poRootFolder" class="medium"/>
								</div>
							</div>
	
							<div class="control-group">
								<eazytec:label styleClass="control-label style3 style18" key="emailConfig.poDefault"/>
								<div class="controls">
									<form:checkbox path="poDefault" id="poDefault" class="medium"/>
								</div>
							</div>
							
							<div class="control-group">
								<eazytec:label styleClass="control-label style3 style18" key="emailConfig.poReplyToDomain"/>
								<div class="controls">
									<form:input path="poReplyToDomain" id="poReplyToDomain" class="medium"/>
								</div>
							</div>	 --%>
											</div>
										</div>
									</div>

									<div>
										<div class="form-horizontal form-actions no-margin">
											<div class="control-group">
												<div class="controls">
													<button type="submit" class="btn btn-primary" name="save"
														id="save"><i class="icon-ok icon-white"></i>
														<c:choose>
															<c:when test="${not empty emailConfig.id}">
																<fmt:message key="button.update" />
															</c:when>
															<c:otherwise>
																<fmt:message key="button.save" />
															</c:otherwise>
														</c:choose>
													</button>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
	$("#wizard").bwizard();
	$(".bwizard-buttons").remove();

	var newHeight = $("#target").height();
	var wizardHeight = (parseInt(newHeight)/2) -20;
	$('#wizard').css({height: wizardHeight, overflow: 'auto'});
</script>