CREATE TABLE transactions(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    request_id INT NOT NULL REFERENCES requests(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('DISBURSEMENT', 'PAYMENT', 'DEPOSIT_TOP_UP', 'WITHDRAWAL')),
    amount NUMERIC(14,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_created_at ON transactions (created_at);
CREATE INDEX idx_transactions_request_created ON transactions (request_id, created_at);
CREATE INDEX idx_transactions_disbursement_created
    ON transactions (created_at) WHERE type = 'DISBURSEMENT';