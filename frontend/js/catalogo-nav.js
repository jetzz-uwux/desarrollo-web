// Navegación directa a las secciones existentes sin modificar app.js.
document.addEventListener("DOMContentLoaded", () => {
  const allowed = new Set(["seccion-libros", "seccion-series", "seccion-resenas", "seccion-ratings", "seccion-estadisticas", "seccion-archivos", "seccion-alt-titles"]);
  function abrirDesdeHash() {
    const id = decodeURIComponent(location.hash.slice(1));
    if (!allowed.has(id)) return;
    const boton = Array.from(document.querySelectorAll(".nav-btn")).find(el => el.dataset.target === id);
    if (boton) boton.click();
  }
  abrirDesdeHash();
  window.addEventListener("hashchange", abrirDesdeHash);
});
