/**
 * @license Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	//alert("ready111");

	if(!CKEDITOR.env.ie) {
		config.language = setCkEditorLanguage();
	//alert("==in=");
	config.contentsCss = ['/styles/bootstrap/main.css', '/scripts/ckeditor/contents.css', '/styles/easybpm/common/common.css', '/styles/easybpm/jqueryUI/jquery-ui-1.8.24.css'];
		config.extraPlugins = 'formchanges,buttonmanager,datetimepicker,userlist,usercontrol,departmentlist,rolelist,grouplist,numberfield,fileupload,richtextbox,gridviewcontrol,autocomplete,tabcontrol,imageuploder,onloadevent,label';
	/*config.baseFloatZIndex = '214748346';*/
	$.getScript("/scripts/ckeditor/plugins/templates/templates/default.js");
	CKEDITOR.addCss("#wizardTab-content{border:2px solid #999999;}.cke_editable{margin:0px;} .drag {position: relative;cursor: move;}");
	}
	
	

	
	
};
var $editor_instance = "";

CKEDITOR.on('instanceReady', function (ev) {
//alert("ready");

	if(!CKEDITOR.env.ie) {

	var dragstart_outside = true;
	var _elementType = "";
	ev.editor.document.on('drop', function (event) {
		if(_elementType != ""){
			if(_elementType == "label" || _elementType == "textfield" || _elementType == "textarea" || _elementType == "select" || _elementType == "radio" || _elementType == "checkbox" || _elementType == "hiddenfield" || _elementType == "button" || _elementType == "datetimepicker" || 
			   _elementType == "userlist" || _elementType == "usercontrol" || _elementType == "departmentlist" || _elementType == "rolelist" || _elementType == "grouplist" || _elementType == "numberfield" || _elementType == "fileupload" || 
			   _elementType == "showbuttons" || _elementType == "subforms" || _elementType == "templates" || _elementType == "image" || _elementType == "richtextbox" || _elementType == "gridviewcontrol" || _elementType == "autocomplete" || _elementType == "tabcontrol" || _elementType == "onloadevent"){
				CKEDITOR.instances["editor1"].openDialog(_elementType);
				_elementType = "";
	      	}
		}
		setTimeout(function(){
			var frame = $("#ckEditor").find("iframe")[0];
			$(frame).contents().find("a.form_elements").remove();
		},1);
	});
		
	$(".form_elements").mousedown(function(){
		_elementType = $(this).attr("id"); 
	});
	ev.editor.removeMenuItem('form');
	
	$editor_instance = CKEDITOR.instances[ev.editor.name];	
		includeJSFileIntoEditor('/scripts/easybpm/form/drag_and_drop_plugin.js', "file");
		includeJSFileIntoEditor('$(".drag").draggable({stop:function(event, ui) { var realElement=$(this).attr("data-cke-realelement"); var decodedElement = decodeURIComponent(realElement);  var element = $(decodedElement); var style = $(element).attr("style"); var st = ""; if(style != undefined){st=style;}var newStyle = st+";top:"+$(this).position().top+"px;left:"+$(this).position().left+"px;position:relative;"; alert("newStyle : "+newStyle); $(element).attr("style", newStyle);  var newHtmlContent = $("<div></div>").append(element).html(); alert("ele : "+newHtmlContent); $(this).attr("data-cke-realelement", encodeURIComponent(newHtmlContent)); }});', "content");
	}
});

CKEDITOR.on('dialogDefinition', function(dialogDefinitionEvent) {
	//alert("ready22111");
	if(!CKEDITOR.env.ie) {

    var dialogDefinition = dialogDefinitionEvent.data.definition;
    // Get rid of annoying confirmation dialog on cancel
    dialogDefinition.dialog.on('cancel', function(cancelEvent) {
        return false;
    }, this, null, -1);
	
	}
});

function includeJSFileIntoEditor(fileOrContent, type){
	//alert("ready122211");
	if(!CKEDITOR.env.ie) {

	var $script = document.createElement('script');	
    if(type == 'file'){
    	$script.src = fileOrContent;
    }else{
    	$script.text = fileOrContent;
    }
	$script.onload = function() {
	    try{
	    	$editor_instance.window.$.Typekit.load();
	    }catch(e){}
	};
	$editor_instance.document.getHead().$.appendChild($script);
	}
}
