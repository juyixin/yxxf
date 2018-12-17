<%@ include file="/common/taglibs.jsp"%>
<style>
<!--
tr.row-height{
    height:5px;
}
-->
</style>
<div class="row-fluid">
	

<table width="100%" id="advance_search"> 
    <c:forEach items="${listViewColumns}" var="listViewColumn" begin="0" varStatus="loop">
        <c:if test="${listViewColumn.isAdvancedSearch == 'true'}">
            	<c:if test="${listViewColumn.isRange == false}">
            	 <c:if test="${loop.index == '0' || loop.index % 2 == '0'}">
		             <tr style="height:15px;"></tr> 
		            
	            </c:if>
	            <%-- <div class="control-group">
					<eazytec:label styleClass="control-label">${listViewColumn.columnTitle}</eazytec:label>
				</div> --%>
                 <td class="pad-L10">
                    <label class="control-label style3 style18 " style="float: right;
    text-align: right;line-height:10px;" >
                        ${listViewColumn.columnTitle}&nbsp;&nbsp;&nbsp;
                    </label>
                </td> 
                <td>
                    <div class="controls">
                       	   <c:if test="${listViewColumn.dataFieldType == 'date'}">
                               <input type="text" class="large datepicker" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                           </c:if>
                           <c:if test="${listViewColumn.dataFieldType == 'datetime'}">
                               <input type="text" class="large datetimepicker" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                           </c:if>
                           <c:if test="${listViewColumn.dataFieldType == '' || listViewColumn.dataFieldType == 'text'}">
                               <input type="text" class="large" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                           </c:if>
                           <c:if test="${listViewColumn.dataFieldType == 'autocomplete'}">
                               <input type="text" class="large" datadictionary="${listViewColumn.dataDictionary}" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" 
                               onkeyup="getAutocompleteData('${fn:replace(listViewColumn.columnTitle,' ','_')}','${listViewColumn.dataDictionary}')" list="searchData"/>
                               <datalist id="searchData">
                           </c:if>
                            <c:if test="${listViewColumn.dataFieldType == 'select'}">
                               <select class="large data_dictionary" datadictionary="${listViewColumn.dataDictionary}" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" ></select>
                           </c:if>
                           <c:if test="${listViewColumn.dataFieldType == 'radio'}">
                               <input type="radio" class="large data_dictionary_radio" datadictionary="${listViewColumn.dataDictionary}" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                           </c:if>
                           <c:if test="${listViewColumn.dataFieldType == 'checkbox'}">
                               <input type="checkbox" class="large data_dictionary_checkbox" datadictionary="${listViewColumn.dataDictionary}" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                           </c:if>
                    </div>
                </td>
                
                <c:if test="${ loop.index != '0' && loop.index % 2 == '1'}">
               </tr>
            	</c:if>                
               </c:if>
        </c:if>
    </c:forEach>
    
     <c:forEach items="${listViewColumns}" var="listViewColumn" begin="0" varStatus="loop">
        <c:if test="${listViewColumn.isAdvancedSearch == 'true'}">
               <c:if test="${listViewColumn.isRange == true}">
                 	<c:if test="${listViewColumn.dataFieldType == 'date'}">
                 	<tr class="row-height"></tr>
                 	<tr>
                 			 <td class="pad-L10">
			                    <label class="control-label style3 style18 " >
			                        ${listViewColumn.columnTitle}
			                    </label>
			                 </td>
			                 <td>
                    			<div class="controls">
                    				 <input type="text" class="large datepicker input-range" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                    			</div>
                    		 </td>
                    		 <td>
                    			<div class="controls" colspan="2">
                    				 <input type="text" class="large datepicker input-range" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                    			</div>
                    		 </td>
                    		  </tr>
                      </c:if>
                      <c:if test="${listViewColumn.dataFieldType == 'datetime'}">
                      	<tr class="row-height"></tr>
                      	<tr>
                      		 <td class="pad-L10">
			                    <label class="control-label style3 style18 " >
			                        ${listViewColumn.columnTitle}
			                    </label>
			                 </td>
			                 <td>
                    			<div class="controls">
                    				  <input type="text" class="large datetimepicker input-range" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                    			</div>
                    		 </td>
                    		 <td class="pad-L10">
			                    <label class="control-label style3 style18 " >
			                        ${listViewColumn.columnTitle}
			                    </label>
			                 </td>
			                 <td>
                    			<div class="controls">
                    				  <input type="text" class="large datetimepicker input-range" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                    			</div>
                    		 </td>
                    		  </tr>
                      </c:if>
                      <c:if test="${listViewColumn.dataFieldType == '' || listViewColumn.dataFieldType == 'text'}">
                      	<tr class="row-height"></tr>
                       	<tr>
                          	<td class="pad-L10">
			                    <label class="control-label style3 style18 " >
			                        ${listViewColumn.columnTitle}
			                    </label>
			                 </td>
			                 <td>
                    			<div class="controls">
                    				  <input type="text" class="large input-range" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                    			</div>
                    		 </td>
			                 <td class="pad-L10">
			                    <label class="control-label style3 style18 " >
			                        ${listViewColumn.columnTitle}
			                    </label>
			                 </td>
			                 <td>
                    			<div class="controls" >
                    				   <input type="text" class="large input-range" id="${fn:replace(listViewColumn.columnTitle,' ','_')}" name="${listViewColumn. dataFields}" />
                    			</div>
                    		 </td>
                        </tr>
                        <tr class="row-height"></tr>
                   </c:if>
                           
               </c:if>
        </c:if>
    </c:forEach>
    <tr style="height:20px;"></tr>
   
    <tr>

			<td colspan="4" align="center">
				<div class="form-actions no-margin">
					<button type="button"
						onClick="_advancedSearch('${listViewColumns[0].listView.viewName}');"
						class="btn btn-primary">
						<fmt:message key="button.search" />
					</button>
					<button type="button" onClick="closeSelectDialog('popupDialog');"
						class="btn btn-primary">
						<fmt:message key="button.cancel" />
					</button>
				</div>
			</td>

		</tr>
