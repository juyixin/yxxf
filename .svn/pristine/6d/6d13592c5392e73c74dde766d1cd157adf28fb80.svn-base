<spring:bind path="listView.*">
    <%@ include file="/common/messages.jsp" %>
</spring:bind>

<div class="container-fluid" id="parent_container"  style="overflow-y: auto;">			
<form:form commandName="listView" name="listView" method="post" action="bpm/listView/saveListView" id="listView" autocomplete="off" cssClass="form-horizontal" enctype="multipart/form-data">
	<form:hidden path="id" />
	<div>
		<div id="collapse-personal" class="accordion-body">

			<div class="control-group" id="list_view_template_row">
				<eazytec:label styleClass="control-label " key="listView.template" />
				<div class="controls">
					<select class="span6" id="list_view_template" onChange="showListViewTemplateData(this.value);"></select>
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label" key="listView.viewName" />
				<div class="controls">
					<c:if test="${empty listView.id || isListViewTemplate == true}">
						<form:input path="viewName" id="viewName" class="span6" />
					</c:if>
					<c:if test="${not empty listView.id && isListViewTemplate == false}">
						<form:input path="viewName" id="viewName" readonly="true" class="span6" />
					</c:if>
				</div>
			</div>
			<div class="control-group">
				<eazytec:label styleClass="control-label " key="listView.viewTitle" />
				<div class="controls">
					<form:input path="viewTitle" id="viewTitle" class="span6"/>
				</div>
			</div>
			<div class="control-group">
				<eazytec:label styleClass="control-label " key="listView.module" />
				<div class="controls">
					<input type="text" autocomplete="off" list="searchModuleresults" value="${moduleName}" id="module" class="span6" name="_showmoduleName" onblur="setFormModuleDetailsByFieldName('module_Id','false','_showmoduleName','true')">
					<datalist id="searchModuleresults" />
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label " key="listView.pageSize" />
				<div class="controls">
					<form:input path="pageSize" id="pageSize" class="span6" value="15" />
				</div>
			</div>
			<div class="span12">
				<div class="span4">
					<div class="control-group">
						<eazytec:label styleClass="control-label " key="listView.isShowSearchBox" />
						<div class="controls checkbox-label">
							<form:checkbox id="isShowSearchBox" path="isShowSearchBox" />
						</div>
					</div>
		
					<div class="control-group">
						<eazytec:label styleClass="control-label " key="listView.isTemplate" />
						<div class="controls checkbox-label">
							<form:checkbox id="isTemplate" path="isTemplate" />
						</div>
					</div>
					
					<div class="control-group">
						<eazytec:label styleClass="control-label  " key="listView.isShow" />
						<div class="controls checkbox-label">
							<form:checkbox id="isShow" path="isShow" />
						</div>
					</div>
				</div>
				
				<div class="span8">
					<div class="control-group">
						<eazytec:label styleClass="control-label " key="listView.isNeedCheckbox" />
						<div class="controls checkbox-label">
							<form:checkbox id="isNeedCheckbox" path="isNeedCheckbox" />
						</div>
					</div>
		
					<div class="control-group">
						<eazytec:label styleClass="control-label " key="listView.isFilterDuplicateData" />
						<div class="controls checkbox-label">
							<form:checkbox id="isFilterDuplicateData" path="isFilterDuplicateData" />
						</div>
					</div>
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label  " key="listView.fromQuery" />
				<div class="controls">
					<form:textarea path="fromQuery" id="fromQuery" rows="3" class="span6" onclick="disableSaveButton();" />
					<img src="/images/jtree_icon.png" onclick="disableSaveButton();showFromQureyWithData();" />
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label" key="listView.selectColumns" />
				<div class="controls">
					<form:textarea path="selectColumns" id="selectColumns" rows="3" class="span6" onclick="disableSaveButton();" />
					<img src="/images/jtree_icon.png" onclick="disableSaveButton();selectTableColumnsTree();" />
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label " key="listView.filterDuplicateDataColumn" />
				<div class="controls">
					<select class="span6" id="list_view_filter_duplicate" onfocus="listViewFillFilterColumn();"></select>
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label " key="listView.whereQuery" />
				<div class="controls">
					<form:textarea path="whereQuery" id="whereQuery" rows="3" class="span6" onclick="disableSaveButton();" />
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label " key="listView.orderBy" />
				<div class="controls">
					<form:textarea path="orderBy" id="orderBy" rows="2" class="span6" onclick="disableSaveButton();" />
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label " key="listView.groupBy" />
				<div class="controls">
					<form:textarea path="groupBy" id="groupBy" rows="2" class="span6" onclick="disableSaveButton();" />
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label " key="list.view.label.onRenderScriptName" />
				<div class="controls">
					<form:input path="onRenderScriptName" id="onRenderScriptName" class="span6" />
				</div>
			</div>

			<div class="control-group">
				<eazytec:label styleClass="control-label " key="list.view.label.onRenderScript" />
				<div class="controls">
					<form:textarea path="onRenderScript" id="onRenderScript" rows="4" class="span6" />
				</div>
			</div>

			<input type="hidden" name="_moduleId" value="" /> 
			<input type="hidden" id="moduleId" name="module_Id" value='${moduleId}' />
			<input type="hidden" id="_moduleName" name="_moduleName" value="${moduleName}" />
		</div>
	</div>
	<div class="control-group" id="buttons">
		<div class="form-actions no-margin" id="saveButtonDiv">
			<%-- <c:if test="${empty listView.id}">
	               <button type="button" onClick="_checkListViewQuery(false);" class="btn btn-primary checkQueryButton padding0 line-height0">
			         		<fmt:message key="listview.button.checkListViewQuery"/>
		         	</button>
	            </c:if>
	            <c:if test="${not empty listView.id}">
	                 <button type="button" onClick="_checkListViewQuery(true);" class="btn btn-primary checkQueryButton padding0 line-height0">
			         		<fmt:message key="listview.button.checkListViewQuery"/>
		         	</button>
	            </c:if> --%>
			<c:choose>
				<c:when test="${isEdit=='true'}">
					<button type="button" onClick="_checkListViewQuery();" class="btn btn-primary">
						<fmt:message key="listview.button.checkListViewQuery" />
					</button>
					<button type="submit" class="btn btn-primary " name="save" id="saveButton" onClick="checkIsTemplateSelected();appendFilterColumnInGroupByField();">
						<c:choose>
							<c:when
								test="${not empty listView.id && isListViewTemplate == false}">
								<fmt:message key="button.update" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.save" />
							</c:otherwise>
						</c:choose>
					</button>
					<button type="button" class="btn btn-primary" name="next" onclick="showListViews('LIST_VIEW','Manage List View');" id="cancelButton" style="cursor: pointer;">
						<fmt:message key="button.cancel" />
					</button>
				</c:when>
				<c:otherwise>
					<button type="button" class="btn btn-primary" name="next" onclick="showListViews('LIST_VIEW','Manage List View');" id="cancelButton" style="cursor: pointer;">
						<fmt:message key="button.back" />
					</button>
				</c:otherwise>
			</c:choose>
			<div class="clearfix"></div>
		</div>
	</div>
