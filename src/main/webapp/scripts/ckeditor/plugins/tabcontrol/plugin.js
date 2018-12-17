
CKEDITOR.plugins.add( 'tabcontrol', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'TabControl', {
			label: form.title.pluginTabControl,
			command: 'showTabControlDialog',
			icon: '/scripts/ckeditor/plugins/tabcontrol/images/tab-add-icon.png'
		});
						
		editor.addCommand( 'showTabControlDialog', new CKEDITOR.dialogCommand( 'tabcontrol' ) );
		
		CKEDITOR.dialog.add( 'tabcontrol', function( editor ){
			var autoAttributes ={value : 1,size : 1,maxLength : 1};
	
			var acceptedTypes ={text : 1,password : 1};
	
			return {
				
				title : form.title.tabDesign,
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
					var tabTitles = currentDialog.getValueOf('info','tab_titles');
					tabTitles = tabTitles.trim();
					if((tabTitles.charAt(tabTitles.length - 1)) == ","){
						tabTitles = tabTitles.substring(0, tabTitles.length-1);
					}
					if((tabTitles.charAt(0)) == ","){
						tabTitles = tabTitles.substring(1, tabTitles.length);
					}
					var tabArray = tabTitles.split(",");
					var wizardTab = '<div class="row-fluid"><div class="span12"><div style="padding-left:3%;padding-right:1%;"><span class="wizard-steps"><div id="wizardTabs">';
					var wizardTabContent = '<div class="row-fluid"><div class="span12"><div style="padding-left:3%;padding-right:1%;"><div id="wizardTab-content">';
					for(var index = 0; index < tabArray.length; index++){
						if(index == 0){
							wizardTab = wizardTab + '<span id="wizardTabSpan-1" class="active-step"><a href="javascript:void(0);" id="wizardTab-1" class="fontSize13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+tabArray[index]+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></span>';
							wizardTabContent = wizardTabContent + '<div id="wizardTab-content-1"  style="display:block"><p></p></div>';
						}else{
							var tabId = index + 1;
							wizardTab = wizardTab + '<span id="wizardTabSpan-'+tabId+'" class="completed-step"><a href="javascript:void(0);" id="wizardTab-'+tabId+'" class="fontSize13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+tabArray[index]+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></span>';
							wizardTabContent = wizardTabContent + '<div id="wizardTab-content-'+tabId+'" style="display:none;" ><p></p></div>';
						}
					}
					var wizardTab = wizardTab + '</div></span></div></div>';
					var wizardTabContent = wizardTabContent + '</div></div></div></div>';
					var wizardInitScript = '<script type="text/javascript">changeWizardByTabForDynamicForm();loadFirstTabAsDefault();</script>';
					var htmlContent = wizardTab+"<br/>"+wizardTabContent+""+wizardInitScript;
					editor.insertHtml( htmlContent );
					var ckEditorIframe = $('#ckEditor').find("iframe")[0];
					$(ckEditorIframe).attr('id','ckEditor_iframe');
					loadScriptFileInCKEditorIframe('ckEditor_iframe');
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
					label : form.label.tabTitle,
					title : form.label.tabTitle,
					elements : [
					    {
							type : 'html',
							align : 'left',
							html : '<span class="color-red">*</span><span class="form-title-msg">'+form.msg.tabDesign+'</span>'
						},{
						id : 'tab_titles',
						type : 'textarea',
						rows : 3,
						label : form.label.tabTitle+'<span class="color-red"> *</span>',
						onShow : function(){
							var currentElement =  this;
							$("#"+currentElement._.labelId).css('color','#524847');
						},
						validate : function(){
							var currentElement =  this;
							var message = "";
							if(this.getValue() == ""){
								message = form.error.emptyname
							}else{
								var iChars = "~`!#$%^&*+=-[]\\\';/{}|\":<>?";
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
						style : 'width:420px'
					}]
				}]
			};
		});
	}
});