</table>
</div>
<input type="hidden" id="listView" name="listView" value="${listViewColumns[0].listView.id}" />

<script type="text/javascript">
function _advancedSearch(viewName){
    var searchParamSize = 0;
    var rangeCount = 1;
    var searchParams = "[";
    $("#advance_search").find("input").each(function(index){searchParamSize = index;});
    $("#advance_search").find("input").each(function(index){
    	if($(this).attr("type") == "radio" && $(this).is(':checked') == true){
    		var attrName = $(this).attr("name");
            var attrValue = $(this).val();
            if(attrValue != ""){
                //searchParams = searchParams + "'"+ attrName +"':'"+attrValue+"'";
                var advanceSearchParamsMap = "{'searchField':'"+attrName+"', 'searchValue':'"+encodeURI(attrValue)+"', 'constraint':'like'}";
                if(searchParamSize >= index){
                	searchParams = searchParams + advanceSearchParamsMap + ",";
                }
            }
    	}/* else if($(this).attr("type") == "checkbox" && $(this).is(':checked') == true){
    		var attrName = $(this).attr("name");
            var attrValue = $(this).val();
            if(attrValue != ""){
                //searchParams = searchParams + "'"+ attrName +"':'"+attrValue+"'";
                var advanceSearchParamsMap = "{'searchField':'"+attrName+"', 'searchValue':'"+attrValue+"', 'constraint':'"+constraint+"'}";
                if(searchParamSize >= index){
                	searchParams = searchParams + advanceSearchParamsMap + ",";
                }
            }
    	} */else if($(this).attr("type") == "text"){
    		var attrName = $(this).attr("name");
    		var attrValue = '';
			if($(this).attr("list") == "searchData") { // search by key for data dictionaries
	    		//alert("=="+$(this).attr("name")+"==="+$(this).attr("value")+"===="+$(this).attr("class")+"=="+$('#searchData').val());
				var dataListValue = $('#searchData').find('option[value="'+$(this).attr("value")+'"]');
				attrValue = dataListValue.attr('id');
			} else {
	            attrValue = $(this).val();
			}
            if(attrValue != ""){
            	var className = $(this).attr("class");
            	var constraint = "";
            	if(className.indexOf("input-range") > -1){
            		if(rangeCount == 1){
            			constraint = ">=";
            			rangeCount = 2;
            		}else{
            			constraint = "<=";
            			rangeCount = 1;
            		}
            	}else if(attrValue.toUpperCase()=='TRUE' || attrValue.toUpperCase()=='FALSE'){
            		constraint='=';
            	}else{
            		constraint = "like";
            	}
            	// searchParams = searchParams + "'"+ attrName +"':'"+attrValue+"'";
                var advanceSearchParamsMap = "{'searchField':'"+attrName+"', 'searchValue':'"+encodeURI(attrValue)+"', 'constraint':'"+constraint+"'}";
                if(searchParamSize >= index){
                	searchParams = searchParams + advanceSearchParamsMap + ",";
                }
            }
    	}
    });
    $("#advance_search").find("select").each(function(index){searchParamSize = index;});
    $("#advance_search").find("select").each(function(index){
        var attrName = $(this).attr("name");
        var attrValue = $(this).val();
        if(attrValue != ""){
           // searchParams = searchParams + "'"+ attrName +"':'"+attrValue+"'";
            var advanceSearchParamsMap = "{'searchField':'"+attrName+"', 'searchValue':'"+encodeURI(attrValue)+"', 'constraint':'like'}";
            if(searchParamSize >= index){
                searchParams = searchParams + advanceSearchParamsMap + ",";
            }
        }
    });
    searchParams = searchParams+ "]";
    
    if(searchParams != null){
    //encodeURLStringComponent
		$.ajax({	
			url: '/bpm/listView/reloadGridWithSearchParamsAndConstraints?listViewId='+$("#listView").val()+'&sortType=asc&searchType=and&searchParams='+searchParams+'&listViewParams='+$("#listViewParamsList").val()+'&sortTypeColumn=',
			type: 'POST',
			async: false,
			dataType: 'json',
			success : function(response) {
				if(response){
					var gridData = eval(response);
			    	$("#_"+viewName).jqGrid('clearGridData');
					$('#_'+viewName).jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
					closeSelectDialog('popupDialog');
			    }
				 advanceSearchParams[0] = searchParams;
			}
	  	});
	} 
}

