/*
 Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license

(function(){CKEDITOR.dialog.add("templates",function(c){function o(a,b){var k=CKEDITOR.dom.element.createFromHtml('<a href="javascript:void(0)" tabIndex="-1" role="option" ><div class="cke_tpl_item"></div></a>'),d='<table style="width:350px;" class="cke_tpl_preview" role="presentation"><tr>';a.image&&b&&(d+='<td class="cke_tpl_preview_img"><img src="'+CKEDITOR.getUrl(b+a.image)+'"'+(CKEDITOR.env.ie6Compat?' onload="this.width=this.width"':"")+' alt="" title=""></td>');d+='<td style="white-space:normal;"><span class="cke_tpl_title">'+
a.title+"</span><br/>";a.description&&(d+="<span>"+a.description+"</span>");k.getFirst().setHtml(d+"</td></tr></table>");k.on("click",function(){p(a.html)});return k}function p(a){var b=CKEDITOR.dialog.getCurrent();b.getValueOf("selectTpl","chkInsertOpt")?(c.fire("saveSnapshot"),c.setData(a,function(){b.hide();var a=c.createRange();a.moveToElementEditStart(c.editable());a.select();setTimeout(function(){c.fire("saveSnapshot")},0)})):(c.insertHtml(a),b.hide())}function i(a){var b=a.data.getTarget(),
c=g.equals(b);if(c||g.contains(b)){var d=a.data.getKeystroke(),f=g.getElementsByTag("a"),e;if(f){if(c)e=f.getItem(0);else switch(d){case 40:e=b.getNext();break;case 38:e=b.getPrevious();break;case 13:case 32:b.fire("click")}e&&(e.focus(),a.data.preventDefault())}}}var h=CKEDITOR.plugins.get("templates");CKEDITOR.document.appendStyleSheet(CKEDITOR.getUrl(h.path+"dialogs/templates.css"));var g,h="cke_tpl_list_label_"+CKEDITOR.tools.getNextNumber(),f=c.lang.templates,l=c.config;return{title:c.lang.templates.title,
minWidth:CKEDITOR.env.ie?440:400,minHeight:340,contents:[{id:"selectTpl",label:f.title,elements:[{type:"vbox",padding:5,children:[{id:"selectTplText",type:"html",html:"<span>"+f.selectPromptMsg+"</span>"},{id:"templatesList",type:"html",focus:!0,html:'<div class="cke_tpl_list" tabIndex="-1" role="listbox" aria-labelledby="'+h+'"><div class="cke_tpl_loading"><span></span></div></div><span class="cke_voice_label" id="'+h+'">'+f.options+"</span>"},{id:"chkInsertOpt",type:"checkbox",label:f.insertOption,
"default":l.templates_replaceContent}]}]}],buttons:[CKEDITOR.dialog.cancelButton],onShow:function(){var a=this.getContentElement("selectTpl","templatesList");g=a.getElement();CKEDITOR.loadTemplates(l.templates_files,function(){var b=(l.templates||"default").split(",");if(b.length){var c=g;c.setHtml("");for(var d=0,h=b.length;d<h;d++)for(var e=CKEDITOR.getTemplates(b[d]),i=e.imagesPath,e=e.templates,n=e.length,j=0;j<n;j++){var m=o(e[j],i);m.setAttribute("aria-posinset",j+1);m.setAttribute("aria-setsize",
n);c.append(m)}a.focus()}else g.setHtml('<div class="cke_tpl_empty"><span>'+f.emptyListMsg+"</span></div>")});this._.element.on("keydown",i)},onHide:function(){this._.element.removeListener("keydown",i)}}})})();*/


