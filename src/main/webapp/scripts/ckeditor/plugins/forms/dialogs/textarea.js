﻿/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license

CKEDITOR.dialog.add("textarea",function(b){return{title:b.lang.forms.textarea.title,minWidth:350,minHeight:220,onShow:function(){delete this.textarea;var a=this.getParentEditor().getSelection().getSelectedElement();a&&"textarea"==a.getName()&&(this.textarea=a,this.setupContent(a))},onOk:function(){var a,b=this.textarea,c=!b;c&&(a=this.getParentEditor(),b=a.document.createElement("textarea"));this.commitContent(b);c&&a.insertElement(b)},contents:[{id:"info",label:b.lang.forms.textarea.title,title:b.lang.forms.textarea.title,
elements:[{id:"_cke_saved_name",type:"text",label:b.lang.common.name,"default":"",accessKey:"N",setup:function(a){this.setValue(a.data("cke-saved-name")||a.getAttribute("name")||"")},commit:function(a){this.getValue()?a.data("cke-saved-name",this.getValue()):(a.data("cke-saved-name",!1),a.removeAttribute("name"))}},{type:"hbox",widths:["50%","50%"],children:[{id:"cols",type:"text",label:b.lang.forms.textarea.cols,"default":"",accessKey:"C",style:"width:50px",validate:CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed),
setup:function(a){this.setValue(a.hasAttribute("cols")&&a.getAttribute("cols")||"")},commit:function(a){this.getValue()?a.setAttribute("cols",this.getValue()):a.removeAttribute("cols")}},{id:"rows",type:"text",label:b.lang.forms.textarea.rows,"default":"",accessKey:"R",style:"width:50px",validate:CKEDITOR.dialog.validate.integer(b.lang.common.validateNumberFailed),setup:function(a){this.setValue(a.hasAttribute("rows")&&a.getAttribute("rows")||"")},commit:function(a){this.getValue()?a.setAttribute("rows",
this.getValue()):a.removeAttribute("rows")}}]},{id:"value",type:"textarea",label:b.lang.forms.textfield.value,"default":"",setup:function(a){this.setValue(a.$.defaultValue)},commit:function(a){a.$.value=a.$.defaultValue=this.getValue()}},{id:"onClick",type:"textarea",label:b.lang.forms.textarea.onclick,"default":"",setup:function(a){this.setValue(a.getAttribute("onClick")||"")},commit:function(a){this.getValue()?a.setAttribute("onClick",this.getValue()):a.removeAttribute("onClick")}},{id:"onFocus",type:"textarea",label:b.lang.forms.textarea.onfocus,"default":"",setup:function(a){this.setValue(a.getAttribute("onFocus")||"")},commit:function(a){this.getValue()?a.setAttribute("onFocus",this.getValue()):a.removeAttribute("onFocus")}}]}]}});*/

﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 *  This plugin create textarea box with properties like name, style, onFocus etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.dialog.add( 'textarea', function( editor )
{
	return {
		title : editor.lang.forms.textarea.title,
		minWidth : 350,
		minHeight : 150,
		onShow : function()
		{
			/*delete this.textarea;

			var element = this.getParentEditor().getSelection().getSelectedElement();
			if ( element && element.getName() == "textarea" )
			{
				this.textarea = element;
				this.setupContent( element );
			}*/
			
			delete this.textarea;

			var editor = this.getParentEditor(),
				selection = editor.getSelection(),
				element = selection.getSelectedElement();

			if ( element && element.data( 'cke-real-element-type' ) && element.data( 'cke-real-element-type' ) == 'textarea' )
			{
				this.textarea = element;
				element = editor.restoreRealElement( this.textarea );
				this.setupContent( element );
				selection.selectElement( this.textarea );
			}
		},
		onOk : function()
		{
			var editor,
				element = this.textarea,
				isInsertMode = !element;

			if ( isInsertMode )
			{
				editor = this.getParentEditor();
				element = editor.document.createElement( 'textarea' );
			}
			this.commitContent( element );

			if ( isInsertMode ){
				//editor.insertElement( element );
				var fakeElement = editor.createFakeElement( element, 'cke_textarea', 'textarea', true );
				console.log(fakeElement);
				if ( !this.textarea )
					editor.insertElement( fakeElement );
				else
				{
					fakeElement.replace( this.textarea );
					editor.getSelection().selectElement( fakeElement );
				}
			}
		},
		contents : [
			{
				id : 'info',
				label : form.label.basicProperties,
				label : form.label.basicProperties,
				elements : [
					{
						type : 'html',
						align : 'left',
						html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.textarea+'</span>'
					},{

						type : 'hbox',
						widths:['50%','50%'],
						children:[
							{
								id : '_cke_saved_name',
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
								setup : function(name, element )
								{
									if ( name == 'clear' )
										this.setValue( this[ 'default' ] || '' );
									else if ( name == 'textarea' )
									{
										this.setValue(
												element.data( 'cke-saved-name' ) ||
												element.getAttribute( 'name' ) ||
												'' );
									}
								},
								commit : function( element )
								{
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
								accessKey : 'C',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								setup : function( name, element )
								{
									if ( name == 'clear' )
										this.setValue( '' );
									else if ( name == 'textarea' && element.getAttribute( 'value' ) )
										this.setValue( element.getAttribute( 'value' ) );
								},
								commit : function( element ){
									
									if ( this.getValue() )
										element.setAttribute( 'value', this.getValue() );
									else
										element.removeAttribute( 'value' );
								}
							}
						]
					
					},{
						id : 'isRequired',
						type : 'checkbox',
						label : form.label.isRequired,
						'default' : '',
						style : 'width:20px;',
						accessKey : 'M',
						//hidden : true,
						setup : function( name, element )
						{
							if ( name == 'clear' ){
								//this.setValue(element.removeAttribute( 'checked' ));
							}else if ( name == 'textarea' && element.getAttribute( 'class' ) ){
								var className = element.getAttribute( 'class' );
								if(className.indexOf("mandatory") > -1){
									this.setValue(element.setAttribute( 'checked' ));
								}
							}
						}
					},{
						type : 'hbox',
						widths:['50%','50%'],
						children:[
							{
								id : 'cols',
								type : 'text',
								label : editor.lang.forms.textarea.cols,
								'default' : '',
								accessKey : 'C',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									if(isNaN(this.getValue())){
										$.msgBox({
											title:form.title.validation,
											content: form.error.columnValueShouldBeNumber,
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
								setup : function( name, element )
								{
									if ( name == 'textarea' )
										this.setValue( element.getAttribute( 'cols' ) || '' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'cols', this.getValue() );
									else
										element.removeAttribute( 'cols' );
								}
							},
							{
								id : 'rows',
								type : 'text',
								label : editor.lang.forms.textarea.rows,
								'default' : '',
								accessKey : 'R',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									if(isNaN(this.getValue())){
										$.msgBox({
											title:form.title.validation,
									    	content:form.error.rowfieldtype,
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
								setup : function( name, element )
								{
									if ( name == 'textarea' )
										this.setValue( element.getAttribute( 'rows' ) || '' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'rows', this.getValue() );
									else
										element.removeAttribute( 'rows' );
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
								style : 'width:200px',
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
							    setup : function(name, element) {
							    	if ( name == 'clear' )
										this.setValue( '' );
									else if ( name == 'textarea' && element.getAttribute( 'table' ) )
										this.setValue( element.getAttribute( 'table' ) );
							    },
							    onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	getTableWithParentTable(element_id, $("#tableId").val(), $("#tableName").val());
							    },
							    onChange : function() {
							    	 var dialog = this.getDialog();
							         var values = dialog.getContentElement( 'info', 'column' );
							         var element_id = '#' + values.getInputElement().$.id;
							         getAllColumnByTableId(element_id, this.getValue(), 'textarea');
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
								setup : function(name, element) {
									if ( name == 'clear' )
										this.setValue( '' );
									else if ( name == 'textarea' && element.getAttribute( 'column' ) )
										this.setValue( element.getAttribute( 'column' ) );
								},
								onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	getAllColumnByTableId(element_id, $("#tableId").val(), 'textarea');
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
						accessKey : 'K',
						onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
						rows : 2,
						setup : function( name, element )
						{
							if ( name == 'clear'){
								this.setValue( '' );
							}else if( name == 'textarea'){
								this.setValue( element.getAttribute( 'onclick' ) );
							} 
							
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
						accessKey : 'F',
						onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
						rows:2,
						setup : function( name, element )
						{
							if ( name == 'clear'){
								this.setValue( '' );
							}else if( name == 'textarea'){
								this.setValue( element.getAttribute( 'ondblclick' ) );
							} 
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
							accessKey : 'F',
							onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
							rows : 2,
							setup : function( name, element )
							{
								if ( name == 'clear'){
									this.setValue( '' );
								}else if( name == 'textarea'){
									this.setValue( element.getAttribute( 'onfocus' ) );
								} 
							},
							commit : function( element )
							{
								if ( this.getValue() )
									element.setAttribute( 'onfocus', this.getValue() );
								else
									element.removeAttribute( 'onfocus' );
							}
						},
						{
							id : 'onblur',
							type : 'textarea',
							label :form.label.onblur,
							'default' : '',
							style : 'width:420px',
							accessKey : 'F',
							onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
							rows:2,
							setup : function( name, element )
							{
								if ( name == 'clear'){
									this.setValue( '' );
								}else if( name == 'textarea'){
									this.setValue( element.getAttribute( 'onblur' ) );
								} 
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
							accessKey : 'F',
							onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
							rows:2,
							setup : function( name, element )
							{
								if ( name == 'clear'){
									this.setValue( '' );
								}else if( name == 'textarea'){
									this.setValue( element.getAttribute( 'onkeypress' ) );
								} 
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
							style : 'height:160px',
							required : false,
							onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
							rows : 2,
							setup : function( name, element  )
							{
								if ( name == 'clear'){
									this.setValue( '' );
								}else if( name == 'textarea'){
									this.setValue( element.getAttribute( 'style' ) );
								} 
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
