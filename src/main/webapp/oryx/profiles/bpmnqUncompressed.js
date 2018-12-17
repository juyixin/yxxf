/**
 * Copyright (c) 2008-2009, Steffen Ryll
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.QueryEvaluator = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
		this.active 		= false;
		this.raisedEventIds = [];
		
        this.facade.offer({
            'name': ORYX.I18N.QueryEvaluator.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.QueryEvaluator.group,
            'icon': ORYX.PATH + "images/xforms_export.png",
            'description': ORYX.I18N.QueryEvaluator.desc,
            'index': 0,
			'toggle': true,
            'minShape': 0,
            'maxShape': 0
        });
		
    },
    
	showOverlay: function(button, pressed){

		if (!pressed) {
			
/*			this.raisedEventIds.each(function(id){
				this.facade.raiseEvent({
						type: 	ORYX.CONFIG.EVENT_OVERLAY_HIDE,
						id: 	id
					});
			}.bind(this))
*/
			this.raisedEventIds = [];
			this.active 		= !this.active;
			
			return;
		} 
		
		var options = {
			command : 'undef'
		}
		
		var optionsPopup = new Ext.Window({
			layout      : 'fit',
            width       : 500,
            height      : 350,
            closable	: true,
            plain       : true,
			modal		: true,
			id			: 'optionsPopup',
			
			buttons: [{
				text	: 'Submit',
				handler	: function(){
					// set options
					options = formPanel.getForm().getValues(false);
					
					optionsPopup.close();
					this.issueQuery(options);
				}.bind(this)
			}, {
                text     : 'Abort',
                handler  : function(){
                    optionsPopup.close();
                }.bind(this)
            }]

		})
		
		var modelIdField = new Ext.form.TextField({
			fieldLabel	: 'Model ID',
			name		: 'modelID',
			grow		: true,
//			hideLabel	: true
		});
		modelIdField.hide();
		
		var checkListener = function(field, checked){
			// keep checked states of all relevant fields in this array 
			if (!this.fieldStates) {
				this.fieldStates = [];
			}
			var found = false;
			var mustShowField = false;
			var i, f;
			for (i = 0; i < this.fieldStates.length; i++){
				f =  this.fieldStates[i];
				// update field entry
				if (f.field === field) {
					found = true;
					f.checked = checked;
				}
				// and at the same time check whether at least one field is checked
				mustShowField = mustShowField || f.checked;
			}
			if (!found) {
				// was not used before, so add to array
				this.fieldStates.push({
					field	: field,
					checked	: checked
				});
				mustShowField = true;
			}
			// change field visibility, if necessary
			if (mustShowField){
				modelIdField.show();
			} else {
				modelIdField.hide();
			}
		}
		
		var formPanel = new Ext.form.FormPanel({
			frame		: true,
			title		: 'Query options',
			bodyStyle	: 'padding:0 10px 0;',
			items		: [{
				// create a radio button group
				xtype		: 'fieldset',
            	autoHeight	: true,
				columns		: 1,
				allowBlank	: false,
				defaultType	: 'radio',
				items		: [
                    {
						boxLabel	: 'Process query', 
						fieldLabel	: 'Query Type', 
						name		: 'command', 
						inputValue	: 'processQuery', 
						checked: true},
					{
					// this is edited by Ahmed Awad on 28.07.09 to reflect compliance checking in the Oryx editor
						boxLabel	: 'Process Compliance Query', 
						labelSeparator: '', 
						name		: 'command', 
						inputValue	: 'processComplianceQuery', 
						//listeners	: {
						//	'check': checkListener.bind(this)
						//} 
					},
                    {
						boxLabel	: 'Run query against specific model', 
						labelSeparator: '',
						name		: 'command',
						inputValue	: 'runQueryAgainstModel',
						listeners	: {
							'check': checkListener.bind(this)
						}
					},
					{
						boxLabel	: 'Run compliance query against specific model', 
						labelSeparator: '',
						name		: 'command',
						inputValue	: 'runComplianceQueryAgainstModel',
						listeners	: {
							'check': checkListener.bind(this)
						}
					},
 //                   {
//						boxLabel	: 'Process MultiQuery', 
//						labelSeparator: '', 
//						name		: 'command', 
//						inputValue	: 'processMultiQuery'},
					{
						xtype		: 'checkbox',
						fieldLabel	: 'Stop after first match in a model was found',
						name		: 'stopAtFirstMatch',
						checked		: true,
					}
                ]
			}]
		});
		formPanel.add(modelIdField);
		
		optionsPopup.add(formPanel);
		optionsPopup.show();
		
		button.toggle();
	},
	
	issueQuery : function(options){

		try {
			var serialized_rdf = this.getRDFFromDOM();
//			serialized_rdf = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + serialized_rdf;

			this.facade.raiseEvent({
	            type: ORYX.CONFIG.EVENT_LOADING_ENABLE,
				text: "Processing query"  //ORYX.I18N.Save.saving
	        });
			// Send the request to the server.
			new Ajax.Request(ORYX.CONFIG.QUERYEVAL_URL, {
				method: 'POST',
				asynchronous: true,
				parameters: {
					resource	: location.href,
					command		: options.command,
					modelID		: options.modelID,
					stopAtFirstMatch: options.stopAtFirstMatch,
					data		: serialized_rdf
				},
                onSuccess: function(response){
                    this.facade.raiseEvent({
						type:ORYX.CONFIG.EVENT_LOADING_DISABLE
					});
					
					var respXML = response.responseXML;
                    var root = respXML.firstChild;
                    var processList = new Array();
                    var nodecnt, graph;
                    var pchildren = root.getElementsByTagName("ProcessGraph");
                    
                    // puts all matching process models into array processList with model ID 
                    // and model elements
					for (nodecnt = 0; nodecnt < pchildren.length; nodecnt++) {
                        graph = pchildren.item(nodecnt);
                        var graphID = graph.getAttributeNode("modelID").nodeValue;
                        
                        processList.push({
							id 			: graphID,
							elements 	: this.processResultGraph(graph),
							metadata    : '',
							description	: this.processMatchDescription(graph) //Ahmed Awad on 28.07.09
						});
                        
                    }
					try {
						this.processProcessList(processList);
					} catch (error) {
						Ext.Msg.alert(ORYX.I18N.Oryx.title, error);
					}
                }.bind(this),
				
				onFailure: function(response){
					this.facade.raiseEvent({
						type:ORYX.CONFIG.EVENT_LOADING_DISABLE
					});
					Ext.Msg.alert(ORYX.I18N.Oryx.title, "Server encountered an error (" + response.statusText + ").\n"
						+ response.responseText);
				}.bind(this)
			});
			
		} catch (error){
			this.facade.raiseEvent({type:ORYX.CONFIG.EVENT_LOADING_DISABLE});
			Ext.Msg.alert(ORYX.I18N.Oryx.title, error);
	 	}

	},
	
    processResultGraph: function(xmlNode){
        var graphElements = new Array();
		
		for (var k = 0; k < xmlNode.childNodes.length; k++) {
            var node = xmlNode.childNodes.item(k);
            if (!(node instanceof Text)) {
                if (node.hasAttribute("id")) { // it is a node
					graphElements.push({
						nodeType : node.nodeName,
						nodeId : node.getAttributeNode("id").nodeValue
					});
					
				} else if ((node.hasAttribute("from"))
						&& node.hasAttribute("to")) { // it is an edge
					graphElements.push({
						edgeType : node.nodeName,
						from : node.getAttributeNode("from").nodeValue,
						to : node.getAttributeNode("to").nodeValue
					});
				}
            }
        }
		return graphElements;
    },
	// Added by Ahmed Awad on 28.07.09 to extract the diagnosis and the match meta data
	processMatchDescription: function(xmlNode){
        var metadata = new Array();
		
		for (var k = 0; k < xmlNode.childNodes.length; k++) {
            var node = xmlNode.childNodes.item(k);
            if ((node.nodeName === "diagnosis")) {
                
					metadata.push({
						diagnosis : node.textContent
					});
					
				} else if ((node.nodeName === "match")) { // it is an edge
					metadata.push({
						match : node.textContent
					});
				}
            
        }
		return metadata;
    },
	/**
	 * 
	 * @param {Array} processList; 
	 * 		elements' fields: id location identifier for process
	 * 						  elements array of graph nodes/edges
	 */
	processProcessList: function(processList){
		if(processList.length == 0) {
			Ext.Msg.alert(ORYX.I18N.Oryx.title, "Found no matching processes!");
			return;
		}
		
		this.isRendering = true;
		
		// load process model meta data
		processList.each(this.getModelMetaData.bind(this));
		
		// transform array of objects into array of arrays
		var data = [];
		processList.each(function( pair ){
/*			var stencilset = pair.value.type;
			// Try to display stencilset title instead of uri
			this.facade.modelCache.getModelTypes().each(function(type){
				if (stencilset == type.namespace) {
					stencilset = type.title;
					return;
				}
			}.bind(this));
*/			
			data.push( [ pair.id, pair.metadata.thumbnailUri + "?" + Math.random(), unescape(pair.metadata.title), pair.metadata.type, pair.metadata.author, pair.elements, pair.description ] )  // Modified by Ahmed
		}.bind(this));

		
		// following is mostly UI logic
		var myProcsPopup = new Ext.Window({
			layout      : 'fit',
            width       : 500,
            height      : 300,
            closable	: true,
            plain       : true,
			modal		: true,
			autoScroll  : true, // Added by Ahmed Awad on 30.07.2009
			title       : 'Query Result',
			id			: 'procResPopup',
			
			buttons: [{
                text     : 'Close',
                handler  : function(){
                    myProcsPopup.close();
                }.bind(this)
            }]

		});
		
		var tableModel = new Ext.data.SimpleStore({
			fields: [
				{name: 'id'}, //, type: 'string', mapping: 'metadata.id'},
				{name: 'icon'}, //, mapping: 'metadata.icon'},
				{name: 'title'}, //, mapping: 'metadata.title'},
				{name: 'type'}, //, mapping: 'metadata.type'},
				{name: 'author'}, //, mapping: 'metadata.author'},
				{name: 'elements'}, //, type: 'array', mapping: 'elements'},
				{name: 'description'}, // Added by Ahmed Awad
			],
			data : data
		});
		
		var iconPanel = new Ext.Panel({
			border	: false,
			autoScroll : true, // Added by Ahmed Awad
	        items	: new this.dataGridPanel({
				store       : tableModel, 
				listeners   :{
					dblclick:this._onDblClick.bind(this)
				}
			})
	    });
		this.setPanelStyle();
		
		myProcsPopup.add(iconPanel);
		
		this.isRendering = false;
		
		myProcsPopup.show();
	},
	
	getModelMetaData : function(processEntry) {
		var metaUri = processEntry.id.replace(/\/rdf$/, '/meta');
		new Ajax.Request(metaUri, 
			 {
				method			: "get",
				asynchronous 	: false,
				onSuccess		: function(transport) {
					processEntry.metadata = transport.responseText.evalJSON();
				}.bind(this),
				onFailure		: function() {
					Ext.MessageBox.alert(ORYX.I18N.Oryx.title, "Error loading model meta data.");
				}.bind(this)
			});
		
	},
	
	_onDblClick: function(dataGrid, index, node, e){
		
		// Select the new range
		dataGrid.selectRange(index, index);

		// Get uri and matched element data from the clicked model
		var modelId 	= dataGrid.getRecord( node ).data.id;
		var matchedElements = dataGrid.getRecord( node ).data.elements;
		var description = dataGrid.getRecord( node ).data.description; // Added by Ahmed Awad on 30.07.09
		// convert object to JSOn representation
		var elementsAsJson = Ext.encode(matchedElements);
		var descriptionAsJson = Ext.encode(description); // Added by Ahmed Awad on 30.07.09
		// escape JSON string to become URI-compliant
		var encodedJson = encodeURIComponent(elementsAsJson);
		var encodedDescription = encodeURIComponent(descriptionAsJson);
		
		// remove the last URI segment, append editor's 'self' and json of model elements
		var slashPos = modelId.lastIndexOf("/");
		//var uri	= modelId.substr(0, slashPos) + "/self" + "?matches=" + encodedJson;
 	    var uri	= modelId.substr(0, slashPos) + "/self" + "?matches=" + encodedJson+"&description="+encodedDescription;
		// Open the model in Editor
		var editor = window.open( uri );
		window.setTimeout(
	        function() {
                if(!editor || !editor.opener || editor.closed) {
                        Ext.MessageBox.alert(ORYX.I18N.Oryx.title, ORYX.I18N.Oryx.editorOpenTimeout).setIcon(Ext.MessageBox.QUESTION);
                }
	        }, 5000);			
		
	},
	
	dataGridPanel : Ext.extend(Ext.DataView, {
		multiSelect		: true,
		//simpleSelect	: true, 
	    cls				: 'iconview',
	    itemSelector	: 'dd',
	    overClass		: 'over',
		selectedClass	: 'selected',
	    tpl : new Ext.XTemplate(
        '<div>',
			'<dl class="repository_iconview">',
	            '<tpl for=".">',
					'<dd >',
					'<div class="image">',
					 '<img src="{icon}" title="{title}" /></div>',
		            '<div><span class="title" title="{[ values.title.length + (values.type.length*0.8) > 30 ? values.title : "" ]}" >{[ values.title.truncate(30 - (values.type.length*0.8)) ]}</span><span class="author" unselectable="on">({type})</span></div>',
		            '<div><span class="type">{author}</span></div>',
					'</dd>',
	            '</tpl>',
			'</dl>',
        '</div>'
	    )
	}), 
	
	setPanelStyle : function() {
		var styleRules = '\
.repository_iconview dd{\
	width		: 200px;\
	height		: 105px;\
	padding		: 10px;\
	border		: 1px solid #EEEEEE;\
	font-family	: tahoma,arial,sans-serif;\
	font-size	: 9px;\
	display		: block;\
	margin		: 5px;\
	text-align	: left;\
	float		: left;\
}\
.repository_iconview dl {\
	width		: 100%;\
	max-width	: 1000px;\
}\
.repository_iconview dd.over{\
	background-color	: #fff5e1;\
}\
.repository_iconview dd.selected{\
	border-color: #FC8B03;\
}\
.repository_iconview dd img{\
	max-width	: 190px;\
	max-height	: 70px;\
}\
.repository_iconview dd .image{\
	width	: 200px;\
	height	: 80px;\
	padding-bottom	: 10px;\
	text-align		: center;\
	vertical-align	: middle;\
	display	:table-cell;\
}\
.repository_iconview dd .title{\
	font-weight	: bold;\
	font-size	: 11px;\
	color		: #555555;\
}\
.repository_iconview dd .author{\
	margin-left	: 5px;\
}';
		Ext.util.CSS.createStyleSheet(styleRules, 'queryResultStyle');
	},
    
});
/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the Global-scope presence pattern according to my thesis*/

if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.GlobalPresencePattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.GlobalPresencePattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.GlobalPresencePattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/GlobalPresencePattern.png",
            'description': ORYX.I18N.GlobalPresencePattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
   },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			//alert("Not Pressed");
			return true;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createGlobalPresencePattern(options);
		button.toggle();
    },
    createGlobalPresencePattern:function(options){
    	//alert("Create Global Presence Pattern");
    	try
    	{
       		var posx=-30;
       		var from = this.drawStartEvent(posx);
       		posx = posx+150;
       		var to = this.drawActivity(posx);
       		this.drawPath(from, to);
       	}
       	catch(error)
       	{
       		Ext.Msg.alert(ORYX.I18N.Oryx.title, error);
       	}
       	
    },
    drawStartEvent:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        //alert("ssn is "+ssn);
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "StartEvent");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","A");
        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
//		alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Leads to");
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the Global-scope presence pattern according to my thesis*/

if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.GlobalAbsencePattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.GlobalAbsencePattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.GlobalAbsencePattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/GlobalAbsencePattern.png",
            'description': ORYX.I18N.GlobalAbsencePattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
   },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			//alert("Not Pressed");
			return true;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createGlobalAbsencePattern(options);
		button.toggle();
    },
    createGlobalAbsencePattern:function(options){
    	//alert("Create Global Presence Pattern");
    	try
    	{
       		var posx=-30;
       		var from = this.drawStartEvent(posx);
       		posx = posx+150;
       		var to = this.drawEndEvent(posx);
       		this.drawPath(from, to);
       	}
       	catch(error)
       	{
       		Ext.Msg.alert(ORYX.I18N.Oryx.title, error);
       	}
       	
    },
    drawStartEvent:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        //alert("ssn is "+ssn);
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "StartEvent");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawEndEvent:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "EndEvent");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        
        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
//		alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Leads to");
		newSequenceFlow.setProperty("oryx-exclude","Some Activity");
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the After-scope presence pattern according to my thesis*/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.BeforePresencePattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    // processGraph:undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.BeforePresencePattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.BeforePresencePattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/PrecedencePattern.png",
            'description': ORYX.I18N.BeforePresencePattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
				
        
		
    },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			
			return;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createPrecedencePattern(options);
	},
    createPrecedencePattern:function(options){
    	
    	var posx=-30;
       	var from = this.drawStartActivity(posx);
       	posx = posx+150;
       	var to = this.drawEndActivity(posx);
       	this.drawPath(from, to);
    },
    drawStartActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","A");
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawEndActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","B");
        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
		//alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Precedes");
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the After-scope presence pattern according to my thesis*/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.BeforeAbsencePattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    // processGraph:undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.BeforeAbsencePattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.BeforeAbsencePattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/BeforeAbsencePattern.png",
            'description': ORYX.I18N.BeforeAbsencePattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
				
        
		
    },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			
			return;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createBeforeAbsencePattern(options);
	},
    createBeforeAbsencePattern:function(options){
    	
    	var posx=-30;
       	var from = this.drawStartEvent(posx);
       	posx = posx+150;
       	var to = this.drawEndActivity(posx);
       	this.drawPath(from, to);
    },
    drawStartEvent:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "StartEvent");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawEndActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","A");
        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
		//alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Precedes");
		newSequenceFlow.setProperty("oryx-exclude","Some Activity");
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the After-scope presence pattern according to my thesis*/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.AfterPresencePattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    // processGraph:undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.AfterPresencePattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.AfterPresencePattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/ResponsePattern.png",
            'description': ORYX.I18N.AfterPresencePattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
				
        
		
    },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			
			return;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createResponsePattern(options);
	},
    createResponsePattern:function(options){
    	
    	var posx=-30;
       	var from = this.drawStartActivity(posx);
       	posx = posx+150;
       	var to = this.drawEndActivity(posx);
       	this.drawPath(from, to);
    },
    drawStartActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","A");
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawEndActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","B");
        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
		//alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Leads to");
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the After-scope presence pattern according to my thesis*/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.AfterAbsencePattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    // processGraph:undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.AfterAbsencePattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.AfterAbsencePattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/AfterAbsencePattern.png",
            'description': ORYX.I18N.AfterAbsencePattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
				
        
		
    },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			
			return;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createAfterAbsencePattern(options);
	},
    createAfterAbsencePattern:function(options){
    	
    	var posx=-30;
       	var from = this.drawStartActivity(posx);
       	posx = posx+150;
       	var to = this.drawEndEvent(posx);
       	this.drawPath(from, to);
    },
    drawStartActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","A");
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawEndEvent:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "EndEvent");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);

        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
		//alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Leads to");
		newSequenceFlow.setProperty("oryx-exclude","Some Activity");	
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the After-scope presence pattern according to my thesis*/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.BetweenAbsenceTypeIPattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    // processGraph:undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.BetweenAbsenceTypeIPattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.BetweenAbsenceTypeIPattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/BetweenAbsenceTypeIPattern.png",
            'description': ORYX.I18N.BetweenAbsenceTypeIPattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
				
        
		
    },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			
			return;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createBetweenAbsenceTypeIPattern(options);
	},
    createBetweenAbsenceTypeIPattern:function(options){
    	
    	var posx=-30;
       	var from = this.drawStartActivity(posx);
       	posx = posx+150;
       	var to = this.drawEndActivity(posx);
       	this.drawPath(from, to);
    },
    drawStartActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","A");
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawEndActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","B");
        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
		//alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Leads to");
		newSequenceFlow.setProperty("oryx-exclude","Some Activity");
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2010 Ahmed Awad

 * 
 * WARNING THIS IS ONLY TO PROVE A CONCEPT!!!! NOT TO BE USED IN PRODUCTION
 * ENVIRONMENT!!!!
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 /* This plugin is meant to create an instance of the After-scope presence pattern according to my thesis*/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.BetweenAbsenceTypeIIPattern = ORYX.Plugins.AbstractPlugin.extend({

    facade: undefined,
    
    // processGraph:undefined,
    
    construct: function(facade){
		
        this.facade = facade;
        
        this.active 		= false;
        this.raisedEventIds = [];
		
		this.facade.offer({
            'name': ORYX.I18N.BetweenAbsenceTypeIIPattern.name,
            'functionality': this.showOverlay.bind(this),
            'group': ORYX.I18N.BetweenAbsenceTypeIIPattern.group,
            'dropDownGroupIcon' : ORYX.PATH +  "images/controlFlow.png",
            'icon': ORYX.PATH + "images/BetweenAbsenceTypeIIPattern.png",
            'description': ORYX.I18N.BetweenAbsenceTypeIIPattern.desc,
            'index': 0,
            'toggle': true,
			'minShape': 0,
            'maxShape': 0
        });
				
        
		
    },
    showOverlay: function(button, pressed){

		if (!pressed) {
			
			this.raisedEventIds = [];
			this.active 		= !this.active;
			
			return;
		} 
		
		var options = {
			command : 'undef'
		}
	
		this.createBetweenAbsenceTypeIIPattern(options);
	},
    createBetweenAbsenceTypeIIPattern:function(options){
    	
    	var posx=-30;
       	var from = this.drawStartActivity(posx);
       	posx = posx+150;
       	var to = this.drawEndActivity(posx);
       	this.drawPath(from, to);
    },
    drawStartActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	var pos={};
        pos['x']=posX;
        pos['y']=0;
        
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","A");
        this.facade.getCanvas().add(newShape);
        return newShape;
            
    },
    drawEndActivity:function(posX){
    	var parentShape=this.facade.getCanvas();
        var newShape;
    	
    	
        var pos={};
        pos['x']=posX;
        pos['y']=0;
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil;
        stencil = ORYX.Core.StencilSet.stencil(ssn + "Task");
        newShape=new ORYX.Core.Node({
                'eventHandlerCallback':this.facade.raiseEvent
            },stencil);
        newShape.setProperty("oryx-name","B");
        this.facade.getCanvas().add(newShape);
        return newShape;
    },
    drawPath:function(from,to){
    	
        //    	alert(from);
        //    	alert(to);
    	
        var newSequenceFlow;
        var parentShape=this.facade.getCanvas();
    	
        var ssn 	= this.facade.getStencilSets().keys()[0];
        var stencil = ORYX.Core.StencilSet.stencil(ssn + "Path");
   	
        newSequenceFlow=new ORYX.Core.Edge({
            'eventHandlerCallback':this.facade.raiseEvent
        },stencil);
        
    	
        // Set the docker
        newSequenceFlow.dockers.first().setDockedShape( from );
        newSequenceFlow.dockers.first().setReferencePoint({
            x: from.bounds.width() / 2.0,
            y: from.bounds.height() / 2.0
        });
    	
        newSequenceFlow.dockers.last().setDockedShape( to );
        newSequenceFlow.dockers.last().setReferencePoint({
            x: to.bounds.width() / 2.0,
            y: to.bounds.height() / 2.0
        });
		//newSequenceFlow.setProperty("oryx-temporalproperty","leadsto");
		//alert(newSequenceFlow.getProperty("oryx-temporalproperty"));
		newSequenceFlow.setProperty("oryx-temporalproperty","Precedes");
		newSequenceFlow.setProperty("oryx-exclude","Some Activity");
        //
        this.facade.getCanvas().add(newSequenceFlow);
        return newSequenceFlow;
    }
    
    
});



/**
 * Copyright (c) 2006
 * Martin Czuchra, Nicolas Peters, Daniel Polak, Willi Tscheschner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/


if(!ORYX.Plugins) {
	ORYX.Plugins = new Object();
}

var formStore = new Ext.data.Store ({
	autoLoad: true,
	proxy: new Ext.data.HttpProxy({
	url:"/form/getAllFormsByJspForm",
	method:'GET'
	}),
	baseParams:{
		isJspForm:"false"
	},
	reader: new Ext.data.JsonReader({ root: 'forms'},[{ name: 'id' }, { name: 'formName'}])
});

var processStore = new Ext.data.Store ({
	autoLoad : true,
	proxy: new Ext.data.HttpProxy({
	url:"/process/getAllProcessAsMap",
	method:'GET',
	}),
	baseParams:{
		isJspForm:"false"
	},
	reader: new Ext.data.JsonReader({ root: 'process'},[{ name: 'id' },{ name: 'processName'}])
});

var newFormStore = new Ext.data.Store({
	autoLoad:true,
	proxy: new Ext.data.HttpProxy({
	url:"/form/getAllFormsByJspForm",
	method:'GET'
	}),
	baseParams:{
		isJspForm:"true"
	},
	reader: new Ext.data.JsonReader({ root: 'forms'},[{ name: 'id' }, { name: 'formName'}])
});

/**
 * Data Store for loading Resource Data.
 */
var resourceStore = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }, {
            name: 'roletype',
            mapping: 'roletype'
        },{
            name: 'superior',
            mapping: 'superior'
        },{
            name: 'subordinate',
            mapping: 'subordinate'
        }
    ],
   /* data: {
        'items': ['']
    },*/
    root: 'items',
});

/**
 * Data Store for loading member permission
 */

var resourceStore_reader = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }, {
            name: 'roletype',
            mapping: 'roletype'
        },{
            name: 'superior',
            mapping: 'superior'
        },{
            name: 'subordinate',
            mapping: 'subordinate'
        }
    ],
   /* data: {
        'items': ['']
    },*/
    root: 'items',
});

var resourceStore_admin = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }, {
            name: 'roletype',
            mapping: 'roletype'
        },{
            name: 'superior',
            mapping: 'superior'
        },{
            name: 'subordinate',
            mapping: 'subordinate'
        }
    ],
   /* data: {
        'items': ['']
    },*/
    root: 'items',
});

var resourceStore_process = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }, {
            name: 'roletype',
            mapping: 'roletype'
        },{
            name: 'superior',
            mapping: 'superior'
        },{
            name: 'subordinate',
            mapping: 'subordinate'
        }
    ],
   /* data: {
        'items': ['']
    },*/
    root: 'items',
});


var selectedShapeObject;
var selectedShapeObjectProperty;
var prevGridTitle = "";
/**
 * Shape property copy functinality
 * @author mahesh
 */
ORYX.Editor.copyShapeProperties = function(){
	console.log(selectedShapeObject.grid);
	selectedShapeObjectProperty = selectedShapeObject.grid.getStore().getRange();
	if(selectedShapeObject.shapeSelection.shapes.length == 1) {
		prevGridTitle = selectedShapeObject.shapeSelection.shapes.first().getStencil().title();
	}
}

/**
 * Shape property paste functinality
 * @author mahesh
 */
