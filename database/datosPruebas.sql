-- =====================================================================
-- BookNest - Datos de prueba iniciales
-- =====================================================================

-- Insertar usuarios de prueba
INSERT INTO users (username, email, password_hash) VALUES 
('camila', 'camila@booknest.com', 'hash_falso'),
('jetzaly', 'jetzaly@booknest.com', 'hash_falso'),
('hans', 'hans@booknest.com', 'hash_falso'),
('juanluis', 'juanluis@booknest.com', 'hash_falso');

-- Insertar libros de prueba
INSERT INTO books (main_title, total_pages) VALUES 
('El nombre del viento', 600),
('Cien años de soledad', 400),
('1984', 328);