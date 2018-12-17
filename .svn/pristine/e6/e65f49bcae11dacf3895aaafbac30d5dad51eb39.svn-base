 <%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
 <%@ include file="/common/taglibs.jsp"%>

<%-- <div class="span12 box">
	<h2>
		<fmt:message key="quickNav.heading" />
	 	<a id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showListViews('QUICK_NAVIGATION','Quick Navigation');" data-icon=""><fmt:message key="Back"/></a> 	
	</h2> --%>
<spring:bind path="quickNavigation.*">
		 	
<c:if test="${not empty errors}">
    
    <script type="text/javascript">
    var error = "";
    <c:forEach var="error" items="${errors}">
    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.error, error, "error");
    </script>
     <c:remove var="errors"/>
</c:if>


<c:if test="${not empty successMessages}">
    <%-- <div class="alert alert-success fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="msg" items="${successMessages}">
            <c:out value="${msg}"/><br />
        </c:forEach>
    </div> 
    <c:remove var="successMessages" scope="session"/>--%>
    <script type="text/javascript">
    var msg = "";
    <c:forEach var="error" items="${successMessages}">
    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.success, msg, "success");
	showListViews("QUICK_NAVIGATION","Quick Navigation");
    </script>
     <c:remove var="successMessages" scope="session"/> 
</c:if>

<c:if test="${not empty status.errorMessages}">
   
   <script type="text/javascript">
    var error = "";
    <c:forEach var="error" items="${status.errorMessages}">
    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.error, error , "error");
	
    </script>
</c:if>

<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
<script type="text/javascript">
var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
setIndexPageByRedirect(path);


$(document).ready(function(){
	if($("#id").val()){
	}else{
		 $("#status").attr('checked', true);
	}
	//alert("${quickNavigation}");
});
function validateQuickNavigationForm(){
	
}
</script>
	</spring:bind>

<div class="page-header">
	<h2>
		<fmt:message key="quickNav.heading" />
	</h2>
</div>

<div align="right">
	<strong><a class="padding10"
		style="text-decoration: underline;" id="backToPreviousPage"
		href="javascript:void(0);" data-role="button" data-theme="b"
		onClick="showListViews('QUICK_NAVIGATION','快速导航');"
		data-icon=""><button class="btn"><fmt:message key="button.back" /></button></a> </strong>
</div>
<div class="height10"></div>
	<div class="span12">
	<div class="row-fluid">
				<div class="widget-body">
					<div class="box_main">
				<form:form  id="quickNavigation" commandName="quickNavigation" method="post" class="form-horizontal no-margin" action="bpm/admin/saveQuickNavigation" autocomplete="off" modelAttribute="quickNavigation" >
					 <form:hidden path="id"/>
					 <form:hidden path="menuId" id="menuId" name="menuId"/>
					 <form:hidden path="processName" id="processName" name="processName"/>
					 <form:hidden path="listViewName" id="listViewName" name="listViewName"/> 
					 <form:hidden path="rootNodeId" id="rootNodeId" name="rootNodeId"/>
					 
					<div class="control-group">
				 		<eazytec:label styleClass="control-label" key="quciNav.name"/>
				 	 	<div class="controls">
			 		 		<form:input path="name" id="name" class="span6"/>
				 		</div>
				 	</div>
				 	
					<div class="control-group">
					 	<eazytec:label styleClass="control-label" key="quciNav.iconPath"/>
				 	 	<div class="controls">
					 		 <form:input type="text" path="iconPath" id="iconPath" onClick="showIcons('iconPath');" class="span6" />
					 	</div>
				 	</div>
					
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="widget.urlType" />
						<div class="controls">
							 <label class="radio inline"> 
									<form:radiobutton path="urlType" name="urlType" id="menuUrl" value="menu" checked="true" onclick="changekUrlType(this.value)" />
									<eazytec:label key="widget.menu" />
							</label>
							<label class="radio inline"> 
									<form:radiobutton path="urlType" name="urlType" id="processlUrl" value="process" onclick="changekUrlType(this.value)" />
									<eazytec:label key="widget.process" />
							</label>
							<label class="radio inline"> 
									<form:radiobutton path="urlType" name="urlType" id="listViewlUrl" value="listViewQuick" onclick="changekUrlType(this.value)" />
									<eazytec:label key="widget.listViewQuickNav" />
							</label>
							<label class="radio inline"> 
								<form:radiobutton path="urlType" name="urlType" id="externalUrl" value="external"   onchange="changekUrlType(this.value)" /> 
								<eazytec:label key="widget.external" />
							</label> 				
						</div>
					</div>
					
					<div class="control-group">
					 	<eazytec:label styleClass="control-label" key="quciNav.url"/>
				 	 	<div class="controls">
					 		 <form:input path="quickNavigationUrl" id="quickNavigationUrl" name= "quickNavigationUrl" class="span6"/>
					 	</div>
				 	</div>
				 	
					<div class="control-group">
					 	<eazytec:label styleClass="control-label" key="quciNav.isAddQuickNav"/>
				 	 	<div class="controls checkbox-label">
					 		 <form:checkbox path="status" id="status"/>
					 	</div>
				 	</div>
				 	<div id="icon_dialog"></div>	
					<div class="form-actions no-margin" align="left">


						<button type="submit" class="btn btn-primary" name="save"
							onclick="bCancel=false;" id="save"><i class="icon-ok icon-white"></i>
							<c:choose>
								<c:when test="${mode != 'CREATE'}">
									<fmt:message key="button.update" />
								</c:when>
								<c:otherwise>	
									<fmt:message key="button.save" />
								</c:otherwise>
							</c:choose>
						</button>
						
						<button type="button" class="btn btn-primary cursor_pointer"
							name="next"
							onClick="showListViews('QUICK_NAVIGATION','快速导航');"
							id="cancelButton"><i class="icon-remove icon-white"></i>
							<fmt:message key="button.cancel" />
						</button>

					</div>
				</form:form>
			</div>				
		</div>
	</div>
