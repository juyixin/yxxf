<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(window).resize(function() {
    $("#processPreviewPopupDialog").dialog("option", "position", "center");
});

$(document).ready(function(){
    $("#processPreviewPopupDialog").dialog("option", "position", "center");
});

</script>
 <form id="urgeForm" method="post" data-remote="true" accept-charset="UTF-8" onSubmit="_execute('target','');" class="padding5">
<div id="jump-full">
		 <input type="hidden" id="organizers" name="organizers" value="${organizers}">
	     <div class="controls">
	         	<textarea rows="6" cols="50" name="notificationMessage" id="notificationMessage" class="textarea-miniwidth" >${notificationMessage}</textarea>
	     </div>
	     <fieldset class="control-group">
	     <label class="checkbox inline style3 style18">
              <input type="checkbox" id="Mail" name="Mail" checked="checked">发送邮件
         </label>
		<label class="checkbox inline style3 style18">
              <input type="checkbox" id="Sms" name="Sms">发送短信
         </label>
         <label class="checkbox inline style3 style18">
              <input type="checkbox" id="internalMsg" name="internalMsg">发送内部消息
         </label>
        
         </fieldset>
          <br></br>
	<input type="button" onclick="sendUrgeNotification();" value="发送" class="btn btn-primary">
</div>
</form>
