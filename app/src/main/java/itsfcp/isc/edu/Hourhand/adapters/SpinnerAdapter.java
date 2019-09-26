package itsfcp.isc.edu.Hourhand.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class SpinnerAdapter extends BaseAdapter {
    List<String> values;
    Context context;

    public SpinnerAdapter(Context context, List<String> values){
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Object getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent){
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv=(TextView)view.findViewById(android.R.id.text1);
        if(pos==0) { //Primer elemento color Azul #39399F
            txv.setBackgroundColor(Color.parseColor("#39399F"));
            txv.setTextColor(Color.parseColor("#FFFFFF")); //Texto color Blanco
        }else { //Otros elementos ...
            txv.setBackgroundColor(Color.parseColor("#FEF9DC"));
            txv.setTextColor(Color.parseColor("#39399F")); //Texto color Azul
        }
        txv.setText(values.get(pos));
        return view;
    }

}
