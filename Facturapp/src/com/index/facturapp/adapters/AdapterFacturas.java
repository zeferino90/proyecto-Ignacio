package com.index.facturapp.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.index.facturapp.Gestion_facturas;
import com.index.facturapp.R;
import com.index.facturapp.clasesextra.Factura;
import com.index.facturapp.dades.FacturaDB;

public class AdapterFacturas extends ArrayAdapter<Factura> {
	
	private List<Factura> datos;
	private Context context;
	private Activity myactivity;
	
	public AdapterFacturas(Activity  activity, Context context, List<Factura> values) {
		super(context, R.layout.item_cliente_long, values);
		datos = values;
		myactivity = activity;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_factura, parent, false);
        final int pos = position;
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
	        convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.e("chivato", "el toque ha llegado al de la posicion: " + String.valueOf(pos));
					Intent intent = new Intent(myactivity, Gestion_facturas.class);
					TextView campoidfactura = (TextView) v.findViewById(R.id.idfactura);
					intent.putExtra("idfactura", Integer.valueOf(campoidfactura.getText().toString()));
					Log.e("chivato", "Num factura: " + campoidfactura.getText().toString());
					//intent.putExtra("nuevo", false);
					FacturaDB fdb = new FacturaDB(myactivity);
					fdb.close();
					v.getContext().startActivity(intent);
					
				}
			});
	        convertView.setTag(getItemId(pos));
        }
        return convertView;
	}
}
