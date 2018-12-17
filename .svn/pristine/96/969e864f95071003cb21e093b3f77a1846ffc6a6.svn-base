<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="createTable.title"/></title>
    <meta name="heading" content="<fmt:message key='createTable.title'/>"/>
</head>

    <script language="javascript">
    
	   addRow('dataTable');

    function addRow(tableID) {
    	var table = document.getElementById(tableID);
        var rowCount = table.rows.length;
	 	var row = table.insertRow(rowCount);

        var cell1 = row.insertCell(0);
        var element1 = document.createElement("input");
        cell1.style.padding="3px";
        element1.type = "text";
        element1.style.width = "175px";
        cell1.appendChild(element1);
        
        var cell2 = row.insertCell(1);
        var element2 = document.createElement("input");
        cell2.style.padding="3px";
        element2.type = "text";
        element2.style.width = "175px";
        cell2.appendChild(element2);
        
        var cell3 = row.insertCell(2);
        var element3 = document.createElement("select");
        cell3.style.padding="3px";
        element3.style.width = "135px";
        element3.style.marginBottom = "0px";
        var fieldTypes=["VARCHAR","INT","TIMESTAMP","BOOLEAN","TINYINT","SMALLINT","MEDIUMINT","FLOAT","DOUBLE","DECIMAL","NUMERIC","DATE","DATETIME","TIME","YEAR","CHAR","TINYBLOB","TINYTEXT","BLOB","TEXT","MEDIUMBLOB","MEDIUMTEXT","LONGBLOB","LONGTEXT"];
        
        for (var i=0;i<fieldTypes.length;i++){
			var option;
			option = document.createElement("option");
			option.setAttribute("value", fieldTypes[i]);
			option.innerHTML = fieldTypes[i];
			element3.appendChild(option);
	    }
        cell3.appendChild(element3);
        
        
        var cell4 = row.insertCell(3);
        var element4 = document.createElement("input");
        cell4.style.padding="3px";
        element4.style.width = "30px";
        element4.type = "text";
       	cell4.appendChild(element4);
       	
        var cell5 = row.insertCell(4);
        var element5 = document.createElement("input");
        cell5.style.padding="3px";
        element5.style.width = "75px";
        element5.type = "text";
       	cell5.appendChild(element5);
        
        var cell6 = row.insertCell(5);
        var element6 = document.createElement("img");
        cell6.style.padding="3px";
        element6.setAttribute("src", "/images/add.gif");
        element6.setAttribute("onClick", "");
        cell6.appendChild(element6);
       
        var cell7 = row.insertCell(6);
        var element7 = document.createElement("img");
        cell7.style.padding="3px";
        element7.setAttribute("src", "/images/delete.gif");
        element7.setAttribute("onClick", "");
        cell7.appendChild(element7);
        
 	}
    
    $(document).ready(function(){
    	$('#modules').val('${moduleName}');
      	 if('${isModuleChanged}' == 'true'){
      		 var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
      		 $("#module_relation_tree").jstree("deselect_node", selected_nodes);
      		 
      		 $("#module_relation_tree").jstree("remove","#"+'${metaTable.id}_${tableModuleId}');

      		$("#module_relation_tree").jstree("create", $("#"+'${tableModuleId}'+"Tables"), false, {attr : {id: '${metaTable.id}_${tableModuleId}', name: "${metaTable.tableName}",moduleid:"${tableModuleId}",nodeid:"${metaTable.id}",isedit:'true',isSystemModule:"${isSystemModule}",nodetype:'table'}, data: "${metaTable.tableName}",metadata: {id: "${metaTable.id}",name:"${metaTable.tableName}",nodeid:"${metaTable.id}"}}, false, true);
   			$("#module_relation_tree").jstree("select_node", '#'+'${metaTable.id}_${tableModuleId}');
      	} 
    });
		</script>
		 <%@ include file="/common/messages.jsp" %>
<div class="span12 box "> 
<h2 id="createTableHeader" style="display: none;"><fmt:message key="createTable.title"/></h2>
 <div class="span7 scroll">
		<div>
			   <form:form id="metaTable" commandName="metaTable" method="post" action="bpm/table/saveTable" autocomplete="off" modelAttribute="metaTable" onclick="_execute('target', '');">
			  <form:hidden path="id"/>
			   <input type="hidden" name="fieldPropertiesList" id="fieldPropertiesList"/>
			   <input type="hidden" name="tableDefaultValues" id="tableDefaultValues" value="<fmt:message key='table.default.values'/>"/>
			    <table width="100%">
			     <tr height="13px"></tr>
			    <tr id="tableNameRow">
			        <td width="271" height="40"><eazytec:label styleClass="control-label style3 style18 " key="metaTable.tableName"/></td>
			        
			        <c:if test="${empty metaTable.id}" >
		        	<td width="436" class="uneditable-input1"><form:input path="tableName" id="tableName" class="medium"/></td>
					</c:if>
					
					<c:if test="${not empty metaTable.id}" >
					<td width="436" class="uneditable-input1"><form:input path="tableName" readonly="true" id="tableName" class="medium"/></td>
					</c:if>
			        
			    </tr>
			      <tr id="chineseNameRow">
			      	<td width="271" height="40"><eazytec:label styleClass="control-label style3 style18 " key="metaTable.chineseName"/></td>
			         <td width="436" class="uneditable-input1"><form:input path="chineseName" id="chineseName" class="medium"/></td>
		        </tr>
       			 <tr id="descriptionRow">
       			 	<td width="271" height="40" ><eazytec:label styleClass="control-label style3 style18 " key="metaTable.description"/></td>
			         <td width="436" class="uneditable-input1"><form:textarea path="description" id="description" cols="18" class="medium" rows="3"/></td>
			    </tr>
			    <tr height="10px;"></tr>
			    <tr>
			    	<td width="271" id="fieldPropertiesLableId" height="40"><eazytec:label styleClass="control-label style3 style18 " key="table.field.properties"/></td>
			        <td colspan="2">
			        		<TABLE cellpadding="1" cellspacing="1"  width="100%">
				        		 <TR>
				        			 <td width="120px" height="30"><center><span class="style18 style3"><label><fmt:message key="table.field.name"/><span class="required">*</span></label></span></center></td>
				        			  <td width="120px" height="30"><center><span class="style18 style3"><label><fmt:message key="metaTable.chineseName"/><span class="required">*</span></label></span></center></td>
						             <td width="70px"  height="30"><center><span class="style18 style3"><label><fmt:message key="table.field.type"/></label></span></center></td>
						             <td width="30px" height="30" ><center><span class="style18 style3"><label><fmt:message key="table.field.length"/></label></span></center></td>
						             <td width="40px" height="30" style="padding-right:34px; "><center><span class="style18 style3"><label><fmt:message key="table.field.default.value"/></label></span></center></td>
						        </TR>
			        		</Table>
			        		<TABLE id="dataTable" cellpadding="1" cellspacing="1"  width="100%" border="1"></TABLE>
			        </td>
			    </tr>
			    <tr height="20px"></tr>
			    
			  <!--  <tr height="20px"></tr>
			    <tr>
			   		<td colspan='3'><span class="style18 style3"><font color="#D58000">Note: Initial UI design and Functionality was added.</font></span></td>
			    </tr> -->
			</table>  
			 </form:form> 
		</div>
	</div> 
</div>
<%-- </page:applyDecorator> --%>