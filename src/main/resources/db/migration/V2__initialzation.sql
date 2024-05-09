INSERT INTO category(id, name)
VALUES (1, 'SUV'),
       (2, 'Sedan'),
       (3, 'Coupe'),
       (4, 'Pickup'),
       (5, 'Convertible');

ALTER SEQUENCE category_id_seq RESTART WITH 6;

INSERT INTO manufacturer(id, name)
VALUES (1, 'Audi'),
       (2, 'Chevrolet'),
       (3, 'Cadillac'),
       (4, 'Acura'),
       (5, 'BMW'),
       (6, 'INFINITI');

ALTER SEQUENCE manufacturer_id_seq RESTART WITH 7;

INSERT INTO model(id, name, manufacturer_id)
VALUES (1, 'Q3', 1),
       (2, 'Malibu', 2),
       (3, 'Escalade ESV', 3),
       (4, 'Corvette', 2),
       (5, '3 Series', 5),
       (6, 'QX80', 6),
       (7, 'TLX', 4);

ALTER SEQUENCE model_id_seq RESTART WITH 8;

INSERT INTO car(id, object_id, model_id, year)
VALUES (1, 'ZRgPP9dBMm', 1, 2020),
       (2, 'GoIb4TNqDz', 1, 2019),
       (3, 'cptB1C1NSL', 2, 2020),
       (4, 'iObFnlwCdJ', 2, 2019),
       (5, 'ElhqsRZDnP', 3, 2020),
       (6, 'bdW1WipxOD', 3, 2019),
       (7, 'LUzyWMYJpW', 4, 2020),
       (8, '3Fep0ntHSg', 7, 2018);

ALTER SEQUENCE car_id_seq RESTART WITH 9;

INSERT INTO car_category(car_id, category_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 1),
       (6, 1),
       (7, 3),
       (7, 5),
       (8, 2);
