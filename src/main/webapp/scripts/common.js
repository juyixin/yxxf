function checkIdCard(object){
    //15位数身份证正则表达式
    var arg1 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
    //18位数身份证正则表达式
    var arg2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/;
    var s=$("#"+object.id).val();
    if (s.match(arg1) == null && s.match(arg2) == null) {
        validateMessageBox("错误","身份证格式不正确", "error");
		$("#"+object.id).val("");
    }
    else {
        return true;
    }
}

function checkNum(object){
	//校验数字类型
    var arg1 = /^(-?\d+)(\.\d+)?$/;　　　　　　　 
    var s=$("#"+object.id).val();
    if (s.match(arg1) == null) {
        validateMessageBox("错误","只能是数字格式", "error");
		$("#"+object.id).val("");
    }
    else {
        return true;
    }
}

function checkMobile(object){
	//校验手机号
    var arg1 = /^(((13[0-9]{1})|(15[0-9]{1})|(14[0-9]{1})|(18[0-9]{1}))+\d{8})$/;　　　　　　　 
    var s=$("#"+object.id).val();
    if (s.match(arg1) == null) {
        validateMessageBox("错误","只能是11位手机号", "error");
        $("#"+object.id).val("");
    }
    else {
        return true;
    }
}

function checkMail(object){
	//校验邮箱格式
    var arg1 = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;　　　　　　　 
    var s=$("#"+object.id).val();
    if (s.match(arg1) == null) {
        validateMessageBox("错误","邮箱格式不正确", "error");
		$("#"+object.id).val("");
    }
    else {
        return true;
    }
}

function clearNoNum(obj){	//验证金额，保证只能输入数字和一个小数点	
		
	obj.value = obj.value.replace(/[^\d.]/g,""); //先把非数字的都替换掉，除了数字和.						
	obj.value = obj.value.replace(/\.{2,}/g,".");	//保证只有出现一个.而没有多个.				
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");	//保证.只出现一次，而不能出现两次以上
	obj.value = obj.value.replace(/^\./g,"");		//必须保证第一个为数字而不是.	
}

function deleteDefpsData(object){
	var rowid =  gridObj.getGridParam('selarrrow');
	//alert(gridObj.html())
	var defpIds = new Array();
	rowid.forEach(function(item) {
		//var defpId = gridObj.jqGrid('getCell', item, 'id');
		defpIds[defpIds.length] = item;
	});
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		if(object=="financerDelete"){
			deleteAllFinancer(defpIds);
		}else if(object=="scoreRangeDelete"){
			deleteAllRange(defpIds);
		}else if(object=="scoreRange2Delete"){
			deleteAllRange2(defpIds);
		}else if(object=="scoreHashDelete"){
			deleteAllHash(defpIds);
		}else if(object=="scoreRoleNumDelete"){
			deleteAllRoleNum(defpIds);
		}else if(object=="indexParamDelete"){
			deleteAllindexParam(defpIds);
		}else if(object=="flfhyDelete"){
			deleteAllFlfhy(defpIds);
		}else if(object=="a"){
			deleteAllDefps(defpIds);
		}else if(object=="indexCalculateDelete"){
			deleteAllIndexCalculate(defpIds);
			
		}
	}
}

function changeCompanyStatus(flag){
	var rowid =  gridObj.getGridParam('selarrrow');
	var defpIds = new Array();
	rowid.forEach(function(item) {
		var defpId = gridObj.jqGrid('getCell', item, 'id');
		defpIds[defpIds.length] = defpId;
	});
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行", "info");
	}
	if(rowid.length !=0 ){
		if(flag=="sleep"){
			sleepAllCompany(defpIds);
		}else if(flag=="active"){
			activeAllCompany(defpIds);
		}
		
	}
}

function sleepAllCompany(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否不考核选中企业",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/admin/sleepAllCompany',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			sleepAllCompanyCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function sleepAllCompanyCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	
	window.location.href="#bpm/admin/sleepcompany"
	cb(status);
}

function activeAllCompany(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否考核选中企业",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/admin/activeAllCompany',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			activeAllCompanyCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function activeAllCompanyCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	
	window.location.href="#bpm/admin/sleepcompany"
	cb(status);
}

function deleteAllFlfhy(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/creditTaxPlat/deleteAllFlfhy',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteIndexFlfhyCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteIndexFlfhyCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	
	loadScoreFlfhyGrid("flfhy")
	cb(status);
}

function deleteAllIndexCalculate(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/taxIndexCalculate/deleteIndexCalculate',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteIndexCalculateCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}


function deleteIndexCalculateCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	
	document.location.href = "#bpm/taxIndexCalculate/list";
	cb(status);
}







