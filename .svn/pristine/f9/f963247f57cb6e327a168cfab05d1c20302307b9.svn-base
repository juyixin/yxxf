<%@ include file="/common/taglibs.jsp"%>

<div class="page-header">
	<div class="pull-left">
		<h2><fmt:message key="sysconfig.title" /></h2>
	</div>
	<div align="right">
		<a class="padding10" style="text-decoration: underline;"
			id="backToPreviousPage" href="javascript:void(0);" data-role="button"
			data-theme="b" onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message
					key="button.back" /></button></a>
	</div>

</div>	
	<spring:bind path="sysConfig.*">
		<%@ include file="/common/messages.jsp"%>
	</spring:bind>
	
<!-- 	<div class="row-fluid">
		<div class="span12">
			<div class="widget span12">
				<div class="widget-body span12"> -->

				<div class="span12">
					<div class="row-fluid">
						<div class="widget-body">
							<div class="box_main">
								<form:form id="sysConfigForm" commandName="sysConfig"
									method="post" class="form-horizontal no-margin"
									action="bpm/admin/saveSysConfig" autocomplete="off"
									modelAttribute="sysConfig" onSubmit="_execute('target','')">
									<form:hidden path="id" />

									<div class="control-group">
										<eazytec:label styleClass="control-label"
											key="sysConfig.selectKey" />
										<div class="controls">
											<c:if test="${mode != 'edit'}">
												<form:input path="selectKey" id="selectKey" class="span6"
													depends="required" />
												</td>
											</c:if>
											<c:if test="${mode == 'edit'}">
												<form:input path="selectKey" id="selectKey" readonly="true"
													class="span6" />
											</c:if>
										</div>
									</div>

									<div class="control-group">
										<eazytec:label styleClass="control-label"
											key="sysConfig.selectType" />
										<div class="controls">
											<c:if test="${mode != 'edit'}">
												<form:select path="selectType" name="selectType"
													id="selectType" class="span6" onChange="onChangeValue()">
													<option value="Integer"
														${fn:contains(sysConfig.selectType, 'Integer') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.integer" />
													</option>

													<option value="Boolean"
														${fn:contains(sysConfig.selectType, 'Boolean') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.boolean" />
													</option>

													<option value="String"
														${fn:contains(sysConfig.selectType, 'String') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.string" />
													</option>
													<option value="Password"
														${fn:contains(sysConfig.selectType, 'String') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.password" />
													</option>
												</form:select>
											</c:if>
											<c:if test="${mode == 'edit'}">
												<form:select path="selectType" name="selectType"
													id="selectType" disabled="true" class="span6"
													onChange="onChangeValue()">
													<option value="Integer"
														${fn:contains(sysConfig.selectType, 'Integer') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.integer" />
													</option>

													<option value="Boolean"
														${fn:contains(sysConfig.selectType, 'Boolean') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.boolean" />
													</option>

													<option value="String"
														${fn:contains(sysConfig.selectType, 'String') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.string" />
													</option>
													<option value="Password"
														${fn:contains(sysConfig.selectType, 'Password') ? 'selected' : ''}>
														<fmt:message key="sysconfig.type.password" />
													</option>
												</form:select>
												<form:hidden id="selectType" path="selectType"
													value="${sysConfig.selectType}" />
											</c:if>
										</div>
									</div>

									<div class="control-group">
										<eazytec:label styleClass="control-label"
											key="sysConfig.selectValue" />
										<script>
									function onChangeValue(){
										var selectType = document.getElementById("selectType");
									
										var selectTypeValue = selectType.options[selectType.selectedIndex].value;
										//if('${sysConfig.id}' != null && '${sysConfig.id}' != ''){
											if(selectTypeValue == "Boolean"){
												$("#selectedValue").html('<form:select	path="selectValue" id="selectValue" class="span6"><option value="true"${fn:contains(sysConfig.selectValue, "true") ? "selected" : ""}>True</option><option value="false"	${fn:contains(sysConfig.selectValue, 'false') ? "selected" : ""}>False</option></form:select>');
											}else if(selectTypeValue == "Password"){
												$("#selectedValue").html('<form:password	showPassword="true" path="selectValue" id="selectValue" class="span6"></form:password>');
											}else{
												$("#selectedValue").html('<form:input	path="selectValue" id="selectValue" class="span6"></form:input>');
											}	
										//}
									}
								</script>
										<div class="controls" id="selectedValue">
											<c:if test="${sysConfig.id != null}">
												<c:if test="${sysConfig.selectType == 'Boolean'}">
													<form:select path="selectValue" id="selectValue"
														class="span6">
														<option value="true"
															${fn:contains(sysConfig.selectValue, 'true') ? 'selected' : ''}>
															<fmt:message key="sysconfig.boolean.true" />
														</option>
														<option value="false"
															${fn:contains(sysConfig.selectValue, 'false') ? 'selected' : ''}>
															<fmt:message key="sysconfig.boolean.false" />
														</option>
													</form:select>
												</c:if>
												<c:if test="${sysConfig.selectType == 'Password'}">
													<form:password showPassword="true" path="selectValue"
														id="selectValue" class="span6"></form:password>
												</c:if>
												<c:if
													test="${sysConfig.selectType != 'Boolean' && sysConfig.selectType != 'Password' }">
													<form:input path="selectValue" id="selectValue"
														class="span6"></form:input>
												</c:if>
											</c:if>
											<c:if test="${sysConfig.id == null}">
												<c:if test="${sysConfig.selectType == 'Boolean'}">
													<form:select path="selectValue" id="selectValue"
														class="span6">
														<option value="true"
															${fn:contains(sysConfig.selectValue, 'true') ? 'selected' : ''}>
															<fmt:message key="sysconfig.boolean.true" />
														</option>
														<option value="false"
															${fn:contains(sysConfig.selectValue, 'false') ? 'selected' : ''}>
															<fmt:message key="sysconfig.boolean.false" />
														</option>
													</form:select>
												</c:if>
												<c:if test="${sysConfig.selectType == 'Password'}">
													<form:password showPassword="true" path="selectValue"
														id="selectValue" class="span6"></form:password>
												</c:if>
												<c:if
													test="${sysConfig.selectType != 'Boolean' && sysConfig.selectType != 'Password'}">
													<form:input path="selectValue" id="selectValue"
														class="span6"></form:input>
												</c:if>
											</c:if>
										</div>
									</div>

									<div class="control-group">
										<eazytec:label styleClass="control-label"
											key="sysConfig.active" />
										<div class="controls checkbox-label">
											<!--<form:checkbox path="" id="" name ="activeSelect" checked="true" value="true"/>-->
											<form:checkbox id="activeSelect" path="systemDefined"
												disabled="${sysConfig.systemDefined}" />
											<c:if test="${sysConfig.systemDefined == true}">
												<form:hidden id="activeSelect" path="systemDefined"
													value="true" />
											</c:if>
										</div>
									</div>

									<div class="control-group">
										<div class="form-actions no-margin">
											<button type="submit" class="btn btn-primary" name="save"
												onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
												<c:choose>
													<c:when test="${sysConfig.id != null}">
														<fmt:message key="button.update" />
													</c:when>
													<c:otherwise>
														<fmt:message key="button.save" />
													</c:otherwise>
												</c:choose>
											</button>
											&nbsp;&nbsp;
											<button type="button" class="btn btn-primary cursor_pointer"
												name="next" onclick="backToPreviousPage()" id="cancelButton"><i class="icon-remove icon-white"></i>
												<fmt:message key="button.cancel" />
											</button>
											<div class="clearfix"></div>
										</div>
									</div>
								</form:form>
							</div>
						</div>
					</div>
				</div>

				<v:javascript formName="sysConfig" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>