ORYX.Editor.pasteShapeProperties = function(){
	var gridTitle = "";
	if(selectedShapeObject.shapeSelection.shapes.length == 1) {
		gridTitle = selectedShapeObject.shapeSelection.shapes.first().getStencil().title();
	}
	if(selectedShapeObjectProperty != undefined){
		if(gridTitle == prevGridTitle){
			var gridObj = selectedShapeObjectProperty;
			for(var i=0; i< gridObj.length;i++) {
					var gridColumnName = gridObj[i]["data"]["name"];
					if(ORYX.I18N.PropertyWindow.form == gridColumnName  || ORYX.I18N.PropertyWindow.organizer == gridColumnName  || ORYX.I18N.PropertyWindow.coordinator == gridColumnName  || ORYX.I18N.PropertyWindow.reader == gridColumnName  || ORYX.I18N.PropertyWindow.creator == gridColumnName  || ORYX.I18N.PropertyWindow.processedUser == gridColumnName  || ORYX.I18N.PropertyWindow.workFlowAdmin  == gridColumnName  || ORYX.I18N.PropertyWindow.ofOrganizer == gridColumnName  || ORYX.I18N.PropertyWindow.ofCoordinator == gridColumnName  || ORYX.I18N.PropertyWindow.ofReader == gridColumnName  || ORYX.I18N.PropertyWindow.ofCreator == gridColumnName  || ORYX.I18N.PropertyWindow.ofProcessedUser == gridColumnName  || ORYX.I18N.PropertyWindow.ofWorkFlowAdmin == gridColumnName){
						var recordObj = {
							column: 1
							,field: "value"
							,grid: selectedShapeObject.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value: gridObj[i]["data"]["value"]
						};
						selectedShapeObject.afterEdit(recordObj);
					}
			}
		}else{
			Ext.Msg.show({
	           title: ORYX.I18N.ERDFSupport.pasteProperyTitle,
	           msg: ORYX.I18N.ERDFSupport.pasteProperyText,
	           icon: Ext.MessageBox.ERROR 
	        });
		}
	}else{
		 Ext.Msg.show({
           title: ORYX.I18N.ERDFSupport.pasteProperyTitle,
           msg: ORYX.I18N.ERDFSupport.pasteProperyTextWarning,
           icon: Ext.MessageBox.ERROR 
        });
	}
}

