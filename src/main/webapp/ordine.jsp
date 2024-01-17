<%@ page import="acquistoManagement.OrdineSingolo" %>
<%@ page import="acquistoManagement.Ordine" %>
<%@ page import="utenteManagement.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 12/01/24
  Time: 12:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  User user = (User) session.getAttribute("user");
  if(user == null)
      response.sendRedirect("login.jsp");

%>
<jsp:include page="./header.jsp" flush="true"></jsp:include>

<body>
<script src="./Script/dynamicCode.js"></script>
	<script>
	document.addEventListener("DOMContentLoaded", dynamicShowOrders("<%=request.getContextPath()%>/OrdiniEffettuatiServlet"));
	</script>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
  <h2>I miei ordini</h2>
  <section id="container">
  </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
