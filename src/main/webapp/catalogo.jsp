<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
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
            dynamicCatalog("<%=request.getContextPath()%>/CatalogServlet");
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
                <div id="filtroBottone">Nascondi filtri</div>
                <div id="schedeProdotto"></div>
            </section>

        </section>
    </main>
    <jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>