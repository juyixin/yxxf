<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <%-- <title><fmt:message key="mypage.title"/></title>
    <meta name="heading" content="<fmt:message key='mypage.title'/>"/> --%>
</head>
<body>
<div class="span2 palette border-right-gray" id="paletteList">
	<ul class="section1 menu">
    	<li><a class="menuitem1 pad-B8"><span class="style7"><span class="style9"><fmt:message key="form.palette.capital"/></span></img></span> <img src="/images/easybpm/common/palette_icon.png" onClick="hidePalette();" class="floatRight"/></a>
    		<ul class="tabBackground mar-B5 height20"><span class="style9 pad-L10"><fmt:message key="form.edit"/></span></ul>
        	<ul id="formSubMenu2" class="submenu pad-B5">
				<li id="formEdit">
					<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/property.png"/></div> <div class="pad-B0 pad-L22 border-bot-dot1"> <fmt:message key="form.editForm"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
				</li>
			</ul>
    		<ul class="tabBackground mar-B5 height20"><span class="style9 pad-L10"><fmt:message key="form.controls"/></span></ul>
			<ul class="submenu" id="formSubMenu">
             <!--  <li id="Header"> <a><div style="float:left; padding-top:3px; "><img src="/images/easybpm/form/heading.png" /></div><div style="padding-bottom:5px; padding-left:22px; border-bottom:dotted 1px;">Heading<img src="images/right_arrow.png" style="float:right;"/></div></a></li>   -->
              <li id="Text" type="string">
              	<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/textbox.png" /></div><div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.textBox"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
              <li id="TextArea" type="string">
              	<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/textarea.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.textArea"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
              <li id="Select" type="enum">
              	<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/dropdown.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.dropDown"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
              <li id="Radio" type="enum">
              	<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/radiobutton.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.radioButton"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
              <li id="Checkbox" type="enum">
              	<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/checkbox.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.checkBox"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li> 
              <li id="File" type="string">
              	<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/fileupload.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.fileUpload"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
              <li id="Email" type="string">
              	<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/email.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.email"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
              <li id="Date" type="date">
              	<a> <div class="floatLeft pad-T0"><img src="/images/easybpm/form/datetime.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.dateAndTime"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
              <li id="SubmitButton" type="string">
              	<a> <div class="floatLeft pad-T0"><img src="/images/easybpm/form/submit.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.submitButton"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
              </li>
               <li id="UserDefineButton" type="string">
              		<a> <div class="floatLeft pad-T0"><img src="/images/easybpm/form/property.png" /></div> <div class="pad-B5 pad-L22 border-bot-dot1"><fmt:message key="form.newButton"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
               </li>
               <li id="showButtons">
					<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/property.png"/></div> <div class="pad-B0 pad-L22 border-bot-dot1"><fmt:message key="form.showButtons"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
			   	</li>
        	</ul>
        	<ul id="formSubMenu3" class="submenu">
				<li id="formAddCol">
					<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/property.png"></div> <div class="pad-B5 pad-L22 border-bot-dot1 height-auto"><fmt:message key="form.addCol"/> <img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
				</li>
				<li id="addSubForm">
					<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/property.png"></div> <div class="pad-B5 pad-L22 border-bot-dot1 height-auto"><fmt:message key="form.addSubForm"/><img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
				</li>
				<li id="addTemplate">
					<a> <div class="floatLeft pad-T3"><img src="/images/easybpm/form/property.png"></div> <div class="pad-B0 pad-L22 border-bot-dot1 height-auto"><fmt:message key="form.addTemplate"/> <img src="images/right_arrow.png" class="floatRight pad-T6"/></div> </a>
				</li>			
			</ul>
    	</li>
    </ul>
