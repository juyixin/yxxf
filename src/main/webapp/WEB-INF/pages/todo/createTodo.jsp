<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create To-do</title>
</head>
<script type="text/javascript">
function toDoCancel() { 
	document.location.href = "#bpm/todo/todoList";
	_execute('target','');
}
</script>
<body>

<%-- <div class="span12 box ">
	<h2>Create To-do
	<a id="backToPreviousPage" href="#bpm/todo/todoList" data-role="button" data-theme="b"  onClick="_execute('target','');" data-icon="">Back</a> 	
	</h2>
	<p>${message}</p>
	<div class="span7 scroll">
		<div class="table-create-user"> 
			<div id="target" class="span10" style="padding-left: 20PX;">
				<form id="menu" commandName="menu" method="post" action="bpm/admin/saveTodo" autocomplete="off" modelAttribute="menu">
			
			
				<input type="hidden" id="id" name="id"/>
				<table width="100%">
					<tr>
						<td width="150" height="40"><span class="style3 style18 style21"><label name="title">Title</label></span></td>
						<td class="uneditable-input1"><input type="text" id="title" name="title" /></td>
					</tr>
					<tr>
						<td width="150" height="40"><span class="style3 style18 style21"><label name="description">Description</label></span></td>
						<td class="uneditable-input1"><input type="text"  id="description" name="description" /></td>
					</tr>
					<tr>
						<td width="150" height="40"><span class="style3 style18 style21"><label name="duedate">Due date</label></span></td>
						<td class="uneditable-input1"><input type="text"  id="description" name="description" /></td>
					</tr>
					<tr>
						<td width="150" height="40"><span class="style3 style18 style21"><label name="assignedto">Assigned to</label></span></td>
						<td class="uneditable-input1">
							<select class="select" id="assignedto"	name="assignedto" style="width:185px;">
								<option selected="selected" value=""></option>
								<option value=""></option>
							</select>
						</td>
					</tr>
					<tr>
						<td width="150" height="40"><span class="style3 style18 style21"><label name="status">status</label></span></td>
						<td class="uneditable-input1"><input type="text"  id="status" name="status" /></td>
					</tr>
					<tr>
						<td></td>
						<td><input class="btn btn-primary normalButton padding0 line-height0" type="submit" value="Create" />
							<button type="button" class="btn btn-primary normalButton padding0 line-height0" name="next" onclick="toDoCancel()" id="cancelButton" style="cursor: pointer;">
			    			Cancel</button>
			    		</td>
					</tr>
				</table>
			</form>
			</div>
		</div>
	</div>
</div>
 --%>
 
 <div class="span12">
	<div class="page-header">
		<div class="pull-left">
			<h2>Create To-do</h2>
			<div class="height10"></div>
		</div>
	</div>
	<div align="right"><a class="padding" id="backToPreviousPage" href="#bpm/todo/todoList" data-role="button" data-theme="b"  onClick="_execute('target','');" data-icon=""><strong>Back</strong></a> </div>
	<div class="row-fluid">
		<div class="widget">
			<div class="widget-body">
				<form id="menu" commandName="menu" method="post" class="form-horizontal no-margin" action="bpm/admin/saveTodo" autocomplete="off" modelAttribute="menu">
					<input type="hidden" id="id" name="id"/>
					
					<div class="control-group" >
						<label class="control-label" name="title">Title</label>
						<div class="controls">
							<input type="text" id="title" name="title" class="span6" />
						</div>
					</div>
					
					<div class="control-group" >
						<label class="control-label" name="description">Description</label>
						<div class="controls">
							<input type="text"  id="description" name="description" class="span6"  />
						</div>
					</div>
					
					<div class="control-group" >
						<label class="control-label" name="duedate">Due Date</label>
						<div class="controls">
							<input type="text"  id="description" name="description" class="span6"  />
						</div>
					</div>
					
					<div class="control-group" >
						<label class="control-label" name="assignedto">Assigned to</label>
						<div class="controls">
							<select class="select" id="assignedto"	name="assignedto" class="span6">
								<option selected="selected" value=""></option>
								<option value=""></option>
							</select>
						</div>
					</div>
					
					<div class="control-group" >
						<label class="control-label" name="status">Status</label>
						<div class="controls">
							<input type="text"  id="status" name="status" class="span6"  />
						</div>
					</div>
					
					<div class="control-group" >
						<div class="form-actions no-margin">
							<button class="btn btn-primary" type="submit" value="Save" >Save</button>
							<button type="button" class="btn btn-primary" name="next" onclick="toDoCancel()" id="cancelButton" class="cursor_pointer">
			    			Cancel</button>
			    			<div class="clearfix"></div>
						</div>
					</div>
					
				</form>
			</div>
		</div>
	</div>
</div>
 </body>