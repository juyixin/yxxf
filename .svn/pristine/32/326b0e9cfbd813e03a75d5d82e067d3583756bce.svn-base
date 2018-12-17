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

