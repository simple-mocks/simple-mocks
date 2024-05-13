CREATE TABLE IF NOT EXISTS KAFKA_PRODUCER
(
    code              varchar(256) NOT NULL PRIMARY KEY,
    description       varchar(1024),
    bootstrap_servers varchar(1024),
    created_at        timestamp    NOT NULL,
    modified_at       timestamp    NOT NULL,
    UNIQUE (code COLLATE NOCASE)
);

CREATE TABLE IF NOT EXISTS KAFKA_PRODUCER_PROPERTY
(
    kafka_producer_code varchar(256) NOT NULL,
    key                 varchar(256) NOT NULL,
    value               varchar(512) NOT NULL,
    created_at          timestamp    NOT NULL,
    modified_at         timestamp    NOT NULL,
    PRIMARY KEY (kafka_producer_code, key),
    foreign key (kafka_producer_code) references KAFKA_PRODUCER (code)
);
