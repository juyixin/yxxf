<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="reportList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>
<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>
<script type="text/javascript">
 $(document).ready(function(){
       $('#grid_header_links').html($('#header_links').html());
});
</script>

<div id="header_links" align="right" style="display:none">
	<a style="padding:10px;" id="createReport" href="bpm/report/createReportForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon="info"><fmt:message key="button.createNew"/></a>
	<a style="padding:10px;" id="deleteReport" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteGroups()"  data-icon="info"><fmt:message key="button.delete"/></a>
</div>



<div class="span10">
   <%--  <h2><fmt:message key="groupList.heading"/></h2> --%>

    <form method="get" action="/bpm/report" id="searchForm" class="form-search">
   <%--  <div id="search" class="input-append">
        <input type="text" size="20" name="q" id="query" value="${param.q}"
               placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
        <button id="button.search" class="btn" type="submit">
            <i class="icon-search"></i> <fmt:message key="button.search"/>
        </button>
    </div> --%>
    </form>

  <%--  <div id="actions" class="form-actions">
         <a class="btn btn-primary" href="<c:url value='/admin/newGroup'/>">
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/></a>

        <a class="btn" href="<c:url value='/mainMenu'/>">
            <i class="icon-ok"></i> <fmt:message key="button.done"/></a>
    </div>
 --%>
    <display:table name="report" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="1" id="report" pagesize="25" class="table table-condensed table-striped table-hover" export="true">
        <display:column property="reportName"  titleKey="report.name" style="width: 25%"
                        url="" paramId="id" paramProperty="id"/>
        <display:column property="description"  titleKey="report.decsription"
                        style="width: 34%"/>
        <display:column property="roles"  titleKey="report.decsription"
                        style="width: 34%"/>
       <display:column sortProperty="enabled"  titleKey="report.action"
                        style="width: 19%; padding-left: 15px" media="html">
            <a href="/bpm/report/edit/${report.id}">Edit</a>&nbsp;&nbsp;<a href="/bpm/report/delete/${report.id}">Delete</a>
        </display:column> 

        <%-- <display:setProperty name="paging.banner.item_name"><fmt:message key="groupList.group"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="groupList.groups"/></display:setProperty>

        <display:setProperty name="export.excel.filename" value="Group List.xls"/>
        <display:setProperty name="export.csv.filename" value="Group List.csv"/>
        <display:setProperty name="export.pdf.filename" value="Group List.pdf"/> --%>
    </display:table>
</div>