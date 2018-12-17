<%@ include file="/common/taglibs.jsp"%>
<c:if test="${isTitleShow == 'false'}">
	<div align="right">
		<input type="button" value="Export" class="btn btn-primary"
			onclick="xmlDownloadLink('${resourceName}','${deploymentId}')" />
	</div>
</c:if>
<%-- <c:if test="${isTitleShow == 'true'}">
	<div class="box ">
		<h2>
			<table class="width1300">
				<tr>
					<td>${processName} XML View</td>
					<td align="right"><a id="headerLink" style="text-decoration: underline;"
						href="javascript:void(0);"
						onclick="showProcessList();"><h6 id="headerText">Back</h6>
					</a>
					</td>
				</tr>
			</table>
		</h2>

	</div>
</c:if>
<div id="XMLHolder" class="style27 "></div> --%>

<div class="span12 target-background">
	<c:if test="${isTitleShow == 'true'}">
		<div class="row-fluid">
			<div class="span12">
				<div>
					<strong><span class="floatLeft fontSize14 pad-L10 pad-T10">${processName} XML View</span></strong>
					<strong><a class="floatRight pad-R10 pad-T6" style="text-decoration: underline;" id="headerLink" href="javascript:void(0);" onclick="showProcessList();">Back</a></strong>
				</div>
				
			</div>
		</div>
	</c:if>
	<div class="row-fluid">
    	<div class="span12" style='background-image: url("/styles/easybpm/images/palette_line.jpg");'>
              <div id="XMLHolder" class="style27 "></div>
		</div>
	</div>
</div>

<script>
var xmlString = '<%=request.getAttribute("xmlString")%>	';
	LoadXMLString('XMLHolder', xmlString);
</script>
