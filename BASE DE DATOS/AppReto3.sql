-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 12-05-2025 a las 10:42:10
-- Versión del servidor: 10.1.44-MariaDB-0+deb9u1
-- Versión de PHP: 7.0.33-0+deb9u7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `2425XabiDeFuentes`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `autores`
--

CREATE TABLE `autores` (
  `id_autor` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `nacionalidad` varchar(255) NOT NULL,
  `fecha_nacimiento` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `autores`
--

INSERT INTO `autores` (`id_autor`, `nombre`, `apellidos`, `nacionalidad`, `fecha_nacimiento`) VALUES
(1, 'GABRIEL', 'GARCÍA MÁRQUEZ', 'COLOMBIANA', '1927-03-06'),
(2, 'GEORGE', 'ORWELL', 'BRITÁNICA', '1903-06-25'),
(3, 'J.R.R.', 'TOLKIEN', 'BRITÁNICA', '1892-01-03'),
(4, 'JANE', 'AUSTEN', 'BRITÁNICA', '1775-12-16'),
(5, 'STEPHEN', 'KING', 'ESTADOUNIDENSE', '1947-09-21'),
(6, 'ISABEL', 'ALLENDE', 'CHILENA', '1942-08-02'),
(7, 'HARUKI', 'MURAKAMI', 'JAPONESA', '1949-01-12'),
(8, 'MARIO', 'VARGAS LLOSA', 'PERUANA', '1936-03-28'),
(9, 'FRANZ', 'KAFKA', 'CHECA', '1883-07-03'),
(10, 'FYODOR', 'DOSTOEVSKY', 'RUSA', '1821-11-11'),
(11, 'VIRGINIA', 'WOOLF', 'BRITÁNICA', '1882-01-25'),
(12, 'J.K.', 'ROWLING', 'BRITÁNICA', '1965-07-31'),
(13, 'MIGUEL', 'DE CERVANTES', 'ESPAÑOLA', '1547-09-29'),
(14, 'LEO', 'TOLSTOY', 'RUSA', '1828-09-09'),
(15, 'ERNEST', 'HEMINGWAY', 'ESTADOUNIDENSE', '1899-07-21'),
(16, 'EMILY', 'DICKINSON', 'ESTADOUNIDENSE', '1830-12-10'),
(17, 'CARLOS', 'RUIZ ZAFÓN', 'ESPAÑOLA', '1964-09-25'),
(18, 'ANTOINE', 'DE SAINT-EXUPÉRY', 'FRANCESA', '1900-06-29'),
(19, 'UMBERTO', 'ECO', 'ITALIANA', '1932-01-05'),
(20, 'MARK', 'TWAIN', 'ESTADOUNIDENSE', '1835-11-30');

--
-- Disparadores `autores`
--
DELIMITER $$
CREATE TRIGGER `before_insert_autores` BEFORE INSERT ON `autores` FOR EACH ROW BEGIN
    SET NEW.nombre = UPPER(NEW.nombre);
    SET NEW.apellidos = UPPER(NEW.apellidos);
    SET NEW.nacionalidad = UPPER(NEW.nacionalidad);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_update_autores` BEFORE UPDATE ON `autores` FOR EACH ROW BEGIN
    SET NEW.nombre = UPPER(NEW.nombre);
    SET NEW.apellidos = UPPER(NEW.apellidos);
    SET NEW.nacionalidad = UPPER(NEW.nacionalidad);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ejemplares`
--

CREATE TABLE `ejemplares` (
  `id_ejemplar` int(11) NOT NULL,
  `estado` varchar(255) NOT NULL,
  `fk_isbn` int(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `ejemplares`
--

INSERT INTO `ejemplares` (`id_ejemplar`, `estado`, `fk_isbn`) VALUES
(1, 'DISPONIBLE', 1001),
(2, 'PRESTADO', 1001),
(3, 'DISPONIBLE', 1001),
(4, 'PRESTADO', 1002),
(5, 'DISPONIBLE', 1002),
(6, 'DISPONIBLE', 1002),
(7, 'PRESTADO', 1003),
(8, 'DISPONIBLE', 1003),
(9, 'DISPONIBLE', 1003),
(10, 'PRESTADO', 1004),
(11, 'DISPONIBLE', 1004),
(12, 'DISPONIBLE', 1004),
(13, 'PRESTADO', 1005),
(14, 'DISPONIBLE', 1005),
(15, 'DISPONIBLE', 1005),
(16, 'PRESTADO', 1006),
(17, 'DISPONIBLE', 1006),
(18, 'DISPONIBLE', 1006),
(19, 'PRESTADO', 1007),
(20, 'DISPONIBLE', 1007),
(21, 'DISPONIBLE', 1007),
(22, 'PRESTADO', 1008),
(23, 'DISPONIBLE', 1008),
(24, 'DISPONIBLE', 1008),
(25, 'PRESTADO', 1009),
(26, 'DISPONIBLE', 1009),
(27, 'DISPONIBLE', 1009),
(28, 'PRESTADO', 1010),
(29, 'DISPONIBLE', 1010),
(30, 'DISPONIBLE', 1010),
(31, 'PRESTADO', 1011),
(32, 'DISPONIBLE', 1011),
(33, 'DISPONIBLE', 1011),
(34, 'PRESTADO', 1012),
(35, 'DISPONIBLE', 1012),
(36, 'DISPONIBLE', 1012),
(37, 'PRESTADO', 1013),
(38, 'DISPONIBLE', 1013),
(39, 'DISPONIBLE', 1013),
(40, 'PRESTADO', 1014),
(41, 'DISPONIBLE', 1014),
(42, 'DISPONIBLE', 1014),
(43, 'PRESTADO', 1015),
(44, 'DISPONIBLE', 1015),
(45, 'DISPONIBLE', 1015),
(46, 'PRESTADO', 1016),
(47, 'DISPONIBLE', 1016),
(48, 'DISPONIBLE', 1016),
(49, 'PRESTADO', 1017),
(50, 'DISPONIBLE', 1017);

--
-- Disparadores `ejemplares`
--
DELIMITER $$
CREATE TRIGGER `before_insert_ejemplares` BEFORE INSERT ON `ejemplares` FOR EACH ROW BEGIN
    SET NEW.estado = UPPER(NEW.estado);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_update_ejemplares` BEFORE UPDATE ON `ejemplares` FOR EACH ROW BEGIN
    SET NEW.estado = UPPER(NEW.estado);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `dni` varchar(10) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `telefono` int(9) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `seguridad_social` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`dni`, `nombre`, `telefono`, `email`, `password`, `seguridad_social`) VALUES
('09876543X', 'SANDRA GÓMEZ', 644444444, 'sandra.gomez@gob.es', 'sandragz', 555666777),
('10987654L', 'CARLOS GONZÁLEZ', 788888888, 'carlos.gonzalez@gob.es', 'carlosG123', 567890567),
('12345678Z', 'SONIA PÉREZ', 777777777, 'sonia.perez@gob.es', 'soniapwd1', 345678345),
('13579246C', 'ELENA GARCÍA', 699999999, 'elena.garcia@gob.es', 'elenag123', 234234234),
('21098765H', 'LUIS FERNÁNDEZ', 611111111, 'luis.fernandez@gob.es', 'luispass1', 123456789),
('23456789D', 'CARLOS RUIZ', 688888888, 'carlos.ruiz@gob.es', 'carlos2024', 789789789),
('32109876V', 'JOSÉ PÉREZ', 700000000, 'jose.perez@gob.es', 'joseperez1', 567567567),
('34567891W', 'VÍCTOR GÓMEZ', 766666666, 'victor.gomez@gob.es', 'victor2024', 234567234),
('43210987Q', 'BEATRIZ ROMERO', 799999999, 'beatriz.romero@gob.es', 'beatrizR25', 890123890),
('45678912A', 'PEDRO SÁNCHEZ', 677777777, 'pedro.sanchez@gob.es', 'pedroS123', 456456456),
('54321098S', 'ANA TORRES', 622222222, 'ana.torres@gob.es', 'ana2025', 987654321),
('56789123G', 'PATRICIA MARTÍN', 755555555, 'patricia.martin@gob.es', 'patriciaM25', 456789456),
('65432109Z', 'LAURA DÍAZ', 711111111, 'laura.diaz@gob.es', 'lauraD25', 890890890),
('67891234M', 'JULIA MARTÍNEZ', 666666666, 'julia.martinez@gob.es', 'julia2024', 123123123),
('76543210J', 'ANTONIO RUIZ', 800000000, 'antonio.ruiz@gob.es', 'antonioR123', 123456789),
('78912345Y', 'FERNANDO LÓPEZ', 744444444, 'fernando.lopez@gob.es', 'fernando123', 987654987),
('87654321N', 'DAVID LÓPEZ', 633333333, 'david.lopez@gob.es', 'davidpass', 111223344),
('89123456F', 'MICHAEL BROWN', 655555555, 'michael.brown@usa.gov', 'mikeusa1', 888999000),
('91234567P', 'MARTA SÁNCHEZ', 733333333, 'marta.sanchez@gob.es', 'martasanz2025', 654321654),
('98765432B', 'RAÚL FERNÁNDEZ', 722222222, 'raul.fernandez@gob.es', 'raulpass', 123987123);

--
-- Disparadores `empleados`
--
DELIMITER $$
CREATE TRIGGER `before_insert_empleados` BEFORE INSERT ON `empleados` FOR EACH ROW BEGIN
    SET NEW.dni = UPPER(NEW.dni);
    SET NEW.nombre = UPPER(NEW.nombre);
    SET NEW.email = LOWER(NEW.email);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_update_empleados` BEFORE UPDATE ON `empleados` FOR EACH ROW BEGIN
    SET NEW.dni = UPPER(NEW.dni);
    SET NEW.nombre = UPPER(NEW.nombre);
    SET NEW.email = LOWER(NEW.email);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros`
--

CREATE TABLE `libros` (
  `isbn` int(13) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `genero` varchar(255) NOT NULL,
  `editorial` varchar(255) NOT NULL,
  `ano` int(11) NOT NULL,
  `fk_id_autor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `libros`
--

INSERT INTO `libros` (`isbn`, `titulo`, `genero`, `editorial`, `ano`, `fk_id_autor`) VALUES
(1001, 'EL VIENTO EN LOS SAUCES', 'FANTASÍA', 'EDITORIAL PLANETA', 2005, 1),
(1002, 'CIEN AÑOS DE SOLEDAD', 'REALISMO MÁGICO', 'EDITORIAL HARPERCOLLINS', 1967, 2),
(1003, '1984', 'DISTOPÍA', 'EDITORIAL PENGUIN', 1949, 3),
(1004, 'MATAR A UN RUISEÑOR', 'DRAMA', 'EDITORIAL RANDOM HOUSE', 1960, 4),
(1005, 'DON QUIJOTE DE LA MANCHA', 'NOVELA CLÁSICA', 'EDITORIAL ESPASA', 1605, 5),
(1006, 'LA SOMBRA DEL VIENTO', 'SUSPENSE', 'EDITORIAL PLANETA', 2001, 6),
(1007, 'EL JUEGO DE ENDER', 'CIENCIA FICCIÓN', 'EDITORIAL ORSON SCOTT CARD', 1985, 7),
(1008, 'HARRY POTTER Y LA PIEDRA FILOSOFAL', 'FANTASÍA', 'EDITORIAL SALAMANDRA', 1997, 8),
(1009, 'EL CÓDIGO DA VINCI', 'THRILLER', 'EDITORIAL DOUBLEDAY', 2003, 9),
(1010, 'CRÓNICA DE UNA MUERTE ANUNCIADA', 'MISTERIO', 'EDITORIAL MONDADORI', 1981, 2),
(1011, 'LA CASA DE LOS ESPÍRITUS', 'REALISMO MÁGICO', 'EDITORIAL SUDAMERICANA', 1982, 10),
(1012, 'EL RETRATO DE DORIAN GRAY', 'FILOSOFÍA', 'EDITORIAL ALIANZA', 1890, 11),
(1013, 'LOS PILARES DE LA TIERRA', 'HISTÓRICA', 'EDITORIAL B', 1989, 12),
(1014, 'LA CATEDRAL DEL MAR', 'HISTÓRICA', 'EDITORIAL GRIJALBO', 2006, 13),
(1015, 'FAHRENHEIT 451', 'CIENCIA FICCIÓN', 'EDITORIAL BERENICE', 1953, 14),
(1016, 'EL ALQUIMISTA', 'AVENTURA', 'EDITORIAL HARPERONE', 1988, 15),
(1017, 'EN EL CAMINO', 'NOVELA DE VIAJES', 'EDITORIAL GROVE PRESS', 1957, 16),
(1018, 'EL NOMBRE DE LA ROSA', 'MISTERIO', 'EDITORIAL BOMPIANI', 1980, 17),
(1019, 'LA ISLA DEL TESORO', 'AVENTURA', 'EDITORIAL JUVENTUD', 1883, 18),
(1020, 'CUMBRES BORRASCOSAS', 'ROMÁNTICA', 'EDITORIAL PENGUIN', 1847, 19);

--
-- Disparadores `libros`
--
DELIMITER $$
CREATE TRIGGER `insert_libros` BEFORE INSERT ON `libros` FOR EACH ROW BEGIN
    SET NEW.isbn = UPPER(NEW.isbn);
    SET NEW.titulo = UPPER(NEW.titulo);
    SET NEW.genero = UPPER(NEW.genero);
    SET NEW.editorial = UPPER(NEW.editorial);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_libros` BEFORE UPDATE ON `libros` FOR EACH ROW BEGIN
    SET NEW.isbn = UPPER(NEW.isbn);
    SET NEW.titulo = UPPER(NEW.titulo);
    SET NEW.genero = UPPER(NEW.genero);
    SET NEW.editorial = UPPER(NEW.editorial);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamos`
--

CREATE TABLE `prestamos` (
  `id_prestamo` int(255) NOT NULL,
  `fecha_prestamo` date NOT NULL,
  `fecha_devolucion` date NOT NULL,
  `fk_dni_usuario` varchar(9) NOT NULL,
  `fk_id_ejemplar` int(11) NOT NULL,
  `fk_dni_empleado` varchar(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `prestamos`
--

INSERT INTO `prestamos` (`id_prestamo`, `fecha_prestamo`, `fecha_devolucion`, `fk_dni_usuario`, `fk_id_ejemplar`, `fk_dni_empleado`) VALUES
(1, '2025-04-05', '2025-04-19', '53865310T', 2, '54321098S'),
(3, '2025-04-10', '2025-04-24', '13172516W', 3, '87654321N'),
(4, '2025-04-12', '2025-04-26', '05933149T', 4, '09876543X'),
(5, '2025-04-15', '2025-04-29', '72084949M', 5, '89123456F'),
(6, '2025-04-17', '2025-05-01', '71454475F', 6, '67891234M'),
(7, '2025-04-20', '2025-05-04', '71532355D', 7, '45678912A'),
(8, '2025-04-22', '2025-05-06', '72080807A', 8, '23456789D'),
(9, '2025-04-25', '2025-05-09', '72099723J', 9, '13579246C'),
(10, '2025-04-28', '2025-05-12', '12342878C', 10, '32109876V'),
(11, '2025-05-01', '2025-05-15', '71464634T', 11, '65432109Z'),
(12, '2025-05-03', '2025-05-17', '44490085N', 12, '98765432B'),
(13, '2025-05-06', '2025-05-20', '05309480E', 13, '91234567P'),
(14, '2025-05-08', '2025-05-22', '71463383Z', 14, '78912345Y'),
(15, '2025-05-10', '2025-05-24', '71897408', 15, '56789123G'),
(16, '2025-05-12', '2025-05-26', '71453503R', 16, '34567891W'),
(17, '2025-05-15', '2025-05-29', '71521120K', 17, '12345678Z'),
(18, '2025-05-18', '2025-06-01', '71526423B', 18, '10987654L'),
(19, '2025-05-20', '2025-06-03', '71466987F', 19, '43210987Q');

--
-- Disparadores `prestamos`
--
DELIMITER $$
CREATE TRIGGER `before_insert_prestamos` BEFORE INSERT ON `prestamos` FOR EACH ROW BEGIN
    SET NEW.fk_dni_usuario = UPPER(NEW.fk_dni_usuario);
    SET NEW.fk_dni_empleado = UPPER(NEW.fk_dni_empleado);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_update_prestamos` BEFORE UPDATE ON `prestamos` FOR EACH ROW BEGIN
    SET NEW.fk_dni_empleado = UPPER(NEW.fk_dni_empleado);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `dni` varchar(9) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `telefono` int(9) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `penalizacion` varchar(2) NOT NULL,
  `fecha_inicio_penalizacion` date NOT NULL,
  `fecha_fin_penalizacion` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`dni`, `nombre`, `telefono`, `email`, `password`, `penalizacion`, `fecha_inicio_penalizacion`, `fecha_fin_penalizacion`) VALUES
('05309480E', 'LUCÍA MARTÍNEZ', 644567123, 'lucia.martinez@yahoo.es', 'luciaM6789', 'NO', '0000-00-00', '0000-00-00'),
('05933149T', 'MIGUEL ÁNGEL RUIZ', 644567891, 'miguel.ruiz@yahoo.es', 'miguel123', 'NO', '0000-00-00', '0000-00-00'),
('12342878C', 'JOSÉ FERNÁNDEZ', 611234876, 'jose.fernandez@outlook.com', 'joseF5678', 'NO', '0000-00-00', '0000-00-00'),
('13172516W', 'JESSICA SMITH', 633456780, 'j.smith@example.com', 'jsmith2024', 'NO', '0000-00-00', '0000-00-00'),
('44490085N', 'RAÚL DÍAZ', 633456890, 'raul.diaz@hotmai.com', 'raulD1234', 'NO', '0000-00-00', '0000-00-00'),
('53865310T', 'CARLOS GARCÍA', 622345679, 'carlos.garcia@hotmail.com', 'carlospwd', 'SI', '2025-04-01', '2025-04-15'),
('71453503R', 'JOSÉ ANTONIO LÓPEZ', 677890987, 'jose.antonio@gmail.com', 'joseA4321', 'NO', '0000-00-00', '0000-00-00'),
('71454475F', 'PEDRO SÁNCHEZ', 666789013, 'pedro.sanchez@gmail.com', 'pedro2025', 'NO', '0000-00-00', '0000-00-00'),
('71463383Z', 'MANUEL GARCÍA', 655678435, 'manuel.garcia@outlook.com', 'manuelG25', 'SI', '2025-03-25', '2025-04-08'),
('71464634T', 'CARLA RUIZ', 622345987, 'carla.ruiz@gmail.com', 'carla1234', 'SI', '2025-03-20', '2025-04-03'),
('71466987F', 'SILVIA GARCÍA', 611234765, 'silvia.garcia@gmail.com', 'silviaG123', 'SI', '2025-04-10', '2025-04-24'),
('71521120K', 'ELENA FERNÁNDEZ', 688901456, 'elena.fernandez@hotmail.com', 'elenaF21', 'SI', '2025-04-15', '2025-04-29'),
('71526423B', 'CARLOS GONZÁLEZ', 699012478, 'carlos.gonzalez@outlook.com', 'carlosG2024', 'NO', '0000-00-00', '0000-00-00'),
('71528272C', 'ÁLVARO RUIZ', 644789012, 'alvaro.ruiz@gmail.com', 'alvaro123', 'NO', '0000-00-00', '0000-00-00'),
('71532355D', 'ANA LÓPEZ', 677890124, 'ana.lopez@hotmail.com', 'anaL1234', 'SI', '2025-04-10', '2025-04-24'),
('71559151X', 'LAURA MARTÍNEZ', 612345678, 'laura.martinez@gmail.com', 'pass1234', 'NO', '0000-00-00', '0000-00-00'),
('71897408', 'SANDRA SÁNCHEZ', 666789756, 'sandra.sanchez@gmail.com', 'sandraS98', 'NO', '0000-00-00', '0000-00-00'),
('72080807A', 'LUIS PÉREZ', 688901235, 'luis.perez@yahoo.es', 'luisP123', 'NO', '0000-00-00', '0000-00-00'),
('72084949M', 'EMILY JOHNSON', 655678902, 'emily.johnson@outlook.com', 'emilyJ25', 'SI', '2025-03-15', '2025-04-01'),
('72099723J', 'MARIA GONZÁLEZ', 699012346, 'maria.gonzalez@gmail.com', 'mariaG2024', 'SI', '2025-04-05', '2025-04-19');

--
-- Disparadores `usuarios`
--
DELIMITER $$
CREATE TRIGGER `before_insert_usuarios` BEFORE INSERT ON `usuarios` FOR EACH ROW BEGIN
    SET NEW.dni = UPPER(NEW.dni);
    SET NEW.nombre = UPPER(NEW.nombre);
    SET NEW.email = LOWER(NEW.email);
    SET NEW.penalizacion = UPPER(NEW.penalizacion);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_update_usuarios` BEFORE UPDATE ON `usuarios` FOR EACH ROW BEGIN
    SET NEW.dni = UPPER(NEW.dni);
    SET NEW.nombre = UPPER(NEW.nombre);
    SET NEW.email = LOWER(NEW.email);
   	SET NEW.penalizacion = UPPER(NEW.penalizacion);
END
$$
DELIMITER ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `autores`
--
ALTER TABLE `autores`
  ADD PRIMARY KEY (`id_autor`);

--
-- Indices de la tabla `ejemplares`
--
ALTER TABLE `ejemplares`
  ADD PRIMARY KEY (`id_ejemplar`),
  ADD KEY `isbn` (`fk_isbn`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `libros`
--
ALTER TABLE `libros`
  ADD PRIMARY KEY (`isbn`),
  ADD KEY `fk_id_autor` (`fk_id_autor`);

--
-- Indices de la tabla `prestamos`
--
ALTER TABLE `prestamos`
  ADD PRIMARY KEY (`id_prestamo`),
  ADD KEY `fk_id_ejemplar` (`fk_id_ejemplar`),
  ADD KEY `fk_dni_empleado` (`fk_dni_empleado`),
  ADD KEY `fk_dni_usuario` (`fk_dni_usuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `prestamos`
--
ALTER TABLE `prestamos`
  MODIFY `id_prestamo` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `ejemplares`
--
ALTER TABLE `ejemplares`
  ADD CONSTRAINT `fk_isbn` FOREIGN KEY (`fk_isbn`) REFERENCES `libros` (`isbn`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `libros`
--
ALTER TABLE `libros`
  ADD CONSTRAINT `fk_id_autor` FOREIGN KEY (`fk_id_autor`) REFERENCES `autores` (`id_autor`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `prestamos`
--
ALTER TABLE `prestamos`
  ADD CONSTRAINT `fk_dni_empleado` FOREIGN KEY (`fk_dni_empleado`) REFERENCES `empleados` (`dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_dni_usuario` FOREIGN KEY (`fk_dni_usuario`) REFERENCES `usuarios` (`dni`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_id_ejemplar` FOREIGN KEY (`fk_id_ejemplar`) REFERENCES `ejemplares` (`id_ejemplar`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