var conditionExpressionId = "";
var globalVariableFromValue = ''; 
var jspForm;
var baseForm;
var formIdMap = {};
var isStartNode = "false";
var isForStartNodeType = false;
ORYX.Plugins.PropertyWindow = {

	facade: undefined,

	construct: function(facade) {
		// Reference to the Editor-Interface
		this.facade = facade;

		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_SHOW_PROPERTYWINDOW, this.init.bind(this));
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_LOADED, this.selectDiagram.bind(this));
		this.init();
	},
	
	init: function(){

		// The parent div-node of the grid
		this.node = ORYX.Editor.graft("http://www.w3.org/1999/xhtml",
			null,
			['div']);

		// If the current property in focus is of type 'Date', the date format
		// is stored here.
		this.currentDateFormat;

		// the properties array
		this.popularProperties = [];
		this.properties = [];
		
		/* The currently selected shapes whos properties will shown */
		this.shapeSelection = new Hash();
		this.shapeSelection.shapes = new Array();
		this.shapeSelection.commonProperties = new Array();
		this.shapeSelection.commonPropertiesValues = new Hash();
		
		this.updaterFlag = false;

		// creating the column model of the grid.
		this.columnModel = new Ext.grid.ColumnModel([
			{
				//id: 'name',
				header: ORYX.I18N.PropertyWindow.name,
				dataIndex: 'name',
				width: 90,
				sortable: true,
				renderer: this.tooltipRenderer.bind(this)
			}, {
				//id: 'value',
				header: ORYX.I18N.PropertyWindow.value,
				dataIndex: 'value',
				id: 'propertywindow_column_value',
				width: 110,
				editor: new Ext.form.TextField({
					allowBlank: false
				}),
				renderer: this.renderer.bind(this)
			},
			{
				header: "Pop",
				dataIndex: 'popular',
				hidden: true,
				sortable: true
			}
		]);

		// creating the store for the model.
        this.dataSource = new Ext.data.GroupingStore({
			proxy: new Ext.data.MemoryProxy(this.properties),
			reader: new Ext.data.ArrayReader({}, [
				{name: 'popular'},
				{name: 'name'},
				{name: 'value'},
				{name: 'icons'},
				{name: 'gridProperties'}
			]),
			sortInfo: {field: 'popular', direction: "ASC"},
			sortData : function(f, direction){
		        direction = direction || 'ASC';
		        var st = this.fields.get(f).sortType;
		        var fn = function(r1, r2){
		            var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
					var p1 = r1.data['popular'], p2  = r2.data['popular'];
		            return p1 && !p2 ? -1 : (!p1 && p2 ? 1 : (v1 > v2 ? 1 : (v1 < v2 ? -1 : 0)));
		        };
		        this.data.sort(direction, fn);
		        if(this.snapshot && this.snapshot != this.data){
		            this.snapshot.sort(direction, fn);
				}
		    },
			groupField: 'popular'
        });
		this.dataSource.load();
		
		this.grid = new Ext.grid.EditorGridPanel({
			clicksToEdit: 1,
			stripeRows: true,
			autoExpandColumn: "propertywindow_column_value",
			width:'auto',
			// the column model
			colModel: this.columnModel,
			enableHdMenu: false,
			view: new Ext.grid.GroupingView({
				forceFit: true,
				groupTextTpl: '{[values.rs.first().data.popular ? ORYX.I18N.PropertyWindow.oftenUsed : ORYX.I18N.PropertyWindow.moreProps]}'
			}),
			
			// the data store
			store: this.dataSource
			
		});

		region = this.facade.addToRegion('east', new Ext.Panel({
			width: 220,
			layout: "fit",
			border: false,
			title: 'Properties',
			items: [
				this.grid 
			]
		}), ORYX.I18N.PropertyWindow.title)

		// Register on Events
		this.grid.on('beforeedit', this.beforeEdit, this, true);
		this.grid.on('afteredit', this.afterEdit, this, true);
		this.grid.view.on('refresh', this.hideMoreAttrs, this, true);
		
		//this.grid.on(ORYX.CONFIG.EVENT_KEYDOWN, this.keyDown, this, true);
		
		// Renderer the Grid
		this.grid.enableColumnMove = false;
		//this.grid.render();

		// Sort as Default the first column
		//this.dataSource.sort('name');

	},
	
	// Select the Canvas when the editor is ready
	selectDiagram: function() {
		
		this.shapeSelection.shapes = [this.facade.getCanvas()];
		
		this.setPropertyWindowTitle();
		this.identifyCommonProperties();
		this.createProperties();
		this.setFormVersionChange();
	},

	specialKeyDown: function(field, event) {
		// If there is a TextArea and the Key is an Enter
		if(field instanceof Ext.form.TextArea && event.button == ORYX.CONFIG.KEY_Code_enter) {
			// Abort the Event
			return false
		}
	},
	tooltipRenderer: function(value, p, record) {
		/* Prepare tooltip */
		p.cellAttr = 'title="' + record.data.gridProperties.tooltip + '"';
		return value;
	},
	
	renderer: function(value, p, record) {
		
		this.tooltipRenderer(value, p, record);
				
		if(value instanceof Date) {
			// TODO: Date-Schema is not generic
			value = value.dateFormat(ORYX.I18N.PropertyWindow.dateFormat);
		} /*else if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_NOTIFICATIONMESSAGE){
			if( value.length != 0 && value != ""){
				return value;
			} else {
				return "Organizer not yet submitted the task";
			}
		} else if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_URGEMESSAGE){
			if(value.length != 0 && value != ""){
				return value;
			}else{
				return "There is a workflow need you to sign off, workflow name is <workFolwName>, created by <createdBy> at <createdAt>, plz click this link <url> to operate, thank you.";
			}
		}*/ else if(String(value).search("<a href='") < 0) {
			// Shows the Value in the Grid in each Line
			value = String(value).gsub("<", "&lt;");
			value = String(value).gsub(">", "&gt;");
			value = String(value).gsub("%", "&#37;");
			value = String(value).gsub("&", "&amp;");

			if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_COLOR) {
				value = "<div class='prop-background-color' style='background-color:" + value + "' />";
			} /*else if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_ISFORSTARTNODE){
				var children = this.facade.getCanvas().children;
				if(children[0] != undefined && children[1] != undefined){
					if(children[0].outgoing[0] != undefined){
						if(children[0].outgoing[0].outgoing[0] != undefined){
							if(children[0].outgoing[0].outgoing[0].id == this.shapeSelection.shapes[0].id){
								//var isTaskPropertyDisable = checkFirstNodeTaskProperty(pair.type());
								//if(isTaskPropertyDisable != true){
									isStartNode = true;
								//}
							}
						}	
					}
				}
				
				return isStartNode;
			}*/ else if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_RUNTIMECOMBO) {
				
				 if(value != 0 && value != "") {
				 jspForm = true;
					if(previousFormId == "" || previousFormId == null){
						previousFormId = value;
					}
					if(formStore.find("id", value) != null) {
						var index = formStore.find("id", value);
						if(index != -1) {
							var aRec = formStore.getAt(index);
							return aRec.get("formName")
						} else{
							//getAndSetFormVersionName(formStore,value,record,"formName");
							var formIndex = formStore.findBy(function (recordData, formName) {
							if (recordData.get('formName') == formIdMap[value]){
							    return recordData.get('formName'); // a record with this data exists
							}
							});
							var aRec = formStore.getAt(formIndex);
							if(aRec != undefined){
								return aRec.get("formName");
							} else {
								return "";
							}
						}
					}else {
						return value; 
					}
				}else {
					jspForm = false;
                   return "";  // display nothing if value is empty
				}	
			}else if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_NEWJSPFORM) {
				
				 if(value != 0 && value != "") {
				 baseForm = true;
					if(previousFormId == "" || previousFormId == null){
						previousFormId = value;
					}
					if(newFormStore.find("id", value) != null) {
						var index = newFormStore.find("id", value);
						if(index != -1) {
							var aRec = newFormStore.getAt(index);
							return aRec.get("formName")
						} else{
							//getAndSetFormVersionName(newFormStore,value,record,"formName");
							var formIndex = newFormStore.findBy(function (recordData, formName) {
							if (recordData.get('formName') == formIdMap[value]){
							    return recordData.get('formName'); // a record with this data exists
							}
							});
							var aRec = newFormStore.getAt(formIndex);
							if(aRec != undefined){
								return aRec.get("formName");
							} else {
								return "";
							}
						}
					}else {
						return value; 
					}
				}else {
				baseForm = false;
                   return "";  // display nothing if value is empty
				}	
			}else if(record.data.gridProperties.type == ORYX.CONFIG.TYPE_CUSTOMTREESELECT) {/*
				
				if(value != "" && value != null) {
					
					var valObj =  Ext.util.JSON.decode(value);
					if(resourceStore.getCount() == 0) {
						resourceStore.loadData(valObj);
					}
					var ownerText = "G - ";
					var performText = "U - ";
					for(var i=0;i<valObj.items.length;i++) {
						if(valObj.items[i].resource_type.toLowerCase() == "potentialowner") {
								ownerText = ownerText + valObj.items[i].resourceassignmentexpr+", ";
						}else if(valObj.items[i].resource_type.toLowerCase() == "humanperformer"){
								performText = performText + valObj.items[i].resourceassignmentexpr+", ";
						}
					}
					return ownerText+"</br>"+performText;
				} else {
                   return "";  // display nothing if value is empty
				}
			*/}			

			record.data.icons.each(function(each) {
				if(each.name == value) {
					if(each.icon) {
						value = "<img src='" + each.icon + "' /> " + value;
					}
				}
				
			});
		}
		return value;
	},

	beforeEdit: function(option) {

		var editorGrid 		= this.dataSource.getAt(option.row).data.gridProperties.editor;
		var editorRenderer 	= this.dataSource.getAt(option.row).data.gridProperties.renderer;

		if(editorGrid) {
			// Disable KeyDown
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);

			option.grid.getColumnModel().setEditor(1, editorGrid);
			
			editorGrid.field.row = option.row;
			// Render the editor to the grid, therefore the editor is also available 
			// for the first and last row
			editorGrid.render(this.grid);
			
			//option.grid.getColumnModel().setRenderer(1, editorRenderer);
			editorGrid.setSize(option.grid.getColumnModel().getColumnWidth(1), editorGrid.height);
		} else {
			return false;
		}
		
		var key = this.dataSource.getAt(option.row).data.gridProperties.propId;
		
		this.oldValues = new Hash();
		this.shapeSelection.shapes.each(function(shape){
			this.oldValues[shape.getId()] = shape.properties[key];
			conditionExpressionId = shape.getId();
		}.bind(this)); 
	},

	afterEdit: function(option) {
		//Ext1.0: option.grid.getDataSource().commitChanges();
		option.grid.getStore().commitChanges();

		var key 			 = option.record.data.gridProperties.propId;
		var selectedElements = this.shapeSelection.shapes;
		
		var oldValues 	= this.oldValues;	
		
		var newValue	= option.value;
		var facade		= this.facade;
		

		// Implement the specific command for property change
		var commandClass = ORYX.Core.Command.extend({
			construct: function(){
				this.key 		= key;
				this.selectedElements = selectedElements;
				this.oldValues = oldValues;
				this.newValue 	= newValue;
				this.facade		= facade;
			},			
			execute: function(){
				this.selectedElements.each(function(shape){
					if(!shape.getStencil().property(this.key).readonly()) {
						shape.setProperty(this.key, this.newValue);
					}
				}.bind(this));
				this.facade.setSelection(this.selectedElements);
				this.facade.getCanvas().update();
				this.facade.updateSelection();
			},
			rollback: function(){
				this.selectedElements.each(function(shape){
					shape.setProperty(this.key, this.oldValues[shape.getId()]);
				}.bind(this));
				this.facade.setSelection(this.selectedElements);
				this.facade.getCanvas().update();
				this.facade.updateSelection();
			}
		})		
		// Instanciated the class
		var command = new commandClass();
		
		// Execute the command
		this.facade.executeCommands([command]);


		// extended by Kerstin (start)
//
		this.facade.raiseEvent({
			type 		: ORYX.CONFIG.EVENT_PROPWINDOW_PROP_CHANGED, 
			elements	: selectedElements,
			key			: key,
			value		: option.value
		});
		// extended by Kerstin (end)
	},
	
	// Cahnges made in the property window will be shown directly
	editDirectly:function(key, value){
		
		this.shapeSelection.shapes.each(function(shape){
			if(!shape.getStencil().property(key).readonly()) {
				shape.setProperty(key, value);
				//shape.update();
			}
		}.bind(this));
		
		/* Propagate changed properties */
		var selectedElements = this.shapeSelection.shapes;
		
		this.facade.raiseEvent({
			type 		: ORYX.CONFIG.EVENT_PROPWINDOW_PROP_CHANGED, 
			elements	: selectedElements,
			key			: key,
			value		: value
		});

		this.facade.getCanvas().update();
		
	},
	
	// if a field becomes invalid after editing the shape must be restored to the old value
	updateAfterInvalid : function(key) {
		this.shapeSelection.shapes.each(function(shape) {
			if(!shape.getStencil().property(key).readonly()) {
				shape.setProperty(key, this.oldValues[shape.getId()]);
				shape.update();
			}
		}.bind(this));
		
		this.facade.getCanvas().update();
	},

	// extended by Kerstin (start)	
	dialogClosed: function(data) {
		var row = this.field ? this.field.row : this.row 
		this.scope.afterEdit({
			grid:this.scope.grid, 
			record:this.scope.grid.getStore().getAt(row), 
			//value:this.scope.grid.getStore().getAt(this.row).get("value")
			value: data
		})
		// reopen the text field of the complex list field again
		this.scope.grid.startEditing(row, this.col);
	},
	// extended by Kerstin (end)
	
	/**
	 * Changes the title of the property window panel according to the selected shapes.
	 */
	setPropertyWindowTitle: function() {
		if(this.shapeSelection.shapes.length == 1) {
			// add the name of the stencil of the selected shape to the title
				region.setTitle(ORYX.I18N.PropertyWindow.title +' ('+this.shapeSelection.shapes.first().getStencil().title()+')' );
		} else {
			region.setTitle(ORYX.I18N.PropertyWindow.title +' ('
							+ this.shapeSelection.shapes.length
							+ ' '
							+ ORYX.I18N.PropertyWindow.selected 
							+')');
		}
	},
	/**
	 * Sets this.shapeSelection.commonPropertiesValues.
	 * If the value for a common property is not equal for each shape the value
	 * is left empty in the property window.
	 */
	setCommonPropertiesValues: function() {
		this.shapeSelection.commonPropertiesValues = new Hash();
		this.shapeSelection.commonProperties.each(function(property){
			var key = property.prefix() + "-" + property.id();
			var emptyValue = false;
			var firstShape = this.shapeSelection.shapes.first();
			
			this.shapeSelection.shapes.each(function(shape){
				if(firstShape.properties[key] != shape.properties[key]) {
					emptyValue = true;
				}
			}.bind(this));
			
			/* Set property value */
			if(!emptyValue) {
				if(key == "oryx-operationcreator"){
					if(firstShape.properties[key]){
						this.shapeSelection.commonPropertiesValues[key]
						= firstShape.properties[key];
					}else{
						this.shapeSelection.commonPropertiesValues[key] = "";//"[{'totalCount':1, 'items':[{resource_type:'withdraw_elm',resource:'Withdraw',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}]}]";
					}
				}else{
					this.shapeSelection.commonPropertiesValues[key]
					= firstShape.properties[key];
				}
			}
		}.bind(this));
	},
	
	/**
	 * Returns the set of stencils used by the passed shapes.
	 */
	getStencilSetOfSelection: function() {
		var stencils = new Hash();
		
		this.shapeSelection.shapes.each(function(shape) {
			stencils[shape.getStencil().id()] = shape.getStencil();
		})
		return stencils;
	},
	
	/**
	 * Identifies the common Properties of the selected shapes.
	 */
	identifyCommonProperties: function() {
		this.shapeSelection.commonProperties.clear();
		
		/* 
		 * A common property is a property, that is part of 
		 * the stencil definition of the first and all other stencils.
		 */
		var stencils = this.getStencilSetOfSelection();
		var firstStencil = stencils.values().first();
		var comparingStencils = stencils.values().without(firstStencil);
		
		
		if(comparingStencils.length == 0) {
			this.shapeSelection.commonProperties = firstStencil.properties();
		} else {
			var properties = new Hash();
			
			/* put all properties of on stencil in a Hash */
			firstStencil.properties().each(function(property){
				properties[property.namespace() + '-' + property.id() 
							+ '-' + property.type()] = property;
			});
			
			/* Calculate intersection of properties. */
			
			comparingStencils.each(function(stencil){
				var intersection = new Hash();
				stencil.properties().each(function(property){
					if(properties[property.namespace() + '-' + property.id()
									+ '-' + property.type()]){
						intersection[property.namespace() + '-' + property.id()
										+ '-' + property.type()] = property;
					}
				});
				properties = intersection;	
			});
			
			this.shapeSelection.commonProperties = properties.values();
		}
	},
	
	onSelectionChanged: function(event) {
		
		/* Event to call afterEdit method */
		this.grid.stopEditing();
		selectedShapeObject = this;
		
		/* Selected shapes */
		this.shapeSelection.shapes = event.elements;
		
		baseForm = false;
		jspForm = false;
		if(event.elements != null && event.elements[0] != null){
			var  shapesJSON = JSON.parse(event.elements[0].properties.toJSON().replace(/-/g,"$").toString());
			if(shapesJSON.oryx$form != null && shapesJSON.oryx$form != "" && shapesJSON.oryx$form.length > 0){
				baseForm = false;
				jspForm = true;
			} else if(shapesJSON.oryx$jspform != null && shapesJSON.oryx$jspform != "" && shapesJSON.oryx$jspform.length > 0){
				jspForm = false;
				baseForm = true;
			} 
		}
		isForStartNodeType = false;
		if(event.elements != null && event.elements[0] != null && event.elements[0].incoming[0] != null && event.elements[0].incoming[0].incoming[0] != null && event.elements[0].incoming[0].incoming[0].toJSON().stencil.id == "StartNoneEvent"){
			isStartNode = "true";
			isForStartNodeType = true;
		}
		
		/* Case: nothing selected */
		if(event.elements.length == 0) {
			this.shapeSelection.shapes = [this.facade.getCanvas()];
		}
		
		/* subselection available */
		if(event.subSelection){
			this.shapeSelection.shapes = [event.subSelection];
		}
		
		this.setPropertyWindowTitle();
		this.identifyCommonProperties();
		this.setCommonPropertiesValues();
		
		// Create the Properties
		
		this.createProperties();
		
		if(this.shapeSelection.shapes.length == 1) {
			var gridTitle = this.shapeSelection.shapes.first().getStencil().title();
			if(gridTitle == ORYX.I18N.PropertyWindow.task){
				this.setPreDefinedProperties();
			} else if(gridTitle == ORYX.I18N.PropertyWindow.startEvent){
				this.setPreDefinedStartEventProperties();
			}
		}
	},
	setPreDefinedStartEventProperties:function(){
		var gridObj = this.grid.getStore().getRange();
		for(var i=0; i< gridObj.length;i++) {
			var gridColumnName = gridObj[i]["data"]["name"];
			if(gridColumnName == ORYX.I18N.PropertyWindow.urgeMessage){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"There is a workflow you need to sign off, workflow name is <workFolwName>, created by <createdBy> at <createdAt>, plz click this link <url> to operate, thank you."
						};
					this.afterEdit(recordObj);
				}
			}else if(gridColumnName == ORYX.I18N.PropertyWindow.notificationMessage){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"There is a workflow need to sign off by organizer : <organizerName>  Workflow name is <workFolwName>"
						};
					this.afterEdit(recordObj);
				}
			}
		}
	},
	setPreDefinedProperties: function(){
		var gridObj = this.grid.getStore().getRange();
		for(var i=0; i< gridObj.length;i++) {
			var gridColumnName = gridObj[i]["data"]["name"];
			if(ORYX.I18N.PropertyWindow.ofCreator == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"[{'totalCount':2, 'items':[{resource_type:'withdraw_elm',resource:'Withdraw',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}, {resource_type:'urge_elm',resource:'Urge',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}]}]"
						};
					this.afterEdit(recordObj);
				}
			}else if(ORYX.I18N.PropertyWindow.ofCoordinator == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"[{'totalCount':1, 'items':[{resource_type:'save_elm',resource:'Save',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}]}]"
						};
					this.afterEdit(recordObj);
				}
			}else if(ORYX.I18N.PropertyWindow.ofReader == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"[{'totalCount':1, 'items':[{resource_type:'circulateperusal_elm',resource:'Circulate Perusal',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}]}]"
						};
					this.afterEdit(recordObj);
				}
			}else if(ORYX.I18N.PropertyWindow.ofProcessedUser == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"[{'totalCount':1, 'items':[{resource_type:'recall_elm',resource:'Recall',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}]}]"
						};
					this.afterEdit(recordObj);
				}
			}else if(ORYX.I18N.PropertyWindow.ofWorkFlowAdmin == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"[{'totalCount':5, 'items':[{resource_type:'jump_elm',resource:'Jump',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}, {resource_type:'transactorreplacement_elm',resource:'TransactorReplacement',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}, {resource_type:'terminate_elm',resource:'Terminate',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}, {resource_type:'suspend_elm',resource:'Suspend',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}, {resource_type:'print_elm',resource:'Print',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}]}]"
						};
					this.afterEdit(recordObj);
				}
			}else if(ORYX.I18N.PropertyWindow.ofOrganizer == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == ""){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:"[{'totalCount':3, 'items':[{resource_type:'submit_elm',resource:'submit',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}, {resource_type:'save_elm',resource:'Save',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}, {resource_type:'returnoperation_elm',resource:'Return Operation',resourceassignmentexpr:'Default Operation',language:'',evaluatestotype:''}]}]"
						};
					this.afterEdit(recordObj);
				}
			} else if(ORYX.I18N.PropertyWindow.isStartNode == gridColumnName){
				/*var isStartNode = "false";
				var currentTask = this.shapeSelection.shapes[0];
				if(children[0] != undefined && children[1] != undefined){
					if(children[0].outgoing[0] != undefined){
						if(children[0].outgoing[0].outgoing[0] != undefined){
							if(children[0].outgoing[0].outgoing[0].id == this.shapeSelection.shapes[0].id){
								//var isTaskPropertyDisable = checkFirstNodeTaskProperty(pair.type());
								//if(isTaskPropertyDisable != true){
									isStartNode = "true";
								//}
							}
						}	
					}
				}
				isForStartNodeType = false;
				if(currentTask.incoming[0] != undefined && currentTask.incoming[0].incoming[0] != undefined){
					if(currentTask.incoming[0].incoming[0].toJSON().stencil.id == "StartNoneEvent"){
						isStartNode = "true";
						isForStartNodeType = true;
					}
				}*/
				var cellValue = gridObj[i]["data"]["value"];
				if(cellValue == "" || isStartNode != cellValue){
					var recordObj = {
							column: 1
							,field: "value"
							,grid: this.grid
							,originalValue: ""
							,record: gridObj[i]
							,row: i
							,value:isStartNode
						};
					this.afterEdit(recordObj);
				}
				isStartNode = "false";
			} else if(ORYX.I18N.PropertyWindow.form == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				var index = formStore.findBy(function (recordData, formName) {
					if (recordData.get('formName') == formIdMap[cellValue]){
					 return recordData.get('formName'); 
					}
				});
				var aRec = formStore.getAt(index);
				if(aRec != undefined){
					if(cellValue == "" || aRec.get("id") != cellValue){
						var recordObj = {
								column: 1
								,field: "value"
								,grid: this.grid
								,originalValue: ""
								,record: gridObj[i]
								,row: i
								,value:aRec.get("id")
							};
						this.afterEdit(recordObj);
					}
				}
			} else if(ORYX.I18N.PropertyWindow.jspForm == gridColumnName){
				var cellValue = gridObj[i]["data"]["value"];
				var index = newFormStore.findBy(function (recordData, formName) {
					if (recordData.get('formName') == formIdMap[cellValue]){
					 return recordData.get('formName'); 
					}
				});
				var aRec = newFormStore.getAt(index);
				if(aRec != undefined){
					if(cellValue == "" || aRec.get("id") != cellValue){
						var recordObj = {
								column: 1
								,field: "value"
								,grid: this.grid
								,originalValue: ""
								,record: gridObj[i]
								,row: i
								,value:aRec.get("id")
							};
						this.afterEdit(recordObj);
					}
				}
			}
		}
	},
	
	setFormVersionChange: function() {
		var taskShapes = [];
		this.shapeSelection.shapes.each(function(shape) {
		var children = shape.children;
		for(var i=0;i<children.length;i++){
			if(children[i].toJSON().stencil.id == ORYX.I18N.PropertyWindow.task){
				//value = JSON.parse(children[i].properties.toJSON().replace(/-/g,"$").toString()).oryx$form;
				taskShapes.push(children[i]);
			}
		}
		});
		if(taskShapes.length > 1){
			for(var i=0;i<taskShapes.length;i++){
				var taskShape = [];
				taskShape.push(taskShapes[i]);
				this.setVersionChangeForDynamicTask(taskShape);
			}
		} else if(taskShapes.length > 0){
			var taskShape = [];
			taskShape = taskShapes;
			this.setVersionChangeForDynamicTask(taskShape);
		}
	},	
	
	setVersionChangeForDynamicTask: function(event) {
		/* Event to call afterEdit method */
		this.grid.stopEditing();
		selectedShapeObject = this;
		
		/* Selected shapes */
		this.shapeSelection.shapes = event;
		
		if(event.subSelection){
			this.shapeSelection.shapes = [event[0].subSelection];
		}
		
		this.setPropertyWindowTitle();
		this.identifyCommonProperties();
		this.setCommonPropertiesValues();
		
		// Create the Properties
		
		this.createProperties();
		if(this.shapeSelection.shapes.length == 1) {
			var gridTitle = this.shapeSelection.shapes.first().getStencil().title();
			if(gridTitle == ORYX.I18N.PropertyWindow.task){
				this.setPreDefinedProperties();
			} else if(gridTitle == ORYX.I18N.PropertyWindow.startEvent){
				this.setPreDefinedStartEventProperties();
			}
		}
	},
	
	/**
	 * Creates the properties for the ExtJS-Grid from the properties of the
	 * selected shapes.
	 */
	createProperties: function() {
	
	
		// add the properties Disable in form
		
	/*	var gridObj  = this.grid.getStore().getRange();
		var jspForm;
		var baseForm;
		
		if( gridObj[3] != undefined && gridObj[4] != undefined){
			var gridFormColumnName = gridObj[3]["data"]["name"];
			var gridJspFormColumnName = gridObj[4]["data"]["name"];
			var gridFormColumnValue = gridObj[3]["data"]["value"];
			var gridJspFormColumnValue = gridObj[4]["data"]["value"];
			if( gridFormColumnName == "Form" && gridJspFormColumnName == "JSP Form"){
				var children = this.facade.getCanvas().children;
				for(var i=0; i< children.length; i++){
				if(children[i].id == conditionExpressionId){
					var  shapesJSON = JSON.parse(children[i].properties.toJSON().replace(/-/g,"$").toString());
					if(gridFormColumnValue.length > 0 && gridJspFormColumnValue.length > 0){
						gridObj[4]["data"]["value"] = "";
						gridObj[3]["data"]["value"] = "";
					}else if(gridFormColumnValue.length > 0 && gridJspFormColumnValue.length == 0 && shapesJSON.oryx$form != "" && shapesJSON.oryx$jspform == ""){
		
						baseForm = false;
						jspForm = true;
					}else if(gridFormColumnValue.length == 0 && gridJspFormColumnValue.length > 0 && shapesJSON.oryx$jspform != "" && shapesJSON.oryx$form == ""){
						baseForm = true;
						jspForm = false;
					}else{
						baseForm = false;
						jspForm = false;
					}
				}
			    }
			}
		}
		if(gridObj[1] != undefined && gridObj[2] != undefined){
			var gridFormColumnName = gridObj[1]["data"]["name"];
			var gridJspFormColumnName = gridObj[2]["data"]["name"];
			
			if( gridFormColumnName == "Form" && gridJspFormColumnName == "JSP Form"){
				var gridFormColumnValue = gridObj[1]["data"]["value"];
				var gridJspFormColumnValue = gridObj[2]["data"]["value"];
				if(gridFormColumnValue.length > 0 && gridJspFormColumnValue.length > 0){
					gridObj[4]["data"]["value"] = "";
					gridObj[3]["data"]["value"] = "";
				}else if(gridFormColumnValue.length > 0 && gridJspFormColumnValue.length == 0){
					baseForm = false;
					jspForm = true;
				}else if(gridFormColumnValue.length == 0 && gridJspFormColumnValue.length > 0){
					baseForm = true;
					jspForm = false;
				}else{
					baseForm = false;
					jspForm = false;
				}
			}
		}*/
		this.properties = [];
		this.popularProperties = [];

		if(this.shapeSelection.commonProperties) {
			
			// add new property lines
			this.shapeSelection.commonProperties.each((function(pair, index) {

				var key = pair.prefix() + "-" + pair.id();
				
				// Get the property pair
				var name		= pair.title();
				var icons		= [];
				var attribute	= this.shapeSelection.commonPropertiesValues[key];
				
				var editorGrid = undefined;
				var editorRenderer = null;
				var isDisabled = false;
				var refToViewFlag = false;

				if(!pair.readonly()){
				
				// Set as the read only for the first task that connect with start node
				
				/*var children = this.facade.getCanvas().children;
				if(children[0] != undefined && children[1] != undefined){
					if(children[0].outgoing[0] != undefined){
						if(children[0].outgoing[0].outgoing[0] != undefined){
							if(children[0].outgoing[0].outgoing[0].id == this.shapeSelection.shapes[0].id){
								var isTaskPropertyDisable = checkFirstNodeTaskProperty(pair.type());
								if(isTaskPropertyDisable != true){
									isDisabled = true;
								}
							}
						}	
					}
				}*/
				var currentTask = this.shapeSelection.shapes[0];
				if(currentTask.incoming != undefined && currentTask.incoming[0] != undefined && currentTask.incoming[0].incoming != undefined && currentTask.incoming[0].incoming[0] != undefined){
					if(currentTask.incoming[0].incoming[0].toJSON().stencil.id == "StartNoneEvent"){
						var isTaskPropertyDisable = checkFirstNodeTaskProperty(pair.type(),pair.title());
						if(isTaskPropertyDisable != true){
							isDisabled = true;
						}
					}
				}
				
				
					switch(pair.type()) {
						case ORYX.CONFIG.TYPE_STRING:
							// If the Text is MultiLine
							if(pair.wrapLines()) {
								// Set the Editor as TextArea
								var editorTextArea = new Ext.form.TextArea({alignment: "tl-tl", allowBlank: pair.optional(),  msgTarget:'title', maxLength:pair.length()});
								editorTextArea.on('keyup', function(textArea, event) {
									this.editDirectly(key, textArea.getValue());
								}.bind(this));								
								
								editorGrid = new Ext.Editor(editorTextArea);
							} else {
								// If not, set the Editor as InputField
								var editorInput = new Ext.form.TextField({allowBlank: pair.optional(),  msgTarget:'title', maxLength:pair.length()});
								editorInput.on('keyup', function(input, event) {
									this.editDirectly(key, input.getValue());
								}.bind(this));
								
								// reverts the shape if the editor field is invalid
								editorInput.on('blur', function(input) {
									if(!input.isValid(false))
										this.updateAfterInvalid(key);
								}.bind(this));
								
								editorInput.on("specialkey", function(input, e) {
									if(!input.isValid(false))
										this.updateAfterInvalid(key);
								}.bind(this));
								
								editorGrid = new Ext.Editor(editorInput);
							}
							break;
						case ORYX.CONFIG.TYPE_BOOLEAN:
							// Set the Editor as a CheckBox
							var editorCheckbox = new Ext.form.Checkbox({disabled:isDisabled});
							editorCheckbox.on('check', function(c,checked) {
								this.editDirectly(key, checked);
							}.bind(this));
							
							editorGrid = new Ext.Editor(editorCheckbox);
							break;
						case ORYX.CONFIG.TYPE_INTEGER:
							// Set as an Editor for Integers
							var numberField = new Ext.form.NumberField({allowBlank: pair.optional(), allowDecimals:false, msgTarget:'title', minValue: pair.min(), maxValue: pair.max(),disabled:isDisabled});
							numberField.on('keyup', function(input, event) {
								this.editDirectly(key, input.getValue());
							}.bind(this));							
							
							editorGrid = new Ext.Editor(numberField);
							break;
						case ORYX.CONFIG.TYPE_FLOAT:
							// Set as an Editor for Float
							var numberField = new Ext.form.NumberField({ allowBlank: pair.optional(), allowDecimals:true, msgTarget:'title', minValue: pair.min(), maxValue: pair.max(),disabled:isDisabled});
							numberField.on('keyup', function(input, event) {
								this.editDirectly(key, input.getValue());
							}.bind(this));
							
							editorGrid = new Ext.Editor(numberField);

							break;
						case ORYX.CONFIG.TYPE_COLOR:
							// Set as a ColorPicker
							// Ext1.0 editorGrid = new gEdit(new form.ColorField({ allowBlank: pair.optional(),  msgTarget:'title' }));

							var editorPicker = new Ext.ux.ColorField({ allowBlank: pair.optional(),  msgTarget:'title', facade: this.facade, disabled:isDisabled });
							
							/*this.facade.registerOnEvent(ORYX.CONFIG.EVENT_COLOR_CHANGE, function(option) {
								this.editDirectly(key, option.value);
							}.bind(this));*/
							
							editorGrid = new Ext.Editor(editorPicker);

							break;
						case ORYX.CONFIG.TYPE_CHOICE:
							var items = pair.items();
							var options = [];
							items.each(function(value) {
								if(value.value() == attribute)
									attribute = value.title();
									
								if(value.refToView()[0])
									refToViewFlag = true;
																
								options.push([value.icon(), value.title(), value.value()]);
															
								icons.push({
									name: value.title(),
									icon: value.icon()
								});
							});
							
							var store = new Ext.data.SimpleStore({
						        fields: [{name: 'icon'},
									{name: 'title'},
									{name: 'value'}	],
						        data : options // from states.js
						    });
							
							// Set the grid Editor

						    var editorCombo = new Ext.form.ComboBox({
								tpl: '<tpl for="."><div class="x-combo-list-item">{[(values.icon) ? "<img src=\'" + values.icon + "\' />" : ""]} {title}</div></tpl>',
						        store: store,
						        displayField:'title',
								valueField: 'value',
						        typeAhead: true,
						        mode: 'local',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:isForStartNodeType,
						    });
								
							editorCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
							}.bind(this))
							
							editorGrid = new Ext.Editor(editorCombo);

							break;
						case ORYX.CONFIG.TYPE_DATE:
							var currFormat = ORYX.I18N.PropertyWindow.dateFormat
							if(!(attribute instanceof Date))
								attribute = Date.parseDate(attribute, currFormat)
							editorGrid = new Ext.Editor(new Ext.form.DateField({ allowBlank: pair.optional(), format:currFormat,  msgTarget:'title',disabled:isDisabled}));
							break;

						case ORYX.CONFIG.TYPE_TEXT:
							
							var cf = new Ext.form.ComplexTextField({
								allowBlank: pair.optional(),
								dataSource:this.dataSource,
								grid:this.grid,
								row:index,
								facade:this.facade,
								disabled:isDisabled
							});
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
							
						// extended by Kerstin (start)
						case ORYX.CONFIG.TYPE_COMPLEX:
							
							var cf = new Ext.form.ComplexListField({ allowBlank: pair.optional(),disabled:isDisabled}, pair.complexItems(), key, this.facade);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						// extended by Kerstin (end)
						
						// extended by Gerardo (Start)
						case "CPNString":
							var editorInput = new Ext.form.TextField(
									{
										allowBlank: pair.optional(),
										msgTarget:'title', 
										maxLength:pair.length(), 
										enableKeyEvents: true,
										disabled:isDisabled
									});
							
							editorInput.on('keyup', function(input, event) {
								this.editDirectly(key, input.getValue());
								alert("huhu");
							}.bind(this));
							
							editorGrid = new Ext.Editor(editorInput);							
							break;
						// extended by Gerardo (End)
							//extended by Shiva
						case ORYX.CONFIG.TYPE_RUNTIMECOMBO:
							/*var formStore = new Ext.data.Store ({
								autoLoad: true,
								proxy: new Ext.data.HttpProxy({
								url:pair.dataurl()}),
								reader: new Ext.data.JsonReader({ root: 'forms'},[{ name: 'id' }, { name: 'formName'}])
							});*/
							
							// Set the grid Editor
							
							
						    var formCombo = new Ext.form.ComboBox({
								//tpl: '<tpl for="."><div class="x-combo-list-item">{[(values.icon) ? "<img src=\'" + values.icon + "\' />" : ""]} {title}</div></tpl>',
						        store: formStore,
						        displayField:'formName',
								valueField: 'id',
						        typeAhead: true,
						        mode: 'local',
						        lazyRender: true,
						        value:'formName',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:baseForm,
						        /*listeners: {
							   		 'expand': function(e) {
							   			// var locationURL = window.location.host+'/form/getAllForms';
							   			// alert(locationURL);
							   			Ext.Ajax.request({
							   				waitMsg: 'Saving changes...',
							   				method: 'POST',
							   				headers: {'Content-Type': 'application/json'},
							   				url:'/form/getAllForms',
							   				failure:function(response){
							   					alert("failure");
							   				},
							   				success:function(response){
							   					alert(response.responseText);
							   					var responseObj = Ext.util.JSON.decode(response.responseText);
							   					store.loadData(responseObj);
							   				}
							   			}); 
							    		},
									}*/
						    });
								
							formCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
								jspForm = true;
							}.bind(this))
							
							editorGrid = new Ext.Editor(formCombo);
							break;
							
						case ORYX.CONFIG.TYPE_ADDPROCESS:
							var processCombo = new Ext.form.ComboBox({
								store : processStore,
								displayField : 'processName',
								valueField : 'id',
								typeAhead : true,
								mode: 'local',
								lazyRender : true,
								value :'processName',
								triggerAction : 'all',
								selectOnFocus :true,
								
							});
							processCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
							}.bind(this));
							
							editorGrid = new Ext.Editor(processCombo);
							break;
						case ORYX.CONFIG.TYPE_NEWJSPFORM:
							/*var formStore = new Ext.data.Store ({
								autoLoad: true,
								proxy: new Ext.data.HttpProxy({
								url:pair.dataurl()}),
								reader: new Ext.data.JsonReader({ root: 'forms'},[{ name: 'id' }, { name: 'formName'}])
							});*/
							
							// Set the grid Editor
							
							var formCombo = new Ext.form.ComboBox({
								//tpl: '<tpl for="."><div class="x-combo-list-item">{[(values.icon) ? "<img src=\'" + values.icon + "\' />" : ""]} {title}</div></tpl>',
						        store: newFormStore,
						        displayField:'formName',
								valueField: 'id',
						        typeAhead: true,
						        mode: 'local',
						        lazyRender: true,
						        value:'formName',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:jspForm,
						        /*listeners: {
							   		 'expand': function(e) {
							   			// var locationURL = window.location.host+'/form/getAllForms';
							   			// alert(locationURL);
							   			Ext.Ajax.request({
							   				waitMsg: 'Saving changes...',
							   				method: 'POST',
							   				headers: {'Content-Type': 'application/json'},
							   				url:'/form/getAllForms',
							   				failure:function(response){
							   					alert("failure");
							   				},
							   				success:function(response){
							   					alert(response.responseText);
							   					var responseObj = Ext.util.JSON.decode(response.responseText);
							   					store.loadData(responseObj);
							   				}
							   			}); 
							    		},
									}*/
						    });
								
							formCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
								baseForm = true;
							}.bind(this))
							
							editorGrid = new Ext.Editor(formCombo);
							break;
						case ORYX.CONFIG.TYPE_REPEATEDOPERATION:
							var repeatedOperationStore = new Ext.data.SimpleStore({
						        fields: ['id', 'formName'],
						        data : [
						        ['true', 'True'],
						        ['false', 'False']] // from states.js
						    });
						    var repeatedOperationCombo = new Ext.form.ComboBox({
						        store: repeatedOperationStore,
						        displayField:'formName',
								valueField: 'id',
						        typeAhead: true,
						        mode: 'local',
						        lazyRender: true,
						        value:'formName',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:isDisabled,
						    });
								
						    repeatedOperationCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
							}.bind(this))
							
							editorGrid = new Ext.Editor(repeatedOperationCombo);
							break;
						case ORYX.CONFIG.TYPE_EMPTYPERSONNEL:
							var emptyPersonnelStore = new Ext.data.SimpleStore({
						        fields: ['id', 'formName'],
						        data : [
						        ['true', 'True'],
						        ['false', 'False']] // from states.js
						    }); 
							
						    var emptyPersonnelCombo = new Ext.form.ComboBox({
						        store: emptyPersonnelStore,
						        displayField:'formName',
								valueField: 'id',
						        typeAhead: true,
						        mode: 'local',
						        lazyRender: true,
						        value:'formName',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:isDisabled,
						    });
								
						    emptyPersonnelCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
							}.bind(this))
							
							editorGrid = new Ext.Editor(emptyPersonnelCombo);
							break;
						case ORYX.CONFIG.TYPE_ISFORSTARTNODE:
							var isForStartNodeText = new Ext.form.TextField({ 
								allowBlank:false,
								disabled:true
								 
							}); 
							editorGrid = new Ext.Editor(isForStartNodeText);
							break;
						case ORYX.CONFIG.TYPE_ISARCHIVE:
							var repeatedIsArchive = new Ext.data.SimpleStore({
						        fields: ['id', 'formName'],
						        data : [
						        ['true', 'True'],
						        ['false', 'False']] // from states.js
						    });
						    var repeateIsArchiveCombo = new Ext.form.ComboBox({
						        store: repeatedIsArchive,
						        displayField:'formName',
								valueField: 'id',
						        typeAhead: true,
						        mode: 'local',
						        lazyRender: true,
						        value:'formName',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:isDisabled
						    });
								
						    repeateIsArchiveCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
							}.bind(this))
							
							editorGrid = new Ext.Editor(repeateIsArchiveCombo);
							break;
						case ORYX.CONFIG.TYPE_READEROPTIONTYPE:
							var readerOptionType = new Ext.data.SimpleStore({
						        fields: ['id', 'formName'],
						        data : [
						        ['onlyreader', 'Only Reader'],
						        ['allstackholder', 'All Stackholder']] // from states.js
						    });
						    var readerOptionTypeCombo = new Ext.form.ComboBox({
						        store: readerOptionType,
						        displayField:'formName',
								valueField: 'id',
						        typeAhead: true,
						        mode: 'local',
						        lazyRender: true,
						        value:'formName',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:isDisabled
						    });
								
						    readerOptionTypeCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
							}.bind(this))
							
							editorGrid = new Ext.Editor(readerOptionTypeCombo);
							break;
						case ORYX.CONFIG.TYPE_ADDOPINION:
							var repeatedOpinion = new Ext.data.SimpleStore({
						        fields: ['id', 'formName'],
						        data : [['true', 'True'],
						        ['false', 'False']] // from states.js
						    });
						    var repeateIsArchiveCombo = new Ext.form.ComboBox({
						        store: repeatedOpinion,
						        displayField:'formName',
								valueField: 'id',
						        typeAhead: true,
						        mode: 'local',
						        lazyRender: true,
						        value:'true',
						        triggerAction: 'all',
						        selectOnFocus:true,
						        disabled:isDisabled
						    });
								
						    repeateIsArchiveCombo.on('select', function(combo, record, index) {
								this.editDirectly(key, combo.getValue());
								globalVariableFromValue= combo.getValue();
							}.bind(this))
							
							editorGrid = new Ext.Editor(repeateIsArchiveCombo);
							break;	
						//end
						case ORYX.CONFIG.TYPE_PROCESSTIMESETTING:
							var cf = new Ext.form.ProcessTimeSetting({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_FORMFIELDAUTOMATIC:
							var cf = new Ext.form.FormFieldAutomatic({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_ONREAD:
							var cf = new Ext.form.OnRead({
								allowBlank: pair.optional(),
								dataSource:this.dataSource,
								grid:this.grid,
								row:index,
								facade:this.facade,
								disabled:isDisabled
							});
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_ONUPDATE:
							var cf = new Ext.form.OnUpdate({
								allowBlank: pair.optional(),
								dataSource:this.dataSource,
								grid:this.grid,
								row:index,
								facade:this.facade,
								disabled:isDisabled
							});
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_ONCREATE:
							var cf = new Ext.form.OnCreate({
								allowBlank: pair.optional(),
								dataSource:this.dataSource,
								grid:this.grid,
								row:index,
								facade:this.facade,
								
							});
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_CUSTOMTREESELECT:
							var cf = new Ext.form.CustomTreeSelect({ allowBlank: false}, pair.complexItems(), key, this.facade,this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_CHOOSECONDITION:
							var cf = new Ext.form.ChooseCondition({ allowBlank: false}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_TRANSACTORRELATIONSHIP:
							var cf = new Ext.form.TransactorRelationship({ allowBlank: false}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_FORMFIELDPERMISSION:
							var cf = new Ext.form.FormFieldPermission({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_FORMMEMBERPERMISSION:
							var cf = new Ext.form.FormMemberPermission({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_MEMBERPERMISSION:
							var cf = new Ext.form.MemberPermission({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end

						case ORYX.CONFIG.TYPE_MEMBERPERMISSIONPROCESS:
							var cf = new Ext.form.MemberPermissionProcess({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_STARTPERMISSION:
							var cf = new Ext.form.StartPermission({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_URGEMESSAGE:
							var cf = new Ext.form.UrgeMessage({
								allowBlank: pair.optional(),
								dataSource:this.dataSource,
								grid:this.grid,
								row:index,
								facade:this.facade
							});
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_NOTIFICATIONMESSAGE:
							var cf = new Ext.form.NotificationMessage({
								allowBlank: pair.optional(),
								dataSource:this.dataSource,
								grid:this.grid,
								row:index,
								facade:this.facade
							});
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_MEMBERPERMISSIONEND:
							var cf = new Ext.form.MemberPermissionEnd({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end
						case ORYX.CONFIG.TYPE_OPERATIONPERMISSION:
							var cf = new Ext.form.OperationPermission({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end 
						case ORYX.CONFIG.TYPE_STARTNODESCRIPT:
							var cf = new Ext.form.StartNodeScript({ allowBlank: false}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end 
						case ORYX.CONFIG.TYPE_ENDNODESCRIPT:
							var cf = new Ext.form.EndNodeScript({ allowBlank: false}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end 
						case ORYX.CONFIG.TYPE_CREATECUSTOMOPERATION:
							var cf = new Ext.form.CreateCustomOperation({ allowBlank: false,disabled:isDisabled}, pair.complexItems(), key, this.facade, this.grid);
							cf.on('dialogClosed', this.dialogClosed, {scope:this, row:index, col:1,field:cf});							
							editorGrid = new Ext.Editor(cf);
							break;
						//end 
						default:
							var editorInput = new Ext.form.TextField({ allowBlank: pair.optional(),  msgTarget:'title', maxLength:pair.length(), enableKeyEvents: true,disabled:isDisabled});
							editorInput.on('keyup', function(input, event) {
								this.editDirectly(key, input.getValue());
							}.bind(this));
							
							editorGrid = new Ext.Editor(editorInput);
					}


					// Register Event to enable KeyDown
					editorGrid.on('beforehide', this.facade.enableEvent.bind(this, ORYX.CONFIG.EVENT_KEYDOWN));
					editorGrid.on('specialkey', this.specialKeyDown.bind(this));

				} else if(pair.type() === ORYX.CONFIG.TYPE_URL || pair.type() === ORYX.CONFIG.TYPE_DIAGRAM_LINK){
					attribute = String(attribute).search("http") !== 0 ? ("http://" + attribute) : attribute;
					attribute = "<a href='" + attribute + "' target='_blank'>" + attribute.split("://")[1] + "</a>"
				}
				
				// Push to the properties-array
				if(pair.visible()) {
					// Popular Properties are those with a refToView set or those which are set to be popular
                    /*if (pair.refToView()[0] || refToViewFlag || pair.popular()) {
                            pair.setPopular();
                    }*/ 
                    if (pair.popular()) {
                    pair.setPopular();
                    }

					
					if(pair.popular()) {
						this.popularProperties.push([pair.popular(), name, attribute, icons, {
							editor: editorGrid,
							propId: key,
							type: pair.type(),
							tooltip: pair.description(),
							renderer: editorRenderer
						}]);
					}
					else {					
						this.properties.push([pair.popular(), name, attribute, icons, {
							editor: editorGrid,
							propId: key,
							type: pair.type(),
							tooltip: pair.description(),
							renderer: editorRenderer
						}]);
					}
				}

			}).bind(this));
		}

		this.setProperties();
	},
	
	hideMoreAttrs: function(panel) {
		// TODO: Implement the case that the canvas has no attributes
		if (this.properties.length <= 0){ return }
		
		// collapse the "more attr" group
		this.grid.view.toggleGroup(this.grid.view.getGroupId(this.properties[0][0]), false);
		
		// prevent the more attributes pane from closing after a attribute has been edited
		this.grid.view.un("refresh", this.hideMoreAttrs, this);
	},

	setProperties: function() {
		var props = this.popularProperties.concat(this.properties);
		
		this.dataSource.loadData(props);
	}
}
ORYX.Plugins.PropertyWindow = Clazz.extend(ORYX.Plugins.PropertyWindow);



/**
 * Editor for complex type
 * 
 * When starting to edit the editor, it creates a new dialog where new attributes
 * can be specified which generates json out of this and put this 
 * back to the input field.
 * 
 * This is implemented from Kerstin Pfitzner
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
var selectedUser = "";

Ext.form.ComplexListField = function(config, items, key, facade){
    Ext.form.ComplexListField.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
};

/**
 * This is a special trigger field used for complex properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.ComplexListField, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function() {
		var ds = this.grid.getStore();
		ds.commitChanges();
		
		if (ds.getCount() == 0) {
			return "";
		}
		
		var jsonString = "[";
		for (var i = 0; i < ds.getCount(); i++) {
			var data = ds.getAt(i);		
			jsonString += "{";	
			for (var j = 0; j < this.items.length; j++) {
				var key = this.items[j].id();
				jsonString += key + ':' + ("" + data.get(key)).toJSON();
				if (j < (this.items.length - 1)) {
					jsonString += ", ";
				}
			}
			jsonString += "}";
			if (i < (ds.getCount() - 1)) {
				jsonString += ", ";
			}
		}
		jsonString += "]";
		
		jsonString = "{'totalCount':" + ds.getCount().toJSON() + 
			", 'items':" + jsonString + "}";
		return Object.toJSON(jsonString.evalJSON());
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			this.grid.destroy(true);
			delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		var initial = new Hash();
		
		for (var i = 0; i < items.length; i++) {
			var id = items[i].id();
			initial[id] = items[i].value();
		}
		
		var RecordTemplate = Ext.data.Record.create(recordType);
		return new RecordTemplate(initial);
	},
	
	/**
	 * Builds up the column model of the grid. The parent element of the
	 * grid.
	 * 
	 * Sets up the editors for the grid columns depending on the 
	 * type of the items.
	 * 
	 * @param {Object} parent The 
	 */
	
	buildColumnModel: function(parent) {
		var cols = [];
		for (var i = 0; i < this.items.length; i++) {
			var id 		= this.items[i].id();
			var header 	= this.items[i].name();
			var width 	= this.items[i].width();
			var type 	= this.items[i].type();
			var editor;
			
			if (type == ORYX.CONFIG.TYPE_STRING) {
				editor = new Ext.form.TextField({ allowBlank : this.items[i].optional(), width : width, hidden : true});
			} else if (type == ORYX.CONFIG.TYPE_CHOICE) {				
				var items = this.items[i].items();
				var select = ORYX.Editor.graft("http://www.w3.org/1999/xhtml", parent, ['select', {style:'display:none'}]);
				var optionTmpl = new Ext.Template('<option value="{value}">{value}</option>');
				items.each(function(value){ 
					optionTmpl.append(select, {value:value.value()}); 
				});				
				
				editor = new Ext.form.ComboBox({
				    typeAhead: true,
				    triggerAction: 'all',
				    transform: select,
				    lazyRender: true,
				    msgTarget: 'title',
				    width: width,
				    listeners: {
						 'blur':function() {
								var selectedVal = this.getValue();
								var rawValue  = this.getRawValue();
						 	},
						 'select':function(combo,record, index) {
							 var selectedVal = this.getValue();
								var rawValue  = this.getRawValue();
								selectedUser=selectedVal;
						 },
				    }
				});	
				
				
			} else if (type == ORYX.CONFIG.TYPE_BOOLEAN) {
				editor = new Ext.form.Checkbox( { width : width } );
			} else if(type == "runtimecombo") {
				// extended by karthick for facilitating runtime combo values.
					/*var formStore = new Ext.data.Store ({
						autoLoad: true,
						proxy: new Ext.data.HttpProxy({
						url:'/admin/getAllGroups'}),
						reader: new Ext.data.JsonReader({ root: 'result'},[{ name: 'id' }, { name: 'name'}])
					});*/
					 var dynmstore = new Ext.data.JsonStore({
				            fields:[{name: 'id',mapping:'id'},{name: 'name',mapping:'name'}],
							data:{'result':['']},
							root:'result'
				       // sortInfo: {field:'common', direction:'ASC'}
				    });
					// Set the grid Editor
					editor = new Ext.form.ComboBox({
				        store: dynmstore,
				        displayField:'name',
						valueField: 'id',
				        typeAhead: true,
				        mode: 'local',
				        lazyRender: true,
				        value:'name',
				        triggerAction: 'all',
				        selectOnFocus:true,
				        listeners: {
							 'focus':function(comp) {
								 if(selectedUser.toLowerCase() == "humanperformer"){
									 Ext.Ajax.request({
							   				waitMsg: 'Saving changes...',
							   				method: 'GET',
							   				headers: {'Content-Type': 'application/json'},
							   				url:'/admin/getAllUsers',
							   				failure:function(response){
							   					alert(ORYX.I18N.Feedback.failure);
							   				},
							   				success:function(response){
							   					//alert(response.responseText);
							   					var responseObj = Ext.util.JSON.decode(response.responseText);
							   					dynmstore.loadData(responseObj);
							   				}
							   			}); 
								 } else if(selectedUser.toLowerCase() == "potentialowner") {
									 Ext.Ajax.request({
							   				waitMsg: 'Saving changes...',
							   				method: 'GET',
							   				headers: {'Content-Type': 'application/json'},
							   				url:'/admin/getAllGroups',
							   				failure:function(response){
							   					alert(ORYX.I18N.Feedback.failure);
							   				},
							   				success:function(response){
							   					//alert(response.responseText);
							   					var responseObj = Ext.util.JSON.decode(response.responseText);
							   					dynmstore.loadData(responseObj);
							   				}
							   			}); 
								 }
							 }
					    }
				    });
			}
					
			cols.push({
				id: 		id,
				header: 	header,
				dataIndex: 	id,
				resizable: 	true,
				editor: 	editor,
				width:		width
	        });
			
		}
		return new Ext.grid.ColumnModel(cols);
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

		var state = this.grid.getView().getScrollState();
		
		var col = option.column;
		var row = option.row;
		var editId = this.grid.getColumnModel().config[col].id;
		// check if there is an item in the row, that disables this cell
		for (var i = 0; i < this.items.length; i++) {
			// check each item that defines a "disable" property
			var item = this.items[i];
			var disables = item.disable();
			if (disables != undefined) {
				
				// check if the value of the column of this item in this row is equal to a disabling value
				var value = this.grid.getStore().getAt(row).get(item.id());
				for (var j = 0; j < disables.length; j++) {
					var disable = disables[j];
					if (disable.value == value) {
						
						for (var k = 0; k < disable.items.length; k++) {
							// check if this value disables the cell to select 
							// (id is equals to the id of the column to edit)
							var disItem = disable.items[k];
							if (disItem == editId) {
								this.grid.getColumnModel().getCellEditor(col, row).disable();
								return;
							}
						}
					}
				}		
			}
		}
		this.grid.getColumnModel().getCellEditor(col, row).enable();
		//this.grid.getView().restoreScroll(state);
	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		//if(!this.dialog) { 
		
			var dialogWidth = 0;
			var recordType 	= [];
			
			for (var i = 0; i < this.items.length; i++) {
				var id 		= this.items[i].id();
				var width 	= this.items[i].width();
				var type 	= this.items[i].type();	
					
				if (type == ORYX.CONFIG.TYPE_CHOICE) {
					type = ORYX.CONFIG.TYPE_STRING;
				}
						
				dialogWidth += width;
				recordType[i] = {name:id, type:type};
			}			
			
			if (dialogWidth > 800) {
				dialogWidth = 800;
			}
			dialogWidth += 22;
			
			var data = this.data;
			if (data == "") {
				// empty string can not be parsed
				data = "{}";
			}
			
			
			var ds = new Ext.data.Store({
		        proxy: new Ext.data.MemoryProxy(eval("(" + data + ")")),				
				reader: new Ext.data.JsonReader({
		            root: 'items',
		            totalProperty: 'totalCount'
		        	}, recordType)
	        });
			ds.load();
					
				
			var cm = this.buildColumnModel();
			
			this.grid = new Ext.grid.EditorGridPanel({
				store:		ds,
		        cm:			cm,
				stripeRows: true,
				clicksToEdit : 1,
				autoHeight:true,
		        selModel: 	new Ext.grid.CellSelectionModel()
		    });	
			
									
			//var gridHead = this.grid.getView().getHeaderPanel(true);
			var toolbar = new Ext.Toolbar(
			[{
				text: ORYX.I18N.PropertyWindow.add,
				handler: function(){
					var ds = this.grid.getStore();
					var index = ds.getCount();
					this.grid.stopEditing();
					var p = this.buildInitial(recordType, this.items);
					ds.insert(index, p);
					ds.commitChanges();
					this.grid.startEditing(index, 0);
				}.bind(this)
			},{
				text: ORYX.I18N.PropertyWindow.rem,
		        handler : function(){
					var ds = this.grid.getStore();
					var selection = this.grid.getSelectionModel().getSelectedCell();
					if (selection == undefined) {
						return;
					}
					this.grid.getSelectionModel().clearSelections();
		            this.grid.stopEditing();					
					var record = ds.getAt(selection[0]);
					ds.remove(record);
					ds.commitChanges();           
				}.bind(this)
			}]);			
		
			// Basic Dialog
			this.dialog = new Ext.Window({ 
				autoScroll: true,
				autoCreate: true, 
				title: ORYX.I18N.PropertyWindow.complex, 
				height: 350, 
				width: dialogWidth, 
				modal:true,
				collapsible:false,
				fixedcenter: true, 
				shadow:true, 
				proxyDrag: true,
				keys:[{
					key: 27,
					fn: function(){
						this.dialog.hide
					}.bind(this)
				}],
				items:[toolbar, this.grid],
				bodyStyle:"background-color:#FFFFFF",
				buttons: [{
	                text: ORYX.I18N.PropertyWindow.ok,
	                handler: function(){
	                    this.grid.stopEditing();	
						// store dialog input
						this.data = this.buildValue();
						this.dialog.hide()
	                }.bind(this)
	            }, {
	                text: ORYX.I18N.PropertyWindow.cancel,
	                handler: function(){
	                	this.dialog.hide()
	                }.bind(this)
	            }]
			});		
				
			this.dialog.on(Ext.apply({}, this.dialogListeners, {
	       		scope:this
	        }));
		
			this.dialog.show();	
		
	
			this.grid.on('beforeedit', 	this.beforeEdit, 	this, true);
			this.grid.on('afteredit', 	this.afterEdit, 	this, true);
			
			this.grid.render();			
	    
		/*} else {
			this.dialog.show();		
		}*/
		
	}
});



Ext.form.ComplexTextField = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
		
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }       
		var grid = new Ext.form.TextArea({
	        anchor		: '100% 100%',
			value		: this.value,
			listeners	: {
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		})
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.text, 
			height		: 500, 
			width		: 500, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store dialog input
					var value = grid.getValue();
					this.setValue(value);
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
					this.setValue(this.value);
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
		
	}
});

Ext.form.OnRead = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
		
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }       
		var grid = new Ext.form.TextArea({
	        anchor		: '100% 100%',
			value		: this.value,
			listeners	: {
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		})
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.onRead, 
			height		: 250, 
			width		: 250, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store dialog input
					var value = grid.getValue();
					this.setValue(value);
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
					this.setValue(this.value);
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
		
	}
});

Ext.form.OnCreate = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
		
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }       
		var grid = new Ext.form.TextArea({
	        anchor		: '100% 100%',
			value		: this.value,
			listeners	: {
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		})
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.onCreate, 
			height		: 250, 
			width		: 250, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store dialog input
					var value = grid.getValue();
					this.setValue(value);
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
					this.setValue(this.value);
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
		
	}
});

Ext.form.OnUpdate = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
		
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }       
		var grid = new Ext.form.TextArea({
	        anchor		: '100% 100%',
			value		: this.value,
			listeners	: {
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		})
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.onUpdate, 
			height		: 250, 
			width		: 250, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store dialog input
					var value = grid.getValue();
					this.setValue(value);
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
					this.setValue(this.value);
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
		
	}
});

/**
 * Create Custom Operation
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
Ext.form.CreateCustomOperation = function(config, items, key, facade,grid){
    Ext.form.CreateCustomOperation.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for Operation Permission view properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.CreateCustomOperation, Ext.form.TriggerField,  {
   /**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
    readOnly:		true,
    emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function() {
		return message;
	},
		
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
		
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
		
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
		
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
		
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
		
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
		
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
			
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
		
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		
		var dialogWidth = 0;
		var recordType 	= [];

		var createCustomOperationPanel = new Ext.Panel({
			id: 'createCustomOperation',
			bodyBorder: false,
			layout:'absolute',
			defaultType: 'textfield',
			border: false,
			height:383,
			width:536,
			//plain:true,
			defaults:{autoScroll: true},
			items: [{
	            x: 0,
	            y: 5,
	            xtype:'label',
	            text: 'Send To:'
	        },{
	            x: 60,
	            y: 0,
	            name: 'to',
	            anchor:'100%'  // anchor width by percentage
	        },{
	            x: 0,
	            y: 35,
	            xtype:'label',
	            text: 'Subject:'
	        },{
	            x: 60,
	            y: 30,
	            name: 'subject',
	            anchor: '100%'  // anchor width by percentage
	        },{
	            x:0,
	            y: 60,
	            xtype: 'textarea',
	            name: 'msg',
	            anchor: '100% 100%'  // anchor width and height
	        }]
		});

		// Basic Dialog
		this.dialog = new Ext.Window({ 

			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.operationPermission,
			height: 450, 
			width: 550, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
			bodyBorder: false,
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[createCustomOperationPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					this.data = this.buildValue();
					this.dialog.hide()
				}.bind(this)
				}, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
				}]
		});		
				
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));
	
		this.dialog.show();	
	}
});


/**
 * End Node Script
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
Ext.form.EndNodeScript = function(config, items, key, facade,grid){
    Ext.form.EndNodeScript.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for Operation Permission view properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.EndNodeScript, Ext.form.TriggerField,  {
   /**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
    readOnly:		true,
    emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType) {
		var jsonString = createEndScriptJson(resourceType);
		return jsonString;
	},
		
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
		
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
		
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
		
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
		
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
		
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
		
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
			
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
		
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];
		
		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
		var resourceValue = record.get("value");
		var permissionType = "endScript";
		
		if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,permissionType,"");
		}else{
			clearObjects(resourceValue,resourceType,permissionType);
		}

		var endScriptTabPanel = new Ext.Panel({
			id: 'endscript',
			bodyBorder: false,
			//layout:'absolute',
			//defaultType: 'textfield',
			border: false,
			height:363,
			width:426,
			//plain:true,
			defaults:{autoScroll: true},
			items: [{
	            id: 'end_script_panel',
				html: "<div id='endScriptTabPanel' class='padding10 overflow'></div>",
				bodyBorder: false,
				border: false,
				autoScroll: true,
				listeners: {
					'render': function () {
						setTimeout(function(){loadEndScriptHtml(resourceType)},400);
					}
				}
	        }]
		});


		// Basic Dialog
		this.dialog = new Ext.Window({ 

			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.endScript,
			height: 450, 
			width: 450, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
			bodyBorder: false,
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[endScriptTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					this.data = this.buildValue(resourceType);
					this.dialog.hide()
				}.bind(this)
				}, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
				}]
		});		
				
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));
	
		this.dialog.show();	
	}
});

/**
 * Start Node Script
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
Ext.form.StartNodeScript = function(config, items, key, facade,grid){
    Ext.form.StartNodeScript.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for Operation Permission view properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.StartNodeScript, Ext.form.TriggerField,  {
   /**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
    readOnly:		true,
    emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType) {
		var jsonString = createStartScriptJson(resourceType);
		return jsonString;
	},
		
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
		
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
		
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
		
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
		
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
		
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
		
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
			
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
		
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];
		
		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
		var resourceValue = record.get("value");
		var permissionType = "startScript";
		
		if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,permissionType,"");
		}else{
			clearObjects(resourceValue,resourceType,permissionType);
		}
		
		var startScriptTabPanel = new Ext.Panel({
			id: 'startscript',
			bodyBorder: false,
			//layout:'absolute',
			//defaultType: 'textfield',
			border: false,
			height:363,
			width:426,
			//plain:true,
			defaults:{autoScroll: true},
			items: [{
	            id: 'start_script_panel',
				html: "<div id='startScriptTabPanel' class='padding10 overflow'></div>",
				bodyBorder: false,
				border: false,
				autoScroll: true,
				listeners: {
					'render': function () {
						setTimeout(function(){loadStartScriptHtml(resourceType)},400);
					}
				}
	        }]
		});


		// Basic Dialog
		this.dialog = new Ext.Window({ 

			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.startScript,
			height: 450, 
			width: 450, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
			bodyBorder: false,
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[startScriptTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					this.data = this.buildValue(resourceType);
					this.dialog.hide()
				}.bind(this)
				}, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
				}]
		});		
				
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));
	
		this.dialog.show();	
	}
});

/**
 * Operation Permission view
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
Ext.form.OperationPermission = function(config, items, key, facade,grid){
    Ext.form.OperationPermission.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for Operation Permission view properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.OperationPermission, Ext.form.TriggerField,  {
   /**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
    readOnly:		true,
    emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType) {
		var operationPermissionBuild = "["+getOperationResourceStoreDataAsString(resourceType)+"]";

		return operationPermissionBuild;
	},
		
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
		
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
		
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
		
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
		
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
		
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
		
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
			
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
		
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];

		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
		var resourceValue = record.get("value");

		var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
		
		if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,"operationPermission","");
		}else{
			clearObjects(resourceValue,resourceType,"operationPermission");
		}

		var chooseFormStore = new Ext.data.Store ({
			autoLoad: true,
			proxy: new Ext.data.HttpProxy({
			url:"/bpm/customOperating/getAllCustomOperations"}),
			reader: new Ext.data.JsonReader({ root: 'customOperations'},[{ name: 'id' }, { name: 'functionName'}])
		});

		var customOperationCombo = new Ext.form.ComboBox({
			store: chooseFormStore,
			displayField:'functionName',
			valueField: 'id',
			lazyRender: true,
			typeAhead: true,
			mode: 'local',
			forceSelection: true,
			triggerAction: 'all',
			emptyText:'Add custom operation...',
			selectOnFocus:true,
			listeners: {
				'select': function(combo, row, index) {
					addCustomOperationResources(this.getValue(),this.getRawValue(),resourceType);
			    }
			}
		});
		
		var operationTabPanel = new Ext.Panel({
			id: 'operationTabPanel',
			bodyBorder: false,
			border: false,
			height:333,
			width:436,
			//plain:true,
			defaults:{autoScroll: true},
			items:[{
				id: 'operation_permission',
				html: "<div id='definedOperationPermission' class='padding5'></div><div style='float:right;padding-right:5px;'>&nbsp;</div>",
				bodyBorder: false,
				border: false,
				autoScroll: true,
				width: 436,
				height: 125,
				listeners: {
					'render': function () {
						setTimeout(function(){loadDefinedOperationList(resourceType)},400);
					}
				}
			},{
				id: 'custom_operation',
				bodyBorder: false,
				autoScroll: true,
				border: false,
				width: 436,
				height: 205,
				items:[{
					id:'customPermissionItem',
					items:[customOperationCombo],
					bodyBorder: false,
					border: false,
					autoScroll: true,
					bodyStyle: 'padding-left:120px',
					width: 436,
					height:24
				},{
					id: 'custom_operation_permission',
					html: "<div id='customOperationPermission' class='padding5 overflow'></div>",
					bodyBorder: false,
					border: false,
					autoScroll: true,
					width: 436,
					height:149
				}]
			}]
		});


		// Basic Dialog
		this.dialog = new Ext.Window({ 

			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.operationPermission,
			height: 414, 
			width: 450, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
			bodyBorder: false,
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[operationTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					this.data = this.buildValue(resourceType);
					this.dialog.hide()
				}.bind(this)
				}, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
				}]
		});		
				
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));
	
		this.dialog.show();	
	}
});


Ext.form.MemberPermissionEnd = function(config, items, key, facade,grid){
    Ext.form.MemberPermissionEnd.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.MemberPermissionEnd, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType) {
		
		var consJsonContent = "${";
		
		var memberContent = "'members':["+getResourceStoreDataAsString(resourceType)+"]";
		consJsonContent = consJsonContent +""+memberContent +"}";
			
		return consJsonContent;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];

		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
    	
    	var recordForm = this.grid.store.getAt(1);
		var resourceTypeForm = recordForm.get("value");
		var paramFormId = "";
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(2);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}
		
    	if(resourceType == ORYX.I18N.PropertyWindow.admin || resourceType == ORYX.I18N.PropertyWindow.reader){
			permissionType = "membersPermissionEnd";
		}else{
			permissionType = "membersFields";
		}
    	
    	if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,permissionType,paramFormId);
		}else{
			clearObjects(resourceValue,resourceType,permissionType);
		}
    	
		var  treePanelContentHTML = '<div id="resources_select" class="pad-T5 pad-L40">';
	    	 treePanelContentHTML += '<select id = "resources_options" name = "resources_options" onchange="loadTree(null,'+"'fieldPermission'"+",'"+resourceType+"'"+')" class="fontSize13">'
			 treePanelContentHTML += '<option value="none"> </option>';
	    	 if(resourceType == ORYX.I18N.PropertyWindow.reader){
				treePanelContentHTML +=  '<option value="potentialOwner">Group</option>';
	      		treePanelContentHTML +=  '<option value="potentialOwner">Role</option>';
				treePanelContentHTML +=  '<option value="potentialOwner">Department</option>';
	    	 }
	    	 treePanelContentHTML +=  '<option value="humanPerformer" selected="selected">User</option>';
	    	 treePanelContentHTML +=  '</select>';
	    	 treePanelContentHTML +=  '</div><div id="tree2" class="pad-L40 pad-T10"></div>';

		var treePanel = new Ext.Panel({
			title: ORYX.I18N.PropertyWindow.cts,
			layout: 'table',
			width: 447,
			height: 300,
			header:false,
			items: [{
				    id: 'tree_select',
				    html: treePanelContentHTML,
				    bodyBorder: false,
				    autoScroll: true,
				    width: 225,
				    height: 300,
				    listeners: {
						'render':function() {
							loadTree("humanPerformer","fieldPermission",resourceType);
						},
					}
				}, {
				    region: 'east',
				    id: 'details-panel',
				    autoScroll: true,
				    collapsible: false,
				    split: true,
				    width: 220,
				    height: 300,
				    html:'<div class="padding10" id="userInfo_"></div><div class="padding10 " id="resource_order"></div>',
				}]
		});
		
		var permissionTabPanel = new Ext.TabPanel({
			renderTo: document.body,
			activeTab: 0,
			height:335,
			width:450,
			//plain:true,
			defaults:{autoScroll: true},
			items:[{
				title: ORYX.I18N.PropertyWindow.members,
				items:[treePanel],
			}]
		});


		// Basic Dialog
		this.dialog = new Ext.Window({ 
			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.premission,
			height: 420, 
			width: 464, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
		
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[permissionTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					
					this.data = this.buildValue(resourceType);
					this.dialog.hide()
					
				}.bind(this)
				}, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
				}]
		});		
		
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));

		this.dialog.show();	
	}
});

Ext.form.StartPermission = function(config, items, key, facade,grid){
    Ext.form.StartPermission.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.StartPermission, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType,paramFormId) {
		var consJsonContent = "${";
		consJsonContent += "'startPermission':"+generateStartPermissionJSON(resourceType,paramFormId)+"";
		consJsonContent += ","+generateStartDynamicTransactorJSON(resourceType,paramFormId)+",'previousFormId' : '"+paramFormId+"'}";
			
		return consJsonContent;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){if(this.disabled){
        return;
    }	
    if(this.dialog){
   	 	return;
    }
	var dialogWidth = 0;
	var recordType 	= [];

	var rowCol = this.grid.getSelectionModel().getSelectedCell();
	var record = this.grid.store.getAt(rowCol[0]);
	var resourceType = record.get("name");
	var resourceValue = record.get("value");
	var fieldResource = getExtjsJsonObjWithName(resourceType);
	
	var recordForm = this.grid.store.getAt(1);
	var resourceTypeForm = recordForm.get("value");
	var paramFormId = "";
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(2);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}
	
	if(resourceValue != ""){
		reInitializeObjectObj(resourceValue,resourceType,"startPermission",paramFormId);
	}else{
		clearObjects(resourceValue,resourceType,"startPermission");
	}

	/*var recordForm = this.grid.store.getAt(1);
	var resourceTypeForm = recordForm.get("value");
	var paramFormId = resourceTypeForm;
    	
		if(resourceValue != ""){
		reInitializeObjectObj(resourceValue,resourceType,permissionType,paramFormId);
	}*/
	
	
		var permissionTabPanel = new Ext.TabPanel({
			renderTo: document.body,
			activeTab: 0,
			height:329,
			width:450,
			//plain:true,
			defaults:{autoScroll: true},
			items:[{
				title: ORYX.I18N.PropertyWindow.premission,
				html: '<div id="startFieldPermissionDiv" class="padding10"></div>',
				listeners:{
					activate : function(){
						setStartPropertyFieldPermission(fieldResource, paramFormId, resourceType,resourceValue);
					}
				}
			},{
				title: ORYX.I18N.PropertyWindow.dynamicTransactor,
				html: '<div id="startDynamicTransactorDiv" class="padding10"></div>',
				listeners:{
					activate : function(){
						setStartDynamicTransactorContent(paramFormId,resourceType,resourceValue);
					}
				
				}
			}]
	});
	
	
	// Basic Dialog
	this.dialog = new Ext.Window({ 
		autoScroll: true,
		autoCreate: true, 
		title: ORYX.I18N.PropertyWindow.premission,
		height: 396, 
		width: 464, 
		modal:true,
		collapsible:false,
		fixedcenter: true, 
		shadow:true, 
		proxyDrag: true,
	
		keys:[{
			key: 27,
			fn: function(){
				this.dialog.hide
			}.bind(this)
		}],
		items:[permissionTabPanel],
		bodyStyle:"background-color:#FFFFFF",
		buttons: [{
			text: ORYX.I18N.PropertyWindow.ok,
			handler: function(){
	  			// this.grid.stopEditing();	
				// store dialog input
				
				this.data = this.buildValue(resourceType,paramFormId);
				this.dialog.hide()
				
			}.bind(this)
			}, {
			text: ORYX.I18N.PropertyWindow.cancel,
			handler: function(){
				this.dialog.hide()
			}.bind(this)
			}]
	});		
	
	this.dialog.on(Ext.apply({}, this.dialogListeners, {
   		scope:this
    }));

	if(resourceType == ORYX.I18N.PropertyWindow.startPermission){
		this.dialog.show();
	}else{
		if(paramFormId == null || paramFormId == "" || paramFormId == undefined){
			Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.formError);	
		}else{
			this.dialog.show();
		}
	}
}
});

