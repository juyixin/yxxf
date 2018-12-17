/**
 *  This plugin show buttons and subforms.
 *  @author mahesh
 *  @author rajmohan
 */
CKEDITOR.plugins.add( 'buttonmanager', {
	init: function( editor ){
		/**
		 * Show all user defined buttons
		 */
		editor.ui.addButton( 'showbuttons', {
			label: form.title.showButton,
			command: 'showButtonsInDialog',
			icon: '/scripts/ckeditor/plugins/buttonmanager/images/button_list.png'
		});
						
		editor.addCommand( 'showButtonsInDialog', new CKEDITOR.dialogCommand( 'showbuttons' ) );
		CKEDITOR.dialog.add( 'showbuttons', function( editor ) {
			var htmlContent = "";
			return {
				title : form.title.allbuttons,
				minWidth : 400,
				minHeight : 200,
				onShow : function(){
					htmlContent = showFormButtonsInDialog(editor);
					$('#all_buttons_div').html(htmlContent);
				},
				contents :
				[{
						id : 'all_buttons',
						label : 'Settings',
						elements : [{
							type : 'html',
							align : 'left',
							html : '<span id="all_buttons_div"></span>'
						}]
					},
				],
				onOk : function(){
					var button = editor.document.createElement( 'input' );
					var btnName = $("#_previewName").val();
					var btnId = $("#_previewId").val();
					if(btnName == "print" || btnName == "save" || btnName == "close"){
						button.setAttribute( 'class', 'btn btn-primary normalButton ck-form-button' );
					}else{
						button.setAttribute( 'class', 'ck-form-button' );
					}
					button.setAttribute( 'id', $("#_previewId").val() );
					button.setAttribute( 'name', $("#_previewName").val() );
					button.setAttribute( 'value', $("#_previewTextValue").val() );
					button.setAttribute( 'onclick', $("#_previewFunc").val() );
					button.setAttribute( 'style', $("#_previewStyle").val() );
					button.setAttribute( 'type', $("#_previewType").val() );
					button.setAttribute( 'iconpath', $("#_previewIconPath").val() );
					
					if(btnName != undefined && btnId != undefined){
						//editor.insertElement( button );
						var fakeElement = "";
						var btnTyp = $("#_previewType").val();
						if(btnTyp == "submit"){
							fakeElement = editor.createFakeElement( button, 'cke_submit_btn drag', 'button', true );
						}else{
							fakeElement = editor.createFakeElement( button, 'cke_button drag', 'button', true );
						}
					
						if ( !this.text )
							editor.insertElement( fakeElement );
						else
						{
							fakeElement.replace( this.text );
							editor.getSelection().selectElement( fakeElement );
						}
					}
				}				
			};
		
		});
		
		/**
		 * Create user defined buttons
		 */
		editor.ui.addButton( 'subforms', {
			label: form.title.subForms,
			command: 'showSubFormInDialog',
			icon: '/scripts/ckeditor/plugins/buttonmanager/images/button_list.png'
		});
						
		editor.addCommand( 'showSubFormInDialog', new CKEDITOR.dialogCommand( 'subforms' ) );
		CKEDITOR.dialog.add( 'subforms', function( editor ) {
			var htmlContent = "";
			var subFormCount=0;
			var tempMA={};
			var subFormName="";
			return {
				title : form.title.subForms,
				minWidth : 400,
				minHeight : 200,
				contents :
					[
						{	
							id : 'sub_form',
							label : 'Sub Form',
							elements :
							[{
									id : 'formName',
									width : '100%',
									type : 'select',
									label : form.label.formnames,
									'default' : '',
									accessKey : 'T',
									style : 'width:200px',
									validate : CKEDITOR.dialog.validate.notEmpty( form.error.empty ),
									items : [['',0]],
									commit : function( data ) {
										data.formId = this.getValue();
									},								
							    onLoad : function(){
							    	var element_id = '#' + this.getInputElement().$.id;
							    	var tableName = $("#tableName").val();
							    	var tableId = $("#tableId").val();
									var tempValue = this.getValue();
							    	$.ajax({
							    		url: 'form/getAllChildTableFormNames?tableId='+tableId,
							    	    type: 'GET',
							    	    dataType: 'json',
							    	    cache:false,
							    		async: false,
							    		success: function(data) {
							    			$( element_id ).empty();
							    			if(data!=null){
							    				$.each(data.forms, function(index, item) {
								                    $(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item.formName, item.id);
								                    tempMA[[item.id]]=item.tableName;
													//$(element_id).val(tempValue);
								                });
								                $(element_id).prepend("<option value='' selected='selected'></option>");
							    			}
							            }
								    });	
								//	this.onChange(element_id);
							    },
							    onChange : function() {
							    	 var dialog = this.getDialog();
							    	 $("#childTableName").val(tempMA[this.getValue()]);
							    	// $(element_Id).val(this.getValue());
							    	 $.ajax({
							            type: 'GET',
							            url: '/bpm/form/getAllSubFormFields?formId='+this.getValue(),
							            dataType: 'json',
							            async: false,
							            cache: false,
							            success: function(data) {
							            	if(data){
							            		subFormCount=data.length;
								            	var formFieldHtml="<b>Form Fields:<br/></b>"
								            	for(var i=0;i<data.length;i++){
								            		if(i==0){
								            			subFormName=data[i].formName;
								            		}
								            		
								            		var tableQuery=data[i].xmlContent.replace(/"/g,"'");
								            		formFieldHtml+='<input fieldValue="'+data[i].fieldValue+'" fieldType="'+data[i].fieldType+'" fieldName="'+data[i].fieldName+'" type="checkbox" '; 
								            		formFieldHtml+=' fieldOnclick="'+data[i].onclick+'" fieldOnBlur= "'+data[i].onblur+'" fieldOnfocus="'+data[i].onfocus+'" fieldOnchange="'+data[i].onchange+'" fieldClass="'+data[i].fieldClass+'" ';
								            		formFieldHtml+=' id="field_'+i+'" tableId="'+data[i].tableId+'" datadictionary="'+data[i].datadictionary+'" columnId="'+data[i].columnId+'" fieldId="'+data[i].fieldId+'" optionTag="'+data[i].optionTags+'" value="'+tableQuery+'">&nbsp;&nbsp;&nbsp;'+data[i].fieldName+'</input><br/>'
								            	}
								            	$("#all_form_fields_div").html(formFieldHtml);
							            	}
							            }
							        });
							    }
							},{
								type : 'html',
								align : 'left',
								id:'formFieldsList',
								lable:'Form Fields',
								html : '<span id="all_form_fields_div"></span><input type="hidden" id="childTableName" name="childTableName"/>'
							}
							
							]
						}
					],
				onOk : function(){
					var parentTableName = $("#tableName").val();
					var childTableName = $("#childTableName").val();
					data = {},
					this.commitContent( data );
					
					var tempMapList=[];
					var count=0;
					for(var i=0;i<subFormCount;i++){
						var tempObj={};
						if($('#field_'+i).attr('checked')) {
							tempObj['xmlContent'] = $("#field_"+i).val();
							tempObj['fieldName'] = $("#field_"+i).attr('fieldName');
							tempObj['fieldId']=$("#field_"+i).attr('fieldId');
							tempObj['tableId']=$("#field_"+i).attr('tableId');
							tempObj['columnId']=$("#field_"+i).attr('columnId');
							tempObj['fieldValue']=$("#field_"+i).attr('fieldValue');
							tempObj['fieldType']=$("#field_"+i).attr('fieldType');
							tempObj['dataDictionary']=$("#field_"+i).attr('datadictionary');
							tempObj['optionTags']=$("#field_"+i).attr('optionTag');
							tempObj['fieldOnclick']=$("#field_"+i).attr('fieldOnclick');
							tempObj['fieldOnfocus']=$("#field_"+i).attr('fieldOnfocus');
							tempObj['fieldOnchange']=$("#field_"+i).attr('fieldOnchange');
							tempObj['fieldClass']=$("#field_"+i).attr('fieldClass');
							tempObj['fieldOnBlur']=$("#field_"+i).attr('fieldOnBlur');
							tempMapList[count]=tempObj;
							count++;
						} 
					}
					var subForm_map={};
					subForm_map['subForm_'+sub_Form_Field_List.length]=tempMapList;
					
					if(tempMapList.length!=0){
						var tempRelationMap={};
						tempRelationMap['parentTableName']=parentTableName;
						tempRelationMap['childTableName']=childTableName;
						sub_Form_Relation_List[sub_Form_Relation_List.length]=tempRelationMap;
						
						sub_Form_Field_List[sub_Form_Field_List.length]=subForm_map;
					}
					$("#all_form_fields_div").empty();
					var tempHtml= $.ajax({
				            type: 'POST',
				            url: '/bpm/form/generateSubFormHtml',
				            data: {"subFormFieldList":encodeURLStringComponent(JSON.stringify(tempMapList)), "subFormName":subFormName},
				            dataType: 'json',
				            async: false
				        }).responseText;
					editor.insertHtml( "<p>"+tempHtml+"</p>" );
				}				
			};
		
		});
	}
});
