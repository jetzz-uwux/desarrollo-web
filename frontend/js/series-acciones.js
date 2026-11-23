document.addEventListener("DOMContentLoaded", () => {
    const panel = document.getElementById("serie-editar-panel");
    const form = document.getElementById("serie-editar-form");
    const mensaje = document.getElementById("serie-lista-mensaje");
    if (!panel || !form) return;
    const campo = id => document.getElementById(id);
    document.querySelectorAll(".js-editar-serie").forEach(b => b.addEventListener("click", () => {
        campo("serie-editar-id").value = b.dataset.id;
        campo("serie-editar-nombre").value = b.dataset.name;
        campo("serie-editar-tipo").value = b.dataset.type;
        panel.hidden = false;
        panel.scrollIntoView({behavior: "smooth"});
    }));
    campo("serie-cancelar").addEventListener("click", () => { panel.hidden = true; form.reset(); });
    form.addEventListener("submit", async event => {
        event.preventDefault();
        const boton = form.querySelector("button[type=submit]"); boton.disabled = true;
        try {
            const r = await fetch(`/api/series/${campo("serie-editar-id").value}`, {
                method: "PUT", headers: {"Content-Type": "application/json"},
                body: JSON.stringify({name: campo("serie-editar-nombre").value.trim(),
                    seriesType: campo("serie-editar-tipo").value})
            });
            if (!r.ok) throw new Error(`No se pudo actualizar la serie (HTTP ${r.status})`);
            window.location.reload();
        } catch (e) { mensaje.textContent = e.message; boton.disabled = false; }
    });
    document.querySelectorAll(".js-eliminar-serie").forEach(b => b.addEventListener("click", async () => {
        if (!window.confirm(`¿Eliminar la serie "${b.dataset.name}"? Solo se puede borrar si no tiene libros asociados.`)) return;
        b.disabled = true;
        try {
            const r = await fetch(`/api/series/${b.dataset.id}`, {method: "DELETE"});
            if (!r.ok) throw new Error(r.status === 409 ? "Primero quita los libros asociados a esta serie."
                : `No se pudo eliminar (HTTP ${r.status})`);
            window.location.reload();
        } catch (e) { mensaje.textContent = e.message; b.disabled = false; }
    }));
});
