<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div id="button_parent_container">
	<div class="row-fluid">
		<div class="span6">
			<div class="widget background-none border-none">
				<div id="button_prop_grid" class="widget-body">
					<div id="buttons_property_grid">
						<table id="_LIST_VIEW_BUTTONS_PROPERTY"></table>
						<div id="_LIST_VIEW_BUTTONS_PROPERTY_PAGER"></div>
                        <div class="control-group"></div>   
					</div>
				</div>
				
				<div id="button_prop_grid_buttons" align="center">
					<div class="control-group">
						<div class="button-background no-margin">
							<button type="button"
								onClick="_saveOrUpdateButtonRecord('${listView.id}');"
								class="btn btn-primary">
								<fmt:message key="button.save" />
							</button>
							<button type="button" onClick="_deleteButtonRecord();"
								class="btn btn-primary">
								<fmt:message key="button.delete" />
							</button>
							<button type="button" onClick="_deleteAllButtonRecord();"
								class="btn btn-primary">
								<fmt:message key="button.deleteAll" />
							</button>
							<div class="clearfix"></div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="span6">
			<div class="widget background-none border-none">
				<div id="button_prop_form" class="widget-body">
					<input type="hidden" id="buttonPropertyId" name="buttonPropertyId" />
					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="listView.buttonProperty.displayName" />
						<div class="controls">
							<input type="text" class="span12" id="buttonDisplayName"
								name="buttonDisplayName" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="listView.buttonProperty.buttonOrder" />
						<div class="controls">
							<input type="text" class="span12" id="buttonOrder"
								name="buttonOrder" value="0" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="module.users"/>
						<div class="controls">
							<input type="hidden" id="listViewButtonUsers" name="listViewButtonUsers" class="span12 white-background" onclick="showUserSelection('User', 'multi' , 'listViewButtonUsers', this, '');" readonly="readonly"/>
			 				<input type="text" id="listViewButtonUsersFullName" name="listViewButtonUsersFullName" class="span12 white-background" onclick="showUserSelection('User', 'multi' , 'listViewButtonUsers', document.getElementById('listViewButtonUsers'), '');" readonly="readonly"/>
						</div>
					</div>
						
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="module.department"/>
						<div class="controls">
							<input type="text" id="listViewButtonDeps" name="listViewButtonDeps" class="span12 white-background" onclick="showDepartmentSelection('Department', 'multi' , 'listViewButtonDeps', this, '');" readonly="readonly"/>
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="module.roles"/>
						<div class="controls">
							<input type="text" id="listViewButtonRoles" name="listViewButtonRoles" class="span12 white-background" onclick="showRoleSelection('Role', 'multi' , 'listViewButtonRoles', this, '');" readonly="readonly"/>
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="module.groups"/>
						<div class="controls">
							<input id="listViewButtonGroups" class="span12 white-background" type="text" readonly="readonly" onclick="showGroupSelection('Group', 'multi' , 'listViewButtonGroups', this, '');" name="listViewButtonGroups">
						</div>
					</div>

					<div id="create_new_redirect_formorprocess_tr" class="displayNone">
						<div class="control-group">
							<eazytec:label styleClass="control-label"
								key="listView.buttonProperty.process" />
							<div class="controls">
								<input type="text" class="span12" id="redirectValue"
									name="redirectValue" />
							</div>
						</div>
					</div>
					<div id="onClickbuttonScript">
						<div class="control-group">
							<eazytec:label styleClass="control-label"
								key="listView.buttonProperty.buttonScript" />
							<div class="controls">
								<input type="text" class="span10" id="buttonScript" readonly='true'
									name="buttonScript" /><a href="javascript:void(0);" onClick="openButtonEvent();"><img src="/images/add.gif"/></a>
							</div>
						</div>

						<div class="displayNone" id="table_name_tr">
							<div class="control-group">
								<eazytec:label styleClass="control-label"
									key="list.view.buttonsProperty.tableName" />
								<div class="controls">
									<select class="span12" id="tableName" name="tableName"
										onChange="loadColumnNameForTable(this.value);">
									</select>
								</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<fmt:message key='list.view.buttonsProperty.tablename.note' />
							</div>
						</div>
						

						<div class="displayNone" id="column_name_tr">
							<div class="control-group">
								<eazytec:label styleClass="control-label"
									key="list.view.buttonsProperty.columnName" />
								<div class="controls">
									<select class="span12" id="columnName" name="columnName">
									</select>
								</div>
							</div>
						</div>

						<div class="control-group">
							<eazytec:label styleClass="control-label"
								key="listView.buttonProperty.iconPath" />
							<input type="hidden" id="iconPath" name="iconPath" />
							<div class="controls">
								<div class="span12">
									<div class="span3">
										<div align="center" class="menu-icon icon_size checkbox-label">
											<img id="choosed_iconPath" src="" />
										</div>
									</div>
									<div class="span4 field-label">  
										<a href="javascript:void(0);" onClick="showMenuIcons('iconPath');"> <eazytec:label styleClass="cursor-pointer" key="listView.buttonProperty.chooseIcon" /></a>
									</div>
									<div class="span4 field-label">
										<a href="javascript:void(0);" onClick="removeIconPath('iconPath');"> <eazytec:label styleClass="cursor-pointer" key="listView.buttonProperty.removeIcon" /></a>
									</div>
								</div>
							</div>
						</div>
					</div>


					<div class="control-group">
						<eazytec:label styleClass="control-label "
							key="list.view.buttonsProperty.status" />
						<div class="controls checkbox-label">
							<input type="checkbox" id="isActive" name="isActive"
								checked="true" />
						</div>
					</div>
				</div>
				
				<div id="button_prop_form_buttons" align="center">
					<div class="control-group">
						<div class="controls button-background no-margin">
							<button type="button" onClick="_clearButtonRecord();"
								class="btn btn-primary">
								<fmt:message key="button.new" />
							</button>
							<button type="button" onClick="_addButtonRecord();"
								class="btn btn-primary">
								<fmt:message key="button.add" />
							</button>
							<button type="button" onClick="_clearButtonRecord();"
								class="btn btn-primary">
								<fmt:message key="button.clear" />
							</button>
							<div class="clearfix"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="icon_dialog"></div>

