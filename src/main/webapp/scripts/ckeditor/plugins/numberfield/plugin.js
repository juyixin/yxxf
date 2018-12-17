/**
 *  This plugin create number field (such as whole number and decimal) with properties like name, style, onFocus etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */

CKEDITOR.plugins.add( 'numberfield', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'NumberField', {
			label: form.title.pluginNUmberField,
			command: 'showNumberFieldInDialog',
			icon: '/scripts/ckeditor/plugins/numberfield/images/number.png'
		});
						
		editor.addCommand( 'showNumberFieldInDialog', new CKEDITOR.dialogCommand( 'numberfield' ) );
		
		CKEDITOR.dialog.add( 'numberfield', function( editor )
				{
					return {
						title : form.title.numberBoxProperties,
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
										html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.numberfield+'</span>'
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
												'default' : '',
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
												validate : function(){
													var currentElement = this;
													if(isNaN(this.getValue())){
														$.msgBox({
														    title:form.title.validation,
														    content: form.error.defaultvaluenumber,
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
													this.setValue( element.getAttribute( 'value' ) );
												},
												commit : function( element ){
													
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

										type : 'hbox',
										widths : [ '33%', '41%', '25%' ],
										children :
										[
											{
												id : 'size',
												type : 'text',
												label : editor.lang.forms.textfield.charWidth,
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
												accessKey : 'M',
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
												id : 'class',
												type : 'select',
												label : form.label.inputType,
												style : 'width:100px',
												'default' : 'number-validation',
												accessKey : 'M',
												onShow : function(){
													var currentElement =  this;
													$("#"+currentElement._.labelId).css('color','#524847');
												},
												items :
												[
													[ form.msg.pluginNumber, 'number-validation' ],
													[ form.msg.pluginDecimal, 'decimal-validation' ]
												],
												 onChange : function() {
											    	 var dialog = this.getDialog();
											         var values = dialog.getContentElement( 'info', 'column' );
											         var element_id = '#' + values.getInputElement().$.id;
											         var type = dialog.getValueOf('info','class');
											         if(type == "number-validation"){
											        	 type = "number";
											         }else if(type == "decimal-validation"){
											        	 type = "decimal";
											         }
											         var tableObj = dialog.getContentElement( 'info', 'table' );
											         getAllColumnByTableId(element_id, tableObj.getValue(), type);
											    },
												setup : function( element ){
													var className = element.getAttribute( 'class' );
													if(className.indexOf("number-validation") > -1){
														this.setValue('number-validation');
													}else if(className.indexOf("decimal-validation") > -1){
														this.setValue('decimal-validation');
													}else{
														this.setValue('');
													}
													//this.setValue( element.getAttribute( 'class' ) || '');
												},
												commit : function(element){/*
													if ( this.getValue() )
														element.setAttribute( 'class', this.getValue() );
													else
														element.removeAttribute( 'class' );
												*/}
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
											         var type = dialog.getValueOf('info','class');
											         if(type == "number-validation"){
											        	 type = "number";
											         }else if(type == "decimal-validation"){
											        	 type = "decimal";
											         }
											         var tableObj = dialog.getContentElement( 'info', 'table' );															
											         getAllColumnByTableId(element_id, tableObj.getValue(), type);
											       
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
													var dialog = this.getDialog();
											    	var element_id = '#' + this.getInputElement().$.id;
											    	var type = dialog.getValueOf('info','class');
											        if(type == "number-validation"){
											        	type = "number";
											        }else if(type == "decimal-validation"){
											        	type = "decimal";
											        }
											    	getAllColumnByTableId(element_id, $("#tableId").val(), 'number');
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
										id : 'type',
										type : 'text',
										label : editor.lang.forms.textfield.type,
										'default' : 'number',
										accessKey : 'M',
										hidden : true,
										setup : function( element )
										{
											this.setValue( element.getAttribute( 'type' ) );
										},
										commit : function( element ){
											
											if ( this.getValue() )
												element.setAttribute( 'type', this.getValue() );
											else
												element.removeAttribute( 'type' );
										}
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
										},{
											id : 'ondblclick',
											type : 'textarea',
											label : form.label.ondblclick,
											'default' : '',
											style : 'width:420px',
											accessKey : 'F',
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
											accessKey : 'F',
											onShow : function(){
													var currentElement =  this;
													$("#"+currentElement._.labelId).css('color','#524847');
												},
											rows:2,
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
											accessKey : 'F',
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
											accessKey : 'F',
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
		}
});
