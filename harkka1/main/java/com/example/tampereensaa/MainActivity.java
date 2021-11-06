package com.example.tampereensaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private float temperature;
    private float windSpeed;
    private String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        if (savedInstanceState != null){
            temperature = savedInstanceState.getFloat("TEMP_KEY");
            windSpeed = savedInstanceState.getFloat("WIND_KEY");
            description = savedInstanceState.getString("DESC_KEY");

        }
    }

    @Override
    protected void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("TEMP_KEY", temperature);
        outState.putFloat("WIND_KEY", windSpeed);
        outState.putString("DESC_KEY", description);
    }

    public void getWeatherData(View view) {
        //Haetaan säätiedot palvelimelta openweathermap API
        //Instantiate the RequestQueue

        String url ="https://api.openweathermap.org/data/2.5/weather?q=tampere&units=metric&appid=3a594df62ac6d6e9e3ae017c56bc9846";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    parseJsonAndUpdateUI( response);
                },
                error -> {
                    TextView weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
                    weatherDescriptionTextView.setText(getString(R.string.ierror));
                    TextView temperatureTextView = findViewById(R.id.temperatureTextView);
                    temperatureTextView.setText("");
                    TextView windTextView = findViewById(R.id.windTextView);
                    windTextView.setText("");
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void parseJsonAndUpdateUI(String response) {
        try {
            //Kaivetaan data JSON objektista
            JSONObject rootObject = new JSONObject ( response);
            temperature = (float) rootObject.getJSONObject("main").getDouble("temp");
            windSpeed = (float) rootObject.getJSONObject("wind").getDouble("speed");
            description = rootObject.getJSONArray("weather").getJSONObject(0).getString("main");

            //Laitetaan data näytölle
            TextView weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
            if (description.equals("Clear")){
                weatherDescriptionTextView.setText(getString(R.string.clear));
            }
            else if (description.equals("Clouds")){
                weatherDescriptionTextView.setText(getString(R.string.clouds));
            }
            else if (description.equals("Atmosphere")){
                weatherDescriptionTextView.setText(getString(R.string.atmosphere));
            }
            else if (description.equals("Snow")){
                weatherDescriptionTextView.setText(getString(R.string.snow));
            }
            else if (description.equals("Rain")){
                weatherDescriptionTextView.setText(getString(R.string.rain));
            }
            else if (description.equals("Drizzle")){
                weatherDescriptionTextView.setText(getString(R.string.drizzle));
            }
            else if (description.equals("Thunderstorm")){
                weatherDescriptionTextView.setText(getString(R.string.thunderstorm));
            }

            TextView temperatureTextView = findViewById(R.id.temperatureTextView);
            temperatureTextView.setText("" + temperature + "C");
            TextView windTextView = findViewById(R.id.windTextView);
            windTextView.setText(windSpeed + " m/s");



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openForecast(View view) {
        //Avataan sääennustesivu
        Intent openForecast = new Intent( this, ForecastActivity.class);
        openForecast.putExtra( "CITY_NAME_KEY", "Tampere");
        startActivity( openForecast );
    }
}