<script type="text/javascript">
	


	$(function() {
		createButtonsProperty = [];
		deleteButtonsProperty = [];
		_button_row_id = 0;
		var listViewId = "${listView.id}";
		buttonsPropertyGrid();
		var isListViewTemplate = "${isListViewTemplate}";
		if (listViewId && isListViewTemplate == "false") {
			getButtonsPropety(listViewId);
		} else if (isListViewTemplate == "true") {
			var listViewTemplateId = "${listViewTemplateId}";
			if (listViewTemplateId) {
				getButtonsPropety(listViewTemplateId);
			} else {
				getButtonsPropety(listViewId);
			}
			var btnsProperty = new Array();
			var gridData = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid(
					'getRowData');
			for (var i = 0; i < gridData.length; i++) {
				if (gridData[i]) {
					var buttonsProperties = gridData[i];
					var btnsPropertyId = buttonsProperties.id;
					btnsPropertyId = i + 1;
					buttonsProperties.id = btnsPropertyId;
					btnsProperty.push(buttonsProperties);
					createButtonsProperty[createButtonsProperty.length] = buttonsProperties;
				}
			}
			$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('clearGridData');
			$('#_LIST_VIEW_BUTTONS_PROPERTY').jqGrid('setGridParam', {
				page : 1,
				data : btnsProperty
			}).trigger('reloadGrid');
		}
		var targetHeight = $('#target').height();
		//$('#button_prop_grid').height(parseInt(targetHeight)-120);
		$('#button_prop_form').height(parseInt(targetHeight)/2 + 303);
		$('#button_prop_grid').height(parseInt(targetHeight)/2 + 303);
		//getButtonPropety(listViewId);
		var rowCount = $("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('getGridParam',
				'records');
		if (rowCount == 0) {
			var viewName = "${listView.viewName}";
			if (viewName) {
				var defaultBtnsProperty = getDefaultBUttonsProperty(listViewId,
						viewName);
				$('#_LIST_VIEW_BUTTONS_PROPERTY').jqGrid('setGridParam', {
					page : 1,
					data : eval(defaultBtnsProperty)
				}).trigger('reloadGrid');
			}
		}
		autoCompleteForAllActiveProcess("redirectValue");
	});
	$(window).bind(
			'resize',
			function() {
				
// 				setTimeout(
// 						function() {
							var winWidth = parseInt($("#button_prop_grid")
									.width());
							var form_height = $('#button_prop_form').height();
							if (winWidth <= 50) {
								winWidth = parseInt($("#wizardTab-content")
										.width() / 2);
								winWidth = winWidth + 2;
							}
							$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid(
									'setGridWidth', winWidth - 22);
							$("#_LIST_VIEW_BUTTONS_PROPERTY").jqGrid('setGridHeight', form_height-100);
// 						}, 1);
			}).trigger('resize');

	function showMenuIcons(iconPath) {
		$
				.ajax({
					url : 'bpm/admin/getMenuIcons',
					type : 'GET',
					dataType : 'json',
					async : false,
					success : function(response) {
						var htmlContent = "<table  style='background-color:#DEDEDE;border:2px solid #fff;' width='100%'><tr>";
						for (var index = 0; index < response.length; index++) {
							if (index % 5 == 0) {
								htmlContent = htmlContent + "</tr><tr>";
							}
							htmlContent = htmlContent
									+ "<td align='center' style='padding:5px;border:2px solid #fff;'><img class='cursor-pointer' onClick='selectMenuIcon(\""
									+ response[index] + "\", \"" + iconPath
									+ "\");'  src='" + response[index]
									+ "'/></td>";
						}
						htmlContent = htmlContent + "</tr></table>";
						$("#icon_dialog").html(htmlContent);
						openSelectDialog('icon_dialog', 300, '选择图标');
					}
				});
	}
	
	function openButtonEvent(){
		openSelectDialogForFixedPosition("popupDialog","","800","290","按钮事件");
		document.location.href = "#bpm/listView/buttonEvent";
		_execute('popupDialog','');
	}

	function openSelectDialog(divId, width, title) {
		$myDialog = $("#" + divId);
		$myDialog.dialog({
			width : width,
			height : 'auto',
			modal : true,
			title : title
		});
		$myDialog.dialog("open");
	}

	function selectMenuIcon(iconPath, iconPathHiddenId) {
		$("#" + iconPathHiddenId).val(iconPath);
		$("#choosed_" + iconPathHiddenId).attr("src", iconPath);
		$("#icon_dialog").dialog("close");
	}

	function removeIconPath(iconPath) {
		if ($("#" + iconPath).val()) {
			$("#" + iconPath).val("");
			$("#choosed_" + iconPath).attr("src", "");
		} else {
			validateMessageBox(form.title.validation, "Icon is not selected.", "error");
		}
	}

	function loadColumnNameForTable(selectedValue) {
		getListViewColumnsForSelectedValue("columnName", selectedValue);
	}

	//load table name in button property for default button
	function loadTableCombo() {
		var selectedFromQuery = $("#fromQuery").val();
		var element_id = "tableName";
		var listViewId = "${listView.id}";
		$("#" + element_id).empty();
		if (listViewId && selectedFromQuery != "") {
			var tablesArray = selectedFromQuery.split(",");
			for (var tableIndex = 0; tableIndex < tablesArray.length; tableIndex++) {
				var tableNameArray = (tablesArray[tableIndex]).split(" AS ");
				if (tableNameArray[0].indexOf("ETEC_") == -1
						&& tableNameArray[0].indexOf("QRTZ_") == -1) {
					$("#" + element_id).get(0).options[$("#" + element_id).get(
							0).options.length] = new Option(tableNameArray[0],
							tableNameArray[0]);
				}
			}
			$("#" + element_id).prepend(
					"<option value='' selected='selected'></option>");
		}
	}
	/* $("#buttonBorder").each(function() {
		  $(this).css("margin-top", $(this).outerHeight());
		}); */
</script>