</form:form>
</div>
<script>
$(document).ready(function(){
	<c:if test="${empty listView.id}" >
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		var rows = $('table.basic_property_table tr');
		if(selected_nodes.attr("id")!=undefined){
			rows.filter('.show_list_view_module').hide();
		    
			var nodePath = $("#module_relation_tree").jstree("get_path", $("#"+selected_nodes.attr("id")), true); 
			var nodeLevel = nodePath.length;
			var moduleId = nodePath[0];
			$("#moduleId").val(moduleId);
			var nodInfo = $("#" + moduleId);
			$("#_moduleName").val(nodInfo.attr("name"));	
		}else{
			rows.filter('.show_list_view_module').show();
		}
		
	</c:if>;
// To load current module name directly into the list view's module name field
	var moduleName = $("#_moduleName").attr('value');
	$("#module").val(moduleName);
	
	
	setModuleNamesList('${moduleId}');
	var isListViewTemplate = "${isListViewTemplate}";
	var listViewTemplateId = "${listViewTemplateId}";
	var listViewId = "${listView.id}";
	var viewName = "${listView.viewName}";
	var element_id = $("#list_view_template");
	if(isListViewTemplate == "true"){
		if(listViewTemplateId){
			$("#list_view_template_row").hide();
			$("#viewName").attr('readonly', 'readonly');
		}else{
			$("#list_view_template_row").show();
		}
		loadListViewTemplate(element_id, listViewId, viewName);
	}
	
	$("#isFilterDuplicateData").click(function(){
		if($("#selectColumns").val() != ""){
			fillSelectedColumnsInFilterCombo($("#list_view_filter_duplicate"), $("#selectColumns").val())
		}
	});
	
	$("#isFilterDuplicateData").click(function() {
		var $this = $(this);
	    if ($this.is(':checked')) {
	    	$("#list_view_filter_duplicate").prop("disabled", false);
	    	if($("#selectColumns").val() != ""){
				fillSelectedColumnsInFilterCombo($("#list_view_filter_duplicate"), $("#selectColumns").val())
			}
	    } else {
	    	$("#list_view_filter_duplicate").prop("disabled", true);
	    }
	});
	var isGlobalChecked = $("#isFilterDuplicateData").is(':checked');
	if(isGlobalChecked){
		$("#list_view_filter_duplicate").prop("disabled", false);
    	if($("#selectColumns").val() != ""){
			fillSelectedColumnsInFilterCombo($("#list_view_filter_duplicate"), $("#selectColumns").val())
		}
	}else{
		$("#list_view_filter_duplicate").prop("disabled", true);
	}
	
	$("#selectColumns").focusout(function(){
		if($("#selectColumns").val() != ""){
			fillSelectedColumnsInFilterCombo($("#list_view_filter_duplicate"), $("#selectColumns").val())
		}
	});
});


