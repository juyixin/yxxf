/**
 *  This plugin create file upload field  with properties like name, size, type etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.plugins.add( 'gridviewcontrol', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'GridviewControl', {
			label: form.title.gridViewControl,
			command: 'showGridviewControlInDialog',
			icon: '/images/easybpm/form/gridview.jpg'
		});
						
		editor.addCommand( 'showGridviewControlInDialog', new CKEDITOR.dialogCommand( 'gridviewcontrol' ) );
		
		CKEDITOR.dialog.add( 'gridviewcontrol', function( editor )
				{
					return {
						title : form.title.gridview,
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
								label : form.title.gridview,
								title : form.title.gridview,
								elements : [
								            {

												id : 'gridTitle',
												type : 'text',
												label : form.label.gridtitle+'<span class="color-red"> *</span>',
												onShow : function(){
													var currentElement =  this;
													$("#"+currentElement._.labelId).css('color','#524847');
												},
												validate : function(){
													var currentElement = this;
													var message = "";
													if(this.getValue() == ""){
														message = form.error.emptygridtitle
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
												setup : function( name, element )
												{
													if ( name == 'clear' )
														this.setValue( '' );
													else if ( name == 'img' && element.getAttribute( 'gridTitle' ) )
														this.setValue( element.getAttribute( 'gridTitle' ) );
												},
												commit : function( element )
												{
													//var element = data.element;

													if ( this.getValue() )
														element.setAttribute( 'gridTitle', this.getValue() );
													else
													{
														element.removeAttribute( 'gridTitle' );
													}
												}
											
								            },
										{
											id : 'gridName',
											type : 'select',
											label : form.label.listviewname+'<span class="color-red"> *</span>',
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
														content:form.error.emptylistview,
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
											items : [['',0]],
											setup : function(name, element) {
												if ( name == 'clear' )
													this.setValue( '' );
												else if ( name == 'img' && element.getAttribute( 'gridName' ) )
													this.setValue( element.getAttribute( 'gridName' ) );
											},
											onLoad : function(){
												var element_id = '#' + this.getInputElement().$.id;
										    	getAllListView(element_id);
										    },
										    onChange:function(){
										    	if(this.getValue() != ""){
										    		getListViewObjForParams(this.getValue());
										    	}
										    },
										    commit : function( element )
											{
										    	if ( this.getValue() )
													element.setAttribute( 'gridName', this.getValue() );
											}
										},{

											type : 'hbox',
											widths : [ '50%', '50%' ],
											children :
											[
												{
													id : 'height',
													type : 'text',
													label : form.label.height,
													'default' : '300',
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
																content: form.error.height,
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
													setup : function( name, element ){
														if ( name == 'clear' )
															this.setValue( '' );
														else if ( name == 'img' && element.getAttribute( 'height' ) )
															this.setValue( element.getAttribute( 'height' ) );
													},
													commit : function(element){
														if ( this.getValue() )
															element.setAttribute( 'height', this.getValue() );
														else
															element.removeAttribute( 'height' );
													}
												},
												{
													id : 'width',
													type : 'text',
													label : form.label.width,
													'default' : '500',
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
																content: form.error.width,
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
													setup : function( name, element ){
														if ( name == 'clear' )
															this.setValue( '' );
														else if ( name == 'img' && element.getAttribute( 'width' ) )
															this.setValue( element.getAttribute( 'width' ) );
													},
													commit : function(element){
														if ( this.getValue() )
															element.setAttribute( 'width', this.getValue() );
														else
															element.removeAttribute( 'width' );
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
													this.setValue( element.getAttribute( 'style' )  || '' );
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
