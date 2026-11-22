-- =====================================================================
-- BookNest - Datos de prueba iniciales
-- =====================================================================
BEGIN;

-- Insertar usuarios de prueba
INSERT INTO users (username, email, password_hash) VALUES 
('camila', 'camila@booknest.com', 'hash_falso'),
('jetzaly', 'jetzaly@booknest.com', 'hash_falso'),
('hans', 'hans@booknest.com', 'hash_falso'),
('juanluis', 'juanluis@booknest.com', 'hash_falso');

-- Insertar géneros
INSERT INTO genres (id, name) VALUES 
(1, 'Realismo Mágico'),
(2, 'Distopía'),
(101, 'Fantasía'),
(102, 'Ciencia Ficción'),
(103, 'Aventura')
ON CONFLICT (name) DO NOTHING;

-- Insertar autores (iniciales + autores de las series)
INSERT INTO authors (id, name) VALUES 
(1, 'Patrick Rothfuss'),
(2, 'Gabriel García Márquez'),
(3, 'George Orwell'),
(101, 'J.K. Rowling'),
(102, 'Brandon Sanderson'),
(103, 'Rick Riordan'),
(104, 'S.J. Kincaid'),
(105, 'Marissa Meyer'),
(106, 'Leigh Bardugo');

-- Insertar libros iniciales completos
INSERT INTO books (id, main_title, original_publication_date, synopsis, total_pages) VALUES 
(1, 'El nombre del viento', '2007-03-27', 'Kvothe relata su verdadera historia desde su infancia en una troupe de artistas itinerantes hasta convertirse en un legendario mago y músico.', 600),
(2, 'Cien años de soledad', '1967-05-30', 'La historia de siete generaciones de la familia Buendía en el pueblo ficticio de Macondo.', 400),
(3, '1984', '1949-06-08', 'En una sociedad totalitaria vigilada por el Gran Hermano, Winston Smith lucha por conservar su libertad de pensamiento.', 328),
(4, 'El temor de un hombre sabio', '2011-03-01', 'Kvothe continúa narrando sus aventuras en la Universidad, su viaje a Vintas y su encuentro con los Adem y Felurian.', 1190);

-- Vincular libros iniciales con sus autores
INSERT INTO book_authors (book_id, author_id) VALUES 
(1, 1),
(2, 2),
(3, 3),
(4, 1);

-- Vincular libros iniciales con sus géneros
INSERT INTO book_genres (book_id, genre_id) VALUES 
(1, 101), -- El nombre del viento -> Fantasía
(2, 1),   -- Cien años de soledad -> Realismo Mágico
(3, 2),   -- 1984 -> Distopía
(3, 102), -- 1984 -> Ciencia Ficción
(4, 101); -- El temor de un hombre sabio -> Fantasía

-- =====================================================================
-- DATOS DE PRUEBA: SERIES LITERARIAS Y SAGAS
-- =====================================================================

-- Series literarias
INSERT INTO literary_series (id, name, series_type) VALUES 
(1, 'Crónica del Asesino de Reyes', 'trilogia'),
(101, 'Harry Potter', 'saga'),
(102, 'Hijos de la Bruma (Nacidos de la Bruma)', 'trilogia'),
(103, 'Percy Jackson y los dioses del Olimpo', 'saga'),
(104, 'Insignia', 'trilogia'),
(105, 'Renegades', 'trilogia'),
(106, 'Sombra y hueso (Trilogía Grisha)', 'trilogia');

-- Libros de las series
INSERT INTO books (id, main_title, original_publication_date, synopsis, total_pages) VALUES 
-- Harry Potter
(101, 'Harry Potter y la piedra filosofal', '1997-06-26', 'Harry descubre en su undécimo cumpleaños que es un mago y asiste al Colegio Hogwarts.', 256),
(102, 'Harry Potter y la cámara secreta', '1998-07-02', 'Un misterioso mensaje en la pared advierte que la Cámara de los Secretos ha sido abierta.', 288),
(103, 'Harry Potter y el prisionero de Azkaban', '1999-07-08', 'Sirius Black escapa de la prisión de Azkaban buscando a Harry.', 360),

