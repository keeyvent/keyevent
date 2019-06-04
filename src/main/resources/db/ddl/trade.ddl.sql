CREATE TABLE IF NOT EXISTS TRADE (
    id IDENTITY,
	product_id Long,
	tradeid VARCHAR(200),
	price DOUBLE,
	create_date TIMESTAMP
);