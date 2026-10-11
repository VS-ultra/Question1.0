CREATE TABLE products (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('CREDIT', 'DEPOSIT')),
    min_amount NUMERIC(14,2) NOT NULL CHECK (min_amount >= 0),
    max_amount NUMERIC(14,2) NOT NULL,
    annual_rate NUMERIC(5,2) NOT NULL CHECK (annual_rate >= 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (min_amount <= max_amount)
);

CREATE INDEX idx_products_is_active ON products (is_active);