</div>
<div id="hiddedPalette" class="span2 palette" style="display:none;">
	<ul class="section1 menu listStyle-none">
	  <li>
	  <a class="menuitem1"></span> <img src="/images/easybpm/common/palette_hide_icon.png"/></a>
		  <ul class="submenu listStyle-none">
	        <!-- <li><a><div style="float:left;">  <img src="images/heading.png" /> </div><div style="padding-bottom:5px;"> </div></a></li>   -->
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/textbox.png" /></div> <div class="pad-B5 pad-L22"></div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/textarea.png" /></div> <div class="pad-B5 pad-L22"></div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/dropdown.png" /></div> <div class="pad-B5 pad-L22"> </div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/radiobutton.png" /></div><div class="pad-B5 pad-L22"></div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/checkbox.png" /></div> <div class="pad-B5 pad-L22"></div></a></li> 
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/fileupload.png" /></div> <div class="pad-B5 pad-L22"> </div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/email.png" /></div> <div class="pad-B5 pad-L22"> </div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/datetime.png" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/submit.png" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/save_btn.jpg" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/print_btn.jpg" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/close_btn.jpg" /></div><div class="pad-B0 pad-L22 height-auto"></div></a><br/><br/><br/></li>
	      </ul>
	  </li>
	</ul>
</div>

<div id="hiddedFormPalette" class="span2 palette" style="display:none;">
	<ul class="section1 menu listStyle-none">
	  <li>
	  <a class="menuitem1"></span> <img src="/images/easybpm/common/palette_hide_icon.png" onClick="showPalette();"/></a>
		  <ul class="submenu listStyle-none">
	        <!-- <li><a><div style="float:left;">  <img src="images/heading.png" /> </div><div style="padding-bottom:5px;"> </div></a></li>   -->
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/textbox.png" /></div> <div class="pad-B5 pad-L22"></div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/textarea.png" /></div> <div class="pad-B5 pad-L22"></div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/dropdown.png" /></div> <div class="pad-B5 pad-L22"> </div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/radiobutton.png" /></div><div class="pad-B5 pad-L22"></div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/checkbox.png" /></div> <div class="pad-B5 pad-L22"></div></a></li> 
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/fileupload.png" /></div> <div class="pad-B5 pad-L22"> </div></a></li>
	        <li><a><div class="pad-T3"> <img src="/images/easybpm/form/email.png" /></div> <div class="pad-B5 pad-L22"> </div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/datetime.png" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/submit.png" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/save_btn.jpg" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/print_btn.jpg" /></div><div class="pad-B0 pad-L22"></div></a></li>
	        <li><a><div class="pad-T0"> <img src="/images/easybpm/form/close_btn.jpg" /></div><div class="pad-B0 pad-L22 height-auto"></div></a><br/><br/><br/></li>
	      </ul>
	  </li>
	</ul>
</div>

