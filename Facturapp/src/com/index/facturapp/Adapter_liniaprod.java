package com.index.facturapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class Adapter_liniaprod extends ArrayAdapter<LiniaProducto> {
	private Context context;
	private LiniaProducto[] datos;
	
	Adapter_liniaprod(Activity context, LiniaProducto[] datos){
		super(context, R.layout.liniaprod, datos);
        this.context = context;
        this.datos = datos;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//View view = convertView;
        //if (view == null) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.liniaprod, parent, false);
        //}

        LiniaProducto item = datos[position];
        if (item!= null) {
            
            TextView nomprod = (TextView) view.findViewById(R.id.nombreProd);
            EditText nprod = (EditText) view.findViewById(R.id.nProd);
            TextView precioUni = (TextView) view.findViewById(R.id.precioUni);
            TextView precioTotal = (TextView) view.findViewById(R.id.precioTotal);
            if ( nomprod != null ) {
                // do whatever you want with your string and long
                nomprod.setText(item.getNombre());
                nprod.setText(Integer.toString(item.getCantidad()));
                precioUni.setText(Float.toString(item.getPrecio()));
                precioTotal.setText(Float.toString(item.getPrecio()* item.getCantidad()));
            }
         }

        return view;
	}
	
	
}
