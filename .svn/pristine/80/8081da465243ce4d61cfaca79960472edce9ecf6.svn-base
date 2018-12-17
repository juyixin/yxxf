<%@ include file="/common/taglibs.jsp"%>

<div class="container-fluid" id="parent_container"  style="overflow-y: auto;">
	<div class="row-fluid" id="group_setting">
	    <div id="group_setting_prop_form" class="span12">
	    	<div id="multi_fields">
	    		<input type="hidden" id="columnPropertyIdInGS" name="columnPropertyIdInGS" />
	    			<div class="widget span12 background-none border-none">
	    				<div class="widget-body span12">
	    				
			      			<div class="span6">	
								<div class="control-group">
							    	<eazytec:label styleClass="control-label " key="list.view.groupSetting.groupName"/>					
						            <div class="controls">
							           	<input type="text" class="span10" id="groupName" name="groupName" />
						            </div>
								</div>
								
								<div class="control-group">
							    	<eazytec:label styleClass="control-label" key="list.view.groupSetting.groupType"/>					
						            <div class="controls" >
				           				<label class="radio inline"><input type="radio" id="groupTypeMulti" name="groupType" value="Multi" checked="true"/> 
				           				<fmt:message key="list.view.groupSetting.groupTypeMulti"/> &nbsp;&nbsp;&nbsp;</label>
				           				<label class="radio inline"><input type="radio" id="groupTypeSingle" name="groupType" value="Single"/>
						           	 	<fmt:message key="list.view.groupSetting.groupTypeSingle"/> </label> 
						            </div>
						        </div>
						        
						        <div class="control-group">
						            <eazytec:label styleClass="control-label" key="list.view.groupSetting.sortType"/>					
						            <div class="controls">
			            				<label class="radio inline">
			            				<input type="radio" id="sortTypeAsc" name="sortType" value="Asc" checked="true"/> 
						           	 	<fmt:message key="list.view.groupSetting.sortTypeAsc"/> &nbsp;&nbsp;&nbsp;</label>
				           				<label class="radio inline">
				           				<input type="radio" id="sortTypeDesc" name="sortType" value="Desc"/> 
				           				<fmt:message key="list.view.groupSetting.sortTypeDesc"/></label>
						            </div>
								</div>
								
								<div class="control-group">
						            <eazytec:label styleClass="control-label" key="list.view.groupSetting.comment" />						
						            <div class="controls">
							           	<textarea class="span10" id="groupSettingcomment" name="groupSettingcomment" /></textarea>
						            </div>
								</div>   
							</div>
			
							<div class="span6">	
								<div class="control-group">
						            <eazytec:label styleClass="control-label" key="list.view.groupSetting.groupFieldsName"/>
						            <div class="controls">
							           	<select class="span10"  id="groupFieldsName" name="groupFieldsName" >
							           	</select>
						            </div>
								</div>
							
								<div class="control-group">
						            <eazytec:label styleClass="control-label" key="list.view.groupSetting.parentGroup"/>					
						            <div class="controls">
							           	<select class="span10" id="parentGroup" name="parentGroup"/></select>
						            </div>
								</div>
								
								<div class="control-group">
						            <eazytec:label styleClass="control-label" key="list.view.groupSetting.orderBy" />						
						            <div class="controls">
							           	<input type="text" class="span10" id="groupOrder" name="groupOrder" value="0"/>
						            </div>
								</div>
							</div>
							
							<div class="control-group span12" align="center">
							 	<div class="button-background no-margin">
									<button type="button" onClick="_addGroupSettingRecord();" class="btn btn-primary">
						         		<fmt:message key="button.add"/>
						         	</button>
						         	<button type="button" onClick="_clearGroupSettingRecord();" class="btn btn-primary">
						         		<fmt:message key="button.clear"/>
						         	</button>
						         	<div class="clearfix"></div>
					         	</div>
							</div>  
						</div>
					</div>
				</div>
			</div> 			

		    <div id="group_setting_prop_grid" class="span12 overflow_y" >
		    	<div class="widget background-none border-none">
	    			<div class="widget-body">
				    	<div id="group_setting_property_grid" >
				    		<div class="control-group">
								<div class="controls" style="margin-left:0;">
									<table id="_LIST_VIEW_GROUP_SETTING_PROPERTY"></table>
									<div id="_LIST_VIEW_GROUP_SETTING_PROPERTY_PAGER"></div>
								</div>
							</div>
							<div class="control-group" align="center">
								<div class="button-background no-margin" style="margin-left:0;" align="center" style="padding-left:0;">
						         	<button type="button" onClick="_saveOrUpdateAllGroupSettingRecord('${listView.id}');" class="btn btn-primary" >
										<fmt:message key="button.save"/>
						    		</button>
						         	<button type="button" onClick="_deleteGroupSettingRecord();" class="btn btn-primary">
						         		<fmt:message key="button.delete"/>
						         	</button>
						         	<button type="button" onClick="_deleteGroupSettingAllRecord();" class="btn btn-primary">
						         		<fmt:message key="button.deleteAll"/>
						         	</button>
						         	<div class="clearfix"></div>
								</div>
							</div>     	 			
				    	</div>
				    </div>
				</div>
		    </div>
		</div>
	</div>

