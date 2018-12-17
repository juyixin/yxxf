
CKEDITOR.plugins.add( 'usercontrol', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'DateTimePicker', {
			label: form.title.labelDateTimePicker,
			command: 'showUserControlDialog',
			icon: '/scripts/ckeditor/plugins/usercontrol/images/usercontrol.png'
		});
						
		editor.addCommand( 'showUserControlDialog', new CKEDITOR.dialogCommand( 'usercontrol' ) );
		
		CKEDITOR.dialog.add( 'usercontrol', function( editor ){
			var autoAttributes ={value : 1,size : 1,maxLength : 1};
	
			var acceptedTypes ={text : 1,password : 1};
	
			return {
				
				title : form.title.usercontrol,
				minWidth : 350,
				minHeight : 150,
				onShow : function() {
					delete this.textField;
	
					var element = this.getParentEditor().getSelection().getSelectedElement();
					if ( element && element.getName() == "input" &&( acceptedTypes[ element.getAttribute( 'type' ) ] || !element.getAttribute( 'type' ) ) ) {
						this.textField = element;
						this.setupContent( element );
					}
				},
				onOk : function() {
					var currentDialog=CKEDITOR.dialog.getCurrent();
					var userControlName = currentDialog.getValueOf('info','_cke_saved_name');
					var englishName = document.getElementById('englishName').value
				
					var fakeElement = "<span id="+englishName+"_"+userControlName+"_username class='user-control'>${username}</span>";
					editor.insertHtml( fakeElement );
				},
				onLoad : function() {
					var autoSetup = function( element ) {
						var value = element.hasAttribute( this.id ) && element.getAttribute( this.id );
						this.setValue( value || '' );
					};
	
					var autoCommit = function( data ) {
						var element = data.element;
						var value = this.getValue();
	
						if ( value )
							element.setAttribute( this.id, value );
						else
							element.removeAttribute( this.id );
					};
	
					this.foreach( function( contentObj ) {
						if ( autoAttributes[ contentObj.id ] ) {
							contentObj.setup = autoSetup;
							contentObj.commit = autoCommit;
						}
					});
				},
				contents : [{
					id : 'info',
					label : editor.lang.forms.textfield.title,
					title : editor.lang.forms.textfield.title,
					elements : [{
						id : '_cke_saved_name',
						type : 'text',
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
						style : 'height:160px',
						setup : function( element ) {
							this.setValue(
									element.data( 'cke-saved-name' ) ||
									element.getAttribute( 'name' ) ||
									'' );
						},
						commit : function( element ) {
							//var element = data.element;

							if ( this.getValue() )
								element.data( 'cke-saved-name', this.getValue() );
							else {
								element.data( 'cke-saved-name', false );
								element.removeAttribute( 'name' );
							}
						}
					}]
				}]
			};
		});
	}
});