function fillSelectedColumnsInFilterCombo(element_id, selectColumns){
	$("#list_view_filter_duplicate").empty();
	var selectColumnsArray = new Array();
	selectColumnsArray = selectColumns.split(",");
	$.each(selectColumnsArray, function(index, item) {
      	$(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item, item);
    });
	$(element_id).prepend("<option value='' selected='selected'></option>");
}

function checkIsTemplateSelected(){
	var templateName = $("#list_view_template").val();
	var moduleName = $("#module").val();
	var isListViewTemplateStatus = templateName ? true : false;
	var container = $("#rightPanel").length > 0 ? "rightPanel" : "target";
	_execute(container,'isListViewTemplate='+isListViewTemplateStatus+'&moduleName='+moduleName);
}

function isListViewExist(){
	 var listviewName = $("#viewName").val();
	 var param="listViewName="+listviewName;
	if(listviewName){
	
	  $.ajax({	
			type: 'POST',
			async: false,
			url : 'bpm/listView/checkListViewExist',
			data: param,
			success : function(response) {
				if(response.success){
					
				}else{
					
					$("#viewName").val("");
					$.msgBox({
						title: form.title.error,
						content: response.error,
					        type: "error",
						
					});
				}
			}
		}); 
	}
}

function appendFilterColumnInGroupByField(){
	var filterDuplicateDataColumn = $("#list_view_filter_duplicate").val();
	var groupBy = $("#groupBy").val();
	if(groupBy != ""){
		if(filterDuplicateDataColumn!="" && filterDuplicateDataColumn!= null && filterDuplicateDataColumn != 'null'){
			$("#groupBy").val(groupBy+","+filterDuplicateDataColumn);	
		}else{
			$("#groupBy").val(groupBy);
		}
	}else if(filterDuplicateDataColumn!="" && filterDuplicateDataColumn!= null && filterDuplicateDataColumn != 'null'){
		$("#groupBy").val(filterDuplicateDataColumn)
	}
}

function listViewFillFilterColumn(){
	if($("#selectColumns").val() != ""){
		fillSelectedColumnsInFilterCombo($("#list_view_filter_duplicate"), $("#selectColumns").val())
	}else{
		$("#list_view_filter_duplicate").empty();
	}
}
</script>