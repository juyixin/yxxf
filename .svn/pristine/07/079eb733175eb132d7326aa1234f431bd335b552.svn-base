<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
</head>
<spring:bind path="module.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>
<script type="text/javascript">
	function validateModuleForm() {	
		
		 var name = document.getElementById('name').value;
		 //var regex = /^[\-a-zA-Z\_\u4e00-\u9eff\s]{1,100}$/;
		  var regex =/^[\-a-zA-Z\_\u4e00-\u9eff\s]*$/;
		if (!name){
			openMessageBox(form.title.error,form.error.moduleNameRequired,"error", true);
			return false;				
		} else if(!name.match(regex)){
			 $.msgBox({
	   		    title:form.title.message,
	   		    content:form.error.moduleNameValidation
	   		});
			return false;
		}else{
			var name = name.replace(/(^[\s]+|[\s]+$)/g, '');
			$("#name").val(name);
		}
		
		var moduleOrder= $('#moduleOrder').val();
		if (/^[0-9]{1,10}$/.test(+moduleOrder) == false){ // OR if (/^[0-9]{1,10}$/.test(+tempVal) && tempVal.length<=10) 
			 $.msgBox({
		   		    title:form.title.message,
		   		    content:form.error.moduleOrderValidation
		   		});
			 $('#moduleOrder').val("");
			 $("#moduleOrder").focus() ;
			 return false;
		} else if(parseInt(moduleOrder)<= 0){
			$.msgBox({
	   		    title:form.title.message,
	   		    content:form.error.moduleOrderNotZero
	   		});
			 $('#moduleOrder').val("");
			 $("#moduleOrder").focus() ;
			 return false;
		}
}
	
$(document).ready(function(){
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isEdit=selected_nodes.attr("isedit");
	var userRole=$("#currentUserRoles").val().split(",");
	var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
	if(isEdit=='true' || isAdmin){
		$("#saveButtonDiv").show();
	}else{
		$("#saveButtonDiv").hide();
		$("#moduleRoles").prop('disabled', true);
		$("#administratorsFullName").prop('disabled', true);
		$("#moduleDepartments").prop('disabled', true);
		$("#moduleGroups").prop('disabled', true);
		$("#viewAdministratorsFullName").prop('disabled', true);
		$("#viewDepartments").prop('disabled', true);
		$("#viewRoles").prop('disabled', true);
		$("#viewGroups").prop('disabled', true);
		$("#viewDepartments").prop("readonly",true);
		$("#viewAdministrators").prop("readonly",true);
		$("#viewRoles").prop("readonly",true);
		$("#viewGroups").prop("readonly",true);
		$("#moduleOrder").prop("readonly",true);
		$("#description").prop("readonly",true);
		$("#name").prop("readonly",true);
		$("#isPublic").prop("readonly",true);
		$("#isSystemModule").prop("readonly",true);
	}
	var moduleEdit = '${isEdit}' ;
	if(moduleEdit == 'false') {
		$("#saveButtonDiv").hide();
	}
	$('#moduleGroups').val('${groups}');
	   $('#moduleDepartments').val('${departments}');
	   $('#moduleRoles').val('${roles}');
	   
	   $('#administrators').val('${users}');
	   $('#viewAdministrators').val('${viewUsers}');
	   
	   $('#viewGroups').val('${viewGroups}');
	   $('#viewDepartments').val('${viewDepartments}');
	   $('#viewRoles').val('${viewRoles}');	
	   var JSONUsers = JSON.parse('${usersJSON}');
		setUserFullNames("administratorsFullName",$('#administrators').val(),JSONUsers);
		setUserFullNames("viewAdministratorsFullName",$('#viewAdministrators').val(),JSONUsers);

	   
<%--	
 
//Classification Auto complete
	var data = eval ("(" + '${classificationList}' + ")"); 
$("#classificationAutosuggest").autoSuggest(data.items, {
	selectedItemProp: "value", 
	searchObjProps: "value",
	asHtmlID : "classification",
	startText:"Enter Classification",
	emptyText:"No Classification found",
	selectionAdded :function(elem){ 
	},
	preFill :'${classifications}'
	});
	
	   changeWizardByTab();
	   var id = document.getElementById("id").value;
	   if(id){
		   if('${module.departments}'.length != 2){
			   $("#public").attr('checked', true);
			   publicOwner();
		   }else{
			   $("#private").attr('checked', true);
			   privateOwner();
		   }
	   }else{
		   $("#private").attr('checked', true);
		   privateOwner();
	   }
	   
	   if('${module.isParent}' == 'true'){
		  
			 $("#parentModuleDiv").hide();
		 }else{
			 $("#parentModuleDiv").show();
		 }
	   $('#administrators').val('${administrators}');
	   $('#departments').val('${departments}');
	   $('#roles').val('${roles}');
	   $('#classification').val('${classifications}');--%>
		
	});
   
  <%-- $("#isParent").click(function(){	 
		var $this = $(this);
	    if ($this.is(':checked')) {
	    	 document.getElementById("parentModule").innerHTML = "";
	    	$("#parentModuleDiv").hide();
	    	
	    }else{
	    	$("#parentModuleDiv").show();
	    }
	}); --%>
   function selectAll(selection){
	   $(selection).find("option").each(function() {
	       $(this).attr('selected', 'selected');
	});
	  
   }
   <%-- function publicOwner(){
	   document.getElementById("administrators").value = "";
		var administratorDiv = document.getElementById("administratorDiv");
		var departmentDiv = document.getElementById("departmentDiv");
		administratorDiv.className="hide";
		departmentDiv.className="";
	}

	function privateOwner(){
		document.getElementById("departments").value = "";
		var administratorDiv = document.getElementById("administratorDiv");
		var departmentDiv = document.getElementById("departmentDiv");
		administratorDiv.className = "";
		departmentDiv.className="hide";
	}--%>
	
	function moduleSubmit(){
		//Get Classification value from autocomplete and set it
		<%--var n=$(".as-values").val().split(",");
			document.getElementById("classification").value = $(".as-values").val(); --%>
		var params = "screenName="+document.getElementById("screenName").value;
		_execute('target',params);
	}