Ext.form.MemberPermissionProcess = function(config, items, key, facade,grid){
    Ext.form.MemberPermissionProcess.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.MemberPermissionProcess, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType,paramFormId) {
		var consJsonContent = "${";
		
		var memberContent = "'processMembers':["+getResourceStoreDataAsStringForProcess(resourceType)+"]";
		if(resourceType == ORYX.I18N.PropertyWindow.process){
			consJsonContent = consJsonContent +""+memberContent +"}";
		}else{
			consJsonContent = consJsonContent +""+memberContent +","+generatedynamicTransactorJSON(resourceType,paramFormId)+",'previousFormId' : '"+paramFormId+"'}";
		}
		
			
		return consJsonContent;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];

		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
    	
    	var recordForm = this.grid.store.getAt(1);
		var resourceTypeForm = recordForm.get("value");
		var paramFormId = "";
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(2);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}
    	
    		if(resourceType == ORYX.I18N.PropertyWindow.process){
			permissionType = "membersPermissionprocess";
		}
    	
    	if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,permissionType,paramFormId);
		}else{
			clearObjects(resourceValue,resourceType,permissionType);
		}
    	
		var  treePanelContentHTML = '<div id="resources_select" class="pad-T5 pad-L40">';
	    	 treePanelContentHTML += '<select id = "resources_options" name = "resources_options" onchange="loadTree(null,'+"'fieldPermission'"+",'"+resourceType+"'"+')" class="fontSize13">'
			 treePanelContentHTML += '<option value="none"> </option>';
	    	 //if(resourceType == "Reader"){
				treePanelContentHTML +=  '<option value="potentialOwner">Group</option>';
	      		treePanelContentHTML +=  '<option value="potentialOwner">Role</option>';
				treePanelContentHTML +=  '<option value="potentialOwner">Department</option>';
	    	// }
	    	 treePanelContentHTML +=  '<option value="humanPerformer" selected="selected">User</option>';
	    	 treePanelContentHTML +=  '</select>';
	    	 treePanelContentHTML +=  '</div><div id="tree2" class="pad-L40 pad-T10"></div>';
		var treePanel = new Ext.Panel({
			title: ORYX.I18N.PropertyWindow.cts,
			layout: 'table',
			width: 447,
			height: 300,
			header:false,
			items: [{
				    id: 'tree_select',
				    html: treePanelContentHTML,
				    bodyBorder: false,
				    autoScroll: true,
				    width: 225,
				    height: 300,
				    listeners: {
						'render':function() {
							loadTree("humanPerformer","fieldPermission",resourceType);
						},
					}
				}, {
				    region: 'east',
				    id: 'details-panel',
				    autoScroll: true,
				    collapsible: false,
				    split: true,
				    width: 220,
				    height: 300,
				    html:'<div class="padding10" id="userInfo_"></div><div class="padding10 " id="resource_order"></div>',
				}]
		});
		var permissionTabPanel = "";
		if(resourceType == ORYX.I18N.PropertyWindow.process){
			permissionTabPanel = new Ext.TabPanel({
				renderTo: document.body,
				activeTab: 0,
				height:335,
				width:450,
				//plain:true,
				defaults:{autoScroll: true},
				items:[{
					title: 'Members',
					items:[treePanel],
				}]
			});
		}else{
			permissionTabPanel = new Ext.TabPanel({
				renderTo: document.body,
				activeTab: 0,
				height:335,
				width:450,
				//plain:true,
				defaults:{autoScroll: true},
				items:[{
					title: ORYX.I18N.PropertyWindow.members,
					items:[treePanel],
				},{
					title: ORYX.I18N.PropertyWindow.dynamicTransactor,
					html: '<div id="dynamicTransactorDiv" class="padding10"></div>',
					listeners:{
						activate : function(){
							setDynamicTransactorContent(paramFormId,resourceType,resourceValue);
						}
					
					}
				}]
			});
		}
		
		// Basic Dialog
		this.dialog = new Ext.Window({ 
			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.premission,
			height: 420, 
			width: 464, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
		
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[permissionTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					
					this.data = this.buildValue(resourceType,paramFormId);
					this.dialog.hide()
					
				}.bind(this)
				}, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
				}]
		});		
		
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));

		if(resourceType == ORYX.I18N.PropertyWindow.process){
			this.dialog.show();
		}else{
			if(paramFormId == null || paramFormId == "" || paramFormId == undefined){
				Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.formError);	
			}else{
				this.dialog.show();
			}
		}
	}
});

