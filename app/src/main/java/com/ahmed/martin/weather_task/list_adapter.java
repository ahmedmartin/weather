package com.ahmed.martin.weather_task;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class list_adapter extends ArrayAdapter <String> {

    private final Activity context;
    private final ArrayList<String> date;
    private final ArrayList<String> temp;

    public list_adapter(Activity context, ArrayList<String> date, ArrayList<String> temp) {
        super(context, R.layout.show_list_view, date);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.date = date;
        this.temp = temp;
    }
    handler hand ;
    public View getView(final int position, View view, final ViewGroup parent) {


        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.show_list_view, null);
            hand =new handler(view);
            view.setTag(hand);
        }
        hand = (handler) view.getTag();


        hand.getdate().setText(date.get(position));
        hand.getTemp().setText(temp.get(position));

        return view;


    }

    public class handler {

        private View v;
        private TextView date;
        private TextView temp;




        public handler(View v) {
            this.v = v;
        }

        public TextView getdate() {
            if(date ==null)
                date =v.findViewById(R.id.date);
            return date;
        }

        public TextView getTemp() {
            if(temp ==null)
                temp =v.findViewById(R.id.temp);
            return temp;
        }

        public void setdata(TextView tv) {
            this.date = tv;
        }

        public void settemp(TextView iv) {
            this.temp = iv;
        }



    }
}
