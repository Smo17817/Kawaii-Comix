function searchAndFilter() {
  let input, filter;
  input = document.getElementById("ricerca");
  filter = input.value.toUpperCase();

  // Filtra il catalogo completo in base al testo di ricerca
  filteredProducts = catalogo.filter(product => {
    const textValue = product.nome.toUpperCase();
    return textValue.includes(filter);
  });

  // Applica i filtri per categoria e genere (se sono selezionati)
  const selectedCategories = Array.from(document.querySelectorAll('input.cat:checked')).map(input => input.value);
  const selectedGenres = Array.from(document.querySelectorAll('input.gen:checked')).map(input => input.value);

  filteredProducts = filteredProducts.filter(product => {
    const categoryMatches = selectedCategories.length === 0 || selectedCategories.includes(product.categoria);
    const genreMatches = selectedGenres.length === 0 || selectedGenres.includes(product.genere);
    return categoryMatches && genreMatches;
  });

  // Calcola il numero totale di pagine basato sui prodotti filtrati
  totalPages = Math.ceil(filteredProducts.length / itemsPerPage);

  // Crea le schede dei prodotti per la prima pagina dei prodotti filtrati utilizzando la funzione createProductCards2
  createProductCards2(1, filteredProducts);

  // Crea i link per la navigazione tra le pagine basati sul numero totale di pagine dei prodotti filtrati
  createPaginationLinks2(totalPages);
}