Ext.form.MemberPermission = function(config, items, key, facade,grid){
    Ext.form.MemberPermission.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.MemberPermission, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType,paramFormId) {
		var consJsonContent = "${";
		
		var memberContent = "'members':["+getResourceStoreDataAsString(resourceType)+"]";
		if(resourceType == ORYX.I18N.PropertyWindow.admin){
			consJsonContent = consJsonContent +""+memberContent +"}";
		}else{
			consJsonContent = consJsonContent +""+memberContent +","+generatedynamicTransactorJSON(resourceType,paramFormId)+",'previousFormId' : '"+paramFormId+"'}";
		}
		
			
		return consJsonContent;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];

		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
    	
    	var recordForm = this.grid.store.getAt(1);
		var resourceTypeForm = recordForm.get("value");
		var paramFormId = "";
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(2);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}
    	
    	if(resourceType == ORYX.I18N.PropertyWindow.admin || resourceType == ORYX.I18N.PropertyWindow.reader){
			permissionType = "membersPermission";
		}else{
			permissionType = "membersFields";
		}
    	
    	if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,permissionType,paramFormId);
		}else{
			clearObjects(resourceValue,resourceType,permissionType);
		}
    	
		var  treePanelContentHTML = '<div id="resources_select" class="pad-T5 pad-L40">';
	    	 treePanelContentHTML += '<select id = "resources_options" name = "resources_options" onchange="loadTree(null,'+"'fieldPermission'"+",'"+resourceType+"'"+')" class="fontSize13">'
			 treePanelContentHTML += '<option value="none"> </option>';
	    	 //if(resourceType == "Reader"){
				treePanelContentHTML +=  '<option value="potentialOwner">Group</option>';
	      		treePanelContentHTML +=  '<option value="potentialOwner">Role</option>';
				treePanelContentHTML +=  '<option value="potentialOwner">Department</option>';
	    	// }
	    	 treePanelContentHTML +=  '<option value="humanPerformer" selected="selected">User</option>';
	    	 treePanelContentHTML +=  '</select>';
	    	 treePanelContentHTML +=  '</div><div id="tree2" class="pad-L40 pad-T10"></div>';
		var treePanel = new Ext.Panel({
			title: ORYX.I18N.PropertyWindow.cts,
			layout: 'table',
			width: 447,
			height: 300,
			header:false,
			items: [{
				    id: 'tree_select',
				    html: treePanelContentHTML,
				    bodyBorder: false,
				    autoScroll: true,
				    width: 225,
				    height: 300,
				    listeners: {
						'render':function() {
							loadTree("humanPerformer","fieldPermission",resourceType);
						},
					}
				}, {
				    region: 'east',
				    id: 'details-panel',
				    autoScroll: true,
				    collapsible: false,
				    split: true,
				    width: 220,
				    height: 300,
				    html:'<div class="padding10" id="userInfo_"></div><div class="padding10 " id="resource_order"></div>',
				}]
		});
		var permissionTabPanel = "";
		if(resourceType == ORYX.I18N.PropertyWindow.admin){
			permissionTabPanel = new Ext.TabPanel({
				renderTo: document.body,
				activeTab: 0,
				height:335,
				width:450,
				//plain:true,
				defaults:{autoScroll: true},
				items:[{
					title: ORYX.I18N.PropertyWindow.members,
					items:[treePanel],
				}]
			});
		}else{
			permissionTabPanel = new Ext.TabPanel({
				renderTo: document.body,
				activeTab: 0,
				height:335,
				width:450,
				//plain:true,
				defaults:{autoScroll: true},
				items:[{
					title: ORYX.I18N.PropertyWindow.members,
					items:[treePanel],
				},{
					title: ORYX.I18N.PropertyWindow.dynamicTransactor,
					html: '<div id="dynamicTransactorDiv" class="padding10"></div>',
					listeners:{
						activate : function(){
							setDynamicTransactorContent(paramFormId,resourceType,resourceValue);
						}
					
					}
				}]
			});
		}
		
		// Basic Dialog
		this.dialog = new Ext.Window({ 
			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.premission,
			height: 420, 
			width: 464, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
		
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[permissionTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					
					this.data = this.buildValue(resourceType,paramFormId);
					this.dialog.hide()
					
				}.bind(this)
				}, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
				}]
		});		
		
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));

		if(resourceType == ORYX.I18N.PropertyWindow.admin){
			this.dialog.show();
		}else{
			if(paramFormId == null || paramFormId == "" || paramFormId == undefined){
				Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.formError);	
			}else{
				this.dialog.show();
			}
		}
	}
});


Ext.form.UrgeMessage = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
		
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }       
		var grid = new Ext.form.TextArea({
	        anchor		: '100% 100%',
			value		: this.value,
			listeners	: {
				render : function(){
					if(this.value.length == 0){
						grid.setValue("There is a workflow you need to sign off, workflow name is <workFolwName>, created by <createdBy> at <createdAt>, plz click this link <url> to operate, thank you.");
					}
				},
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		})
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.urgeMessage, 
			height		: 250, 
			width		: 250, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						this.setValue("There is a workflow you need to sign off, workflow name is <workFolwName>, created by <createdBy> at <createdAt>, plz click this link <url> to operate, thank you.");
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store dialog input
					var value = grid.getValue();
					this.setValue(value);
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
			if(this.value.length > 0){
				this.setValue(this.value);
			}else{
				this.setValue("There is a workflow you need to sign off, workflow name is <workFolwName>, created by <createdBy> at <createdAt>, plz click this link <url> to operate, thank you.");
			}
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
		
	}
});

Ext.form.NotificationMessage = Ext.extend(Ext.form.TriggerField,  {

	defaultAutoCreate : {tag: "textarea", rows:1, style:"height:16px;overflow:hidden;" },

    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
		
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }       
		var grid = new Ext.form.TextArea({
	        anchor		: '100% 100%',
			value		: this.value,
			listeners	: {
				render : function(){
					if(this.value.length == 0){
						grid.setValue("There is a workflow need to sign off by organizer : <organizerName>  Workflow name is <workFolwName>");
						
					}
				},
				focus: function(){
					this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
				}.bind(this)
			}
		})
		
		
		// Basic Dialog
		var dialog = new Ext.Window({ 
			layout		: 'anchor',
			autoCreate	: true, 
			title		: ORYX.I18N.PropertyWindow.notificationMessage, 
			height		: 250, 
			width		: 250, 
			modal		: true,
			collapsible	: false,
			fixedcenter	: true, 
			shadow		: true, 
			proxyDrag	: true,
			keys:[{
				key	: 27,
				fn	: function(){
						this.setValue("There is a workflow need to sign off by organizer : <organizerName>  Workflow name is <workFolwName>");
						dialog.hide()
				}.bind(this)
			}],
			items		:[grid],
			listeners	:{
				hide: function(){
					this.fireEvent('dialogClosed', this.value);
					//this.focus.defer(10, this);
					dialog.destroy();
				}.bind(this)				
			},
			buttons		: [{
                text: ORYX.I18N.PropertyWindow.ok,
                handler: function(){	 
					// store dialog input
					var value = grid.getValue();
					if(value != ""){
						this.setValue(value);
					}else{
						this.setValue('Organizer not yet set the task');
					}
					
					this.dataSource.getAt(this.row).set('value', value)
					this.dataSource.commitChanges()

					dialog.hide()
                }.bind(this)
            }, {
                text: ORYX.I18N.PropertyWindow.cancel,
                handler: function(){
				if(this.value.length > 0){
					this.setValue(this.value);
				}else{
					this.setValue("There is a workflow need to sign off by organizer : <organizerName>  Workflow name is <workFolwName>");
				}
                	dialog.hide()
                }.bind(this)
            }]
		});		
				
		dialog.show();		
		grid.render();

		this.grid.stopEditing();
		grid.focus( false, 100 );
		
	}
});


/**
 * 
 */
Ext.form.FormMemberPermission = function(config, items, key, facade,grid){
    Ext.form.FormMemberPermission.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.FormMemberPermission, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	
	fieldPermissionCheck: function (fieldId){
		var fieldPermission  = document.getElementById(fieldId).checked;
		if(fieldPermission == true){
			return 1;
		}else{
			return 0;
		}		
	},
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType,paramFormId) {
		var consJsonContent = "${"+generateFieldPermissionJSON(resourceType,paramFormId);
		var memberContent = ",'members':["+getResourceStoreDataAsString(resourceType)+"]";
		consJsonContent = consJsonContent +""+memberContent +","+generatedynamicTransactorJSON(resourceType,paramFormId)+",'previousFormId' : '"+paramFormId+"'}";
			
		return consJsonContent;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];

		//var fieldResponseDataGlob = "";

		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
		var fieldResource = getExtjsJsonObjWithName(resourceType);
		
		var recordForm = this.grid.store.getAt(3);
		var resourceTypeForm = recordForm.get("value");
		var paramFormId = "";
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(4);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}
		
		if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,"membersFields",paramFormId);
		}else{
			clearObjects(resourceValue,resourceType,"membersFields");
		}

		
		if(paramFormId == null || paramFormId == "" || paramFormId == undefined){
			Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.formError);	
		}else{
			var treePanel = new Ext.Panel({
				title: ORYX.I18N.PropertyWindow.cts,
				layout: 'table',
				width: 447,
				height: 300,
				header:false,
				items: [{
					    id: 'tree_select',
					    html: '<div id="resources_select" class="pad-T5 pad-L40">'+
					    	  '<select id = "resources_options" name = "resources_options" onchange="loadTree(null,'+"'fieldPermission',"+"'"+resourceType+"'"+')" class="fontSize13">'+
				          	  '<option value="none"> </option>'+
		              		  '<option value="potentialOwner">Group</option>'+
		              		  '<option value="potentialOwner">Role</option>'+
							  '<option value="potentialOwner">Department</option>'+
				              '<option value="humanPerformer" selected="selected">User</option>'+
				              '</select>'+	
					    	  '</div><div id="tree2" class="pad-L40 pad-T10"></div>',
					    bodyBorder: false,
					    autoScroll: true,
					    width: 225,
					    height: 300,
					    listeners: {
							'render':function() {
								loadTree("humanPerformer","fieldPermission",resourceType);
							},
						}
					}, {
					    region: 'east',
					    id: 'details-panel',
					    autoScroll: true,
					    collapsible: false,
					    split: true,
					    width: 220,
					    height: 300,
					    html:'<div class="padding10" id="userInfo_"></div><div class="padding10 " id="resource_order"></div>',
					}]
			});
		
			var permissionTabPanel = new Ext.TabPanel({
				renderTo: document.body,
				activeTab: 0,
				height:368,
				width:506,
				//plain:true,
				defaults:{autoScroll: true},
				items:[{
					title: ORYX.I18N.PropertyWindow.members,
					items:[treePanel],
				},{
					title: ORYX.I18N.PropertyWindow.premission,
					html: '<div id="fieldPermissionDiv" class="padding10"></div>',
					listeners:{
						activate : function(){
							setFieldPermissionContent(fieldResource,paramFormId,resourceType,resourceValue);
						}
						
					}
				},{
					title: ORYX.I18N.PropertyWindow.dynamicTransactor,
					html: '<div id="dynamicTransactorDiv" class="padding10"></div>',
					listeners:{
						activate : function(){
							setDynamicTransactorContent(paramFormId,resourceType,resourceValue);
						}
						
					}
				}]
			});


			// Basic Dialog
			this.dialog = new Ext.Window({ 
				autoScroll: true,
				autoCreate: true, 
				title: ORYX.I18N.PropertyWindow.premission,
				height: 450, 
				width: 520, 
				modal:true,
				collapsible:false,
				fixedcenter: true, 
				shadow:true, 
				proxyDrag: true,
			
				keys:[{
					key: 27,
					fn: function(){
						this.dialog.hide
					}.bind(this)
				}],
				items:[permissionTabPanel],
				bodyStyle:"background-color:#FFFFFF",
				buttons: [{
					text: ORYX.I18N.PropertyWindow.ok,
					handler: function(){
			  			// this.grid.stopEditing();	
						// store dialog input
						var fieldResource = getExtjsJsonObjWithName(resourceType);
						if(fieldResource.getCount() > 0){
							this.data = this.buildValue(resourceType,paramFormId);
							this.dialog.hide()
						}else{
							var formFieldLength = getFormCountLength(paramFormId);
							if( formFieldLength == 0){
								this.data = this.buildValue(resourceType,paramFormId);
								this.dialog.hide()
							}else{
								Ext.MessageBox.alert('Information', ORYX.I18N.PropertyWindow.permissionError);	
							}
						}
						
					}.bind(this)
					}, {
					text: ORYX.I18N.PropertyWindow.cancel,
					handler: function(){
						this.dialog.hide()
					}.bind(this)
					}]
			});		
			
			this.dialog.on(Ext.apply({}, this.dialogListeners, {
		   		scope:this
		    }));
	
			this.dialog.show();	
		}
	}
});

Ext.form.ProcessTimeSetting = function(config, items, key, facade,grid){
	Ext.form.ProcessTimeSetting.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.ProcessTimeSetting, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(paramFormId) {
		var openionJSON = "${";
		openionJSON += "'processTimeSetting':"+generateTimeSettingJSON()+"}";
		return openionJSON;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];
		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
    	var recordForm = this.grid.store.getAt(0);
	var resourceTypeForm = recordForm.get("value");
	var taskName = resourceTypeForm;
		var opinionTabPanel = new Ext.TabPanel({
			renderTo: document.body,
			activeTab: 0,
			height:330,
			width:580,
			//plain:true,
			defaults:{autoScroll: true},
			items:[{
				title: ORYX.I18N.PropertyWindow.timeSetting+taskName,
				html: '<div id="processTimeSetting"> </div>',
				listeners:{
					activate : function(){
						setProcessTimeSetting(resourceValue,resourceType,taskName);
						
					}
				}
			}]
		});

		// Basic Dialog
		this.dialog = new Ext.Window({ 
			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.processTimeSetting,
			height: 400, 
			width: 600, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
		
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[opinionTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					//if(fieldResponseDataGlob != ""){
					if(validateWarinigDays()){
						this.data = this.buildValue();
						this.dialog.hide()
					}else{
						Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.ptsError);	
					}
					//}else{
					//	Ext.MessageBox.alert('Information','Please set the field permissions');	
					//}
				}.bind(this)
			},{
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
			}]
		});		
		
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));

		this.dialog.show();	
	}
});

Ext.form.FormFieldAutomatic = function(config, items, key, facade,grid){
	Ext.form.FormFieldAutomatic.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.FormFieldAutomatic, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(paramFormId) {
		var openionJSON = "${";
		openionJSON += "'formFieldAutomatic':"+getFormFieldAutomaticJson(paramFormId)+",'previousFormId':'"+paramFormId+"'}";
		return openionJSON;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];
		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
    	var recordForm = this.grid.store.getAt(3);
	var resourceTypeForm = recordForm.get("value");
	var paramFormId = "";
	var previousFormIds = "";
	var children=this.facade.getCanvas().children;
		for(var i=0;i<children.length;i++){
			if(children[i].properties != undefined){
				var  shapesJSON = JSON.parse(children[i].properties.toJSON().replace(/-/g,"$").toString());
				if(shapesJSON.oryx$form != undefined || shapesJSON.oryx$jspform != undefined){
					if(shapesJSON.oryx$form != "" && shapesJSON.oryx$form.length > 0){
						previousFormIds += shapesJSON.oryx$form+",";
					}else{
						previousFormIds += shapesJSON.oryx$jspform+",";
					}
				}
		
			}
		}
		if(previousFormIds != ""){
			var checkLastIndex = previousFormIds;
			previousFormIds = (checkLastIndex.slice(-1) == ",") ? previousFormIds.substring(0, previousFormIds.length - 1) : previousFormIds;
		}
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(4);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}
		var opinionTabPanel = new Ext.TabPanel({
			renderTo: document.body,
			activeTab: 0,
			height:344,
			width:610,
			//plain:true,
			defaults:{autoScroll: true},
			items:[{
				title: ORYX.I18N.PropertyWindow.opinion,
				html: '<div style="margin: 10px;" id="opinionDiv"></div><br/><table id="opinionFormFillTable"  cellpadding="0" cellspacing="0"  width="100%" border="1"><tr><td></td><td><center><span>'+ORYX.I18N.PropertyWindow.fillForm+'</span></center></td><td><center><span>'+ORYX.I18N.PropertyWindow.fillField+'</span></center></td><td><center><span>'+ORYX.I18N.PropertyWindow.fillValue+'</span></center></td><td><center><span>'+ORYX.I18N.PropertyWindow.fillType+'</span></center></td><td style="display:none"><span>From Form</span></td><td style="display:none"><span>From Form Field</span></td><td><center><span>'+ORYX.I18N.PropertyWindow.isSkip+'</span></center></td></tr></table><br/><center><input type="button" class="btn btn-primary" style="font-size:11px;padding:2px 7px;" id="delete_formFieldOpinion" value="Delete" onClick="deleteSelectedFormField()"/>&nbsp&nbsp<input type="button" class="btn btn-primary" style="font-size:11px;padding:2px 7px;" id="delete_allFormFieldOpinion" value="DeleteAll" onClick="deleteAllFormField()"/></center>',
				listeners:{
					activate : function(){
						setFormFieldAutomaticField(resourceValue,resourceType,paramFormId,previousFormIds);
						
					}
				}
			}]
		});

		// Basic Dialog
		this.dialog = new Ext.Window({ 
			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.formFieldAutomatic,
			height: 421, 
			width: 625, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
		
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[opinionTabPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					//if(fieldResponseDataGlob != ""){
						this.data = this.buildValue(paramFormId);
						this.dialog.hide()
					//}else{
					//	Ext.MessageBox.alert('Information','Please set the field permissions');	
					//}
				}.bind(this)
			},{
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
			}]
		});		
		
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
	   		scope:this
	    }));

		this.dialog.show();	
	}
});

/**
 * Form Field Permission and Members
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
Ext.form.FormFieldPermission = function(config, items, key, facade,grid){
    Ext.form.FormFieldPermission.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};


/**
 * This is a special trigger field used for choose condition expression properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.FormFieldPermission, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
	
	
	fieldPermissionCheck: function (fieldId){
		var fieldPermission  = document.getElementById(fieldId).checked;
		if(fieldPermission == true){
			return 1;
		}else{
			return 0;
		}		
	},
	
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(resourceType,paramFormId) {


		var consJsonContent = "${"+generateFieldPermissionJSON(resourceType,paramFormId)+",'previousFormId' : '"+paramFormId+"'}";
		
		//var memberContent = ",'members':["+getResourceStoreDataAsString()+"]";
		//consJsonContent = consJsonContent +""+memberContent +"}";
			
		return consJsonContent;
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];
		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
		var fieldResource = getExtjsJsonObjWithName(resourceType);

		var recordForm = this.grid.store.getAt(3);
		var resourceTypeForm = recordForm.get("value");
		var paramFormId = "";
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(4);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}

		if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,"FieldPermission",paramFormId);
		}else{
			clearObjects(resourceValue,resourceType,"FieldPermission");
		}
		
		if(paramFormId == null || paramFormId == "" || paramFormId == undefined){
			Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.formError);	
		}else{
			
			var permissionTabPanel = new Ext.TabPanel({
				renderTo: document.body,
				activeTab: 0,
				height:329,
				width:450,
				//plain:true,
				defaults:{autoScroll: true},
				items:[{
					title: ORYX.I18N.PropertyWindow.premission,
					html: '<div id="fieldPermissionDiv" class="padding10"></div>',
					listeners:{
						activate : function(){
							setFieldPermissionContent(fieldResource,paramFormId,resourceType,resourceValue);
						}
					}
				}]
			});

			// Basic Dialog
			this.dialog = new Ext.Window({ 
				autoScroll: true,
				autoCreate: true, 
				title: ORYX.I18N.PropertyWindow.premission,
				height: 420, 
				width: 464, 
				modal:true,
				collapsible:false,
				fixedcenter: true, 
				shadow:true, 
				proxyDrag: true,
			
				keys:[{
					key: 27,
					fn: function(){
						this.dialog.hide
					}.bind(this)
				}],
				items:[permissionTabPanel],
				bodyStyle:"background-color:#FFFFFF",
				buttons: [{
					text: ORYX.I18N.PropertyWindow.ok,
					handler: function(){
			  			// this.grid.stopEditing();	
						// store dialog input
						//if(fieldResponseDataGlob != ""){
							this.data = this.buildValue(resourceType,paramFormId);
							this.dialog.hide()
						//}else{
						//	Ext.MessageBox.alert('Information','Please set the field permissions');	
						//}
						
					}.bind(this)
					}, {
					text: ORYX.I18N.PropertyWindow.cancel,
					handler: function(){
						this.dialog.hide()
					}.bind(this)
					}]
			});		
			
			this.dialog.on(Ext.apply({}, this.dialogListeners, {
		   		scope:this
		    }));
	
			this.dialog.show();	
		}
	}
});

/**
 * Custom Condition For Next transactor
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
Ext.form.TransactorRelationship = function(config, items, key, facade, grid){
	Ext.form.TransactorRelationship.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid 	= grid;
};

/**
 * This is a special trigger field used for complex properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.TransactorRelationship, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function() {
		return generateTransactorRelationshipJson();
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;/**
		 * Custom Tree select view
		 * 
		 * @param {Object} config
		 * @param {Object} items
		 * @param {Object} key
		 * @param {Object} facade
		 */
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];

		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
	
		var transactorRelationshipPanel = new Ext.Panel({
			id:'relationship_panel',
		    border: false,
		    align:'middle',
	        bodyStyle:'text-align:center',
		    items: [{
			   		id: 'transactor_relationship_panel',
			   		html: '<div id="transactorRelationshipDiv" align="center"><table id="transactorRelationshipTable" cellpadding="0" cellspacing="0"width="100%" border="0"></table><table id="transactorRelationshipDataTable" cellpadding="0" cellspacing="0" width="100%" border="1"></table></div>',
		            bodyBorder: false,
		            autoScroll: true
			}],
			listeners: {
		        render: function() {
					setTimeout(function(){setTransactorRelationship(resourceValue,resourceType)},200);
				}
			}
		});
		
		// Basic Dialog
		this.dialog = new Ext.Window({
			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.transactorRelationship, 
			height: 350, 
			width: 500, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
			
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[transactorRelationshipPanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					this.data = this.buildValue();
					this.dialog.hide()
				}.bind(this)
			    }, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
			    }]
		});		
		
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
       		scope:this,
        }));
	
		this.dialog.show();	
	}
});

/**
 * Custom Condition For Next transactor
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
Ext.form.ChooseCondition = function(config, items, key, facade, grid){
    Ext.form.ChooseCondition.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid = grid;
};

/**
 * This is a special trigger field used for complex properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.ChooseCondition, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function(fieldResponseData,paramFormId) {
		return generateConitionExpressionJson(paramFormId);
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		//return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			//return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;/**
		 * Custom Tree select view
		 * 
		 * @param {Object} config
		 * @param {Object} items
		 * @param {Object} key
		 * @param {Object} facade
		 */
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
        show : function(){ // retain focus styling
            this.onFocus();	
			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			return;
        },
        hide : function(){

            var dl = this.dialogListeners;
            this.dialog.un("show", dl.show,  this);
            this.dialog.un("hide", dl.hide,  this);
			
			this.dialog.destroy(true);
			//this.grid.destroy(true);
			//delete this.grid;
			delete this.dialog;
			
			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
			
			// store data and notify parent about the closed dialog
			// parent has to handel this event and start editing the text field again
			this.fireEvent('dialogClosed', this.data);
			
			Ext.form.ComplexListField.superclass.setValue.call(this, this.data);
        }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		//option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		var dialogWidth = 0;
		var recordType 	= [];
		
		var rowCol = this.grid.getSelectionModel().getSelectedCell();
    	var record = this.grid.store.getAt(rowCol[0]);
    	var resourceType = record.get("name");
    	var resourceValue = record.get("value");
    	
    	if(resourceValue != ""){
			reInitializeChooseConditionObject(resourceValue);
		}else{
			clearObjects(resourceValue,resourceType,"ChooseCondition");
		}
		var children = this.facade.getCanvas().children;
		var previousFormId = "";
		for(var i=0; i< children.length; i++){
				if(children[i].outgoing[0] != undefined){
				var edges =  this.facade.getCanvas().edges;
				for(var j=0;j<edges.length;j++){
					for(var outGoing = 0; outGoing < children[i].outgoing.length ; outGoing++){
						if(children[i].outgoing[outGoing].id == edges[j].id && conditionExpressionId == edges[j].id){
							var  shapesJSON = JSON.parse(children[i].properties.toJSON().replace(/-/g,"$").toString());
							if(shapesJSON.oryx$form != undefined){
								if(shapesJSON.oryx$form != "" && shapesJSON.oryx$form.length > 0){
									previousFormId = shapesJSON.oryx$form;
								}else{
									previousFormId = shapesJSON.oryx$jspform;
								}
							}else{
								
								var xorJson = JSON.parse(children[i].incoming[0].incoming[0].properties.toJSON().replace(/-/g,"$").toString());						if(xorJson.oryx$form != "" && xorJson.oryx$form.length > 0){
									previousFormId = xorJson.oryx$form;
								}else{
									previousFormId = xorJson.oryx$jspform;
								}
							}
						}
					}
				}
			}
				
		}
		if( previousFormId != ""){
		var chooseFormStore = new Ext.data.Store ({
			autoLoad: false,
			proxy: new Ext.data.HttpProxy({
			url:"/form/getFormById",
			method:'GET'
			}),
			baseParams:{
			formId:previousFormId
			},
			reader: new Ext.data.JsonReader({ root: 'forms'},[{ name: 'id' }, { name: 'formName'}])
		});
		}
		var fieldResponseDataGlob = "";
		var paramFormId = "";
		var conditionTextField = new Ext.form.TextField({ 
			readOnly:true,
			value:'',
			listeners:{
				render: function(conditionTextField){
				if(chooseFormStore != undefined){
					chooseFormStore.load({	
						'callback':function(){
		  					conditionTextField.setValue(chooseFormStore.reader.jsonData.forms[0].formName);
				 		}
					 });
				 }
				 paramFormId = previousFormId;
				 conditionExpression(previousFormId,resourceValue);
					
				}
			}
								 
		}); 
		var combo = new Ext.form.ComboBox({
			store: chooseFormStore,
			displayField:'formName',
			valueField: 'id',
			lazyRender: true,
			typeAhead: true,
			mode: 'local',
			forceSelection: true,
			triggerAction: 'all',
			emptyText:'Select a form...',
			selectOnFocus:true,
			listeners: {
				'select': function(combo, row, index) {
					paramFormId = this.getValue();
					conditionExpression(this.getValue(),resourceValue);
				}
			}
		});
		
		var treeExpressionPanel = new Ext.Panel({
		    border: false,
		    align:'middle',
	        bodyStyle:'text-align:center',
		    items: [{
					id: 'choose_combo',
					bodyStyle:'padding-left:10px;',
					align:'middle',
					items:[conditionTextField],
		            bodyBorder: false,
		            autoScroll: true,
			},{
			   		id: 'choose_condition_html',
		            html: '<div id="chooseContentDiv" align="center"></div><br/><table  id="condition_Expression"  cellpadding="0" cellspacing="0"  width="100%" border="1"><tr><td><span><b>'+ORYX.I18N.PropertyWindow.parameter+'</b></span></td><td><span><b>'+ORYX.I18N.PropertyWindow.relation+'</b></span></td><td><span><b>'+ORYX.I18N.PropertyWindow.value +'</b></span></td><td><span><b>'+ORYX.I18N.PropertyWindow.join1+'</b></span></td><td><span><b>'+ORYX.I18N.PropertyWindow.operation+'</b></span></td></tr></table>',
		            bodyBorder: false,
		            autoScroll: true,
					bodyStyle:'padding:0 40px;',
		            //width: 100,
		            //height: 500	
			}]
		});

		var treeScriptPanel = new Ext.Panel({
		    border: false,
		    align:'middle',
	        bodyStyle:'text-align:center',
		    items: [{
			   		id: 'choose_script_condition_html',
		            html: '<div id="chooseContentDiv" align="center"><textarea id="sequencescriptfunction" name="sequencescriptfunction" style="width:450px;height:220px;" ></textarea></div>',
		            bodyBorder: false,
		            autoScroll: true,
					bodyStyle:'padding-top:5px;',
			}]
		});

		var treePanel = new Ext.Panel({
			id:'condition_panel',
		    border: false,
		    align:'middle',
	        bodyStyle:'text-align:center',
		    items: [{
					id: 'choose_combo',
					html:'<div id="expression_condition_panel"><table style="margin:0 auto" width="28%"><tr><td><input type="radio" id="expScriptExpression" name="expScript" checked="true" onclick="changeSequenceFlow(\'expression\')" value="expression"></td><td><b>Expression</b></td><td><input type="radio" id="expScriptJavascript" name="expScript" onclick="changeSequenceFlow(\'javascript\')" value="Javascript"></td><td><b>Javascript</b></td></div>',
		            bodyBorder: false,
		            autoScroll: true,
					bodyStyle:'padding-bottom:10px;padding-top:5px;',
			},{
			   		id: 'condition_expression_panel',
		            items:[treeExpressionPanel],
		            bodyBorder: false,
		            autoScroll: true
			},{
					id: 'condition_script_panel',
		            items:[treeScriptPanel],
		            bodyBorder: false,
		            autoScroll: true,
			}],
			listeners: {
		        render: function() {
					setTimeout(function(){changeSequenceFlow("expression")},200);
				}
			}
		});
		
		// Basic Dialog
		this.dialog = new Ext.Window({ 
			autoScroll: true,
			autoCreate: true, 
			title: ORYX.I18N.PropertyWindow.chooseCondition, 
			height: 350, 
			width: 600, 
			modal:true,
			collapsible:false,
			fixedcenter: true, 
			shadow:true, 
			proxyDrag: true,
			
			keys:[{
				key: 27,
				fn: function(){
					this.dialog.hide
				}.bind(this)
			}],
			items:[treePanel],
			bodyStyle:"background-color:#FFFFFF",
			buttons: [{
				text: ORYX.I18N.PropertyWindow.ok,
				handler: function(){
		  			// this.grid.stopEditing();	
					// store dialog input
					this.data = this.buildValue(fieldResponseDataGlob,paramFormId);
					this.dialog.hide()
				}.bind(this)
			    }, {
				text: ORYX.I18N.PropertyWindow.cancel,
				handler: function(){
					this.dialog.hide()
				}.bind(this)
			    }]
		});		
		
		this.dialog.on(Ext.apply({}, this.dialogListeners, {
       		scope:this,
        }));
	
		this.dialog.show();	
	}
});

