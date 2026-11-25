-- US-22: cancelamento de colheita com motivo (o registro original é preservado)
ALTER TABLE colheitas ADD COLUMN cancelada BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE colheitas ADD COLUMN motivo_cancelamento VARCHAR(500);
ALTER TABLE colheitas ADD COLUMN cancelada_por_id BIGINT;
ALTER TABLE colheitas ADD COLUMN cancelada_em TIMESTAMP(6);

ALTER TABLE colheitas
    ADD CONSTRAINT fk_colheitas_cancelada_por
        FOREIGN KEY (cancelada_por_id) REFERENCES usuarios(id);
