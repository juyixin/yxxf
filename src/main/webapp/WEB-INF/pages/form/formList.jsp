<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='user.manageUsers'/>"/>
</head>
<%@ include file="/common/messages.jsp" %>
<script type="text/javascript">

//the javascript written here because of using jstl tag for get department list
//construct json data for jsTree


/* $(function () {
     var targetWidth = $('#target').width();
     var targetHeight = $('#target').height();
     var _width = parseInt(targetWidth) / 5;
     $('#module_tree').height(parseInt(targetHeight)-42);
     $('#module_tree').width("100%");
     $('#user-grid').width('100%');
     $('#user-details').width('100%');
     $('#user-grid').height(parseInt(targetHeight)-42);
     var jsonData = '${moduleTree}';
     constructJsTree("module_tree",jsonData,"getModuleFormGrid",false);
});
$("#module_tree").bind('before.jstree', function(event, data) {
	if (data.func == 'check_move') {
		return false;
	}
});
function getModuleFormGrid(e,data){
	
	var filters = "";
	var id = data.rslt.obj.data("id");
	if( id=="all"){
		filters = "";
	}else{
		filters = { "groupOp": "AND", "rules": [{ "field": "module", "op": "eq", "data": id }] };
	}
	
	jqGridFilter(filters , gridObj);

    function jqGridFilter(filtersparam, grid) {
        grid.setGridParam({
            postData: {
                filters: filtersparam
            },
            search: true
        });
        grid.trigger("reloadGrid");
    }
	  
} */

$(function () {
    var targetWidth = $('#rightPanel').width();
    $('#user-grid').width(targetWidth);
   $('#user-details').width(targetWidth);
});



</script>

<div class="span12 target-background">
    <div class="container-fluid" id="parent_container">
       <%--  <div class="row-fluid">
            <div class="span12">
                <div class="titleBG">
                    <span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="admin.module.list"/></span>
                </div>
            </div>
        </div> --%>
        <div class="row-fluid">
            <div class="span12" style='background-image: url("/styles/easybpm/images/palette_line.jpg");'>
                <!-- <div class="span3" style="width:20%;">
                    <div id="module_tree" class="floatLeft department-list-departments department-jstree"></div>
                </div> -->
                <div class="span9 width-per-80">
                    <div class="span12">
                           <div id="user-details" class="floatLeft department-list-users">
                            <div id="user-grid" >
                                <%= request.getAttribute("script") %>
                            </div>
                            <div id="header_links" align="right" class="displayNone">
								<a class="padding10" id="copyForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="goBackToFormListView()"  data-icon=""><fmt:message key="button.back"/></a>
							</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%-- <div class="span12 target-background">
    <div class="row-fluid">
        <div class="span12">
            <div class="titleBG">
                <span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="user.manageUsers"/></span>
            </div>
        </div>
    </div>
     <%@ include file="/common/messages.jsp" %>
    <div class="row-fluid" style='background-image: url("/styles/easybpm/images/palette_line.jpg");'>
        <div class="span12">
               <table width="100%">
                       <tr>
                           <td width="25%">
                               <div class="span12">
                                   <div id="department_tree" class="floatLeft department-list-departments department-jstree"></div>
                               </div>
                           </td>
                           <td width="75%">
                               <div class="span12">
                               <div id="user-details" class="floatLeft department-list-users">
                            <div id="header_links" align="right" style="display:none">
                                <a style="padding:10px;" id="createUser" href="#bpm/admin/userform" data-role="button" data-theme="b"  onClick="return createDepartmentUser()"  data-icon=""><fmt:message key="button.createNew"/></a>
                                <a style="padding:10px;" id="enableUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="updateUserStatus('disable');"  data-icon=""><fmt:message key="button.disable"/></a>
                                <a style="padding:10px;" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteUsers()"  data-icon=""><fmt:message key="button.delete"/></a>
                            </div>
                            <div id="user-grid" >
                            <%= request.getAttribute("script") %>
                            </div>
                            </div>
                            </div>
                           </td>
                       </tr>
               </table>
        </div>
    </div>
</div> --%>
<script type="text/javascript">
$('#grid_header_links').html($('#header_links').html());
$(document).ready(function() {

    setTimeout(function() {
        $("#all a").removeClass("").addClass('jstree-clicked');
    },300);

 });
</script>
