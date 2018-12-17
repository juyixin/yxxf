<%@ include file="/common/taglibs.jsp"%>

 <spring:bind path="fileUpload.*">
   <%@ include file="/common/messages.jsp" %>
</spring:bind>
<%--
<div class="span12 box">
 	<h2><fmt:message key="custom.icon.upload.title"/></h2>
</div>
<form:form commandName="fileUpload" method="post" action="bpm/fileupload/uploadCustomIcon" enctype="multipart/form-data" id="uploadForm" cssClass="form-horizontal">
	<div class="container-fluid" id="parent_container">
		<div class="row-fluid">
			<div class="span12" style="padding-left:25%;padding-top:2%;">
	            <eazytec:label key="upload.icon" styleClass="control-label style3 style18"/>
                <input type="file" name="file" id="file_choose" style="height:25px;"/>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="padding-left:25%;">
	           <eazytec:label key="upload.icon.info"/>
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
</form:form>
<div class="container-fluid" id="parent_container">
	<div class="row-fluid">
		<div class="span2"></div>
		<div  class="span8 box">
			<eazytec:label key="custom.icons" styleClass="control-label style3 style18"/>
			<div id="custom_icon_container" style="overflow:auto"></div>
		</div>
		
		<div class="span2"></div>
	</div>
</div>		 --%>

	<div class="page-header">
		<div class="pull-left">
			<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="customIcon.title"/></h2>
		</div>
		<div class="height10"></div>
	</div>
	<div class="row-fluid">
		<div class="span12">
			<div class="widget">
				<div class="widget-header">
					<div class="title">
						<span class="fs1" aria-hidden="true"></span><fmt:message key="customIcon.upload"/>
					</div>
				</div>
				<div class="widget-body">
					<form:form commandName="fileUpload" method="post" class="form-horizontal no-margin" action="bpm/fileupload/uploadCustomIcon" enctype="multipart/form-data" id="uploadForm">
						<div class="control-group" id="parent_container">
							<div class="checkbox-label browse_button" >
								<span class="fontSize14"><fmt:message key="customIcon.iconToUpload"/></span>
	                			<input type="file" name="file" id="file_choose" class="height25"/>
							</div>
						</div>
							
						<div class="control-group browse_button" >
							<eazytec:label key="upload.icon.info"/>
						</div>
							
						<div>
							<div class="control-group browse_button">
								<button type="submit" name="upload" class="btn btn-primary" onclick="bCancel=false;">
		               				<i class="icon-upload icon-white"></i> <fmt:message key="button.upload"/>
		            			</button>
							</div>
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
						<span></span><fmt:message key="customIcon.customIcons"/>
					</div>
				</div>
				<div class="widget-body">
					<div class="control-group" id="parent_container">
						<div class="controls">
							<div id="custom_icon_container" class="overflow"></div>
						</div>
					</div>		
				</div>
			</div>
		</div>
	</div>
	
	<%-- <div class="row-fluid">
		<div class="span12">
			<div class="widget">
				<div class="widget-body">
					<form:form commandName="fileUpload" method="post" action="bpm/fileupload/uploadCustomIcon" enctype="multipart/form-data" id="uploadForm" class="form-horizontal no-margin">
						
						<div class="control-group" id="parent_container">
							<div class="controls browse_button">
								<eazytec:label key="upload.icon" styleClass="control-label"/>&nbsp;
                				<input type="file" name="file" id="file_choose" style="height:25px;"/>
							</div>
						</div>
						
						<div class="control-group">
							<div class="controls" >
								<eazytec:label key="upload.icon.info"/>
							</div>
						</div>
						
						<div class="control-group">
							<div class="controls upload_button" >
								<button type="submit" name="upload" class="btn btn-primary" onclick="bCancel=false;">
	                				<i class="icon-upload icon-white"></i> <fmt:message key="button.upload"/>
	            				</button>
							</div>
						</div>
						
						<div class="control-group" id="parent_container">
							<div class="controls">
								<div class="span10">
									<eazytec:label key="custom.icons"/>
									<div id="custom_icon_container" ></div>
								</div>
							</div>
						</div>	
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
	 --%>
<script type="text/javascript">
$(document).ready(function(){
	getCustomIcons();
	fileOnChange();
	$("#custom_icon_container").height($("#target").height()-360);
});

function getCustomIcons(){
	$.ajax({
		url: 'bpm/fileupload/getCustomIcons',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			var htmlContent = "<table style='background-color:#DEDEDE;border:2px solid #fff;' width='100%'><tr>";
			for(var index = 0; index < response.length; index++){
				if(index % 7 == 0){
					htmlContent = htmlContent + "</tr><tr>";
				}
				htmlContent = htmlContent + "<td class='custom-icon-td' align='center' style='padding:5px;border:2px solid #fff;'><div class='delete-image-zone-div displayNone'><img src='/images/remove.png' class='btn-delete' onClick='removeCustomIcon(\""+response[index]+"\");' id='button-zone'></div><img class='cursor-pointer'  src='"+response[index]+"'/></td>";
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

function removeCustomIcon(src){
	var status="";
	var message="";
	$.msgBox({
		title : form.title.confirm,
		content : form.msg.confirmToDeleteIcon,
		type : "confirm",
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
				$.ajax({
					url: 'bpm/fileupload/removeCustomIcon?src='+src,
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
						getCustomIcons();
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
            //alert('Width:'+this.width +' Height:'+ this.height+' '+ Math.round(chosen.size/1024)+'KB');
            if(this.width > 18 && this.height > 18){
            	$.msgBox({
        			title : form.title.message,
        			content : form.msg.maximumImageSize,
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
function clearFileUploadObj(){
	$("#file_choose").val('');
}
</script>
