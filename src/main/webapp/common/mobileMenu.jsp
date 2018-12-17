<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<ul class="nav">
	<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menu">
		<c:if test="${menu.isParent==false && menu.active == true}">
			<c:if test="${menu.urlType == 'url'}">
				<li><a id="${menu.id}" href="#bpm${menu.menuUrl}" onClick="setBreadCrumb('${fn:toLowerCase(menu.name)}', '', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" title="${menu.helpText}"><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
			</c:if>
			<c:if test="${menu.urlType == 'script'}">
				<li><a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(menu.name)}', '', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side','');" title="${menu.helpText}" ><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
			</c:if>
			<c:if test="${menu.urlType == 'newTab'}">
				<li id="sideMenuNewTab"><a id="${menu.id}" href="${menu.menuUrl}" name="newTab" onClick="setBreadCrumb('${fn:toLowerCase(menu.name)}', '', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" title="${menu.helpText}" ><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
			</c:if>
			<c:if test="${menu.urlType == 'listview'}">
				<li><a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(menu.name)}', '', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" title="${menu.helpText}" ><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
			</c:if>	
			<c:if test="${menu.urlType == 'process'}">
				<li><a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(menu.name)}', '', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" title="${menu.helpText}" ><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
			</c:if>
		</c:if>
	</c:forEach>
</ul>

