package com.example.tampereensaa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForecastActivity extends AppCompatActivity {

    private String cityName = null;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        cityName = getIntent().getStringExtra("CITY_NAME_KEY");

        //Laitetaan intentistä saatu paikkakunnan nimi otsikkotekstiksi
        TextView forecastCityNameTextView = findViewById(R.id.forecastCityNameTextView);
        forecastCityNameTextView.setText(cityName);

        //ladataan sääennuste
        queue = Volley.newRequestQueue(this);
        getForecastForCity();
    }

    public void getForecastForCity () {
        //Haetaan säätiedot palvelimelta openweathermap API
        //Instantiate the RequestQueue

        String url ="https://api.openweathermap.org/data/2.5/forecast?q=tampere&units=metric&appid=3a594df62ac6d6e9e3ae017c56bc9846";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    parseJsonAndUpdateUI( response );
                },
                error -> {
                    //virhe verkosta hakemisessa
                    TextView weatherForecastListTextView = findViewById(R.id.weatherForecastListTextView);
                    weatherForecastListTextView.setText(getString(R.string.ierror));
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseJsonAndUpdateUI(JSONObject response) {
        String weatherForecastItemString;
        try {
            JSONArray forecastList = response.getJSONArray("list");
            for ( int i=0; i<forecastList.length(); i++){
                JSONObject weatherItem = forecastList.getJSONObject(i);
                weatherForecastItemString = weatherItem.getJSONArray("weather").getJSONObject(0).getString("main");
                float temperature = (float)weatherItem.getJSONObject("main").getDouble("temp");
                //weatherForecastItemString += " " + temperature + " C";
                TextView weatherForecastListTextView = findViewById(R.id.weatherForecastListTextView);
                if (weatherForecastItemString.equals("Clear")){
                    weatherForecastItemString = getString(R.string.clear);
                }
                else if (weatherForecastItemString.equals("Clouds")){
                    weatherForecastItemString = getString(R.string.clouds);
                }
                else if (weatherForecastItemString.equals("Clouds")){
                    weatherForecastItemString = getString(R.string.clouds);
                }
                else if (weatherForecastItemString.equals("Atmosphere")){
                    weatherForecastItemString = getString(R.string.atmosphere);
                }
                else if (weatherForecastItemString.equals("Snow")){
                    weatherForecastItemString = getString(R.string.snow);
                }
                else if (weatherForecastItemString.equals("Rain")){
                    weatherForecastItemString = getString(R.string.rain);
                }
                else if (weatherForecastItemString.equals("Drizzle")){
                    weatherForecastItemString = getString(R.string.drizzle);
                }
                else if (weatherForecastItemString.equals("Thunderstorm")){
                    weatherForecastItemString = getString(R.string.thunderstorm);
                }
                weatherForecastListTextView.append( getString(R.string.in) + (i+1)*3 + getString(R.string.hours)  +weatherForecastItemString + " " + temperature + " C" + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}