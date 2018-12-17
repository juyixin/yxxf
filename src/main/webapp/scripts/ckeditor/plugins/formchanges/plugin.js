﻿﻿/**
 *  This plugin create fake element and when double click the element show corresponding element properties 
 *  The dialog definition onOk, onShow, onLoad functionality has been overwritten here 
 *  @author mahesh
 */
(function(){
	var onloadEventScript = "";
	CKEDITOR.plugins.add('formchanges',{
		init:function(initEvent){
			initEvent.on('doubleclick',function(event){
				var elementData=event.data.element;
				var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
				if(is_chrome) {
					CKEDITOR.instances.editor1.document.getById('form_div').removeAttribute("contenteditable");
					//CKEDITOR.instances.editor1.document.getById('form_div').setAttribute("contenteditable","inherit");
				}
				var isSubFormEle = event.data.element.$.getAttribute("data-cke-realelement");
				if(isSubFormEle == null || isSubFormEle.indexOf("subForm_") < 0){
					if(elementData.hasClass('cke_radio')||elementData.hasClass('cke_radio_checked'))
						event.data.dialog='radio';
					else if(elementData.hasClass('cke_checkbox')||elementData.hasClass('cke_checkbox_checked'))
						event.data.dialog='checkbox';
					else if(elementData.hasClass('cke_select'))
						event.data.dialog='select';
					else if(elementData.hasClass('cke_textfield'))
						event.data.dialog='textfield';
					else if(elementData.hasClass('cke_textarea'))
						event.data.dialog='textarea';
					else if(elementData.hasClass('cke_datefield'))
						event.data.dialog='datetimepicker';
					else if(elementData.hasClass('cke_userlist'))
						event.data.dialog='userlist';
					else if(elementData.hasClass('cke_departmentlist'))
						event.data.dialog='departmentlist';
					else if(elementData.hasClass('cke_rolelist'))
						event.data.dialog='rolelist';
					else if(elementData.hasClass('cke_grouplist'))
						event.data.dialog='grouplist';
	                else if(elementData.hasClass('cke_numberfield'))
						event.data.dialog='numberfield';
			      	else if(elementData.hasClass('cke_label'))
						event.data.dialog='label';
	                else if(elementData.hasClass('cke_fileupload'))
						event.data.dialog='fileupload';
	                else if(elementData.hasClass('cke_button')||elementData.hasClass('cke_submit_btn'))
						event.data.dialog='button';
	                else if(elementData.hasClass('cke_richtextbox'))
						event.data.dialog='richtextbox';
	                else if(elementData.hasClass('cke_gridview'))
						event.data.dialog='gridviewcontrol';
	                else if(elementData.hasClass('cke_autocomplete'))
						event.data.dialog='autocomplete';
	                else if(elementData.hasClass('onloadevent')){
	                	onloadEventScript = elementData.$.innerHTML;
	                	event.data.dialog='onloadevent';
	                }
					else return null;
				}
			});
			
			if(initEvent.contextMenu)
				initEvent.contextMenu.addListener(function(event,elementData){
					if(event&&event.data('cke-real-element-type')=='radiofield'&&(event.hasClass('cke_radio')||event.hasClass('cke_radio_checked')))
						return{radio:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='checkboxfield'&&(event.hasClass('cke_checkbox')||event.hasClass('cke_checkbox_checked')))
						return{checkbox:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='selectfield'&&(event.hasClass('cke_select')))
						return{select:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='textfield'&&(event.hasClass('cke_textfield')))
						return{textfield:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='textareafield'&&(event.hasClass('cke_textarea')))
						return{textarea:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='datefield'&&(event.hasClass('cke_datefield')))
						return{datetimepicker:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='userlist'&&(event.hasClass('cke_userlist')))
						return{userlist:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='departmentlist'&&(event.hasClass('cke_departmentlist')))
						return{departmentlist:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='rolelist'&&(event.hasClass('cke_rolelist')))
						return{rolelist:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='grouplist'&&(event.hasClass('cke_grouplist')))
						return{rolelist:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='numberfield'&&(event.hasClass('cke_numberfield')))
						return{numberfield:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='label'&&(event.hasClass('cke_label')))
						return{label:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='fileupload'&&(event.hasClass('cke_fileupload')))
						return{fileupload:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='button'&&(event.hasClass('cke_button')||event.hasClass('cke_submit_btn')))
						return{button:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='richtextbox'&&(event.hasClass('cke_richtextbox')))
						return{richtextbox:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='gridviewcontrol'&&(event.hasClass('cke_gridview')))
						return{richtextbox:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='autocomplete'&&(event.hasClass('cke_autocomplete')))
						return{autocomplete:CKEDITOR.TRISTATE_OFF};
					else if(event&&event.data('cke-real-element-type')=='onloadevent'&&(event.hasClass('onloadevent')))
						return{onloadevent:CKEDITOR.TRISTATE_OFF};
					else return null;
				});
			},
			afterInit:function(initEvent){
				var eventData=initEvent.dataProcessor,eData=eventData&&eventData.dataFilter;
				if(eData)
					eData.addRules({
						elements:{
							img:function(element){
								var imageElementAttr = element.attributes;
								if(imageElementAttr.class == 'richtextbox'){
									var fakeElement =  initEvent.createFakeParserElement(element,'cke_richtextbox drag','richtextbox',false);
									return addStyleToCKEDITORFakeElement(fakeElement, element);
								}else if(imageElementAttr.class == 'gridviewcontrol'){
									var fakeElement =  initEvent.createFakeParserElement(element,'cke_gridview drag','gridviewcontrol',false);
									return addStyleToCKEDITORFakeElement(fakeElement, element);
								}
							},
                              label: function(element){
								var fakeElement = initEvent.createFakeParserElement(element,'cke_label drag','label',false);
									return addStyleToCKEDITORFakeElement(fakeElement, element);
							},
							input:function(element){
								
								var elementAttr = element.attributes;
								switch(elementAttr.type){
									case 'radio':
										if(elementAttr.checked){
											var fakeElement =  initEvent.createFakeParserElement(element,'cke_radio_checked drag','radiofield',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}else{
											var fakeElement =  initEvent.createFakeParserElement(element,'cke_radio drag','radiofield',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}
										break;
									case 'checkbox':
										if(elementAttr.checked){
											var fakeElement =  initEvent.createFakeParserElement(element,'cke_checkbox_checked drag','checkboxfield',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}else{ 
											var fakeElement =  initEvent.createFakeParserElement(element,'cke_checkbox drag','checkboxfield',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}
										break;
									case 'text':
										var clsName = $(element).attr("class");
										if(clsName && clsName.indexOf("autocomplete") > -1){
											 var fakeElement =initEvent.createFakeParserElement(element,'cke_autocomplete drag','autocomplete',false);
											 return addStyleToCKEDITORFakeElement(fakeElement, element);
										} else {
											var fakeElement = initEvent.createFakeParserElement(element,'cke_textfield drag','textfield',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}
										break;
									case 'password':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_textfield drag','textfield',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'email':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_textfield drag','textfield',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'userlist':
										var obj = [];
										initEvent.createFakeParserElement( element, 'cke_hidden displayNone user-list-hidden', 'hiddenfield' );
//										initEvent.createFakeParserElement(element,'cke_userlist','userlist',false);
										var fakeElement = initEvent.createFakeParserElement(element,'cke_userlist drag','userlist',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'departmentlist':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_departmentlist drag','departmentlist',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'rolelist':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_rolelist drag','rolelist',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'grouplist':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_grouplist drag','grouplist',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'date':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_datefield drag','datefield',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'number':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_numberfield drag','numberfield',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'label':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_label drag','label',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'file':
										var fakeElement = initEvent.createFakeParserElement(element,'cke_fileupload drag','fileupload',false);
										return addStyleToCKEDITORFakeElement(fakeElement, element);
										break;
									case 'button':
										if(elementAttr.type == "submit"){
											var fakeElement = initEvent.createFakeParserElement(element,'cke_submit_btn drag','button',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}else{ 
											var fakeElement = initEvent.createFakeParserElement(element,'cke_button drag','button',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}
										break;
									case 'submit':
										if(elementAttr.type == "submit"){
											var fakeElement = initEvent.createFakeParserElement(element,'cke_submit_btn drag','button',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}else{ 
											var fakeElement = initEvent.createFakeParserElement(element,'cke_button drag','button',false);
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}
										break;
									case 'hidden':
										if(elementAttr.userlistrandom)
											var fakeElement = initEvent.createFakeParserElement( element, 'cke_hidden displayNone user-list-hidden', 'hiddenfield' );
											return addStyleToCKEDITORFakeElement(fakeElement, element);
										}
							},
							select:function(element){
								var fakeElement = initEvent.createFakeParserElement(element,'cke_select drag','selectfield',false);
								return addStyleToCKEDITORFakeElement(fakeElement, element);
							},
							textarea:function(element){
								var fakeElement = initEvent.createFakeParserElement(element,'cke_textarea drag','textareafield',false);
								return addStyleToCKEDITORFakeElement(fakeElement, element);
							}
						},
					});
				},
				requires:['fakeobjects']
	});
	
	CKEDITOR.on('dialogDefinition',function(dialogEvent){
		var dialogType = dialogEvent.data.name;
		var dialogDefination = dialogEvent.data.definition;
		var editor=dialogEvent.editor;
		if(dialogType=='radio'){
			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){	
					var dialog = this;
					dialog.radioField = false;
					dialog.editMode = false;
					var selection = editor.getSelection();
					var element = selection.getSelectedElement();
					if(element && element.data('cke-real-element-type') && element.data('cke-real-element-type') == 'radiofield'){
						dialog.editMode = true;
						dialog.radioField = element;
						element=editor.restoreRealElement(dialog.radioField);
						dialog.setupContent(element);
						selection.selectElement(dialog.radioField);
					}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var radioName = currentDialog.getValueOf('info','name');
				var isChecked=currentDialog.getValueOf('info','checked_radio')==true?true:false;
				
				
					
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', 'radio' );
				element.setAttribute('id', document.getElementById('englishName').value+"_"+radioName);
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var dictRadioName = currentDialog.getValueOf('info','radio_data_choice');
				if(isRequired == true){
					var dictRadioName = currentDialog.getValueOf('info','radio_data_choice');
					if(dictRadioName.toLowerCase() == "dynamic" ){
						element.setAttribute( 'class', "data_dictionary_radio mandatory" );
						element.setAttribute( 'isrequired', "mandatory" );
					} else {
						element.setAttribute( 'class', "mandatory" );
					}
				}else {
					var dictRadioName = currentDialog.getValueOf('info','radio_data_choice');
					if(dictRadioName.toLowerCase() == "dynamic" ){
						element.setAttribute( 'class', "data_dictionary_radio" );
					} else {
						element.setAttribute( 'isrequired', "" );
					}
						
				}
				this.commitContent(element);
				var className = '';
				if(!isChecked){
					className = 'cke_radio drag';
					element.removeAttribute('checked');
				}else{
					className = 'cke_radio_checked drag';
					element.setAttribute('checked','checked');
				}
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'radiofield', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info4','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.radioField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
					//fakeElement.replace(this.radioField);
					//editor.getSelection().selectElement(fakeElement);
				}
			};
		}else if(dialogType=='checkbox'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){		
					var dialog=this;
					dialog.checkboxField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='checkboxfield'){
						dialog.editMode=true;
						dialog.checkboxField=element;
						element=editor.restoreRealElement(dialog.checkboxField);
						dialog.setupContent(element);
						selection.selectElement(dialog.checkboxField);
					}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var checkName = currentDialog.getValueOf('info','txtName');
				var isChecked=currentDialog.getValueOf('info','cmbSelected')==true?true:false;
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', 'checkbox' );
				element.setAttribute('id', document.getElementById('englishName').value+"_"+checkName);
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var dictCheckBoxName = currentDialog.getValueOf('info','checkbox_data_choice');
				if(isRequired == true){
					if(dictCheckBoxName.toLowerCase() == "dynamic"){
						element.setAttribute( 'class', "data_dictionary_checkbox mandatory" );
						element.setAttribute( 'isrequired', "mandatory" );
					}else{
						element.setAttribute( 'class', "mandatory" );
					}							
				}else{
					if(dictCheckBoxName.toLowerCase() == "dynamic"){
						element.setAttribute( 'class', "data_dictionary_checkbox" );
						
					}else{
						element.setAttribute( 'isrequired', "" );
					}
						
				}
				this.commitContent(element);
				var className = '';
				if(!isChecked){
					className = 'cke_checkbox drag';
					element.removeAttribute('checked');
				}else{
					className = 'cke_checkbox_checked drag';
					element.setAttribute('checked','checked');
				}
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'checkboxfield', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info4','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.checkboxField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
					//fakeElement.replace(this.checkboxField);
					//editor.getSelection().selectElement(fakeElement);
				}
				
			};
		}else if(dialogType=='textfield'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.textField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='textfield'){
						dialog.editMode=true;
						dialog.textField=element;
						element=editor.restoreRealElement(dialog.textField);
						dialog.setupContent(element);
						selection.selectElement(dialog.textField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var defaultValue = currentDialog.getValueOf('info','value');
				var maxLength = currentDialog.getValueOf('info','maxLength');
				if(maxLength != "" && defaultValue > maxLength) {
					$.msgBox({
										    title:form.title.validation,
										    content: "Default value length should not be exceeded maximum characters value",//+defaultValue._.labelIdform.error.maxcharnumber,
										   /* afterClose:function(){
										    	//defaultValue.getInputElement()
													$("#cke_261_textInput").focus();
										    	$("#cke_261_textInput").css('color','#FF225A');
										    }*/
								
										});
					return false;
				}
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', type );
				if(isRequired == true){
					if(type == "email" )
						element.setAttribute( 'class', "email-validation mandatory" );
					else
						element.setAttribute( 'class', "mandatory" );
				}else if(type == "email" ){
					element.setAttribute( 'class', "email-validation" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+textName);
				this.commitContent(element);
				var className = 'cke_textfield drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'textfield', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info4','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.textField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='userlist'){
			var userListId;
			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.textField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='userlist'){
						dialog.editMode=true;
						dialog.textField=element;
						element=editor.restoreRealElement(dialog.textField);
						dialog.setupContent(element);
						userListId = element.getAttribute('id');
						userListRandomNumber = element.getAttribute('userlistrandom')
						selection.selectElement(dialog.textField);
						
					}
				});
			};
			dialogDefination.onOk=function(){
					var randomNumber=Math.floor(Math.random()*900000000)
					var currentDialog=CKEDITOR.dialog.getCurrent();
					var type =currentDialog.getValueOf('info','type');
					var element = editor.document.createElement('input');		
					var column = currentDialog.getValueOf('info','column');
					element.setAttribute( 'column', column );
					element.setAttribute( 'type', 'hidden' );					
					element.setAttribute('userlistrandom',randomNumber);
//					element.setAttribute('readonly',true);
					var elementFullName = editor.document.createElement('input');
					elementFullName.setAttribute('type',type);
					elementFullName.setAttribute('userlistrandom',randomNumber);
					var textName=currentDialog.getValueOf('info','_cke_saved_name');
					element.setAttribute( 'name', textName);
					var id = document.getElementById('englishName').value+"_"+textName;
					var idFullName = document.getElementById('englishName').value+"_"+textName+"FullName";
					element.setAttribute( 'id', id);
					elementFullName.setAttribute( 'id', id+"FullName");
					element.setAttribute('class','cke_hidden displayNone user-list-hidden');
					//$(element).attr('style','display:none');
					//element.setAttribute('style','display:none');
					
//					elementFullName.setAttribute('readonly',true);
					var userSelectType = currentDialog.getValueOf('info','selectType');
					elementFullName.setAttribute('onclick', "showUserSelection('User', '"+userSelectType+"' , '"+id+"',document.getElementById('"+id+"'), '');");
					var isRequired = currentDialog.getValueOf('info','isRequired');
					
					if(isRequired == true){
							elementFullName.setAttribute( 'class', "mandatory" );
					}else if(element.getAttribute('class')){
						elementFullName.setAttribute( 'class' );
					}
					
					elementFullName.setAttribute( 'name', textName+"_FullName" );
					
					//this.commitContent(element);
					this.commitContent(elementFullName);
					var className = 'cke_userlist drag';
					var fakeElement =  editor.createFakeElement( element, 'cke_hidden displayNone user-list-hidden', 'hiddenfield' );
					fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info2','style'));					
					var fakeElementFullName = editor.createFakeElement(elementFullName,className,'userlist',false);
					fakeElementFullName = addStyleToCKEDITORFakeElementOnOK(fakeElementFullName, currentDialog.getValueOf('info2','style'));
					if(userListId != null && userListId !== '' && userListId.length>0){
						var hiddenFieldId = userListId.substring(0,userListId.length-8);
						var frame = $("#ckEditor").find("iframe")[0];
						var hiddenFieldList =  $(frame).contents().find("img.user-list-hidden");
						for(var i=0;i<hiddenFieldList.length;i++){
							var elementSourceCode = $($(frame).contents().find("img.user-list-hidden")[i]).attr("data-cke-realelement");
							var decodedElementSourceCode = $(decodeURIComponent(elementSourceCode));
							if(decodedElementSourceCode.attr('userlistrandom') == userListRandomNumber){
								$(decodedElementSourceCode).attr('id',id)
								$(decodedElementSourceCode).attr('userlistrandom',randomNumber);
								$(decodedElementSourceCode).attr('data-cke-saved-name',textName);
								var createDiv = document.createElement('div');
								var newCode = $(createDiv).html(decodedElementSourceCode);
								newCode = $(createDiv).html();
								var encodedNewCode = encodeURIComponent(newCode);
								$($(frame).contents().find("img.user-list-hidden")[i]).attr("class","cke_hidden displayNone user-list-hidden");
								$($(frame).contents().find("img.user-list-hidden")[i]).attr("data-cke-realelement",encodedNewCode);
							}
						}
					}
					if(!this.editMode){
					editor.insertElement(fakeElement);
					editor.insertElement(fakeElementFullName);
				}else{
					fakeElementFullName.replace(this.textField);
					editor.getSelection().selectElement(fakeElementFullName);
				}
			};
		}else if(dialogType=='departmentlist'){
			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.textField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='departmentlist'){
						dialog.editMode=true;
						dialog.textField=element;
						element=editor.restoreRealElement(dialog.textField);
						dialog.setupContent(element);
						selection.selectElement(dialog.textField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type = currentDialog.getValueOf('info','type');
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', type );
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				var id = document.getElementById('englishName').value+"_"+textName;
				element.setAttribute( 'id', id);
				var departmentSelectType = currentDialog.getValueOf('info','selectType');
				element.setAttribute('onclick', "showDepartmentSelection('Department', '"+departmentSelectType+"' , this.id, this, '');");
				var isRequired = currentDialog.getValueOf('info','isRequired');
				if(isRequired == true){
						element.setAttribute( 'class', "mandatory" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}
				this.commitContent(element);
				var className = 'cke_departmentlist drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'departmentlist', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info2','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.textField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='rolelist'){
			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.textField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='rolelist'){
						dialog.editMode=true;
						dialog.textField=element;
						element=editor.restoreRealElement(dialog.textField);
						dialog.setupContent(element);
						selection.selectElement(dialog.textField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', type );
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				var id = document.getElementById('englishName').value+"_"+textName;
				element.setAttribute( 'id', id);
				var roleSelectType = currentDialog.getValueOf('info','selectType');
				element.setAttribute('onclick', "showRoleSelection('Role', '"+roleSelectType+"' , this.id, this, '');");
				var isRequired = currentDialog.getValueOf('info','isRequired');
				if(isRequired == true){
						element.setAttribute( 'class', "mandatory" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}
				this.commitContent(element);
				var className = 'cke_rolelist drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'rolelist', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info2','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.textField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='grouplist'){
			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.textField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='grouplist'){
						dialog.editMode=true;
						dialog.textField=element;
						element=editor.restoreRealElement(dialog.textField);
						dialog.setupContent(element);
						selection.selectElement(dialog.textField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', type );
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				var id = document.getElementById('englishName').value+"_"+textName;
				element.setAttribute( 'id', id);
				var roleSelectType = currentDialog.getValueOf('info','selectType');
				element.setAttribute('onclick', "showGroupSelection('Group', '"+roleSelectType+"' , this.id, this, '');");
				var isRequired = currentDialog.getValueOf('info','isRequired');
				if(isRequired == true){
						element.setAttribute( 'class', "mandatory" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}
				this.commitContent(element);
				var className = 'cke_grouplist drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'grouplist', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info2','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.textField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='textarea'){
			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.textareaField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='textareafield'){
						dialog.editMode=true;
						dialog.textareaField=element;
						element=editor.restoreRealElement(dialog.textareaField);
						dialog.setupContent('textarea',element);
						selection.selectElement(dialog.textareaField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				var textareaName = currentDialog.getValueOf('info','_cke_saved_name');
				var element = editor.document.createElement('textarea');
				var isRequired = currentDialog.getValueOf('info','isRequired');
				if(isRequired == true){
						element.setAttribute( 'class', "mandatory" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}
				this.commitContent(element);
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+textareaName);
				var defaultValue = currentDialog.getValueOf('info','value');
				var className = 'cke_textarea drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				element.setText(defaultValue);
				var fakeElement = editor.createFakeElement(element, className, 'textareafield', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info4','style'));
				if(!this.editMode)
					editor.insertElement(fakeElement);
				else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.textareaField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
					
				}
			};
		}else if(dialogType == 'select'){
			
			function getOption(element){
				element=f(element);
				return element?element.getChildren():false;
			};
			function f(element){
				if(element&&element.domId&&element.getInputElement().$)
					return element.getInputElement();
				else if(element&&element.$)
					return element;
				else
					return false;
			};
			dialogDefination.onShow=function(){
				var dialog = this;
				dialog.editMode = false;
				delete dialog.selectBox;
				dialog.setupContent('clear');
				var selection = editor.getSelection();
				var element = selection.getSelectedElement();
				if(element && element.data('cke-real-element-type') && element.data('cke-real-element-type') == 'selectfield'){
					dialog.editMode = true;
					dialog.selectBox = element;
					element=editor.restoreRealElement(dialog.selectBox);
					dialog.setupContent('select',element);
					var options=getOption(element);
					for(var index = 0; index < options.count(); index++)
						dialog.setupContent('option', options.getItem(index));
					selection.selectElement(dialog.selectBox);
				}
			};
			dialogDefination.onOk=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				var selectName = currentDialog.getValueOf('info','txtName');
				var element = editor.document.createElement('select');
				element.setAttribute('id', document.getElementById('englishName').value+"_"+selectName);
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var dictComboName = currentDialog.getValueOf('info','data_choice');
				if(isRequired == true){					
					if(dictComboName.toLowerCase() == "dynamic"){
						element.setAttribute( 'class', "data_dictionary mandatory" );
					}else{
						element.setAttribute( 'class', "mandatory" );
					}
				}else {
					if(dictComboName.toLowerCase() == "dynamic"){
						element.setAttribute( 'class', "data_dictionary" );
					}else{
						element.setAttribute( 'class', '');
					}
				}
				this.commitContent(element);
				var className = 'cke_select drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'selectfield', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info3','style'));
				if(!this.editMode)
					editor.insertElement(fakeElement);
				else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.selectBox);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
			
		}else if(dialogType=='datetimepicker'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.dateField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='datefield'){
						dialog.editMode=true;
						dialog.dateField=element;
						element=editor.restoreRealElement(dialog.dateField);
						dialog.setupContent(element);
						selection.selectElement(dialog.dateField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var element = editor.document.createElement('input');
				//element.setAttribute( 'type', 'date' );
				element.setAttribute( 'readonly', 'true' );
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+textName);
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var className = currentDialog.getValueOf('info','class');
				if(isRequired == true){
					if(className == "datepicker" )
						element.setAttribute( 'class', "datepicker mandatory" );
					else if(className == "datetimepicker" )
						element.setAttribute( 'class', "datetimepicker mandatory" );
					else
						element.setAttribute( 'class', "mandatory" );
				}else if(className == "datepicker" ){
					element.setAttribute( 'class', "datepicker" );
				}else if(className == "datetimepicker" ){
					element.setAttribute( 'class', "datetimepicker" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}				
				this.commitContent(element);
				//element.setAttribute( 'type', 'date' );
				var className = 'cke_datefield drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'datefield', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info2','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.dateField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='numberfield'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.numberField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='numberfield'){
						dialog.editMode=true;
						dialog.numberField=element;
						element=editor.restoreRealElement(dialog.numberField);
						dialog.setupContent(element);
						selection.selectElement(dialog.numberField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var defaultValue = currentDialog.getValueOf('info','value');
				var maxLength = currentDialog.getValueOf('info','maxLength');
				if(defaultValue > maxLength) {
					$.msgBox({
										    title:form.title.validation,
										    content: "Default value length should not be exceeded maximum characters value",//+defaultValue._.labelIdform.error.maxcharnumber,
										   /* afterClose:function(){
										    	//defaultValue.getInputElement()
													$("#cke_261_textInput").focus();
										    	$("#cke_261_textInput").css('color','#FF225A');
										    }*/
								
										});
					return false;
				}
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', type );
				element.setAttribute( 'step', 'any' ); // to allow decimal numbers
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+textName);
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var className = currentDialog.getValueOf('info','class');
				if(isRequired == true){
					if(className == "number-validation" )
						element.setAttribute( 'class', "number-validation mandatory" );
					else if(className == "decimal-validation" )
						element.setAttribute( 'class', "decimal-validation mandatory" );
					else
						element.setAttribute( 'class', "mandatory" );
				}else if(className == "number-validation" ){
					element.setAttribute( 'class', "number-validation" );
				}else if(className == "decimal-validation" ){
					element.setAttribute( 'class', "decimal-validation" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}	
				this.commitContent(element);
				var className = 'cke_numberfield drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'numberfield', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info4','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.numberField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='label'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){	
					delete this.label;				
					var dialog=this;
					dialog.label=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='label'){
						dialog.editMode=true;
						dialog.label=element;
						element=editor.restoreRealElement(dialog.label);
						dialog.setupContent(element);
						selection.selectElement(dialog.label);
						}
				});
			};
			dialogDefination.onOk=function(){

				var currentDialog=CKEDITOR.dialog.getCurrent();
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				var element =CKEDITOR.dom.element.createFromHtml('<label type="label" class="cke_label drag">'+textName+'</label>');
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+textName);
			//	var className = currentDialog.getValueOf('info','class');
				
				this.commitContent(element);
				var className = 'cke_label drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'label', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info4','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.label);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='fileupload'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.fileuploadField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='fileupload'){
						dialog.editMode=true;
						dialog.fileuploadField=element;
						element=editor.restoreRealElement(dialog.fileuploadField);
						dialog.setupContent(element);
						selection.selectElement(dialog.fileuploadField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', type );
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+textName);
				var isRequired = currentDialog.getValueOf('info','isRequired');
				if(isRequired == true){
						element.setAttribute( 'class', "mandatory" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}
				this.commitContent(element);
				var className = 'cke_fileupload drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'fileupload', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info3','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.fileuploadField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='button'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.buttonField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='button'){
						dialog.editMode=true;
						dialog.buttonField=element;
						element=editor.restoreRealElement(dialog.buttonField);
						dialog.setupContent(element);
						selection.selectElement(dialog.buttonField);
						}
					$('.cke_dialog_body').css({height :'374px',width :'441px'});
					$('.cke_resizer').css('display', 'none');
		 			$('.cke_resizer_ltr').css('display', 'none');
					$('.cke_dialog_ui_input_textarea').css('line-height', '35px');
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var element = editor.document.createElement('input');
				element.setAttribute( 'type', type );
				var textName=currentDialog.getValueOf('info','name');
				var btnId=currentDialog.getValueOf('info','id');
				var iconPath=currentDialog.getValueOf('info','iconPath');
				var btnStyle=currentDialog.getValueOf('info3','style');
				
				if(iconPath){
					btnStyle = 'background: #ccc url('+iconPath+') no-repeat top left;padding-left: 25px;' + btnStyle;
				}
				element.setAttribute( 'style', btnStyle );
				this.commitContent(element);
				var className = '';
				if(type == 'submit'){
					className = 'cke_submit_btn drag';
				}else{
					className = 'cke_button drag';
				}
				$("#_btnId").val(btnId) ;
				$("#_btnName").val(textName) ;
				$("#_btnTextValue").val(currentDialog.getValueOf('info','value')) ;
	        	$("#_btnFunc").val(currentDialog.getValueOf('info2','onclick')) ;
	        	$("#_iconPath").val(iconPath);
	        	$("#_btnStyle").val(btnStyle);	
	        	$("#_btnType").val(type) ;
	        	var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'button', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info3','style'));
				var isEditMode = this.editMode;
				var _buttonField = this.buttonField;
        		if(btnId){
        			var clickedBtn = "";
        			$.msgBox({
        			    title: form.title.confirm,
        			    content: form.title.pluginAddButtonTitle,
        			    type: "prompt",
        			    inputs: [
        			             { header: form.msg.pluginUpdateButtonManager, type: "radio", name: "userDefBtn", value: "update"},
        			             { header: form.msg.pluginAddForm, type: "radio", name: "userDefBtn", value: "", checked:"checked" }],
        			    buttons: [{ value: "Ok" }, { value: "Cancel" }],
        			    success: function (result, values) {
        			    	clickedBtn = result;
        			    	if(result == "Ok"){
        			    		var selectedVal = "";
        				    	var selected = $("input[type='radio'][name='userDefBtn']:checked");
											
        			    		selectedValue = selected.val();
												
        				        if (selectedValue == "create") {
        				        	$("#_btnId").remove() ;
        				        	$("#saveFormButton").ajaxSubmit(function(){
        				        		$('<input>').attr({
        				    			    type: 'hidden',
        				    			    id: '_btnId',
        				    			    name: 'id'
        				    			}).appendTo('#saveFormButton');
        				        	});
        				        }else if (selectedValue == "update") {
        				        	$("#saveFormButton").ajaxSubmit();
        				        }
        			    	}else{
        			    		return false;
        			    	}
        			    },
            		    afterClose : function(){
            		    	if(clickedBtn == "Ok"){
            		    		if(isEditMode){
	            		    		fakeElement.replace( _buttonField );
	            					editor.getSelection().selectElement( fakeElement );
	            				}
            		    	}
            		    }
        			});
            	}else{
            		var formButtonId = "";
            		var clickedBtn = "";
            			var temp = 0;
				function reCallMsgBox() {
							$.msgBox({
            		    title: form.title.confirm,
            		    content: form.title.pluginAddButtonTitle,
            		    type: "prompt",
            		    inputs: [
            		             { header: form.msg.pluginAddButtonManager, type: "radio", name: "userDefBtn", value: "create"},
            		             { header: form.msg.pluginAddForm, type: "radio", name: "userDefBtn", value: "form"}],
            		    buttons: [{ value: "Ok" }, { value: "Cancel" }],
            		    success: function (result, values) {
											
            		    	clickedBtn = result;
            		    	if(result == "Ok"){
            		    		var selectedVal = "";
            			    	var selected = $("input[type='radio'][name='userDefBtn']:checked");
            			    	selectedValue = selected.val();

												if(selectedValue == "" || selectedValue === undefined) {
																		alert("Select any one option");										
															setTimeout(function() {reCallMsgBox()}, 1000);
															
												} else {
																	
															if (selectedValue == "create") {
            			        	$("#_btnId").remove();
            			        	$("#saveFormButton").ajaxSubmit(function(response){ 
            			        		formButtonId = response.formButton.id;
            			        		$('<input>').attr({
            			    			    type: 'hidden',
            			    			    id: '_btnId',
            			    			    name: 'id'
            			    			}).appendTo('#saveFormButton');
            			        	});
            			        }
													
            		    	if(clickedBtn == "Ok"){
            		    		if(!isEditMode){
            		    			element.setAttribute( 'id',  formButtonId);
            		    			var frame = $("#ckEditor").find("iframe")[0];
            						var formStyle = $($(frame).contents().find("form")).attr("style");
            						if(formStyle.contains("background-image")){
            							element = addStyleToCKEDITORElementOnOK(element);
            						}
            		    			var updateFakeElement = editor.createFakeElement(element, className, 'button', false);
            		    			updateFakeElement = addStyleToCKEDITORFakeElementOnOK(updateFakeElement, btnStyle);
                    		    	editor.insertElement( updateFakeElement );
                				}else{
                					element.setAttribute( 'id',  formButtonId);
                					var frame = $("#ckEditor").find("iframe")[0];
                					var formStyle = $($(frame).contents().find("form")).attr("style");
                					if(formStyle.contains("background-image")){
                						element = addStyleToCKEDITORElementOnOK(element);
                					}
                					var updateFakeElement = editor.createFakeElement(element, className, 'button', false);
                					updateFakeElement = addStyleToCKEDITORFakeElementOnOK(updateFakeElement, btnStyle);
                					updateFakeElement.replace( _buttonField );
                					editor.getSelection().selectElement( updateFakeElement );
                				}
            		    	}
														
												}
            			        
            		   
            		    	}else{
            		    		return false;
            		    	}
            		    }
            		    
            		    	
            		});
				} if(temp == 0){
						reCallMsgBox();
						temp++;
					}
            	}  
			};
		}else if(dialogType=='richtextbox'){
			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.richtextboxField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='richtextbox'){
						dialog.editMode=true;
						dialog.richtextboxField=element;
						element=editor.restoreRealElement(dialog.richtextboxField);
						dialog.setupContent('img', element);
						selection.selectElement(dialog.richtextboxField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				var rtfName = currentDialog.getValueOf('info','_cke_saved_name');
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var element = editor.document.createElement('img');
				if(isRequired) {
					element.setAttribute( 'class', "richtextbox mandatory");
				}else {
					element.setAttribute( 'class', "richtextbox");
				}
				element.setAttribute( 'src', "/scripts/ckeditor/plugins/richtextbox/images/cke_richtextbox.png");
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+rtfName);
				this.commitContent(element);
				var className = 'cke_richtextbox drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'richtextbox', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info','style'));
				if(!this.editMode)
					editor.insertElement(fakeElement);
				else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.richtextboxField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='gridviewcontrol'){
			

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.gridviewcontrolField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='gridviewcontrol'){
						dialog.editMode=true;
						dialog.gridviewcontrolField=element;
						element=editor.restoreRealElement(dialog.gridviewcontrolField);
						dialog.setupContent('img', element);
						selection.selectElement(dialog.gridviewcontrolField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				var element = editor.document.createElement('img');
				element.setAttribute( 'src', "/scripts/ckeditor/plugins/gridviewcontrol/images/gridview.png");
				element.setAttribute( 'class', "gridviewcontrol");
				element.setAttribute( 'gridViewParams', JSON.stringify(listViewParams));
				this.commitContent(element);
				var className = 'cke_gridview drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'gridviewcontrol', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info','style'));
				if(!this.editMode)
					editor.insertElement(fakeElement);
				else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.gridviewcontrolField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		}else if(dialogType=='autocomplete'){

			dialogDefination.onLoad=function(){
				var currentDialog = CKEDITOR.dialog.getCurrent();
				currentDialog.on('show',function(){					
					var dialog=this;
					dialog.autocompleteField=false;
					dialog.editMode=false;
					var selection=editor.getSelection();
					var element=selection.getSelectedElement();
					if(element&&element.data('cke-real-element-type')&&element.data('cke-real-element-type')=='autocomplete'){
						dialog.editMode=true;
						dialog.autocompleteField=element;
						element=editor.restoreRealElement(dialog.autocompleteField);
						dialog.setupContent(element);
						selection.selectElement(dialog.autocompleteField);
						}
				});
			};
			dialogDefination.onOk=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var type =currentDialog.getValueOf('info','type');
				var isRequired = currentDialog.getValueOf('info','isRequired');
				var element = editor.document.createElement('input');
				element.setAttribute( 'autocomplete', "off");
				element.setAttribute( 'type', type );
				if(isRequired == true){
					element.setAttribute( 'class', "mandatory autocomplete" );
				}else if(isRequired == false ){
					element.setAttribute( 'class', "autocomplete" );
				}else if(element.getAttribute('class')){
					element.setAttribute( 'class' );
				}	
				var textName=currentDialog.getValueOf('info','_cke_saved_name');
				element.setAttribute( 'id', document.getElementById('englishName').value+"_"+textName);
				this.commitContent(element);
				var className = 'cke_autocomplete drag';
				var frame = $("#ckEditor").find("iframe")[0];
				var formStyle = $($(frame).contents().find("form")).attr("style");
				if(formStyle.contains("background-image")){
					element = addStyleToCKEDITORElementOnOK(element);
				}
				var fakeElement = editor.createFakeElement(element, className, 'autocomplete', false);
				fakeElement = addStyleToCKEDITORFakeElementOnOK(fakeElement, currentDialog.getValueOf('info','style'));
				if(!this.editMode){
					editor.insertElement(fakeElement);
				}else{
					if(formStyle.contains("background-image")){
						fakeElement.replace(this.autocompleteField);
						editor.getSelection().selectElement(fakeElement);
					}else{
						editor.insertElement(fakeElement);
					}
				}
			};
		
		}else if(dialogType=='onloadevent'){
			dialogDefination.onShow=function(){
				var currentDialog=CKEDITOR.dialog.getCurrent();
				onloadEventScript = decodeURIComponent(onloadEventScript);
				onloadEventScript = onloadEventScript.replace("<script>","");
				onloadEventScript = onloadEventScript.replace("</script>","");				
				onloadEventScript = onloadEventScript.replace("{cke_protected}","");
				onloadEventScript = onloadEventScript.replace("<!--","");
				onloadEventScript = onloadEventScript.replace("-->","");
				onloadEventScript = onloadEventScript.replace("<br>","");				
				currentDialog.setValueOf('info','onLoadEvent', onloadEventScript);
			};
			dialogDefination.onOk=function(){
				var frame = $("#ckEditor").find("iframe")[0];
				$(frame).contents().find("div.onloadevent").remove();
				var currentDialog=CKEDITOR.dialog.getCurrent();
				var onLoadEvent = currentDialog.getValueOf('info','onLoadEvent');
				var onloadScript = "<div id='form_onloadevent' class='onloadevent'><script>"+onLoadEvent+"</script></div>";
				editor.insertHtml( onloadScript );
			};
		}
	});
})();