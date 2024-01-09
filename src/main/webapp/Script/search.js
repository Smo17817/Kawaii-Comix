function searchAndFilter() {
  let input, filter, schede, product;
  input = document.getElementById("ricerca");
  filter = input.value.toUpperCase();
  schede = document.getElementById("schedeProdotto");
  product = schede.querySelectorAll(".scheda");

  const selectedCategories = Array.from(document.querySelectorAll('input.cat:checked')).map(input => input.value);
  const selectedGenres = Array.from(document.querySelectorAll('input.gen:checked')).map(input => input.value);

  for (const item of product) {
    let a = item.querySelector(".pname");
    let textValue = a.textContent || a.innerText;
    const prodottoCategoria = item.dataset.categoria;
    const prodottoGenere = item.dataset.genere;

    const nameMatches = textValue.toUpperCase().indexOf(filter) > -1;
    const categoryMatches = selectedCategories.length === 0 || selectedCategories.includes(prodottoCategoria);
    const genreMatches = selectedGenres.length === 0 || selectedGenres.includes(prodottoGenere);

    if (filter && (selectedCategories.length > 0 || selectedGenres.length > 0)) {
      // Se è presente una ricerca per nome e filtri attivi, considera solo i prodotti che corrispondono a entrambi
      if (nameMatches && categoryMatches && genreMatches) {
        item.style.display = "";
      } else {
        item.style.display = "none";
      }
    } else if (filter) {
      // Se è presente solo una ricerca per nome, considera solo i prodotti che corrispondono al nome
      if (nameMatches) {
        item.style.display = "";
      } else {
        item.style.display = "none";
      }
    } else if (selectedCategories.length > 0 || selectedGenres.length > 0) {
      // Se ci sono solo filtri attivi, considera solo i prodotti che corrispondono ai filtri
      if (categoryMatches && genreMatches) {
        item.style.display = "";
      } else {
        item.style.display = "none";
      }
    } else {
      // Se non ci sono né ricerca né filtri attivi, mostra tutti i prodotti
      item.style.display = "";
    }

  }
}











