CREATE DATABASE IF NOT EXISTS stocks_analytics;

CREATE OR REPLACE TABLE stocks_analytics.stocks (
    company LowCardinality(String),
    date Date CODEC(DoubleDelta, ZSTD(16)),
    open Float32 CODEC(Gorilla, ZSTD(16)),
    high Float32 CODEC(Gorilla, ZSTD(16)),
    low Float32 CODEC(Gorilla, ZSTD(16)),
    close Float32 CODEC(Gorilla, ZSTD(16)),
    volume Float32 CODEC(Gorilla, ZSTD(16))
) ENGINE = MergeTree()
PARTITION BY toStartOfYear(date)
PRIMARY KEY (company, date)
ORDER BY (company, date);
