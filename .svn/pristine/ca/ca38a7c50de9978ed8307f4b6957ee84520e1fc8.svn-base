
<div id="print_preview_form">
	<div class="container-fluid" id="print_preview_template_container">
		<div id="wizard">
<!-- 			<div class="row-fluid" > -->
<!-- 				<div class="span8"> -->
					<ol id="print_template">
						<li id="tab_1"><fmt:message key="form.template.source"/></li>
						<li id="tab_2"><fmt:message key="form.template.preview"/></li>
					</ol>
					<a href="#" onClick="showFormFieldList();" style="color:blue;" class="pull-right"><fmt:message key="form.formFieldList"/></a>
<!-- 					<div class="wizard-steps"> -->
<!-- 						<ul id="wizardTabs"> -->
<!-- 							<div class="active-step"> -->
<!-- 								<a href="#" id="wizardTab-1" class="fontSize13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
<!-- 									<fmt:message key="form.template.source"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
<!-- 								</a> -->
<!-- 							</div> -->
<!-- 							<div class="completed-step"> -->
<!-- 								<a href="#" id="wizardTab-2" onclick="previewHtmlContent();" class="fontSize13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
<!-- 									<fmt:message key="form.template.preview"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
<!-- 								</a> -->
<!-- 							</div> -->
<!-- 						</ul> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div class="span4" style="margin-top:20px;"> -->
<!-- 					<a href="#" onClick="showFormFieldList();" style="color:blue;" class="pull-right">Form Field List</a> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<div id="wizardTab-content-1" class="well">
				<div class="row-fluid" id="print_preview_template_tr">
					<div class="span12 height-per-100">
						<textarea id="print_template_content" rows="10" cols="10" class="width-per-95 height-per-95"></textarea>
					</div>
				</div>
			</div>
			<div id="wizardTab-content-2" class="well">
				<div class="row-fluid" id="print_preview_template_tr">
					<div class="span12">
						<div id="form_template_preview"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="mar-B15 mar-R15  mar-T10" align="right">
	<input type="button" value='<fmt:message key="button.save"/>' onclick="saveFormPrintTemplate();" class="btn btn-primary"/>
	<input type="button" value='<fmt:message key="form.clear"/>' onclick="clearHtmlContent();" class="btn btn-primary"/>
</div>
<div id="template_preview_src"></div>
<script type="text/javascript">

$(document).ready(function(){
	var height = $("#target").height();
	$("#print_template li").each(function() {
		$(this).click(function() {
			var tabId = $(this).attr('id');
			if(tabId == 'tab_2'){
				previewHtmlContent();
			} else if(tabId == 'tab_1'){
				$( "#print_preview_template_tr" ).height(height - 325);
			}
		});
	});
	$( "#print_preview_template_tr" ).height(height - 325);
});

$(function(){
	changeWizardByTab();
	var height = $("#target").height();
	$( "#print_preview_template_container" ).height(height - 155);
	//$( "#print_preview_template_tr" ).height(height - 325);
	cloneStyleSheet();
	$( "#print_preview_template_iframe" ).height(height - 325);
	$("#print_template_content").val($("#printTemplate").val());
});

function previewHtmlContent(){
	var htmlContent = $( "#print_template_content" ).val();
	$('#print_preview_template_iframe').contents().find('body').html(htmlContent);
}

function clearHtmlContent(){
	$( "#print_template_content" ).val("");
	$('#print_preview_template_iframe').contents().find('body').html("");
}

function showFormFieldList(){
	$("#template_preview_src").html($("#htmlSourceView").val());
	var htmlContent = "<div class='container-fluid'  style='min-height:264px;overflow:auto;'>";
	htmlContent = htmlContent + "<div class='row-fluid'><div class='span12' style='padding-bottom:15px;font-style:italic;' >Use the key in the template for display the value.</div></div>";
	htmlContent = htmlContent + "<div class='row-fluid' style='font-weight:bold;background:#DEDEDE;'><div class='span5 pad-L20'>Field Name</div><div class='span1'></div><div class='span6 pad-L20'>Key</div></div>";
	$("#template_preview_src").find("input,select,textarea,img").each(function(index){
		var name = $(this).attr("name");
		var id = $(this).attr("id");
		if( id != 'subFormAdd' && id != 'subFormDelete') {
			htmlContent = htmlContent + "<div class='row-fluid'><div class='span5 pad-L5' >"+name+"</div><div class='span1'> : </div><div class='span6'> &#36;&#123;"+name+"&#125;</div></div>";
		}
	});
	var htmlContent = htmlContent + "</div>";
	$("#template_preview_src").html(htmlContent);
	openFormFieldDialog();
}

function openFormFieldDialog(){
	 $myDialog = $("#template_preview_src");
	 $myDialog.dialog({
     	width: 300,
      	height: 300,
      	modal: true,
      	title: "Form Fields - "+$("#formName").val()
	  });
	 $myDialog.dialog("open");
}

function saveFormPrintTemplate(){
	$.ajax({	
		url: '/form/saveFormPrintTemplate',
		type: 'POST',
		async: false,
		data: { formId: $("#id").val(), printTemplate: $("#print_template_content").val()},
		dataType: 'json',
		success : function(response) {
			if(response.isSaved == 'true'){
				validateMessageBox(form.title.success, response.message, "success");
			}else{
				validateMessageBox(form.title.error, response.message, "error");
			}
		}
  	});
}
$("#wizard").bwizard();
$(".bwizard-buttons").css({display: 'none'});
var newHeight = $("#target").height();
var wizardHeight = parseInt(newHeight) - 188;
$('#wizard').css({height: wizardHeight, overflow: 'auto'});
</script>
