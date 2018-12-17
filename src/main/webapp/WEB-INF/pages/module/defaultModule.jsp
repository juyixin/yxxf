<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
</head>
<spring:bind path="module.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>
<script type="text/javascript">

</script>

<div >
<spring:bind path="module.*">
        <%@ include file="/common/messages.jsp" %>
    </spring:bind>
	<div class="pad-L10 pad-B10">
		<form:form id="module" commandName="module" method="post" action="bpm/module/saveModule" autocomplete="off"	modelAttribute="module" cssClass="form-horizontal" onSubmit="_execute('target','')">
			<form:hidden path="id"/>
		    
			<div class="height60"></div>
			<div id="wizardTab-content">
				<div id="wizardTab-content-1" class="displayBlock">
					<fieldset>
						<legend class="accordion-heading">
						   <%--  <a class="style3 style18" data-toggle="collapse" href="#collapse-personal"><b><fmt:message key="user.personalDetails"/></b></a> --%>
						</legend>
						<div id="collapse-personal" class="accordion-body">
					    	<table>
								<tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="module.name"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 		 <form:input path="name" id="name" class="medium"/>
			 	</td>
			 </tr>
			 <tr>
			   		<td width="271" height="40" class="pad-T8 pad-B8"><eazytec:label styleClass="control-label style3 style18" key="module.description"/></td>
			         <td width="436" class="uneditable-input1 pad-T8 pad-B8"><form:textarea path="description" rows="3" id="description" cols="27" class="medium"/></td>
			  </tr>	
			  
			 <%--  <tr>
				 <td width="271" height="40">
				 	<eazytec:label styleClass="control-label style3 style18" key="module.form"/>
				 </td>
				 <td width="436" class="uneditable-input1">
				 	<div class="uneditable-input1 pad-T8">
							
							<select name="forms" id="forms" multiple="true" class="medium" >
							<c:forEach items="${module.forms}" var="form">
							
									<option value="${form.id}" selected>${form.formName}</option>
								</c:forEach>
							</select>
					</div>
				 </td>
			</tr>    
			  
			  <tr>
				 <td width="271" height="40">
				 	<eazytec:label styleClass="control-label style3 style18" key="module.process"/>
				 </td>
				 <td width="436" class="uneditable-input1">
				 	<div class="uneditable-input1 pad-T8">
							
							<select name="processes" id="processes" multiple="true" class="medium" >
							<c:forEach items="${module.processes}" var="process">
									<option value="${process.id}" selected>${process.name}</option>
								</c:forEach>
							</select>
					</div>
				 </td>
			</tr>  --%>
			  <%-- <tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="module.sortNo"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 		 <form:input path="moduleOrder" id="moduleOrder" class="medium"/>
			 	</td>
			 </tr>
			  <tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="module.isParent"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
						 <form:checkbox id="isParent" path="isParent" />
			 	</td>
			 </tr>
			 <tr id="parentModuleDiv">
				 <td width="271" height="40">
				 	<eazytec:label styleClass="control-label style3 style18" key="module.parentModule"/>
				 </td>
				 <td>
				 	<div class="uneditable-input1">
				 	<select name="parentModule" id="parentModule" multiple="true"  class="medium" >
				 	</select>
							
					</div>
				 </td>
			</tr>
			<tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="module.ownerType"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
					<label class="radio inline">
						 <input type="radio" id="private" name="owner" />
						      <eazytec:label key="module.private"/>
					</label>
					<label class="radio inline">
						<input type="radio" id="public" name="owner" />
						    <eazytec:label  key="module.public"/>
					</label>	            
			 	</td>
			 </tr>
			
			 <tr id="administratorDiv" class="hide" style="line-height:65px;">
			 	 <td width="271" height="40" >
			 		<span class="style3 style18 style21"><eazytec:label styleClass="control-label style3 style18" key="module.administrator"/></span>
			 	 </td>
			 	 <td width="436" class="uneditable-input1">
			 		 <select name="administrators" id="administrators" multiple="true"  class="medium" >
					</select>
			 	</td>
			  </tr>
			  
			   <tr>
				 <td width="271" height="40">
				 	<eazytec:label styleClass="control-label style3 style18" key="module.classification"/>
				 </td>
				 <td width="436" class="uneditable-input1">
				 	<div class="uneditable-input1 pad-T8">
						<input type="text" id="classifications" name="classifications" class="medium white-background" />
					</div>
				 </td>
			</tr>  --%>
			  
							</table>
						</div>
					</fieldset> 
					
				</div>
				
				</div>
		</form:form>
	</div>
</div>

