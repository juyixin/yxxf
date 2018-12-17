<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<!-- Error Messages -->
<c:if test="${not empty errors}">
    <script type="text/javascript">
    var error = "";
    <c:forEach var="error" items="${errors}">
    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.error, error, "error");
    </script>
    <c:remove var="errors"/>
</c:if>

<!-- Success Messages -->
<c:if test="${not empty successMessages}">
    <script type="text/javascript">
    var msg = "";
    <c:forEach var="error" items="${successMessages}">
    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.success, msg, "success");
    </script>
     <c:remove var="successMessages" scope="session"/> 
</c:if>
<c:if test="${!newTab}">
	<c:if test="${not empty status.errorMessages}">
	   <script type="text/javascript">
	    var error = "";
	    <c:forEach var="error" items="${status.errorMessages}">
	    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
		</c:forEach>
		validateMessageBox(form.title.error, error , "error");
	    </script>
	</c:if>
</c:if>
<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
<script type="text/javascript">
var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
setIndexPageByRedirect(path);
</script>