/**
 * Custom Tree select view
 * 
 * @param {Object} config
 * @param {Object} items
 * @param {Object} key
 * @param {Object} facade
 */
var selectedUser = "";

Ext.form.CustomTreeSelect = function(config, items, key, facade,grid){
    Ext.form.CustomTreeSelect.superclass.constructor.call(this, config);
	this.items 	= items;
	this.key 	= key;
	this.facade = facade;
	this.grid = grid;
};

/**
 * This is a special trigger field used for complex properties.
 * The trigger field opens a dialog that shows a list of properties.
 * The entered values will be stored as trigger field value in the JSON format.
 */
Ext.extend(Ext.form.CustomTreeSelect, Ext.form.TriggerField,  {
	/**
     * @cfg {String} triggerClass
     * An additional CSS class used to style the trigger button.  The trigger will always get the
     * class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified.
     */
    triggerClass:	'x-form-complex-trigger',
	readOnly:		true,
	emptyText: 		ORYX.I18N.PropertyWindow.clickIcon,
		
	/**
	 * Builds the JSON value from the data source of the grid in the dialog.
	 */
	buildValue: function() {
		return "";
		//return Object.toJSON(getResourceStoreDataAsString().evalJSON());
	},
	
	/**
	 * Returns the field key.
	 */
	getFieldKey: function() {
		return this.key;
	},
	
	/**
	 * Returns the actual value of the trigger field.
	 * If the table does not contain any values the empty
	 * string will be returned.
	 */
    getValue : function(){
		// return actual value if grid is active
		if (this.grid) {
			return this.buildValue();			
		} else if (this.data == undefined) {
			return "";
		} else {
			return this.data;
		}
    },
	
	/**
	 * Sets the value of the trigger field.
	 * In this case this sets the data that will be shown in
	 * the grid of the dialog.
	 * 
	 * @param {Object} value The value to be set (JSON format or empty string)
	 */
	setValue: function(value) {	
		if (value.length > 0) {
			// set only if this.data not set yet
			// only to initialize the grid
			if (this.data == undefined) {
				this.data = value;
			}
		}
	},
	
	/**
	 * Returns false. In this way key events will not be propagated
	 * to other elements.
	 * 
	 * @param {Object} event The keydown event.
	 */
	keydownHandler: function(event) {
		return false;
	},
	
	/**
	 * The listeners of the dialog. 
	 * 
	 * If the dialog is hidded, a dialogClosed event will be fired.
	 * This has to be used by the parent element of the trigger field
	 * to reenable the trigger field (focus gets lost when entering values
	 * in the dialog).
	 */
    dialogListeners : {
    	  show : function(){ // retain focus styling
              this.onFocus();	
  			this.facade.registerOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
  			this.facade.disableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
  			return;
          },
          hide : function(){

              var dl = this.dialogListeners;
              this.dialog.un("show", dl.show,  this);
              this.dialog.un("hide", dl.hide,  this);
  			
  			this.dialog.destroy(true);
  			//this.grid.destroy(true);
  			//delete this.grid;
  			delete this.dialog;
  			
  			this.facade.unregisterOnEvent(ORYX.CONFIG.EVENT_KEYDOWN, this.keydownHandler.bind(this));
  			this.facade.enableEvent(ORYX.CONFIG.EVENT_KEYDOWN);
  			
  			// store data and notify parent about the closed dialog
  			// parent has to handel this event and start editing the text field again
  			this.fireEvent('dialogClosed', this.data);
  			
  			Ext.form.CustomTreeSelect.superclass.setValue.call(this, this.data);
          }
    },	
	
	/**
	 * Builds up the initial values of the grid.
	 * 
	 * @param {Object} recordType The record type of the grid.
	 * @param {Object} items      The initial items of the grid (columns)
	 */
	buildInitial: function(recordType, items) {
		var initial = new Hash();
		
		for (var i = 0; i < items.length; i++) {
			var id = items[i].id();
			initial[id] = items[i].value();
		}
		
		var RecordTemplate = Ext.data.Record.create(recordType);
		return new RecordTemplate(initial);
	},
	
	/**
	 * After a cell was edited the changes will be commited.
	 * 
	 * @param {Object} option The option that was edited.
	 */
	afterEdit: function(option) {
		option.grid.getStore().commitChanges();
	},
		
	/**
	 * Before a cell is edited it has to be checked if this 
	 * cell is disabled by another cell value. If so, the cell editor will
	 * be disabled.
	 * 
	 * @param {Object} option The option to be edited.
	 */
	beforeEdit: function(option) {

		var state = this.grid.getView().getScrollState();
		
		var col = option.column;
		var row = option.row;
		var editId = this.grid.getColumnModel().config[col].id;
		// check if there is an item in the row, that disables this cell
		for (var i = 0; i < this.items.length; i++) {
			// check each item that defines a "disable" property
			var item = this.items[i];
			var disables = item.disable();
			if (disables != undefined) {
				
				// check if the value of the column of this item in this row is equal to a disabling value
				var value = this.grid.getStore().getAt(row).get(item.id());
				for (var j = 0; j < disables.length; j++) {
					var disable = disables[j];
					if (disable.value == value) {
						
						for (var k = 0; k < disable.items.length; k++) {
							// check if this value disables the cell to select 
							// (id is equals to the id of the column to edit)
							var disItem = disable.items[k];
							if (disItem == editId) {
								this.grid.getColumnModel().getCellEditor(col, row).disable();
								return;
							}
						}
					}
				}		
			}
		}
		this.grid.getColumnModel().getCellEditor(col, row).enable();
		//this.grid.getView().restoreScroll(state);
	},
	
    /**
     * If the trigger was clicked a dialog has to be opened
     * to enter the values for the complex property.
     */
    onTriggerClick : function(){
        if(this.disabled){
            return;
        }	
        if(this.dialog){
       	 	return;
        }
		//if(!this.dialog) { 
		
		/*var dialogWidth = 0;
		var recordType 	= [];
		
		for (var i = 0; i < this.items.length; i++) {
			var id 		= this.items[i].id();
			var width 	= this.items[i].width();
			var type 	= this.items[i].type();	
				
			if (type == ORYX.CONFIG.TYPE_CHOICE) {
				type = ORYX.CONFIG.TYPE_STRING;
			}
					
			dialogWidth += width;
			recordType[i] = {name:id, type:type};
		}			
		
		if (dialogWidth > 800) {
			dialogWidth = 800;
		}
		dialogWidth += 22;*/
		
		var dialogWidth = 0;
		var recordType 	= [];

		//var fieldResponseDataGlob = "";
		var rowCol = this.grid.getSelectionModel().getSelectedCell();
		var record = this.grid.store.getAt(rowCol[0]);
		var resourceType = record.get("name");
		var resourceValue = record.get("value");
		
		var recordForm = this.grid.store.getAt(3);
		var resourceTypeForm = recordForm.get("value");
		var paramFormId = "";
		if(resourceTypeForm != "" && resourceTypeForm.length > 0){
			paramFormId = resourceTypeForm;
		}else{
			var recordJspForm = this.grid.store.getAt(4);
			var resourceTypeFormValue = recordJspForm.get("value");
			paramFormId = resourceTypeFormValue;
		}
		
		var recordNodeType = this.grid.store.getAt(24);
		var nodeType = recordNodeType.get("value");
		
		if(resourceValue != ""){
			reInitializeObjectObj(resourceValue,resourceType,"membersFields",paramFormId);
		}else{
			clearObjects(resourceValue,resourceType,"membersFields");
		}
		
		var fieldResource = getExtjsJsonObjWithName(resourceType);

		if(paramFormId == null || paramFormId == "" || paramFormId == undefined){
			Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.formError);	
		}else{
			var treePanel = new Ext.Panel({
			    title: ORYX.I18N.PropertyWindow.cts,
			    layout: 'table',
			    width: 447,
			    height: 300,
			    header:false,
			    items: [{
			            id: 'tree_select',
			            html: '<div id="resources_select" class="pad-T5 pad-L40">'+
			            '<select id = "resources_options" name = "resources_options" onchange="loadTree(null,'+"'customTree'"+",'"+resourceType+"'"+')" class="fontSize13">'+
		                '<option value="none"> </option>'+
		                '<option value="potentialOwner">Group</option>'+
		                '<option value="potentialOwner">Role</option>'+
		                '<option value="potentialOwner">Department</option>'+
		                '<option value="humanPerformer" selected="selected">User</option>'+
		                '</select>'+	
			            	'</div><div id="tree2" class="pad-L40 pad-T10"></div>',
			            bodyBorder: false,
			            autoScroll: true,
			            width: 225,
			            height: 300,
			            listeners: {
						 'render':function() {
							 loadTree("humanPerformer","customTree",resourceType, nodeType);
						 	},
					    }

			        }, {
			            region: 'east',
			            id: 'details-panel',
			            autoScroll: true,
			            collapsible: false,
			            split: true,
			            width: 220,
			            height: 300,
			            html:'<div class="padding10" id="userInfo_"></div><div class="padding10 " id="resource_order"></div>',
			        }
			    ]
			});

			var permissionTabPanel = new Ext.TabPanel({
				renderTo: document.body,
				activeTab: 0,
				height:335,
				width:450,
				//plain:true,
				defaults:{autoScroll: true},
				items:[{
					title: ORYX.I18N.PropertyWindow.members,
					items:[treePanel],
				},{
					title: ORYX.I18N.PropertyWindow.premission,
					html: '<div id="fieldPermissionDiv" class="padding10"></div>',
					listeners:{
						activate : function(){
							setFieldPermissionContent(fieldResource,paramFormId,resourceType,resourceValue);
						}
						
					}
				},{
					title: ORYX.I18N.PropertyWindow.dynamicTransactor,
					html: '<div id="dynamicTransactorDiv" class="padding10"></div>',
					listeners:{
						activate : function(){
							setDynamicTransactorContent(paramFormId,resourceType,resourceValue);
						}
						
					}
				}]
			});
			// Basic Dialog
			this.dialog = new Ext.Window({ 
				autoScroll: true,
				autoCreate: true,
				title: ORYX.I18N.PropertyWindow.customTreeSelect, 
				height: 420, 
				width: 464, 
				modal:true,
				collapsible:false,
				fixedcenter: true, 
				shadow:true, 
				proxyDrag: true,
				keys:[{
					key: 27,
					fn: function(){
						this.dialog.hide
					}.bind(this)
				}],
				items:[permissionTabPanel],
				bodyStyle:"background-color:#FFFFFF",
				
				buttons: [{
	                text: ORYX.I18N.PropertyWindow.submit,
	                handler: function(){
						// store dialog input
						//this.data = this.buildValue();
	                 	//var rowCol = this.grid.getSelectionModel().getSelectedCell();
	                	//var record = this.grid.store.getAt(rowCol[0]);
	                	//var resourceType = record.get("name");

						var fieldResource = getExtjsJsonObjWithName(resourceType);
	                	if(fieldResource.getCount() > 0){
	                		if(isResourceOrderDivVisible()) {
								var success = addOrderToResources();
								if(success) {
									this.data = assignOrderAndGetResourceData(resourceType,paramFormId);
									this.dialog.hide();
								}
							}else {
									showResourceOrderDiv();	
									renderResourceOrderView(resourceType, nodeType);
							}
	                	}else{
	                		if(isResourceOrderDivVisible()) {
	                			this.data = assignOrderAndGetResourceData(resourceType,paramFormId);
	                		}
					var dialog = this.dialog;
	                		var fieldLength = 0;
	                		Ext.Ajax.request({
						url: "/bpm/form/getAllFormFields",
						params: {formId :paramFormId},
						asynchronous: false,
						method: 'GET',
						failure:function(response,options){
							},
						success:function(response,options){	
							var fieldData = response.responseText;
							var jsonFieldData = {};
							jsonFieldData = JSON.parse(fieldData.toString());
							var fieldParse = jsonFieldData.forms;
							fieldLength = fieldParse.length;
							if( fieldLength == 0){
								if(isResourceOrderDivVisible()) {
									var success = addOrderToResources();
									if(success) {
										dialog.hide();
									}
									}else {
										showResourceOrderDiv();	
										renderResourceOrderView(resourceType, nodeType);
									}
							}else{
								  Ext.MessageBox.alert('Information',ORYX.I18N.PropertyWindow.permissionError);
							}	
						},
					});
	                	}
	                }.bind(this)
	            }, {
	                text: ORYX.I18N.PropertyWindow.cancel,
	                handler: function(){
	                	this.dialog.hide()
	                }.bind(this)
	            }],
	            listeners: {
					 'show':function() {
						  if(resourceStore.getCount() != 0) {
							  showResourceOrderDiv();	
							  renderResourceOrderView(resourceType, nodeType);
						  }
					 	},
	            }
			});				
				
			this.dialog.on(Ext.apply({}, this.dialogListeners, {
	       		scope:this
	        }));
		
			this.dialog.show();	
		}
	}
});

/**
 * Copyright (c) 2008
 * Willi Tscheschner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/

if(!ORYX.Plugins)
	ORYX.Plugins = new Object();

/**
 * Supports EPCs by offering a syntax check and export and import ability..
 * 
 * 
 */
ORYX.Plugins.ERDFSupport = Clazz.extend({

	facade: undefined,
	
	ERDFServletURL: '/erdfsupport',

	/**
	 * Offers the plugin functionality:
	 * 
	 */
	construct: function(facade) {
		
		this.facade = facade;
			
			
		/*this.facade.offer({
			'name':				ORYX.I18N.ERDFSupport.exp,
			'functionality': 	this.exportERDF.bind(this),
			'group': 			'Export',
            dropDownGroupIcon: ORYX.PATH + "images/export2.png",
			'icon': 			ORYX.PATH + "images/erdf_export_icon.png",
			'description': 		ORYX.I18N.ERDFSupport.expDesc,
			'index': 			0,
			'minShape': 		0,
			'maxShape': 		0
		});
					
		this.facade.offer({
			'name':				ORYX.I18N.ERDFSupport.imp,
			'functionality': 	this.importERDF.bind(this),
			'group': 			'Export',
            dropDownGroupIcon: ORYX.PATH + "images/import.png",
			'icon': 			ORYX.PATH + "images/erdf_import_icon.png",
			'description': 		ORYX.I18N.ERDFSupport.impDesc,
			'index': 			1,
			'minShape': 		0,
			'maxShape': 		0
		});*/

	},

	
	/**
	 * Imports an AML description
	 * 
	 */
	importERDF: function(){
		this._showImportDialog();
	},		

	
	/**
	 * Imports an AML description
	 * 
	 */
	exportERDF: function(){
        // Show deprecation message
        Ext.Msg.show({
           title:ORYX.I18N.ERDFSupport.deprTitle,
           msg: ORYX.I18N.ERDFSupport.deprText,
           buttons: Ext.Msg.YESNO,
           fn: function(buttonId){
               if(buttonId === 'yes'){
                    var s   = this.facade.getERDF();
                    
                    //this.openXMLWindow( s );
                    this.openDownloadWindow(window.document.title + ".xml", s);
               }
           }.bind(this),
           icon: Ext.MessageBox.WARNING 
        });
	},
	
	/**
	 * 
	 * 
	 * @param {Object} url
	 * @param {Object} params
	 * @param {Object} successcallback
	 */
	sendRequest: function( url, params, successcallback, failedcallback ){

		var suc = false;

		new Ajax.Request(url, {
            method			: 'POST',
            asynchronous	: false,
            parameters		: params,
			onSuccess		: function(transport) {
				
				suc = true;
				
				if(successcallback){
					successcallback( transport.result )	
				}
				
			}.bind(this),
			
			onFailure		: function(transport) {

				if(failedcallback){
					
					failedcallback();
					
				} else {
					Ext.Msg.alert(ORYX.I18N.Oryx.title, ORYX.I18N.ERDFSupport.impFailed);
					ORYX.log.warn("Import ERDF failed: " + transport.responseText);	
				}
				
			}.bind(this)		
		});
		
		
		return suc;
							
	},


	loadERDF: function( erdfString, success, failed ){
		
		var s 	= erdfString;
		s 		= s.startsWith('<?xml') ? s : '<?xml version="1.0" encoding="utf-8"?>'+s+'';	
						
		var parser	= new DOMParser();			
		var doc 	=  parser.parseFromString( s ,"text/xml");
							
		if( doc.firstChild.tagName == "parsererror" ){

			Ext.MessageBox.show({
					title: 		ORYX.I18N.ERDFSupport.error,
 					msg: 		ORYX.I18N.ERDFSupport.impFailed2 + doc.firstChild.textContent.escapeHTML(),
					buttons: 	Ext.MessageBox.OK,
					icon: 		Ext.MessageBox.ERROR
				});
																
			if(failed)
				failed();
				
		} else if( !this.hasStencilSet(doc) ){
			
			if(failed)
				failed();		
		
		} else {
			
			this.facade.importERDF( doc );
			
			if(success)
				success();
		
		}
	},

	hasStencilSet: function( doc ){
		
		var getElementsByClassNameFromDiv 	= function(doc, id){ return $A(doc.getElementsByTagName('div')).findAll(function(el){ return $A(el.attributes).any(function(attr){ return attr.nodeName == 'class' && attr.nodeValue == id }) })	}

		// Get Canvas Node
		var editorNode 		= getElementsByClassNameFromDiv( doc, '-oryx-canvas')[0];
		
		if( !editorNode ){
			this.throwWarning(ORYX.I18N.ERDFSupport.noCanvas);
			return false
		}
		
		var stencilSetNode 	= $A(editorNode.getElementsByTagName('a')).find(function(node){ return node.getAttribute('rel') == 'oryx-stencilset'});

		if( !stencilSetNode ){
			this.throwWarning(ORYX.I18N.ERDFSupport.noSS);
			return false
		}
		
		var stencilSetUrl	= stencilSetNode.getAttribute('href').split("/")
		stencilSetUrl		= stencilSetUrl[stencilSetUrl.length-2] + "/" + stencilSetUrl[stencilSetUrl.length-1];
		
//		var isLoaded = this.facade.getStencilSets().values().any(function(ss){ return ss.source().endsWith( stencilSetUrl ) })
//		if( !isLoaded ){
//			this.throwWarning(ORYX.I18N.ERDFSupport.wrongSS);
//			return false
//		}
				
		return true;
	},
	
	throwWarning: function( text ){
		Ext.MessageBox.show({
					title: 		ORYX.I18N.Oryx.title,
 					msg: 		text,
					buttons: 	Ext.MessageBox.OK,
					icon: 		Ext.MessageBox.WARNING
				});
	},
	
	/**
	 * Opens a new window that shows the given XML content.
	 * 
	 * @param {Object} content The XML content to be shown.
	 */
	openXMLWindow: function(content) {
		var win = window.open(
		   'data:application/xml,' + encodeURIComponent(
		     content
		   ),
		   '_blank', "resizable=yes,width=600,height=600,toolbar=0,scrollbars=yes"
		);
	},
	
	/**
	 * Opens a download window for downloading the given content.
	 * 
	 */
	openDownloadWindow: function(file, content) {
		var win = window.open("");
		if (win != null) {
			win.document.open();
			win.document.write("<html><body>");
			var submitForm = win.document.createElement("form");
			win.document.body.appendChild(submitForm);
			
			submitForm.appendChild( this.createHiddenElement("download", content));
			submitForm.appendChild( this.createHiddenElement("file", file));
			
			
			submitForm.method = "POST";
			win.document.write("</body></html>");
			win.document.close();
			submitForm.action= ORYX.PATH + "/download";
			submitForm.submit();
		}		
	},
	
	/**
	 * Creates a hidden form element to communicate parameter values.
	 * 
	 * @param {Object} name  The name of the hidden field
	 * @param {Object} value The value of the hidden field
	 */
	createHiddenElement: function(name, value) {
		var newElement = document.createElement("input");
		newElement.name=name;
		newElement.type="hidden";
		newElement.value = value;
		return newElement
	},

	/**
	 * Opens a upload dialog.
	 * 
	 */
	_showImportDialog: function( successCallback ){
	
	    var form = new Ext.form.FormPanel({
			baseCls: 		'x-plain',
	        labelWidth: 	50,
	        defaultType: 	'textfield',
	        items: [{
	            text : 		ORYX.I18N.ERDFSupport.selectFile, 
				style : 	'font-size:12px;margin-bottom:10px;display:block;',
	            anchor:		'100%',
				xtype : 	'label' 
	        },{
	            fieldLabel: ORYX.I18N.ERDFSupport.file,
	            name: 		'subject',
				inputType : 'file',
				style : 	'margin-bottom:10px;display:block;',
				itemCls :	'ext_specific_window_overflow'
	        }, {
	            xtype: 'textarea',
	            hideLabel: true,
	            name: 'msg',
	            anchor: '100% -63'  
	        }]
	    });



		// Create the panel
		var dialog = new Ext.Window({ 
			autoCreate: true, 
			layout: 	'fit',
			plain:		true,
			bodyStyle: 	'padding:5px;',
			title: 		ORYX.I18N.ERDFSupport.impERDF, 
			height: 	350, 
			width:		500,
			modal:		true,
			fixedcenter:true, 
			shadow:		true, 
			proxyDrag: 	true,
			resizable:	true,
			items: 		[form],
			buttons:[
				{
					text:ORYX.I18N.ERDFSupport.impBtn,
					handler:function(){
						
						var loadMask = new Ext.LoadMask(Ext.getBody(), {msg:ORYX.I18N.ERDFSupport.impProgress});
						loadMask.show();
						
						window.setTimeout(function(){
					
							
							var erdfString =  form.items.items[2].getValue();
							this.loadERDF(erdfString, function(){loadMask.hide();dialog.hide()}.bind(this), function(){loadMask.hide();}.bind(this))
														
														
							
						}.bind(this), 100);
			
					}.bind(this)
				},{
					text:ORYX.I18N.ERDFSupport.close,
					handler:function(){
						
						dialog.hide();
					
					}.bind(this)
				}
			]
		});
		
		// Destroy the panel when hiding
		dialog.on('hide', function(){
			dialog.destroy(true);
			delete dialog;
		});


		// Show the panel
		dialog.show();
		
				
		// Adds the change event handler to 
		form.items.items[1].getEl().dom.addEventListener('change',function(evt){
				var text = evt.target.files[0].getAsText('UTF-8');
				form.items.items[2].setValue( text );
			}, true)

	}
	
});
/**
 * Copyright (c) 2009
 * Kai Schlichting
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

/**
 * Enables exporting and importing current model in JSON.
 */
ORYX.Plugins.JSONSupport = ORYX.Plugins.AbstractPlugin.extend({

    construct: function(){
        // Call super class constructor
        arguments.callee.$.construct.apply(this, arguments);
        
        /*this.facade.offer({
            'name': ORYX.I18N.JSONSupport.exp.name,
            'functionality': this.exportJSON.bind(this),
            'group': ORYX.I18N.JSONSupport.exp.group,
            dropDownGroupIcon: ORYX.PATH + "images/export2.png",
			'icon': ORYX.PATH + "images/page_white_javascript.png",
            'description': ORYX.I18N.JSONSupport.exp.desc,
            'index': 0,
            'minShape': 0,
            'maxShape': 0
        });
        
        this.facade.offer({
            'name': ORYX.I18N.JSONSupport.imp.name,
            'functionality': this.showImportDialog.bind(this),
            'group': ORYX.I18N.JSONSupport.imp.group,
            dropDownGroupIcon: ORYX.PATH + "images/import.png",
			'icon': ORYX.PATH + "images/page_white_javascript.png",
            'description': ORYX.I18N.JSONSupport.imp.desc,
            'index': 1,
            'minShape': 0,
            'maxShape': 0
        });*/
    },
    
    exportJSON: function(){
        var json = this.facade.getSerializedJSON();
        this.openDownloadWindow(window.document.title + ".json", json);
    },
    
    /**
     * Opens a upload dialog.
     *
     */
    showImportDialog: function(successCallback){
    
        var form = new Ext.form.FormPanel({
            baseCls: 'x-plain',
            labelWidth: 50,
            defaultType: 'textfield',
            items: [{
                text: ORYX.I18N.JSONSupport.imp.selectFile,
                style: 'font-size:12px;margin-bottom:10px;display:block;',
                anchor: '100%',
                xtype: 'label'
            }, {
                fieldLabel: ORYX.I18N.JSONSupport.imp.file,
                name: 'subject',
                inputType: 'file',
                style: 'margin-bottom:10px;display:block;',
                itemCls: 'ext_specific_window_overflow'
            }, {
                xtype: 'textarea',
                hideLabel: true,
                name: 'msg',
                anchor: '100% -63'
            }]
        });
        
        // Create the panel
        var dialog = new Ext.Window({
            autoCreate: true,
            layout: 'fit',
            plain: true,
            bodyStyle: 'padding:5px;',
            title: ORYX.I18N.JSONSupport.imp.name,
            height: 350,
            width: 500,
            modal: true,
            fixedcenter: true,
            shadow: true,
            proxyDrag: true,
            resizable: true,
            items: [form],
            buttons: [{
                text: ORYX.I18N.JSONSupport.imp.btnImp,
                handler: function(){
                
                    var loadMask = new Ext.LoadMask(Ext.getBody(), {
                        msg: ORYX.I18N.JSONSupport.imp.progress
                    });
                    loadMask.show();
                    
                    window.setTimeout(function(){
                        var json = form.items.items[2].getValue();
                        try {
                            this.facade.importJSON(json, true);
                            dialog.close();
                        } 
                        catch (error) {
                            Ext.Msg.alert(ORYX.I18N.JSONSupport.imp.syntaxError, error.message);
                        }
                        finally {
                            loadMask.hide();
                        }
                    }.bind(this), 100);
                    
                }.bind(this)
            }, {
                text: ORYX.I18N.JSONSupport.imp.btnClose,
                handler: function(){
                    dialog.close();
                }.bind(this)
            }]
        });
        
        // Show the panel
        dialog.show();
        
        // Adds the change event handler to 
        form.items.items[1].getEl().dom.addEventListener('change', function(evt){
            var text = evt.target.files[0].getAsText('UTF-8');
            form.items.items[2].setValue(text);
        }, true)
        
    }
    
});
/**
 * Copyright (c) 2009, Matthias Kunze, Kai Schlichting
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/
if (!ORYX.Plugins) 
    ORYX.Plugins = {};

if (!ORYX.Config)
	ORYX.Config = {};

/*
Commented(Rajasekar)
ORYX.Config.Feedback = {
	VISIBLE_STATE: "visible",
	HIDDEN_STATE: "hidden",
	INFO: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, set eiusmod tempor incidunt et labore et dolore magna aliquam. Ut enim ad minim veniam, quis nostrud exerc. Irure dolor in reprehend incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse molestaie cillum. Tia non ob ea soluad incommod quae egen ium improb fugiend. Officia",
	CSS_FILE: ORYX.PATH + "/css/feedback.css"
}*/

