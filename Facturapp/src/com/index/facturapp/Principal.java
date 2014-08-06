package com.index.facturapp;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.index.facturapp.adapters.AdapterFacturas;
import com.index.facturapp.clasesextra.Cliente;
import com.index.facturapp.clasesextra.Factura;
import com.index.facturapp.dades.FacturaDB;

public class Principal extends ListActivity {

	private ListActivity myactivity;
	private AdapterFacturas adapter;
	private int numfact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		FacturaDB fdb = new FacturaDB(this);
		myactivity = this;
		List<Factura> facturas = new ArrayList<Factura>();
		try{
		facturas = fdb.getFacturas();
		}
		catch(Exception e){
			Log.e("dberror", "STACKTRACE");
			Log.e("DBerror", Log.getStackTraceString(e));
		}
		this.getNumfact(facturas);
		adapter = new AdapterFacturas(this, facturas);
		setListAdapter(adapter);
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				final int pos = position;
				final AlertDialog.Builder dialog = new AlertDialog.Builder(myactivity);
				String[] items = {"A–adir cliente", "Eliminar", "Cancelar"};
				dialog.setTitle("Estas seguro de eliminar " + adapter.getItem(position) + "?");
				dialog.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0){
							final AlertDialog.Builder dialog3 = new AlertDialog.Builder(myactivity);
							LayoutInflater inflater = myactivity.getLayoutInflater();
							final View layout = inflater.inflate(R.layout.clientesdialog, null);
							dialog3.setView(layout);
							dialog3.setPositiveButton("A–adir", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Cliente cliente = new Cliente();
									EditText campodni = (EditText) layout.findViewById(R.id.dni);
									EditText camponombre = (EditText) layout.findViewById(R.id.nombre);
									EditText campoape1 = (EditText) layout.findViewById(R.id.apellido1);
									EditText campoape2 = (EditText) layout.findViewById(R.id.apellido2);
									EditText campodir = (EditText) layout.findViewById(R.id.direccion);
									EditText campoloc = (EditText) layout.findViewById(R.id.localidad);
									cliente.setDni(campodni.getText().toString());
									cliente.setNombre(camponombre.getText().toString());
									cliente.setApellido1(campoape1.getText().toString());
									cliente.setApellido2(campoape2.getText().toString());
									cliente.setDir(campodir.getText().toString());
									cliente.setLocalidad(campoloc.getText().toString());
									FacturaDB fdb = new FacturaDB(getBaseContext());
									fdb.createCliente(cliente);
									adapter.getItem(pos).setCliente(cliente);
									adapter.notifyDataSetChanged();
								}
							});
							dialog3.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							dialog3.show();
						}
						else if (which == 1){
							final AlertDialog.Builder dialog2 = new AlertDialog.Builder(myactivity);
							String[] items = {"Si", "Cancelar"};
							dialog2.setTitle("Estas seguro de eliminar " + adapter.getItem(pos) + "?");
							dialog2.setItems(items, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (which == 0){
										Factura paborrar = adapter.getItem(pos);
										adapter.remove(paborrar);
										FacturaDB fdb = new FacturaDB(myactivity);
										fdb.removeFactura(paborrar);
									}
									else {
										dialog.dismiss();
									}
								}
							});
							try{
							dialog2.show();
							}
							catch(Exception e){
								Log.e("dialogerror", "STACKTRACE");
								Log.e("dialogerror", Log.getStackTraceString(e));
							}
						}
						else {
							dialog.dismiss();
						}
					}
				});
				try{
				dialog.show();
				}
				catch(Exception e){
					Log.e("dialogerror", "STACKTRACE");
					Log.e("dialogerror", Log.getStackTraceString(e));
				}
				return true;
			}
			
		});
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent intent = new Intent(myactivity, Gestion_facturas.class);
				intent.putExtra("factura", (Parcelable)adapter.getItem(position));
				startActivity(intent);
			}
		});

	}
	
	private int getNumfact(List<Factura> facturas) {
		int n = facturas.size();
		int aux = 0;
		for (int i = 0; i < n; i++){
			if (aux < facturas.get(i).getNumFact()){
				aux = facturas.get(i).getNumFact();
			}
		}
		return aux+1;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.anadir) {
			final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			final View layout = inflater.inflate(R.layout.facturadialog, null);
			final EditText campofact = (EditText) layout.findViewById(R.id.numfact);
			campofact.setText(String.valueOf(this.numfact));
			dialog.setView(layout);			
			dialog.setPositiveButton("A–adir", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Factura factura = new Factura();
					factura.setNumFact(Integer.valueOf(campofact.getText().toString()));
					factura.setEstado("En progreso"); 
					Date date = new Date();
					factura.setData(date);
					FacturaDB fdb = new FacturaDB(myactivity);
					fdb.createFactura(factura);
					Intent intent = new Intent(myactivity, Gestion_facturas.class);
					intent.putExtra("nuevo", true);
					intent.putExtra("factura", (Parcelable)factura);
					startActivity(intent);
				}

				
			});
			dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.show();
			//intent.putExtra("factura", factura); esto es para cuando no sea una nueva sino una de las que aprete del listado
			return true;
		}
		else if (id == R.id.action_settings){
			Intent intent = new Intent(this, Preferencias.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
