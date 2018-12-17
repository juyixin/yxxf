<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>


      
 	<div class="row-fluid">
 	<div class="box_main" style="padding-top: 10px">
		<div class="widget">
			<div class="widget-body">
				<form:form  id="txlFrom" commandName="txlFrom" method="post" action="bpm/admin/saveTxleditForm" autocomplete="off" modelAttribute="txlFrom" enctype="multipart/form-data">
				  	<form:hidden path="id"/>
				  	<input type="hidden" id="deleteArray" name="deleteArray"/>
						<div>
							<div class="form-horizontal no-margin" id="general">	
							<input type="hidden" name="id" value="${id}">				
								<div class="control-group">
									<label for="title" class="control-label">部门</label>
									<div class="controls">
										<form:input  path="bm"  id="bm" name="bm" rows="6" required="true" style="width:600px"></form:input>
									</div>
								</div>
								<div class="control-group">
										<label for="content" class="control-label">备注</label>
										<div  class="controls">	
											<div id="ckEditor" style="width:700px">
											<form:textarea  class="span8" path="bz" id="bz" name="bz" rows="10"></form:textarea>
											
										</div>
									</div>
								</div>
								
								
								<div class="control-group">
									

								<div class="control-group" align="center">
									<div class="form-actions no-margin pad-L0">	
										<span id="saveButtonDiv">
											<button type="submit" name="save" onclick="bCancel=false;"
												class="btn btn-primary" id="saveButton" class="cursor_pointer">
														保存		
											</button>
										</span> 
										<span id="cancelButtonDiv" class="">
											<button type="button" class="btn btn-primary" name="cancel"
												onclick="backToPreviousPage()" id="cancelButton"
												class="cursor_pointer">
														取消
											</button>
										</span>
									</div>
								</div>
							</div>
						</div>
				</form:form>
			</div>
		</div>
	</div>
</div>