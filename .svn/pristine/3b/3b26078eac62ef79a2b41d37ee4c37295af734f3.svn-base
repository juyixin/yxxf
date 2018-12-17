/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license

CKEDITOR.dialog.add("textfield",function(b){function e(a){var a=a.element,c=this.getValue();c?a.setAttribute(this.id,c):a.removeAttribute(this.id)}function f(a){this.setValue(a.hasAttribute(this.id)&&a.getAttribute(this.id)||"")}var g={email:1,password:1,search:1,tel:1,text:1,url:1};return{title:b.lang.forms.textfield.title,minWidth:350,minHeight:150,onShow:function(){delete this.textField;var a=this.getParentEditor().getSelection().getSelectedElement();if(a&&"input"==a.getName()&&(g[a.getAttribute("type")]||
!a.getAttribute("type")))this.textField=a,this.setupContent(a)},onOk:function(){var a=this.getParentEditor(),c=this.textField,b=!c;b&&(c=a.document.createElement("input"),c.setAttribute("type","text"));c={element:c};b&&a.insertElement(c.element);this.commitContent(c);b||a.getSelection().selectElement(c.element)},onLoad:function(){this.foreach(function(a){if(a.getValue&&(a.setup||(a.setup=f),!a.commit))a.commit=e})},contents:[{id:"info",label:b.lang.forms.textfield.title,title:b.lang.forms.textfield.title,
elements:[{type:"hbox",widths:["50%","50%"],children:[{id:"_cke_saved_name",type:"text",label:b.lang.forms.textfield.name,"default":"",accessKey:"N",setup:function(a){this.setValue(a.data("cke-saved-name")||a.getAttribute("name")||"")},commit:function(a){a=a.element;this.getValue()?a.data("cke-saved-name",this.getValue()):(a.data("cke-saved-name",!1),a.removeAttribute("name"))}},{id:"value",type:"text",label:b.lang.forms.textfield.value,"default":"",accessKey:"V",commit:function(a){if(CKEDITOR.env.ie&&
!this.getValue()){var c=a.element,d=new CKEDITOR.dom.element("input",b.document);c.copyAttributes(d,{value:1});d.replace(c);a.element=d}else e.call(this,a)}}]},{type:"hbox",widths:["50%","50%"],children:[{id:"size",type:"text",label:b.lang.forms.textfield.charWidth,"default":"",accessKey:"C",style:"width:50px",validate:CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed)},{id:"maxLength",type:"text",label:b.lang.forms.textfield.maxChars,"default":"",accessKey:"M",style:"width:50px",
validate:CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed)}],onLoad:function(){CKEDITOR.env.ie7Compat&&this.getElement().setStyle("zoom","100%")}},{id:"type",type:"select",label:b.lang.forms.textfield.type,"default":"text",accessKey:"M",items:[[b.lang.forms.textfield.typeEmail,"email"],[b.lang.forms.textfield.typePass,"password"],[b.lang.forms.textfield.typeSearch,"search"],[b.lang.forms.textfield.typeTel,"tel"],[b.lang.forms.textfield.typeText,"text"],[b.lang.forms.textfield.typeUrl,
"url"]],setup:function(a){this.setValue(a.getAttribute("type"))}},
{id:"onclick",type:"textarea",label:b.lang.forms.textfield.onclick,"default":"",setup:function(a){this.setValue(a.getAttribute("onclick"))}},
{id:"onfocus",type:"textarea",label:b.lang.forms.textfield.onfocus,"default":"",setup:function(a){this.setValue(a.getAttribute("onfocus"))}}],commit:function(a){var c=a.element;if(CKEDITOR.env.ie){var d=c.getAttribute("type"),e=this.getValue();d!=e&&(d=CKEDITOR.dom.element.createFromHtml('<input type="'+e+'"></input>',b.document),c.copyAttributes(d,{type:1}),d.replace(c),a.element=d)}else c.setAttribute(this.getValue(),this.getValue())}}]}});*/


﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 *  This plugin create text field (such as textbox, password, email) with properties like name, type, style, onFocus etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.dialog.add( 'textfield', function( editor )
{
	var autoAttributes =
	{
		value : 1,
		size : 1,
		maxLength : 1
	};

	var acceptedTypes =
	{
		text : 1,
		password : 1
	};

	return {
		title : form.title.textboxProperties,
		minWidth : 350,
		minHeight : 150,
		onShow : function()
		{},
		onOk : function()
		{},
		onLoad : function()
		{},
		contents : [
			{
				id : 'info',
				label : form.label.basicProperties,
				title : form.label.basicProperties,
				elements : [
						{
							type : 'html',
							align : 'left',
							html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.textfield+'</span>'
						},{

							type : 'hbox',
							widths : [ '50%', '50%' ],
							style : 'padding-top: 21px;',
							children :
							[
								{
									id : '_cke_saved_name',
									type : 'text',
									label : editor.lang.forms.textfield.name+'<span class="color-red"> *</span>',
									onShow : function(){
										var currentElement =  this;
										$("#"+currentElement._.labelId).css('color','#524847');
									},
									validate : function(){
										var currentElement =  this;
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
									id : 'value',
									type : 'text',
									label : form.label.defaultValue,
									'default' : '',
									onShow : function(){
										var currentElement =  this;
										$("#"+currentElement._.labelId).css('color','#524847');
									},
									setup : function( element ){
										this.setValue( element.getAttribute( 'value' ) || '');
									},
									commit : function(element){
										if ( this.getValue() )
											element.setAttribute( 'value', this.getValue() );
										else
											element.removeAttribute( 'value' );
									}
								}
							],
							onLoad : function()
							{
								// Repaint the style for IE7 (#6068)
								if ( CKEDITOR.env.ie7Compat )
									this.getElement().setStyle( 'zoom', '100%' );
							}
						
						},{
							id : 'isRequired',
							type : 'checkbox',
							label : form.label.isRequired,
							'default' : '',
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
						type : 'hbox',
						widths : [ '33%', '41%', '25%' ],
						children :
						[
							{
								id : 'size',
								type : 'text',
								label : editor.lang.forms.textfield.charWidth,
								'default' : '',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									if(isNaN(this.getValue())){
										$.msgBox({
									    title:form.title.validation,
									    content: form.error.charwidthnumber,
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
								setup : function( element ){
									this.setValue( element.getAttribute( 'size' ) || '');
								},
								commit : function(element){
									if ( this.getValue() )
										element.setAttribute( 'size', this.getValue() );
									else
										element.removeAttribute( 'size' );
								}
							},
							{
								id : 'maxLength',
								type : 'text',
								label : editor.lang.forms.textfield.maxChars,
								'default' : '',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									if(isNaN(this.getValue())){
										$.msgBox({
										    title:form.title.validation,
										    content: form.error.maxcharnumber,
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
								setup : function( element ){
									this.setValue( element.getAttribute( 'maxLength' ) || '');
								},
								commit : function(element){
									if ( this.getValue() )
										element.setAttribute( 'maxLength', this.getValue() );
									else
										element.removeAttribute( 'maxLength' );
								}
							},{

								id : 'type',
								type : 'select',
								label : form.label.inputType,
								'default' : 'text',
								style : 'width:100px;',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								items :
								[
									[ editor.lang.forms.textfield.typeText, 'text' ],
									[ editor.lang.forms.textfield.typePass, 'password' ],
									[ editor.lang.forms.textfield.typeEmail, 'email' ]
								],
								setup : function( element )
								{
									this.setValue( element.getAttribute( 'type' ) );
								},
								onChange : function() {
							    	 var dialog = this.getDialog();
							         var values = dialog.getContentElement( 'info', 'column' );
							         var element_id = '#' + values.getInputElement().$.id;
							         var tableObj = dialog.getContentElement( 'info', 'table' );
							         getAllColumnByTableId(element_id, tableObj.getValue(), this.getValue());
							    },
								commit : function( element )
								{
									//var element = data.element;

									if ( CKEDITOR.env.ie )
									{
										var elementType = element.getAttribute( 'type' );
										var myType = this.getValue();

										if ( elementType != myType )
										{
											var replace = CKEDITOR.dom.element.createFromHtml( '<input type="' + myType + '"></input>', editor.document );
											element.copyAttributes( replace, { type : 1 } );
											replace.replace( element );
											editor.getSelection().selectElement( replace );
											data.element = replace;
										}
									}
									else
										element.setAttribute( 'type', this.getValue() );
								}
							}
						],
						onLoad : function()
						{
							// Repaint the style for IE7 (#6068)
							if ( CKEDITOR.env.ie7Compat )
								this.getElement().setStyle( 'zoom', '100%' );
						}
					},{
						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : 'table',
								type : 'select',
								label : form.label.table+'<span class="color-red"> *</span>',
								'default' : '',
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
										    content:form.error.emptytable,
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
							         getAllColumnByTableId(element_id, this.getValue(), 'text');
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
								/*onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	getAllColumnByTableId(element_id, $("#tableId").val(), 'text');
							    },*/
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
					}
				]
			},{
				id: 'info2',
				label: form.label.mouseEvents,
				title: form.label.mouseEvents,
				elements: [
						{
							id : 'onclick',
							type : 'textarea',
							label : form.label.onclick,
							'default' : '',
							style : 'width:420px',
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
						},{
							id : 'ondblclick',
							type : 'textarea',
							label : form.label.ondblclick,
							'default' : '',
							style : 'width:420px',
							rows:2,
							onShow : function(){
								var currentElement =  this;
								$("#"+currentElement._.labelId).css('color','#524847');
							},
							setup : function( element )
							{
								this.setValue(
										element.data( 'ondblclick' ) ||
										element.getAttribute( 'ondblclick' ) ||
										'' );
							},
							commit : function( element )
							{
								if ( this.getValue() )
									element.setAttribute( 'ondblclick', this.getValue() );
								else
									element.removeAttribute( 'ondblclick' );
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
							style : 'width:420px',
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
						
						},{
							id : 'onblur',
							type : 'textarea',
							label :form.label.onblur,
							'default' : '',
							style : 'width:420px',
							rows:2,
							onShow : function(){
								var currentElement =  this;
								$("#"+currentElement._.labelId).css('color','#524847');
							},
							setup : function( element )
							{
								this.setValue(
										element.data( 'onblur' ) ||
										element.getAttribute( 'onblur' ) ||
										'' );
							},
							commit : function( element )
							{
								if ( this.getValue() )
									element.setAttribute( 'onblur', this.getValue() );
								else
									element.removeAttribute( 'onblur' );
							}
						},{
							id : 'onkeypress',
							type : 'textarea',
							label : form.label.onkeypress,
							'default' : '',
							style : 'width:420px',
							rows:2,
							onShow : function(){
								var currentElement =  this;
								$("#"+currentElement._.labelId).css('color','#524847');
							},
							setup : function( element )
							{
								this.setValue(
										element.data( 'onkeypress' ) ||
										element.getAttribute( 'onkeypress' ) ||
										'' );
							},
							commit : function( element )
							{
								if ( this.getValue() )
									element.setAttribute( 'onkeypress', this.getValue() );
								else
									element.removeAttribute( 'onkeypress' );
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
							'default' : '',
							style : 'height:160px',
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