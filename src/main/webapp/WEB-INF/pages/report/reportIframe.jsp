
<%@ include file="/common/taglibs.jsp"%>


<script type="text/javascript"> 

	var newHeight = $("#target").height();
	var personalHeight = parseInt(newHeight) - 90;
	$('#iframeId').css({height: personalHeight,overflow: 'auto'});
</script>


<div>
	<div class="height10"></div>
	<div align="right">
		<strong><a id="backToPreviousPage"
			style="text-decoration: underline; padding: 10px;"
			href="javascript:void(0);" data-role="button" data-theme="b"
			onClick="showListViews('REPORT', 'All Reports');"><fmt:message key="button.back" /></a> </strong>
	</div>
	<body>	
		<div class="height10"></div>
		<div>
			<iframe id="iframeId" src="${url}" width="100%" marginwidth=0
				marginheight=0 hspace=0 vspace=0 frameborder=0 scrolling=auto>

			</iframe>
		</div>
	</body>


</div>


