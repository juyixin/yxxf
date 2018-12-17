<%@ include file="/common/taglibs.jsp"%>

<spring:bind path="fileUpload.*">
   <%@ include file="/common/messages.jsp" %>
</spring:bind>
<%-- 
 <div class="span12 box">
 	<h2><fmt:message key="logo.upload.title"/></h2>
</div>
<form:form commandName="fileUpload" method="post" action="bpm/fileupload/uploadLogoImage" enctype="multipart/form-data" id="uploadForm" cssClass="form-horizontal">
	<div class="container-fluid" id="parent_container">
		<div class="row-fluid">
			<div class="span12" style="padding-left:25%;padding-top:2%;">
	            <eazytec:label key="upload.logo" styleClass="form-lbl-position control-label style3 style18"/>
                <input type="file" name="file" id="file_choose" style="height:25px;"/>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="padding-left:28%;">
	           <eazytec:label key="upload.logo.info"/>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="padding-left:45%;padding-top:2%;">
	            <button type="submit" name="upload" class="btn btn-primary" onclick="bCancel=false;">
	                <i class="icon-upload icon-white"></i> <fmt:message key="button.upload"/>
	            </button>
			</div>
		</div>
	</div>									
</form:form> --%>
 
<%-- <div class="container-fluid" id="parent_container">
	<div class="row-fluid">
		<div class="span2"></div>
		<div  class="span8 box">
			<eazytec:label key="all.logos" styleClass="control-label style3 style18"/>
			<div id="custom_icon_container" style="overflow:auto"></div>
		</div>
		
		<div class="span2"></div>
	</div>
</div>	 --%>

<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="logo.upload.title"/></h2>
	</div>
	<div class="height10"></div>
</div>
<div class="row-fluid">
	<div class="span12">
		<div class="widget">
			<div class="widget-header">
				<div class="title">
					<span class="fs1" aria-hidden="true"></span><fmt:message key="logo.upload.upload"/>
				</div>
			</div>
			<div class="widget-body">
				<form:form commandName="fileUpload" method="post" class="form-horizontal no-margin" action="bpm/fileupload/uploadLogoImage" enctype="multipart/form-data" id="uploadForm">
					
					<div class="control-group" id="parent_container">
						<div class="checkbox-label browse_button" >
							<span class="fontSize14"><fmt:message key="logo.upload.upload"/></span>
	              				<input type="file" name="file" id="file_choose" class="height25" />
						</div>
					</div>
					
					<div class="control-group browse_button" >
						<eazytec:label key="upload.logo.info"/>
					</div>
				
					<div class="control-group browse_button">
						<button type="submit" name="upload" class="btn btn-primary" onclick="bCancel=false;">
               				<i class="icon-upload icon-white"></i> <fmt:message key="button.upload"/>
           				</button>
					</div>
				</form:form>	
			</div>
		</div>
	</div>
</div>
<div class="row-fluid">
	<div class="span12">
		<div class="widget">
			<div class="widget-header">
				<div class="title">
					<span></span><fmt:message key="logo.upload.allLogos"/>
				</div>
			</div>
			<div class="widget-body">
				<div class="control-group" id="parent_container" >
					<div id="custom_icon_container"  class="overflow"></div>
				</div>	
			</div>
		</div>
	</div>
</div>
	<%-- <div class="row-fluid">
		<div class="span12">
			<div class="widget">
				<div class="widget-body">
					<form:form commandName="fileUpload" method="post" action="bpm/fileupload/uploadLogoImage" enctype="multipart/form-data" id="uploadForm" class="form-horizontal no-margin">
						
						<div class="control-group" id="parent_container">
							<div class="controls browse_button">
								<eazytec:label key="upload.logo" styleClass="control-label"/>&nbsp;
                				<input type="file" name="file" id="file_choose" style="height:25px;"/>
							</div>
						</div>
						
						<div class="control-group" id="parent_container">
							<div class="controls">
								<eazytec:label key="upload.logo.info"/>
							</div>
						</div>
						
						<div class="control-group" >
							<div class="controls upload_button">
								<button type="submit" name="upload" class="btn btn-primary" onclick="bCancel=false;">
	                				<i class="icon-upload icon-white"></i> <fmt:message key="button.upload"/>
	            				</button>
							</div>
						</div>
						
						<div class="control-group" >
							<div class="controls" id="parent_container">
								<eazytec:label key="all.logos"/>
								<div id="custom_icon_container"></div>
							</div>
						</div>
						
					</form:form>	
				</div>
			</div>
		</div>
	</div> --%>

	
