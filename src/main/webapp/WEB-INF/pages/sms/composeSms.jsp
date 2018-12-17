
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
	 <div class="page-header">
		<div class="pull-left">
			<h2><fmt:message key="userProfile.title"/></h2>
		</div>
		<div class="height10"></div>
	</div>

<div class="box_main">
	<div class="row-fluid">
		<div class="span12">
			<div class="widget span12">
				<div class="widget-body form-horizontal no-margin span12">
						<div class="control-group">
							<label class="control-label"><fmt:message key="home.smsRecipient"/></label>
							<div class="controls">
								<form name="form1" method="post" action="">
		      						<label> <input class="mail-text" type="text" name="text" id="text"/> </label>
		   						</form>   
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label"><fmt:message key="home.smsContent"/></label>
							<div class="controls">
								<form name="form1" method="post" action="">
			      					<label>
			        					<textarea class="mail-text" name="text" id="text"></textarea>
			      					</label>
			    				</form>  
							</div>
						</div>
						
						<div class="control-group">
							<div class="form-actions no-margin">
								<button type="button" name="sendSms" id="sendSms" class="btn btn-primary""><i class="icon-ok icon-white"></i>
									<fmt:message key="button.send"/>
								</button>
								<button type="button" name="clearSms" id="clearSms" class="btn btn-primary""><i class="icon-remove icon-white"></i>
									<fmt:message key="button.clear"/>
								</button>
								
							</div>
							<div class="clearfix"></div>
						</div>
						
					</div>
				</div>
			</div>
		</div>
	</div>
