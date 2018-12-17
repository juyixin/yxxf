﻿/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/*CKEDITOR.dialog.add("form",function(a){var d={action:1,id:1,method:1,enctype:1,target:1};return{title:a.lang.forms.form.title,minWidth:350,minHeight:200,onShow:function(){delete this.form;var b=this.getParentEditor().elementPath().contains("form",1);b&&(this.form=b,this.setupContent(b))},onOk:function(){var b,a=this.form,c=!a;c&&(b=this.getParentEditor(),a=b.document.createElement("form"),!CKEDITOR.env.ie&&a.append(b.document.createElement("br")));c&&b.insertElement(a);this.commitContent(a)},onLoad:function(){function a(b){this.setValue(b.getAttribute(this.id)||
"")}function e(a){this.getValue()?a.setAttribute(this.id,this.getValue()):a.removeAttribute(this.id)}this.foreach(function(c){d[c.id]&&(c.setup=a,c.commit=e)})},contents:[{id:"info",label:a.lang.forms.form.title,title:a.lang.forms.form.title,elements:[{id:"txtName",type:"text",label:a.lang.common.name,"default":"",accessKey:"N",setup:function(a){this.setValue(a.data("cke-saved-name")||a.getAttribute("name")||"")},commit:function(a){this.getValue()?a.data("cke-saved-name",this.getValue()):(a.data("cke-saved-name",
!1),a.removeAttribute("name"))}},{id:"action",type:"text",label:a.lang.forms.form.action,"default":"",accessKey:"T"},{type:"hbox",widths:["45%","55%"],children:[{id:"id",type:"text",label:a.lang.common.id,"default":"",accessKey:"I"},{id:"enctype",type:"select",label:a.lang.forms.form.encoding,style:"width:100%",accessKey:"E","default":"",items:[[""],["text/plain"],["multipart/form-data"],["application/x-www-form-urlencoded"]]}]},{type:"hbox",widths:["45%","55%"],children:[{id:"target",type:"select",
label:a.lang.common.target,style:"width:100%",accessKey:"M","default":"",items:[[a.lang.common.notSet,""],[a.lang.common.targetNew,"_blank"],[a.lang.common.targetTop,"_top"],[a.lang.common.targetSelf,"_self"],[a.lang.common.targetParent,"_parent"]]},{id:"method",type:"select",label:a.lang.forms.form.method,accessKey:"M","default":"GET",items:[["GET","get"],["POST","post"]]}]}]}]}});*/

