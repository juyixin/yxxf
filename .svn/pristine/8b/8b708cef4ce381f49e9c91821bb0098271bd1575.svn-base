﻿/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license
*/
/*CKEDITOR.dialog.add("hiddenfield",function(d){return{title:d.lang.forms.hidden.title,hiddenField:null,minWidth:350,minHeight:110,onShow:function(){delete this.hiddenField;var a=this.getParentEditor(),b=a.getSelection(),c=b.getSelectedElement();c&&(c.data("cke-real-element-type")&&"hiddenfield"==c.data("cke-real-element-type"))&&(this.hiddenField=c,c=a.restoreRealElement(this.hiddenField),this.setupContent(c),b.selectElement(this.hiddenField))},onOk:function(){var a=this.getValueOf("info","_cke_saved_name");
this.getValueOf("info","value");var b=this.getParentEditor(),a=CKEDITOR.env.ie&&!(8<=CKEDITOR.document.$.documentMode)?b.document.createElement('<input name="'+CKEDITOR.tools.htmlEncode(a)+'">'):b.document.createElement("input");a.setAttribute("type","hidden");this.commitContent(a);a=b.createFakeElement(a,"cke_hidden","hiddenfield");this.hiddenField?(a.replace(this.hiddenField),b.getSelection().selectElement(a)):b.insertElement(a);return!0},contents:[{id:"info",label:d.lang.forms.hidden.title,title:d.lang.forms.hidden.title,
elements:[{id:"_cke_saved_name",type:"text",label:d.lang.forms.hidden.name,"default":"",accessKey:"N",setup:function(a){this.setValue(a.data("cke-saved-name")||a.getAttribute("name")||"")},commit:function(a){this.getValue()?a.setAttribute("name",this.getValue()):a.removeAttribute("name")}},{id:"value",type:"text",label:d.lang.forms.hidden.value,"default":"",accessKey:"V",setup:function(a){this.setValue(a.getAttribute("value")||"")},commit:function(a){this.getValue()?a.setAttribute("value",this.getValue()):
a.removeAttribute("value")}}]}]}});*/

﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 *  This plugin create hidden field with properties like name, value etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.dialog.add( 'hiddenfield', function( editor )
{
	return {
		title : editor.lang.forms.hidden.title,
		hiddenField : null,
		minWidth : 350,
		minHeight : 110,
		onShow : function()
		{
			delete this.hiddenField;

			var editor = this.getParentEditor(),
				selection = editor.getSelection(),
				element = selection.getSelectedElement();

			if ( element && element.data( 'cke-real-element-type' ) && element.data( 'cke-real-element-type' ) == 'hiddenfield' )
			{
				this.hiddenField = element;
				element = editor.restoreRealElement( this.hiddenField );
				this.setupContent( element );
				selection.selectElement( this.hiddenField );
			}
		},
		onOk : function()
		{
			var name = this.getValueOf( 'info', '_cke_saved_name' ),
				value = this.getValueOf( 'info', 'value' ),
				editor = this.getParentEditor(),
				element = CKEDITOR.env.ie && !( CKEDITOR.document.$.documentMode >= 8 ) ?
					editor.document.createElement( '<input name="' + CKEDITOR.tools.htmlEncode( name ) + '">' )
					: editor.document.createElement( 'input' );

			element.setAttribute( 'type', 'hidden' );
			element.setAttribute('id', document.getElementById('formName').value+"_"+name);
			this.commitContent( element );
			var fakeElement = editor.createFakeElement( element, 'cke_hidden', 'hiddenfield' );
			if ( !this.hiddenField )
				editor.insertElement( fakeElement );
			else
			{
				fakeElement.replace( this.hiddenField );
				editor.getSelection().selectElement( fakeElement );
			}
			return true;
		},
		contents : [
			{
				id : 'info',
				label : editor.lang.forms.hidden.title,
				title : editor.lang.forms.hidden.title,
				elements : [
					{
						type : 'html',
						align : 'left',
						html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.hiddenfield+'</span>'
					}, {


						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : '_cke_saved_name',
								type : 'text',
								label : editor.lang.forms.hidden.name+'<span class="color-red"> *</span>',
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
									if ( this.getValue() )
										element.setAttribute( 'name', this.getValue() );
									else
									{
										element.removeAttribute( 'name' );
									}
								}
							},
							{
								id : 'value',
								type : 'text',
								label : form.label.defaultValue,
								'default' : '',
								accessKey : 'V',
								onShow : function(){
								var currentElement =  this;
								$("#"+currentElement._.labelId).css('color','#524847');
								},
								setup : function( element )
								{
									this.setValue( element.getAttribute( 'value' ) || '' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'value', this.getValue() );
									else
										element.removeAttribute( 'value' );
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
								onShow : function(){
								var currentElement =  this;
								$("#"+currentElement._.labelId).css('color','#524847');
								},
								style : 'width:200px',
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
							         getAllColumnByTableId(element_id, this.getValue(), 'hidden');
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
											content:form.error.emptycolumn,
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
								style : 'width:200px',
								items : [['',0]],
								setup : function(element) {
									this.setValue(
											element.data( 'column' ) ||
											element.getAttribute( 'column' ) ||
											'' );
								},
								onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	getAllColumnByTableId(element_id, $("#tableId").val(), 'hidden');
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
					
					}
				]
			}
		]
	};
});