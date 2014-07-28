package com.index.facturapp.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.index.facturapp.R;

public class Adaptercatprod extends ArrayAdapter<String> {

	private Context context;
	private List<String> datos;
	
	public Adaptercatprod(Activity context, List<String> values){
		super(context, R.layout.spinner_item, values);
		datos = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.catprod_row, parent, false);
        
        String item = datos.get(position);
        
        
        if(item != null){
        	TextView text = (TextView) convertView.findViewById(R.id.spinneritem);
        	text.setText(item);
        	Log.e("cargando listfragment", item);
        }
        return convertView;
	}
	
	
}