﻿/*
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/
CKEDITOR.dialog.add( 'form', function( editor )
{
	
	function keydownHandler(evt){
		evt.stop();
		if(evt.data.getKey() == 27){
			$.msgBox({
				title: form.title.message,
				content: form.title.pluginCreateForm,
			    type: "info",
			    afterClose : function(){
			    	$(" .cke_dialog_ui_button_ok").focus()
			    }
			});
		}
	}
	
	var autoAttributes =
	{
		action : 1,
		id : 1,
		method : 1,
		enctype : 1,
		target : 1
	};
	
	function removeimagePath(){
		var currentDialog=CKEDITOR.dialog.getCurrent();
    	var currentStyle=currentDialog.getValueOf('info','imagePath');
    	currentDialog.setValueOf('info','imagePath', '');
	}

	return {
		title : editor.lang.forms.form.title,
		minWidth : 350,
		minHeight : 200,
		onShow : function()
		{
			getImageHeight();
			delete this.form;
			// for chrome fix. element is null in chrome. So, removing contenteditable attribute for getting element. 
			//CKEDITOR.instances.editor1.document.getById('form_div').removeAttribute("contenteditable");
			var element = this.getParentEditor().getSelection().getStartElement();
			var form = element && element.getAscendant( 'form', true );
			//CKEDITOR.instances.editor1.document.getById('form_div').setAttribute("contenteditable","true");
			if ( form )
			{
				this.form = form;
				this.setupContent( form );
			}
			
			//$(" .cke_dialog_ui_button_cancel").hide();
			$(" .cke_dialog_close_button").hide();
			
			var dialogElement = this._.element;
			dialogElement.on( 'keydown', keydownHandler, this );
		},
		onOk : function()
		{
			var editor,
			element = this.form,
			isInsertMode = !element;
			//alert(isInsertMode);
			if ( isInsertMode )
			{
				editor = this.getParentEditor();
				//var paraElement = editor.document.getElementsByTag("p");
				//paraElement.$[0].remove();
				element = editor.document.createElement( 'form' );
				//Append previous html while updating form
				var sourceHtmlContent = CKEDITOR.instances.editor1.getData();
				if(sourceHtmlContent != ''){
					var htmlSource = $("#htmlsource").html();
					$("#htmlsource").html(sourceHtmlContent);
					element.appendHtml($("#"+$("#formName").val()).html());
					$("#htmlsource").html(htmlSource); 
				}
				//end
				element.append( editor.document.createElement( 'br' ) );
//				setTimeout(function(){
//					CKEDITOR.instances['editor1'].destroy();
//					renderCKEditor();
//					var editor_data = CKEDITOR.instances.editor1.getData();
//					CKEDITOR.instances.editor1.setData(editor_data, function() {
//						CKEDITOR.instances.editor1.focus();
//						var height = $("#target").height();
//						var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
//						ckEditor.css('height',parseInt(height)-295);
//						$("#ckEditor").css('height',parseInt(height)-180);
//					});
//				},200);
				var htmlContent = "";
				//for(var i=0; i<25; i++){
					htmlContent = htmlContent + "<p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>";
				//}
				htmlContent = htmlContent + "<script>setDefaultValue('"+$("#formName").val()+"');</script>";
				element.$.innerHTML = "<div id='form_div' contentEditable='true'>"+htmlContent+"</div>";
				//element.$.innerHTML = "<script>setDefaultValue('"+$("#formName").val()+"');</script>";
				//editor.document.getBody().removeAttribute("contenteditable");
				editor.insertElement( element );
			}
			this.commitContent( element );
			$(" .cke_dialog_ui_button_cancel").show();
			$(" .cke_dialog_close_button").show();
			/*var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
			if(is_chrome) {
				CKEDITOR.instances.editor1.focus();
				var selection = editor.getSelection();
				var range = selection.getRanges()[0];
				var pCon = range.startContainer.getAscendant('body',true); //getAscendant('p',true);
				var newRange = new CKEDITOR.dom.range(range.document);
				newRange.moveToPosition(pCon, CKEDITOR.POSITION_BEFORE_START);
				newRange.select();
			}*/
			//CKEDITOR.instances.editor1.document.getById('form_div').setAttribute("contenteditable","true");
			
			// If not add the background image the palette option disabled
			if($("#backgroungImgPath").val() == ""){
				$("#paletteList .widget").css("position","relative");
				$("#paletteList .widget").append(
						$("<div>", {
							'id'    : "form-palette-overlay",
							'class' : "form-palette-overlay"
						})
				);
			}else{
				$("#paletteList .widget").css("position","");
				if($("#paletteList .widget").find("#form-palette-overlay").length > 0){
					$("#paletteList .widget").find("#form-palette-overlay").remove();
				}
			}

		},
		onLoad : function()
		{
			function autoSetup( element )
			{
				this.setValue( element.getAttribute( this.id ) || '' );
			}

			function autoCommit( element )
			{
				if ( this.getValue() )
					element.setAttribute( this.id, this.getValue() );
				else
					element.removeAttribute( this.id );
			}

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
				label : editor.lang.forms.form.title,
				title : editor.lang.forms.form.title,
				elements : [
					{
						id : 'txtName',
						type : 'text',
						style : 'width:420px',
						label : editor.lang.common.name,
						'default' : $("#formName").val(),
						className : 'cke_disabled',
						onLoad : function()
						{
							this.getInputElement().setAttribute( 'readOnly', true );
						},
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'name' ) ||
									'' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'name', this.getValue() );
						}
					},
					{
						id : 'action',
						type : 'text',
						label : editor.lang.forms.form.action,
						'default' : '#'
					},
					{
						type : 'hbox',
						widths : [ '45%', '55%' ],
						children :
						[
							{
								id : 'id',
								type : 'text',
								className : 'cke_disabled',
								onLoad : function()
								{
									this.getInputElement().setAttribute( 'readOnly', true );
								},
								label : editor.lang.common.id,
								'default' : $("#formName").val()
							},
							{
								id : 'enctype',
								type : 'select',
								label : editor.lang.forms.form.encoding,
								style : 'width:100%',
								'default' : '',
								items :
								[
									[ '' ],
									[ 'text/plain' ],
									[ 'multipart/form-data' ],
									[ 'application/x-www-form-urlencoded' ]
								]
							}
						]
					},{
						id : 'imagePath',
						type : 'text',
						label : form.label.uploadedImagePath,
						'default' : '',
						className : 'cke_disabled',
						hidden:true,
						onLoad : function()
						{
							this.getInputElement().setAttribute( 'readOnly', true );
						},
						setup : function( element )
						{
								this.setValue(element.getAttribute( 'imagePath' ) || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'imagePath', this.getValue() );
							else
								element.removeAttribute( 'imagePath' );
								
						}
					},{
						type : 'html',
						align : 'left',
						html :  "<form  method='post' enctype='multipart/form-data' action='/form/uploadFormImage' id='formImageUploadForm' > "
							  + "<input style='height:25px;' size='35px' type='file' id='formUploadImage' name='file'/></form> "
					},{
						type : 'hbox',
						widths : ['90px', '150px' ],
						hidden : false,
						children :
						[
							{
								type : 'button',
								id : 'upload',
								label : form.label.uploadImage,
								title : form.label.uploadImage,
								className : 'btn btn-primary mar-L80',
								onClick : function() {
									 var ajaxLoaderFrame = new ajaxLoader(document.getElementById('ajax_loader'));
									_uploadFormBGImage();
									if (ajaxLoaderFrame) ajaxLoaderFrame.remove();
								}
							},
							{
								type : 'button',
								id : 'removeIcon',
								label : form.label.removeImage,
								title : form.label.removeImage,
								className : 'btn btn-primary',
								onClick : function() {
									$("#backgroungImgPath").val("");
									formImage = "";
									removeFormImage=true;
									$("#formUploadImage").val("");
									removeimagePath();
								}
							}
						]
					
					},{
						id : 'method',
						type : 'text',
						label : editor.lang.forms.form.method,
						style : 'width:420px',
						'default' : 'post',
						hidden: true
					},
					{
						type : 'textarea',
						id : 'style',
						label : 'Style',
						style : 'width:420px',
						required : false,
						hidden : true,
						'default' : 'min-height:300px', 
						rows : 2,
						setup : function( element  )
						{	
							this.setValue(element.getAttribute( 'style' ) || '' );
						},
						commit : function( element ){
							var height = $(".cke_wysiwyg_frame").parent().height();
							var winWidth = $(window).width() / 1.4;	
							//Previous style
							var previousStyle = element.getAttribute( 'style' );
							
							var currentStyle = "min-height:"+height+"px;width:"+winWidth+"px";
							var imageUploaded="";
							if(formImage!=""){
								var imageUploaded=window.location.protocol+"//"+window.location.host+"/resources/"+formUserLogin+"/"+formImage;
								currentStyle=currentStyle+";height:"+formImageHeight+"px;background-image:url("+imageUploaded+");background-repeat: no-repeat;";
								$("#backgroungImgPath").val(imageUploaded);
							}else if(previousStyle!=null && previousStyle!="" && previousStyle!="null" && !removeFormImage){
								currentStyle=previousStyle;
							}
							
							if ( currentStyle )
								setTimeout(function() {
									element.setAttribute( 'style', currentStyle );
					    		}, 500);
							else
								element.removeAttribute( 'style' );
						}
					
					},
					{

						id : 'table',
						type : 'text',
						label : form.label.table,
						'default' : $("#tableId").val(),
						hidden : true,
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'table' ) ||
									'' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'table', this.getValue() );
						}
					
					}
				]
			},{
				id: 'info2',
				label: 'Event',
				title: 'Event',
				elements: [
						{  
							id : 'onsubmit',
							type : 'textarea',
							label : 'Javascript Event : On Submit',
							'default' : '',
							style : 'width:420px',
							rows:2,	
							setup : function( element )
							{
								this.setValue(element.getAttribute( 'data-cke-pa-onsubmit' ) || element.getAttribute( 'onsubmit' ) ||	'' );
							},
							commit : function( element )
							{
								
								if ( this.getValue() != "" ) {
									element.setAttribute( 'data-cke-pa-onsubmit', this.getValue() );

								} else {
									element.removeAttribute( 'onsubmit' );

								}
							}
						},{  
							id : 'onload',
							type : 'textarea',
							label : 'Javascript Event : On Load',
							'default' : '',
							style : 'width:420px',
							rows:2,	
							setup : function( element )
							{
								this.setValue(element.getAttribute( 'data-cke-pa-onload' ) || element.getAttribute( 'onload' ) ||	'' );
							},
							commit : function( element )
							{
								if ( this.getValue() != "") {
									element.setAttribute( 'data-cke-pa-onload', this.getValue() );
								} else {
									element.removeAttribute( 'onload' );
								}
							}
						},
							{  
							id : 'javaEventOnSubmit',
							type : 'textarea',
							label : 'Java Event : On Submit',
							'default' : '',
							style : 'width:420px',
							rows:2,	
							setup : function( element )
							{
								this.setValue(element.getAttribute( 'javaEventOnSubmit' ) ||	'' );
							},
							commit : function( element )
							{
								if ( this.getValue() != "")
									element.setAttribute( 'javaEventOnSubmit', this.getValue() );
								else
									element.removeAttribute( 'javaEventOnSubmit' );

							}
						},
						{  
							id : 'javaEventOnLoad',
							type : 'textarea',
							label : 'Java Event : On Load',
							'default' : '',
							style : 'width:420px',
							rows:2,	
							setup : function( element )
							{
								this.setValue(element.getAttribute( 'javaEventOnLoad' ) ||	'' );
							},
							commit : function( element )
							{
								if ( this.getValue() != "" )
									element.setAttribute( 'javaEventOnLoad', this.getValue() );
								else
									element.removeAttribute( 'javaEventOnLoad' );
							}
						}/*,
						{  
						id : 'onunload',
						type : 'textarea',
						label : form.label.onUnLoad,
						'default' : '',
						style : 'width:420px',
						accessKey : 'C',
						rows:2,	
						setup : function( element )
						{
							this.setValue(element.getAttribute( 'data-cke-pa-onunload' ) || element.getAttribute( 'onunload' ) ||	'' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'onunload', this.getValue() );
							else
								element.removeAttribute( 'onunload' );
						}
					}*/
				]
			
			}
		]
	};
});
