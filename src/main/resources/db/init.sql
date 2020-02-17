CREATE TABLE account(
    id bigint auto_increment,
    current_amount decimal (8),
    blocked_amount decimal (8) DEFAULT 0
);

CREATE TABLE transfer(
    id bigint auto_increment,
    debit_account_id bigint,
    credit_account_id bigint,
    amount decimal (8),
    status varchar,
    created_at timestamp not null,
    updated_at timestamp null
);

INSERT INTO account VALUES  (1, 100, 0);
INSERT INTO account VALUES  (2, 200, 0);
INSERT INTO account VALUES  (3, 300, 0);
INSERT INTO account VALUES  (4, 400, 0);
INSERT INTO account VALUES  (5, 500, 0);
INSERT INTO account VALUES  (6, 600, 0);
INSERT INTO account VALUES  (7, 700, 0);
INSERT INTO account VALUES  (8, 800, 0);
INSERT INTO account VALUES  (9, 000, 0);
INSERT INTO account VALUES  (10, 1000, 0);
INSERT INTO account VALUES  (11, 1100, 0);
INSERT INTO account VALUES  (12, 1200, 0);