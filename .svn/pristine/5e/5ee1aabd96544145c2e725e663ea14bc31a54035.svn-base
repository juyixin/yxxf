﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

// Comment by mahesh for extend the button properties files

/*CKEDITOR.dialog.add("button",function(b){function d(a){var b=this.getValue();b?(a.attributes[this.id]=b,"name"==this.id&&(a.attributes["data-cke-saved-name"]=b)):(delete a.attributes[this.id],"name"==this.id&&delete a.attributes["data-cke-saved-name"])}return{title:b.lang.forms.button.title,minWidth:350,minHeight:150,onShow:function(){delete this.button;var a=this.getParentEditor().getSelection().getSelectedElement();a&&a.is("input")&&a.getAttribute("type")in{button:1,reset:1,submit:1}&&(this.button=
a,this.setupContent(a))},onOk:function(){var a=this.getParentEditor(),b=this.button,d=!b,c=b?CKEDITOR.htmlParser.fragment.fromHtml(b.getOuterHtml()).children[0]:new CKEDITOR.htmlParser.element("input");this.commitContent(c);var e=new CKEDITOR.htmlParser.basicWriter;c.writeHtml(e);c=CKEDITOR.dom.element.createFromHtml(e.getHtml(),a.document);d?a.insertElement(c):(c.replace(b),a.getSelection().selectElement(c))},contents:[{id:"info",label:b.lang.forms.button.title,title:b.lang.forms.button.title,elements:[{id:"name",
type:"text",label:b.lang.common.name,"default":"",setup:function(a){this.setValue(a.data("cke-saved-name")||a.getAttribute("name")||"")},commit:d},{id:"value",type:"text",label:b.lang.forms.button.text,accessKey:"V","default":"",setup:function(a){this.setValue(a.getAttribute("value")||"")},commit:d},{id:"type",type:"select",label:b.lang.forms.button.type,"default":"button",accessKey:"T",items:[[b.lang.forms.button.typeBtn,"button"],[b.lang.forms.button.typeSbm,"submit"],[b.lang.forms.button.typeRst,
"reset"]],setup:function(a){this.setValue(a.getAttribute("type")||"")},commit:d}]}]}});*/

