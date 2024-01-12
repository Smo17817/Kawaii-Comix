<%@ page import="utenteManagement.User" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 12/01/24
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<%User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
%>
<body>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
    <section id= "address-info">
        <div class="form-wrapper">
            <h2>I tuoi dati</h2>
            <form action="AddressServlet" name="login" method="get">
                <div class="form-row">
                    <label for="indirizzo">Indirizzo:</label>
                    <input type="text"   id= "indirizzo" placeholder="<%=user.getIndirizzo()%>" name="indirizzo">
                </div>
                <div  class="form-row">
                    <label for="citta">Città:</label>
                    <input type="text" id ="citta" placeholder="<%=user.getCitta()%>" name="citta">
                </div>
                <div  class="form-row">
                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia"placeholder="<%=user.getProvincia()%>" name="provincia">
                </div>
                <div  class="form-row">
                    <label for="cap">CAP:</label>
                    <input type="text" id="cap" placeholder="<%=user.getCap()%>" pattern="^[0-9]{5}$"name="cap" placeholder="00000">
                </div>
                <div  class="form-row">
                    <label for="nazione">Nazione:</label>
                    <select name="nazione" id="nazione">
                        <option><%=user.getNazione()%><option>
                    </select>
                </div>
                <div class="sub-class">
                    <button type="submit">Invia</button>
                </div>
            </form>
        </div>
    </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