﻿/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function()
{
	var doc = CKEDITOR.document;

	CKEDITOR.dialog.add( 'templates', function( editor )
		{
			// Constructs the HTML view of the specified templates data.
			function renderTemplatesList( container, templatesDefinitions )
			{
				// clear loading wait text.
				container.setHtml( '' );

				for ( var i = 0, totalDefs = templatesDefinitions.length ; i < totalDefs ; i++ )
				{

					var definition = CKEDITOR.getTemplates( templatesDefinitions[ i ] );
					
						imagesPath = definition.imagesPath;
						templates = definition.templates;
						count = templates.length;

					for ( var j = 0 ; j < count ; j++ )
					{
						var template = templates[ j ];
						var item =  createTemplateItem( template, imagesPath );
						item.setAttribute( 'aria-posinset', j + 1 );
						item.setAttribute( 'aria-setsize', count );
						container.append( item );
					}
				}
			}

			function createTemplateItem( template, imagesPath )
			{
				var item = CKEDITOR.dom.element.createFromHtml(
						'<a href="javascript:void(0)" tabIndex="-1" role="option" >' +
							'<div class="cke_tpl_item"></div>' +
						'</a>' );

				// Build the inner HTML of our new item DIV.
				var html = '<table style="width:350px;" class="cke_tpl_preview" role="presentation"><tr>';

				if ( template.image && imagesPath )
					html += '<td class="cke_tpl_preview_img"><img src="' + CKEDITOR.getUrl( imagesPath + template.image ) + '"' + ( CKEDITOR.env.ie6Compat ? ' onload="this.width=this.width"' : '' ) + ' alt="" title=""></td>';

				html += '<td style="white-space:normal;"><span class="cke_tpl_title">' + template.title + '</span><br/>';

				if ( template.description )
					html += '<span>' + template.description + '</span>';

				html += '</td></tr></table>';

				item.getFirst().setHtml( html );

				item.on( 'click', function() { 
					insertTemplate( template.html,template.formStyle );
					//editor.insertHtml( template.html );
					//CKEDITOR.dialog.getCurrent().hide();
				} );

				return item;
			}

			/**
			 * Insert the specified template content into editor.
			 * @param {Number} index
			 */
			function insertTemplate( html,formStyle )
			{
				
				var dialog = CKEDITOR.dialog.getCurrent(),
					isInsert = dialog.getValueOf( 'selectTpl', 'chkInsertOpt' );

				if ( isInsert )
				{
					// Everything should happen after the document is loaded (#4073).
					editor.on( 'contentDom', function( evt )
					{
						evt.removeListener();
						dialog.hide();

						// Place the cursor at the first editable place.
						var range = new CKEDITOR.dom.range( editor.document );
						range.moveToElementEditStart( editor.document.getBody() );
						range.select( 1 );
						setTimeout( function()
						{
							editor.fire( 'saveSnapshot' );
						}, 0 );
					});

					editor.fire( 'saveSnapshot' );					
					$("#template_data").html(editor.getData());
					var formTag = $('#template_data').find("form")[0];
					$("#template_data").html("");
					destroyCKEditorForTemplate(formTag, html,formStyle);
				}
				else
				{
					editor.insertHtml( html );
					dialog.hide();
				}
			}

			function keyNavigation( evt )
			{
				var target = evt.data.getTarget(),
						onList = listContainer.equals( target );

				// Keyboard navigation for template list.
				if (  onList || listContainer.contains( target ) )
				{
					var keystroke = evt.data.getKeystroke(),
						items = listContainer.getElementsByTag( 'a' ),
						focusItem;

					if ( items )
					{
						// Focus not yet onto list items?
						if ( onList )
							focusItem = items.getItem( 0 );
						else
						{
							switch ( keystroke )
							{
								case 40 :					// ARROW-DOWN
									focusItem = target.getNext();
									break;

								case 38 :					// ARROW-UP
									focusItem = target.getPrevious();
									break;

								case 13 :					// ENTER
								case 32 :					// SPACE
									target.fire( 'click' );
							}
						}

						if ( focusItem )
						{
							focusItem.focus();
							evt.data.preventDefault();
						}
					}
				}
			}

			// Load skin at first.
			//CKEDITOR.skins.load( editor, 'templates' );
			var h=CKEDITOR.plugins.get("templates");
			CKEDITOR.document.appendStyleSheet(CKEDITOR.getUrl(h.path+"dialogs/templates.css"));


			var listContainer;

			var templateListLabelId = 'cke_tpl_list_label_' + CKEDITOR.tools.getNextNumber(),
				lang = editor.lang.templates,
				config = editor.config;
			return {
				title :editor.lang.templates.title,

				minWidth : CKEDITOR.env.ie ? 440 : 400,
				minHeight : 340,

				contents :
				[
					{
						id :'selectTpl',
						label : lang.title,
						elements :
						[
							{
								type : 'vbox',
								padding : 5,
								children :
								[
									{
										id : 'selectTplText',
										type : 'html',
										html :
											'<span>'  +
												lang.selectPromptMsg +
											'</span>'
									},
									{
										id : 'templatesList',
										type : 'html',
										focus: true,
										html :
											'<div class="cke_tpl_list" tabIndex="-1" role="listbox" aria-labelledby="' + templateListLabelId+ '">' +
												'<div class="cke_tpl_loading"><span></span></div>' +
											'</div>' +
											'<span class="cke_voice_label" id="' + templateListLabelId + '">' + lang.options+ '</span>'
									},
									{
										id : 'chkInsertOpt',
										type : 'checkbox',
										label : lang.insertOption,
										'default' : config.templates_replaceContent
									}
								]
							}
						]
					}
				],

				buttons : [ CKEDITOR.dialog.cancelButton ],

				onShow : function()
				{
					var templatesListField = this.getContentElement( 'selectTpl' , 'templatesList' );
					listContainer = templatesListField.getElement();

					CKEDITOR.loadTemplates( config.templates_files, function()
						{
						
							var templates = ( config.templates || 'default' ).split( ',' );
							
							if ( templates.length )
							{
								renderTemplatesList( listContainer, templates );
								templatesListField.focus();
							}
							else
							{
								listContainer.setHtml(
									'<div class="cke_tpl_empty">' +
										'<span>' + lang.emptyListMsg + '</span>' +
									'</div>' );
							}
						});

					this._.element.on( 'keydown', keyNavigation );
				},

				onHide : function()
				{
					this._.element.removeListener( 'keydown', keyNavigation );
				}
			};
		});
})();