# ClickHouse Stocks Analytics

The project for "Advanced Databases" class on "System and Software Engineering" master program at HSE University.

## Project goal
To demonstrate the possibilities of OLAP DBMS ClickHouse for storing and processing stock data
of companies from S&P 500 rating using Grafana and Android application.

## Group members
- Andrey Volkov
- Asgar Zagitov
- Alina Kolchanova
- Liana Batalova

## Project description

The project allows users to see stocks data from S&P 500 rating in two formats: Grafana dashboard
and Android App. The data is stored in DBMS ClickHouse.

### Components
The project consists of several applications/servers:

- **ClickHouse server** - main server of ClickHouse DBMS
- **ClickHouse metrics exporter**
- **Prometheus** - storage for ClickHouse metrics
- **Grafana** - tool for visualization stocks time series
- **Loader** - back-end application that saves data to ClickHouse in real time
- **Reader** - back-end application that executes queries in ClickHouse by requests from Android
- **Android App** - application that shows stats & charts based on the result from Reader

### Data
Data: S&P 500 stock data ([Kaggle link](https://www.kaggle.com/kp4920/s-p-500-stock-data-time-series-analysis/data?select=all_stocks_5yr.csv)).

### Applications functionalities

- Loader
  1. loads dataset of S&P 500 stock data
  2. inserts the data to ClickHouse server

- Reader
  1. receives requests from Android App
  2. selects data from ClickHouse
  3. map data to special format
  4. returns data to Android App

### UI functionalities

The user will be provided with 2 interfaces to access stocks data stored in ClickHouse - Grafana & Android App. The functionality of these interfaces will be the following:

1. user selects the date range
2. user selects the companies (one or more in the list of 500 companies)
3. user discover the charts on open, high, low, close prices for selected companies

## Project planning

- 1 week - prepare project infrastructure
- 2 weeks - design ClickHouse tables (check different options and make the most effective design decision)
- 1 week - develop Loader application
- 1 week - develop Reader application
- 1 weeks - develop Android application
- 1 week - test all project components
