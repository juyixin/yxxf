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


$(function () {
     var targetWidth = $('#rightPanel').width();
     var targetHeight = $('#rightPanel').height();
     $('#user-grid').width(parseInt(targetWidth)-25);
     $('#user-details').width(parseInt(targetWidth)-25);
     $('#user-grid').height(parseInt(targetHeight)-48);
     $('#user-details').height(parseInt(targetHeight)-48);
});

</script>

<div class="span12 target-background">
    <div class="container-fluid" id="parent_container">
        <div class="row-fluid">
            <!-- <div class="span12" style='background-image: url("/styles/easybpm/images/palette_line.jpg");'>
               <div class="span3" style="width:20%;">
                    <div id="module_tree" class="floatLeft department-list-departments department-jstree"></div>
                </div>
                <div class="span9" > -->
                    <div class="span12">
                           <div id="user-details" class="floatLeft department-list-users">
                            <div id="user-grid" >
                                <%= request.getAttribute("script") %>
                            </div>
                            <div id="header_links" align="right" style="display:none;">
									<a class="padding10" id="manageTemplateForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="manageTemplateForms('${moduleId}')"  data-icon="info"><fmt:message key="form.palette.templates"/></a>
									<a class="padding10" id="copyForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="copyForms('${moduleId}')"  data-icon=""><fmt:message key="form.copyForm"/></a>
									<a class="padding10" id="deleteForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteForms();"  data-icon=""><fmt:message key="form.deleteForms"/></a>
                            		<%-- <a style="padding:10px;" id="manageTemplateForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="manageTemplateForms('${moduleId}')"  data-icon="info"><fmt:message key="form.palette.templates"/></a>
									<a style="padding:10px;" id="copyForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="copyForms('${moduleId}')"  data-icon="info"><fmt:message key="form.copyForm"/></a>
									<a style="padding:10px;" id="deleteForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteForms();"  data-icon="info"><fmt:message key="form.deleteForms"/></a> --%>
							</div> 
                        </div>
                    </div>
               <!--  </div>
            </div> -->
		</div>
		<div class="row-fluid">
			    <!-- <div class="span12" style='background-image: url("/styles/easybpm/images/palette_line.jpg");'>
			       <div class="span3" style="width:20%;">
			            <div id="module_tree" class="floatLeft department-list-departments department-jstree"></div>
			        </div>
			        <div class="span9" > -->
			            <div class="span12">
			            	<div id="user-details" class="floatLeft department-list-users">
			                    <div id="user-grid" >
			                        <%= request.getAttribute("script") %>
			                    </div>
			                    <div id="header_links" align="right" style="display:none;">
									<a style="padding:10px;" id="copyForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="copyForms('${moduleId}')"  data-icon=""><fmt:message key="form.copyForm"/></a>
									
									<a style="padding:10px;" id="deleteForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteForms();"  data-icon=""><fmt:message key="form.deleteForms"/></a>
								</div> 
			                 </div>
			             </div>
			        <!--  </div>
			    </div> -->
		</div>
	</div>
</div>
<script type="text/javascript"><!--
$('#grid_header_links').html($('#header_links').html());
$(document).ready(function() {
if('${isJspSuccess}' == 'true'){
validateMessageBox(form.title.success, form.msg.jspFormUpload, "success");
}else if('${isJspSuccess}' == 'false'){

validateMessageBox(form.title.error, form.msg.faildUploadJsp, "error");
}
var title = document.getElementById("module_title");
//var content = document.createTextNode("   (Note : The form with  * symbol is the template form )");
//title.appendChild(content);

    setTimeout(function() {
        $("#all a").removeClass("").addClass('jstree-clicked');
    },300);
    
    var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isSystemModule=selected_nodes.attr("issystemmodule");
	var isAdmin=selected_nodes.attr("isedit");
    /*var userRole=$("#currentUserRoles").val().split(",");
    var isAdmin=checkRoles(userRole,"ROLE_ADMIN");*/
    if(isAdmin == 'true' && '${isJspForm}' == 'false'){
    	$("#copyForms").show();
    	$("#manageTemplateForms").show();
    }else{
    	$("#copyForms").hide();
    	$("#manageTemplateForms").hide();
    }
  
    if('${isEdit}' == 'true' && '${isJspForm}' == 'false'){
    	$("#deleteForms").show();
    }else{
    	$("#deleteForms").hide();
    }
    var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
    
    if(isSystemModule=="true" && !isAdmin){
		$("#deleteForms").hide();
	}else{
		$("#deleteForms").show();
	}
	

 });
--></script>