ORYX.Plugins.Feedback = ORYX.Plugins.AbstractPlugin.extend({
	
    construct: function(facade, data){
		/*
		 * data.name == "ORYX.Plugins.Feedback"
		 * data.source == "feedback.js"
		 * data.properties ... properties defined in plugins.xml/profiles.xml [{key:value}, ...]
		 */
	
		this.facade = facade;
	
		// extract properties, we're interested in
		((data && data.properties) || []).each(function(property){
			if (property.cssfile) {ORYX.Config.Feedback.CSS_FILE = property.css_file}
		}.bind(this));
		
        // load additional css information
        var fileref = document.createElement("link");
            fileref.setAttribute("rel", "stylesheet");
            fileref.setAttribute("type", "text/css");
            fileref.setAttribute("href", ORYX.Config.Feedback.CSS_FILE);
        document.getElementsByTagName("head")[0].appendChild(fileref);

        // declare HTML references
        this.elements = {
    		container: null,
    		tab: null,
    		dialog: null,
			form: null,
			info: null
    	}
        
        // create feedback tab
        this.createFeedbackTab();
        
    },
    
    /**
     * Creates the feedback tab, which is used to open the feedback dialog.
     */
    createFeedbackTab: function(){
    	this.elements.tab = document.createElement("div");
    	this.elements.tab.setAttribute("class", "tab");
		this.elements.tab.innerHTML = (ORYX.I18N.Feedback.name + " &#8226;")
    	
    	this.elements.container = document.createElement("div");
    	this.elements.container.setAttribute("id", "feedback");
    	
    	this.elements.container.appendChild(this.elements.tab);
    	document.body.appendChild(this.elements.container);
          	    	
    	// register events
    	Event.observe(this.elements.tab, "click", this.toggleDialog.bindAsEventListener(this));
    },
    
    /**
     * Hides or shows the feedback dialog
     */
    toggleDialog: function(event) {

		if (event) {
			Event.stop(event);			
		}

    	var dialog = this.elements.dialog || this.createDialog();
    	
    	if (ORYX.Config.Feedback.VISIBLE_STATE == dialog.state) {
			this.elements.tab.innerHTML = (ORYX.I18N.Feedback.name + " &#8226;");
    		Element.hide(dialog);
    		dialog.state = ORYX.Config.Feedback.HIDDEN_STATE;
    	} 
    	else {
			this.elements.tab.innerHTML = (ORYX.I18N.Feedback.name + " &#215;");
    		Element.show(dialog);
    		dialog.state = ORYX.Config.Feedback.VISIBLE_STATE;
    	}

    },
    
    /**
     * Creates the feedback dialog
     */
    createDialog: function() {
    	if (this.elements.dialog) {
    		return this.elements.dialog;
    	}

		// reset the input formular
		var resetForm = function() {
			[description, title, mail].each(function(element){
				element.value = element._defaultText || "";
				element.className = "low";
			});
		}

		// wrapper for field focus behavior
		var fieldOnFocus = function(event) {
			var e = Event.element(event);
			if (e._defaultText && e.value.strip() == e._defaultText.strip()) {
				e.value = "";
				e.className = "high";
			}
		}		
		var fieldOnBlur = function(event) {
			var e = Event.element(event);
			if (e._defaultText && e.value.strip() == "") {
				e.value = e._defaultText;
				e.className = "low";
			}
		}

    	// create form and submit logic (ajax)
		this.elements.form = document.createElement("form");
		this.elements.form.action = ORYX.CONFIG.ROOT_PATH + "feedback";
		this.elements.form.method = "POST";
		this.elements.form.onsubmit = function(){
			
			try {
				
				var failure = function() {
					Ext.Msg.alert(ORYX.I18N.Feedback.failure, ORYX.I18N.Feedback.failureMsg);
	                this.facade.raiseEvent({
	                    type: ORYX.CONFIG.EVENT_LOADING_DISABLE
	                });
					// show dialog again with old information
					this.toggleDialog();
				}
				
				var success = function(transport) {
					if (transport.status < 200 || transport.status >= 400) {
						return failure(transport);
					}
					this.facade.raiseEvent({
						type:ORYX.CONFIG.EVENT_LOADING_STATUS,
						text:ORYX.I18N.Feedback.success
					});
					resetForm();
				}
				
			
				this.elements.form.model.value = this.facade.getSerializedJSON();
				this.elements.form.environment.value = this.getEnv();
			
				var params = {};
				$A(this.elements.form.elements).each(function(element){
					params[element.name] = element.value;
				});
				params["name"]= ORYX.Editor.Cookie.getParams().identifier;
				params["subject"] = ("[" + params["subject"] + "] " + params["title"]);
				this.facade.raiseEvent({
					type:ORYX.CONFIG.EVENT_LOADING_STATUS,
					text:ORYX.I18N.Feedback.sending
				});
				new Ajax.Request(ORYX.CONFIG.ROOT_PATH + "feedback", {
					method: "POST",
					parameters: params,
					onSuccess: success.bind(this),
					onFailure: failure.bind(this)
				});
			
				// hide dialog immediately 
				this.toggleDialog();
			}
			catch(e) {
				failure();
				ORYX.Log.warn(e);
			}
			// stop form submission through browser
			return false; 
		}.bind(this);
		
		
		// create input fields
		var fieldset = document.createElement("div");
			fieldset.className = "fieldset";
		    
		var f_subject = document.createElement("input");
		    f_subject.type = "hidden";
			f_subject.name = "subject";
			f_subject.style.display = "none";
		
		var description = document.createElement("textarea");
			description._defaultText = ORYX.I18N.Feedback.descriptionDesc;
		    description.name = "description";
		Event.observe(description, "focus", fieldOnFocus.bindAsEventListener());
		Event.observe(description, "blur", fieldOnBlur.bindAsEventListener());
		
		var title = document.createElement("input");
			title._defaultText = ORYX.I18N.Feedback.titleDesc;
			title.type = "text";
			title.name = "title";
		Event.observe(title, "focus", fieldOnFocus.bindAsEventListener());
		Event.observe(title, "blur", fieldOnBlur.bindAsEventListener());
			
		var mail = document.createElement("input");
			mail._defaultText = ORYX.I18N.Feedback.emailDesc;
			mail.type = "text";
			mail.name = "email";
		Event.observe(mail, "focus", fieldOnFocus.bindAsEventListener());
		Event.observe(mail, "blur", fieldOnBlur.bindAsEventListener());
		
		var submit = document.createElement("input");
			submit.type = "button";
			submit.className = "submit";
			submit.onclick=this.elements.form.onsubmit;
			if (ORYX.I18N.Feedback.submit) {
				submit.value = ORYX.I18N.Feedback.submit;
			}
			
		var environment = document.createElement("input");
			environment.name = "environment";
			environment.type = "hidden";
			environment.style.display = "none";
			
		var model = document.createElement("input");
			model.name = "model"
			model.type = "hidden";
			model.style.display = "none";
			
		fieldset.appendChild(f_subject);
		fieldset.appendChild(description);
		fieldset.appendChild(title);
		fieldset.appendChild(mail);
		fieldset.appendChild(environment);
		fieldset.appendChild(model);
		fieldset.appendChild(submit);
		
		// (p)reset default values of input fields
		resetForm();
			
		// create subjects
		var list = document.createElement("ul");
	    list.setAttribute("class", "subjects");
		
		var l_subjects = [];
		
		$A(ORYX.I18N.Feedback.subjects).each( function(subject, index){
			try {
				
				// create list item
				var item = document.createElement("li");
					item._subject = subject.id;
				    item.className = subject.id;
					item.innerHTML = subject.name;
					item.style.width = parseInt(100/$A(ORYX.I18N.Feedback.subjects).length)+"%"; // set width corresponding to number of subjects
				
				// add subjects to list
				l_subjects.push(item);
				list.appendChild(item);

				var handler = function(){
					l_subjects.each(function(element) {
						if (element.className.match(subject.id)) { // if current element is selected
							element.className = element._subject + " active";
							f_subject.value = subject.name;
							
							// update description, depending on subject if input field is empty
							if (description.value == description._defaultText) {
								description.value = subject.description;
							}
							
							// set _defaultText to newly selected subject
							description._defaultText = subject.description;
							
							// set info pane if appropriate
							if (subject.info && (""+subject.info).strip().length > 0) {
								this.elements.info.innerHTML = subject.info;
							}
							else {
								this.elements.info.innerHTML = ORYX.I18N.Feedback.info || "";
							}
						}
						else {
							element.className = element._subject;
						}
					}.bind(this));
				}.bind(this);
				
				// choose/unchoose topics
				Event.observe(item, "click", handler);
				
				// select last item
				if (index == (ORYX.I18N.Feedback.subjects.length - 1)) {
					description.value = "";
					description._defaultText = "";
					
					handler();
				}
				
			} // if something goes wrong, we wont give up, just ignore it
			catch (e) {
				ORYX.Log.warn("Incomplete I10N for ORYX.I18N.Feedback.subjects", subject, ORYX.I18N.Feedback.subjects)
			}
		}.bind(this));
	
		this.elements.form.appendChild(list);
		this.elements.form.appendChild(fieldset);
		
		this.elements.info = document.createElement("div");
		this.elements.info.setAttribute("class", "info");
		this.elements.info.innerHTML = ORYX.I18N.Feedback.info || "";
		
		var head = document.createElement("div");
			head.setAttribute("class", "head");

    	this.elements.dialog = document.createElement("div");
		this.elements.dialog.setAttribute("class", "dialog");
		this.elements.dialog.appendChild(head);
		this.elements.dialog.appendChild(this.elements.info);
		this.elements.dialog.appendChild(this.elements.form);

		
		this.elements.container.appendChild(this.elements.dialog);
		
    	return this.elements.dialog;
    },

    getEnv: function(){
        var env = "";
        
        env += "Browser: " + navigator.userAgent;
        
        env += "\n\nBrowser Plugins: ";
        if (navigator.plugins) {
            for (var i = 0; i < navigator.plugins.length; i++) {
                var plugin = navigator.plugins[i];
                env += plugin.name + ", ";
            }
        }
        
        if ((typeof(screen.width) != "undefined") && (screen.width && screen.height)) 
            env += "\n\nScreen Resolution: " + screen.width + 'x' + screen.height;
        
        return env;
    }
});

 * Copyright (c) 2010
 * Robert Böhme, Philipp Berger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/

if(!ORYX.Plugins)
	ORYX.Plugins = new Object();

ORYX.Plugins.DockerCreation = Clazz.extend({
	
	construct: function( facade ){
		this.facade = facade;		
		this.active = false; //true-> a ghostdocker is shown; false->ghostdocker is hidden

		//visual representation of the Ghostdocker
		this.circle = ORYX.Editor.graft("http://www.w3.org/2000/svg", null ,
				['g', {"pointer-events":"none"},
					['circle', {cx: "8", cy: "8", r: "3", fill:"yellow"}]]); 	
		
		//Event registrations
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_MOUSEDOWN, this.handleMouseDown.bind(this));
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_MOUSEOVER, this.handleMouseOver.bind(this));
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_MOUSEOUT, this.handleMouseOut.bind(this));
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_MOUSEMOVE, this.handleMouseMove.bind(this));
		/*
		 * Double click is reserved for label access, so abort action
		 */
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_DBLCLICK,function(){window.clearTimeout(this.timer)}.bind(this));
		/*
		 * click is reserved for selecting, so abort action when mouse goes up
		 */
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_MOUSEUP,function(){window.clearTimeout(this.timer)}.bind(this));

	},
	
	/**
	 * MouseOut Handler
	 * 
	 *hide the Ghostpoint when Leaving the mouse from an edge
	 */
	handleMouseOut: function(event, uiObj) {
		
		if (this.active) {		
			this.hideOverlay();
			this.active = false;
		}	
	},
	
	/**
	 * MouseOver Handler
	 * 
	 *show the Ghostpoint if the edge is selected
	 */
	handleMouseOver: function(event, uiObj) {
		//show the Ghostdocker on the edge
		if (uiObj instanceof ORYX.Core.Edge && this.isEdgeDocked(uiObj)){
			this.showOverlay(uiObj, this.facade.eventCoordinates(event));
		}
		//ghostdocker is active
		this.active = true;
		
	},
	
	/**
	 * MouseDown Handler
	 * 
	 *create a Docker when clicking on a selected edge
	 */
	handleMouseDown: function(event, uiObj) {	
		if (event.which==1 && uiObj instanceof ORYX.Core.Edge && this.isEdgeDocked(uiObj)){
			//Timer for Doubleclick to be able to create a label
			window.clearTimeout(this.timer);
			
			this.timer = window.setTimeout(function () {
				// Give the event to enable one click creation and drag
				this.addDockerCommand({
		            edge: uiObj,
					event: event,
		            position: this.facade.eventCoordinates(event)
		        });
	
			}.bind(this),200);
			this.hideOverlay();
	
		}
	},
	
	/**
	 * MouseMove Handler
	 * 
	 *refresh the ghostpoint when moving the mouse over an edge
	 */
	handleMouseMove: function(event, uiObj) {		
			if (uiObj instanceof ORYX.Core.Edge && this.isEdgeDocked(uiObj)){
				if (this.active) {	
					//refresh Ghostpoint
					this.hideOverlay();			
					this.showOverlay( uiObj, this.facade.eventCoordinates(event));
				}else{
					this.showOverlay( uiObj, this.facade.eventCoordinates(event));	
				}		
			}	
	},
	
	/**
	 * returns true if the edge is docked to at least one node
	 */
	isEdgeDocked: function(edge){
		return !!(edge.incoming.length || edge.outgoing.length);
	},
	
	
	/**
	 * Command for creating a new Docker
	 * 
	 * @param {Object} options
	 */
	addDockerCommand: function(options){
	    if(!options.edge)
	        return;
	    
	    var commandClass = ORYX.Core.Command.extend({
	        construct: function(edge, docker, pos, facade, options){            
	            this.edge = edge;
	            this.docker = docker;
	            this.pos = pos;
	            this.facade = facade;
				this.options= options;
	        },
	        execute: function(){
	            this.docker = this.edge.addDocker(this.pos, this.docker);
				this.index = this.edge.dockers.indexOf(this.docker);                                    
	            this.facade.getCanvas().update();
	            this.facade.updateSelection();
	            this.options.docker=this.docker;
	
	        },
	        rollback: function(){
	          
	             if (this.docker instanceof ORYX.Core.Controls.Docker) {
	                    this.edge.removeDocker(this.docker);
	             }             
	            this.facade.getCanvas().update();
	            this.facade.updateSelection(); 
	        }
	    });
	    var command = new commandClass(options.edge, options.docker, options.position, this.facade, options);    
	    this.facade.executeCommands([command]);
	
	    
		this.facade.raiseEvent({
			uiEvent:	options.event,
			type:		ORYX.CONFIG.EVENT_DOCKERDRAG}, options.docker );
	    
	},
	
	/**
	 *show the ghostpoint overlay
	 *
	 *@param {Shape} edge
	 *@param {Point} point
	 */
	showOverlay: function(edge, point){
		var best = point;
		var pair = [0,1];
		var min_distance = Infinity;
	
		// calculate the optimal point ON THE EDGE to display the docker
		for (var i=0, l=edge.dockers.length; i < l-1; i++) {
			var intersection_point = ORYX.Core.Math.getPointOfIntersectionPointLine(
				edge.dockers[i].bounds.center(),
				edge.dockers[i+1].bounds.center(),
				point,
				true // consider only the current segment instead of the whole line ("Strecke, statt Gerade") for distance calculation
			);
			
			
			if(!intersection_point) {
				continue;
			}
	
			var current_distance = ORYX.Core.Math.getDistancePointToPoint(point, intersection_point);
			if (min_distance > current_distance) {
				min_distance = current_distance;
				best = intersection_point;
			}
		}
	
		this.facade.raiseEvent({
				type: 			ORYX.CONFIG.EVENT_OVERLAY_SHOW,
				id: 			"ghostpoint",
				shapes: 		[edge],
				node:			this.circle,
				ghostPoint:		best,
				dontCloneNode:	true
			});			
	},
	
	/**
	 *hide the ghostpoint overlay
	 */
	hideOverlay: function() {
		
		this.facade.raiseEvent({
			type: ORYX.CONFIG.EVENT_OVERLAY_HIDE,
			id: "ghostpoint"
		});	
	}

});
 * Copyright (c) 2008
 * Willi Tscheschner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * 
 * HOW to USE the OVERLAY PLUGIN:
 * 	You can use it via the event mechanism from the editor
 * 	by using facade.raiseEvent( <option> )
 * 
 * 	As an example please have a look in the overlayexample.js
 * 
 * 	The option object should/have to have following attributes:
 * 
 * 	Key				Value-Type							Description
 * 	================================================================
 * 
 *	type 			ORYX.CONFIG.EVENT_OVERLAY_SHOW | ORYX.CONFIG.EVENT_OVERLAY_HIDE		This is the type of the event	
 *	id				<String>							You have to use an unified id for later on hiding this overlay
 *	shapes 			<ORYX.Core.Shape[]>					The Shapes where the attributes should be changed
 *	attributes 		<Object>							An object with svg-style attributes as key-value pair
 *	node			<SVGElement>						An SVG-Element could be specified for adding this to the Shape
 *	nodePosition	"N"|"NE"|"E"|"SE"|"S"|"SW"|"W"|"NW"|"START"|"END"	The position for the SVG-Element relative to the 
 *														specified Shape. "START" and "END" are just using for a Edges, then
 *														the relation is the start or ending Docker of this edge.
 *	
 * 
 **/
if (!ORYX.Plugins) 
    ORYX.Plugins = new Object();

ORYX.Plugins.Overlay = Clazz.extend({

    facade: undefined,
	
	styleNode: undefined,
    
    construct: function(facade){
		
        this.facade = facade;

		this.changes = [];

		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_OVERLAY_SHOW, this.show.bind(this));
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_OVERLAY_HIDE, this.hide.bind(this));	

		this.styleNode = document.createElement('style')
		this.styleNode.setAttributeNS(null, 'type', 'text/css')
		
		document.getElementsByTagName('head')[0].appendChild( this.styleNode )

    },
	
	/**
	 * Show the overlay for specific nodes
	 * @param {Object} options
	 * 
	 * 	String				options.id		- MUST - Define the id of the overlay (is needed for the hiding of this overlay)		
	 *	ORYX.Core.Shape[] 	options.shapes 	- MUST - Define the Shapes for the changes
	 * 	attr-name:value		options.changes	- Defines all the changes which should be shown
	 * 
	 * 
	 */
	show: function( options ){
		
		// Checks if all arguments are available
		if( 	!options || 
				!options.shapes || !options.shapes instanceof Array ||
				!options.id	|| !options.id instanceof String || options.id.length == 0) { 
				
					return
					
		}
		
		//if( this.changes[options.id]){
		//	this.hide( options )
		//}
			

		// Checked if attributes are setted
		if( options.attributes ){
			
			// FOR EACH - Shape
			options.shapes.each(function(el){
				
				// Checks if the node is a Shape
				if( !el instanceof ORYX.Core.Shape){ return }
				
				this.setAttributes( el.node , options.attributes )
				
			}.bind(this))

		}	
		
		var isSVG = true
		try {
			isSVG = options.node && options.node instanceof SVGElement;
		} catch(e){}
		
		// Checks if node is setted and if this is an SVGElement		
		if ( options.node && isSVG) {
			
			options["_temps"] = []
						
			// FOR EACH - Node
			options.shapes.each(function(el, index){
				
				// Checks if the node is a Shape
				if( !el instanceof ORYX.Core.Shape){ return }
				
				var _temp = {}
				_temp.svg = options.dontCloneNode ? options.node : options.node.cloneNode( true );
				
				// Add the svg node to the ORYX-Shape
				el.node.firstChild.appendChild( _temp.svg )		
				
				// If
				if (el instanceof ORYX.Core.Edge && !options.nodePosition) {
					options['nodePosition'] = "START"
				}
						
				// If the node position is setted, it has to be transformed
				if( options.nodePosition ){
					
					var b = el.bounds;
					var p = options.nodePosition.toUpperCase();
										
					// Check the values of START and END
					if( el instanceof ORYX.Core.Node && p == "START"){
						p = "NW";
					} else if(el instanceof ORYX.Core.Node && p == "END"){
						p = "SE";
					} else if(el instanceof ORYX.Core.Edge && p == "START"){
						b = el.getDockers().first().bounds
					} else if(el instanceof ORYX.Core.Edge && p == "END"){
						b = el.getDockers().last().bounds
					}

					// Create a callback for the changing the position 
					// depending on the position string
					_temp.callback = function(){
						
						var x = 0; var y = 0;
						
						if( p == "NW" ){
							// Do Nothing
						} else if( p == "N" ) {
							x = b.width() / 2;
						} else if( p == "NE" ) {
							x = b.width();
						} else if( p == "E" ) {
							x = b.width(); y = b.height() / 2;
						} else if( p == "SE" ) {
							x = b.width(); y = b.height();
						} else if( p == "S" ) {
							x = b.width() / 2; y = b.height();
						} else if( p == "SW" ) {
							y = b.height();
						} else if( p == "W" ) {
							y = b.height() / 2;
						} else if( p == "START" || p == "END") {
							x = b.width() / 2; y = b.height() / 2;
						}						
						else {
							return
						}
						
						if( el instanceof ORYX.Core.Edge){
							x  += b.upperLeft().x ; y  += b.upperLeft().y ;
						}
						
						_temp.svg.setAttributeNS(null, "transform", "translate(" + x + ", " + y + ")")
					
					}.bind(this)
					
					_temp.element = el;
					_temp.callback();
					
					b.registerCallback( _temp.callback );
					
				}
				
				// Show the ghostpoint
				if(options.ghostPoint){
					var point={x:0, y:0};
					point=options.ghostPoint;
					_temp.callback = function(){
						
						var x = 0; var y = 0;
						x = point.x -7;
						y = point.y -7;
						_temp.svg.setAttributeNS(null, "transform", "translate(" + x + ", " + y + ")")
						
					}.bind(this)
					
					_temp.element = el;
					_temp.callback();
					
					b.registerCallback( _temp.callback );
				}
				
				if(options.labelPoint){
					var point={x:0, y:0};
					point=options.labelPoint;
					_temp.callback = function(){
						
						var x = 0; var y = 0;
						x = point.x;
						y = point.y;
						_temp.svg.setAttributeNS(null, "transform", "translate(" + x + ", " + y + ")")
						
					}.bind(this)
					
					_temp.element = el;
					_temp.callback();
					
					b.registerCallback( _temp.callback );
				}
				
				
				options._temps.push( _temp )	
				
			}.bind(this))
			
			
			
		}		
	

		// Store the changes
		if( !this.changes[options.id] ){
			this.changes[options.id] = [];
		}
		
		this.changes[options.id].push( options );
				
	},
	
	/**
	 * Hide the overlay with the spefic id
	 * @param {Object} options
	 */
	hide: function( options ){
		
		// Checks if all arguments are available
		if( 	!options || 
				!options.id	|| !options.id instanceof String || options.id.length == 0 ||
				!this.changes[options.id]) { 
				
					return
					
		}		
		
		
		// Delete all added attributes
		// FOR EACH - Shape
		this.changes[options.id].each(function(option){
			
			option.shapes.each(function(el, index){
				
				// Checks if the node is a Shape
				if( !el instanceof ORYX.Core.Shape){ return }
				
				this.deleteAttributes( el.node )
							
			}.bind(this));

	
			if( option._temps ){
				
				option._temps.each(function(tmp){
					// Delete the added Node, if there is one
					if( tmp.svg && tmp.svg.parentNode ){
						tmp.svg.parentNode.removeChild( tmp.svg )
					}
		
					// If 
					if( tmp.callback && tmp.element){
						// It has to be unregistered from the edge
						tmp.element.bounds.unregisterCallback( tmp.callback )
					}
							
				}.bind(this))
				
			}
		
			
		}.bind(this));

		
		this.changes[options.id] = null;
		
		
	},
	
	
	/**
	 * Set the given css attributes to that node
	 * @param {HTMLElement} node
	 * @param {Object} attributes
	 */
	setAttributes: function( node, attributes ) {
		
		
		// Get all the childs from ME
		var childs = this.getAllChilds( node.firstChild.firstChild )
		
		var ids = []
		
		// Add all Attributes which have relation to another node in this document and concate the pure id out of it
		// This is for example important for the markers of a edge
		childs.each(function(e){ ids.push( $A(e.attributes).findAll(function(attr){ return attr.nodeValue.startsWith('url(#')}) )})
		ids = ids.flatten().compact();
		ids = ids.collect(function(s){return s.nodeValue}).uniq();
		ids = ids.collect(function(s){return s.slice(5, s.length-1)})
		
		// Add the node ID to the id
		ids.unshift( node.id + ' .me')
		
		var attr				= $H(attributes);
        var attrValue			= attr.toJSON().gsub(',', ';').gsub('"', '');
        var attrMarkerValue		= attributes.stroke ? attrValue.slice(0, attrValue.length-1) + "; fill:" + attributes.stroke + ";}" : attrValue;
        var attrTextValue;
        if( attributes.fill ){
            var copyAttr        = Object.clone(attributes);
        	copyAttr.fill		= "black";
        	attrTextValue		= $H(copyAttr).toJSON().gsub(',', ';').gsub('"', '');
        }
                	
        // Create the CSS-Tags Style out of the ids and the attributes
        csstags = ids.collect(function(s, i){return "#" + s + " * " + (!i? attrValue : attrMarkerValue) + "" + (attrTextValue ? " #" + s + " text * " + attrTextValue : "") })
		
		// Join all the tags
		var s = csstags.join(" ") + "\n" 
		
		// And add to the end of the style tag
		this.styleNode.appendChild(document.createTextNode(s));
		
		
	},
	
	/**
	 * Deletes all attributes which are
	 * added in a special style sheet for that node
	 * @param {HTMLElement} node 
	 */
	deleteAttributes: function( node ) {
				
		// Get all children which contains the node id		
		var delEl = $A(this.styleNode.childNodes)
					 .findAll(function(e){ return e.textContent.include( '#' + node.id ) });
		
		// Remove all of them
		delEl.each(function(el){
			el.parentNode.removeChild(el);
		});		
	},
	
	getAllChilds: function( node ){
		
		var childs = $A(node.childNodes)
		
		$A(node.childNodes).each(function( e ){ 
		        childs.push( this.getAllChilds( e ) )
		}.bind(this))

    	return childs.flatten();
	}

    
});
/**
};
    ORYX.Plugins = new Object();

ORYX.Plugins.PluginLoader = Clazz.extend({
	
    facade: undefined,
	mask: undefined,
	processURI: undefined,
	
    construct: function(facade){
		this.facade = facade;
		
		/*this.facade.offer({
			'name': ORYX.I18N.PluginLoad.AddPluginButtonName,
			'functionality': this.showManageDialog.bind(this),
			'group': ORYX.I18N.SSExtensionLoader.group,
			'icon': ORYX.PATH + "images/labs/script_add.png",
			'description': ORYX.I18N.PluginLoad.AddPluginButtonDesc,
			'index': 8,
			'minShape': 0,
			'maxShape': 0
		});*/},
	showManageDialog: function(){
			this.mask = new Ext.LoadMask(Ext.getBody(), {msg:ORYX.I18N.Oryx.pleaseWait});
			this.mask.show();
	var data=[];
	//(var plugins=this.facade.getAvailablePlugins();
	var plugins=[];
	var loadedStencilSetsNamespaces = this.facade.getStencilSets().keys();
	//get all plugins which could be acivated
	this.facade.getAvailablePlugins().each(function(match) {
	if ((!match.requires 	|| !match.requires.namespaces 	
			|| match.requires.namespaces.any(function(req){ return loadedStencilSetsNamespaces.indexOf(req) >= 0 }) )
		&&(!match.notUsesIn 	|| !match.notUsesIn.namespaces 	
				|| !match.notUsesIn.namespaces.any(function(req){ return loadedStencilSetsNamespaces.indexOf(req) >= 0 }))){
		plugins.push( match );

	}});
	
	plugins.each(function(plugin){
			data.push([plugin.name, plugin.engaged===true]);
			})
		if(data.length==0){return};
		var reader = new Ext.data.ArrayReader({}, [
        {name: 'name'},
		{name: 'engaged'} ]);
		
		var sm = new Ext.grid.CheckboxSelectionModel({
			listeners:{
			beforerowselect: function(sm,nbr,exist,rec){
			this.mask = new Ext.LoadMask(Ext.getBody(), {msg:ORYX.I18N.Oryx.pleaseWait});
			this.mask.show();
				this.facade.activatePluginByName(rec.data.name, 
						function(sucess,err){
						this.mask.hide();

							if(!!sucess){
								sm.suspendEvents();
								sm.selectRow(nbr, true);
								sm.resumeEvents();
							}else{
								Ext.Msg.show({
		   							   title: ORYX.I18N.PluginLoad.loadErrorTitle,
									   msg: ORYX.I18N.PluginLoad.loadErrorDesc + ORYX.I18N.PluginLoad[err],
									   buttons: Ext.MessageBox.OK
									});
							}}.bind(this));
				return false;
				}.bind(this),
			rowdeselect: function(sm,nbr,rec){
						sm.suspendEvents();
						sm.selectRow(nbr, true);
						sm.resumeEvents();
					}
			}});
	    var grid2 = new Ext.grid.GridPanel({
	    		store: new Ext.data.Store({
		            reader: reader,
		            data: data
		        	}),
		        cm: new Ext.grid.ColumnModel([
		            
		            {id:'name',width:390, sortable: true, dataIndex: 'name'},
					sm]),
			sm: sm,
	        width:450,
	        height:250,
	        frame:true,
			hideHeaders:true,
	        iconCls:'icon-grid',
			listeners : {
				render: function() {
					var recs=[];
					this.grid.getStore().each(function(rec){

						if(rec.data.engaged){
							recs.push(rec);
						}
					}.bind(this));
					this.suspendEvents();
					this.selectRecords(recs);
					this.resumeEvents();
				}.bind(sm)
			}
	    });

		var newURLWin = new Ext.Window({
					title:		ORYX.I18N.PluginLoad.WindowTitle, 
					//bodyStyle:	"background:white;padding:0px", 
					width:		'auto', 
					height:		'auto',
					modal:		true
					//html:"<div style='font-weight:bold;margin-bottom:10px'></div><span></span>",
				});
		newURLWin.add(grid2);
		newURLWin.show();
		this.mask.hide();

		}
		})
			/**
 * Copyright (c) 2009
 * Sven Wagner-Boysen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/

/**
   @namespace Oryx name space for plugins
   @name ORYX.Plugins
*/
 if(!ORYX.Plugins)
	ORYX.Plugins = new Object();
	

