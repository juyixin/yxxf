/* Interface to YUI TreeView object to easily define a tree */
/* By JavaScriptKit.com- http://www.javascriptkit.com       */
/* Last updated: Dec 13th, 2006                             */

function jktreeview(treeid){
	this.treetop=new YAHOO.widget.TreeView(treeid)
	this.treetop.setDynamicLoad(loadNodeData, '')
}

jktreeview.prototype.addItem=function(itemText, noderef, href){
	var noderef=(typeof noderef!="undefined" && noderef!="")? noderef : this.treetop.getRoot()
	var treebranch=new YAHOO.widget.TextNode(itemText, noderef, false)
	if (typeof href!="undefined")
		treebranch.href=href
	return treebranch
}

/**
 * Extended additem method to support dynamic javascript function
 */

jktreeview.prototype.addItem=function(itemText, noderef, href,functionToInvoke) {
	//alert(itemText+"====="+noderef+"========="+this.treetop.getRoot()+"========="+href+"========"+functionToInvoke);
	var noderef=(typeof noderef!="undefined" && noderef!="")? noderef : this.treetop.getRoot()
	var treebranch=new YAHOO.widget.TextNode(itemText, noderef, false);
	if (typeof href!="undefined"){
		treebranch.href=href
	}
	if(typeof functionToInvoke != "undefined"){
		treebranch.functionToInvoke = functionToInvoke
	}
	return treebranch
}

function loadNodeData(node, fnLoadComplete)  {
	//console.log(node);
	var parentNode = "";
	var FunctionToInvokeOfparentNode = node.functionToInvoke
	var nodeLabel = node.data;
	//alert(FunctionToInvokeOfparentNode);
	if(FunctionToInvokeOfparentNode != null) {
		var parentNodes = FunctionToInvokeOfparentNode.split(",");
		parentNode = parentNodes[1];
	} else {
		parentNode = node.parent.data;
	}
	//alert("===func=============="+nodeLabel+"========"+parentNode+"===="+fnLoadComplete);
		if(nodeLabel !== "Departments" && (parentNode == "'departments'" || parentNode == "null" )) {
			//alert("===call===dep");
			Ext.Ajax.request({
				waitMsg: 'Saving changes...',
				method: 'GET',
				url:'/admin/getDepartmentsBySuperDep',
				params:{superDep:nodeLabel},
				failure:function(response){
				    fnLoadComplete();
				},
				success:function(response){
					var responseObj = Ext.util.JSON.decode(response.responseText);
					if(responseObj != null) {
						for(var i=0;i<responseObj.length;i++) {
						     var tempNode = new YAHOO.widget.TextNode(responseObj[i].viewName, node, false);
						     tempNode.data = responseObj[i].id;
						     var regex = /\('.*?',/;
						     var method = FunctionToInvokeOfparentNode.replace(regex, "('"+responseObj[i].id+"',");
						    // alert(method);
						     tempNode.functionToInvoke = method;
						}
					     fnLoadComplete();
					}
				}
			});
		} else if(nodeLabel != "Groups" && (parentNode == "'groups'" || parentNode == "null" )) {
			//alert("===call===gr");
			if(nodeLabel != "Groups") {
				Ext.Ajax.request({
					waitMsg: 'Saving changes...',
					method: 'GET',
					url:'/admin/getChildGroups',
					params:{groupId:nodeLabel},
					failure:function(response){
					    fnLoadComplete();
					},
					success:function(response){
						var responseObj = Ext.util.JSON.decode(response.responseText);
						if(responseObj != null) {
							for(var i=0;i<responseObj.length;i++) {
							     var tempNode = new YAHOO.widget.TextNode(responseObj[i].viewName, node, false);
							     var regex = /\('.*?',/;
							     var method = FunctionToInvokeOfparentNode.replace(regex, "('"+responseObj[i].id+"',");
							     //alert(method);
							     tempNode.functionToInvoke = method;
							}
						     fnLoadComplete();
						}
					}
				});
			} else {
			    fnLoadComplete();
			}
		} else {
			//alert("===no===call===");
		    fnLoadComplete();
		}
}
