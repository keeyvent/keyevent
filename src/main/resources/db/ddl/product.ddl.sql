CREATE TABLE IF NOT EXISTS PRODUCT (
    id IDENTITY,
	cusip VARCHAR(10),
	CpnRate double,
	IssShrtName VARCHAR(200),
	MatD VARCHAR(200),
	CpnEffD VARCHAR(200),
	FrstCpnD VARCHAR(200),
	ProdTyp VARCHAR(200),
	RatingAgency VARCHAR(200),
	RatingValue VARCHAR(200),
	ratinggroup VARCHAR(200)
);