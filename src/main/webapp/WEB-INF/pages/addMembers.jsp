<%@ include file="/common/taglibs.jsp"%>
 <form:form id="addMember" method="post" data-remote="true" action="/bpm/manageTasks/addMember" accept-charset="UTF-8" onSubmit="closeSelectDialog('involveUsersPopupDialog'); _execute('target','');" >
 <input name="taskId" type="hidden" value="${taskId}" />
 <input name="type" id="type" type="hidden" value="${type}" />
   <table>
   	<tr>
		<td>
 		 		<p><fmt:message key="task.users"/></p> 
  		 	 	<%-- <select id="userId" name="userId" onclick="usersTreeList('Involve Members', 'userId', 'single')">
  		 	 		<c:if test="${type != 'Involve Members'}">
  		 	 			<option/>
  		 	 		</c:if> 
				<c:forEach items="${availableUsers}" var="userId">
					<option value="${userId.value}">${userId.label}</option>
				</c:forEach>
			</select>		 --%>	
			<input type="text" id="userId" maxlength="50" name="userId" onclick="showUserSelection('User', 'single' , 'userId', this, '')" class="large"/>
			<c:if test="${type == involveMembers}">
				    <p> <fmt:message key="task.roles"/></p> 
				    <div class="controls">
					    <select id="identityLinkType" name="identityLinkType" class="large">
				        	<c:forEach items="${availableRtTaskRoles}" var="identityLinkType">
						    	 <option value="${identityLinkType.value}">${identityLinkType.label}</option>
						    </c:forEach>
					    </select>
				    </div>
			</c:if>
		</td>
	</tr> 
	<tr></tr>
	<tr>
		<td colspan="2" align="center" class="pad-T20">
		
			<c:if test="${type == involveMembers}">
				<button type="submit" name="submit" class="btn btn-small" onclick="return validateAddMembers(this.form);">Submit</button>
			</c:if>

			<c:if test="${type != involveMembers}">
				<button type="submit" name="submit" class="btn btn-small">Submit</button>
			</c:if>
		</td>
	</tr> 
  </table>  
</form:form>