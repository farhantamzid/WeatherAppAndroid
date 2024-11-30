package com.example.weatherappandroid;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String API = "YHYAP8UP6ZJP5P76GAG4LJLNZ";
    private String CITY = "Dhaka";

    private weatherTask currentTask;

    Button refreshButton;
    TextView tvSunrise, tvSunset,tvMainTemp, tvMinTemp, tvMaxTemp, tvCurrentCondition,  tvFeelsLike, tvHumidity, tvChanceOfRain, tvWindSpeed, tvMainLocation,
            date1,date2,date3,date4,date5,date6,min1,min2,min3,min4,min5,min6,max1,max2,max3,max4,max5,max6;
    ImageView upperImageView, conditionImage1,conditionImage2,conditionImage3,conditionImage4,conditionImage5,conditionImage6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvSunrise= findViewById(R.id.tvSunrise);
        tvSunset=findViewById(R.id.tvSunset);
        tvMainTemp=findViewById(R.id.tvMainTemp);
        tvMinTemp=findViewById(R.id.tvMinTemp);
        tvMaxTemp=findViewById(R.id.tvMaxTemp);
        tvCurrentCondition=findViewById(R.id.tvCurrentCondition);
        tvFeelsLike=findViewById(R.id.tvFeelsLike);
        tvHumidity=findViewById(R.id.tvHumidity);
        tvChanceOfRain=findViewById(R.id.tvChanceOfRain);
        tvWindSpeed=findViewById(R.id.tvWindSpeed);
        tvMainLocation=findViewById(R.id.tvMainLocation);
        date1=findViewById(R.id.date1);
        date2=findViewById(R.id.date2);
        date3=findViewById(R.id.date3);
        date4=findViewById(R.id.date4);
        date5=findViewById(R.id.date5);
        date6=findViewById(R.id.date6);
        min1=findViewById(R.id.min1);
        min2=findViewById(R.id.min2);
        min3=findViewById(R.id.min3);
        min4=findViewById(R.id.min4);
        min5=findViewById(R.id.min5);
        min6=findViewById(R.id.min6);
        max1=findViewById(R.id.max1);
        max2=findViewById(R.id.max2);
        max3=findViewById(R.id.max3);
        max4=findViewById(R.id.max4);
        max5=findViewById(R.id.max5);
        max6=findViewById(R.id.max6);




        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshWeatherForDefaultCity();

            }
        });

            Button showDialogButton = findViewById(R.id.button);

            showDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Enter City");

                    final EditText input = new EditText(MainActivity.this);
                    builder.setView(input);

                    builder.setPositiveButton("OK", (dialog, which) -> {
                        // Get the text entered by the user
                        String userInput = input.getText().toString().trim();
                        if (!userInput.isEmpty()) {
                            updateWeatherForNewCity(userInput);  // Call the method to update weather
                        } else {
                            Toast.makeText(MainActivity.this, "City cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                    });

                    builder.show();
                }
            });


        upperImageView=findViewById(R.id.upperImageView);
        conditionImage1=findViewById(R.id.conditionImage1);
        conditionImage2=findViewById(R.id.conditionImage2);
        conditionImage3=findViewById(R.id.conditionImage3);
        conditionImage4=findViewById(R.id.conditionImage4);
        conditionImage5=findViewById(R.id.conditionImage5);
        conditionImage6=findViewById(R.id.conditionImage6);

        View view = findViewById(R.id.viewView);



        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();

        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3000);

        animationDrawable.start();


        new weatherTask().execute();
    }


    protected void updateWeatherForNewCity(String newCity) {
        if (currentTask != null && currentTask.getStatus() == AsyncTask.Status.RUNNING) {
            currentTask.cancel(true);
        }

        CITY = newCity;
        currentTask = new weatherTask();
        currentTask.execute();
    }


    protected void refreshWeatherForDefaultCity() {
        if (currentTask != null && currentTask.getStatus() == AsyncTask.Status.RUNNING) {
            currentTask.cancel(true);
        }

        CITY = "Dhaka";
        currentTask = new weatherTask();
        currentTask.execute();
    }


    class weatherTask extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.viewView).setVisibility(View.GONE);
            findViewById(R.id.cardConstraintLayout).setVisibility(View.GONE);
            refreshButton.setVisibility(View.GONE);

        }

        protected String doInBackground(String... args) {
            return HttpRequest.executeGet("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+CITY+"?unitGroup=metric&include=days%2Ccurrent&key="+API+"&contentType=json");
        }


        @SuppressLint("DefaultLocale")
        @Override

        protected void onPostExecute(String result) {
            try {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.viewView).setVisibility(View.VISIBLE);
                findViewById(R.id.cardConstraintLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.errorText).setVisibility(View.GONE);

                JSONObject jsonResponse = new JSONObject(result);


                JSONObject currentConditions = jsonResponse.getJSONObject("currentConditions");


                tvMainTemp.setText(String.format("%s", Math.round(Double.parseDouble(currentConditions.getString("temp")))));
                tvFeelsLike.setText(String.format("%s°", Math.round(Double.parseDouble(currentConditions.getString("feelslike")))));
                tvHumidity.setText(String.format("%d%%", Math.round(Double.parseDouble(currentConditions.getString("humidity")))));
                tvWindSpeed.setText(String.format("%s km/h", Math.round(Double.parseDouble(currentConditions.getString("windspeed")))));
                tvChanceOfRain.setText(String.format("%d%%", Math.round(Double.parseDouble(currentConditions.getString("precipprob")))));
                tvSunrise.setText(currentConditions.getString("sunrise").substring(0, 5));
                tvSunset.setText(currentConditions.getString("sunset").substring(0, 5));
                tvCurrentCondition.setText(currentConditions.getString("conditions"));
                tvMainLocation.setText(jsonResponse.getString("resolvedAddress"));
                CITY = jsonResponse.getString("resolvedAddress");

                String mainConditionIcon = currentConditions.getString("icon").replace('-','_');
                int mainConditionResId = getResources().getIdentifier(mainConditionIcon, "drawable", getPackageName());
                upperImageView.setImageResource(mainConditionResId);


                JSONArray days = jsonResponse.getJSONArray("days");
                for (int i = 0; i < 6; i++) {
                    JSONObject day = days.getJSONObject(i);


                    TextView dateView = null;
                    switch (i) {
                        case 0: dateView = findViewById(R.id.date1); break;
                        case 1: dateView = findViewById(R.id.date2); break;
                        case 2: dateView = findViewById(R.id.date3); break;
                        case 3: dateView = findViewById(R.id.date4); break;
                        case 4: dateView = findViewById(R.id.date5); break;
                        case 5: dateView = findViewById(R.id.date6); break;
                    }
                    if (dateView != null) dateView.setText(day.getString("datetime"));


                    TextView minTempView = null;
                    TextView maxTempView = null;
                    switch (i) {
                        case 0: minTempView = findViewById(R.id.min1); maxTempView = findViewById(R.id.max1); break;
                        case 1: minTempView = findViewById(R.id.min2); maxTempView = findViewById(R.id.max2); break;
                        case 2: minTempView = findViewById(R.id.min3); maxTempView = findViewById(R.id.max3); break;
                        case 3: minTempView = findViewById(R.id.min4); maxTempView = findViewById(R.id.max4); break;
                        case 4: minTempView = findViewById(R.id.min5); maxTempView = findViewById(R.id.max5); break;
                        case 5: minTempView = findViewById(R.id.min6); maxTempView = findViewById(R.id.max6); break;
                    }

                    if (minTempView != null) minTempView.setText(String.format("%s°C", day.getString("tempmin")));
                    if (maxTempView != null) maxTempView.setText(String.format("%s°C", day.getString("tempmax")));


                    ImageView conditionImageView = null;
                    switch (i) {
                        case 0: conditionImageView = findViewById(R.id.conditionImage1); break;
                        case 1: conditionImageView = findViewById(R.id.conditionImage2); break;
                        case 2: conditionImageView = findViewById(R.id.conditionImage3); break;
                        case 3: conditionImageView = findViewById(R.id.conditionImage4); break;
                        case 4: conditionImageView = findViewById(R.id.conditionImage5); break;
                        case 5: conditionImageView = findViewById(R.id.conditionImage6); break;
                    }

                    if (conditionImageView != null) {
                        String conditionIcon = day.getString("icon");
                        int resId = getResources().getIdentifier(conditionIcon.replace("-", "_"), "drawable", getPackageName());
                        if (resId != 0) {
                            conditionImageView.setImageResource(resId);
                        }
                    }
                }

                tvMinTemp.setText("min: " + min1.getText().subSequence(0, min1.getText().length() - 4)+"°");
                tvMaxTemp.setText("max: " + max1.getText().subSequence(0, max1.getText().length() - 4)+"°");

            } catch (Exception e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.viewView).setVisibility(View.GONE);
                findViewById(R.id.cardConstraintLayout).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.errorText)).setText("Error retrieving weather data.");
                refreshButton.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
    }
    }



