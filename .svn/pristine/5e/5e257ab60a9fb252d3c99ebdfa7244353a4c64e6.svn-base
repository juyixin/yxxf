<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="newgroup.title"/></title>
    <meta name="heading" content="<fmt:message key='newgroup.title'/>"/>
</head>
<spring:bind path="news.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>
<%-- <p>
    <fmt:message key="newgroup.title">
        <fmt:param><c:url value="/newgroup"/></fmt:param>
    </fmt:message>
</p> --%>
<%-- <p>${message}</p> --%>
<!-- <p style="text-align: center">
    <a href="http://www.flickr.com/photos/mcginityphoto/6716288117/" title="Lake McDonald by McGinityPhoto, on Flickr">
      <img src="http://farm8.staticflickr.com/7035/6716288117_5d84ab851d_z.jpg" width="640" height="426" 
      alt="Lake McDonald" style="margin: 20px; border: 1px solid black"></a>
</p> -->
<div class="span12 box ">
<h2>Create News</h2>
 <p>${message}</p> 
 <div class="span7 scroll">
		<div class="table-create-user">
			 <%--  <form:form  commandName="news" method="post" action="" autocomplete="off" modelAttribute="news"> 
			  <form:hidden path="id"/>--%>
			    <table width="100%" border="1">
			    <tr>
			        <td width="271" height="40"><span class="style18 style3"><label><fmt:message key="task.title"/></label></span></td>
			        <td width="436" class="uneditable-input1"><input type="text" name="title" id="Title"/></td>
			    </tr>
			    <tr>
			    	<td width="271" height="40"><span class="style18 style3"><label><fmt:message key="menu.description"/></label></span></td>
			         <td width="436" class="uneditable-input1"><input type="text" name="description" id="description"/></td>
			    </tr>
			      <tr>
			      	<td width="271" height="40"><span class="style3 style18 style21"><label><fmt:message key="home.imageUpload"/></label></span></td>
			        <td width="436" class="uneditable-input1">
			         <input type="file" name="imageUpload" id="imageUpload"/>
		            </td>
       			</tr>
			    <tr>
			    <td></td>
			        <td width="436">
			            <input class="submit_btn" type="submit" value="Submit"/>
			        </td>
			    </tr>
			    <tr height="20px"></tr>
			    <tr>
			   		<td colspan='3'><span class="style18 style3"><font color="#D58000"></font><fmt:message key="home.news.note"/></span></td>
			    </tr>
			</table>  
			<%-- </form:form> --%>
		</div>
	</div> 
</div>
<%-- </page:applyDecorator> --%>