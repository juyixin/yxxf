<%@ include file="/common/taglibs.jsp"%>

<spring:bind path="listView.*">
        <%@ include file="/common/messages.jsp" %>
</spring:bind>

<form:form commandName="listView" method="post" action="bpm/listView/saveListView" id="listView" autocomplete="off" 
		   cssClass="form-horizontal" enctype="multipart/form-data" onSubmit="_execute('target','')">
    <form:hidden path="id"/> 
    <div style="padding-left:10px;padding-bottom: 10px;"/>
	<fieldset>
		<div id="collapse-personal" class="accordion-body">
			<table width="73%">
				<tr>									
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.viewName"/>
				            <div class="controls">
					            <c:if test="${empty listView.id}">
					                <form:input path="viewName" id="viewName" class="large"/>
					            </c:if>
					            <c:if test="${not empty listView.id}">
					                <form:input path="viewName" id="viewName" readonly="true" class="large"/>
					            </c:if>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.module"/>
				            <div class="controls">
					                <input type="text" autocomplete="off" list="searchModuleresults" value='' id="module" class="large" name="_moduleName" onblur="setFormModuleDetails('module.Id','false')">
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.pageSize"/>
				            <div class="controls">
					                <form:input path="pageSize" id="pageSize" class="large" value="15"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.isFilterDuplicateData"/>
				            <div class="controls">
				            		<form:checkbox id="isFilterDuplicateData" path="isFilterDuplicateData"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.isShowSearchBox"/>
				            <div class="controls">
					               <form:checkbox id="isShowSearchBox" path="isShowSearchBox"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.isNeedCheckbox"/>
				            <div class="controls">
				            		<form:checkbox id="isNeedCheckbox" path="isNeedCheckbox"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.selectColumns"/>
				            <div class="controls">
					            <form:textarea path="selectColumns" id="selectColumns" rows="3" cols="45" style="width:86%"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.fromQuery"/>
				            <div class="controls">
					            <form:textarea path="fromQuery" id="fromQuery" rows="3" cols="45" style="width:86%"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.whereQuery"/>
				            <div class="controls">
					            <form:textarea path="whereQuery" id="whereQuery" rows="3" cols="45" style="width:86%"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.orderBy"/>
				            <div class="controls">
					            <form:textarea path="orderBy" id="orderBy" rows="2" cols="45" style="width:86%"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset class="control-group">
				            <eazytec:label styleClass="control-label style3 style18 " key="listView.groupBy"/>
				            <div class="controls">
					            <form:textarea path="groupBy" id="groupBy" rows="2" cols="45" style="width:86%"/>
				            </div>
			        	</fieldset>
					</td>
				</tr>
			</table>
		</div>
	</fieldset> 
</form:form> 

