<%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 04/01/24
  Time: 23:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
  <head>
    <meta http-equiv = “Content-type” content = “text/html; charset=utf-8” />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name=" description" content="Vendita Manga al Dettaglio" />
    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
    <meta name="robots" content="index,follow" />
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <%
      String currentPage = request.getServletPath(); // Ottieni il percorso della tua pagina JSP corrente
      String cssFile = "";
      boolean AdditionalCss = false;

      if (currentPage.equals("/index.jsp")) {
        cssFile = "index.css";
        AdditionalCss = true;
      }
    %>
    <% if(AdditionalCss){%>
      <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/global.css">
    <%}%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/<%=cssFile%>">
    <link rel="icon" href="<%=request.getContextPath()%>/icons/Luffys_flag_2_icon-icons.com_76119.ico"/>
    <link
            href="https://fonts.googleapis.com/css2?family=Pattaya&display=swap"
            rel="stylesheet"
    />
    <title>Kawaii Comix</title>
  </head>
</html>
