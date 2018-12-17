
/**
 *  This plugin create auto complete field with properties like name, type, style, onFocus etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.plugins.add( 'autocomplete', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'Autocomplete', {
			label: 'Auto Complete',
			command: 'showAutocompleteInDialog',
			icon: '/scripts/ckeditor/plugins/autocomplete/images/autocomplete.png'
		});
						
		editor.addCommand( 'showAutocompleteInDialog', new CKEDITOR.dialogCommand( 'autocomplete' ) );
		
		
		function addOption( combo, optionText, optionValue, documentObject, index )
		{
			combo = getSelect( combo );
			var oOption;
			if ( documentObject )
				oOption = documentObject.createElement( "OPTION" );
			else
				oOption = document.createElement( "OPTION" );

			if ( combo && oOption && oOption.getName() == 'option' )
			{
				if ( CKEDITOR.env.ie ) {
					if ( !isNaN( parseInt( index, 10) ) )
						combo.$.options.add( oOption.$, index );
					else
						combo.$.options.add( oOption.$ );

					oOption.$.innerHTML = optionText.length > 0 ? optionText : '';
					oOption.$.value     = optionValue;
				}
				else
				{
					if ( index !== null && index < combo.getChildCount() )
						combo.getChild( index < 0 ? 0 : index ).insertBeforeMe( oOption );
					else
						combo.append( oOption );

					oOption.setText( optionText.length > 0 ? optionText : '' );
					oOption.setValue( optionValue );
				}
			}
			else
				return false;

			return oOption;
		}
		// Remove all selected options from a SELECT object.
		function removeSelectedOptions( combo )
		{
			combo = getSelect( combo );

			// Save the selected index
			var iSelectedIndex = getSelectedIndex( combo );

			// Remove all selected options.
			for ( var i = combo.getChildren().count() - 1 ; i >= 0 ; i-- )
			{
				if ( combo.getChild( i ).$.selected )
					combo.getChild( i ).remove();
			}

			// Reset the selection based on the original selected index.
			setSelectedIndex( combo, iSelectedIndex );
		}
		//Modify option  from a SELECT object.
		function modifyOption( combo, index, title, value )
		{
			combo = getSelect( combo );
			if ( index < 0 )
				return false;
			var child = combo.getChild( index );
			child.setText( title );
			child.setValue( value );
			return child;
		}
		function removeAllOptions( combo )
		{
			combo = getSelect( combo );
			while ( combo.getChild( 0 ) && combo.getChild( 0 ).remove() )
			{ /*jsl:pass*/ }
		}
		// Moves the selected option by a number of steps (also negative).
		function changeOptionPosition( combo, steps, documentObject )
		{
			combo = getSelect( combo );
			var iActualIndex = getSelectedIndex( combo );
			if ( iActualIndex < 0 )
				return false;

			var iFinalIndex = iActualIndex + steps;
			iFinalIndex = ( iFinalIndex < 0 ) ? 0 : iFinalIndex;
			iFinalIndex = ( iFinalIndex >= combo.getChildCount() ) ? combo.getChildCount() - 1 : iFinalIndex;

			if ( iActualIndex == iFinalIndex )
				return false;

			var oOption = combo.getChild( iActualIndex ),
				sText	= oOption.getText(),
				sValue	= oOption.getValue();

			oOption.remove();

			oOption = addOption( combo, sText, sValue, ( !documentObject ) ? null : documentObject, iFinalIndex );
			setSelectedIndex( combo, iFinalIndex );
			return oOption;
		}
		function getSelectedIndex( combo )
		{
			combo = getSelect( combo );
			return combo ? combo.$.selectedIndex : -1;
		}
		function setSelectedIndex( combo, index )
		{
			combo = getSelect( combo );
			if ( index < 0 )
				return null;
			var count = combo.getChildren().count();
			combo.$.selectedIndex = ( index >= count ) ? ( count - 1 ) : index;
			return combo;
		}
		function getOptions( combo )
		{
			combo = getSelect( combo );
			return combo ? combo.getChildren() : false;
		}
		function getSelect( obj )
		{
			if ( obj && obj.domId && obj.getInputElement().$ )				// Dialog element.
				return  obj.getInputElement();
			else if ( obj && obj.$ )
				return obj;
			return false;
		}
		
		CKEDITOR.dialog.add( 'autocomplete', function( editor ) {
		
		return {
			title : form.label.autocompletefield,
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
					label : form.label.autocompletefield,
					title : form.label.autocompletefield,
					elements : [
							{
								type : 'html',
								align : 'left',
								html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.autocompletefield+'</span>'
							},{

								type : 'hbox',
								widths : [ '50%', '50%' ],
								children :
								[
									{
										id : '_cke_saved_name',
										type : 'text',
										style : 'width:205px',
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
										id : 'dictionary_combo',
										type : 'text',
										style : 'width:205px',
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
											if(this.getValue() == ""){
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
							widths : [ '100%', '0%' ],
							children :
							[
								{
									id : 'size',
									type : 'text',
									label : editor.lang.forms.textfield.charWidth,
									'default' : '',
									accessKey : 'C',
									style : 'width:420px',
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
									hidden:true,
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
									onLoad : function(){
								    	var element_id = '#' + this.getInputElement().$.id;
								    	getAllColumnByTableId(element_id, $("#tableId").val(), 'text');
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
							'default' : 'text',
							style : 'width:420px;',
							accessKey : 'M',
							hidden : true,
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
										var replace = CKEDITOR.dom.element.createFromHtml( '<input type="radio"></input>', editor.document );
										element.copyAttributes( replace, { type : 1 } );
										replace.replace( element );
										editor.getSelection().selectElement( replace );
										data.element = replace;
									}
								}
								else
									element.setAttribute( 'type', this.getValue() );
							}
						},{
					    	type : 'text',
							id:	"datadictionaryid_hidden",
							label: 'Data Dictionary Id',
							hidden: true
					    },{
							type : 'textarea',
							id : 'style',
							label : form.label.style,
							style : 'width:420px',
							required : false,
							hidden : true,
							rows : 2,
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
