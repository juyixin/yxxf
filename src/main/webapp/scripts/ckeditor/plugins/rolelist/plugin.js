
CKEDITOR.plugins.add( 'rolelist', {
	init: function( editor ){
		editor.addCommand( 'showRolelistInDialog', new CKEDITOR.dialogCommand( 'rolelist' ) );
		CKEDITOR.dialog.add( 'rolelist', function( editor ) {
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
				title : form.title.rolefield,
				minWidth : 350,
				minHeight : 150,
				onShow : function()
				{
					delete this.textField;

					var element = this.getParentEditor().getSelection().getSelectedElement();
					if ( element && element.getName() == "input" &&
							( acceptedTypes[ element.getAttribute( 'type' ) ] || !element.getAttribute( 'type' ) ) )
					{
						this.textField = element;
						this.setupContent( element );
					}
				},
				onOk : function()
				{
					var editor,
						element = this.textField,
						isInsertMode = !element;
					if ( isInsertMode )
					{
						editor = this.getParentEditor();
						console.log(editor);
						element = editor.document.createElement( 'input' );
						element.setAttribute( 'type', 'text' );
					}

					if ( isInsertMode ){
						//editor.insertElement( element );
						var fakeElement = editor.createFakeElement( element, 'cke_rolelist', 'text', true );
						console.log(fakeElement);
						if ( !this.text )
							editor.insertElement( fakeElement );
						else
						{
							fakeElement.replace( this.text );
							editor.getSelection().selectElement( fakeElement );
						}
					}
					this.commitContent( { element : element } );
				},
				onLoad : function()
				{
					var autoSetup = function( element )
					{
						var value = element.hasAttribute( this.id ) && element.getAttribute( this.id );
						this.setValue( value || '' );
					};

					var autoCommit = function( data )
					{
						var element = data.element;
						var value = this.getValue();

						if ( value )
							element.setAttribute( this.id, value );
						else
							element.removeAttribute( this.id );
					};

					this.foreach( function( contentObj )
						{
							if ( autoAttributes[ contentObj.id ] )
							{
								contentObj.setup = autoSetup;
								contentObj.commit = autoCommit;
							}
						} );
				},
				contents : [
					{
						id : 'info',
						label : form.label.basicProperties,
						title : form.label.basicProperties,
						elements : [
							{
								type : 'html',
								align : 'left',
								html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.rolelist+'</span>'
							}, 
							{
								id : '_cke_saved_name',
								type : 'text',
								label : editor.lang.forms.textfield.name+'<span class="color-red"> *</span>',
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
										id : 'size',
										type : 'text',
										label : editor.lang.forms.textfield.charWidth,
										'default' : '',
										accessKey : 'C',
										style : 'width:205px;',
										onShow : function(){
											var currentElement =  this;
											$("#"+currentElement._.labelId).css('color','#524847');
										},
										validate : function(){
											var currentElement = this;
											if(isNaN(this.getValue())){
												$.msgBox({
													title:form.title.validation,
													content:form.error.charwidthnumber,
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
										id : 'selectType',
										type : 'select',
										label : form.label.selectType,
										'default' : 'single',
										style : 'width:205px;',
										accessKey : 'E',
										onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
										items :
										[
										 	[ 'Single' , 'single' ],
										 	[ 'Multiple' , 'multi' ],
										],
										setup : function( element )
										{
											this.setValue( element.getAttribute( 'selectType' ) );
										},
										commit : function( element )
										{
											//var element = data.element;
											if ( CKEDITOR.env.ie )
											{
												var elementType = element.getAttribute( 'selectType' );
												var myType = this.getValue();

												if ( elementType != myType )
												{
													var replace = CKEDITOR.dom.element.createFromHtml( '<input selectType="' + myType + '"></input>', editor.document );
													element.copyAttributes( replace, { type : 1 } );
													replace.replace( element );
													editor.getSelection().selectElement( replace );
													data.element = replace;
												}
											}
											else
												element.setAttribute( 'selectType', this.getValue() );
										}
									}
								],
								onLoad : function()
								{
									// Repaint the style for IE7 (#6068)
									if ( CKEDITOR.env.ie7Compat )
										this.getElement().setStyle( 'zoom', '100%' );
								}
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
									         getAllColumnByTableId(element_id, this.getValue(), 'role');
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
									    	getAllColumnByTableId(element_id, $("#tableId").val(), 'role');
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
								type : 'select',
								label : form.label.inputType,
								'default' : 'rolelist',
								style : 'width:140px;',
								accessKey : 'M',
								hidden : true,
								items :
								[
									[ editor.lang.forms.textfield.typeText, 'rolelist' ]
								],
								setup : function( element )
								{
									this.setValue( element.getAttribute( 'type' ) );
								},
								commit : function( element )
								{
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
									else{
										element.setAttribute( 'type', this.getValue() );
									}
								}
							
							
							}
						]
					},{
						id: 'info2',
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
