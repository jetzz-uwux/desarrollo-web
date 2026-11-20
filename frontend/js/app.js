document.addEventListener("DOMContentLoaded", () => {
    const alertBox = document.getElementById("alert-box");

    // --- NOTIFICACIONES (Errores y Confirmaciones) ---
    function showNotification(message, type = "success") {
        alertBox.textContent = message;
        alertBox.className = `alert ${type}`;
        alertBox.classList.remove("hidden");

        setTimeout(() => {
            alertBox.classList.add("hidden");
        }, 5000);
    }

    // --- NAVEGACIÓN ENTRE SECCIONES ---
    document.querySelectorAll(".nav-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            document.querySelectorAll(".nav-btn").forEach(b => b.classList.remove("active"));
            document.querySelectorAll(".panel").forEach(p => p.classList.add("hidden"));

            btn.classList.add("active");
            const targetId = btn.getAttribute("data-target");
            document.getElementById(targetId).classList.remove("hidden");

            if (targetId === "seccion-ratings") {
                loadGlobalRatings();
            } else if (targetId === "seccion-estadisticas") {
                loadMostRead();
            }
        });
    });

    // --- CARGAR LIBROS EN LOS SELECTORES ---
    const reviewBookSelect = document.getElementById("review-book-select");
    const filterReviewBook = document.getElementById("filter-review-book");
    const ratingBookSelect = document.getElementById("rating-book-select");

    async function loadBooks() {
        try {
            const books = await BookNestAPI.getBooks();
            const defaultOption = '<option value="">-- Selecciona un libro --</option>';
            reviewBookSelect.innerHTML = defaultOption;
            ratingBookSelect.innerHTML = defaultOption;
            filterReviewBook.innerHTML = '<option value="">Todos los libros</option>';

            books.forEach(book => {
                const option = `<option value="${book.id}">${book.id} - ${book.mainTitle}</option>`;
                reviewBookSelect.innerHTML += option;
                ratingBookSelect.innerHTML += option;
                filterReviewBook.innerHTML += option;
            });
        } catch (error) {
            showNotification(`Error al cargar libros: ${error.message}`, "error");
        }
    }

    // ==================== LÓGICA DE RESEÑAS ====================
    const reviewForm = document.getElementById("review-form");
    const reviewsTbody = document.getElementById("reviews-tbody");
    const btnReviewCancel = document.getElementById("btn-review-cancel");
    const reviewFormTitle = document.getElementById("review-form-title");

    async function loadReviews() {
        const bookId = filterReviewBook.value;
        const userId = document.getElementById("filter-review-user").value;
        reviewsTbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando...</td></tr>';

        try {
            const reviews = await BookNestAPI.getReviews(bookId, userId);
            if (!reviews || reviews.length === 0) {
                reviewsTbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay reseñas registradas.</td></tr>';
                return;
            }

            reviewsTbody.innerHTML = reviews.map(r => {
                const bookLabel = r.book ? `${r.book.mainTitle} (#${r.book.id})` : "N/A";
                const userIdVal = r.user ? r.user.id : "";
                const userLabel = r.user ? `${r.user.username || "Usuario"} (#${r.user.id})` : "N/A";

                return `
                    <tr>
                        <td>${r.id}</td>
                        <td>${bookLabel}</td>
                        <td>${userLabel}</td>
                        <td>${r.content}</td>
                        <td>
                            <button class="btn btn-outline btn-sm" onclick='prepareEditReview(${JSON.stringify(r)})'>Editar</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteReviewItem(${r.id}, ${userIdVal || 'null'})">Eliminar</button>
                        </td>
                    </tr>
                `;
            }).join("");
        } catch (error) {
            reviewsTbody.innerHTML = '<tr><td colspan="5" class="text-center">Error al cargar reseñas.</td></tr>';
            showNotification(`Error al obtener reseñas: ${error.message}`, "error");
        }
    }

    reviewForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const reviewId = document.getElementById("review-id").value;

        // Estructura de Review.java: { user: { id }, book: { id }, content }
        const payload = {
            book: { id: Number(reviewBookSelect.value) },
            user: { id: Number(document.getElementById("review-user-id").value) },
            content: document.getElementById("review-content").value.trim()
        };

        try {
            if (reviewId) {
                await BookNestAPI.updateReview(reviewId, payload);
                showNotification(`Reseña #${reviewId} actualizada correctamente.`, "success");
            } else {
                await BookNestAPI.createReview(payload);
                showNotification("Reseña publicada correctamente.", "success");
            }
            resetReviewForm();
            loadReviews();
        } catch (error) {
            showNotification(`Error al guardar reseña: ${error.message}`, "error");
        }
    });

    window.prepareEditReview = (review) => {
        document.getElementById("review-id").value = review.id;
        reviewBookSelect.value = review.book ? review.book.id : "";
        document.getElementById("review-user-id").value = review.user ? review.user.id : "";
        document.getElementById("review-content").value = review.content || "";

        reviewFormTitle.textContent = `Editando Reseña #${review.id}`;
        document.getElementById("btn-review-submit").textContent = "Actualizar Reseña";
        btnReviewCancel.classList.remove("hidden");
    };

    window.deleteReviewItem = async (id, userId) => {
        if (!confirm(`¿Eliminar la reseña #${id}?`)) return;
        try {
            await BookNestAPI.deleteReview(id, userId || "");
            showNotification(`Reseña #${id} eliminada.`, "success");
            loadReviews();
        } catch (error) {
            showNotification(`Error al eliminar reseña: ${error.message}`, "error");
        }
    };

    function resetReviewForm() {
        reviewForm.reset();
        document.getElementById("review-id").value = "";
        reviewFormTitle.textContent = "Nueva Reseña";
        document.getElementById("btn-review-submit").textContent = "Publicar Reseña";
        btnReviewCancel.classList.add("hidden");
    }

    btnReviewCancel.addEventListener("click", resetReviewForm);
    document.getElementById("btn-filter-reviews").addEventListener("click", loadReviews);

    // ==================== LÓGICA DE CALIFICACIONES (RATINGS) ====================
    const ratingForm = document.getElementById("rating-form");
    const ratingsGlobalTbody = document.getElementById("ratings-global-tbody");

    function getRatingPayload() {
        // Estructura de Rating.java: { user: { id }, book: { id }, score }
        return {
            book: { id: Number(ratingBookSelect.value) },
            user: { id: Number(document.getElementById("rating-user-id").value) },
            score: Number(document.getElementById("rating-score").value)
        };
    }

    ratingForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        try {
            const created = await BookNestAPI.createRating(getRatingPayload());
            showNotification(`Calificación registrada con ID #${created.id}.`, "success");
            ratingForm.reset();
            loadGlobalRatings();
        } catch (error) {
            showNotification(`Error al registrar calificación: ${error.message}`, "error");
        }
    });

    document.getElementById("btn-rating-update").addEventListener("click", async () => {
        const ratingId = document.getElementById("rating-id-edit").value;
        if (!ratingId) {
            showNotification("Ingresa el ID de calificación que deseas actualizar.", "error");
            return;
        }
        try {
            await BookNestAPI.updateRating(ratingId, getRatingPayload());
            showNotification(`Calificación #${ratingId} actualizada.`, "success");
            loadGlobalRatings();
        } catch (error) {
            showNotification(`Error al actualizar calificación: ${error.message}`, "error");
        }
    });

    document.getElementById("btn-rating-delete").addEventListener("click", async () => {
        const ratingId = document.getElementById("rating-id-edit").value;
        if (!ratingId) {
            showNotification("Ingresa el ID de calificación que deseas eliminar.", "error");
            return;
        }
        if (!confirm(`¿Eliminar calificación #${ratingId}?`)) return;
        try {
            await BookNestAPI.deleteRating(ratingId);
            showNotification(`Calificación #${ratingId} eliminada.`, "success");
            ratingForm.reset();
            loadGlobalRatings();
        } catch (error) {
            showNotification(`Error al eliminar calificación: ${error.message}`, "error");
        }
    });

    async function loadGlobalRatings() {
        ratingsGlobalTbody.innerHTML = '<tr><td colspan="4" class="text-center">Cargando...</td></tr>';
        try {
            const stats = await BookNestAPI.getGlobalStats();
            if (!stats || stats.length === 0) {
                ratingsGlobalTbody.innerHTML = '<tr><td colspan="4" class="text-center">Sin datos.</td></tr>';
                return;
            }
            // Campos de RatingRepository.GlobalStat: bookId, mainTitle, averageScore, totalRatings
            ratingsGlobalTbody.innerHTML = stats.map(s => `
                <tr>
                    <td>${s.bookId}</td>
                    <td>${s.mainTitle}</td>
                    <td>${s.averageScore != null ? Number(s.averageScore).toFixed(2) + " ★" : "Sin votos"}</td>
                    <td>${s.totalRatings ?? 0}</td>
                </tr>
            `).join("");
        } catch (error) {
            ratingsGlobalTbody.innerHTML = '<tr><td colspan="4" class="text-center">Error al cargar.</td></tr>';
            showNotification(`Error al cargar promedios: ${error.message}`, "error");
        }
    }

    document.getElementById("btn-refresh-ratings").addEventListener("click", loadGlobalRatings);

    // ==================== LÓGICA DE ESTADÍSTICAS ====================
    const mostReadTbody = document.getElementById("most-read-tbody");
    const userStatsTbody = document.getElementById("user-stats-tbody");

    async function loadMostRead() {
        mostReadTbody.innerHTML = '<tr><td colspan="3" class="text-center">Cargando...</td></tr>';
        try {
            const list = await BookNestAPI.getMostReadBooks();
            if (!list || list.length === 0) {
                mostReadTbody.innerHTML = '<tr><td colspan="3" class="text-center">Sin datos.</td></tr>';
                return;
            }
            // Campos de PopularBookStat: bookId, mainTitle, finishedReadersCount
            mostReadTbody.innerHTML = list.map(item => `
                <tr>
                    <td>${item.bookId}</td>
                    <td>${item.mainTitle}</td>
                    <td>${item.finishedReadersCount}</td>
                </tr>
            `).join("");
        } catch (error) {
            mostReadTbody.innerHTML = '<tr><td colspan="3" class="text-center">Error al cargar.</td></tr>';
            showNotification(`Error al cargar más leídos: ${error.message}`, "error");
        }
    }

    document.getElementById("btn-user-stats").addEventListener("click", async () => {
        const userId = document.getElementById("stats-user-id").value;
        if (!userId) {
            showNotification("Ingresa un ID de usuario.", "error");
            return;
        }
        userStatsTbody.innerHTML = '<tr><td colspan="2" class="text-center">Consultando...</td></tr>';
        try {
            const stats = await BookNestAPI.getUserPersonalStats(userId);
            if (!stats || stats.length === 0) {
                userStatsTbody.innerHTML = '<tr><td colspan="2" class="text-center">Este usuario aún no tiene lecturas registradas.</td></tr>';
                return;
            }
            // Campos de UserPersonalStat: status, total
            userStatsTbody.innerHTML = stats.map(s => `
                <tr>
                    <td>${s.status}</td>
                    <td>${s.total}</td>
                </tr>
            `).join("");
        } catch (error) {
            userStatsTbody.innerHTML = '<tr><td colspan="2" class="text-center">Error al consultar.</td></tr>';
            showNotification(`Error en estadísticas personales: ${error.message}`, "error");
        }
    });

    document.getElementById("btn-load-stats").addEventListener("click", loadMostRead);

    // Inicialización al abrir la página
    loadBooks();
    loadReviews();
});