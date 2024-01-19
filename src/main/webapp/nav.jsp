<%@ page import="utenteManagement.User" %>

<% User user = (User) session.getAttribute("user");
	String text = "Accedi";
	if(user != null)
		text = "Area Personale";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <header class="main-head">
    <nav>
      <h1 id="logo">Kawaii Comix</h1>
      <ul>
        <li><a href="./index.jsp"><img src="./icons/home_icon-icons.com_64840.ico" title="Homepage">Home</a></li>
        <li><a href="./catalogo.jsp"><img src="./icons/book.ico" title="Catalogo">Catalogo</a></li>
        <li><a href="./areapersonale.jsp"><img src="./icons/user1.ico" title="<%=text%>"> <%= text %> </a></li>
        <li><a href="./carrello.jsp"><img src="./icons/cart1.ico" title="Carrello">Carrello</a></li>
      </ul>
    </nav>
  </header>
