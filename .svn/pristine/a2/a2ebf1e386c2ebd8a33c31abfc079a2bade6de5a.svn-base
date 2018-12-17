<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="parentMenu">
	<c:if test="${parentMenu.id== 'a5699bf2-51a9-11e3-bfeb-e069959ac2ac' || parentMenu.id== 'ff8081813cb4dd84013cb4f24a5f0000' || parentMenu.id == '33326fce-00f4-11e2-acc6-e069959eee96' || parentMenu.id == 'ff8081813fc2cf52013fc2ead9fffffa' || parentMenu.id == 'ff8081813d438548013d43994e560000' }">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a id='parent-${parentMenu.id}' href='#${parentMenu.id}' data-parent="#accordion1" data-toggle="collapse" class="accordion-toggle text-capitalize">${fn:toLowerCase(parentMenu.name)} </a>
			</div>
			<div class="accordion-body collapse" id="${parentMenu.id}" style="height: 0px;">
				<div class="accordion-inner">
					<ul class="staff">
						<c:forEach items="<%= this.getServletContext().getAttribute(assMenus) %>" var="menu">
							<c:if test="${menu.parentMenu.id == parentMenu.id && menu.active == true}">
								<c:if test="${menu.urlType == 'url'}">
									<li><a class="designation text-capitalize " id="${menu.id}" href="#bpm${menu.menuUrl}" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" title="${menu.helpText}">${fn:toLowerCase(menu.name)}</a></li>
								</c:if>
								<c:if test="${menu.urlType == 'script'}">
									<li><a class="designation text-capitalize" id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side','');" title="${menu.helpText}" >${fn:toLowerCase(menu.name)}</a></li>
								</c:if>
								<c:if test="${menu.urlType == 'newTab'}">
									<li class="designation text-capitalize" id="sideMenuNewTab"><a id="${menu.id}" href="${menu.menuUrl}" name="newTab" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','','','','side');" title="${menu.helpText}" >${fn:toLowerCase(menu.name)}</a></li>
								</c:if>
								<c:if test="${menu.urlType == 'listview'}">
									<li><a class="designation text-capitalize" id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" title="${menu.helpText}" >${fn:toLowerCase(menu.name)}</a></li>
								</c:if>	
								<c:if test="${menu.urlType == 'process'}">
									<li><a class="designation text-capitalize" id="${menu.id}" href="javascript:void(0);" onClick="setBreadCrumb('${fn:toLowerCase(parentMenu.name)}', '${fn:toLowerCase(menu.name)}', '');checkMenu('','${menu.urlType}','${menu.menuUrl}','${menu.id}','${menu.listViewName}','${menu.listViewParam}','${menu.processName}','side');" title="${menu.helpText}" >${fn:toLowerCase(menu.name)}</a></li>
								</c:if>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</c:if>				
</c:forEach> 
