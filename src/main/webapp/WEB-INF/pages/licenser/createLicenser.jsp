<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %>
<head>
    <title><fmt:message key="licenser.title"/></title>
    <meta name="home" content="licenser"/>
</head>
<script type="text/javascript">
   $(document).ready(function(){
	//loadDateFieldWithImg('expDate');
	   $( '#expDate' ).datepicker({
			dateFormat: 'yy-mm-dd',
			changeYear: true,
			changeMonth: true,
			yearRange: "-0:+50",
			//yearRange:  '2014:2050',
			showOn: "button",
			buttonImage:'/images/easybpm/form/rbl/_cal.gif',
			//maxDate: new Date(),
		});
});   
 /* $(document).ready(function(){
	  changeWizardByTab(); 
	   $( '#expDate' ).datepicker({
			dateFormat: 'yy-mm-dd',
			changeYear: true,
			changeMonth: true,
			yearRange: "-120:+0",
			showOn: "button",
			buttonImage:'/images/easybpm/form/rbl/_cal.gif',
			maxDate: new Date(),
		}); */

function getLincenceKey(){
	var expDate=$("#expDate").val();
	var serialFieldLetter=$("#serialFieldLetter").val();
	var serialFieldNumber=$("#serialFieldNumber").val();
	var versionNumber=$("#versionNumber").val();
	
	if(expDate==""){
		validateMessageBox("Expiry Date Error", "Expiry date should not be empty" , "error","expDate");
		$("#licenserKey").val("");
		return false;
	}
	
	if(serialFieldLetter.length!=2){
		validateMessageBox("Serial Field Error", "Serial text should be two characters" , "error","serialFieldLetter");
		$("#licenserKey").val("");
		return false;
	}
	
    if(serialFieldNumber.length!=4){
    	validateMessageBox("Serial Field Error", "Serial number should be four digits " , "error","serialFieldNumber");
    	$("#licenserKey").val("");
    	return false;
	}
    
	if(versionNumber.length==0){
		validateMessageBox("Version Error", "Version number should not be empty " , "error","versionNumber");
		$("#licenserKey").val("");
		return false;
	}
	
	if(serialFieldNumber==0){
		validateMessageBox("Version Error", "Serial number should not be 0 " , "error","serialFieldNumber");
		$("#licenserKey").val("");
		return false;
	}
	
	if(versionNumber>255){
		validateMessageBox("Version Error", "Version number should not be greater than 255 " , "error","versionNumber");
		$("#licenserKey").val("");
		return false;
	}
	
	var serialField=serialFieldLetter+serialFieldNumber;
	
	$.ajax({	
		url: '/bpm/licenser/getCreatedLicense?expDate='+expDate+'&serialField='+serialField+'&versionField='+versionNumber,
		type: 'GET',
		async: false,
		success : function(response) {
			if(response.sucessMessage){
				$.msgBox({
    				title : form.title.success,
    				content : response.sucessMessage,
    				type : "success",
    				afterClose : function() {
    					$("#"+response.elementId).val(response.licenserKey);
    				}
    			});
			}else{
				validateMessageBox(form.title.error, response.errorMessage , "error",response.elementId);
			}	 
		}
  	});
}
</script>

 <div class="page-header">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="license.title"/></h2>
	</div>
	<div class="height10"></div>
	<div class="height10"></div>
	<div class="height10"></div>
	
<div class="row-fluid">
	<div class="widget">
		<div class="widget-body">
	
		<form:form method="post" action="bpm/admin/userform" class="form-horizontal no-margin">
			<!-- <div class="span12"> -->
				<!-- <div class="span9" style="width:78%"> -->
					
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="licenser.expDate" />
							<div class="controls">
								<input type="text" name="expDate" id="expDate" class="white-background" style="width:23.2%"readonly="true" />
							</div>
						</div>
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="licenser.serialField" />
							<div class="controls">
								<input type="text" maxlength="2" name="serialFieldLetter" id="serialFieldLetter" onkeyup="this.value=this.value.replace(/[^a-z]/g,'');" class="serialText span3 white-background" style="width:12.5%" value="cq"/>
								<input type="number" maxlength="4" name="serialFieldNumber" id="serialFieldNumber" onkeyup="this.value=this.value.replace(/[^0-9]/g,'');" class="serialNumber span3 white-background" style="width:12%" value="0037"/>
								<br/><fmt:message key="licenser.serialField.letterHelp" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						&nbsp;&nbsp;&nbsp;
								<fmt:message key="licenser.serialField.numberHelp" />	
							</div>
						</div>
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="licenser.version" />
							<div class="controls">
								<input type="number" maxlength="3" name="versionNumber" id="versionNumber" onkeyup="this.value=this.value.replace(/[^0-9]/g,'');" class="serialNumber white-background" style="width:23.2%" value="020"/>
								<br/><fmt:message key="licenser.version.numberHelp" />
							</div>
						</div>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="licenser.licenserKey" />
							<div class="controls">
								<input type="text" name="licenserKey" id="licenserKey" class="white-background" style="width:23.2%" readonly="true" />
							</div>
						</div>
						
						<div class="control-group">
							<div class="form-actions no-margin">	
									<button type="button" class="btn btn-primary" name="encodeSettings" onclick="getLincenceKey();" id="encodeSettings" class="cursor_pointer">
										<fmt:message key="licenser.encodeSettings"/>
									</button>
							</div>
						</div>	
					<!-- </div> -->
				
					
			<!-- </div> -->
		</form:form>
	</div>
</div>
</div>

