
CKEDITOR.plugins.add( 'onloadevent', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'OnLoadEvent', {
			label: form.title.pluginOnloadEvent,
			command: 'showOnLoadEventDialog',
			icon: '/scripts/ckeditor/plugins/usercontrol/images/usercontrol.png'
		});
						
		editor.addCommand( 'showOnLoadEventDialog', new CKEDITOR.dialogCommand( 'onloadevent' ) );
		
		CKEDITOR.dialog.add( 'onloadevent', function( editor ){
			return {				
				title : form.title.formOnLoadEventName,
				minWidth : 350,
				minHeight : 150,
				onShow : function() {},
				onOk : function() {},
				onLoad : function() {},
				contents : [{
					id : 'info',
					label : form.label.onLoadEventName,
					title : form.label.onLoadEventName,
					elements : [{
						id : 'onLoadEvent',
						type : 'textarea',
						label : form.label.onLoadEventName,
						'default' : '',
						style : 'width:420px',
						accessKey : 'C',
						onShow : function(){
														var currentElement =  this;
														$("#"+currentElement._.labelId).css('color','#524847');
													},
						rows:5
					}]
				}]
			};
		});
	}
});
