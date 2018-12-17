<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<ul class="hidden-phone">
	<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="topMenu">
		<c:if test="${topMenu.isParent==true && topMenu.menuType=='top' && topMenu.active == true}">
			<li>
				<c:choose>
					<c:when test="${topMenu.urlType == 'newTab'}">
						<a id="headNewTab" name="newTab" title="${topMenu.helpText}" href="${topMenu.menuUrl}" onClick="#" > 
							<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu1"  src="${topMenu.iconPath1}" height="12px" width="12px"/>
							<c:if test="${not empty topMenu.iconPath2}">
					  			<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu2" class="top-menu-img" src="${topMenu.iconPath2}" style="display:none;"/>
					  		</c:if>
					  		<c:if test="${empty topMenu.iconPath2}">
					  			<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu2" class="top-menu-img" src="${topMenu.iconPath1}" style="display:none;"/>
					  		</c:if>
							<span class="pad-L5 text-capitalize">${fn:toLowerCase(topMenu.name)}</span>
						</a>
					</c:when>
					<%-- <c:when test="${topMenu.urlType == 'listview' || topMenu.urlType == 'process' || topMenu.urlType == 'report' || topMenu.urlType == 'url' && topMenu.id != 'ff8081813c8630b9013c863c52110001'}"> --%>
						 <c:when test="${topMenu.urlType == 'listview' || topMenu.urlType == 'process' || topMenu.urlType == 'report'}">
						<a id= "top_menu_${topMenu.id}" title="${topMenu.helpText}" href="javascript:void(0);" onClick="#"  > 
							<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu1" src="${topMenu.iconPath1}" height="12px" width="12px"/>
							<c:if test="${not empty topMenu.iconPath2}">
					  			<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu2" class="top-menu-img" src="${topMenu.iconPath2}" style="display:none;"/>
					  		</c:if>
					  		<c:if test="${empty topMenu.iconPath2}">
					  			<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu2" class="top-menu-img" src="${topMenu.iconPath1}" style="display:none;"/>
					  		</c:if>
							<span class="pad-L5 text-capitalize">${fn:toLowerCase(topMenu.name)}</span> 
						</a>
					</c:when>
			
					<c:otherwise>
						<a id= "top_menu_${topMenu.id}" title="${topMenu.helpText}" href="#bpm${topMenu.menuUrl}" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '', '','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('${fn:replace(fn:toLowerCase(topMenu.name),' ','')}','${topMenu.urlType}','${topMenu.menuUrl}','${topMenu.indexPage}','','','','header','','top_menu_${topMenu.id}','${fn:toLowerCase(topMenu.name)}');" > 
							<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu1" src="${topMenu.iconPath1}" height="12px" width="12px"/>
							<c:if test="${not empty topMenu.iconPath2}">
					  			<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu2" class="top-menu-img" src="${topMenu.iconPath2}" style="display:none;"/>
					  		</c:if>
					  		<c:if test="${empty topMenu.iconPath2}">
					  			<img id="${fn:toLowerCase(topMenu.name)}_menu_onselect_menu2" class="top-menu-img" src="${topMenu.iconPath1}" style="display:none;"/>
					  		</c:if>
							<span class="pad-L5 text-capitalize">${fn:toLowerCase(topMenu.name)}</span> 
						</a>
					</c:otherwise>
				</c:choose>
				<ul class="dropdown-menu">
					<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="parentMenu">
						<c:if test="${parentMenu.isParent==true && parentMenu.menuType=='side' && parentMenu.parentMenu.id == topMenu.id && parentMenu.active == true}">	
							
							<c:set var="isDone" value="0" scope="page"></c:set>
							
							<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menuP">
								<c:if test="${isDone ne '1'}">
									<c:if test="${menuP.parentMenu.id == parentMenu.id && menuP.active == true}">
										<c:set var="isDone" value="1" scope="page"></c:set>
									</c:if>
								</c:if>
							</c:forEach>
							
							<!-- 判断二级菜单是否有下级菜单，如果没有不显示小箭头 -->
							
							<li class="<c:if test="${isDone eq '1'}">dropdown-submenu</c:if>">
							
						
								<c:if test="${parentMenu.urlType == 'url'}">
									<a id="${parentMenu.id}" href="#bpm${parentMenu.menuUrl}" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.id}','','','','side');" title="${parentMenu.helpText}"><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span></a>
								</c:if>
								<c:if test="${parentMenu.urlType == 'script'}">
									<a id="${parentMenu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${parentMenu.urlType}','','${parentMenu.id}','','','','side','${parentMenu.menuUrl}');" title="${parentMenu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span></a>
								</c:if>
								<c:if test="${parentMenu.urlType == 'newTab'}">
									<a id="${parentMenu.id}" href="${parentMenu.menuUrl}" name="newTab" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.id}','','','','side');" title="${parentMenu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span></a>
								</c:if>
								<c:if test="${parentMenu.urlType == 'listview'}">
									<a id="${parentMenu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('${parentMenu.name}','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.id}','${parentMenu.listViewName}','${parentMenu.listViewParam}','${parentMenu.processName}','side');" title="${parentMenu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span></a>
								</c:if>	
								<c:if test="${parentMenu.urlType == 'report'}">
									<a id="${parentMenu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.reportName}');checkMenu('${parentMenu.name}','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.id}','${parentMenu.reportName}','${parentMenu.reportParam}','${parentMenu.processName}','side');" title="${parentMenu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span></a>
								</c:if>	
								<c:if test="${parentMenu.urlType == 'process'}">
									<a id="${parentMenu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${parentMenu.urlType}','${parentMenu.menuUrl}','${parentMenu.id}','${parentMenu.listViewName}','${parentMenu.listViewParam}','${parentMenu.processName}','side');" title="${parentMenu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(parentMenu.name)}</span></a>
								</c:if>		
								
								<ul class="subdown">
									<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menu">
										<c:if test="${menu.isParent!=true && menu.parentMenu.id == parentMenu.id && menu.parentMenu.parentMenu.id == topMenu.id && menu.active == true}">
											<li>
												<c:if test="${menu.urlType == 'url'}">
													<a id="${menu.id}" href="#bpm${menu.menuUrl}" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side','','top_menu_${topMenu.id}','${fn:toLowerCase(topMenu.name)}');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a>
												</c:if>
												<c:if test="${menu.urlType == 'script'}">
													<a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side','','top_menu_${topMenu.id}','${fn:toLowerCase(topMenu.name)}');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a>
												</c:if>
												<c:if test="${menu.urlType == 'newTab'}">
													<a id="${menu.id}" href="${menu.menuUrl}" name="newTab" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side','','top_menu_${topMenu.id}','${fn:toLowerCase(topMenu.name)}');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a>
												</c:if>
												<c:if test="${menu.urlType == 'listview'}">
													<a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('${menu.name}','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side','','top_menu_${topMenu.id}','${fn:toLowerCase(topMenu.name)}');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a>
												</c:if>	
												<c:if test="${menu.urlType == 'report'}">
													<a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('${menu.name}','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.reportName}','${menu.reportParam}','${menu.processName}','side','','top_menu_${topMenu.id}','${fn:toLowerCase(topMenu.name)}');" title="${menu.helpText}" ><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a>
												</c:if>
												<c:if test="${menu.urlType == 'process'}">
													<a id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(topMenu.name)}', '${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}','${topMenu.menuUrl}','${topMenu.urlType}','${topMenu.listViewName}');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side','','top_menu_${topMenu.id}','${fn:toLowerCase(topMenu.name)}');" title="${menu.helpText}"><span class="pad-L5 text-capitalize">${fn:toLowerCase(menu.name)}</span></a>
												</c:if>
											</li>								
										</c:if>
									</c:forEach>
								</ul>
							</li>		
						</c:if>
					</c:forEach>
				</ul>
			</li>
		</c:if>
	</c:forEach> 
</ul>

<%-- <%= this.getServletContext().getAttribute("topMenuScript") %> --%>