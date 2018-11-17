package com.ahmed.martin.weather_task;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class weather_details extends AppCompatActivity {


    ArrayList <String> arr_date = new ArrayList<>();
    ArrayList <String> arr_temp = new ArrayList<>();
    ListView days_list;
    list_adapter adapter;
    String city;

    TextView temp , date,press,humi,city_name;

    SharedPreferences ref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        days_list = findViewById(R.id.days_list);
        temp=findViewById(R.id.temp);
        date=findViewById(R.id.date);
        press=findViewById(R.id.press);
        humi=findViewById(R.id.humidity);
        city_name=findViewById(R.id.city);

        city = getIntent().getStringExtra("city");
        city_name.setText(city);

        adapter=new list_adapter(this,arr_date,arr_temp);
        days_list.setAdapter(adapter);

        ref = getSharedPreferences(city,0);
        download dd = new download();
        if(network_connected()){
            dd.execute("http://api.openweathermap.org/data/2.5/forecast?q=" + city + ",EG&appid=2584a1f3df4ceafb4138e5b16bea0970");
        }else {
           dd.parse_data(ref.getString("data",""));
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    boolean network_connected(){
        ConnectivityManager mang = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        return mang.getActiveNetworkInfo() != null && mang.getActiveNetworkInfo().isConnected();
    }

    public class download extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = download(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences.Editor edit = ref.edit();
            edit.putString("data",s);
            edit.commit();
            parse_data(s);
        }

        void parse_data(String s){
            if(TextUtils.isEmpty(s)){
                Toast.makeText(weather_details.this, "Should Network Connected At Least One Time", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject all_data = new JSONObject(s);
                    JSONArray arr = all_data.getJSONArray("list");
                    JSONObject weather = arr.getJSONObject(0).getJSONObject("main");

                    temp.setText(weather.getString("temp") + " K");
                    press.setText("pressure : " + weather.getString("pressure"));
                    humi.setText("Humidity : " + weather.getString("humidity") + "%");
                    String ss = arr.getJSONObject(0).getString("dt_txt");

                    String as[] = ss.split(" ");
                    date.setText(as[0]);
                    for (int i = 0; i < arr.length(); i++) {
                        String st = arr.getJSONObject(i).getString("dt_txt");
                        String arr_st[] = st.split(" ");
                        if (as[1].equals(arr_st[1])) {
                            weather = arr.getJSONObject(i).getJSONObject("main");
                            arr_temp.add(weather.getString("temp") + " K");
                            arr_date.add(arr_st[0]);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



        String download(InputStream is) throws IOException {
            String rLine = "";
            StringBuilder answer = new StringBuilder();

            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader rd = new BufferedReader(isr);

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer.toString();
        }
    }
}
