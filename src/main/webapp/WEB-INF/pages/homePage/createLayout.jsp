<%@ include file="/common/taglibs.jsp"%>
<script>
$(document).ready(function(){
	 $('#widgetNames').val('${widgetNames}');
});
</script>
<%@ include file="/common/taglibs.jsp"%>
<div class="span12 box">
	<h2>
		<fmt:message key="widget.heading" />
	</h2>
<spring:bind path="layout.*">
        <%@ include file="/common/messages.jsp" %>
</spring:bind>

<div class="span7 scroll">
		<div class="table-create-user">
			<div id="target" class="span10" style="padding-left: 20PX;">

<form:form  id="layout" commandName="layout" method="post" action="bpm/admin/saveLayout" autocomplete="off" modelAttribute="layout" onSubmit="closeSelectDialog('popupDialog');_execute('target','')">
			<form:hidden path="id"/>
			<table>
				<tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="layout.name"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 		 <form:input path="name" id="name" class="medium" onclick="showListViewSelection('ListView', 'single' , 'listView', this, '');" />
			 	</td>
			 </tr>
			  <tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="layout.description"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 		 <form:input path="description" id="description" class="medium"/>
			 	</td>
			 </tr>
			 <tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="layout.noOfWidget"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 	 <form:select path="noOfWidget" name="noOfWidget" id="noOfWidget" class="medium">
								<option value="3" >3</option>
								<option value="2" >2</option>
								<option value="8" >8</option>
								<option value="10" >10</option>
								<option value="12" >12</option>
							</form:select>
			 	</td>
			 </tr>
			   
			 <tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="layout.widgetNames"/>
			 	</td>
			 	 <td width="436" class="uneditable-input1">
			 	 <input type="text" id="widgetNames" name="widgetNames" class="medium white-background" onClick="showWidgetSelection('Widget', 'multi' , 'widgetNames', this, '');" readonly="readonly"/>
			 	</td>
			 </tr>
			  <tr>
			 	 <td width="271" height="40" >
			 		<eazytec:label styleClass="control-label style3 style18" key="layout.status"/>
			 	</td>
			 	  <td width="436" class="uneditable-input1">
						 <form:checkbox id="status" path="status" />
			 	</td>
			 </tr> 
			 <tr>
			 <td></td>
			 	<td>
			<div>
				<input type="submit" value="Submit" class="btn btn-small" onclick="return validateLayoutForm(this.form)"/>
				<input type="button" value="Cancel" class="btn btn-small clearButton mar-L10" onclick="closeSelectDialog('popupDialog');" />
			</div>
			</td>
		</tr>
			    	
			</table>
				
			</form:form>
			</div>
			</div>
			</div>
			</div>
			
