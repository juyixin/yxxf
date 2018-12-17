<%@ page session="true" import="dtw.webmail.model.*"%>

<%-- Import taglibs and i18n bundle --%>
<%@ include file="/common/taglibs.jsp"%>
<%
	JwmaHtmlHelper htmlhelper = (JwmaHtmlHelper) session.getValue("jwma.htmlhelper");
	JwmaInboxInfo inbox = (JwmaInboxInfo) session.getValue("jwma.inboxinfo");
	JwmaMessage message = (JwmaMessage) session.getValue("jwma.composemessage");
	JwmaPreferences prefs = (JwmaPreferences) session.getValue("jwma.preferences");
	String inserthandler = "onChange=\"this.form.to.value=this.value;\"";
	JwmaError error = null;
	Object o = session.getValue("jwma.error");
	if (o != null) {
		error = (JwmaError) o;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
        "http://www.w3.org/TR/REC-html40/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<title></title>
</head>
<body bgcolor="#ffffff" link="#666666" vlink="#666666" alink="#FFFFFF">
	<script type="text/javascript">
		var count = 0;
		var dataList = [];
		var toAddress = [];
		var userJson = JSON.parse('${usersJSON}');

		function submitSend(aform) {
			var toIds = $('#to').val();
			var toFullNames = $('#toFullName').val();
			var cctoIds = $('#ccto').val();
			var cctoFullNames = $('#cctoFullName').val();
			var bcctoIds = $('#bccto').val();
			var bcctoFullNames = $('#bcctoFullName').val();
			var editor_data = CKEDITOR.instances['content'].getData();
			$("#body").val(editor_data);
			toAddressValidation(toFullNames, toIds);
			document.getElementById('to').value = toAddress;
			toAddress.length = 0;
			toAddressValidation(cctoFullNames, cctoIds);
			document.getElementById('ccto').value = toAddress;
			toAddress.length = 0;
			toAddressValidation(bcctoFullNames, bcctoIds);
			document.getElementById('bccto').value = toAddress;
			toAddress.length = 0;
			aform.savedraft.value = "false";
			$("#compose").submit();
		}

		function toAddressValidation(toFullNames, toIds) {
			if (toFullNames.length > 0) {
				var fullNames = toFullNames.split(",");
				for (var i = 0; i < fullNames.length; i++) {
					if (fullNames[i].indexOf(' ') > -1) {
						if (fullNames[i].indexOf('>') > -1) {
							toAddress[toAddress.length] = fullNames[i];
						} else {
							var toId = toIds.split(",");
							for (var j = 0; j < toId.length; j++) {
								if (userJson[toId[j]] == fullNames[i]) {
									toAddress[toAddress.length] = toId[j];
								}
							}
						}
					} else {
						if (fullNames[i].indexOf('@') > -1) {
							toAddress[toAddress.length] = fullNames[i];
						}
					}
				}
			}
		}

		function submitSave(aform) {
			var userJson = '${usersJSON}';
			var toIds = $('#to').val();
			var toFullNames = $('#toFullName').val();
			var cctoIds = $('#ccto').val();
			var cctoFullNames = $('#cctoFullName').val();
			var bcctoIds = $('#bccto').val();
			var bcctoFullNames = $('#bcctoFullName').val();

			var editor_data = CKEDITOR.instances['content'].getData();
			$("#body").val(editor_data);
			toAddressValidation(toFullNames, toIds);
			document.getElementById('to').value = toAddress;
			toAddress.length = 0;
			toAddressValidation(cctoFullNames, cctoIds);
			document.getElementById('ccto').value = toAddress;
			toAddress.length = 0;
			toAddressValidation(bcctoFullNames, bcctoIds);
			document.getElementById('bccto').value = toAddress;
			toAddress.length = 0;
			aform.savedraft.value = "true";
			$("#compose").submit();
		}

		function renderCKEditor() {
			CKEDITOR.replace('content', {
				toolbar : [ {
					name : 'insert',
					items : [ 'Image', 'Table', 'Uploader' ]
				}, {
					name : 'basicstyles',
					items : [ 'Bold', 'Italic', 'Underline', 'Strike' ]
				}, {
					name : 'colors',
					items : [ 'TextColor', 'BGColor' ]
				}, {
					name : 'styles',
					items : [ 'Format', 'FontSize', 'Font' ]
				}

				]
			});
		}

		$(function() {
			var JSONUsers = JSON.parse('${usersJSON}');
			$('#to').val('${to}');
			$('#ccto').val('${cc}');
			$('#bccto').val('${bcc}');
			setUserFullNames('toFullName', '${to}', JSONUsers);
			setUserFullNames('cctoFullName', '${cc}', JSONUsers);
			setUserFullNames('bcctoFullName', '${bcc}', JSONUsers);
			renderCKEditor();
			showOrHideSendTime();
			getEmailContactsAsAutocomplete('toFullName');
			getEmailContactsAsAutocomplete('cctoFullName');
			getEmailContactsAsAutocomplete('bcctoFullName');
			$('#toFullName').attr('readonly', false);
			$('#cctoFullName').attr('readonly', false);
			$('#bcctoFullName').attr('readonly', false);
			
			var date = new Date();
			var offset = date.getTimezoneOffset();
			$('#timeZoneOffset').val(offset);
		});

		$('#sendLater').click(function() {
			showOrHideSendTime();
		});

		function showOrHideSendTime() {
			if ($('#sendLater').attr('checked')) {
				$("#sendTime").show();
				loadDateTimeFieldEazytecFormat('sendTime');
				$('#sendLater').val(true);
			} else {
			    $("#sendTime").hide();
			    $("#sendTime").datetimepicker("destroy");
			    $("#sendTime").val('');
			    $('#sendLater').val(false);
			}
		}

		function cloneDocumentMappingRow(id) {
			var table = document.getElementById(id);
			if (table) {
				var tbody = table.getElementsByTagName('tbody')[0];
				var numRows = tbody.rows.length;
				var lastRow = tbody.rows[numRows - 1];
				var newRow = lastRow.cloneNode(true);
				newRow.setAttribute('id', 'row' + numRows);
				$(newRow).find('input,file').each(
						function() {
							var currentNameAttr = $(this).attr('name'); // get the
							var newNameAttr = currentNameAttr.replace(
									/([^\d]*)(\d*)([^\w]*)/, function(match,
											p1, p2, p3) {
										return p1 + (count) + p3;
									});
							$(this).attr('id', newNameAttr);
							if (newNameAttr.indexOf("id") !== -1) {
								$(this).attr('value', '');
							} else {
							}
						});
				tbody.appendChild(newRow);
				var fileInputField = document.getElementById("files" + count);
				fileInputField.value = "";

			}
			count++;
		}

		function deleteRow(row) {
			if (count <= 0) {
				alert("No Privilege to Remove");
				return false;
			} else {
				var delRow = row.parentElement.parentElement.rowIndex;//row.parentNode.parentNode.parentNode.rowIndex;
				document.getElementById('attachFiles').deleteRow(delRow);
				count--;
			}
		}

		function deleteUserName(event, id) {
			if (event.keyCode == 8 || event.keyCode == 46) {
				var toIds = $('#' + id).val();
				var toFullNames = $('#' + id + 'FullName').val();
				var index = '';
				var fullNameObj = document.getElementById(id + 'FullName');
				var startPos = fullNameObj.selectionStart;
				var beforeCursor = toFullNames.substring(0, startPos);
				beforeCursor = beforeCursor.split(',');
				var afterCursor = toFullNames.substring(startPos,
						toFullNames.length);
				afterCursor = afterCursor.split(',');
				var deletedWord = beforeCursor[beforeCursor.length - 1]
						+ afterCursor[0];
				if (deletedWord.indexOf(' ') > -1) {
					if (deletedWord.indexOf('>') > -1) {
						toFullNames = toFullNames.split(',');
						index = toFullNames.indexOf(deletedWord);
						toFullNames.splice(index, 1);
						$('#' + id + 'FullName').val(toFullNames);
						return false;
					} else {
						var toId = toIds.split(',');
						for (var j = 0; j < toId.length; j++) {
							if (userJson[toId[j]] == deletedWord) {
								var deletedWordId = toId[j];
								index = toId.indexOf(deletedWordId);
								toId.splice(index, 1);
								$('#' + id).val(toId);
								toFullNames = toFullNames.split(',');
								index = toFullNames.indexOf(deletedWord);
								toFullNames.splice(index, 1);
								if (toFullNames.length[toFullNames.length] != ","
										&& $('#' + id).val().length > 0)
									$('#' + id + 'FullName').val(toFullNames);
								else
									$('#' + id + 'FullName').val(toFullNames);
								return false;
							}
						}
					}
				} else {
					if (deletedWord.indexOf('@') > -1) {
						if (afterCursor.length > 1) {
							toFullNames = toFullNames.split(',');
							index = toFullNames.indexOf(deletedWord);
							toFullNames.splice(index, 1);
							$('#' + id + 'FullName').val(toFullNames);
							return false;
						}
					}
				}
			}
			return true;
		}
	</script>

	<%@ include file="/common/messages.jsp" %>
	<div class="page-header">
		<div class="pull-left">
			<h2> <fmt:message key="jwma.composeMail"/></h2>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="span12">
	<div class="row-fluid">
				<div class="widget-body">
					<div class="box_main">
					<form:form id="compose" method="post" enctype="multipart/form-data"
						action="bpm/mail/jwma/sendMail" onSubmit="_execute('target','')"
						class="form-horizontal no-margin">
						<input type="hidden" id="body" name="body" />
						<input type="hidden" id="timeZoneOffset" name="timeZoneOffset"
							value='000' />

						<div class="control-group">
						 	<eazytec:label styleClass="control-label" key="message.to"/>
							<div class="controls">
								<input type="hidden" id="to" name="to" /> 
								<input type="text" id="toFullName" name="toFullName" class="span8" onkeypress="return deleteUserName(event,'to')" /> 
								<img src="/images/jtree_icon.png" onclick="showMailToUserSelection();" />
							</div>
						</div>

						<div class="control-group">
							<eazytec:label styleClass="control-label" key="compose.cc" />
							<div class="controls">
								<input type="hidden" id="ccto" name="ccto" /> 
								<input type="text" id="cctoFullName" name="cctoFullName" class="span8" onkeypress="return deleteUserName(event,'ccto')" /> 
								<img src="/images/jtree_icon.png" onclick="showMailCCUserSelection();" />
							</div>
						</div>

						<div class="control-group">
						 	<eazytec:label styleClass="control-label" key="compose.bcc" />
							<div class="controls">
								<input type="hidden" id="bccto" name="bccto"/>
								<input type="text" id="bcctoFullName" name="bcctoFullName" class="span8" onkeypress="return deleteUserName(event,'bccto')"/>
								<img src="/images/jtree_icon.png" onclick="showMailBCCUserSelection();"  />
				      		</div>
						</div>
					
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="message.subject" />
							<div class="controls">
        						<input type="text" name="subject" class="span8" value="<%= message.getSubject() %>">
							</div>
						</div>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="compose.attachments" />
				    		<div class="controls">
				    		 	<table id="attachFiles" class="subButton">
									<tbody>
								    	<tr id="clonablerow">
								   		 	<td class="uneditable-input1"> <input type="file" name="files" id="files"/></td>
								   			 <td class="uneditable-input1">&nbsp;&nbsp;
								   			 <a href="javascript:void(0);" onclick="cloneDocumentMappingRow('attachFiles')"><fmt:message key="form.add"/></a></td>
											 <td class="uneditable-input1">&nbsp;&nbsp;
											 <a href="javascript:void(0);" onclick="deleteRow(this)"><fmt:message key="task.involved.remove"/></a></td>
								   		</tr>
						   			</tbody>
						    	 </table>
				    		</div>
						</div>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="message.message" />
							<div class="controls">
							    	<div id="ckEditor" class="span8">
										<textarea id="content"><%= message.getBody() %></textarea>
									</div>
							</div>
						</div>
						
						<div class="control-group">
							<div class="form-actions no-margin">
								<button type="button" class="btn btn-primary" name="send" onClick="submitSend(this.form);"><fmt:message key="compose.sendmail"/></button>
			    				<button type="button" class="btn btn-primary" name="save" onClick="submitSave(this.form);"><fmt:message key="compose.savedraft"/></button>
					    		<div class="clearfix"></div>
					    	</div>
					    </div>	
					 	<div id="multiPartDiv" class="row-fluid width-per-100">
					      	<% if(message.isMultipart()) {
					              JwmaMessagePart[] parts=message.getMessageParts();
					              for (int i=0;i<parts.length;i++) {
					                if (prefs.isDisplayingInlined()) {
					      	%>
					      	<%= htmlhelper.displayPartInlined(session,parts[i],prefs,"compose") %>
					      	<%        } else { %>
					      	<%= htmlhelper.getPartDescription(parts[i],"compose") %>
					      	<%
					                }
					              }
					         }
							  %>
						</div>
  						<input type="hidden" name="savedraft">
					</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
