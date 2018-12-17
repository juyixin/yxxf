<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%
request.setCharacterEncoding("utf-8");
%>
<ul class="hidden-phone">
	<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="parentMenu">
		<c:if test="${parentMenu.isParent==true && parentMenu.menuType=='header' && parentMenu.active == true && parentMenu.id != 'a5699bf2-51a9-11e3-bfeb-e069959ac2ac' && parentMenu.id != 'ff8081813cb4dd84013cb4f24a5f0000'}">
			<li class="dropdown">
				<c:choose>
					<c:when test="${parentMenu.urlType == 'newTab'}">
						<a id="headNewTab" name="newTab" title="${parentMenu.helpText}" href="${parentMenu.menuUrl}" onClick="setBreadCrumb('${parentMenu.name}', '', '','${parentMenu.menuUrl}');checkMenu('${fn:replace(fn:toLowerCase(parentMenu.name),' ','')}','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.indexPage}','','','','header');" data-toggle="dropdown" class="dropdown-toggle" > <img src="${parentMenu.iconPath1}" height="12px" width="12px"/><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span> <span class="caret icon-white"></span></a>
					</c:when>
					<c:when test="${parentMenu.urlType == 'listview' || parentMenu.urlType == 'process'}">
						<a title="${parentMenu.helpText}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '', '','${parentMenu.menuUrl}');checkMenu('${fn:replace(fn:toLowerCase(parentMenu.name),' ','')}','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.indexPage}','${parentMenu.listViewName}','${parentMenu.listViewParam}','${parentMenu.processName}','header');" data-toggle="dropdown" class="dropdown-toggle" > <img src="${parentMenu.iconPath1}" height="12px" width="12px"/><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span> <span class="caret icon-white"></span></a>
					</c:when>
				<c:otherwise>
					<c:set var="parentMenuUrl" value="${parentMenu.menuUrl}"/>
					<c:choose>
						<c:when test="${fn:startsWith(parentMenuUrl, '/')}">
						<a title="${parentMenu.helpText}" href="javascript:void(0);"
							onClick="callLocationByUrl('#bpm${parentMenu.menuUrl}');setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '', '','${parentMenu.menuUrl}');checkMenu('${fn:replace(fn:toLowerCase(parentMenu.name),' ','')}','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.indexPage}','','','','header');"
							data-toggle="dropdown" class="dropdown-toggle"> <img
							src="${parentMenu.iconPath1}" height="12px" width="12px" /><span
							class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span>
							
							<c:set var="hasChildValues" value="false"/>
							
							<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menu">
								<c:if test="${menu.parentMenu.id == parentMenu.id && menu.active == true}">
									<c:set var="hasChildValues" value="true"/>
								</c:if>
							</c:forEach>
							<c:if test="${hasChildValues == true}">
								<span class="caret icon-white"></span>
							</c:if>
						</a>
						</c:when>
						<c:otherwise>
						<a title="${parentMenu.helpText}" href="${parentMenu.menuUrl}"
							onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '', '','${parentMenu.menuUrl}');checkMenu('${fn:replace(fn:toLowerCase(parentMenu.name),' ','')}','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.indexPage}','','','','header');"
							data-toggle="dropdown" class="dropdown-toggle"> <img
							src="${parentMenu.iconPath1}" height="12px" width="12px" /><span
							class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span>
							
							<c:set var="hasChildValues" value="false"/>
							
							<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menu">
								<c:if test="${menu.parentMenu.id == parentMenu.id && menu.active == true}">
									<c:set var="hasChildValues" value="true"/>
								</c:if>
							</c:forEach>
							<c:if test="${hasChildValues == true}">
								<span class="caret icon-white"></span>
							</c:if>
						</a>
						</c:otherwise>
					</c:choose>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${hasChildValues == true}">
					<ul class="dropdown-menu pull-right">
						<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menu">
							<c:if test="${menu.parentMenu.id == parentMenu.id && menu.active == true}">
								<c:if test="${menu.urlType == 'url'}">
								<c:set var="menuUrl" value="${menu.menuUrl}"/>
									<c:choose>
										<c:when test="${fn:startsWith(menuUrl, '/')}">
												<li><a id="${menu.id}" href="#bpm${menu.menuUrl}" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '','${parentMenu.menuUrl}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" title="${menu.helpText}"><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
										</c:when>
										<c:otherwise>
												<li><a id="${menu.id}" href="${menu.menuUrl}" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '','${parentMenu.menuUrl}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" title="${menu.helpText}"><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
										</c:otherwise>
									</c:choose>
								</c:if>
								<c:if test="${menu.urlType == 'script'}">
									<li><a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '','${parentMenu.menuUrl}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side','');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
								</c:if>
								<c:if test="${menu.urlType == 'newTab'}">
									<li id="sideMenuNewTab"><a id="${menu.id}" href="${menu.menuUrl}" name="newTab" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '','${parentMenu.menuUrl}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
								</c:if>
								<c:if test="${menu.urlType == 'listview'}">
									<li><a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '','${parentMenu.menuUrl}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
								</c:if>	
								<c:if test="${menu.urlType == 'process'}">
									<li><a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '','${parentMenu.menuUrl}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a></li>
								</c:if>
							</c:if>
						</c:forEach>
					</ul>
				</c:if>
			</li>
		</c:if>
	</c:forEach> 
	<!-- <li class="dropdown">
    	<a data-toggle="dropdown" class="dropdown-toggle" href="" data-original-title="">
        	Theming
	        <span class="caret icon-white"></span>
        </a>
	    <ul class="dropdown-menu pull-right">
	        <li>
	          <a href="#" id="default" data-original-title="">Default</a>
	        </li>
	        <li>
	          <a href="#" id="facebook" data-original-title="">Facebook</a>
	        </li>
	        <li>
	          <a href="#" id="foursquare" data-original-title="">Foursquare</a>
	        </li>
	        <li>
	          <a href="#" id="google-plus" data-original-title="">Google+</a>
	        </li>
	        <li>
	          <a href="#" id="instagram" data-original-title="">Instagram</a>
	        </li>
	        <li>
	          <a href="#" id="whitesmoke" data-original-title="">White Smoke</a>
	        </li>
	        <li>
	          <a href="#" id="grey" data-original-title="">Grey</a>
	        </li>
	    </ul>
    </li> -->
	<li>
		<!--<a title="Help" href="#bpm/help/help"><span class="fs1" aria-hidden="true" data-icon="&#xe03b;"></span></a>-->
	</li>
	<li class="dropdown">
		<a title="设置" data-toggle="dropdown" class="dropdown-toggle" href=""> <span class="fs1" aria-hidden="true" data-icon="&#xe090;"></span> </a>
		<ul class="dropdown-menu pull-right">
			<li><a href="#bpm/admin/getChangePassword" onClick="setBreadCrumb('用户选项', '', '','/admin/getChangePassword','root','');_execute('target', '');" id="changePassword">用户选项</a></li>
			<li><a href="#bpm/admin/userform" onClick="setBreadCrumb('用户资料', '', '','/admin/userform','root','');_execute('target', 'from=profile');" id="profile">用户资料</a></li>
			
		</ul>
	</li>
	<li>
		<a title="退出" href="logout" onClick="clearMenuDataFromCookie();window.top.logOut();" ><span class="fs1" aria-hidden="true" data-icon="&#xe0b1;"></span></a>
	</li>
</ul>

<%-- <%= this.getServletContext().getAttribute("headerMenuScript") %> --%>
