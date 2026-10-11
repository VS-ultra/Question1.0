CREATE TABLE requests(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    amount NUMERIC(14,2) NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('NEW', 'APPROVED', 'REJECTED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    decided_at TIMESTAMP
);

CREATE INDEX idx_requests_user_id    ON requests (user_id);
CREATE INDEX idx_requests_product_id ON requests (product_id);
CREATE INDEX idx_requests_status_created_at ON requests (status, created_at);
