enum WeatherRequests {
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

public class WeatherRequest {
    public static final int CurrentWeather = 1;
    public static final int DailyForecastFor7Days = 2;
    public static final int BasicWeatherMaps = 3;
    public static final int MinuteForecast = 4;
    public static final int HistoricalWeather = 5;

    private final int requestType;
    private final String query;

    WeatherRequest(int requestType, String query) {
        this.requestType = requestType;
        this.query = query;
    }

    public int getRequestType() {
        return requestType;
    }

    public String getQuery() {
        return query;
    }
}
