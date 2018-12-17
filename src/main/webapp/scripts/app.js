/**
 * Backbone functionality has been done here.
 * Client side MVC architecture and SPA has been handled here
 * Every request has been send to server from here like form submission and other request except ajax call  
 * @author mahesh
 */

$(function(){	
	//alert("hhhj");
		//define master model
		var FormModel = Backbone.Model.extend({});
		
		//define collection
	    var BackboneCollection = Backbone.Collection.extend({ model: FormModel});
		
		 //define master view
	    var BackboneView = Backbone.View.extend({
			el: $("#default_body"),
			id : 'target-container',
			initialize : function() {
				 this.collection = new BackboneCollection();		
				 this.on("change:filterType", this.filterByType, this);
			},
	        filterByType: function () {
	        	var username = $.cookie('username');
	        	if(username == null){
	        	   document.location.href = "/logout";
	        	   return true;
	        	}
	        	var timestamp = new Date().getUTCMilliseconds();
	        	if(filterURL.indexOf("processEditor") == -1){
		        	if(_params != ""){					
		        		_params = _params+"&_windowid="+timestamp;
					} else {
						_params = "_windowid="+timestamp;
					}
	        	}
	        	var _hostURL = location.protocol+"//"+window.location.host+"/";
		           var navURL = filterURL;
		           if(navURL.indexOf("?") > -1){
		        	   navURL = filterURL+"&"+_params;
		           } else {
		        	   if(_params != ""){					
			        	   navURL = filterURL+"?"+_params;
			           }
		           }
		           if(filterURL.indexOf("processEditor") > -1){
		        	   //start rajmohan temp fix for save popup confirm that you want to leave - data you have entered may not be saved
		        	   $("#target_iframe").remove();
		        	   	var iframeEl = document.createElement('iframe');
		        	   	iframeEl.setAttribute("id", "target_iframe");
		        	    var iframeParent=document.getElementById('target_iframe_div');
		        	    iframeParent.appendChild(iframeEl);
		        	    //end
		        	    
		        	   $("#target_div").hide();
		        	   //var ajaxLoaderFrame = new ajaxLoader(document.getElementById('ajax_loader'));
				 		var height = $("#target").height();
				 		var iframeHeight = height - 15;
						document.getElementById('target_iframe').style.width = "99.5%";
				 		document.getElementById('target_iframe').style.height = iframeHeight+"px";
				 		document.getElementById('target_iframe').style.padding = "3px";
				 		document.getElementById('target_iframe').style.border = "0px";
				 		if(_params != ""){
				 			document.getElementById('target_iframe').setAttribute('SRC', 'bpm/process/processEditor'+"?"+encodeURI(_params));
				 		}else{
				 			document.getElementById('target_iframe').setAttribute('SRC', 'bpm/process/processEditor');
				 		}
				 		$("#target_iframe_div").show();
				 		//if (ajaxLoaderFrame) ajaxLoaderFrame.remove();
				 	}else{
				 		 var ajaxLoaderDiv = new ajaxLoader(document.getElementById('ajax_loader'));
				 		 $.get(navURL, function(template){
				 			 if(template.indexOf('default_body') > -1){
				 				 document.location.href = "#bpm/user/homePage";
				 				_mainContainer = "target";
				 			 }else{
				 				 if(template.indexOf('<body id="login">') > -1){
					 				 document.location.href = "/logout";
					 			 }else{
					 				 if(_mainContainer == 'target'){
					 					var breadCrumb = '';
					 					breadCrumb = createBreadCrumb();
					 					template = breadCrumb+template;
					 				 }
				 				     $("#target_iframe_div").hide();
									 $("#"+_mainContainer).html("");
									 //alert(_mainContainer);
									 if (ajaxLoaderDiv) ajaxLoaderDiv.remove();
									 $("#target_div").show();
								 	 $("#"+_mainContainer).html(template);
					 			 } 
				 			 }
						   })
				 		 .done(function() {  })
				 		 .fail(function() {
				 			 alert("数据加载失败，请刷新重试!");
				 			 if (ajaxLoaderDiv) ajaxLoaderDiv.remove();
				 		 });
				 	}
		           _params="";
    		},
			render : function(eventName) {				
			},	
			events: {
				 "submit form": "submitForm",
			},
			submitForm: function (e) {
				e.preventDefault();
				 var ajaxLoaderDiv = new ajaxLoader(document.getElementById('ajax_loader'));
				 var username = $.cookie('username');
				 if(username == null){
	        	   document.location.href = "/logout";
	        	   return true;
				 }
				 var _hostURL = location.protocol+"//"+window.location.host+"/";
				 var action = $(e.target).attr("action");
				 var formParams = action+"?"+_params;
				 $(e.target).attr("action", formParams);
				 $(e.target).ajaxSubmit(function(template){ 
					 if(template.indexOf('default_body') > -1){
		 				 document.location.href = "#bpm/user/homePage";
		 				_mainContainer = "target";
		 			 }else{
			 			 if(template.indexOf('<body id="login">') > -1){
			 				 document.location.href = "/logout";
			 			 }else{
			 				if(_mainContainer == 'target'){
			 					var breadCrumb = '';
			 					breadCrumb = createBreadCrumb();
			 					template = breadCrumb+template;
			 				 }
			 				    $("#target_iframe_div").hide();
								$("#"+_mainContainer).html("");
							 	if (ajaxLoaderDiv) ajaxLoaderDiv.remove();
							 	$("#"+_mainContainer).html(template);
						 		$("#"+_mainContainer).show(); 
			 			 }
		 			 }
				 });
	        	 return false;
			 }
		});

	    //add routing
	    var WorkspaceRouter = Backbone.Router.extend({
	    	// Routes navigate handle corresponding request
	        routes: {
	            "bpm/*action": "_renderContent"
	        },
	        _renderContent: function (e) {
	        	var actualURL = e;
	        	// Backbone was not handle the same request call at a time 
	        	// Here we achieve through append timestamp on url
	        	var urlTimestamp = "";
	        	var timestamp = new Date().getUTCMilliseconds();
	        	if(e.indexOf("_windowid") > -1){
	        		e = e.substring(0, e.indexOf("/_windowid"));
	        		urlTimestamp = "bpm/"+ e + "/_windowid=" + timestamp;
	        		// Here done the browser back functionality
	        		var localHistory = eval(localHistoryStorage);
	        		var localHistoryLength = localHistory.length;
		        	if(localHistoryLength > 0){
		        		for(var index = 0; index < localHistory.length; index++){
		        			var localHistoryData = eval('('+localHistory[index]+')');
		        			var currentURL = "_"+actualURL.substring(0, actualURL.indexOf("/_windowid")).replace("/","_");
		        			$.each(localHistoryData, function(key, val) {
		        				if(key == currentURL){
		        					_mainContainer = val.container;
		        					if(val.params != ""){
		        			    		_params = val.params;
		        			    	}else{
		        			    		_params = "";
		        			    	}
		        					if(val.menuName != ""){
		        						showSideMenu(val.menuName ,val.tableId);
		        					}else{
		        						showSideMenu(prevMenuName ,val.tableId);
		        					}
		        					
		        				}
			        	 	});
		        		}
		        	}
	        		
	        	}else{
	        		urlTimestamp = "bpm/"+ e + "/_windowid=" + timestamp;
	        	}
	        	workspaceRouter.navigate(urlTimestamp, {trigger: false, replace: true});
	        	filterURL = "bpm/"+e;
	        	backboneView.filterType = "bpm/"+e;
	        	backboneView.trigger("change:filterType");
	        }
	    });

	    //create instance of master view
	    var backboneView = new BackboneView();
	    
	    //create router instance
	    var workspaceRouter = new WorkspaceRouter();
	    
	    //start history service
	    Backbone.history.start();
	});
