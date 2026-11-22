const API_BASE_URL = "http://localhost:8081";

async function request(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const config = {
        headers: {
            "Content-Type": "application/json",
            ...options.headers
        },
        ...options
    };

    const response = await fetch(url, config);

    if (!response.ok) {
        let errorMessage = `Error HTTP ${response.status}`;
        try {
            const errorData = await response.json();
            errorMessage = errorData.message || errorData.error || JSON.stringify(errorData);
        } catch {
            const textError = await response.text();
            if (textError) errorMessage = textError;
        }
        throw new Error(errorMessage);
    }

    if (response.status === 204) return null;
    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

const BookNestAPI = {
    // --- CATÁLOGO DE LIBROS ---
    getBooks: () => request("/api/books"),
    
    createBook: (libro) => request("/api/books", {
        method: "POST",
        body: JSON.stringify(libro)
    }),

    linkAuthor: (bookId, authorId) => request(`/api/books/${bookId}/authors`, {
        method: "POST",
        body: JSON.stringify({ authorId })
    }),

    linkGenre: (bookId, genreId) => request(`/api/books/${bookId}/genres`, {
        method: "POST",
        body: JSON.stringify({ genreId })
    }),
    
    getAuthors: () => request("/api/authors"),
    getGenres: () => request("/api/genres"),
    createSeries: (serie) => request("/api/series", {
        method: "POST",
        body: JSON.stringify(serie)
    }),

    // --- RESEÑAS (/api/reviews) ---
    getReviews: (bookId = "", userId = "") => {
        const params = new URLSearchParams();
        if (bookId) params.append("bookId", bookId);
        if (userId) params.append("userId", userId);
        const query = params.toString() ? `?${params.toString()}` : "";
        return request(`/api/reviews${query}`);
    },
    createReview: (reviewData) => request("/api/reviews", {
        method: "POST",
        body: JSON.stringify(reviewData)
    }),
    updateReview: (id, reviewData) => request(`/api/reviews/${id}`, {
        method: "PUT",
        body: JSON.stringify(reviewData)
    }),
    deleteReview: (id, userId = "") => {
        const query = userId ? `?userId=${encodeURIComponent(userId)}` : "";
        return request(`/api/reviews/${id}${query}`, {
            method: "DELETE"
        });
    },

    // --- CALIFICACIONES Y ESTADÍSTICAS (/api/stats) ---
    createRating: (ratingData) => request("/api/stats/rating", {
        method: "POST",
        body: JSON.stringify(ratingData)
    }),
    updateRating: (id, ratingData) => request(`/api/stats/rating/${id}`, {
        method: "PUT",
        body: JSON.stringify(ratingData)
    }),
    deleteRating: (id) => request(`/api/stats/rating/${id}`, {
        method: "DELETE"
    }),
    getGlobalStats: (bookId = "") => {
        const query = bookId ? `?bookId=${encodeURIComponent(bookId)}` : "";
        return request(`/api/stats/global${query}`);
    },
    getMostReadBooks: () => request("/api/stats/most-read"),
    getUserPersonalStats: (userId) => request(`/api/stats/user/${encodeURIComponent(userId)}`),

    // --- ARCHIVOS COLABORATIVOS (/api/files) ---
    getFiles: (bookId = "", language = "", q = "") => {
        const params = new URLSearchParams();
        if (bookId) params.append("bookId", bookId);
        if (language) params.append("language", language);
        if (q) params.append("q", q);
        const query = params.toString() ? `?${params.toString()}` : "";
        return request(`/api/files${query}`);
    },
    uploadFile: async (formData) => {
        // Para multipart/form-data NO se debe poner Content-Type manual; el navegador agrega el boundary
        const response = await fetch(`${API_BASE_URL}/api/files/upload`, {
            method: "POST",
            body: formData
        });
        if (!response.ok) {
            let errorMessage = `Error HTTP ${response.status}`;
            try {
                const errorData = await response.json();
                errorMessage = errorData.message || errorData.error || JSON.stringify(errorData);
            } catch {
                const textError = await response.text();
                if (textError) errorMessage = textError;
            }
            throw new Error(errorMessage);
        }
        return response.json();
    },
    updateFile: (id, fileData) => request(`/api/files/${id}`, {
        method: "PUT",
        body: JSON.stringify(fileData)
    }),
    deleteFile: (id) => request(`/api/files/${id}`, {
        method: "DELETE"
    }),
    getDownloadUrl: (id) => `${API_BASE_URL}/api/files/download/${id}`,

    // --- TÍTULOS ALTERNATIVOS (/api/alternative-titles) ---
    getAlternativeTitles: (bookId = "", language = "", q = "") => {
        const params = new URLSearchParams();
        if (bookId) params.append("bookId", bookId);
        if (language) params.append("language", language);
        if (q) params.append("q", q);
        const query = params.toString() ? `?${params.toString()}` : "";
        return request(`/api/alternative-titles${query}`);
    },
    createAlternativeTitle: (data) => request("/api/alternative-titles", {
        method: "POST",
        body: JSON.stringify(data)
    }),
    updateAlternativeTitle: (id, data) => request(`/api/alternative-titles/${id}`, {
        method: "PUT",
        body: JSON.stringify(data)
    }),
    deleteAlternativeTitle: (id) => request(`/api/alternative-titles/${id}`, {
        method: "DELETE"
    })

};

