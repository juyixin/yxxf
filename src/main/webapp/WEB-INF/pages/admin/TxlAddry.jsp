<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>

<script type="text/javascript">

   
$("#saveButton").click(function(event) { 
		var status = true;
		var phone = document.getElementById('dh').value;
		if(phone!=null&&phone!=""&&phone!=undefined){	
		if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){ 
			status = false;
	    }
		if(status==false){
			status = true;
		if(!(/^0\d{2,3}-?\d{7,8}$/.test(phone))){
	    	status = false;
	    }
	    	
	    }
	}else{
		status = false;
	}   
		if(status == false){	
		openMessageBox(form.title.error,"电话格式不正确","error", true);
		event.preventDefault();
		}
});

	
	function backToList(){
		document.location.href="#bpm/admin/petitionLetterPersonList";
		_execute('target', '');
	}

</script>
      
 	<div class="row-fluid">
 	<div class="box_main" style="padding-top: 10px">
		<div class="widget">
			<div class="widget-body">
				<form:form  id="txlryFrom" commandName="txlryFrom" method="post" action="bpm/admin/saveTxlryForm" autocomplete="off" modelAttribute="txlryFrom" enctype="multipart/form-data">
				  	<form:hidden path="id"/>
				  	<input type="hidden" id="deleteArray" name="deleteArray"/>
						<div>
							<div class="form-horizontal no-margin" id="general">	
							<input type="hidden" name="sjdm" value="${bm}">
							 <input type="hidden" name="bmdmm" value="${bmdm}">				
								<div class="control-group">
									<label for="title" class="control-label">姓名</label>
									<div class="controls">
										<form:input  path="xm"  id="xm" name="xm" rows="6" required="true" style="width:600px"></form:input>
										<em style="color:red;margin-left:5px">*</em>
									</div>
								</div>
								<div class="control-group">
									<label for="title" class="control-label">部门</label>
									<div class="controls">
										<form:input  path="bm"  id="bm" name="bm" rows="6" required="true" readonly="true" value="${bm}" style="width:600px"></form:input>
									</div>
								</div>
								<div class="control-group">
									<label for="title" class="control-label">电话</label>
									<div class="controls">
										<form:input  path="dh"  id="dh" name="dh" rows="6" required="true" style="width:600px"></form:input>
										<em style="color:red;margin-left:5px">*</em>
									</div>
								</div>
								<div class="control-group">
									<label for="title" class="control-label">办公电话</label>
									<div class="controls">
										<form:input  path="tele"  id="tele" name="tele" rows="6"  style="width:600px"></form:input>
									</div>
								</div>
								
								<div class="control-group">
									<label for="title" class="control-label">排序</label>
									<div class="controls">
										<form:input  path="px"  id="px" name="px" rows="6"  style="width:600px"></form:input>
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