/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license

CKEDITOR.dialog.add("select",function(c){function h(a,b,e,d,c){a=f(a);d=d?d.createElement("OPTION"):document.createElement("OPTION");if(a&&d&&"option"==d.getName())CKEDITOR.env.ie?(isNaN(parseInt(c,10))?a.$.options.add(d.$):a.$.options.add(d.$,c),d.$.innerHTML=0<b.length?b:"",d.$.value=e):(null!==c&&c<a.getChildCount()?a.getChild(0>c?0:c).insertBeforeMe(d):a.append(d),d.setText(0<b.length?b:""),d.setValue(e));else return!1;return d}function m(a){for(var a=f(a),b=g(a),e=a.getChildren().count()-1;0<=
e;e--)a.getChild(e).$.selected&&a.getChild(e).remove();i(a,b)}function n(a,b,e,d){a=f(a);if(0>b)return!1;a=a.getChild(b);a.setText(e);a.setValue(d);return a}function k(a){for(a=f(a);a.getChild(0)&&a.getChild(0).remove(););}function j(a,b,e){var a=f(a),d=g(a);if(0>d)return!1;b=d+b;b=0>b?0:b;b=b>=a.getChildCount()?a.getChildCount()-1:b;if(d==b)return!1;var d=a.getChild(d),c=d.getText(),o=d.getValue();d.remove();d=h(a,c,o,!e?null:e,b);i(a,b);return d}function g(a){return(a=f(a))?a.$.selectedIndex:-1}
function i(a,b){a=f(a);if(0>b)return null;var e=a.getChildren().count();a.$.selectedIndex=b>=e?e-1:b;return a}function l(a){return(a=f(a))?a.getChildren():!1}function f(a){return a&&a.domId&&a.getInputElement().$?a.getInputElement():a&&a.$?a:!1}return{title:c.lang.forms.select.title,minWidth:CKEDITOR.env.ie?460:395,minHeight:CKEDITOR.env.ie?320:300,onShow:function(){delete this.selectBox;this.setupContent("clear");var a=this.getParentEditor().getSelection().getSelectedElement();if(a&&"select"==a.getName()){this.selectBox=
a;this.setupContent(a.getName(),a);for(var a=l(a),b=0;b<a.count();b++)this.setupContent("option",a.getItem(b))}},onOk:function(){var a=this.getParentEditor(),b=this.selectBox,e=!b;e&&(b=a.document.createElement("select"));this.commitContent(b);if(e&&(a.insertElement(b),CKEDITOR.env.ie)){var d=a.getSelection(),c=d.createBookmarks();setTimeout(function(){d.selectBookmarks(c)},0)}},contents:[{id:"info",label:c.lang.forms.select.selectInfo,title:c.lang.forms.select.selectInfo,accessKey:"",elements:[{id:"txtName",
type:"text",widths:["25%","75%"],labelLayout:"horizontal",label:c.lang.common.name,"default":"",accessKey:"N",style:"width:350px",setup:function(a,b){"clear"==a?this.setValue(this["default"]||""):"select"==a&&this.setValue(b.data("cke-saved-name")||b.getAttribute("name")||"")},commit:function(a){this.getValue()?a.data("cke-saved-name",this.getValue()):(a.data("cke-saved-name",!1),a.removeAttribute("name"))}},{id:"txtValue",type:"text",widths:["25%","75%"],labelLayout:"horizontal",label:c.lang.forms.select.value,
style:"width:350px","default":"",className:"cke_disabled",onLoad:function(){this.getInputElement().setAttribute("readOnly",!0)},setup:function(a,b){"clear"==a?this.setValue(""):"option"==a&&b.getAttribute("selected")&&this.setValue(b.$.value)}},{type:"hbox",widths:["175px","170px"],children:[{id:"txtSize",type:"text",labelLayout:"horizontal",label:c.lang.forms.select.size,"default":"",accessKey:"S",style:"width:175px",validate:function(){var a=CKEDITOR.dialog.validate.integer(c.lang.common.validateNumberFailed);
return""===this.getValue()||a.apply(this)},setup:function(a,b){"select"==a&&this.setValue(b.getAttribute("size")||"");CKEDITOR.env.webkit&&this.getInputElement().setStyle("width","86px")},commit:function(a){this.getValue()?a.setAttribute("size",this.getValue()):a.removeAttribute("size")}},{type:"html",html:"<span>"+CKEDITOR.tools.htmlEncode(c.lang.forms.select.lines)+"</span>"}]},{type:"html",html:"<span>"+CKEDITOR.tools.htmlEncode(c.lang.forms.select.opAvail)+"</span>"},{type:"hbox",widths:["115px",
"115px","100px"],children:[{type:"vbox",children:[{id:"txtOptName",type:"text",label:c.lang.forms.select.opText,style:"width:115px",setup:function(a){"clear"==a&&this.setValue("")}},{type:"select",id:"cmbName",label:"",title:"",size:5,style:"width:115px;height:75px",items:[],onChange:function(){var a=this.getDialog(),b=a.getContentElement("info","cmbValue"),e=a.getContentElement("info","txtOptName"),a=a.getContentElement("info","txtOptValue"),d=g(this);i(b,d);e.setValue(this.getValue());a.setValue(b.getValue())},
setup:function(a,b){"clear"==a?k(this):"option"==a&&h(this,b.getText(),b.getText(),this.getDialog().getParentEditor().document)},commit:function(a){var b=this.getDialog(),e=l(this),d=l(b.getContentElement("info","cmbValue")),c=b.getContentElement("info","txtValue").getValue();k(a);for(var f=0;f<e.count();f++){var g=h(a,e.getItem(f).getValue(),d.getItem(f).getValue(),b.getParentEditor().document);d.getItem(f).getValue()==c&&(g.setAttribute("selected","selected"),g.selected=!0)}}}]},{type:"vbox",children:[{id:"txtOptValue",
type:"text",label:c.lang.forms.select.opValue,style:"width:115px",setup:function(a){"clear"==a&&this.setValue("")}},{type:"select",id:"cmbValue",label:"",size:5,style:"width:115px;height:75px",items:[],onChange:function(){var a=this.getDialog(),b=a.getContentElement("info","cmbName"),e=a.getContentElement("info","txtOptName"),a=a.getContentElement("info","txtOptValue"),d=g(this);i(b,d);e.setValue(b.getValue());a.setValue(this.getValue())},setup:function(a,b){if("clear"==a)k(this);else if("option"==
a){var e=b.getValue();h(this,e,e,this.getDialog().getParentEditor().document);"selected"==b.getAttribute("selected")&&this.getDialog().getContentElement("info","txtValue").setValue(e)}}}]},{type:"vbox",padding:5,children:[{type:"button",id:"btnAdd",style:"",label:c.lang.forms.select.btnAdd,title:c.lang.forms.select.btnAdd,style:"width:100%;",onClick:function(){var a=this.getDialog();a.getParentEditor();var b=a.getContentElement("info","txtOptName"),e=a.getContentElement("info","txtOptValue"),d=a.getContentElement("info",
"cmbName"),c=a.getContentElement("info","cmbValue");h(d,b.getValue(),b.getValue(),a.getParentEditor().document);h(c,e.getValue(),e.getValue(),a.getParentEditor().document);b.setValue("");e.setValue("")}},{type:"button",id:"btnModify",label:c.lang.forms.select.btnModify,title:c.lang.forms.select.btnModify,style:"width:100%;",onClick:function(){var a=this.getDialog(),b=a.getContentElement("info","txtOptName"),e=a.getContentElement("info","txtOptValue"),d=a.getContentElement("info","cmbName"),a=a.getContentElement("info",
"cmbValue"),c=g(d);0<=c&&(n(d,c,b.getValue(),b.getValue()),n(a,c,e.getValue(),e.getValue()))}},{type:"button",id:"btnUp",style:"width:100%;",label:c.lang.forms.select.btnUp,title:c.lang.forms.select.btnUp,onClick:function(){var a=this.getDialog(),b=a.getContentElement("info","cmbName"),c=a.getContentElement("info","cmbValue");j(b,-1,a.getParentEditor().document);j(c,-1,a.getParentEditor().document)}},{type:"button",id:"btnDown",style:"width:100%;",label:c.lang.forms.select.btnDown,title:c.lang.forms.select.btnDown,
onClick:function(){var a=this.getDialog(),b=a.getContentElement("info","cmbName"),c=a.getContentElement("info","cmbValue");j(b,1,a.getParentEditor().document);j(c,1,a.getParentEditor().document)}}]}]},{type:"hbox",widths:["40%","20%","40%"],children:[{type:"button",id:"btnSetValue",label:c.lang.forms.select.btnSetValue,title:c.lang.forms.select.btnSetValue,onClick:function(){var a=this.getDialog(),b=a.getContentElement("info","cmbValue");a.getContentElement("info","txtValue").setValue(b.getValue())}},
{type:"button",id:"btnDelete",label:c.lang.forms.select.btnDelete,title:c.lang.forms.select.btnDelete,onClick:function(){var a=this.getDialog(),b=a.getContentElement("info","cmbName"),c=a.getContentElement("info","cmbValue"),d=a.getContentElement("info","txtOptName"),a=a.getContentElement("info","txtOptValue");m(b);m(c);d.setValue("");a.setValue("")}},{id:"chkMulti",type:"checkbox",label:c.lang.forms.select.chkMulti,"default":"",accessKey:"M",value:"checked",setup:function(a,b){"select"==a&&this.setValue(b.getAttribute("multiple"));
CKEDITOR.env.webkit&&this.getElement().getParent().setStyle("vertical-align","middle")},commit:function(a){this.getValue()?a.setAttribute("multiple",this.getValue()):a.removeAttribute("multiple")}}]}]}]}});*/

﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 *  This plugin create select box with properties like name, style, onFocus etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.dialog.add( 'select', function( editor )
{
	// Add a new option to a SELECT object (combo or list).
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
	
	// Remove all selected options from a SELECT object.
	function removeAllSelectedOptions( combo )
	{
		combo = getSelect( combo );

		// Remove all selected options.
		for ( var i = combo.getChildren().count() - 1 ; i >= 0 ; i-- )
		{
				combo.getChild( i ).remove();
		}
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

	return {
		title : form.title.selectBox,
		minWidth : 395,
		minHeight : 150,
		onShow : function()
		{
			
			
			delete this.selectBox;
			this.setupContent( 'clear' );
			var element = this.getParentEditor().getSelection().getSelectedElement();
			if ( element && element.getName() == "select" )
			{
				this.selectBox = element;
				this.setupContent( element.getName(), element );

				// Load Options into dialog.
				var objOptions = getOptions( element );
				for ( var i = 0 ; i < objOptions.count() ; i++ )
					this.setupContent( 'option', objOptions.getItem( i ) );
			}
		},
		onOk : function()
		{
			var editor = this.getParentEditor(),
				element = this.selectBox,
				isInsertMode = !element;

			if ( isInsertMode )
				element = editor.document.createElement( 'select' );

			if ( isInsertMode )
			{
				var fakeElement = editor.createFakeElement( element, 'cke_select', 'select', true );
				if ( !this.select )
					editor.insertElement( fakeElement );
				else
				{
					fakeElement.replace( this.select );
					editor.getSelection().selectElement( fakeElement );
				}
				
				//editor.insertElement( element );
				if ( CKEDITOR.env.ie )
				{
					var sel = editor.getSelection(),
						bms = sel.createBookmarks();
					setTimeout(function()
					{
						sel.selectBookmarks( bms );
					}, 0 );
				}
			}
			this.commitContent( element );
		},
		contents : [
			{
				id : 'info',
				label : form.label.basicProperties,
				title : form.label.basicProperties,
				accessKey : '',
				elements : [
					{
						type : 'html',
						align : 'left',
						html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.selectbox+'</span>'
					},   
					{
						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : 'txtName',
								type : 'text',
								//widths : [ '25%','75%' ],
								//labelLayout : 'horizontal',
								label : editor.lang.common.name +"<span class='color-red'> *</span>",
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
								//style : 'width:350px',
								setup : function( name, element )
								{
									if ( name == 'clear' )
										this.setValue( this[ 'default' ] || '' );
									else if ( name == 'select' )
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
								id : 'txtValue',
								type : 'text',
								//widths : [ '25%','75%' ],
								//labelLayout : 'horizontal',
								label : editor.lang.forms.select.value,
								//style : 'width:350px',
								'default' : '',
								className : 'cke_disabled',
								onLoad : function()
								{
									this.getInputElement().setAttribute( 'readOnly', true );
								},
								setup : function( name, element )
								{
									if ( name == 'clear'){
										this.setValue( '' );
									}else if( name == 'select'){
										this.setValue( element.$.value );
									} 
										
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
							}else if ( name == 'select' && element.getAttribute( 'class' ) ){
								var className = element.getAttribute( 'class' );
								if(className.indexOf("mandatory") > -1){
									this.setValue(element.setAttribute( 'checked' ));
								}
							}
						}
					
					},
					{
						type : 'hbox',
						widths : [ '175px', '170px' ],
						hidden : true,
						children :
						[
							{
								id : 'txtSize',
								type : 'text',
								labelLayout : 'horizontal',
								label : editor.lang.forms.select.size,
								'default' : '1',
								accessKey : 'S',
								style : 'width:175px',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									if(isNaN(this.getValue())){
										$.msgBox({
							    		    title:form.title.validation,
							    		    content:form.error.sizefieldtype,
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
									if ( name == 'select' )
										this.setValue( element.getAttribute( 'size' ) || '' );
									if ( CKEDITOR.env.webkit )
										this.getInputElement().setStyle( 'width', '86px' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'size', this.getValue() );
									else
										element.removeAttribute( 'size' );
								}
							},
							{
								type : 'html',
								html : '<span>' + CKEDITOR.tools.htmlEncode( editor.lang.forms.select.lines ) + '</span>'
							}
						]
					},
					{

						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								type : 'select',
								id : 'data_choice',
								label : editor.lang.forms.select.opAvail,
								title : editor.lang.forms.select.opAvail,
								size : 1,
								style : 'width:200px;height:25px',
								items : [[form.title.pluginStatic,'static'],[form.title.pluginDynamic,'dynamic']],
								onChange : function() 
								{
									if(this.getValue() == "dynamic") {
										var dialog = this.getDialog();
										var sectionElement = dialog.getContentElement( 'info', 'content_hbox' );
										sectionElement.getElement().hide();
										var selectedElement = dialog.getContentElement( 'info', 'set_selected_content' );
										selectedElement.getElement().hide();
										var dictComboElement = dialog.getContentElement( 'info', 'dictionary_combo' );
										dictComboElement.getElement().show();										
										
									}else if(this.getValue() == "static") {
										var dialog = this.getDialog();
										var sectionElement = dialog.getContentElement( 'info', 'content_hbox' );
										sectionElement.getElement().show();
										var selectedElement = dialog.getContentElement( 'info', 'set_selected_content' );
										selectedElement.getElement().show();
										var dictComboElement = dialog.getContentElement( 'info', 'dictionary_combo' );
										dictComboElement.getElement().hide();
									}
									
								},
								setup : function(name, element) {
//									var dialog = this.getDialog();
//									var sectionElement = dialog.getContentElement( 'info', 'dictionary_combo' );
//									sectionElement.getElement().hide();
									if ( name == 'clear' )
										this.setValue( '' );
									else if ( name == 'select' && element.getAttribute( 'optiontype' ) )
										this.setValue( element.getAttribute( 'optiontype' ) );
								},
								onLoad : function(){
//									 var dialog = this.getDialog();
//										var sectionElement = dialog.getContentElement( 'info', 'dictionary_combo' );
//										sectionElement.getElement().hide();
								    	//getTableWithParentTable(element_id, $("#tableId").val(), $("#tableName").val());
									if(this.getValue() == "static") {
										var dialog = this.getDialog();
										var dictComboElement = dialog.getContentElement( 'info', 'dictionary_combo' );
										dictComboElement.getElement().hide();
										
									}else if(this.getValue() == "dynamic"){
										var dialog = this.getDialog(),
										names = dialog.getContentElement( 'info', 'cmbName' ),
										values = dialog.getContentElement( 'info', 'cmbValue' );
										removeAllSelectedOptions( names );
										removeAllSelectedOptions( values );
									}
								},
							    commit : function( element )
								{
							    	//var element = data.element;
							    	if ( CKEDITOR.env.ie )
									{
										var elementType = element.getAttribute( 'data_choice' );
										var optionTypeValue = this.getValue();
										if ( elementType != table )
										{
											var replace = CKEDITOR.dom.element.createFromHtml( '<input data_choice="' + optionTypeValue + '"></input>', editor.document );
											element.copyAttributes( replace, { type : 1 } );
											replace.replace( element );
											editor.getSelection().selectElement( replace );
											data.element = replace;
										}
									}
									else {
										element.setAttribute( 'optionType', this.getValue() );
									}
								},
							},
							{
								id : 'dictionary_combo',
								type : 'text',
								label : form.label.dataDictionary+'<span class="color-red"> *</span>',
								'default' : '',
								accessKey : 'C',
								style : 'width:200px',
								onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
								validate : function(){
									var currentElement = this;
									var currentDialog = CKEDITOR.dialog.getCurrent();
									var radioDataChoice = currentDialog.getValueOf( 'info', 'data_choice' );
									if(this.getValue() == "" && radioDataChoice == "dynamic"){
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
							    setup : function(name, element) { 
									var currentDialog = CKEDITOR.dialog.getCurrent();
							    	var comboDataChoice = currentDialog.getValueOf( 'info', 'data_choice' );
							    	if(comboDataChoice == "dynamic"){
										if ( name == 'clear' ){
											this.setValue( '' );
										}else if ( name == 'select' && element.getAttribute( 'dataDictionary' ) ){
											this.setValue( element.getAttribute( 'dataDictionaryLabel' ) );
											var currentDialog = CKEDITOR.dialog.getCurrent();
											currentDialog.setValueOf( 'info', 'datadictionaryid_hidden', element.getAttribute( 'dataDictionary' ));
										}
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
						]
					},
					{
				    	type : 'text',
						id:	"datadictionaryid_hidden",
						label: form.title.pluginDataDictionaryId,
						hidden: true
				    },
					{
						type : 'hbox',
						id:"content_hbox",
						widths : [ '115px', '115px' ,'100px','100px' ],
						children :
						[
							{
								type : 'vbox',
								children :
								[
									{
										id : 'txtOptName',
										type : 'text',
										label : editor.lang.forms.select.opText,
										style : 'width:115px',
										setup : function( name, element )
										{
											if ( name == 'clear' )
												this.setValue( "" );
										}
									},
									{
										type : 'select',
										id : 'cmbName',
										label : '',
										title : '',
										size : 5,
										style : 'width:115px;height:75px',
										items : [],
										onChange : function()
										{
											var dialog = this.getDialog(),
												values = dialog.getContentElement( 'info', 'cmbValue' ),
												optName = dialog.getContentElement( 'info', 'txtOptName' ),
												optValue = dialog.getContentElement( 'info', 'txtOptValue' ),
												iIndex = getSelectedIndex( this );

											setSelectedIndex( values, iIndex );
											optName.setValue( this.getValue() );
											optValue.setValue( values.getValue() );
										},
										setup : function( name, element )
										{
											if ( name == 'clear' )
												removeAllOptions( this );
											else if ( name == 'option' )
												addOption( this, element.getText(), element.getText(),
													this.getDialog().getParentEditor().document );
										},
										commit : function( element )
										{
											var dialog = this.getDialog(),
												optionsNames = getOptions( this ),
												optionsValues = getOptions( dialog.getContentElement( 'info', 'cmbValue' ) ),
												selectValue = dialog.getContentElement( 'info', 'txtValue' ).getValue();

											removeAllOptions( element );

											for ( var i = 0 ; i < optionsNames.count() ; i++ )
											{
												var oOption = addOption( element, optionsNames.getItem( i ).getValue(),
													optionsValues.getItem( i ).getValue(), dialog.getParentEditor().document );
												if ( optionsValues.getItem( i ).getValue() == selectValue )
												{
													oOption.setAttribute( 'selected', 'selected' );
													oOption.selected = true;
												}
											}
										}
									}
								]
							},
							{
								type : 'vbox',
								children :
								[
									{
										id : 'txtOptValue',
										type : 'text',
										label : editor.lang.forms.select.opValue,
										style : 'width:115px',
										setup : function( name, element )
										{
											if ( name == 'clear' )
												this.setValue( "" );
										}
									},
									{
										type : 'select',
										id : 'cmbValue',
										label : '',
										size : 5,
										style : 'width:115px;height:75px',
										items : [],
										onChange : function()
										{
											var dialog = this.getDialog(),
												names = dialog.getContentElement( 'info', 'cmbName' ),
												optName = dialog.getContentElement( 'info', 'txtOptName' ),
												optValue = dialog.getContentElement( 'info', 'txtOptValue' ),
												iIndex = getSelectedIndex( this );

											setSelectedIndex( names, iIndex );
											optName.setValue( names.getValue() );
											optValue.setValue( this.getValue() );
										},
										setup : function( name, element )
										{
											if ( name == 'clear' )
												removeAllOptions( this );
											else if ( name == 'option' )
											{
												var oValue	= element.getValue();
												addOption( this, oValue, oValue,
													this.getDialog().getParentEditor().document );
												if ( element.getAttribute( 'selected' ) == 'selected' )
													this.getDialog().getContentElement( 'info', 'txtValue' ).setValue( oValue );
											}
										}
									}
										
								]
							},{

								type : 'vbox',
								style:'padding-top:60px;',
								children :
								[
									{
										type : 'button',
										id : 'btnUp',
										label : form.label.btnUp,
										title : form.title.btnUp,
										onClick : function()
										{
											//Move up.
											var dialog = this.getDialog(),
												names = dialog.getContentElement( 'info', 'cmbName' ),
												values = dialog.getContentElement( 'info', 'cmbValue' );

											changeOptionPosition( names, -1, dialog.getParentEditor().document );
											changeOptionPosition( values, -1, dialog.getParentEditor().document );
										}
									},
									{
										type : 'button',
										id : 'btnDown',
										label : form.label.btnDown,
										title : form.title.btnDown,
										onClick : function()
										{
											//Move down.
											var dialog = this.getDialog(),
												names = dialog.getContentElement( 'info', 'cmbName' ),
												values = dialog.getContentElement( 'info', 'cmbValue' );

											changeOptionPosition( names, 1, dialog.getParentEditor().document );
											changeOptionPosition( values, 1, dialog.getParentEditor().document );
										}
									}
								]
							
							},
							{
								type : 'vbox',
								style:'padding-top:20px;line-height:2;',
								children :
								[
									{
										type : 'button',
										id : 'btnAdd',
										style : '',
										label : form.label.btnAdd,
										title : form.label.btnAdd,
										style : 'width:100%;',
										onClick : function()
										{
											//Add new option.
											var dialog = this.getDialog(),
												parentEditor = dialog.getParentEditor(),
												optName = dialog.getContentElement( 'info', 'txtOptName' ),
												optValue = dialog.getContentElement( 'info', 'txtOptValue' ),
												names = dialog.getContentElement( 'info', 'cmbName' ),
												values = dialog.getContentElement( 'info', 'cmbValue' );
											if(optName.getValue() == ""){
												$.msgBox({
									    		    title:form.title.validation,
									    		    content:form.msg.pluginSelectError,
									    		    afterClose:function(){
									    		    	optName.getInputElement().focus();
									    		    	$("#"+optName._.labelId).css('color','#FF225A');
									    		    }
									   			});
											}else if(optValue.getValue() == ""){
												$.msgBox({
									    		    title:form.title.validation,
									    		    content:form.msg.pluginSelectValueError,
									    		    afterClose:function(){
									    		    	optValue.getInputElement().focus();
									    		    	$("#"+optValue._.labelId).css('color','#FF225A');
									    		    }
									   			});
											}else{
												addOption(names, optName.getValue(), optName.getValue(), dialog.getParentEditor().document );
												addOption(values, optValue.getValue(), optValue.getValue(), dialog.getParentEditor().document );
												optName.setValue( "" );
												optValue.setValue( "" );
												$("#"+optName._.labelId).css('color','#524847');
												$("#"+optValue._.labelId).css('color','#524847');
											}
										}
									},
									{
										type : 'button',
										id : 'btnModify',
										label : form.label.btnModify,
										title : form.label.btnModify,
										style : 'width:100%;',
										onClick : function()
										{
											//Modify selected option.
											var dialog = this.getDialog(),
												optName = dialog.getContentElement( 'info', 'txtOptName' ),
												optValue = dialog.getContentElement( 'info', 'txtOptValue' ),
												names = dialog.getContentElement( 'info', 'cmbName' ),
												values = dialog.getContentElement( 'info', 'cmbValue' ),
												iIndex = getSelectedIndex( names );

											if ( iIndex >= 0 )
											{
												modifyOption( names, iIndex, optName.getValue(), optName.getValue() );
												modifyOption( values, iIndex, optValue.getValue(), optValue.getValue() );
											}
										}
									},
									{

										type : 'button',
										id : 'btnDelete',
										style : 'width:100%;',
										label : form.label.btnDelete,
										title : editor.lang.forms.select.btnDelete,
										onClick : function()
										{
											// Delete option.
											var dialog = this.getDialog(),
												names = dialog.getContentElement( 'info', 'cmbName' ),
												values = dialog.getContentElement( 'info', 'cmbValue' ),
												optName = dialog.getContentElement( 'info', 'txtOptName' ),
												optValue = dialog.getContentElement( 'info', 'txtOptValue' );

											removeSelectedOptions( names );
											removeSelectedOptions( values );

											optName.setValue( "" );
											optValue.setValue( "" );
										}
									
									}
								]
							}
						]
					},
					{
						id:"set_selected_content",
						type : 'hbox',
						widths : [ '50%', '40%' ],
						children :
						[
							{
								type : 'button',
								id : 'btnSetValue',
								label : form.label.btnSetAsSelectedValue,
								title : form.label.btnSetAsSelectedValue,
								onClick : function()
								{
									//Set as default value.
									var dialog = this.getDialog(),
										values = dialog.getContentElement( 'info', 'cmbValue' ),
										txtValue = dialog.getContentElement( 'info', 'txtValue' );
									txtValue.setValue( values.getValue() );
								}
							},
							{
								type : 'button',
								id : 'btnRemoveValue',
								label : form.label.btnRemoveValue,
								title : form.label.btnRemoveValue,
								onClick : function()
								{
									//Set as default value.
									var dialog = this.getDialog(),
									txtValue = dialog.getContentElement( 'info', 'txtValue' );
									txtValue.setValue( '' );
								}
							}
							/*,
							{
								id : 'chkMulti',
								type : 'checkbox',
								label : editor.lang.forms.select.chkMulti,
								'default' : '',
								accessKey : 'M',
								value : "checked",
								setup : function( name, element )
								{
									if ( name == 'select' )
										this.setValue( element.getAttribute( 'multiple' ) );
									if ( CKEDITOR.env.webkit )
										this.getElement().getParent().setStyle( 'vertical-align', 'middle' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'multiple', this.getValue() );
									else
										element.removeAttribute( 'multiple' );
								}
							}*/
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
									else if ( name == 'select' && element.getAttribute( 'table' ) )
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
							         getAllColumnByTableId(element_id, this.getValue(), 'select');
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
								style : 'width:200px',
								items : [['',0]],
								setup : function(name, element) {
									if ( name == 'clear' )
										this.setValue( '' );
									else if ( name == 'select' && element.getAttribute( 'column' ) )
										this.setValue( element.getAttribute( 'column' ) );
								},
								onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	getAllColumnByTableId(element_id, $("#tableId").val(), 'select');
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
						id : 'onchange',
						type : 'textarea',
						label : form.label.onchange,
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
						onShow : function(){
									var currentElement =  this;
									$("#"+currentElement._.labelId).css('color','#524847');
								},
						rows : 2,
						setup : function( name, element  )
						{
							if ( name == 'select' )
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
