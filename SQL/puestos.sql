use gucumatz;

insert into puesto (nombre, tipoComida, referencias, latitud, longitud)
values  ("Ensaladas & Baguettes - 1", "Rápida", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.3233305, -99.1807663),
        ("Flautas & Chilaquiles - 2", "Rápida", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.3233501, -99.1807958),
        ("Pizzas & Más - 3", "Rápida", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.3233836, -99.1808132),
        ("Quesadillas & Tacos - 4", "Rápida", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.3234134, -99.1808052),
        ("Chapatas & Enchiladas - 5", "Rápida", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.323436, -99.180807),
        ("Frutas & Ensaladas - 6", "Jugos y Fruta", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.323459, -99.180797),
        ("Burritos & Chilaquiles - 7", "Rápida", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.323467, -99.180784),
        ("Dulces & Refrescos - 8", "Snacks", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.323479, -99.180764),
        ("Taquitos de Guisado - 9", "Rápida", "Velaria, Estacionamiento de estudiantes [Planta Alta]", 19.3234874, -99.1807441),
        ("Diez", "Rápida", "Frente al Acceso del Estacionamiento de estudiantes", 19.323737, -99.180358),
        ("Once", "Comida corrida", "Frente al Acceso del Estacionamiento de estudiantes", 19.323756, -99.180214),
        ("Puesto", "Snacks", "Atrás de la cafetería", 19.324319, -99.179270),
        ("Chilaquiles & Pambazos", "Rápida", "Atrás de la cafetería", 19.324304, -99.179192),
        ("Puesto de sushi", "Sushi / Rápida", "Atrás de la cafetería", 19.324403, -99.179234),
        ("Puesto", "Comidas completas", "Atrás de la cafetería", 19.324372, -99.179190),
        ("La Tía Aly", "Comida casera", "Atrás de la cafetería", 19.324328, -99.179237),
        ("Harry's", "Rápida", "Atrás de la cafetería", 19.324328, -99.179165),
        ("050", "Snacks", "Atrás de la cafetería", 19.324330, -99.179117),
        ("Frosty", "Helados / Nieves", "Atrás de la cafetería", 19.324332, -99.179210),
        ("Cafetería", "Por peso", "Dentro de la cafetería", 19.3245047, -99.1793115);

insert into fotospuesto (puesto_id, url)
values (1, "puesto/1-ensaladas-y-baguettes.jpg"),
       (2, "puesto/2-flautas-y-chilaquiles.jpg"),
       (3, "puesto/3-pizzas-y-mas.jpg"),
       (4, "puesto/4-quesadillas-y-tacos.jpg"),
       (5, "puesto/5-chapatas-y-enchiladas.jpg"),
       (6, "puesto/6-frutas-y-ensaladas.jpg"),
       (7, "puesto/7-burritos-y-chilaquiles.jpg"),
       (8, "puesto/8-dulces-y-refrescos.jpg"),
       (9, "puesto/9-taquitos-de-guisado.jpg"),
       (10, "puesto/10-diez-2.jpg"),
       (10, "puesto/10-diez.jpg"),
       (11, "puesto/11-once-2.jpg"),
       (11, "puesto/11-once.jpg"),
       (12, "puesto/12.jpg"),
       (13, "puesto/13-chilaquiles.jpg"),
       (14, "puesto/14-sushi.jpg"),
       (15, "puesto/15.jpg"),
       (16, "puesto/16-la-tia-aly.jpg"),
       (17, "puesto/17-harrys.jpg"),
       (18, "puesto/18-050.jpg"),
       (19, "puesto/19-frosty.jpg"),
       (20, "puesto/20-cafeteria-1.jpg"),
       (20, "puesto/20-cafeteria-2.jpg"),
       (20, "puesto/20-cafeteria-3.jpg");
