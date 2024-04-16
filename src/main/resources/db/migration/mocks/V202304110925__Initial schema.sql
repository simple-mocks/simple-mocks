CREATE TABLE IF NOT EXISTS SERVICE (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code varchar(16) NOT NULL UNIQUE,
    created_at timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS HTTP_MOCK (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    method varchar(16) NOT NULL,
    path_regex varchar(512) NOT NULL,
    service_id INTEGER NOT NULL,
    type varchar(64) NOT NULL,
    storage_type varchar(64) NOT NULL,
    storage_id varchar(128) NOT NULL,
    created_at timestamp NOT NULL,
    foreign key (service_id) references SERVICE(id)
);
