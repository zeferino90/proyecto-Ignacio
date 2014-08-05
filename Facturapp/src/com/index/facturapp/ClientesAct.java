package com.index.facturapp;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.index.facturapp.adapters.Adapter_clientes;
import com.index.facturapp.clasesextra.Cliente;
import com.index.facturapp.dades.FacturaDB;

public class ClientesAct extends ListActivity {

	private Adapter_clientes adapter;
	private ListActivity myactivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacturaDB fdb = new FacturaDB(this);
		myactivity = this;
		List<String> clientes = new ArrayList<String>();
		try{
		clientes = fdb.getClientes();
		}
		catch(Exception e){
			Log.e("dberror", "STACKTRACE");
			Log.e("DBerror", Log.getStackTraceString(e));
		}
		adapter = new Adapter_clientes(this, clientes);
		setListAdapter(adapter);
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				final int pos = position;
				final AlertDialog.Builder dialog = new AlertDialog.Builder(myactivity);
				String[] items = {"Eliminar", "Cancelar"};
				dialog.setTitle("Estas seguro de eliminar " + adapter.getItem(position) + "?");
				dialog.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0){
							String paborrar = adapter.getItem(pos);
							adapter.remove(paborrar);
							FacturaDB fdb = new FacturaDB(myactivity);
							fdb.removeCliente(paborrar);
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
	}
	
	@Override
	protected void onStop() {
		FacturaDB fdb = new FacturaDB(this);
		fdb.close();
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		FacturaDB fdb = new FacturaDB(this);
		fdb.close();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clientes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.anadir3) {
			final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			final View layout = inflater.inflate(R.layout.clientesdialog, null);
			dialog.setView(layout);
			dialog.setPositiveButton("A–adir", new DialogInterface.OnClickListener() {
				
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
					String aux = new String();
		            aux = camponombre.getText().toString();
		            aux = aux.concat(" ");
		            aux = aux.concat(campoape1.getText().toString());
		            aux = aux.concat(" ");
		            aux = aux.concat(campoape2.getText().toString());
					adapter.add(aux);
				}
			});
			dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