$(function(){
     /* if(advanceSearchParams[0]){
        var searchParams = eval(advanceSearchParams[0]);
        $.each(searchParams[0], function(key, value){
            key = key.replace(".","_");
            $("#"+key).val(value);
        });
    } */

     $(".datepicker").datepicker({
        showOn: 'button',
        //buttonText: 'Show Date',
        buttonImageOnly: true,
        buttonImage: '/images/easybpm/form/rbl/_cal.gif',
        dateFormat: 'yy-mm-dd',
        maxDate: 0,
        constrainInput: true
    });
     $(".datetimepicker").datetimepicker({
        showOn: 'button',
        //buttonText: 'Show Date',
        buttonImageOnly: true,
        buttonImage: '/images/easybpm/form/rbl/_cal.gif',
        dateFormat: 'yy-mm-dd',
        timeFormat: 'H:mm',
        maxDate: 0,
        constrainInput: true
    });
});


$( ".data_dictionary" ).each(function( index ) {	
	var element_id = this.getAttribute("id");
	var parent_dictId = this.getAttribute("datadictionary");
	addOptionValToDictionary(element_id,parent_dictId);  
});

$( ".data_dictionary_radio" ).each(function( index ) {	
	var element_id = this.getAttribute("id");
	var parent_dictId = this.getAttribute("datadictionary");
	radioButtonForDataDictionary(element_id,parent_dictId);
});

$( ".data_dictionary_checkbox" ).each(function( index ) {	
	var element_id = this.getAttribute("id");
	var parent_dictId = this.getAttribute("datadictionary");
	checkboxForDataDictionary(element_id,parent_dictId);
});

$( ".autocomplete" ).each(function( index ) {
	var element_id = this.getAttribute("id");
	var parent_dictId = this.getAttribute("datadictionary");
	autoCompleteForDataDictionary(element_id,parent_dictId);
});
</script>
