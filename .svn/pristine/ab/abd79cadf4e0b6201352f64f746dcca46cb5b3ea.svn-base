<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<!-- side menu start  -->
<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="topMenu">
<c:if test="${topMenu.isParent==true && (topMenu.menuType=='top' || topMenu.menuType=='header') && topMenu.active == true}">
<div id="${fn:replace(fn:toLowerCase(topMenu.name),' ','')}_menu">
	<ul class="section menu">
       <li>
	       	<!--<span class="style3">
	       		<a class="menu">
		       		<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="parentSideMenu">
						<c:if test="${parentSideMenu.isParent==true && parentSideMenu.menuType=='side' && parentSideMenu.parentMenu.id == topMenu.id}">	
		       				<span class="style7">${fn:toUpperCase(topMenu.name)} MENU</span>
		       			 </c:if>
					</c:forEach>
	       		</a>
	       	</span>
       	--></li>
	</ul>   
	<ul id="accordion_${fn:replace(fn:toLowerCase(topMenu.name),' ','')}" class="section menu">
		<c:set var='count' value='0' />
		<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="parentSideMenu">
			<c:if test="${parentSideMenu.isParent==true && parentSideMenu.menuType=='side' && parentSideMenu.parentMenu.id == topMenu.id && parentSideMenu.active == true}">	
				<li><a href='${parentSideMenu.menuUrl}' title='${parentSideMenu.helpText}'> <img src="${parentSideMenu.iconPath1}" height="12px" width="12px"/><span class="pad-L15">${parentSideMenu.name}</span> </a>
					<ul class="submenu">
						<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menu">
							<c:if test="${menu.isParent!=true && menu.parentMenu.id == parentSideMenu.id && menu.parentMenu.parentMenu.id == topMenu.id && menu.active == true}">
								<c:if test="${menu.urlType == 'url'}">
									<li><a id="${menu.id}" href="#bpm${menu.menuUrl}" data-role="button" data-theme="b"  onClick="resetSubSideMenu();checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" data-icon="" title="${menu.helpText}" accordianindex='${count}'><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L35">${menu.name}</span></a></img></li>
								</c:if>
								<c:if test="${menu.urlType == 'script'}">
									<li><a id="${menu.id}" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="${menu.menuUrl};resetSubSideMenu();checkMenu('','${menu.urlType}','','${menu.id}','','','','side','${menu.menuUrl}');" data-icon="" title="${menu.helpText}" accordianindex='${count}'><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L35">${menu.name}</span></a></img></li>
								</c:if>
								<c:if test="${menu.urlType == 'newTab'}">
									<li id="sideMenuNewTab"><a id="${menu.id}" href="${menu.menuUrl}" name="newTab" data-role="button" data-theme="b"  onClick="resetSubSideMenu();checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" data-icon="" title="${menu.helpText}" accordianindex='${count}'><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L35">${menu.name}</span></a></img></li>
								</c:if>
								<c:if test="${menu.urlType == 'listview'}">
									<li><a id="${menu.id}" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="resetSubSideMenu();checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" data-icon="" title="${menu.helpText}" accordianindex='${count}'><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L35">${menu.name}</span></a></img></li>
								</c:if>	
								<c:if test="${menu.urlType == 'report'}">
									<li><a id="${menu.id}" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="resetSubSideMenu();checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.reportName}','${menu.listViewParam}','${menu.processName}','side');" data-icon="" title="${menu.helpText}" accordianindex='${count}'><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L35">${menu.name}</span></a></img></li>
								</c:if>
								<c:if test="${menu.urlType == 'process'}">
									<li><a id="${menu.id}" href="javascript:void(0);" data-role="button" data-theme="b" onClick="resetSubSideMenu();checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" data-icon="" title="${menu.helpText}" accordianindex='${count}'><img src="${menu.iconPath1}" height="12px" width="12px"/><span class="pad-L35">${menu.name}</span></a></img></li>
								</c:if>								
							</c:if>
						</c:forEach>
					</ul>
				  </li>
				  <c:set var='count' value='${count+1}' />
                </c:if>
		</c:forEach>
	</ul>
</div>
</c:if>
</c:forEach>
<!-- side menu end  -->
			                 	
