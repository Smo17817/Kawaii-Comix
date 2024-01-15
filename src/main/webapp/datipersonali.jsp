<%@ page import="utenteManagement.User" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 12/01/24
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<% User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;}%>
  
<body>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
  <section id="personal-info">
    <div class="form-wrapper">
      <form action="DatiPersonaliServlet" method="POST">
        <h2>Informazioni personali</h2>
        <div class="form-row">
          <label for="nome">Nome:</label>
          <input type="text" id="nome" name="nome"  placeholder="<%=user.getNome()%>"/>
        </div>
        <div class="form-row">
          <label for="cognome">Cognome:</label>
          <input type="text" id="cognome" name="cognome"  placeholder="<%=user.getCognome()%>"/>
        </div>
        <div class="form-row">
          <label for="email">Email:</label>
          <input type="email" id="email"name="email"  placeholder="<%=user.getEmail()%>"/>
        </div>
        <div class="form-row">
          <label for="password">Password:</label>
          <input type="password" id="password" name="password1" />
        </div>
        <div class="form-row">
          <label for="conferma-pass">Conferma Password:</label>
          <input type="password" id="conferma-pass" name="password2" />
        </div>
        <div class= "sub-class">
          <button type="submit">Invia</button>
        </div>
      </form>
    </div>

  </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
