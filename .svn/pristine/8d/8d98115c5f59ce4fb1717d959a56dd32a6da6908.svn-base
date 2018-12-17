/**
 *  This plugin create file upload field  with properties like name, size, type etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.plugins.add( 'fileupload', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'File', {
			label: form.title.pluginFileUpload,
			command: 'showFileUploadInDialog',
			icon: '/scripts/ckeditor/plugins/fileupload/images/fileupload.png'
		});
						
		editor.addCommand( 'showFileUploadInDialog', new CKEDITOR.dialogCommand( 'fileupload' ) );
		
		CKEDITOR.dialog.add( 'fileupload', function( editor )
				{
					return {
						title : form.title.fileupload,
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
											html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.fileupload+'</span>'
										},
										{
										id : '_cke_saved_name',
										type : 'text',
										label : editor.lang.forms.textfield.name,
										'default' : '',
										accessKey : 'N',
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
										style : 'width:420px',
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
									},{

										id : 'isRequired',
										type : 'checkbox',
										label : form.label.isRequired,
										'default' : '',
										style : 'width:20px;',
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
											         getAllColumnByTableId(element_id, this.getValue(), 'file');
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
											    	getAllColumnByTableId(element_id, $("#tableId").val(), 'file');
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
										id : 'class',
										type : 'text',
										label : form.label.className,
										'default' : 'file-upload-control',
										style : 'width:420px;',
										accessKey : 'M',
										hidden : true,
										setup : function( element )
										{
											this.setValue( element.getAttribute( 'class' ) );
										},
										commit : function( element ){
											
											if ( this.getValue() )
												element.setAttribute( 'class', this.getValue() );
											else
												element.removeAttribute( 'class' );
										}
									},{
										id : 'size',
										type : 'text',
										label : 'Size',
										'default' : '',
										style : 'width:420px;',
										accessKey : 'M',
										hidden : true,
										setup : function( element )
										{
											this.setValue( element.getAttribute( 'size' ) );
										},
										commit : function( element ){
											
											if ( this.getValue() )
												element.setAttribute( 'size', this.getValue() );
											else
												element.removeAttribute( 'size' );
										}
									},{
										id : 'type',
										type : 'text',
										label : editor.lang.forms.textfield.type,
										'default' : 'file',
										style : 'width:420px;',
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
									},{
										id : 'multiple',
										type : 'text',
										label : editor.lang.forms.textfield.type,
										'default' : 'multiple',
										style : 'width:420px;',
										accessKey : 'M',
										hidden : true,
										setup : function( element )
										{
											this.setValue( element.getAttribute( 'multiple' ) );
										},
										commit : function( element ){
											
											if ( this.getValue() )
												element.setAttribute( 'multiple', this.getValue() );
											else
												element.removeAttribute( 'multiple' );
										}
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
										label :form.label.onchange,
										'default' : '',
										accessKey : 'G',
										style : 'height:160px',
										rows : 2,
										onShow : function(){
													var currentElement =  this;
													$("#"+currentElement._.labelId).css('color','#524847');
												},
										setup : function( name, element )
										{
											if ( name == 'select' )
												this.setValue( element.getAttribute( 'onchange' ) || '' );
										},commit : function( element ){
											
											if ( this.getValue() )
												element.setAttribute( 'onchange', this.getValue() );
											else
												element.removeAttribute( 'onchange' );
										}
									}
								]
							},{

								id: 'info3',
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
										setup : function( element)
										{
											if (element.getAttribute( 'style' ) )
												this.setValue( element.getAttribute( 'style' ) || '' );
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