<div class="span10" id="sourceView">
	<div class="row-fluid background-white">
		<div class="span12">
			<div class="titleBG">
	     		<span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="form.createForm"/> </span><span id="showFormName" class="floatLeft fontSize14 pad-T10"></span>
	     		<span class="floatRight fontSize14 pad-L10 pad-T2">
	     			<div id="toggle-fullscreen-div" style="cursor: pointer;">
	     				<img  src="/images/easybpm/common/min.png" />
	     			</div>
	     		</span>
	     		<div class="floatRight pad-T2"><ul id="tabs"><li id="tabs-1" class="selected"><fmt:message key="form.design "/></li><li id="tabs-2"><fmt:message key="form.hTMLSource"/></li><!-- <li id="tabs-3">XML Source</li> --><li id="tabs-4"><fmt:message key="form.preview"/></li></ul></div>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span12">
		  	<div id="tabs-content" class="">	
			  	<div id="tab-content-1" class="height-per-100">
				   	<div class="row-fluid background-white">
						<div class="span12 height-per-100">
							<div id="main-f1" class="height-per-91 overflow pad-L10"></div>
						</div>
						<div class="span12 height-per-100">
						<div class="span11"/>
						<div class="span1">
							<div id="submitForm" class="mar-B15 mar-R10 mar-T5" align="right">
								<form:form  commandName="form" method="post" action="bpm/form/saveForm" autocomplete="off" modelAttribute="form" id="saveForm" onClick="return isDublicateFormName();"> 
									<input type="hidden" id="formName" name="formName"/>
									<input type="hidden" id="description" name="description"/>
									<input type="hidden" id="xmlSource" name="xmlSource"/>
									<input type="hidden" id="htmlSource" name="htmlSource"/>
									 <input type="submit" value="Save Form" class="btn btn-primary normalButton mar-L10 padding0 line-height0"/>
									<!--<input type="button" value="Save Form" class="btn btn-primary normalButton"/>-->
								</form:form>
							</div>
							</div>
						</div>
				   	</div>
				    <div class="row-fluid height-per-24 borderRadius5 width-per-100" id="fieldProperties">
				   		<!-- <div class="span12"> -->
							<div class="tabBackground width-per-100">
								<ul id="propertyTab">
									<li id="propertyTabs-1" class="selected"><span class="pad-L5 fontSize12 fontFamily-italic"><fmt:message key="form.generalProperties"/></span></li>
									<li id="propertyTabs-2"><span class="pad-L5 fontSize12 fontFamily-italic"><fmt:message key="form.advancedProperties"/></span></li>
								</ul>
							</div>
							<div id="propertyTab-content" class="width-per-100">
								<div id="properties_error" class= "displayNone pad-L10 pad-T6 width-per-95"></div>
								<div id="propertyTab-content-1"><div id="generalProperties" width="100%" class="pad-T10 min-height113 width-per-100"></div></div>
								<div id="propertyTab-content-2"><div id="advancedProperties" width="100%" class="pad-T10 min-height113 width-per-100"></div></div>
							</div>
				   		<!-- </div> -->
				   	</div>
			   	</div>
	 			   	<div id="tab-content-2" class="textalign-left height-per-95">
			   		<div class="row-fluid background-white height-per-95">
				   		 <pre class="mxml" name="htmlsource" id="htmlsource" contenteditable="true"></pre>
				   		 <div id="updateForm" class="mar-B15 mar-R15" align="right">
	                           <input type="button" value="Apply" onclick= "load_html();" class="btn btn-primary normalButton"/>
	                     </div>
			   		</div>
			   	</div>
			   	<div id="tab-content-3" class="textalign-left height-per-95">
			   		<div class="row-fluid background-white height-per-95">
						<pre class="mxml" name="xmlsource" id="xmlsource" contenteditable="true"></pre>
						<div id="updateForm" class="mar-B15 mar-R10" align="right">
							<input type="button" value="Update Form" onclick= "load_xml();" class="btn btn-primary updateFormButton"/>
						</div>
					</div>
				</div>
		  		<div id="tab-content-4" class="textalign-left height-per-95 width-per-97">
					<div class="row-fluid height-per-95 background-white overflow pad-L10"><div id="previewdst"></div></div>
				</div>
				
			</div>
		</div>
	</div><input type="hidden" id="htmlSource" name="htmlSource"/>