-- Hijos de la Bruma
(104, 'El Imperio Final', '2006-07-17', 'En un mundo donde cae ceniza del cielo, una joven ladrona descubre sus poderes alománticos.', 688),
(105, 'El Pozo de la Ascensión', '2007-08-21', 'Tras derrotar al Lord Legislador, Luthadel enfrenta el asedio de tres ejércitos.', 800),
(106, 'El Héroe de las Eras', '2008-10-14', 'Vin y Elend buscan las pistas dejadas por el Lord Legislador para salvar el mundo.', 768),

-- Percy Jackson
(107, 'El ladrón del rayo', '2005-06-28', 'Percy Jackson descubre que es hijo de Poseidón y debe recuperar el rayo maestro de Zeus.', 288),
(108, 'El mar de los monstruos', '2006-04-01', 'Percy y sus amigos viajan al Mar de los Monstruos para rescatar a Grover y el Vellocino de Oro.', 256),
(109, 'La maldición del titán', '2007-05-01', 'La diosa Artemisa desaparece y los semidioses deben rescatarla antes del solsticio de invierno.', 288),

-- Insignia
(110, 'Insignia', '2012-07-10', 'Tom Raines es reclutado por las Fuerzas Intrasolares para entrenar como combatiente neuronal.', 448),
(111, 'Vortex', '2013-07-09', 'Tom debe demostrar su lealtad mientras las corporaciones luchan por el control de los reclutas.', 464),
(112, 'Catalyst', '2014-07-08', 'El enfrentamiento final por el futuro de la Tierra y los jóvenes soldados de la Aguja.', 480),

-- Renegades
(113, 'Renegados', '2017-11-07', 'Nova busca venganza infiltrándose entre los Renegados, los superhéroes que gobiernan Gatlon.', 592),
(114, 'Archienemigos', '2018-11-06', 'La doble identidad de Nova corre peligro mientras busca el casco de Ace Anarchy.', 560),
(115, 'Supernova', '2019-11-05', 'La batalla definitiva entre los Renegados y los Anarquistas decide el destino de la ciudad.', 592),

-- Sombra y hueso
(116, 'Sombra y hueso', '2012-06-05', 'Alina Starkov despierta un poder latente capaz de destruir la Sombra que divide Ravka.', 416),
(117, 'Asedio y tormenta', '2013-06-04', 'Alina y Mal huyen a través del Mar Auténtico mientras el Oscuro reúne un nuevo ejército.', 448),
(118, 'Ruina y ascenso', '2014-06-17', 'Sin sus poderes y debilitada, Alina debe encontrar el tercer amplificador de Morozova.', 432);

-- Vincular libros de series con autores
INSERT INTO book_authors (book_id, author_id) VALUES 
(101, 101), (102, 101), (103, 101),
(104, 102), (105, 102), (106, 102),
(107, 103), (108, 103), (109, 103),
(110, 104), (111, 104), (112, 104),
(113, 105), (114, 105), (115, 105),
(116, 106), (117, 106), (118, 106);

-- Vincular libros de series con géneros
INSERT INTO book_genres (book_id, genre_id) VALUES 
(101, 101), (102, 101), (103, 101),
(104, 101), (105, 101), (106, 101),
(107, 101), (107, 103), (108, 101), (109, 101),
(110, 102), (111, 102), (112, 102),
(113, 102), (114, 102), (115, 102),
(116, 101), (117, 101), (118, 101);

-- Vincular libros con sus series y orden de lectura
INSERT INTO book_series (book_id, series_id, volume_number, reading_order) VALUES 
(1, 1, 1, 1), (4, 1, 2, 2),
(101, 101, 1, 1), (102, 101, 2, 2), (103, 101, 3, 3),
(104, 102, 1, 1), (105, 102, 2, 2), (106, 102, 3, 3),
(107, 103, 1, 1), (108, 103, 2, 2), (109, 103, 3, 3),
(110, 104, 1, 1), (111, 104, 2, 2), (112, 104, 3, 3),
(113, 105, 1, 1), (114, 105, 2, 2), (115, 105, 3, 3),
(116, 106, 1, 1), (117, 106, 2, 2), (118, 106, 3, 3);

-- Sincronizar secuencias BIGSERIAL de PostgreSQL
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('genres_id_seq', (SELECT MAX(id) FROM genres));
SELECT setval('authors_id_seq', (SELECT MAX(id) FROM authors));
SELECT setval('literary_series_id_seq', (SELECT MAX(id) FROM literary_series));
SELECT setval('books_id_seq', (SELECT MAX(id) FROM books));

COMMIT;