/**
 * This plugin provides methods to layout elements that typically contain 
 * a bunch of child elements, such as subprocesses or lanes.
 * 
 * @class ORYX.Plugins.ContainerLayouter
 * @extends ORYX.Plugins.AbstractPlugin
 * @param {Object} facade
 * 		The facade of the Editor
 */
ORYX.Plugins.ContainerLayouter = {

	/**
	 *	Constructor
	 *	@param {Object} Facade: The Facade of the Editor
	 */
	construct: function(facade){
		this.facade = facade;

		// this does NOT work, because lanes and pools are loaded at start and initialized with a default size
		// if the lane was saved and had a bigger size, the dockers/edges will be corrupted, because the first 
		// positioning is handled as a resize event which triggers the layout with incorrect oldBounds!
		
		//this.facade.registerOnEvent('layout.container.minBounds', 
		//							this.handleLayoutContainerMinBounds.bind(this));
		//this.facade.registerOnEvent('layout.container.dockers', 
		//							this.handleLayoutContainerDockers.bind(this));
		
		this.hashedContainers = new Hash();
	},
	
	handleLayoutContainerDockers: function(event) {
		var sh = event.shape;
		
		if (!this.hashedContainers[sh.resourceId]) {
			this.hashedContainers[sh.resourceId] = sh.bounds.clone();
			return;
		}
		
		var offset = sh.bounds.upperLeft();
		offset.x -= this.hashedContainers[sh.resourceId].upperLeft().x;
		offset.y -= this.hashedContainers[sh.resourceId].upperLeft().y;
		
		this.hashedContainers[sh.resourceId] = sh.bounds.clone();
		
		this.moveChildDockers(sh, offset);
	},
	
	/**
	 * 
	 * 
	 * @param {Object} event
	 * 		The layout event object
	 */
	handleLayoutContainerMinBounds: function(event) {
		var shape = event.shape;
		var topOffset = event.topOffset;
		var oldBounds = shape._oldBounds;
		var options = event.options;
		var ignoreList = (options.ignoreChildsWithId ? options.ignoreChildsWithId : new Array());
		
		var childsBounds = this.retrieveChildsIncludingBounds(shape, ignoreList);
		if(!childsBounds) {return;}
		
		/* Get the upper left child shape */
		var ulShape = this.getChildShapesWithout(shape, ignoreList).find(function(node) {
			return childsBounds.upperLeft().y == node.bounds.upperLeft().y;
		});
		
		/* Ensure minimum size of the container */
		if(this.ensureContainersMinimumSize(shape, childsBounds, ulShape.absoluteBounds(), ignoreList, options)) {
			return;
		};
		
		
		var childsUl = childsBounds.upperLeft();
		var childsLr = childsBounds.lowerRight();
		var bottomTopSpaceRatio = (childsUl.y ? childsUl.y : 1) / 
				((oldBounds.height() - childsLr.y) ? (oldBounds.height() - childsLr.y) : 1);
		
		var newYValue = bottomTopSpaceRatio * (shape.bounds.height() - childsBounds.height())
						/ (1 + bottomTopSpaceRatio );
		
		this.getChildShapesWithout(shape, ignoreList).each(function(childShape){
			var innerOffset = childShape.bounds.upperLeft().y - childsUl.y;
			childShape.bounds.moveTo({	x: childShape.bounds.upperLeft().x,	
										y: newYValue + innerOffset});
		});
		
		/* Calculate adjustment for dockers */
		var yAdjustment = ulShape.bounds.upperLeft().y - ulShape._oldBounds.upperLeft().y;
		
		/* Move docker by adjustment */
		this.moveChildDockers(shape, {x: 0, y: yAdjustment});
	},
	
	/**
	 * Ensures that the container has a minimum height and width to place all
	 * child elements inside.
	 * 
	 * @param {Object} shape
	 * 		The container.
	 * @param {Object} childsBounds
	 * 		The bounds including all children
	 * @param {Object} ulChildAbsBounds
	 * 		The absolute bounds including all children
	 */
	ensureContainersMinimumSize: function(shape, childsBounds, ulChildAbsBounds, ignoreList, options) {
		var bounds = shape.bounds;
		var ulShape = bounds.upperLeft();
		var lrShape = bounds.lowerRight();
		var ulChilds = childsBounds.upperLeft();
		var lrChilds = childsBounds.lowerRight();
		var absBounds = shape.absoluteBounds();
		if(!options) {
			options = new Object();
		}
		
		if(!shape.isResized) {
			/* Childs movement after widening the conatiner */
			var yMovement = 0;
			var xMovement = 0;
			var changeBounds = false;
			
			/* Widen the shape by the child bounds */
			var ulx = ulShape.x;
			var uly = ulShape.y;
			var lrx = lrShape.x;
			var lry = lrShape.y;
			
			if(ulChilds.x < 0) {
				ulx += ulChilds.x;
				xMovement -= ulChilds.x;
				changeBounds = true;
			}
			
			if(ulChilds.y < 0) {
				uly += ulChilds.y;
				yMovement -= ulChilds.y;
				changeBounds = true;
			}
			
			var xProtrusion = xMovement + ulChilds.x + childsBounds.width()
								- bounds.width();
			if(xProtrusion > 0) {
				lrx += xProtrusion;
				changeBounds = true;
			}
			
			var yProtrusion = yMovement + ulChilds.y + childsBounds.height()
								- bounds.height();
			if(yProtrusion > 0) {
				lry += yProtrusion;
				changeBounds = true;
			}
			
			bounds.set(ulx, uly, lrx, lry);
			
			/* Update hashed bounds for docker positioning */
			if(changeBounds) {
				this.hashedContainers[shape.resourceId] = bounds.clone();
			}
			
			this.moveChildsBy(shape, {x: xMovement, y: yMovement}, ignoreList);
			
			/* Signals that children are already move to correct position */
			return true;
		}
		
		/* Resize container to minimum size */
		
		var ulx = ulShape.x;
		var uly = ulShape.y;
		var lrx = lrShape.x;
		var lry = lrShape.y;
		changeBounds = false;
			
		/* Ensure height */
		if(bounds.height() < childsBounds.height()) {
			/* Shape was resized on upper left in height */
			if(ulShape.y != shape._oldBounds.upperLeft().y &&
				lrShape.y == shape._oldBounds.lowerRight().y) {
				uly = lry - childsBounds.height() - 1;	
				if(options.fixedY) {
					uly -= childsBounds.upperLeft().y;
				}
				changeBounds = true;
			} 
			/* Shape was resized on lower right in height */
			else if(ulShape.y == shape._oldBounds.upperLeft().y &&
				lrShape.y != shape._oldBounds.lowerRight().y) {
				lry = uly + childsBounds.height() + 1;	
				if(options.fixedY) {
					lry += childsBounds.upperLeft().y;
				}
				changeBounds = true;
			} 
			/* Both upper left and lower right changed */
			else if(ulChildAbsBounds) {
				var ulyDiff = absBounds.upperLeft().y - ulChildAbsBounds.upperLeft().y;
				var lryDiff = absBounds.lowerRight().y - ulChildAbsBounds.lowerRight().y;
				uly -= ulyDiff;
				lry -= lryDiff;
				uly--;
				lry++;
				changeBounds = true;
			}
		}
		
		/* Ensure width */
		if(bounds.width() < childsBounds.width()) {
			/* Shape was resized on upper left in height */
			if(ulShape.x != shape._oldBounds.upperLeft().x &&
				lrShape.x == shape._oldBounds.lowerRight().x) {
				ulx = lrx - childsBounds.width() - 1;
				if(options.fixedX) {
					ulx -= childsBounds.upperLeft().x;
				}	
				changeBounds = true;
			} 
			/* Shape was resized on lower right in height */
			else if(ulShape.x == shape._oldBounds.upperLeft().x &&
				lrShape.x != shape._oldBounds.lowerRight().x) {
				lrx = ulx + childsBounds.width() + 1;
				if(options.fixedX) {
					lrx += childsBounds.upperLeft().x;
				}	
				changeBounds = true;
			} 
			/* Both upper left and lower right changed */
			else if(ulChildAbsBounds) {
				var ulxDiff = absBounds.upperLeft().x - ulChildAbsBounds.upperLeft().x;
				var lrxDiff = absBounds.lowerRight().x - ulChildAbsBounds.lowerRight().x;
				ulx -= ulxDiff;
				lrx -= lrxDiff;
				ulx--;
				lrx++;
				changeBounds = true;
			}
		}
		
		/* Set minimum bounds */
		bounds.set(ulx, uly, lrx, lry);
		if(changeBounds) {
			//this.hashedContainers[shape.resourceId] = bounds.clone();
			this.handleLayoutContainerDockers({shape:shape});
		}
	},
	
	/**
	 * Moves all child shapes and related dockers of the container shape by the 
	 * relative move point.
	 * 
	 * @param {Object} shape
	 * 		The container shape
	 * @param {Object} relativeMovePoint
	 * 		The point that defines the movement
	 */
	moveChildsBy: function(shape, relativeMovePoint, ignoreList) {
		if(!shape || !relativeMovePoint) {
			return;
		}
		
		/* Move child shapes */
		this.getChildShapesWithout(shape, ignoreList).each(function(child) {
			child.bounds.moveBy(relativeMovePoint);
		});
		
		/* Move related dockers */
		//this.moveChildDockers(shape, relativeMovePoint);
	},
	
	/**
	 * Retrieves the absolute bounds that include all child shapes.
	 * 
	 * @param {Object} shape
	 */
	getAbsoluteBoundsForChildShapes: function(shape) {
//		var childsBounds = this.retrieveChildsIncludingBounds(shape);
//		if(!childsBounds) {return undefined}
//		
//		var ulShape = shape.getChildShapes(false).find(function(node) {
//			return childsBounds.upperLeft().y == node.bounds.upperLeft().y;
//		});
//		
////		var lrShape = shape.getChildShapes(false).find(function(node) {
////			return childsBounds.lowerRight().y == node.bounds.lowerRight().y;
////		});
//		
//		var absUl = ulShape.absoluteBounds().upperLeft();
//		
//		this.hashedContainers[shape.getId()].childsBounds = 
//						new ORYX.Core.Bounds(absUl.x, 
//											absUl.y,
//											absUl.x + childsBounds.width(),
//											absUl.y + childsBounds.height());
//		
//		return this.hashedContainers[shape.getId()];
	},
	
	/**
	 * Moves the docker when moving shapes.
	 * 
	 * @param {Object} shape
	 * @param {Object} offset
	 */
	moveChildDockers: function(shape, offset){
		
		if (!offset.x && !offset.y) {
			return;
		} 
		
		// Get all nodes
		shape.getChildNodes(true)
			// Get all incoming and outgoing edges
			.map(function(node){
				return [].concat(node.getIncomingShapes())
						.concat(node.getOutgoingShapes())
			})
			// Flatten all including arrays into one
			.flatten()
			// Get every edge only once
			.uniq()
			// Get all dockers
			.map(function(edge){
				return edge.dockers.length > 2 ? 
						edge.dockers.slice(1, edge.dockers.length-1) : 
						[];
			})
			// Flatten the dockers lists
			.flatten()
			.each(function(docker){
				docker.bounds.moveBy(offset);
			})
	},
	
	/**
	 * Calculates the bounds that include all child shapes of the given shape.
	 * 
	 * @param {Object} shape
	 * 		The parent shape.
	 */
	retrieveChildsIncludingBounds: function(shape, ignoreList) {
		var childsBounds = undefined;
		this.getChildShapesWithout(shape, ignoreList).each(function(childShape, i) {
			if(i == 0) {
				/* Initialize bounds that include all direct child shapes of the shape */
				childsBounds = childShape.bounds.clone();
				return;
			}
			
			/* Include other child elements */
			childsBounds.include(childShape.bounds);			
		});
		
		return childsBounds;
	},
	
	/**
	 * Returns the direct child shapes that are not on the ignore list.
	 */
	getChildShapesWithout: function(shape, ignoreList) {
		var childs = shape.getChildShapes(false);
		return childs.findAll(function(child) {
					return !ignoreList.member(child.getStencil().id());				
				});
	}
}

ORYX.Plugins.ContainerLayouter = ORYX.Plugins.AbstractPlugin.extend(ORYX.Plugins.ContainerLayouter);
/**
 * Copyright (c) 2009
 * Willi Tscheschner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/

if(!ORYX.Plugins) { ORYX.Plugins = {} }
if(!ORYX.Plugins.Layouter) { ORYX.Plugins.Layouter = {} }

new function(){
	
	/**
	 * Edge layouter is an implementation to layout an edge
	 * @class ORYX.Plugins.Layouter.EdgeLayouter
	 * @author Willi Tscheschner
	 */
	ORYX.Plugins.Layouter.EdgeLayouter = ORYX.Plugins.AbstractLayouter.extend({
		
		/**
		 * Layout only Edges
		 */
		layouted : [	"http://b3mn.org/stencilset/bpmn1.1#SequenceFlow", 
						"http://b3mn.org/stencilset/bpmn1.1#MessageFlow",
						"http://b3mn.org/stencilset/bpmn2.0#MessageFlow",
						"http://b3mn.org/stencilset/bpmn2.0#SequenceFlow", 
						"http://b3mn.org/stencilset/bpmn2.0conversation#ConversationLink",
						"http://b3mn.org/stencilset/epc#ControlFlow",
						"http://www.signavio.com/stencilsets/processmap#ProcessLink",
						"http://www.signavio.com/stencilsets/organigram#connection"],
		
		/**
		 * Layout a set on edges
		 * @param {Object} edges
		 */
		layout: function(edges){
			edges.each(function(edge){
				this.doLayout(edge)
			}.bind(this))
		},
		
		/**
		 * Layout one edge
		 * @param {Object} edge
		 */
		doLayout: function(edge){
			// Get from and to node
			var from 	= edge.getIncomingNodes()[0]; 
			var to 		= edge.getOutgoingNodes()[0];
			
			// Return if one is null
			if (!from || !to) { return }
			
			var positions = this.getPositions(from, to, edge);
		
			if (positions.length > 0){
				this.setDockers(edge, positions[0].a, positions[0].b);
			}
				
		},
		
		/**
		 * Returns a set on positions which are not containt either 
		 * in the bounds in from or to.
		 * @param {Object} from Shape where the edge is come from
		 * @param {Object} to Shape where the edge is leading to
		 * @param {Object} edge Edge between from and to
		 */
		getPositions : function(from, to, edge){
			
			// Get absolute bounds
			var ab = from.absoluteBounds();
			var bb = to.absoluteBounds();
			
			// Get center from and to
			var a = ab.center();
			var b = bb.center();
			
			var am = ab.midPoint();
			var bm = bb.midPoint();
		
			// Get first and last reference point
			var first = Object.clone(edge.dockers.first().referencePoint);
			var last = Object.clone(edge.dockers.last().referencePoint);
			// Get the absolute one
			var aFirst = edge.dockers.first().getAbsoluteReferencePoint();
			var aLast = edge.dockers.last().getAbsoluteReferencePoint(); 
			
			// IF ------>
			// or  |
			//     V
			// Do nothing
			if (Math.abs(aFirst.x-aLast.x) < 1 || Math.abs(aFirst.y-aLast.y) < 1) {
				return []
			}
			
			// Calc center position, between a and b
			// depending on there weight
			var m = {}
			m.x = a.x < b.x ? 
					(((b.x - bb.width()/2) - (a.x + ab.width()/2))/2) + (a.x + ab.width()/2): 
					(((a.x - ab.width()/2) - (b.x + bb.width()/2))/2) + (b.x + bb.width()/2);

			m.y = a.y < b.y ? 
					(((b.y - bb.height()/2) - (a.y + ab.height()/2))/2) + (a.y + ab.height()/2): 
					(((a.y - ab.height()/2) - (b.y + bb.height()/2))/2) + (b.y + bb.height()/2);
								
								
			// Enlarge both bounds with 10
			ab.widen(5); // Wide the from less than 
			bb.widen(20);// the to because of the arrow from the edge
								
			var positions = [];
			var off = this.getOffset.bind(this);
			
			// Checks ----+
			//            |
			//            V
			if (!ab.isIncluded(b.x, a.y)&&!bb.isIncluded(b.x, a.y)) {
				positions.push({
					a : {x:b.x+off(last,bm,"x"),y:a.y+off(first,am,"y")},
					z : this.getWeight(from, a.x < b.x ? "r" : "l", to, a.y < b.y ? "t" : "b", edge)
				});
			}
						
			// Checks | 
			//        +--->
			if (!ab.isIncluded(a.x, b.y)&&!bb.isIncluded(a.x, b.y)) {
				positions.push({
					a : {x:a.x+off(first,am,"x"),y:b.y+off(last,bm,"y")},
					z : this.getWeight(from, a.y < b.y ? "b" : "t", to, a.x < b.x ? "l" : "r", edge)
				});
			}
						
			// Checks  --+
			//           |
			//           +--->
			if (!ab.isIncluded(m.x, a.y)&&!bb.isIncluded(m.x, b.y)) {
				positions.push({
					a : {x:m.x,y:a.y+off(first,am,"y")},
					b : {x:m.x,y:b.y+off(last,bm,"y")},
					z : this.getWeight(from, "r", to, "l", edge, a.x > b.x)
				});
			}
			
			// Checks | 
			//        +---+
			//            |
			//            V
			if (!ab.isIncluded(a.x, m.y)&&!bb.isIncluded(b.x, m.y)) {
				positions.push({
					a : {x:a.x+off(first,am,"x"),y:m.y},
					b : {x:b.x+off(last,bm,"x"),y:m.y},
					z : this.getWeight(from, "b", to, "t", edge, a.y > b.y)
				});
			}	
			
			// Sort DESC of weights
			return positions.sort(function(a,b){ return a.z < b.z ? 1 : (a.z == b.z ? -1 : -1)});
		},
		
		/**
		 * Returns a offset for the pos to the center of the bounds
		 * 
		 * @param {Object} val
		 * @param {Object} pos2
		 * @param {String} dir Direction x|y
		 */
		getOffset: function(pos, pos2, dir){
			return pos[dir] - pos2[dir];
		},
		
		/**
		 * Returns a value which shows the weight for this configuration
		 * 
		 * @param {Object} from Shape which is coming from
		 * @param {String} d1 Direction where is goes
		 * @param {Object} to Shape which goes to
		 * @param {String} d2 Direction where it comes to
		 * @param {Object} edge Edge between from and to
		 * @param {Boolean} reverse Reverse the direction (e.g. "r" -> "l")
		 */
		getWeight: function(from, d1, to, d2, edge, reverse){
			
			d1 = (d1||"").toLowerCase();
			d2 = (d2||"").toLowerCase();
			
			if (!["t","r","b","l"].include(d1)){ d1 = "r"}
			if (!["t","r","b","l"].include(d2)){ d1 = "l"}
			
			// If reverse is set
			if (reverse) {
				// Reverse d1 and d2
				d1 = d1=="t"?"b":(d1=="r"?"l":(d1=="b"?"t":(d1=="l"?"r":"r")))
				d2 = d2=="t"?"b":(d2=="r"?"l":(d2=="b"?"t":(d2=="l"?"r":"r")))
			}
			
					
			var weight = 0;
			// Get rules for from "out" and to "in"
			var dr1 = this.facade.getRules().getLayoutingRules(from, edge)["out"];
			var dr2 = this.facade.getRules().getLayoutingRules(to, edge)["in"];

			var fromWeight = dr1[d1];
			var toWeight = dr2[d2];


			/**
			 * Return a true if the center 1 is in the same direction than center 2
			 * @param {Object} direction
			 * @param {Object} center1
			 * @param {Object} center2
			 */
			var sameDirection = function(direction, center1, center2){
				switch(direction){
					case "t": return Math.abs(center1.x - center2.x) < 2 && center1.y < center2.y
					case "r": return center1.x > center2.x && Math.abs(center1.y - center2.y) < 2
					case "b": return Math.abs(center1.x - center2.x) < 2 && center1.y > center2.y
					case "l": return center1.x < center2.x && Math.abs(center1.y - center2.y) < 2
					default: return false;
				}
			}

			// Check if there are same incoming edges from 'from'
			var sameIncomingFrom = from
								.getIncomingShapes()
								.findAll(function(a){ return a instanceof ORYX.Core.Edge})
								.any(function(e){ 
									return sameDirection(d1, e.dockers[e.dockers.length-2].bounds.center(), e.dockers.last().bounds.center());
								});

			// Check if there are same outgoing edges from 'to'
			var sameOutgoingTo = to
								.getOutgoingShapes()
								.findAll(function(a){ return a instanceof ORYX.Core.Edge})
								.any(function(e){ 
									return sameDirection(d2, e.dockers[1].bounds.center(), e.dockers.first().bounds.center());
								});
			
			// If there are equivalent edges, set 0
			//fromWeight = sameIncomingFrom ? 0 : fromWeight;
			//toWeight = sameOutgoingTo ? 0 : toWeight;
			
			// Get the sum of "out" and the direction plus "in" and the direction 						
			return (sameIncomingFrom||sameOutgoingTo?0:fromWeight+toWeight);
		},
		
		/**
		 * Removes all current dockers from the node 
		 * (except the start and end) and adds two new
		 * dockers, on the position a and b.
		 * @param {Object} edge
		 * @param {Object} a
		 * @param {Object} b
		 */
		setDockers: function(edge, a, b){
			if (!edge){ return }
			
			// Remove all dockers (implicit,
			// start and end dockers will not removed)
			edge.dockers.each(function(r){
				edge.removeDocker(r);
			});
			
			// For a and b (if exists), create
			// a new docker and set position
			[a, b].compact().each(function(pos){
				var docker = edge.createDocker(undefined, pos);
				docker.bounds.centerMoveTo(pos);
			});
			
			// Update all dockers from the edge
			edge.dockers.each(function(docker){
				docker.update()
			})
			
			// Update edge
			//edge.refresh();
			edge._update(true);
			
		}
	});
	
	
}()
if(!ORYX.Plugins)
	ORYX.Plugins = new Object();
if (!ORYX.Config) {
	ORYX.Config = new Object();
}
/*
 * http://oryx.processwave.org/gadget/oryx_stable.xml
 */
ORYX.Config.WaveThisGadgetUri = "http://ddj0ahgq8zch6.cloudfront.net/gadget/oryx_stable.xml";
ORYX.Plugins.WaveThis = Clazz.extend({
	
	/**
	 *	Constructor
	 *	@param {Object} Facade: The Facade of the Editor
	 */
	construct: function(facade) {
		this.facade = facade;
		/*this.facade.offer({
			'name':				ORYX.I18N.WaveThis.name,
			'functionality': 	this.waveThis.bind(this),
			'group': 			ORYX.I18N.WaveThis.group,
			'icon': 			ORYX.PATH + "images/waveThis.png",
			'description': 		ORYX.I18N.WaveThis.desc,
            'dropDownGroupIcon':ORYX.PATH + "images/export2.png",

		});*/
		
		this.changeDifference = 0;
		
		// Register on events for executing commands and save, to monitor the changed status of the model 
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_UNDO_EXECUTE, function(){ this.changeDifference++ }.bind(this) );
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_EXECUTE_COMMANDS, function(){ this.changeDifference++ }.bind(this) );
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_UNDO_ROLLBACK, function(){ this.changeDifference-- }.bind(this) );
		
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_MODEL_SAVED, function(){ this.changeDifference =0}.bind(this) );

	},
	waveThis: function(){
		var modelUri;
		if(!location.hash.slice(1)){
			Ext.Msg.alert(ORYX.I18N.WaveThis.name, ORYX.I18N.WaveThis.failUnsaved);
			return;
		}
		else{
			modelUri = ORYX.CONFIG.WEB_URL+'/backend/poem/'+(location.hash.slice(1).replace(/^\/?/,"").replace(/\/?$/,""))+"/json";
		}
		if(this.changeDifference!=0){
	        Ext.Msg.confirm(ORYX.I18N.WaveThis.name, "You have unsaved changes in your model. Proceed?", function(id){
	        	if(id=="yes"){
	        		this._openWave(modelUri);
	        	}
	        },this);
		}else{
			this._openWave(modelUri);
		}
		
	},
	_openWave: function(modelUri){
		var win = window.open("");
		if (win != null) {
			win.document.open();
			win.document.write("<html><body>");
			var submitForm = win.document.createElement("form");
			win.document.body.appendChild(submitForm);
			
			var createHiddenElement = function(name, value) {
				var newElement = document.createElement("input");
				newElement.name=name;
				newElement.type="hidden";
				newElement.value = value;
				return newElement
			}
			
			submitForm.appendChild( createHiddenElement("u", modelUri) );
			submitForm.appendChild( createHiddenElement("g", ORYX.Config.WaveThisGadgetUri) );
			
			
			submitForm.method = "POST";
			win.document.write("</body></html>");
			win.document.close();
			submitForm.action= "https://wave.google.com/wave/wavethis?t=Oryx%20Model%20Export";
			submitForm.submit();
		}
	}
})/**
		})) {
	}