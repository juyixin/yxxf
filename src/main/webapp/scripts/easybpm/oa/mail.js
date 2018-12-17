/**
 * java scripts for mail functionality
 */

function getMailInbox(){
	alert("test");
}

//To get the moving table columns
function getEmailContactsAsAutocomplete(fieldId) {
	var dataArray = [];
	$.ajax({
		type : 'GET',
		async : false,
		url : '/bpm/mail/jwma/getUserEmailContacts',
		success : function(data) {
			$.each(data, function(index, item) {
				dataArray.push(item.fullName+' <'+item.email+'>');
            });
		}
	});
	loadMultiAutocomplete(fieldId,dataArray);
}

function split( val ) {
	return val.split( /,\s*/ );
}
function extractLast( term ) {
    return split( term ).pop();
}

function loadMultiAutocomplete(fieldId,dataArray){
	$( '#'+fieldId )
	// don't navigate away from the field on tab when selecting an item
	.bind( "keydown", function( event ) {
	  if ( event.keyCode === $.ui.keyCode.TAB &&
	      $( this ).data( "ui-autocomplete" ).menu.active ) {
		  event.preventDefault();
      }
	  })
	  .autocomplete({
	      minLength: 0,
	      source: function( request, response ) {
	        // delegate back to autocomplete, but extract the last term
	        response( $.ui.autocomplete.filter(
	        		dataArray, extractLast( request.term ) ) );
	      },
	      focus: function() {
	        // prevent value inserted on focus
	        return false;
	      },
	      select: function( event, ui ) {
	        var terms = split( this.value );
	        // remove the current input
	        terms.pop();
	        // add the selected item
	        terms.push( ui.item.value );
	        // add placeholder to get the comma-and-space at the end
	        terms.push( "" );
	        this.value = terms.join( "," );
	        return false;
	      }
	  });
	  
  /*var escapeRegexp = $.ui.autocomplete.escapeRegex;
	$.extend( $.ui.autocomplete, {
	    escapeRegex: function( pressedLetter ) {
	        return '^' + escapeRegexp(pressedLetter);
	    }
	});*/
	
}

function showMailToUserSelection(){
	var selectedValue=$('#to').val();
	showUserSelection('User', 'multi' , 'to', selectedValue, '');
}

function showMailCCUserSelection(){
	var selectedValue=$('#ccto').val();
	showUserSelection('User', 'multi' , 'ccto', selectedValue, '');
}

function showMailBCCUserSelection(){
	var selectedValue=$('#bccto').val();
	showUserSelection('User', 'multi' , 'bccto', selectedValue, '');
}

function downloadAttachments(attachNumber){
	document.location.href = "bpm/mail/jwma/downloadAttachments?number="+attachNumber;	    
}

function removeFileAttachments(attachNumber){
	$('#'+attachNumber).remove();
	$('<input>').attr({
	    type: 'hidden',
	    id: 'removedAttachment',
	    name: 'removedAttachment',
	    value:attachNumber
	}).appendTo('#compose');
}

//mails Delete
function deleteSelectedMails(from){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length !=0 ){
		var mailNumbers = "";
		rowid.forEach(function(item) {
			var number = gridObj.jqGrid('getCell', item, 'messageNumber');
			if(mailNumbers == ""){
				mailNumbers = number;
			} else {
				mailNumbers = mailNumbers+","+number;
			}
			
		});
		
		var params = 'mailNumbers='+mailNumbers+'&from='+from;
		$.msgBox({
		    title: form.title.confirm,
		    content: "确定要删除该邮件吗 ?",
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	document.location.href = "#bpm/mail/jwma/deleteSelectedMails";
		    		_execute('target',params);
		        }else{
		  		  return false;
		  	  }
		    }
		});
	}
	if(rowid.length ==0){
		$.msgBox({
   		    title:form.title.message,
   		    content:"请选择要删除的邮件",
   		});
	}
}

//mail Delete
function deleteMail(from,mailNumbers){
	var params = 'mailNumbers='+mailNumbers+'&from='+from;
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除该邮件吗 ?",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/mail/jwma/deleteSelectedMails";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

//Contacts Delete
function deleteMailContacts(){
	var rowid =  gridObj.getGridParam('selarrrow');
	
	var contactId=null;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'Id');
		if(contactId == null){
			contactId = id;
		} else {
			contactId = contactId+","+id;
		}
		
	});
	
	if(rowid.length !=0 ){
		deleteAllContactsConfirm(contactId);
	}
	if(rowid.length ==0){
		$.msgBox({
   		    title:form.title.message,
   		    content:"Please select atleast single contact to Delete",
   		});
	}
}

function deleteAllContactsConfirm(contactIds){
	var message = '';
	params = 'contactIds='+contactIds;
	var showSuccess = false;
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除选中的联系人吗?",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	
	        	$.ajax({
	    			type : 'POST',
	    			async : false,
	    			url : 'bpm/mail/jwma/deleteSelectedContacts?contactIds='+contactIds,
	    			success : function(response) {
	    				if(response.successMsg){
	    					message = response.successMsg;
	    					isSuccess = true;
	    					showListViews('CONTACTS','Contacts');
	    					showSuccess = true;
	    					
	    				}else{
	    					message = response.errorMsg;
	    					validateMessageBox(form.title.error, message, "error");
	    				}
	    			},
	        		failure : function(response) {
	        			message = response.errorMsg;
	        			validateMessageBox(form.title.error, message, "error");
	        		}
	    		});
	        	
	        }else{
	  		  return false;
	  	  }
	    },afterClose : function(){
	    	if(showSuccess){
	    		validateMessageBox(form.title.success, message , "success");
	    	}
	    }
	});
}


function replaceMailBreadcrumb(rootNodeName, menuName){
	var breadcrumb = '<div class="span12" style="min-height: 10px;"><ul class="breadcrumb"><li class="text-capitalize">'+rootNodeName+" >" +'<span class="divider"></span></li><li class="text-capitalize">'+menuName+'</li></ul></div>';
	$('#breadCrumb').html(breadcrumb);
}