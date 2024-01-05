function scrollSenzaOffset(event) {
    event.preventDefault(); // Previene il comportamento predefinito del link
    
    let target = document.querySelector(event.target.getAttribute('href'));
    target.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

