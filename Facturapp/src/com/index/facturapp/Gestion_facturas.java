package com.index.facturapp;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.index.facturapp.FacturaDB;

public class Gestion_facturas extends ListActivity {
	Bundle bundle;
	private LiniaProducto[] liniaprod;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.gestion_facturas);
		FacturaDB fdb = new FacturaDB(this);
		SQLiteDatabase db = fdb.getWritableDatabase();
		db.close();
		Bundle bundle = getIntent().getExtras();
		setContentGestionFacturas(bundle);
		
	}

	
	
	private void setContentGestionFacturas(Bundle bundle) {
		// TODO Auto-generated method stub
		//Si no es nueva consultar base de datos
			//generar todas las linias segun los productos obtenidos de esa factura
		//sino generar linia vacia
		if (bundle.getBoolean("nuevo")){
			//si cal falta poner el codigo para hacerla vacia
		}
		else{
			FacturaDB fdb = new FacturaDB(this);
			liniaprod = fdb.getLiniasProducto(bundle.getInt("factura"));
			//ListView main = (ListView)findViewById(R.layout.gestion_facturas); //esto puede petar
			Adapter_liniaprod adaptador = new Adapter_liniaprod(this, liniaprod);
			setListAdapter(adaptador);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mgestion_facturas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.anadir) {
			//a–adir linia producto
		}
		return super.onOptionsItemSelected(item);
	}

	

}
