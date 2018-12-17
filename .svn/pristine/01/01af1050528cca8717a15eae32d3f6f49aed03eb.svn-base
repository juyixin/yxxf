/**
 *  This plugin create label field  with properties like name, style, onFocus etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */

CKEDITOR.plugins.add( 'label', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'Label', {
			label: form.title.pluginLabel,
			command: 'showLabelInDialog',
			icon: '/scripts/ckeditor/plugins/label/images/label.jpg'
		});
						
		editor.addCommand( 'showLabelInDialog', new CKEDITOR.dialogCommand( 'label' ) );
		
		CKEDITOR.dialog.add( 'label', function( editor )
				{
					return {
						title : form.title.labelProperties,
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
										html : '<span class="color-red">*</span><span class="form-title-msg">Label support only for varchar</span>'
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
													}/*else if(this.getValue().indexOf(' ') >= 0){
														message = form.error.namespace
													}else{
														var iChars = "~`!#$%^&*+=-[]\\\';,/{}|\":<>?";
														for (var i = 0; i < this.getValue().length; i++) {
														  if (iChars.indexOf(this.getValue().charAt(i)) != -1) {
															  message= form.error.specialcharacter
														  }
														}
													}*/
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