</script>

<%-- <div class="span12 box " style="overflow: auto;"> 
	<c:if test="${empty module.id}" >
	     <h2><fmt:message key="module.create"/></h2>
	</c:if>
	<c:if test="${not empty module.id}" >
	    <h2><fmt:message key="module.alter"/></h2>
	</c:if>				

<spring:bind path="module.*">
        <%@ include file="/common/messages.jsp" %>
    </spring:bind>
    
	<div style="padding-left:10px;padding-bottom: 10px;">
		<form:form id="module" commandName="module" method="post" action="bpm/module/saveModule" autocomplete="off"	modelAttribute="module" cssClass="form-horizontal" onSubmit="moduleSubmit();">
			<form:hidden path="id"/>
			<!-- <div class="height60"></div> -->
			<form:hidden path="classification"/>
			 <input type="hidden" id="newClassification"/> 
		    
			<div class="wizard-steps pad-L200 ">
				<ul id="wizardTabs">
					<div class="active-step"><a href="#" id="wizardTab-1" class="fontSize13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="module.general"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></div>
					<div class="completed-step"><a href="#" id="wizardTab-2" class="fontSize13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="module.others"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></div>
					</ul>
			</div>
			<div class="height60"></div>
			<div id="wizardTab-content">
				<div id="wizardTab-content-1" class="displayBlock">
					<fieldset>
						<legend class="accordion-heading">
						    <a class="style3 style18" data-toggle="collapse" href="#collapse-personal"><b><fmt:message key="user.personalDetails"/></b></a>
						</legend>
						<div id="collapse-personal" class="accordion-body">
					    	<table>
								<tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="module.name"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 	  <c:if test="${module.name == 'Default Module'}">
			 		 <form:input path="name" id="name" class="medium" readonly="true"/>
			 	  </c:if>
			 	  <c:if test="${module.name != 'Default Module'}">
			 		 <form:input path="name" id="name" class="medium"/>
			 	  </c:if>
			 		 
			 	</td>
			 </tr>
			 <tr>
			   		<td width="271" height="40" class="pad-T8 pad-B8"><eazytec:label styleClass="control-label style3 style18" key="module.description"/></td>
			   		
			         <td width="436" class="uneditable-input1 pad-T8 pad-B8">
			          <c:if test="${module.name == 'Default Module'}">
			 		<form:textarea path="description" rows="3" id="description" cols="27" class="medium" readonly="true"/>
			 	  </c:if>
			 	  <c:if test="${module.name != 'Default Module'}">
			 		<form:textarea path="description" rows="3" id="description" cols="27" class="medium"/>
			 	  </c:if>
			         
			         </td>
			  </tr>	
			  <tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="module.sortNo"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 		 <form:input path="moduleOrder" id="moduleOrder" class="medium"/>
			 	</td>
			 </tr>
			 <tr height="15px;"></tr>
			   <tr>
			  	<td>
			  		<table>
			  			<tr>
				  			<td width="500" height="40" >
				  				<center><span class="style3 style18"><label><fmt:message key="module.isPublic"/></label></span></center>
						 	</td>
						 	 <td width="436" class="uneditable-input1">
						 	 	 <form:checkbox id="isPublic" path="isPublic"/>
						 	</td>
						</tr>
			  		</table>
			  	</td>
			  	<td>
			  		<table>
			  			<tr>
				  			<td width="271" height="40" >
				  				<span class="style3 style18"><label><fmt:message key="module.isSystemModule"/></label></span>
						 	</td>
						 	 <td width="436" class="uneditable-input1">
						 	 	 <form:checkbox id="isSystemModule" path="isSystemModule"/>
						 	</td>
						</tr>
			  		</table>
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
				 	<select name="parentModule" id="parentModule" multiple="true"  class="medium" onclick="showModuleSelection('Module', 'multi' , 'parentModule', this, '');">
				 	<c:forEach items="${module.parentModules}" var="parMod">
									<option value="${parMod.id}" selected>${parMod.name}</option>
					</c:forEach>
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
						 <input type="radio" id="private" name="owner" onclick="privateOwner()"/>
						      <eazytec:label key="module.private"/>
					</label>
					<label class="radio inline">
						<input type="radio" id="public" name="owner" onclick="publicOwner()"/>
						    <eazytec:label  key="module.public"/>
					</label>	            
			 	</td>
			 </tr>
			 	   <tr>
				 <td width="271" height="40">
				 	<eazytec:label styleClass="control-label style3 style18" key="module.classification"/>
				 </td>
				 <td width="436" class="uneditable-input1">
				 	<div class="uneditable-input1 pad-T8">
							<input type="text" id="classificationAutosuggest" class="medium"/>
					</div>
				 </td>
			</tr> 
			   
							</table>
						</div>
					</fieldset> 
					
				</div>
				<div id="wizardTab-content-2" class="displayNone">
					<fieldset>
			    	  	<legend class="accordion-heading">
				          	<a class="style3 style18" data-toggle="collapse" href="#collapse-job"><b><fmt:message key="user.jobDetails"/></b></a>
				     	</legend> 
				     	<div id="collapse-job" class="accordion-body">
				     	<table>
			 </table>
			 <table> 
			 <tr height="25px"></tr>
			<tr>
				<td>
					<table>
						<tr>
							<th colspan="1">Admin Permission</th>
						</tr>
						<tr id="administratorDiv" style="line-height:65px;">
					 	 <td>
					 		<span class="style3 style18 style21"><eazytec:label styleClass="control-label style3 style18" key="module.users"/></span>
					 	 </td>
					 	 <td class="uneditable-input1">
					 		<input type="hidden" id="administrators" name="administrators" class="medium white-background" onclick="showUserSelection('User', 'multi' , 'administrators', this, '');" readonly="readonly"/>
					 		<input type="text" id="administratorsFullName" name="administratorsFullName" class="medium white-background" onclick="showUserSelection('User', 'multi' , 'administrators', document.getElementById('administrators'), '');" readonly="readonly"/>
					 		
					 	</td>
					  </tr>
					  <tr id="departmentDiv">
					 	 <td>
					 		<eazytec:label styleClass="control-label style3 style18" key="module.department"/>
					 	 </td>
					 	 <td class="uneditable-input1">
					 	 	<input type="text" id="moduleDepartments" name="departments" class="medium white-background" onclick="showDepartmentSelection('Department', 'multi' , 'moduleDepartments', this, '');" readonly="readonly"/>
					 	</td>
					  </tr>
					  <tr>
							 <td>
							 	<eazytec:label styleClass="control-label style3 style18" key="module.roles"/>
							 </td>
							 <td class="uneditable-input1">
							 	<div class="uneditable-input1 pad-T8">
									<input type="text" id="moduleRoles" name="roles" class="medium white-background" onclick="showRoleSelection('Role', 'multi' , 'moduleRoles', this, '');" readonly="readonly"/>
								</div>
							 </td>
						</tr>
						<tr>
							 <td>
							 	<eazytec:label styleClass="control-label style3 style18" key="module.groups"/>
							 </td>
							 <td class="uneditable-input1">
							 	<div class="uneditable-input1 pad-T8">
							<input id="moduleGroups" class="medium white-background" type="text" readonly="readonly" onclick="showGroupSelection('Group', 'multi' , 'moduleGroups', this, '');" name="groups">
								</div>
							 </td>
						</tr>
					</table>
				</td>
				<td>
					<table>
							<tr>
								<th colspan="1">View Permission</th>
							</tr>
							<tr id="administratorViewDiv" style="line-height:65px;">
							 	 <td>
							 		<span class="style3 style18 style21"><eazytec:label styleClass="control-label style3 style18" key="module.viewUsers"/></span><!--  module.viewUsers,module.viewDepartment -->
							 	 </td>
							 	 <td class="uneditable-input1">
							 		<input type="hidden" id="viewAdministrators" name="viewAdministrators" class="medium white-background" onclick="showUserSelection('User', 'multi' , 'viewAdministrators', this, '');" readonly="readonly"/>
							 		<input type="text" id="viewAdministratorsFullName" name="viewAdministratorsFullName" class="medium white-background" onclick="showUserSelection('User', 'multi' , 'viewAdministrators', document.getElementById('viewAdministrators'), '');" readonly="readonly"/>							 	
							 	</td>
							  </tr>
							
							 <tr id="departmentDiv">
							 	 <td>
							 		<eazytec:label styleClass="control-label style3 style18" key="module.viewDepartment"/>
							 	 </td>
							 	 <td class="uneditable-input1">
							 	 	<input type="text" id="viewDepartments" name="viewDepartments" class="medium white-background" onclick="showDepartmentSelection('Department', 'multi' , 'viewDepartments', this, '');" readonly="readonly"/>
							 	</td>
							  </tr>
							  
							  <tr>
								 <td>
								 	<eazytec:label styleClass="control-label style3 style18" key="module.viewRoles"/>
								 </td>
								 <td class="uneditable-input1">
								 	<div class="uneditable-input1 pad-T8">
										<input type="text" id="viewRoles" name="viewRoles" class="medium white-background" onclick="showRoleSelection('Role', 'multi' , 'viewRoles', this, '');" readonly="readonly"/>
									</div>
								 </td>
							</tr>
							<tr>
								 <td>
								 	<eazytec:label styleClass="control-label style3 style18" key="module.viewGroups"/><!-- module.viewRolesn,module.viewGroups -->
								 </td>
								 <td class="uneditable-input1">
								 	<div class="uneditable-input1 pad-T8">
								<input id="viewGroups" class="medium white-background" type="text" readonly="readonly" onclick="showGroupSelection('Group', 'multi' , 'viewGroups', this, '');" name="viewGroups">
									</div>
								 </td>
							</tr>
					</table>
				</td>
			</tr>
			  
			
			
			  
			
				    
			<tr>
				 <td width="271" height="40">
				 	<eazytec:label styleClass="control-label style3 style18" key="module.form"/>
				 </td>
				 <td width="436" class="uneditable-input1">
				 	<div class="uneditable-input1 pad-T8">
							
							<select name="forms" id="forms" multiple="true" class="medium" onclick="showFormSelection('Form', 'multi' , 'forms', this, '')">
							<c:forEach items="${module.forms}" var="form">
							
									<option value="${form.id}" selected>${form.formName}</option>
								</c:forEach>
							</select>
					</div>
				 </td>
			</tr>    
			<tr>
				 <td width="271" height="40">
				 	<eazytec:label styleClass="control-label style3 style18" key="module.menu"/>
				 </td>
				 <td width="436" class="uneditable-input1">
				 	<div class="uneditable-input1 pad-T8">
							
							<select name="menus" id="menus" multiple="true" class="medium" onclick="selectAll(this);showTreeList('Select Menu', 'menus', 'multiple','menuTree','popupDialog',this)">
							<c:forEach items="${module.menus}" var="menu">
									<option value="${menu.id}" selected>${menu.name}</option>
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
							
							<select name="processes" id="processes" multiple="true" class="medium" onclick="showProcessSelection('Process', 'multi' , 'processes', this, '')">
							<c:forEach items="${module.processes}" var="process">
									<option value="${process.id}" selected>${process.name}</option>
								</c:forEach>
							</select>
					</div>
				 </td>
			</tr> 
			
			<tr>
			    	<td></td>
			        <td width="436">
				        <div class="button pad-T8" id="saveButtonDiv">
				            <c:if test="${module.name != 'Default Module'}">
						        <c:choose>
						        	<c:when test="${not empty module.id}">
						        		<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="next" onclick="return validateModuleForm(this.form);"  >
					        				<fmt:message key="button.update"/>
					        			</button>
					        		</c:when>
					        		<c:otherwise>
										<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="next" onclick="return validateModuleForm(this.form);"  >
							        		<fmt:message key="button.save"/>
							    		</button>
					    			</c:otherwise>
					    		</c:choose>
				    		</c:if>
				    	</div>
			      </td>
			    </tr>      
			 </table>
			 </form:form>
						</div>
					
				</div> --%>
				
	<!-- new -->
				
		<div class="row-fluid">
			<div class="span12" class="overflow">
				<div class="widget-body">
					<form:form id="module" commandName="module" method="post" action="bpm/module/saveModule" autocomplete="off"	modelAttribute="module" cssClass="form-horizontal no-margin" onSubmit="moduleSubmit();">
						<form:hidden path="id"/>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="module.name"/>
							<div class="controls">
								<c:if test="${module.name == 'Default Module'}">
						 		 	<form:input path="name" id="name" class="span8" readonly="true"/>
						 	  	</c:if>
						 	  	<c:if test="${module.name != 'Default Module'}">
						 		 	<form:input path="name" id="name" class="span8"/>
						 	  	</c:if>
							</div>
						</div>
			
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="module.description"/>
							<div class="controls ">
								<c:if test="${module.name == 'Default Module'}">
						 			<form:textarea path="description" rows="3" id="description" class="span8" readonly="true"/>
						 	  	</c:if>
						 	  	<c:if test="${module.name != 'Default Module'}">
						 			<form:textarea path="description" rows="3" id="description"  class="span8"/>
						 	  </c:if>
							</div>
						</div>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="module.sortNo"/>
							<div class="controls">
								<form:input path="moduleOrder" id="moduleOrder" class="span8"/>
							</div>
						</div>
						
						<div class="span6">
							<label class="control-label" > <fmt:message key="module.isPublic"/> </label>
							<div class="controls checkbox-label">
								<form:checkbox id="isPublic" path="isPublic"/>
							</div>
						</div>
		
						<div class="span6">
							<label class="control-label" > <fmt:message key="module.isSystemModule"/> </label>
							<div class="controls checkbox-label">
								<form:checkbox id="isSystemModule" path="isSystemModule"/>
							</div>
						</div>
			
						
						<div class="span6">
							<div class="title">
								<h5><fmt:message key="module.title.adminPermission"/></h5>
							</div>
							
							<div class="control-group" id="administratorViewDiv">
								<eazytec:label styleClass="control-label" key="module.users"/>
								<div class="controls">
									<input type="hidden" id="administrators" name="administrators" class="span9 white-background" onclick="showUserSelection('User', 'multi' , 'administrators', this, '');" readonly="readonly"/>
					 				<input type="text" id="administratorsFullName" name="administratorsFullName" class="span9 white-background" onclick="showUserSelection('User', 'multi' , 'administrators', document.getElementById('administrators'), '');" readonly="readonly"/>
								</div>
							</div>
							
							<div class="control-group" id="departmentDiv" >
								<eazytec:label styleClass="control-label" key="module.department"/>
								<div class="controls">
									<input type="text" id="moduleDepartments" name="departments" class="span9 white-background" onclick="showDepartmentSelection('Department', 'multi' , 'moduleDepartments', this, '','true');" readonly="readonly"/>
								</div>
							</div>
							
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="module.roles"/>
								<div class="controls">
									<input type="text" id="moduleRoles" name="roles" class="span9 white-background" onclick="showRoleSelection('Role', 'multi' , 'moduleRoles', this,'', 'true');" readonly="readonly"/>
								</div>
							</div>
							
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="module.groups"/>
								<div class="controls">
									<input id="moduleGroups" class="span9 white-background" type="text" readonly="readonly" onclick="showGroupSelection('Group', 'multi' , 'moduleGroups', this,'', 'true');" name="groups">
								</div>
							</div>
						</div>
						
						<div class="span6">
							<div class="title">
								<h5><fmt:message key="module.title.viewPermission"/></h5>
							</div>
							
							<div class="control-group"  id="administratorViewDiv">
								<eazytec:label styleClass="control-label" key="module.viewUsers"/>
								<div class="controls">
									<input type="hidden" id="viewAdministrators" name="viewAdministrators" class="span9 white-background" onclick="showUserSelection('User', 'multi' , 'viewAdministrators', this, '');" readonly="readonly"/>
							 		<input type="text" id="viewAdministratorsFullName" name="viewAdministratorsFullName" class="span9 white-background" onclick="showUserSelection('User', 'multi' , 'viewAdministrators', document.getElementById('viewAdministrators'), '');" readonly="readonly"/>							 	
								</div>
							</div>
						
							<div class="control-group" id="departmentDiv">
								<eazytec:label styleClass="control-label" key="module.viewDepartment"/>
								<div class="controls">
									<input type="text" id="viewDepartments" name="viewDepartments" class="span9 white-background" onclick="showDepartmentSelection('Department', 'multi' , 'viewDepartments', this, '','true');" readonly="readonly"/>
								</div>
							</div>
							
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="module.viewRoles"/>
								<div class="controls">
									<input type="text" id="viewRoles" name="viewRoles" class="span9 white-background" onclick="showRoleSelection('Role', 'multi' , 'viewRoles', this,'', 'true');" readonly="readonly"/>
								</div>
							</div>
						
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="module.viewGroups"/>
								<div class="controls">
									<input id="viewGroups" class="span9 white-background" type="text" readonly="readonly" onclick="showGroupSelection('Group', 'multi' , 'viewGroups', this, '','true');" name="viewGroups">
								</div>
							</div>
						</div>
						<c:if test="${module.name != 'Default Module'}">
						<div class="span12" id="saveButtonDiv">
							<div class="control-group" align="center">
								<div class="form-actions no-margin" style="padding-left: 75px;">
										<c:choose>
							        		<c:when test="${not empty module.id}">
							        			<button type="submit" class="btn btn-primary" name="next" onclick="return validateModuleForm(this.form);"  >
						        					<fmt:message key="button.update"/>
						        				</button>
						        			</c:when>
						        			<c:otherwise>
												<button type="submit" class="btn btn-primary" name="next" onclick="return validateModuleForm(this.form);"  >
							        				<fmt:message key="button.save"/>
							    				</button>
						    				</c:otherwise>
						    			</c:choose>
					    		</div>	
							</div>
						</div>
						</c:if>	
					</form:form>
				</div>
			</div>
		</div>
						

<v:javascript formName="module" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script> 
