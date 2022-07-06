INSERT INTO main.Category (category_id, category_name)
VALUES (1, 'Beverages'),
       (2, 'Meat products'),
       (3, 'Milk products'),
       (4, 'cereals');
INSERT INTO main.Product (product_id, category_id, product_name, product_characteristics)
VALUES (1, 1, 'Beer', 'Beer from Lviv'),
       (2, 1, 'Orange juice', 'fresh'),
       (3, 1, 'Redbull', 'Energy drink'),
       (4, 2, 'Chicken legs', 'big'),
       (5, 2, 'Sausages', 'Hot dog Sausages'),
       (6, 3, 'Milk', '8.5% Milk'),
       (7, 3, 'Yogurt', 'Strawberry yogurt'),
       (8, 3, 'Cheese', 'yellow'),
       (9, 4, 'Buckwheat', 'Ordinary Buckwheat'),
       (10, 4, 'Rice', 'Good rice'),
       (11, 4, 'Bulgur', 'sumptuous');
INSERT INTO Store_Product (upc, upc_prom, product_id, selling_price, products_number, promotional_product)
VALUES (111111111111, 111111111111, 1, 5, 10, 1),
       (111111111112, 111111111112, 2, 3, 0, 0),
       (111111112223, NULL, 3, 2, 1, 1),
       (111122223333, 111122223333, 4, 7, 100, 1),
       (222211113333, 222211113333, 5, 8, 0, 0),
       (555544443333, NULL, 6, 1, 23, 1),
       (432143214321, 432143214321, 7, 20, 0, 0),
       (222222222222, 222222222222, 8, 71, 9, 1),
       (666666666666, 666666666666, 9, 21, 3, 1),
       (777777777777, NULL, 10, 10, 10, 1),
       (101010101010, 101010101010, 11, 100, 32, 1);
INSERT INTO Employee (id_employee, login, password, empl_surname, empl_name, empl_patronymic, empl_role, salary,
                      date_of_birth, date_of_start, phone_number, city, street, zip_code)
VALUES (1, 'abbbab', 'acscdscszc', 'Kohanyi', 'Andryi', 'Vasulyovuch', 'CASHIER', 2000, 2002 - 01 - 01, 2020 - 01 - 05,
        380970970970, 'Kyiv', 'Dobra', 111111111),
       (2, 'abbasbab', 'acscdscszc', 'Romanova', 'Maria', 'Romanivna', 'MANAGER', 3500, 1999 - 03 - 26, 2017 - 04 - 08,
        380123456789, 'Lviv', 'Kolomyiska', 222222222),
       (3, 'abbsdvbab', 'acscdscszc', 'Kymednyi', 'Rosette', NULL, 'CASHIER', 2500, 1987 - 06 - 03, 2021 - 10 - 02,
        380123123123, 'Kyiv', 'Ostannya', 333333333),
       (4, 'abbhytbab', 'acscdscszc', 'Kashtan', 'Olga', 'Oleksandrivna', 'MANAGER', 3500, 1976 - 01 - 28,
        2015 - 12 - 12, 380970567234, 'Kharkiv', 'Vesnyana', 444444444);
INSERT INTO Customer_Card (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code,
                           percentage)
VALUES (1, 'Garnyi', 'Dima', 'Danulovuch', 380987123423, 'Lviv', 'Geroyiv', 123123123, 3),
       (2, 'Garbuzova', 'Anastasia', NULL, 380981234512, 'Mukolaiv', 'Kashtanova', 123412345, 12),
       (3, 'Alekseenko', 'Dmytro', 'Ostapovuch', 380670985617, 'Zaporizha', 'Malunova', 213213213, 19),
       (4, 'Malanuch', 'Yuriy', 'Pavlovuch', 380678736181, 'Zumna Voda', 'Tankova', 567432098, 2);
INSERT INTO Receipt (check_number, id_employee, card_number, print_date, sum_total, pdv)
VALUES (1, 1, 2, 2022 - 01 - 02, 49, 2),
       (2, 1, 4, 2022 - 02 - 02, 500, 10),
       (3, 3, 2, 2022 - 02 - 02, 1562, 15),
       (4, 3, 1, 2021 - 12 - 31, 10000, 100),
       (5, 2, 3, 2022 - 01 - 15, 178, 12),
       (6, 4, NULL, 2022 - 01 - 23, 1800, 11),
       (7, 3, 4, 2021 - 12 - 30, 4325, 40);
INSERT INTO Sale (UPC, check_number, product_number, selling_price)
VALUES (111111111111, 1, 20, 12),
       (111111111112, 3, 16, 22),
       (111111112223, 2, 1, 90),
       (111122223333, 5, 3, 23),
       (222211113333, 7, 13, 55),
       (555544443333, 3, 45, 32),
       (432143214321, 1, 11, 31),
       (222222222222, 2, 5, 65),
       (666666666666, 4, 2, 22),
       (777777777777, 4, 9, 41),
       (101010101010, 6, 1, 180);