<script type="text/javascript">
$(function(){
	createGroupSettingProperty = [];
	deleteGroupSettingProperty = [];
	__gs_row_id = 0;
	var listViewId = "${listView.id}";
	groupSettingGrid();
	var targetHeight = $('#target').height();
	var targetWidth = $('#target').width();
	$('#group_setting').height(parseInt(targetHeight)-120);
	//$('#group_setting_prop_grid').height(parseInt(targetHeight)-370);
	$('#group_setting_prop_grid').width('100%');
	var isListViewTemplate = "${isListViewTemplate}";
	if(listViewId && isListViewTemplate == "false"){
		getGroupSettingPropety(listViewId);
	}else if(isListViewTemplate == "true"){
		var listViewTemplateId = "${listViewTemplateId}";
		if(listViewTemplateId){
			getGroupSettingPropety(listViewTemplateId);
		}else{
			getGroupSettingPropety(listViewId);
		}	
		var gsProperty = new Array();
		
		var gridData = $("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('getRowData'); 
		var _grid_row_id = 0;
		for(var i = 0; i < gridData.length; i++){
		    if (gridData[i]){
		    	var groupSettinsProperties = gridData[i];
		    	var colsPropertyId = groupSettinsProperties.id;
		    	colsPropertyId = ++_grid_row_id;
			    groupSettinsProperties.id = colsPropertyId;
			    gsProperty.push(groupSettinsProperties);
          		createGroupSettingProperty[createGroupSettingProperty.length] = groupSettinsProperties;
			}
		}	
    	$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('clearGridData');
		$('#_LIST_VIEW_GROUP_SETTING_PROPERTY').jqGrid('setGridParam', {page: 1, data: gsProperty}).trigger('reloadGrid');
	}
		
	if(listViewId){
		getListViewColumns("groupFieldsName");
	}
	 $('input[type=radio][name="groupType"]').live('change', function() { 
		 var groupType = $(this).val();
		 if(groupType == "Multi"){
			 $("#parentGroup").removeAttr("disabled");
		 }else{
			 $("#parentGroup").val("");
			 $("#parentGroup").attr("disabled", "disabled");
		 }
		
	 });
});

$(window).bind('resize', function() {
// 	 setTimeout(function() {
		 var winWidth = parseInt($("#group_setting_prop_grid").width());
  		if(winWidth <= 100){
  			winWidth = parseInt($("#wizardTab-content").width());
  			winWidth = winWidth + 9;
  		}
  		$("#_LIST_VIEW_GROUP_SETTING_PROPERTY").jqGrid('setGridWidth', winWidth-22);
// 	 },1);			
}).trigger('resize');
updateParentGroupSelectBoxList("","");
</script>
