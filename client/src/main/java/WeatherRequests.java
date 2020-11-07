public enum WeatherRequests {
    CurrentWeather(1, "Current Weather forecast"),
    DailyForecastFor7Days(2, "Daily forecast for 7 days"),
    BasicWeatherMaps(3, "Basic Weather maps"),
    MinuteForecast(4, "Minute forecast for 1 hour"),
    HistoricalWeather(5, "Historical Weather for 5 days");

    private final int value;
    private final String description;

    WeatherRequests(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
