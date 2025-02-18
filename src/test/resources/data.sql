CREATE TABLE orders
(
    id                      UUID PRIMARY KEY,
    customer_id             BIGINT,
    status                  VARCHAR(255),
    estimated_delivery_date TIMESTAMP,
    express_delivery        BOOLEAN,
    created_at              TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP NOT NULL
);

CREATE TABLE order_item
(
    id         UUID PRIMARY KEY,
    order_id   UUID,
    product_id BIGINT,
    quantity   INT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE order_tracking
(
    id         UUID PRIMARY KEY,
    order_id   UUID,
    latitude   DOUBLE PRECISION,
    longitude  DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE payment
(
    id             UUID PRIMARY KEY,
    order_id       UUID,
    amount         DOUBLE PRECISION,
    status         VARCHAR(255),
    payment_method VARCHAR(255),
    created_at     TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

INSERT INTO orders (id, customer_id, status, estimated_delivery_date, express_delivery, created_at, updated_at)
VALUES ('2f5d9a63-e7f2-4f6e-91b9-3b61a7c829f2', 1, 'OPEN', '2025-03-01 12:00:00', TRUE,
        '2025-02-16T13:27:31.167337+00:00', '2025-02-16T13:27:31.167337+00:00'),
       ('2f5d9a63-e7f2-4f6e-91b9-3b61a7c829f3', 1, 'OPEN', '2025-03-01 12:00:00', TRUE,
        '2025-02-16T13:27:31.167337+00:00', '2025-02-16T13:27:31.167337+00:00')
;

INSERT INTO order_item (id, order_id, product_id, quantity, created_at, updated_at)
VALUES ('ba216cd4-6cc6-44c3-a92f-c62a5c6a6cc9', '2f5d9a63-e7f2-4f6e-91b9-3b61a7c829f2', 1001, 2,
        '2025-02-16T13:27:31.167337+00:00',
        '2025-02-16T13:27:31.167337+00:00');

INSERT INTO order_tracking (id, order_id, latitude, longitude, created_at, updated_at)
VALUES ('1ac66c72-2445-429d-bf6a-5f9b8cf40a71', '2f5d9a63-e7f2-4f6e-91b9-3b61a7c829f2', 40.7128, -74.0060,
        '2025-02-16T13:27:31.167337+00:00', '2025-02-16T13:27:31.167337+00:00');

INSERT INTO payment (id, order_id, amount, status, payment_method, created_at, updated_at)
VALUES ('9f9574a0-74e7-4d87-b768-16f801cc6164', '2f5d9a63-e7f2-4f6e-91b9-3b61a7c829f2', 150.75, 'PAID',
        'CREDIT_CARD', '2025-02-16T13:27:31.167337+00:00', '2025-02-16T13:27:31.167337+00:00');
