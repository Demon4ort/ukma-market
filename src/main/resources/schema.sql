CREATE TABLE Category (
    CategoryID int NOT NULL ,
    CategoryName varchar(255),
    PRIMARY KEY (CategoryID)
);
INSERT INTO Category (CategoryID, CategoryName)
VALUES (1, 'Beverages'),
       (2, 'Meat products'),
       (3, 'Milk products'),
       (4, 'cereals');

CREATE TABLE Product (
    ProductID int NOT NULL,
    CategoryID int,
    ProductName varchar(50),
    ProductCharacteristics varchar(10000),
    PRIMARY KEY (ProductID),
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);
INSERT INTO Product (ProductID, ProductName, ProductCharacteristics)
VALUES (1, 1, 'Beer', 'Beer from Lviv'),
       (2, 1, 'Orange juice'),
       (3, 1, 'Redbull', 'Energy drink'),
       (4, 2, 'Chicken legs'),
       (5, 2, 'Sausages', 'Hot dog Sausages'),
       (6, 3, 'Milk', '8.5% Milk'),
       (7, 3, 'Yogurt', 'Strawberry yogurt'),
       (8, 3, 'Cheese',),
       (9, 4, 'Buckwheat','Ordinary Buckwheat'),
       (10, 4, 'Rice', 'Good rice'),
       (11, 4, 'Bulgur');
CREATE TABLE Store_Product (
    UPC varchar(12) NOT NULL,
    UPC_prom varchar(12),
    ID_Product int NOT NULL,
    selling_price Decimal(13,4) NOT NULL,
    products_number int NOT NULL,
    promotional_product Boolean NOT NULL,
    PRIMARY KEY (UPC),
    FOREIGN KEY (UPC_prom) REFERENCES Store_Product(UPC),
    FOREIGN KEY (ID_Product) REFERENCES Product(ProductID)
);
INSERT INTO Store_Product (UPC, UPC_prom, ID_Product, selling_price, products_number, promotional_product)
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
CREATE TABLE Employee (
    id_employee varchar(10) NOT NULL,
    empl_surname varchar(50) NOT NULL,
    empl_name varchar(50) NOT NULL ,
    empl_patronymic varchar(50),
    empl_role varchar(50) NOT NULL ,
    salary Decimal(13,4) NOT NULL ,
    date_of_birth DATE NOT NULL ,
    date_of_start DATE NOT NULL ,
    phone_number varchar(13) NOT NULL ,
    city varchar(50) NOT NULL ,
    street varchar(50) NOT NULL ,
    zip_code varchar(9) NOT NULL,
    PRIMARY KEY (id_employee)
);
CREATE TABLE Customer_Card (
    card_number varchar(13) NOT NULL ,
    cust_surname varchar(50) NOT NULL,
    cust_name varchar(50) NOT NULL,
    cust_patronymic varchar(50),
    phone_number varchar(13) NOT NULL ,
    city varchar(50) NOT NULL ,
    street varchar(50) NOT NULL ,
    zip_code varchar(9) NOT NULL,
    percentage int NOT NULL,
    PRIMARY KEY (card_number)
);
CREATE TABLE Check (
    check_number varchar(10) NOT NULL ,
    id_employee varchar(10) NOT NULL ,
    card_number varchar(13),
    print_date DATE NOT NULL ,
    sum_total decimal (13,4) NOT NULL ,
    vat decimal (13,4) NOT NULL,
    PRIMARY KEY (check_number),
    FOREIGN KEY (id_employee) REFERENCES Employee(id_employee),
    FOREIGN KEY (card_number) REFERENCES Customer_Card(card_number)

);
CREATE TABLE Sale (
    UPC varchar(12) NOT NULL,
    check_number varchar(10) NOT NULL,
    product_number int NOT NULL,
    selling_price DECIMAL(13,4) NOT NULL,
    PRIMARY KEY (UPC),
    PRIMARY KEY (check_number),
    FOREIGN KEY (UPC) REFERENCES Store_Product(UPC),
    FOREIGN KEY (check_number) REFERENCES Check(check_number)
);

