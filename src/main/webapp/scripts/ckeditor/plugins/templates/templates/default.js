/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license

CKEDITOR.addTemplates("default",{imagesPath:CKEDITOR.getUrl(CKEDITOR.plugins.getPath("templates")+"templates/images/"),templates:[{title:"Image and Title",image:"template1.gif",description:"One main image with a title and text that surround the image.",html:'<h3><img style="margin-right: 10px" height="100" width="100" align="left"/>Type the title here</h3><p>Type the text here</p>'},{title:"Strange Template",image:"template2.gif",description:"A template that defines two colums, each one with a title, and some text.",
html:'<table cellspacing="0" cellpadding="0" style="width:100%" border="0"><tr><td style="width:50%"><h3>Title 1</h3></td><td></td><td style="width:50%"><h3>Title 2</h3></td></tr><tr><td>Text 1</td><td></td><td>Text 2</td></tr></table><p>More text goes here.</p>'},{title:"Text and Table",image:"template3.gif",description:"A title with some text and a table.",html:'<div style="width: 80%"><h3>Title goes here</h3><table style="width:150px;float: right" cellspacing="0" cellpadding="0" border="1"><caption style="border:solid 1px black"><strong>Table title</strong></caption></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr></table><p>Type the text here</p></div>'}]});*/



﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

var newTemplates=[
{title:form.msg.pluginTemplateImage,image:'template1.gif',description:form.msg.pluginTemplateDescription,html:'<h3><img style="margin-right: 10px" height="100" width="100" align="left"/>Type the title here</h3><p>Type the text here</p>'},

{title:form.title.pluginTemplateStrange,image:'template2.gif',description:form.msg.pluginTemplateTextDescription,html:'<table cellspacing="0" cellpadding="0" style="width:100%" border="0"><tr><td style="width:50%"><h3>Title 1</h3></td><td></td><td style="width:50%"><h3>Title 2</h3></td></tr><tr><td>Text 1</td><td></td><td>Text 2</td></tr></table><p>More text goes here.</p>'},

{title:form.title.pluginTemplateText,image:'template3.gif',description:form.msg.pluginTemplateTextAndTable,html:'<div style="width: 80%"><h3>Title goes here</h3><table style="width:150px;float: right" cellspacing="0" cellpadding="0" border="1"><caption style="border:solid 1px black"><strong>Table title</strong></caption></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr></table><p>Type the text here</p></div>'}
];

$.ajax({
	url: 'form/getAllTemplateForms',
    type: 'GET',
    dataType: 'json',
	async: false,
	cache:false,
	success: function(data) {
	if(data != null){
		var forms=data.forms;
		var defaultLength=newTemplates.length;
		for(var i=0;i<forms.length;i++){
			var mapObj = {};
			var formName= forms[i].formName;
			var description = forms[i].description;
			//to get the content inside the form tag
			var templateContent="<div>"+forms[i].htmlSource+"</div>";
			var htmlObject = $(templateContent);
			
			var formStyle="";
			$(htmlObject).find('form').each(function(){
				formStyle=$(this).attr('style');
			});
			
			$(htmlObject).find('input').each(function(){
					$(this).attr('column','');
	                $(this).attr('table','');
				});
			 
		    $(htmlObject).find('textarea').each(function(){
					$(this).attr('column','');
		               $(this).attr('table','');
				});
		    $(htmlObject).find('select').each(function(){
					$(this).attr('column','');
		            $(this).attr('table','');
				});    
			
			templateContent=htmlObject.find("#"+formName).html();
			
			
			  mapObj['title'] =formName;
              mapObj['image'] = 'template1.gif';
              mapObj['description'] = description;
              mapObj['html'] = templateContent; 
              mapObj['formStyle'] = formStyle; 
              newTemplates[defaultLength+i]= mapObj;
    	}
	}
	}
});	

CKEDITOR.addTemplates('default',{imagesPath:CKEDITOR.getUrl(CKEDITOR.plugins.getPath('templates')+'templates/images/'),templates:newTemplates});
