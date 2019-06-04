CREATE TABLE IF NOT EXISTS BID (
    bidid IDENTITY,
	product_id Long,
	trade_id VARCHAR(200),
	client_id Long,
	bidprice DOUBLE,
	amount DOUBLE,
	succeed Boolean

);