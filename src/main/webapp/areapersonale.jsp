<%@ page import="utenteManagement.User" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 10/01/24
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null)
    response.sendRedirect("login.jsp");
%>
<html>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body>
  <main>
    <jsp:include page="./nav.jsp" flush="true"></jsp:include>
    <section class="mid">
      <h2>Il mio Account</h2>
      <div class="banner">
        <video autoplay>
          <source src="./video/banner.mp4" type="video/mp4">
        </video>
      </div>
      <div class="account">
        <ul>
          <li> <a href="#">
            <img class="proimg" alt="" src="./icons/profile.ico">
            Dati personali
          </a> </li>
          <li> <a href="#">
            <img class="proimg" alt="" src="./icons/address.ico">
            Indirizzo
          </a></li>
          <li> <a href="#">
            <img class="proimg" alt="" src="./icons/cart.ico">
            Carrello
          </a> </li>
          <li> <a href="OrdineEffettuatiServlet">
            <img class="proimg" alt="" src="./icons/calendar.ico">
            I miei Ordini
          </a></li>
          <li> <a href="LogOutServlet">
            <img class="proimg" alt="" src="./icons/exit.ico">
            Esci
          </a></li>
        </ul>
        <ul id="admin">

        </ul>
      </div>
    </section>
    <jsp:include page="./footer.jsp" flush="true"></jsp:include>
  </main>
</body>
</html>
