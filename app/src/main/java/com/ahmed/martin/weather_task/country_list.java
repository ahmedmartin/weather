package com.ahmed.martin.weather_task;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class country_list extends AppCompatActivity {

    String cities [] ={"Cairo","Giza","Luxor","Alexandria","Aswan"};
    ArrayAdapter <String> adapter ;
    ListView city_list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        city_list=findViewById(R.id.cities);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,cities);
        city_list.setAdapter(adapter);
        city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent t = new Intent(country_list.this,weather_details.class);
                t.putExtra("city",cities[position]);
                startActivity(t);
            }
        });
    }

    public void contact_us(View view) {
        startActivity(new Intent(this,contact_us.class));
    }
}