<script type="text/javascript">
$(document).ready(function(){
	getAllLogos();
	fileOnChange();
	$("#custom_icon_container").height($("#target").height()-360);
});

function getAllLogos(){
	$.ajax({
		url: 'bpm/fileupload/getAllLogos',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			var htmlContent = "<table style='background-color:#DEDEDE;border:2px solid #fff;' width='100%'><tr>";
			for(var index = 0; index < response.length; index++){
				if(index % 3 == 0){
					htmlContent = htmlContent + "</tr><tr>";
				}
				htmlContent = htmlContent + "<td class='custom-icon-td' align='center' style='padding:5px;border:2px solid #fff;'><div class='delete-image-zone-div displayNone'><img src='/images/remove.png' class='btn-delete' onClick='removeLogo(\""+response[index]+"\");' id='button-zone'></div><img title='Click to change the logo.' onclick='selectLogo(\""+response[index]+"\");' class='cursor-pointer'  src='"+response[index]+"'/></td>";
			}
			if(response.length == 1){
				htmlContent = htmlContent + "<td align='center' width='33%' style='padding:5px;border:2px solid #fff;'></td><td width='33%' align='center' style='padding:5px;border:2px solid #fff;'></td>";
			}else if(response.length == 2){
				htmlContent = htmlContent + "<td align='center' width='33%' style='padding:5px;border:2px solid #fff;'></td>";
			}
			htmlContent = htmlContent + "</tr></table>";
			$("#custom_icon_container").html(htmlContent);
			
			$(".custom-icon-td").mouseover(function(){
			    $(this).find(".delete-image-zone-div").show();
			}).mouseout(function(){
			    $(this).find(".delete-image-zone-div").hide();
			});
		} 
	});
}

function removeLogo(src){
	var status="";
	var message="";
	$.msgBox({
		title : form.title.confirm,
		content : form.msg.confirmToDeleteLogo,
		type : "confirm",
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
				$.ajax({
					url: 'bpm/fileupload/removeLogo?src='+src,
				    type: 'GET',
				    dataType: 'json',
					async: false,
					success : function(response) {
						if(response.status=="success"){
							status="success";
						}else{
							status="error";
						}
						message=response.message;
					} 
				});
			}
		},
		afterClose : function() {
			if(status=="success"){
				$.msgBox({
					title : form.title.success,
					content : message,
					type : "success",
					afterClose : function() {
						getAllLogos();
					}
				});	
			}else if(status=="error"){
				$.msgBox({
					title : form.title.error,
					content : message,
					type : "error"
				});
			}
		}
	});
}

var url = window.URL || window.webkitURL;
function fileOnChange(){
	$("#file_choose").change(function(e) {
        var chosen = this.files[0];
        var image = new Image();
        image.onload = function() {
            if(this.width > 200 && this.height > 45){
            	$.msgBox({
        			title : form.title.message,
        			content : form.msg.maximumLogoSize,
        			afterClose : function() {
        				$("#file_choose").val('');
        			}
        		});
            }
        };
        image.onerror = function() {
        	$.msgBox({
    			title : form.title.message,
    			content : form.msg.uploadImageFileOnly,
    			afterClose : function() {
    				$("#file_choose").val('');
    			}
    		});
        };
        image.src = url.createObjectURL(chosen);                    
	});
}

function selectLogo(imgSrc){
	$.ajax({
		url: 'bpm/fileupload/setSelectedLogoAsActive?logoPath='+imgSrc,
	    type: 'GET',
	    dataType: 'json',
		success : function(response) {
			var res = eval(response);
   			if(res.length == 1){
				$("#bpm_logo").attr("src", res);
			}
		}
	});
}

function clearFileUploadObj(){
	$("#file_choose").val('');
}
</script>
