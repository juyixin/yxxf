<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div>
	<spring:bind path="opinion.*">
		<%@ include file="/common/messages.jsp"%>
	</spring:bind>


	<div>
		<form:form id="opinion" commandName="opinion" method="post"
			autocomplete="off" modelAttribute="opinion">
			<form:hidden path="id"/>
			<label class="style3 style18 ">意见</label>
			<table id="opinionTable" class="table table-condensed no-margin">
				<c:if test="${isFormReadOnly != true}">
					<textarea id="textarea_autocomplete_field" autocomplete="off" placeholder='按"空格键"可选择自己的常用意见'
						cols="25" rows="5" style=" width: 500px;"></textarea>
					<tr>
						<td style="padding: 0;" width="436" class="border-none">
							<button type="button"
								class="btn btn-primary normalButton padding0 line-height0"
								name="save" onclick="saveOpinion()" id="saveButton">
								<fmt:message key="button.save" />
							</button>
						</td>
					</tr>
					<tr>
						<td class="border-none"><br></td>
					</tr>
				</c:if>
				<c:forEach items="${opinionList}" var="opinionMsg">
					<tr id='${opinionMsg.id}'>
						<td width="80%" class="border-none"><label class="style3 style18 ">提交人 :
								&nbsp;${opinionMsg.userId} <br> 内容 :
								&nbsp;${opinionMsg.message} <br> 提交时间 :
								&nbsp;${opinionMsg.submittedOn} <br> 任务名称 :
								&nbsp;${opinionMsg.taskName} <br>
						</label> &nbsp;
						<c:if
							test="${opinionMsg.taskId == taskId && opinionMsg.userId == loggedInUser}">
							<td class="border-none"><a class="padding10" onclick="deleteOpinion('${opinionMsg.id}')" href="javascript:void(0);" id="deleteOpinion">删除</a></td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
		</form:form>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var jazz_musicians = [];
		$.ajax({
			type : 'GET',
			async : false,
			url : 'bpm/opinion/getUserOpinionsByUser',
			success : function(response) {
				if($.browser.version  < 12){
					$.each(response, function(index, item) {
						jazz_musicians[jazz_musicians.length] = "<a href='#' onclick='opinionOnClick();'><font color='white'>"+item+"</font></a>";
					});
				} else {
					jazz_musicians = response;
				}
			}
		});

		$("#textarea_autocomplete_field").smartAutoComplete({
			source : jazz_musicians,
			maxResults : 5,
			delay : 200
		});
		
		$("#textarea_autocomplete_field").bind({
			keyIn : function(ev) {
				var tag_list = ev.smartAutocompleteData.query.split(",");
				//pass the modified query to default event
				ev.smartAutocompleteData.query = $.trim(tag_list[tag_list.length - 1]);
			},
			itemSelect : function(ev, selected_item) {
				var options = $(this).smartAutoComplete();
				//get the text from selected item
				var selected_value = $(selected_item).text();
				var cur_list = $(this).val().split(",");
				cur_list[cur_list.length - 1] = selected_value;
				$(this).val(cur_list.join(","));
				//set item selected property.25
				options.setItemSelected(true);
				//hide results container
				$(this).trigger('lostFocus');
				//prevent default event handler from executing
				ev.preventDefault();
			},
		});
	});
	
	function opinionOnClick(){
	}
	
	function deleteOpinion(opinionId) {
		$.ajax({
			type : 'get',
			async : false,
			url : 'bpm/opinion/deleteOpinion?opinionId=' + opinionId,
			success : function(data) {
				$('#' + opinionId).remove();
			}
		});
	}

	function saveOpinion() {
		var message = document.getElementById("textarea_autocomplete_field").value;
		var taskId = $('#taskId').val();
		var processInsId = $('#processInsId').val();
		var taskName = $('#taskName').val();
		//var regex = /^[^*|\":<>[\]{}`\\()';@&$]+$/;
		if (message.trim() == '') {
			validateMessageBox(form.title.message, form.msg.opnionMandatory , false);
		}else {
			var userFullName = $('#userFullName').val();
			$.ajax({
				type : 'post',
				async : false,
				url : '/bpm/opinion/saveOpinion?message=' + message
						+ '&taskId=' + taskId + '&processInsId='
						+ processInsId + '&taskName='
						+ taskName +'&userFullName='+userFullName,
				success : function(data) {
					document
							.getElementById("textarea_autocomplete_field").value = "";
					var table = document.getElementById("opinionTable");
					var row = table.insertRow(1);
					var cell1 = row.insertCell(0);
					var elemenet1 = document.createElement('label');
					elemenet1.setAttribute("class", "style3 style18");
					cell1.setAttribute("width", "80%");
					cell1.setAttribute("class","border-none");
					row.setAttribute("id", data.id);
					elemenet1.innerHTML = "<br>" + "提交人 :"
							+ data.userFullName + "<br>内容 :  "
							+ data.message + "<br>提交时间 :  "
							+ data.submitStr + "<br>任务名称 :  "
							+ data.taskName
							+ "<br>&nbsp;<hr width='100%'>";
					cell1.appendChild(elemenet1);
					var cell2 = row.insertCell(1);
					var elemenet2 = document.createElement('a');
					cell2.setAttribute("class","border-none");
					cell2.setAttribute("width", "20%");
					elemenet2.setAttribute("onclick", "deleteOpinion('"
							+ data.id + "')");
					elemenet2
							.setAttribute("href", "javascript:void(0)");
					elemenet2
					.setAttribute("class", "padding10");
					elemenet2.innerHTML = "删除";

					cell2.appendChild(elemenet2);
				}
			});
		}
	}
</script>
<v:javascript formName="opinion" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>