</div>

<script type="text/javascript">
//var urlType = 'internal';
$(function(){
	urlType = $("input[type='radio']:checked").val();
	if(urlType == 'menu'){
		$( "#quickNavigationUrl" ).attr( "onClick", "showQuickNaviURLnew('Menu', 'single' , 'menuId',  document.getElementById('menuId'), '')" );
	} else if(urlType == 'process') {
		$( "#quickNavigationUrl" ).attr( "onClick", "showProcessSelectionTreeForQuickNav('Process', 'single' , 'processName', document.getElementById('processName'), '')" );
	} else if(urlType == 'listViewQuick') {
		$( "#quickNavigationUrl" ).attr( "onClick", "showListViewSelection('ListView', 'single' , 'listViewName', document.getElementById('listViewName'), '')" );
	}
	$("#save").click(function(event) { 
		
		var url = $('#quickNavigationUrl').val();
		if(urlType == 'external' && url != ''){
			
			
			var pattern = new RegExp("(https?:\/\/(?:www\.|(?!www))[^\s\.]+\.[^\s]{2,}|www\.[^\s]+\.[^\s]{2,})"); // fragment locator 
			if(pattern.test(url)) {
				return true;
			} else {
				openMessageBox(form.title.error, form.msg.urlNotValid,"error",true);
			 	event.preventDefault();
			  	return false;
			}
		}
	});
	
});

function changekUrlType(value) {
	urlType = value;
	 document.getElementById('menuId').value = '';
	 document.getElementById('processName').value = '';
	  document.getElementById('listViewName').value = '';
	if(value == 'menu'){
		 //if($('#quickNavigationUrl').attr('onClick') == undefined){
			$('#quickNavigationUrl').val('');
			 $( "#quickNavigationUrl" ).attr( "onClick", "showQuickNaviURLnew('Menu', 'single' , 'menuId',  document.getElementById('menuId'), '')" );
		// }
		
	} else if(value == 'process') {
	$('#quickNavigationUrl').val('');
		$( "#quickNavigationUrl" ).attr( "onClick", "showProcessSelectionTreeForQuickNav('Process', 'single' , 'processName', document.getElementById('processName'), '')" );
	} else if(urlType == 'listViewQuick') {
		
		$('#quickNavigationUrl').val('');
		$( "#quickNavigationUrl" ).attr( "onClick", "showListViewSelection('List View', 'single' , 'listViewName', document.getElementById('listViewName'), '')" );
	} else {
		 $( "#quickNavigationUrl" ).removeAttr( "onClick" );
	}
	$('#quickNavigationUrl').val('');
}


</script>
<script type="text/javascript">


/*function checkUrlValue() {
	var urlValue = document.getElementById("quickNavigationUrl").value;
	urlType = $("input[type='radio']:checked").val();
	if(urlType == 'external'){
	var regex = new RegExp("(https?:\/\/(?:www\.|(?!www))[^\s\.]+\.[^\s]{2,}|www\.[^\s]+\.[^\s]{2,})");
        if (regex.test(urlValue)) {
            return true;
        } else {
           $.msgBox({
                title : form.title.message,
                content : form.msg.validUrl,
                buttons : [ {
                    value : "Ok"
                } ],
                success : function(result) {
                    if (result == "Ok") {
                        // $("#quickNavigationUrl").val('');
                        //$("#quickNavigationUrl").focus();
                    }
                }
            });			
            return false;
            }
	}
}*/


</script>
