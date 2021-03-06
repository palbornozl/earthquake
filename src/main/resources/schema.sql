DROP SCHEMA IF EXISTS EARTHQUAKE;

CREATE SCHEMA IF NOT EXISTS EARTHQUAKE;

DROP TABLE IF EXISTS EARTHQUAKE;

CREATE TABLE IF NOT EXISTS EARTHQUAKE.EARTHQUAKE
(
    created_at   timestamp not null default current_timestamp() primary key,
    origen        varchar(250),
    observacion      varchar(250),
    fecha_inicio datetime,
    fecha_fin    datetime,
    magnitud_min float,
    magnitud_max float,
    salida       json
);