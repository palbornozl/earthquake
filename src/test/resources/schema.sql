DROP TABLE IF EXISTS EARTHQUAKE;

CREATE TABLE IF NOT EXISTS EARTHQUAKE
(
    created_at   timestamp not null default current_timestamp() primary key,
    campo        varchar(250),
    fecha_inicio datetime,
    fecha_fin    datetime,
    magnitud_min float,
    magnitud_max float,
    salida       json
);