<%@ page import="utenteManagement.User" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 13/01/24
  Time: 00:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
  Object user = session.getAttribute("user");
  if(user == null || (user instanceof User))
    response.sendRedirect("loginAdmin.jsp");
%>
<jsp:include page="./header.jsp" flush="true"></jsp:include>

<body>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<section id="new_product">
  <div class="form-wrapper">
    <form enctype ="multipart/form-data" action="AddProdottoServlet" method="POST">
      <h2>Aggiungi un Prodotto</h2>
      <div class="form-row">
        <label for="isbn">ISBN: </label>
        <input type="text" id="isbn" name="isbn" maxlength="17" placeholder="00000000000000000">
      </div>
      <div class="form-row">
        <label for="nome">Nome: </label>
        <input type="text"  id="nome" name="nome">
      </div>
      <div class="form-row">
        <label for="autore">Autore: </label>
        <input type="text"  id="autore" name="autore">
      </div>
      <div class="form-row">
        <label for="descrizione">Descrizione: </label>
        <textarea id ="descrizione" name="descrizione"></textarea>
      </div>
      <div class="file-row">
        <label for="upload-input">File: </label>
        <input type="file" class = "input_container" name="file" id="upload-input">
      </div>
      <div class="form-row">
        <label for="prezzo">Prezzo: </label>
        <input type="number" id="prezzo" step="0.01" min="1" name="prezzo" >
      </div>
      <div class="form-row">
        <label for="quantita">Quantità: </label>
        <input  id="quantita" type="number" min="0" name="quantita">
      </div>
      <div class="form-row">
        <label for="genere">Genere: </label>
        <select  id="genere" name="genere" required>
          <option>-scegliere genere-</option>
          <option>Avventura</option>
          <option>Azione</option>
          <option>Combattimento</option>
          <option>Commedia</option>
          <option>Crimine</option>
          <option>Drammatico</option>
          <option>Fantascienza</option>
          <option>Fantastico</option>
          <option>Fantasy</option>
          <option>Gang Giovanili</option>
          <option>Giallo</option>
          <option>Guerra</option>
          <option>Horror</option>
          <option>Magia</option>
          <option>Mecha</option>
          <option>Mistero</option>
          <option>Musicale</option>
          <option>Poliziesco</option>
          <option>Psicologico</option>
          <option>Scolastico</option>
          <option>Sentimentale</option>
          <option>Sportivo</option>
          <option>Storico</option>
          <option>Supereroi</option>
          <option>Thriller</option>
        </select>
      </div>
      <div class="form-row">
        <label for="categoria">Categoria: </label>
        <select id="categoria" name="categoria">
          <option>-scegliere categoria-</option>
          <option>Art Book</option>
          <option>Character Book</option>
          <option>Josei</option>
          <option>Kodomo</option>
          <option>Manga</option>
          <option>Manga Italiani</option>
          <option>Manhwa</option>
          <option>Novel</option>
          <option>Seinen</option>
          <option>Shojo</option>
          <option>Shonen</option>
          <option>Web Comic</option>
          <option>Manga Magazine</option>
        </select>
      </div>
      <div class= "sub-class">
        <button type="submit">Aggiungi</button>
      </div>
    </form>
  </div>
</section>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>

</body>
</html>
