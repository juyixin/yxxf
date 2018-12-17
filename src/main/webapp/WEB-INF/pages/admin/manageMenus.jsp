<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="menuList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<script type="text/javascript">

function constructMenuTree(){
$.ajaxSetup({ cache: false });
	 $("#menu_js_tree").jstree({
	      "json_data" : {
	        "ajax" : {
	        	"url" : function( node ){
	            	if( node == -1 ){
	                	return "bpm/admin/getTreeNode?currentNode=&rootNode=&nodeLevel=0"
	                } else {
	                	var nodeLevel = 1;
   	                	var currentNode = node.context.parentNode.getAttribute("id");
   	                	var rootNode = node.parents("li").attr("id");
   	                	if(currentNode == "top" || currentNode == "header"){
   	                		rootNode = currentNode;
   	                	}else{
   	                		nodeLevel = 2;
   	                	}
						return "bpm/admin/getTreeNode?currentNode="+currentNode+"&rootNode="+rootNode+"&nodeLevel="+nodeLevel
	                 }
	            },
	            "type" : "get", 
	            "cache": false, 
	            "success" : function(ops) {
			    setTimeout(function(){
			          data = []
			          for( opnum in ops ){
			            var op = ops[opnum]
			            node = {
		           		 "data" : op.data,
				             "metadata" :  op.metadata ,
				             "attr"   :op.attr,
				             "state" : "closed"
			            }
			            data.push( node );
			          }
			          return data; 
			    },100);
	            }
	         }
	      },
	      "plugins" : [ "themes", "json_data", "ui", "crrm" ]
	  }).bind("select_node.jstree", function (e, data) {
	    	var currentNode = data.rslt.obj.attr("id");
	    	var rootNode = data.inst.get_path()[0];
	    	var level = data.inst.get_path().length;
	    	if(level > 1){
	    		var params = 'id='+currentNode;
	        	document.location.href = "#bpm/admin/menuForm";
	        	_execute('menu_form',params);	
	    	}
	    	var currentNode = data.rslt.obj.attr("id");
       		$("li[id~='"+currentNode+"'] > ins.jstree-icon").trigger('click');
	    }); 
} 

$(document).ready(function(){
	var height = $("#target").height();
	$("#menu_form").css('height',parseInt(height)- 138);
	$("#menu_tree").css('height',parseInt(height)- 138);
	constructMenuTree();
}); 

</script>


<div class="row-fluid">
	<div id="header_links" class="page-header">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="user.manage.menus"/></h2>
		<div id="header_links" align="right" >
			<a class="padding3" onclick="deleteMenu();" href="javascript:void(0)" ><button class="btn"><fmt:message key="button.delete"/></button></a>
			<a class="padding3" onclick="_execute('menu_form','');refreshJSTree('menu_js_tree');" href="#bpm/admin/menuForm" ><button class="btn"><fmt:message key="button.createNew"/></button></a>
		</div>
	</div>
	  	<div class="span12">
			<div class="span3">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span><fmt:message key="menu.manageMenus.title"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="menu_tree" class="menuForm_border" >
							<div id="menu_js_tree" class="department_tree.jstree-focused "></div>
						</div>
						<div class="height10"></div>
					</div>
				</div>
			</div>
			<div class="span9">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span><fmt:message key="menu.manageMenus.create"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="menu_form" class="manageMenu_boder" >
			 				<%@ include file="/WEB-INF/pages/admin/menuForm.jsp" %>
						</div>
						<div class="height10"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