function deleteAllindexParam(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/taxIndexParam/deleteAllindexParam',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteIndexParamCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteIndexParamCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	
	document.location.href = "#bpm/taxIndexParam/list";
	cb(status);
}

function deleteAllRoleNum(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/creditTaxPlat/deleteAllRoleNum',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteRoleNumCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteRoleNumCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	loadScoreRoleNumGrid("rolenum")
	cb(status);
}

function deleteAllHash(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/creditTaxPlat/deleteAllHash',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteHashCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteHashCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	loadScoreHashGrid("hash")
	cb(status);
}

function deleteAllRange(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/creditTaxPlat/deleteAllRange',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteRangeCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteRangeCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	loadScoreRangeGrid("range")
	cb(status);
}

function deleteAllRange2(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/creditTaxPlat/deleteAllRange2',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteRange2CB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteRange2CB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	loadScoreRange2Grid("range2")
	cb(status);
}


function deleteRolePara(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var RoleParaIds = new Array();
	rowid.forEach(function(item) {
		var RoleParaId = gridObj.jqGrid('getCell', item, 'id');
		RoleParaIds[RoleParaIds.length] = RoleParaId;
	});
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		deleteAllRolePara(RoleParaIds);
	}
}


function deleteAllRolePara(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/creditTaxPlat/deleteScoreRolePara',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'roleParaIds='+JSON.stringify(defpIds)+'&gridId='+document.getElementById("hierarchyParentId").value,
	    	        		success : function(response) {
	    	        			deleteRoleParaCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}



function deleteNoticeData(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var RoleParaIds = new Array();
	rowid.forEach(function(item) {
		var RoleParaId = gridObj.jqGrid('getCell', item, 'id');
		RoleParaIds[RoleParaIds.length] = RoleParaId;
	});
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		deleteAllNoticeData(RoleParaIds);
	}
}

function deleteAllNoticeData(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/creditTaxPlat/deleteNotice',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'noticeId='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteNoticeCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}



function deleteNoticeCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	
	document.location.href = "#bpm/admin/showNoticePLatList";
	cb(status);
}

function deleteAllFinancer(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/admin/deleteAllFinancer',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteFinancerCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteFinancerCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	window.location.href="#bpm/admin/Finance"
	cb(status);
}


function deleteAllDefps(defpIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/credittax/deleteAllDefplxx',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'defpIds='+JSON.stringify(defpIds),
	    	        		success : function(response) {
	    	        			deleteDefpCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteDefpCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	showListViews('DEFP_LIST_VIEW','大额发票')
	
	cb(status);
}




function deleteFtxxData(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var ftxxIds = new Array();
	rowid.forEach(function(item) {
		var ftxxId = gridObj.jqGrid('getCell', item, 'id');
		ftxxIds[ftxxIds.length] = ftxxId;
	});
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		deleteAllFtxxs(ftxxIds);
	}
}

function deleteAllFtxxs(ftxxIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/credittax/deleteAllFtxx',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'ftxxIds='+JSON.stringify(ftxxIds),
	    	        		success : function(response) {
	    	        			deleteFtxxCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteFtxxCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	showListViews('FTXX_LIST_VIEW','房土信息')
	
	cb(status);
}



function deleteQyzggsxxData(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var qyzggsxxIds = new Array();
	rowid.forEach(function(item) {
		var qyzggsxxId = gridObj.jqGrid('getCell', item, 'id');
		qyzggsxxIds[qyzggsxxIds.length] = qyzggsxxId;
	});
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		deleteAllQyzggsxxs(qyzggsxxIds);
	}
}

function deleteAllQyzggsxxs(qyzggsxxIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/credittax/deleteAllQyzggsxx',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'qyzggsxxIds='+JSON.stringify(qyzggsxxIds),
	    	        		success : function(response) {
	    	        			deleteQyzggsxxCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteQyzggsxxCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	showListViews('QYZGGSXX_LIST_VIEW','企业职工个税信息')
	
	cb(status);
}



function deleteZdsssxxxData(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var zdsssxxxIds = new Array();
	rowid.forEach(function(item) {
		var zdsssxxxId = gridObj.jqGrid('getCell', item, 'id');
		zdsssxxxIds[zdsssxxxIds.length] = zdsssxxxId;
	});
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		deleteAllZdsssxxxs(zdsssxxxIds);
	}
}

