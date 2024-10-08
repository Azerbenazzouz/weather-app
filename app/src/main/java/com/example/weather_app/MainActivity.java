package com.example.weather_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    TextView cityName, weather;
    Button search;

    String url;
    String api_key= "0139660c4180218462eaaab1d4e09d23";
    final String[] temp = {""};

    class getWeather extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            StringBuilder result = new StringBuilder();
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line= "";
                while((line = reader.readLine()) != null){
                    result.append(line).append('\n');
                }
                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String resault){
            super.onPostExecute(resault);
            try {
                JSONObject jsonObject = new JSONObject(resault);
                String weatherTemp = jsonObject.getJSONObject("main").getString("temp");
                weather.setText(weatherTemp.concat(" \u2103"));
            }catch (Exception e){
//                e.printStackTrace();
                weather.setText("No Data Found");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        weather = findViewById(R.id.weather);
        search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"cityName.getText()",Toast.LENGTH_SHORT);
                String city = cityName.getText().toString();
                try {
                    if(!city.equals("")){
                        url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=" + api_key;
                    }else{
                        Toast.makeText(MainActivity.this,"Enter City Name",Toast.LENGTH_SHORT);
                    }

                    getWeather task = new getWeather();
                    temp[0] = task.execute(url).get();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                if(!temp[0].equals("")){
                    weather.setText(temp[0]);
                }else {
                    weather.setText("No Data Found");
                }
            }
        });
    }
}