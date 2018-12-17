<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/messages.jsp" %>
<!-- <table width="80%">
<form>
<tr>
	<td><span class="floatLeft fontSize14 pad-L10 pad-T10"><b>Query</b></span></td>
</tr>
<tr>
	<td style="padding-left: 10px;"><textarea placeholder="SELECT table_alias_name.COLUMN_NAME AS column_alias_name FROM TABLE_NAME AS table_alias_name" id="queryArea" rows="5" cols="100" style="width: 98%"></textarea>
	</td>
</tr>
	<tr align="right" >
		<td style="padding-right: 7px;"><button type="submit" class="btn btn-primary checkQueryButton padding0 line-height0" name="save" id="saveButton" onClick="processToExecuteQuery();">
	Execute Query
	</button>
		</td>
	</tr>
	<tr height="10px;"></tr>
</form>
</table>
<div id="print_preview" class="displayNone"></div>
 -->
 <div>
 	<textarea placeholder="SELECT table_alias_name.COLUMN_NAME AS column_alias_name FROM TABLE_NAME AS table_alias_name" id="queryArea" rows="5" class="span12" ></textarea>
 </div>
 <div align="right">
 	<button type="submit" style="padding-right: 7px;" class="btn btn-primary" name="save" id="saveButton" onClick="processToExecuteQuery();">
	Execute Query
	</button>
 </div>