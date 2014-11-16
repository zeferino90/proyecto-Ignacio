package com.index.facturapp.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.index.facturapp.ProdsCat;
import com.index.facturapp.R;

public class Adaptercatprod extends ArrayAdapter<String> {

	private Context context;
	private List<String> datos;
	private String color;
	private boolean listener;
	
	public Adaptercatprod(Activity context, List<String> values, String color, boolean listener){
		super(context, R.layout.spinner_item, values);
		datos = values;
		this.context = context;
		this.color = color;
		this.listener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.catprod_row, parent, false);
        
        final String item = datos.get(position);
        
        
        if(item != null){
        	
        	TextView text = (TextView) convertView.findViewById(R.id.spinneritem);
        	if(color.equals("black")) text.setTextColor(Color.BLACK);
        	text.setText(item);
        	Log.e("cargando listfragment", item);
        }
        if(listener){
	        convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String categoria = item;
					Intent intent = new Intent(getContext(), ProdsCat.class);
					intent.putExtra("categoria", categoria);
					Log.e("ProdCat", "empieza activity productos de una categoria");
					getContext().startActivity(intent);
				}
			});
        }
        return convertView;
	}
	
	
}
