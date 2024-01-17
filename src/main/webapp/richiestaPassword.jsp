<%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 17/01/24
  Time: 01:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
  <section id ="reset-pass">
    <div id="reset-img"><img src="./images/forgot.jpg" alt=""/></div>
    <form  action = "ForgotPasswordServlet" method = "POST" class = "reset-form">
      <h3>Reset password</h3>
      <div class="form-row">
        <label for="email">Email:</label>
        <input type="email" id="email" name = "email" placeholder="E-mail"/>
      </div>
      <div class="form-row">
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" placeholder="Password"/>
      </div>
      <div class="form-row">
        <label for="conf-password">Conferma Password:</label>
        <input type="password" id="conf-password" name="conf-password" placeholder="Password"/>
      </div>
      <div class= "sub-class">
        <button type="submit">Invia</button>
      </div>
    </form>
  </section>
</main>

<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
