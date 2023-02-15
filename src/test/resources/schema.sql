DROP SCHEMA IF EXISTS EARTHQUAKE;

CREATE SCHEMA IF NOT EXISTS EARTHQUAKE;

DROP TABLE IF EXISTS EARTHQUAKE;

CREATE TABLE IF NOT EXISTS EARTHQUAKE.TBL_EARTHQUAKE
(
    id           uuid         not null DEFAULT RANDOM_UUID() primary key,
    created_at   timestamp not null default current_timestamp(),
    origen       varchar(250),
    observacion  varchar(250),
    fecha_inicio datetime,
    fecha_fin    datetime,
    magnitud_min float,
    magnitud_max float,
    salida       json,
    token        text    not null
);