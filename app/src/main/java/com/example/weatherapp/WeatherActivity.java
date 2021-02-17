package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WeatherActivity extends AppCompatActivity {

    private CurrentWeatherService currentWeatherService;

    private View weatherContainer;
    private ProgressBar weatherProgressBar;
    private TextView temperature, location, weatherCondition, humid;
    private ImageView weatherConditionIcon;
    private EditText locationField;
    private FloatingActionButton fab;

    private boolean fetchingWeather = false;
    private int textCount = 0;
    private String currentLocation = "Saharanpur";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentWeatherService = new CurrentWeatherService(this);
        humid= findViewById(R.id.humidity);
        weatherContainer = findViewById(R.id.weather_container);
        weatherProgressBar = findViewById(R.id.weather_progress_bar);
        temperature = findViewById(R.id.temperature);
        location = findViewById(R.id.location);
        weatherCondition = findViewById(R.id.weather_condition);
        weatherConditionIcon = findViewById(R.id.weather_condition_icon);
        locationField = findViewById(R.id.location_field);
        fab = findViewById(R.id.fab);

        locationField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                count = s.toString().trim().length();
                fab.setImageResource(count == 0 ? R.drawable.refresh : R.drawable.search);
                textCount = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textCount == 0) {
                    refreshWeather();
                } else {
                    searchForWeather(locationField.getText().toString());
                    locationField.setText("");
                }
            }
        });

        // Start a search for the default location
        searchForWeather(currentLocation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentWeatherService.cancel();
    }

    private void refreshWeather() {
        if (fetchingWeather) {
            return;
        }
        searchForWeather(currentLocation);
    }

    private void searchForWeather(@NonNull final String location) {
        toggleProgress(true);
        fetchingWeather = true;
        currentWeatherService.getCurrentWeather(location, currentWeatherCallback);
    }

    private void toggleProgress(final boolean showProgress) {
        weatherContainer.setVisibility(showProgress ? View.GONE : View.VISIBLE);
        weatherProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
    }

    private final CurrentWeatherService.CurrentWeatherCallback currentWeatherCallback = new CurrentWeatherService.CurrentWeatherCallback() {

        @Override
        public void onCurrentWeather(@NonNull CurrentWeather currentWeather) {
            currentLocation = currentWeather.location;
            temperature.setText(String.valueOf(currentWeather.getTempCelcius()));
            location.setText(currentWeather.location);
            weatherCondition.setText(currentWeather.weatherCondition);
            humid.setText(currentWeather.humid);
            weatherConditionIcon.setImageResource(CurrentWeatherUtils.getWeatherIconResId
                    (currentWeather.conditionId));
            toggleProgress(false);
            fetchingWeather = false;
        }

        @Override
        public void onError(@Nullable Exception exception) {
            toggleProgress(false);
            fetchingWeather = false;
            Toast.makeText(WeatherActivity.this, "There was an error fetching weather, " +
                    "try again.", Toast.LENGTH_SHORT).show();
        }
    };

}