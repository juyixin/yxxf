
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid padding5">
	<div class="row-fluid">
		<div class="span12">
			<form:form id="layout" commandName="layout" method="post" autocomplete="off" modelAttribute="layout" action="#" onsubmit="_execute('target', '');" >
				<div class="row-fluid control-group" id="layoutDiv" align='center'>
					<div class="span4">
						<div>
					 		 <img src="/images/col_1.jpg" onclick="selectRadio(1)">
						</div>
						<div>
							<form:radiobutton id="one" value="1" path="noOfWidget" name="noOfWidget"></form:radiobutton>
						</div>
					</div>
				
					<div class="span4">
						<div>
					 		 <img src="/images/col_2.jpg" onclick="selectRadio(2)">
						</div>
						<div>
							<form:radiobutton id="two" value="2" path="noOfWidget" name="noOfWidget"></form:radiobutton>
						</div>
					</div>
				
					<div class="span4">
						<div>
					  		<img src="/images/col_3.jpg" onclick="selectRadio(3)">
					  	</div>
					  	<div>
					  		<form:radiobutton id="three" value="3" path="noOfWidget" name="noOfWidget"/>					  		
							
					  	</div>
					</div>
				</div>
				
				<div class="row-fluid control-group" align='center' id="permisisonDiv" class="displayNone">
					<div class="span2">
						<eazytec:label styleClass="control-label style3 style18" key="form.palette.department" />
					</div>
					<div class="span4">
						<input name="layoutDepartments" id="layoutDepartments" class="white-background" onclick="showDepartmentSelection('Department', 'multi' , 'layoutDepartments', this, '');" readonly="readonly" />
					</div>
					<div class="span2">
						<eazytec:label styleClass="control-label style3 style18" key="form.palette.role" />
					</div>
					<div class="span4">
						<input name="layoutRoles" id="layoutRoles" class="white-background" onclick="showRoleSelection('Role', 'multi' , 'layoutRoles', this, '');" readonly="readonly"/>
					</div>
				</div>
				<div class="span12 row-fluid form-actions no-margin" id="LayoutButtonDiv">
					<center>
						<span id="saveButtonDiv">
						<button type="button" class="btn btn-primary"	name="submit" id="selectButton">
							<fmt:message key="button.select" />
						</button>
						</span>
						<span id="cancelButtonDiv" class="">
							<button type="button" id="cancelButton"	class="btn btn-primary"	name="cancel" onClick="closeSelectDialog('popupDialog');">
								<fmt:message key="button.cancel" />
							</button>
						</span>
					</center>
					<div class="clearfix"></div>
				</div>
		 					
				<%-- <div class="form-horizontal no-margin">
							<div class="control-group" >		
								<center>					
									<div class="form-actions no-margin" align="center">	
										<span id="saveButtonDiv">
											<button type="submit" class="btn btn-primary"	name="select" id="selectButton">
												<fmt:message key="button.select" />
											</button>
										</span>
										<span id="cancelButtonDiv" class="">
											<button type="submit" id="cancelButton"	class="btn btn-primary"	name="cancel" onClick="closeSelectDialog('popupDialog');">
												<fmt:message key="button.cancel" />
											</button>
										</span>
										<div class="clearfix"></div>
									</div>
								</center>
							</div>
						</div> --%>
				<%-- <div class="form-horizontal no-margin">
				<div class="row-fluid control-group" id="buttonDiv">
					<div class="span3"></div>
					<div class="span3" align="right">
						<div class="button" id="selectButtonDiv">
							<button type="submit" 
								class="btn btn-primary"
								name="select" id="selectButton">
								<fmt:message key="button.select" />
							</button>
						</div>
					</div>
					<div class="span1"></div>
					<div class="span3">
						<div class="button" id="cancelButtonDiv">
							<button type="submit" id="cancelButton"
								class="btn btn-primary"
								name="cancel" onClick="closeSelectDialog('popupDialog');">
								<fmt:message key="button.cancel" />
							</button>
						</div>
					</div>
				</div></div> --%>
			</form:form>
		</div>
	</div>
</div>
<script type="text/javascript">

$("#selectButton").click(function() {
	var selectedValues = $("input:radio[name=noOfWidget]:checked").val();
	 //setDivHeight("divsample");
	 if(selectedValues == '1'){
			$("#divsample").html("<div class='span12 div-height'   style='border:1px solid gray;'> </div> ");
		}
		else if(selectedValues == '2'){
			$("#divsample").html("<div class='span6 div-height' style='height: 669px; border:1px solid gray;' > </div><div class='span6 div-height' style='height: 669px; border:1px solid gray;'> </div>");
		}else if(selectedValues == '3'){/* setDivHeight("span4"); */
			$("#divsample").html("<div class='span4 div-height' style='border:1px solid gray;'> </div><div class='span4 div-height' style='border:1px solid gray;'> </div><div class='span4 div-height' style='border:1px solid gray;'> </div>");
		}
		setspan12Height();
	document.getElementById("noOfWidget").value = selectedValues;
	$("#departments").val($("#layoutDepartments").val());
	$("#roles").val($("#layoutRoles").val());
	var selected_nodes = $("#layoutTree").jstree("get_selected", null, true); 
	$("#assignedTo").val(selected_nodes.attr("name"));
	$("div#popupDialog").empty();
	$("#popupDialog").dialog("destroy");

});

 $(function(){
	 $("#layoutDiv").css('min-height',200);
	 $("#permisisonDiv").css('min-height',50);
	 $("#buttonDiv").css('min-height',50);
	if('${layoutDepartments}' != null && '${layoutDepartments}' != ""){
		$("#layoutDepartments").val('${layoutDepartments}');	
	}
	if('${layoutRoles}' != null && '${layoutRoles}' != ""){
		$("#layoutRoles").val('${layoutRoles}');	
	}
});
$(document).ready(function() {
   if( '${layoutType}' === 'adminLayout'){
		document.getElementById("permisisonDiv").style.display = 'block';
	}else{
		document.getElementById("permisisonDiv").style.display = 'none';
	}
});
</script>
                
