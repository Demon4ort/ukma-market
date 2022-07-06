CREATE TABLE IF NOT EXISTS main.Category
(
    category_id   VARCHAR(256) NOT NULL,
    category_name VARCHAR(256) NOT NULL,
    PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS main.Product
(
    product_id              VARCHAR(256)   NOT NULL,
    category_id             VARCHAR(256)   NOT NULL,
    product_name            VARCHAR(50)    NOT NULL,
    product_characteristics VARCHAR(10000) NOT NULL,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES Category (category_id) ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS main.Store_Product
(
    upc                 VARCHAR(256)   NOT NULL,
    upc_prom            VARCHAR(256),
    product_id          VARCHAR(256)   NOT NULL,
    selling_price       DECIMAL(13, 4) NOT NULL,
    products_number     INT            NOT NULL,
    promotional_product BOOLEAN        NOT NULL,
    PRIMARY KEY (upc),
    FOREIGN KEY (upc_prom) REFERENCES Store_Product (upc) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (product_id) REFERENCES Product (product_id) ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS main.Employee
(
    id_employee     VARCHAR(256)   NOT NULL,
    login           VARCHAR(256)   NOT NULL UNIQUE,
    password        VARCHAR(256)   NOT NULL,
    empl_surname    VARCHAR(50)    NOT NULL,
    empl_name       VARCHAR(50)    NOT NULL,
    empl_patronymic VARCHAR(50),
    empl_role       VARCHAR(50)    NOT NULL,
    salary          Decimal(13, 4) NOT NULL,
    date_of_birth   DATE           NOT NULL,
    date_of_start   DATE           NOT NULL,
    phone_number    VARCHAR(12)    NOT NULL,
    city            VARCHAR(50)    NOT NULL,
    street          VARCHAR(50)    NOT NULL,
    zip_code        VARCHAR(9)     NOT NULL,
    PRIMARY KEY (id_employee)
);

CREATE TABLE IF NOT EXISTS main.Customer_Card
(
    card_number     VARCHAR(256) NOT NULL,
    cust_surname    VARCHAR(50)  NOT NULL,
    cust_name       VARCHAR(50)  NOT NULL,
    cust_patronymic VARCHAR(50),
    phone_number    VARCHAR(12)  NOT NULL,
    city            VARCHAR(50)  NOT NULL,
    street          VARCHAR(50)  NOT NULL,
    zip_code        VARCHAR(9)   NOT NULL,
    percentage      INT          NOT NULL,
    PRIMARY KEY (card_number)
);

CREATE TABLE IF NOT EXISTS main.Receipt
(
    check_number VARCHAR(256)   NOT NULL,
    id_employee  VARCHAR(256)   NOT NULL,
    card_number  VARCHAR(13),
    print_date   DATE           NOT NULL,
    sum_total    DECIMAL(13, 4) NOT NULL,
    pdv          DECIMAL(13, 4) NOT NULL,
    PRIMARY KEY (check_number),
    FOREIGN KEY (id_employee) REFERENCES Employee (id_employee) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (card_number) REFERENCES Customer_Card (card_number) ON UPDATE CASCADE ON DELETE NO ACTION
);
CREATE TABLE IF NOT EXISTS main.Sale
(
    upc            VARCHAR(256)   NOT NULL,
    check_number   VARCHAR(256)   NOT NULL,
    product_number INT            NOT NULL,
    selling_price  DECIMAL(13, 4) NOT NULL,
    PRIMARY KEY (upc, check_number),
    FOREIGN KEY (upc) REFERENCES Store_Product (upc) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (check_number) REFERENCES Receipt (check_number) ON UPDATE CASCADE ON DELETE CASCADE
);
