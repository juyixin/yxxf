﻿/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license

CKEDITOR.dialog.add("checkbox",function(d){return{title:d.lang.forms.checkboxAndRadio.checkboxTitle,minWidth:350,minHeight:140,onShow:function(){delete this.checkbox;var a=this.getParentEditor().getSelection().getSelectedElement();a&&"checkbox"==a.getAttribute("type")&&(this.checkbox=a,this.setupContent(a))},onOk:function(){var a,b=this.checkbox;b||(a=this.getParentEditor(),b=a.document.createElement("input"),b.setAttribute("type","checkbox"),a.insertElement(b));this.commitContent({element:b})},contents:[{id:"info",
label:d.lang.forms.checkboxAndRadio.checkboxTitle,title:d.lang.forms.checkboxAndRadio.checkboxTitle,startupFocus:"txtName",elements:[{id:"txtName",type:"text",label:d.lang.common.name,"default":"",accessKey:"N",setup:function(a){this.setValue(a.data("cke-saved-name")||a.getAttribute("name")||"")},commit:function(a){a=a.element;this.getValue()?a.data("cke-saved-name",this.getValue()):(a.data("cke-saved-name",!1),a.removeAttribute("name"))}},{id:"txtValue",type:"text",label:d.lang.forms.checkboxAndRadio.value,
"default":"",accessKey:"V",setup:function(a){a=a.getAttribute("value");this.setValue(CKEDITOR.env.ie&&"on"==a?"":a)},commit:function(a){var b=a.element,c=this.getValue();c&&!(CKEDITOR.env.ie&&"on"==c)?b.setAttribute("value",c):CKEDITOR.env.ie?(c=new CKEDITOR.dom.element("input",b.getDocument()),b.copyAttributes(c,{value:1}),c.replace(b),d.getSelection().selectElement(c),a.element=c):b.removeAttribute("value")}},{id:"cmbSelected",type:"checkbox",label:d.lang.forms.checkboxAndRadio.selected,"default":"",
accessKey:"S",value:"checked",setup:function(a){this.setValue(a.getAttribute("checked"))},commit:function(a){var b=a.element;if(CKEDITOR.env.ie){var c=!!b.getAttribute("checked"),e=!!this.getValue();c!=e&&(c=CKEDITOR.dom.element.createFromHtml('<input type="checkbox"'+(e?' checked="checked"':"")+"/>",d.document),b.copyAttributes(c,{type:1,checked:1}),c.replace(b),d.getSelection().selectElement(c),a.element=c)}else this.getValue()?b.setAttribute("checked","checked"):b.removeAttribute("checked")}}]}]}});

