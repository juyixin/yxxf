<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="menuList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<%= request.getAttribute("script") %>

<!--

<div class="span10">
    <h2><fmt:message key="menuList.heading"/></h2>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="#bpm/admin/menuForm" onClick="_execute('target','');">
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/></a>

        <a class="btn" href="<c:url value='/mainMenu'/>">
            <i class="icon-ok"></i> <fmt:message key="button.done"/></a>
    </div>

    <display:table name="menuList" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="1" id="menus" pagesize="25" class="table table-condensed table-striped table-hover" export="true">
        <display:column property="name" escapeXml="true" sortable="true" titleKey="menu.menuname" style="width: 25%"
                        url="javascript:_execute('bpm/admin/menuForm?from=list','target');" paramId="id" paramProperty="id"/>
        <display:column property="description" escapeXml="true" sortable="true" titleKey="menu.decsription"
                        style="width: 34%"/>
       

        <display:setProperty name="paging.banner.item_name"><fmt:message key="menuList.menu"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="menuList.menus"/></display:setProperty>

        <display:setProperty name="export.excel.filename" value="Department List.xls"/>
        <display:setProperty name="export.csv.filename" value="Department List.csv"/>
        <display:setProperty name="export.pdf.filename" value="Department List.pdf"/>
    </display:table>
</div>-->