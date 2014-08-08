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
import com.index.facturapp.clasesextra.Factura;

public class AdapterFacturas extends ArrayAdapter<Factura> {
	
	private List<Factura> datos;
	private Context context;
	
	public AdapterFacturas(Context context, List<Factura> values) {
		super(context, R.layout.item_cliente_long, values);
		datos = values;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_factura, parent, false);
        
        Factura item = datos.get(position);
        if(item != null){
        	TextView campoidfactura = (TextView) convertView.findViewById(R.id.idfactura);
        	TextView campocliente = (TextView) convertView.findViewById(R.id.cliente);
        	TextView campoestado = (TextView) convertView.findViewById(R.id.estado);
        	campoidfactura.setText(String.valueOf(item.getNumFact()));
        	if(item.getCliente()!= null){
        		campocliente.setText(item.getCliente().getNombre() + " " + item.getCliente().getApellido1());
        	}
        	else {
        		campocliente.setText("Sin cliente");
        	}
        	campoestado.setText(item.getEstado());
        	Log.e("cargando listFacturas", String.valueOf(item.getNumFact()));
        }
        return convertView;
	}
}