*/
﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/
/**
 *  This plugin create checkbox with properties like name, style, onFocus etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.dialog.add( 'checkbox', function( editor )
{
	return {
		title : editor.lang.forms.checkboxAndRadio.checkboxTitle,
		minWidth : 350,
		minHeight : 140,
		onShow : function()
		{},
		onOk : function()
		{},
		contents : [
			{
				id : 'info',
				label : form.label.basicProperties,
				title : form.label.basicProperties,
				startupFocus : 'txtName',
				elements : [
					{
						type : 'html',
						align : 'left',
						html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.checkbox+'</span>'
					}, 
					{


						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : 'txtName',
								type : 'text',
								label : editor.lang.common.name+'<span class="color-red"> *</span>',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									var message = "";
									if(this.getValue() == ""){
										message = form.error.emptyname
									}else if(this.getValue().indexOf(' ') >= 0){
										message = form.error.namespace
									}else{
										var iChars = "~`!#$%^&*+=-[]\\\';,/{}|\":<>?";
										for (var i = 0; i < this.getValue().length; i++) {
										  if (iChars.indexOf(this.getValue().charAt(i)) != -1) {
											  message= form.error.specialcharacter
										  }
										}
									}
									if(message != ""){
										$.msgBox({
										    title:form.title.validation,
										    content:message,
										    afterClose:function(){
										    	currentElement.getInputElement().focus();
										    	$("#"+currentElement._.labelId).css('color','#FF225A');
										    }
										});
										return false;
									}else{
										$("#"+currentElement._.labelId).css('color','#524847');
									}
								},
								'default' : '',
								accessKey : 'N',
								setup : function( element )
								{
									this.setValue(
											element.data( 'cke-saved-name' ) ||
											element.getAttribute( 'name' ) ||
											'' );
								},
								commit : function( element )
								{
									//var element = data.element;

									// IE failed to update 'name' property on input elements, protect it now.
									if ( this.getValue() )
										element.data( 'cke-saved-name', this.getValue() );
									else
									{
										element.data( 'cke-saved-name', false );
										element.removeAttribute( 'name' );
									}
								}
							},
							{
								type : 'select',
								id : 'checkbox_data_choice',
								label : form.label.availableoptions,
								title : form.label.availableoptions,
								size : 1,
								style : 'width:208px;height:25px',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								items : [[form.title.pluginStatic,'static'],[form.title.pluginDynamic,'dynamic']],
								'default':'static',
								onChange : function() 
								{
									if(this.getValue() == "dynamic") {
										var dialog = this.getDialog();
										var sectionElement = dialog.getContentElement( 'info', 'value_checkbox' );
										sectionElement.getElement().hide();
										var dictComboElement = dialog.getContentElement( 'info', 'dictionary_combo' );
										dictComboElement.getElement().show();
										
									}else if(this.getValue() == "static") {
										var dialog = this.getDialog();
										var sectionElement = dialog.getContentElement( 'info', 'value_checkbox' );
										sectionElement.getElement().show();
										var dictComboElement = dialog.getContentElement( 'info', 'dictionary_combo' );
										dictComboElement.getElement().hide();
									}
									
								},
								setup : function(element )
								{
									this.setValue(
											element.data( 'optiontype' ) ||
											element.getAttribute( 'optiontype' ) ||
											'' );
//									var dialog = this.getDialog();
//									var sectionElement = dialog.getContentElement( 'info', 'dictionary_combo' );
//									sectionElement.getElement().hide();
//									
								},
								onLoad : function(){
									if(this.getValue() == "static") {
											var dialog = this.getDialog();
											var dictComboElement = dialog.getContentElement( 'info', 'dictionary_combo' );
											dictComboElement.getElement().hide();
									}
//									 	var dialog = this.getDialog();
//										var sectionElement = dialog.getContentElement( 'info', 'dictionary_combo' );
//										sectionElement.getElement().hide();
								    	//getTableWithParentTable(element_id, $("#tableId").val(), $("#tableName").val());
								    },
							    commit : function( element )
								{
							    	//var element = data.element;
							    	if ( CKEDITOR.env.ie )
									{
										var elementType = element.getAttribute( 'checkbox_data_choice' );
										var optionTypeValue = this.getValue();
										if ( elementType != table )
										{
											var replace = CKEDITOR.dom.element.createFromHtml( '<input checkbox_data_choice="' + optionTypeValue + '"></input>', editor.document );
											element.copyAttributes( replace, { type : 1 } );
											replace.replace( element );
											editor.getSelection().selectElement( replace );
											data.element = replace;
										}
									}
									else {
										element.setAttribute( 'optionType', this.getValue() );
									}
								},
							}
						],
						onLoad : function()
						{
							// Repaint the style for IE7 (#6068)
							if ( CKEDITOR.env.ie7Compat )
								this.getElement().setStyle( 'zoom', '100%' );
						}
					},{
						id : 'dictionary_combo',
						type : 'text',
						label : form.label.dataDictionary+'<span class="color-red"> *</span>',
						'default' : '',
						accessKey : 'C',
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						validate : function(){
							var currentElement = this;
							var currentDialog = CKEDITOR.dialog.getCurrent();
							var radioDataChoice = currentDialog.getValueOf( 'info', 'checkbox_data_choice' );
							if(this.getValue() == "" && radioDataChoice == "dynamic"){
								$.msgBox({
					    		    title:form.title.validation,
					    		    content: form.error.emptydatadictionaryfield,
					    		    /*afterClose:function(){
					    		    	currentElement.getInputElement().focus();
					    		    	$("#"+currentElement._.labelId).css('color','#FF225A');
					    		    }*/
					   			});
								return false;
							}else{
								$("#"+currentElement._.labelId).css('color','#524847');
							}
						},
						items : [['',0]],
					    setup : function(element) { 
							var currentDialog = CKEDITOR.dialog.getCurrent();
							if ( element.getAttribute( 'dataDictionary' ) ){
								this.setValue( element.getAttribute( 'dataDictionaryLabel' ) );
								var currentDialog = CKEDITOR.dialog.getCurrent();
								currentDialog.setValueOf( 'info', 'datadictionaryid_hidden', element.getAttribute( 'dataDictionary' ));
							}
						},
					    onClick: function(){
					    	var element_id = this.getInputElement().$.id;
					    	var selectedValue = this.getInputElement().$.value;
					    	var dialog = this.getDialog();
							var hiddenElement = dialog.getContentElement('info', 'datadictionaryid_hidden');
							var hiddenElementId = hiddenElement._.inputId;
							var dictionaryValue = CKEDITOR.dialog.getCurrent().getValueOf( 'info', 'datadictionaryid_hidden' );
					    	showDataDictionaryListTree(form.title.dataDictionaryList, 'single', element_id , dictionaryValue , '', hiddenElementId);
					    },
					    commit : function( element )
						{
					    	var currentDialog = CKEDITOR.dialog.getCurrent();
							var dictionaryValue = currentDialog.getValueOf( 'info', 'datadictionaryid_hidden' );
					    	if ( CKEDITOR.env.ie )
							{
								var elementType = element.getAttribute( 'dictionary_combo' );
								if ( elementType != table )
								{
									var replace = CKEDITOR.dom.element.createFromHtml( '<input dictionary_combo="' + dictionaryValue + '"></input>', editor.document );
									element.copyAttributes( replace, { type : 1 } );
									replace.replace( element );
									editor.getSelection().selectElement( replace );
									data.element = replace;
								}
							}
							else {
								element.setAttribute( 'dataDictionary', dictionaryValue);
								element.setAttribute( 'dataDictionaryLabel', this.getValue());
							}
						}
					},{

						id : 'value_checkbox',
						type : 'text',
						label : editor.lang.forms.checkboxAndRadio.value+'<span class="color-red"> *</span>',
						'default' : '',
						accessKey : 'V',
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						validate : function(){
							var currentElement = this;
							var currentDialog = CKEDITOR.dialog.getCurrent();
							var checkboxDataChoice = currentDialog.getValueOf( 'info', 'checkbox_data_choice' );
							if(this.getValue() == "" && checkboxDataChoice == "static"){
								$.msgBox({
					    		    title:form.title.validation,
					    		    content: form.error.emptyvaluefield,
					    		    afterClose:function(){
					    		    	currentElement.getInputElement().focus();
					    		    	$("#"+currentElement._.labelId).css('color','#FF225A');
					    		    }
					   			});
								return false;
							}else{
								$("#"+currentElement._.labelId).css('color','#524847');
							}
						},
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'value' ) || '' );
						},
						commit : function( element )
						{
							//var element = data.element;

							if ( this.getValue() )
								element.setAttribute( 'value', this.getValue() );
							else
								element.removeAttribute( 'value' );
						}
					
					},{
						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : 'isRequired',
								type : 'checkbox',
								label : form.label.isRequired,
								'default' : '',
								accessKey : 'M',
								//hidden : true,
								setup : function( element )
								{
									if(element.getAttribute( 'class' )){
										var className = element.getAttribute( 'class' );
										if(className.indexOf("mandatory") > -1){
											this.setValue(element.setAttribute( 'checked' ));
										}
									}
								}
							},{
								id : 'cmbSelected',
								type : 'checkbox',
								label : editor.lang.forms.checkboxAndRadio.selected,
								'default' : '',
								accessKey : 'S',
								value : "checked",
								onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
								setup : function( element )
								{
									this.setValue( element.getAttribute( 'checked' ) );
								},
								commit : function( element )
								{
									//var element = data.element;

									if ( CKEDITOR.env.ie )
									{
										var isElementChecked = !!element.getAttribute( 'checked' ),
											isChecked = !!this.getValue();

										if ( isElementChecked != isChecked )
										{
											var replace = CKEDITOR.dom.element.createFromHtml( '<input type="checkbox"'
												   + ( isChecked ? ' checked="checked"' : '' )
												   + '/>', editor.document );

											element.copyAttributes( replace, { type : 1, checked : 1 } );
											replace.replace( element );
											editor.getSelection().selectElement( replace );
											data.element = replace;
										}
									}
									else
									{
										var value = this.getValue();
										if ( value )
											element.setAttribute( 'checked', 'checked' );
										else
											element.removeAttribute( 'checked' );
									}
								}
							}
						]
					
					},
					{

						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : 'table',
								type : 'select',
								label : form.label.table+'<span class="color-red"> *</span>',
								'default' : '',
								accessKey : 'C',
								style : 'width:205px',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									if(this.getValue() == ""){
										$.msgBox({
										    title:form.title.validation,
										    content: form.error.emptytable,
										    afterClose:function(){
										    	currentElement.getInputElement().focus();
										    	$("#"+currentElement._.labelId).css('color','#FF225A');
										    }
										});
										return false;
									}else{
										$("#"+currentElement._.labelId).css('color','#524847');
									}
								},
								items : [['',0]],
							    setup : function(element) {
							    	this.setValue(
											element.data( 'table' ) ||
											element.getAttribute( 'table' ) ||
											'' );
							    },
							    onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	getTableWithParentTable(element_id, $("#tableId").val(), $("#tableName").val());
							    },
							    onChange : function() {
							    	 var dialog = this.getDialog();
							         var values = dialog.getContentElement( 'info', 'column' );
							         var element_id = '#' + values.getInputElement().$.id;
							         getAllColumnByTableId(element_id, this.getValue(), 'checkbox');
							    },
							    commit : function( element )
								{
							    	//var element = data.element;
							    	if ( CKEDITOR.env.ie )
									{
										var elementType = element.getAttribute( 'table' );
										var table = this.getValue();

										if ( elementType != table )
										{
											var replace = CKEDITOR.dom.element.createFromHtml( '<input table="' + table + '"></input>', editor.document );
											element.copyAttributes( replace, { type : 1 } );
											replace.replace( element );
											editor.getSelection().selectElement( replace );
											data.element = replace;
										}
									}
									else
										element.setAttribute( 'table', this.getValue() );
								}
							},
							{
								id : 'column',
								type : 'select',
								label : form.label.column+'<span class="color-red"> *</span>',
								'default' : '',
								accessKey : 'M',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									if(this.getValue() == ""){
										$.msgBox({
											title:form.title.validation,
											content: form.error.emptycolumn,
											afterClose:function(){
												currentElement.getInputElement().focus();
												$("#"+currentElement._.labelId).css('color','#FF225A');
											}
										});
										return false;
									}else{
										$("#"+currentElement._.labelId).css('color','#524847');
									}
								},
								style : 'width:205px',
								items : [['',0]],
								setup : function(element) {
									this.setValue(
											element.data( 'column' ) ||
											element.getAttribute( 'column' ) ||
											'' );
								},
								onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	getAllColumnByTableId(element_id, $("#tableId").val(), 'checkbox');
							    },
							    commit : function( element )
								{
							    	//var element = data.element;
							    	if ( CKEDITOR.env.ie )
									{
										var elementType = element.getAttribute( 'column' );
										var column = this.getValue();

										if ( elementType != column )
										{
											var replace = CKEDITOR.dom.element.createFromHtml( '<input column="' + column + '"></input>', editor.document );
											element.copyAttributes( replace, { type : 1 } );
											replace.replace( element );
											editor.getSelection().selectElement( replace );
											data.element = replace;
										}
									}
									else
										element.setAttribute( 'column', this.getValue() );
								}
							}
						]
					},{
				    	type : 'text',
						id:	"datadictionaryid_hidden",
						label: 'Data Dictionary Id',
						hidden: true
				    }
				]
			},{

				id: 'info2',
				label: form.label.mouseEvents,
				title: form.label.mouseEvents,
				elements: [
					{
						id : 'onchange',
						type : 'textarea',
						label : form.label.onchange,
						'default' : '',
						style : 'width:420px',
						rows : 2,
						accessKey : 'G',
						onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
						setup : function( element )
						{
							this.setValue(
									element.data( 'onchange' ) ||
									element.getAttribute( 'onchange' ) ||
									'' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'onchange', this.getValue() );
							else
								element.removeAttribute( 'onchange' );
						}
					},{
						id : 'onclick',
						type : 'textarea',
						label : form.label.onclick,
						'default' : '',
						style : 'width:420px',
						accessKey : 'C',
						rows:2,
						onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},	
						setup : function( element )
						{
							this.setValue(
									element.data( 'onclick' ) ||
									element.getAttribute( 'onclick' ) ||
									'' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'onclick', this.getValue() );
							else
								element.removeAttribute( 'onclick' );
						}
					}
				]
			},{
				id: 'info3',
				label: form.label.otherEvents,
				title: form.label.otherEvents,
				elements: [
						  {
								id : 'onfocus',
								type : 'textarea',
								label : form.label.onfocus,
								'default' : '',
								style : 'width:160px',
								accessKey : 'F',
								rows:2,
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								setup : function( element )
								{
									this.setValue(
											element.data( 'onfocus' ) ||
											element.getAttribute( 'onfocus' ) ||
											'' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'onfocus', this.getValue() );
									else
										element.removeAttribute( 'onfocus' );
								}
							}
				]
			
			},{

				id: 'info4',
				label: form.label.style,
				title: form.label.style,
				elements: [
						  {
							type : 'textarea',
							id : 'style',
							label : form.label.style,
							style : 'width:160px',
							required : false,
							rows : 2,
							onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
							setup : function( element  )
							{
								this.setValue(element.getAttribute( 'style' ) || '' );
							},
							commit : function( element ){
								
								if ( this.getValue() )
									element.setAttribute( 'style', this.getValue() );
								else
									element.removeAttribute( 'style' );
							}
						}
				]
			
			}
		]
	};
});
