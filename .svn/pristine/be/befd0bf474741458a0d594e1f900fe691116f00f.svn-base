<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="createTable.title"/></title>
    <meta name="heading" content="<fmt:message key='createTable.title'/>"/>
</head>

    <script language="javascript">
  
    getTabelsByParentTable("#importTableName");
    $("#importTableName").css({"width":"220px"});
    var select = document.getElementById("importTableName");
    for(var i=0;i<select.options.length;i++){
    	select.options[i].style.width="220px";
    }
    function loadColumnNames(){
    if($("#importTableName option:selected").text().length < 30){
    	document.getElementById("importTableName").style.width="220px";
    } else {
    	document.getElementById("importTableName").style.width=$("#importTableName option:selected").text().length+"0px";
    }
    	
    	var tableId=$("#importTableName").val() ;
    }
    </script>



<body>
	<div class="row-fluid">
		<!-- <div class="span12"> -->
		<form:form id="csvDumpForm" method="post" style="padding-top:20px"
			cssClass="form-horizontal" action="#" enctype="multipart/form-data"
			onsubmit="setTimeout(function() {closeSelectDialog('importCSVPopupDialog');}, 2);_execute('rightPanel', '');">
			<div class="control-group">
				<eazytec:label styleClass="control-label" key="table.name" />
				<div class="controls">
					<select name="importTableName" id="importTableName" style="width:220px" class="medium" onchange="loadColumnNames();"></select>
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label" key="table.location" />
				<div class="controls">
					<input type="file" name="file" id="csvDumpfile"
						class="medium bpm_import_file" onchange="loadColumnNames();"
						multiple="multiple" />
				</div>
			</div>
			<div class="control-group">
				<div class="form-actions no-margin">
					<button type="submit" class="btn btn-primary" name="save"
						onclick="return checkDumpFilePath('#csvDumpfile','XLS','bpm/table/importCSV','csvDumpForm');"
						id="fileSubmit">
						<fmt:message key="table.button.import" />
					</button>
					<button type="button" class="btn btn-primary " name="next"
						onclick="closeSelectDialog('importCSVPopupDialog');"
						id="cancelButton" style="cursor: pointer;">
						<fmt:message key="button.cancel" />
					</button>
					<div class="clearfix"></div>
				</div>
			</div>
		</form:form>
	</div>
</body>
<%-- <table width="100%">	
		<tr>
			<td class="fontSize14 pad-R20" align="right">Table Name &nbsp;&nbsp;&nbsp;&nbsp;</td>	
			<td ><div class="pad-T20"><select name="importTableName" id="importTableName" class="medium" onchange="loadColumnNames();"></select></div></td>
		</tr>
		<tr>
			<td class="fontSize14" align="right">Choose Location &nbsp;&nbsp;</td>	
			<td ><div style="width:320px;"  class="pad-T20">	 <input type="file" name="file" id="csvDumpfile" multiple="multiple" class="bpm_import_file" size="23"/></div></td>
		</tr>
		<tr>							
			<td ></td><td>
			<div align="left" class="pad-T20">
				<input type="submit" value="<fmt:message key="table.button.import"/>" id="fileSubmit" class="btn btn-primary clearButton mar-L10 pad-B3 height25" onclick="return checkDumpFilePath('#csvDumpfile','XLS','bpm/table/importCSV','csvDumpForm');" />
				<input type="button" value="Cancel" class="btn btn-primary clearButton mar-L10 pad-B3 height25" onclick="closeSelectDialog('importCSVPopupDialog');" />
			</div>
			</td>
		</tr>						
	</table> --%>
			
	<!-- </div> -->
