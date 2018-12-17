/**
 *  This plugin create file upload field  with properties like name, size, type etc...
 *  The below onOk, onShow, onLoad functionality overwrite in "/scripts/ckeditor/plugins/formchanges/plugin.js" for onOk, onShow, onLoad functionality
 *  @author mahesh
 */
CKEDITOR.plugins.add( 'imageuploder', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'Uploader', {
			label: form.title.pluginLocalImageUpload,
			command: 'showImageUploadInDialog',
			icon: '/scripts/ckeditor/plugins/imageuploder/images/imageuploder.png'
		});
		
		editor.addCommand( 'showImageUploadInDialog', new CKEDITOR.dialogCommand( 'imageuploder' ) );
		
		CKEDITOR.dialog.add( 'imageuploder', function( editor )
				{
			
			var imagePath = "";
			
			var autoAttributes =
			{
				action : 1,
				id : 1,
				method : 1,
				enctype : 1,
				target : 1
			};
			

			function _uploadMailImage(){
				var fileExtension = ['jpeg', 'jpg', 'png', 'gif', 'bmp'];
				var uploadImageName=$("#formUploadImage").val();
				var contentResponse="";
				var isSuccess=false;
				if(uploadImageName!=""){
					if ($.inArray(uploadImageName.split('.').pop().toLowerCase(), fileExtension) == -1) {
						$.msgBox({
							title: form.title.message,
							content: form.msg.pluginImageFormat,
						    type: "info",
						    afterClose : function(){
						    	return false;
						    }
						});
						return false;
					}else{
						$("#formMailImageUploadForm").ajaxSubmit(function(response){ 
			        		if(response.success){
								contentResponse=response.success;
								loginUser=response.loginUser;
								isSuccess=true;
								formImageUploder(contentResponse,isSuccess,uploadImageName,loginUser);
								imagePath = window.location.protocol+"//"+window.location.host+"/resources/"+loginUser+"/"+response.fileName;
								return true;
							}else{
								return false;
							}
			        	});
						
					}
				}else{
					return false;
				}
				
			}
			
			function removeMailImagePath(){
				$.msgBox({
					title: form.title.message,
					content: form.msg.pluginImageDelete,
					type: "info",
					afterClose : function(){
					    	return false;
					    }
				});
			}
			
			return {
				title : editor.lang.forms.form.title,
				minWidth : 350,
				minHeight : 150,
				onOk : function()
				{
					this.getParentEditor().insertHtml("<img src="+imagePath+">");
				},
				contents : [
					{
						id : 'info',
						label : editor.lang.forms.form.title,
						title : editor.lang.forms.form.title,
						elements : [
							{
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
								html :  "<form  method='post' enctype='multipart/form-data' action='/form/uploadFormImage' id='formMailImageUploadForm' > "
									  + "<input style='height:25px;' size='35px' type='file' id='formUploadImage' name='file'/></form> "
							},{
								type : 'hbox',
								widths : [ '170px', '170px' ],
								hidden : false,
								children :
								[
									{
										type : 'button',
										id : 'upload',
										label : form.label.uploadImage,
										title : form.label.uploadImage,
										onClick : function() {
											_uploadMailImage();
										}
									}/*,
									{
										type : 'button',
										id : 'removeIcon',
										label : form.label.removeImage,
										title : form.label.removeImage,
										onClick : function() {
											formImage = "";
											removeMailImagePath();
										}
									}*/
								]
							
							}
						]
					
					}
				]
			};
});
		
						
	}
});
