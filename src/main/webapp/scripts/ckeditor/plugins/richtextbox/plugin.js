/**
 *  This plugin create date field (such as date field and date time field) with properties like name, style etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.plugins.add( 'richtextbox', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'RichTextbox', {
			label: form.title.pluginRichTextBox,
			command: 'showRichTextboxInDialog',
			icon: '/scripts/ckeditor/plugins/richtextbox/images/richtextbox.jpg'
		});
						
		editor.addCommand( 'showRichTextboxInDialog', new CKEDITOR.dialogCommand( 'richtextbox' ) );

		CKEDITOR.dialog.add( 'richtextbox', function( editor )
		{
			return {
				title : form.title.richtextbox,
				minWidth : 350,
				minHeight : 220,
				onShow : function(){},
				onOk : function(){},
				contents : [
					{
						id : 'info',
						label : form.title.richtextbox,
						title : form.title.richtextbox,
						elements : [
							{
								type : 'html',
								align : 'left',
								html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.richtextbox+'</span>'
							},
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
									else if ( name == 'img' )
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
								id : 'isRequired',
								type : 'checkbox',
								label : form.label.isRequired,
								'default' : '',
								accessKey : 'M',
								//hidden : true,
								
								setup : function(name, element) {
									if ( name == 'clear' )
										this.setValue( '' );
									else if ( name == 'img' && element.getAttribute( 'class' ) ){
										var className = element.getAttribute( 'class' );
										if(className.indexOf("mandatory") > -1){
											this.setValue(element.setAttribute( 'checked' ));
										}
									}
								}
							},
							{
								type : 'hbox',
								widths:['50%','50%'],
								children:[
									{
										id : 'width',
										type : 'text',
										label : 'Width',
										'default' : '755',
										accessKey : 'C',
										onShow : function() {
												var currentElement =  this;
												$("#"+currentElement._.labelId).css('color','#524847');
										},
										validate : function(){
											var currentElement = this;
											if(isNaN(this.getValue())){
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
										setup : function( name, element )
										{
											if ( name == 'img' )
												this.setValue( element.getAttribute( 'width' ) || '' );
										},
										commit : function( element )
										{
											if ( this.getValue() )
												element.setAttribute( 'width', this.getValue() );
											else
												element.removeAttribute( 'width' );
										}
									},
									{
										id : 'height',
										type : 'text',
										label : 'Height',
										'default' : '250',
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
											if ( name == 'img' )
												this.setValue( element.getAttribute( 'height' ) || '' );
										},
										commit : function( element )
										{
											if ( this.getValue() )
												element.setAttribute( 'height', this.getValue() );
											else
												element.removeAttribute( 'height' );
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
											else if ( name == 'img' && element.getAttribute( 'table' ) )
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
									         getAllColumnByTableId(element_id, this.getValue(), 'richtextbox');
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
											else if ( name == 'img' && element.getAttribute( 'column' ) )
												this.setValue( element.getAttribute( 'column' ) );
										},
										onLoad : function(){
									    	var element_id = '#' + this.getInputElement().$.id;
									    	getAllColumnByTableId(element_id, $("#tableId").val(), 'richtextbox');
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
									},{

										type : 'textarea',
										id : 'style',
										label : form.label.style,
										style : 'width:420px',
										required : false,
										hidden : true,
										rows : 2,
										setup : function( name, element )
										{
											if ( name == 'clear' )
												this.setValue( '' );
											else if ( name == 'img' && element.getAttribute( 'style' ) )
												this.setValue( element.getAttribute( 'style' ) || ''  );
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
					}
				]
			};
		});
	}
});