</div>
<div id="form_button_dialog"></div>
<!-- end new ui  -->
<div class="clear"></div>
<script type="text/javascript">
	var selected = null ;
	function setFormValues(){
		var formName = rblFormArray[ IDFORM ].e_name;
		var xmlSource = $("#xmlsource").text();
		var htmlSource = $("#htmlsource").text();
		var description = rblFormArray[ IDFORM ].e_desc;
		if(formName != ""){
			$("#formName").val(formName);
			document.getElementById("showFormName").innerHTML = "&nbsp;- " + formName;
		}
		$("#xmlSource").val(xmlSource);
		$("#htmlSource").val(htmlSource);
		$("#description").val(description);
	}
	
	$(document).ready(function(){ 
		var height = $("#target").height();
		$("#main-f1").height(parseInt(height)-235)
		$.datepicker.setDefaults($.extend({showMonthAfterYear: false}, $.datepicker.regional['fr']));
		$('#tabs-content').addClass('js');	
		$("#tabs li").each(function() {
			$(this).click(function() {
				var formName=$("#formName").val();
				if(formName!=""){
					var tabId = $(this).attr('id');
				    tabId = tabId.split('-');
				    formTabActions(tabId[1]);
				    var tabContent = document.getElementById('tab-content-' + tabId[1]);
				    tabContent.style.display = 'block';
				    $(this).addClass('selected');
				    $(this).siblings().removeClass('selected');	
				    $(tabContent).siblings().css('display','none');
				}else{
					getFormNameInMsgBox();
				}
			});
	    });
		$('#formAddCol').click(function() { addCol();});
		$('#addSubForm').click(function() { 
			var formName=$("#formName").val();	
			if(formName!=""){
				addFormPanel();
			}else{
				getFormNameInMsgBox();
			}
		});
		$('#formEdit').click(function() { showFormPanel();});
		$('#addTemplate').click(function() {
			var formName=$("#formName").val();	
			if(formName!=""){
				addTemplatePanel();
			}else{
				getFormNameInMsgBox();
			}
		});
		initDrop() ; //in rbl_base.js (from this go to addNewElement() in rbl_elt.js)
		$(".connectedSortable").resizable();
		enableRowSelectable(".connectedSortable li"); 
		initFormData(); //in rbl_form.js
		if(nb_col==0){
			addCol();
		}else{
			nb_col=0;
			addCol();
		} 
		setPropertiesTabs();
		
		$('#showButtons').click(function() { showFormButtons();});
	});
	
	function showFormButtons(){
		var loggedInUser = "${pageContext.request.remoteUser}";
		$.ajax({
			url: 'bpm/form/getFormButtons',
		    type: 'GET',
		    dataType: 'json',
			async: false,
			success : function(response) {
				var htmlContent = "<table  width='100%'><tr>";
				for(var index = 0; index < response.length; index++){
					if(index % 5 == 0){
						htmlContent = htmlContent + "</tr><tr>";
					}
					htmlContent = htmlContent + "<td style='padding:10px;' align='center'> "
					+ "<input type='button' value='"+response[index].name+"' "
					+ "style='"+response[index].style+"' class='btn btn-primary normalButton' "
					+ "onClick='selectButton(\""+response[index].id+"\", \""+response[index].name+"\", \""+response[index].style+"\", \""+response[index].func+"\", \""+response[index].createdBy+"\", \""+loggedInUser+"\");' /></td>";
				}
				htmlContent = htmlContent + "</tr></table>";
				$("#form_button_dialog").html(htmlContent);
				openSelectDialog('form_button_dialog', 500, 'Select button');
			} 
		});
	}
	
	
	function openSelectDialog(divId, width, title){
		 clearPreviousPopup();
		 $myDialog = $("#"+divId);
		 $myDialog.dialog({
	        width: width,
	        height: 'auto',
	        modal: true,
	        title: title
		 });
		 $myDialog.dialog("open");
	}

	function selectButton(id, name, style, func, createdBy, loggedInUser){
		selectButtonToBeAddInForm(id, name, style, func, createdBy, loggedInUser);
		$("#form_button_dialog").dialog("close");
	}
	
	function hidePalette(){
		$(document.getElementById('paletteList')).css('display','none');
		$(document.getElementById('hiddedFormPalette')).css('display','block');
		$(document.getElementById('sourceView')).css('width','96.9%');
	}
	
	function showPalette(){
			$(document.getElementById('paletteList')).css('display','block');
			$(document.getElementById('hiddedFormPalette')).css('display','none');
			$(document.getElementById('sourceView')).css('width','85%');
	} 
	$('#toggle-fullscreen-div').on('click', function(){
		activateFullScreenMode('target');
		setTimeout(function(){
			var height = $("#target").height();
	    	$("#htmlsource").css('height',parseInt(height)-105);
	    	$("#previewdst").css('height',parseInt(height)-105);
	    	$("#main-f1").css('height',parseInt(height)-255);
		},100);
	});
	  
</script>
</body>
   
