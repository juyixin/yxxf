<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div class="page-header">
	<div class="pull-left">
		<h2>消息详情</h2>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="row-fluid">
	<div class="span12">
		<div class="widget">
       			<div class="form-horizontal no-margin">
       				
       				<div class="control-group">
						<label class="control-label">发送人：</label>
						<div class="controls" style="padding-top: 8px;">
							${sendFullnames}
						</div>
					</div>
       			
       			
					<div class="control-group">
						<label class="control-label">接收人：</label>
						<div class="controls" style="padding-top: 8px;">
							${fullnames}
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">主题 ：<span class="required"></span></label>
						<div class="controls" style="padding-top: 8px;">
							${message.messageName}
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">内容 ：<span class="required"></span></label>
						<div class="controls" style="padding-top: 8px;">
							<eazytec:out escapeXml="false">${message.messageBody}</eazytec:out>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">附件：</label>
						<div class="controls" style="padding-top: 8px;">
							<c:forEach items="${message.messageFiles}" var="file">
								<p><a href="bpm/messages/downloadMF?mid=${file.id}">${file.fileName}</a></p>
							</c:forEach>
						</div>
					</div>
				</div>	
		</div>
	</div>
</div>