<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
<body>
    <jsp:include page="./nav.jsp" flush="true"></jsp:include>
    <script src="./Script/dynamicCode.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var filtroBottone = document.getElementById('filtroBottone');
            var filtri = document.getElementById('filtri');

            filtroBottone.addEventListener('click', function() {
                if (filtri.style.display === 'none') {
                    filtri.style.display = 'block';
                    filtroBottone.textContent = 'Nascondi filtri';
                } else {
                    filtri.style.display = 'none';
                    filtroBottone.textContent = 'Mostra filtri';
                }
            });
        });
    </script>

    <script>
        $(document).ready(function() {
            dynamicCatalog("<%=request.getContextPath()%>/CatalogoServlet");
            dynamicCategorie("<%=request.getContextPath()%>/CategoriaServlet");
            dynamicGeneri("<%=request.getContextPath()%>/GenereServlet");
        });
    </script>
    <main>
        <section id="catalogo">
            <div id="filtri">
                <h2>Filtra Per</h2>
                <table>
                    <tbody id="categorie">
                    </tbody>
                </table>
                <table>
                    <tbody id="generi">
                    </tbody>
                </table>
            </div>


            <section id="prodotti">
                <div class="features">
                    <div id="filtroBottone">Nascondi filtri</div>
                    <form class="search" id="search-bar">
                        <input type="search"  autocomplete="off" placeholder="Type something..." name="q" class="search__input" id="ricerca" onkeyup="searchAndFilter()">
                        <div class="search__button" id="search-button">
                            <i class="ri-search-2-line search__icon"></i>
                            <i class="ri-close-line search__close"></i>
                        </div>
                    </form>
                </div>
                <div id="schedeProdotto"></div>
                <div id="pagination-container" class="pagination-container"></div>
            </section>
        </section>
    </main>
    <jsp:include page="./footer.jsp" flush="true"></jsp:include>
    <script src="./Script/search.js"></script>
    <script src="./Script/main.js"></script>
</body>
</html>

