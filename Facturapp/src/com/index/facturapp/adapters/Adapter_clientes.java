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
import com.index.facturapp.clasesextra.Cliente;

public class Adapter_clientes extends ArrayAdapter<Cliente> {

	private Context context;
	private List<Cliente> datos;
	
	public Adapter_clientes(Context context, List<Cliente> values, int id_layout) {
		super(context, id_layout, values);
		datos = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_cliente_long, parent, false);
        
        String item = new String();
        if(datos.get(position).getNombre() != null){
        	item = datos.get(position).getNombre();
            item = item.concat(" ");
            if(datos.get(position).getApellido1() != null){
            	item = item.concat(datos.get(position).getApellido1());
            	item = item.concat(" ");
            	if(datos.get(position).getApellido2() != null){
            		item = item.concat(datos.get(position).getApellido2());
                	item = item.concat(" ");
            	}
            }
        }
        
        
        
        
        if(item != null){
        	TextView text = (TextView) convertView.findViewById(R.id.nombrecliente);
        	text.setText(item);
        	Log.e("cargando listcliente", item);
        }
        return convertView;
	}
}