function deleteAllZdsssxxxs(zdsssxxxIds){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	        	$.ajax({
	    	        		url: 'bpm/credittax/deleteAllZdsssxxx',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'zdsssxxxIds='+JSON.stringify(zdsssxxxIds),
	    	        		success : function(response) {
	    	        			deleteZdsssxxxCB(response,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
	    	    			$.msgBox({
        						title : deleteResult,
        						content : message,
    							type : deleteType
        					});		
	     		    }
	     		}
	});
}

function deleteZdsssxxxCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	showListViews('ZDSSSXXX_LIST_VIEW','重大涉税事项信息')
	
	cb(status);
}


function showAppealCmpPt(){
	$.ajax({
 			url: 'bpm/user/getInfo',
			type: 'GET',
			success : function(result) {
			var param=[{"userId":result}];
			showListViewsWithContainerAndParam('TAX_APPEAL_CMP','','target',param);
			}
	});


}

function showRiskAppealCmpt(){
	$.ajax({
		url: 'bpm/user/getInfo',
		type:'GET',
		success:function(response){
			var param=[{"userid":response}];
			showListViewsWithContainerAndParam('TAX_RISK_APPEAL_CMP','','target',param);
		
		}
	});
}

function showAppealPersonList(){
	$.ajax({
		url: 'bpm/user/getInfo',
		type:'GET',
		success:function(response){
			var param=[{"userId":response}];
			showListViewsWithContainerAndParam('TAX_APPEAL_PERSON','','target',param);
		
		}
	});
}

function getZqCompany(userid){
 	document.location.href = "#bpm/search/getZqCompanyList";
 	var sql="select baseid from etec_tax_cmp_baseinfo where gwrygh='"+userid+"'";
	_execute('taxindexcalculateDialog','sql='+sql);
	$myDialog = $("#taxindexcalculateDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 9), 70],
                       title: "企业 列表"
	             });
	 $myDialog.dialog("open"); 
}

function getAppealDetail(userid,quater,year){
 	document.location.href = "#bpm/search/getAppealList";
 	params='userid='+userid+'&quater='+quater+'&year='+year;
	_execute('appealDialog',params);
	$myDialog = $("#appealDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 9), 70],
                       title: "申诉信息列表"
	             });
	 $myDialog.dialog("open"); 
}

function getGqRiskDetail(userid,quater,year){ 
 	document.location.href = "#bpm/search/getRiskList";
 	var type='gqjd';
 	params='userid='+userid+'&quater='+quater+'&year='+year+'&type='+type;
	_execute('appealDialog',params);
	$myDialog = $("#appealDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 9), 70],
                       title: "风险疑点应对信息列表"
	             });
	 $myDialog.dialog("open"); 
}

function getFxRiskDetail(userid,quater,year){ 
 	document.location.href = "#bpm/search/getRiskList";
 	var type='fxyd'
 	params='userid='+userid+'&quater='+quater+'&year='+year+'&type='+type;
	_execute('appealDialog',params);
	$myDialog = $("#appealDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 9), 70],
                       title: "风险疑点应对信息列表"
	             });
	 $myDialog.dialog("open"); 
}

function showZqCompanyDialog(abc){
	
 	document.location.href = "#bpm/admin/companyReadOnlyForm";
	_execute('popupDialog','baseid='+$(abc).attr("id"));
	$("#popupDialog").dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       resizable:false,
                       position: [($(window).width() / 9), 70],
                       title: "企业 信息",
                       close:function(event,ui){
                       		$("#popupDialog").dialog("destroy");
                       }
                       
	             });
	 $("#popupDialog").dialog("open"); 
}

function _showName(cellValue, OPTIONS, rawObject){
	return '<a onclick="javascript:_showListViewCompanyName(\''+rawObject.companyname+'\');"><font style="color:#428bca;"><b><u>'+cellValue+'</u></b></font></a>';
}

function _showListViewCompanyName(name){
	$("#taxindexCompanyNameDialog").dialog("close");
	$("#cmpName").val(name);	
}
function parentGridWidth(){

	var width = $("#nameDiv").width();
	var winWidth = parseInt(width)-5;
	return winWidth;
	
}


function showFinancerForTj(dom){
	document.location.href = "#bpm/admin/showFinancerForm";

	_execute('popupDialog','sfzhm='+$(dom).attr("id"));
	$("#popupDialog").dialog({
					   width: 'auto',
                       height: 500,
                       top: '10px',
                       modal: true,
                       resizable:false,
                       position: [($(window).width() / 9), 70],
                       title: " 财务人员信息",
                       close:function(event,ui){
                       		$("#appealDialog").dialog("destroy");
                       }
	});
	$("#appealDialog").dialog("open");
	
}

function organizationFinancerGridWidth(){
	var width = $("#parent_container").width();
	var winWidth = parseInt(width) - 25;
	return winWidth;
}


