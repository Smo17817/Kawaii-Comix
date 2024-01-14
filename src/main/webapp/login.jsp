<%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 07/01/24
  Time: 12:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <jsp:include page="./header.jsp" flush="true"></jsp:include>
  <body>
    <jsp:include page="./nav.jsp" flush="true"></jsp:include>
    <main>
      <section id="login">
        <div class="login-img">
          <img src="./images/anya.jpg" alt="anya image (fantasy character from anime Spy x Family)">
        </div>
        <div class="form-wrapper">
          <h3>Accedi al tuo Account</h3>
          <form action="LoginServlet" method="post">
            <input type="hidden" name="jspName" value="login">
            <input type="email" name="email" placeholder="E-mail" id="email">
            <input type="password" name="password" placeholder="Password" id = "password">
            <button type="submit" id="loginButton">Invia</button>
          </form>
        </div>
        <div class="links">
          <a href="#">Hai dimenticato la password?</a>
          <p>Non sei iscritto? <br> <a href="./signup.jsp">Iscriviti!!!</a> </p>

        </div>
      </section>
    </main>
    <jsp:include page="./footer.jsp" flush="true"></jsp:include>
  </body>
</html>
