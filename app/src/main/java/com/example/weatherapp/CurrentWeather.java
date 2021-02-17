package com.example.weatherapp;

public class CurrentWeather {
    final String location;
    final int conditionId;
    final String weatherCondition;
    final double tempKelvin;
    final String humid;

    public CurrentWeather(final String location,
                          final int conditionId,
                          final String weatherCondition,
                          final double tempKelvin,
                          final String humid)
    {
        this.location = location;
        this.conditionId = conditionId;
        this.weatherCondition = weatherCondition;
        this.tempKelvin = tempKelvin;
        this.humid = humid;
    }

    public int getTempCelcius() {
        return (int)(tempKelvin-271.15);
    }
}