/**
 *  This plugin create button (such as submit, button) with properties like name, type, style, onClick etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.dialog.add( 'button', function( editor )
{
	
	function showIcons(iconPath){
		$.ajax({
			url: 'bpm/admin/getMenuIcons',
		    type: 'GET',
		    dataType: 'json',
			async: false,
			success : function(response) {
				var htmlContent = "<table  style='background-color:#DEDEDE;border:2px solid #fff;' width='100%'><tr>";
				for(var index = 0; index < response.length; index++){
					if(index % 5 == 0){
						htmlContent = htmlContent + "</tr><tr>";
					}
					htmlContent = htmlContent + "<td align='center' style='padding:5px;border:2px solid #fff;' ><img class='cursor-pointer' onClick='selectIconForBtn(\""+response[index]+"\");' src='"+response[index]+"'/></td>";
				}
				htmlContent = htmlContent + "</tr></table>";
				$("#popupDialog").html(htmlContent);
				openSelectDialog('popupDialog', 300, '选择图标');
			} 
		});
	}

	function openSelectDialog(divId, width, title){
		 $myDialog = $("#"+divId);
		 $myDialog.dialog({
	        width: width,
	        height: 'auto',
	        modal: true,
	        title: title,
	        zIndex: 100200
	      });
		 $myDialog.dialog("open");
	}


	function removeIconPath(){
		var currentDialog=CKEDITOR.dialog.getCurrent();
    	var currentStyle=currentDialog.getValueOf('info','iconPath');
    	currentDialog.setValueOf('info','iconPath', '');
	}
	
	
	function commitAttributes( element )
	{
		var val = this.getValue();
		if ( val )
		{
			element.attributes[ this.id ] = val;
			if ( this.id == 'name' )
				element.attributes[ 'data-cke-saved-name' ] = val;
		}
		else
		{
			delete element.attributes[ this.id ];
			if ( this.id == 'name' )
				delete element.attributes[ 'data-cke-saved-name' ];
		}
	}

	return {
		title : editor.lang.forms.button.title,
		minWidth : 421,
		minHeight : 256,
		onShow : function()
		{
			delete this.button;
			var element = this.getParentEditor().getSelection().getSelectedElement();
			if ( element && element.is( 'button' ) )
			{
				var type = element.getAttribute( 'type' );
				if ( type in { button:1, reset:1, submit:1 } )
				{
					this.button = element;
					this.setupContent( element );
				}
			}
		},
		onOk : function()
		{
			var editor = this.getParentEditor(),
				element = this.button,
				isInsertMode = !element;

			var fake = element ? CKEDITOR.htmlParser.fragment.fromHtml( element.getOuterHtml() ).children[ 0 ]
					: new CKEDITOR.htmlParser.element( 'button' );
			this.commitContent( fake );

			var writer = new CKEDITOR.htmlParser.basicWriter();
			fake.writeHtml( writer );
			var newElement = CKEDITOR.dom.element.createFromHtml( writer.getHtml(), editor.document );	
			$("#_btnId").val(newElement.getAttribute( 'id' )) ;
			$("#_btnName").val(newElement.getAttribute( 'name' )) ;
			$("#_btnTextValue").val(newElement.getAttribute( 'value' )) ;
        	$("#_btnFunc").val(newElement.getAttribute( 'onclick' )) ;
        	$("#_btnStyle").val(newElement.getAttribute( 'style' )) ;	
        	$("#_btnType").val(newElement.getAttribute( 'type' )) ;
        	$("#_iconPath").val(newElement.getAttribute( 'iconPath' ));
        	
        	
        	var btnName = newElement.getAttribute( 'name' );
        	if(btnName == "print" || btnName == "save" || btnName == "close"){
        		newElement.setAttribute( 'class', 'btn btn-primary normalButton ck-form-button' );
			}else{
				newElement.setAttribute( 'class', 'ck-form-button' );
			}
        	if(newElement != undefined){
        		if(newElement.getAttribute( 'id' )){
        			var clickedBtn = "";
        			$.msgBox({
        			    title: form.title.confirm,
        			    content: form.msg.addtoformbuttonconfirm,
        			    type: "prompt",
        			    inputs: [
        			             { header: form.msg.addtoformandbuttonmanager, type: "radio", name: "userDefBtn", value: "create"},
        			             { header: form.msg.updatetoformandbuttonmanager, type: "radio", name: "userDefBtn", value: "update"},
        			             { header: form.msg.addtoform, type: "radio", name: "userDefBtn", value: "", checked:"checked" }],
        			    buttons: [{ value: "Ok" }, { value: "Cancel" }],
        			    success: function (result, values) {
        			    	clickedBtn = result;
        			    	if(result == "Ok"){
        			    		var selectedVal = "";
        				    	var selected = $("input[type='radio'][name='userDefBtn']:checked");
        			    		selectedValue = selected.val();
        				        if (selectedValue == "create") {
        				        	$("#_btnId").remove() ;
        				        	$("#saveFormButton").ajaxSubmit(function(){
        				        		$('<input>').attr({
        				    			    type: 'hidden',
        				    			    id: '_btnId',
        				    			    name: 'id'
        				    			}).appendTo('#saveFormButton');
        				        	});
        				        }else if (selectedValue == "update") {
        				        	$("#saveFormButton").ajaxSubmit();
        				        }
        			    	}else{
        			    		return false;
        			    	}
        			    },
            		    afterClose : function(){
            		    	if(clickedBtn == "Ok"){
	            		    	if ( !isInsertMode ){
	            					newElement.replace( element );
	            					editor.getSelection().selectElement( newElement );
	            				}
            		    	}
            		    }
        			});
            	}else{
            		var formButtonId = "";
            		var clickedBtn = "";
            		$.msgBox({
            		    title: form.title.confirm,
            		    content: form.msg.addtoformbuttonconfirm,
            		    type: "prompt",
            		    inputs: [
            		             { header: form.msg.addtoformandbuttonmanager, type: "radio", name: "userDefBtn", value: "create"},
            		             { header: form.msg.addtoform, type: "radio", name: "userDefBtn", value: "", checked:"checked"}],
            		    buttons: [{ value: "Ok" }, { value: "Cancel" }],
            		    success: function (result, values) {
            		    	clickedBtn = result;
            		    	if(result == "Ok"){
            		    		var selectedVal = "";
            			    	var selected = $("input[type='radio'][name='userDefBtn']:checked");
            			    	selectedValue = selected.val();
            			        if (selectedValue == "create") {
            			        	$("#_btnId").remove();
            			        	$("#saveFormButton").ajaxSubmit(function(response){ 
            			        		formButtonId = response.formButton.id;
            			        		$('<input>').attr({
            			    			    type: 'hidden',
            			    			    id: '_btnId',
            			    			    name: 'id'
            			    			}).appendTo('#saveFormButton');
            			        	});
            			        }
            		    	}else{
            		    		return false;
            		    	}
            		    },
            		    afterClose : function(){
            		    	if(clickedBtn == "Ok"){
            		    		if ( isInsertMode ){
                		    		newElement.setAttribute( 'id',  formButtonId);
                    		    	editor.insertElement( newElement );
                				}else{
                					newElement.replace( element );
                					newElement.setAttribute( 'id',  formButtonId);
                					editor.getSelection().selectElement( newElement );
                				}
            		    	}
            		    }
            		    	
            		});
            	}  
        	}        	
		},
		contents : [
			{
				id : 'info',
				label : editor.lang.forms.button.title,
				title : editor.lang.forms.button.title,
				elements : [
					{
						id : 'id',
						type : 'text',
						label : 'id',
						hidden : true,
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'id' ) || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'id', this.getValue() );
							else
								element.removeAttribute( 'id' );
						}
					},{
						id : 'name',
						type : 'text',
						label : editor.lang.common.name+'<span class="color-red"> *</span>',
						'default' : '',
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
						setup : function( element )
						{
							this.setValue(element.data( 'cke-saved-name' ) ||element.getAttribute( 'name' ) || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'name', this.getValue() );
							else
								element.removeAttribute( 'name' );
						}
					},
					{
						id : 'value',
						type : 'text',
						label : editor.lang.forms.button.text+'<span class="color-red"> *</span>',
						accessKey : 'V',
						'default' : '',
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						validate : function(){
							var currentElement =  this;
							if(this.getValue() == ""){
								$.msgBox({
								    title:form.title.validation,
								    content: form.error.emptybuttonname,
								    afterClose:function(){
					    		    	currentElement.getInputElement().focus();
					    		    	$("#"+currentElement._.labelId).css('color','#FF225A');
					    		    }
								});
								return false;
							}else if((this.getValue().length) > 16){
								$.msgBox({
								    title:form.title.validation,
								    content: form.error.buttonlabelsize,
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
							this.setValue( element.getAttribute( 'value' ) || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'value', this.getValue() );
							else
								element.removeAttribute( 'value' );
						}
					},
					{
						id : 'type',
						width : '100%',
						type : 'select',
						style : 'width:420px',
						label : form.label.buttonType,
						'default' : 'button',
						accessKey : 'T',
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						items :
						[
							[ editor.lang.forms.button.typeBtn, 'button' ],
							[ editor.lang.forms.button.typeSbm, 'submit' ]
							/*[ editor.lang.forms.button.typeRst, 'reset' ]*/
						],
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'type' ) || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'type', this.getValue() );
							else
								element.removeAttribute( 'type' );
						}
					},{

						id : 'iconPath',
						type : 'text',
						label : form.label.iconPath,
						'default' : '',
						className : 'cke_disabled',
						onLoad : function()
						{
							this.getInputElement().setAttribute( 'readOnly', true );
						},
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'iconPath' ) || '' );
								
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'iconPath', this.getValue() );
							else
								element.removeAttribute( 'iconPath' );
								
						}
					
					},{
						type : 'hbox',
						widths : [ '60px', '170px' ],
						hidden : false,
						children :
						[
							{
								type : 'button',
								id : 'chooseIcon',
								label : form.label.chooseIcon,
								title : form.label.chooseIcon,
								onClick : function() {
									showIcons();
								}
							},
							{
								type : 'button',
								id : 'removeIcon',
								label : form.label.removeIcon,
								title : form.label.removeIcon,
								onClick : function() {
									removeIconPath();
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
						type : 'textarea',
						id : 'onclick',
						style : 'height:215px',
						label : form.label.onclick,
						required : false,
						rows : 2,
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'onclick' ) || element.$.dataset.ckePaOnclick || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'onclick', this.getValue() );
							else
								element.removeAttribute( 'onclick' );
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
						style : 'width:421px; height: 115px;',
						required : false,
						rows : 2,
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						setup : function( element  )
						{
							/*if(element.getAttribute( 'style' ).indexOf("background") > -1){
								var bgStyle = element.getAttribute( 'style' );
								var bglength = bgStyle.indexOf("padding-left: 25px;");
								var customStyle ="";
								if(bglength>-1){
									customStyle = bgStyle.substring(bglength+19, bgStyle.length);
								}else{
									customStyle=bgStyle;
								}
								
								this.setValue( customStyle || '' );								
							}else{*/
								this.setValue( element.getAttribute( 'style' ) || '' );
							//}
						},
						commit : function( element ){
							
							/*if ( this.getValue() )
								element.setAttribute( 'style', this.getValue() );
							else
								element.removeAttribute( 'style' );*/
						}
					},{
						id : 'class',
						type : 'text',
						label : 'class',
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'class' ) || '' );
						},
						commit : function( element )
						{
							element.setAttribute( 'class', 'ck-form-button' );
						}
					}
				]
			
			}
		]
	};
});
