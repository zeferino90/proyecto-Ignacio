package com.index.facturapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.index.facturapp.FacturaDB;
import com.index.facturapp.clasesextra.Categoria;
import com.index.facturapp.clasesextra.Factura;
import com.index.facturapp.clasesextra.LiniaProducto;
import com.index.facturapp.clasesextra.Producto;

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
			liniaprod = fdb.getLiniasProducto((Factura) getIntent().getSerializableExtra("factura"));
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
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.productdialog);
			dialog.setTitle("Escoge tu producto");
			
			FacturaDB fdb = new FacturaDB(this);
			String[] categorias = fdb.getCategorias();
			Spinner spincat = (Spinner)dialog.findViewById(R.id.spincategoria);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias); 
			spincat.setAdapter(adapter);
			
			spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent,
		                android.view.View v, int position, long id) {
		        			FacturaDB fdb = new FacturaDB(getApplicationContext());
		        			String[] categorias = fdb.getCategorias();
		        			Producto[] productos = fdb.getProductoscat(categorias[position]);
		        			Spinner spinprod = (Spinner)dialog.findViewById(R.id.spinproducto);
		        			int n = productos.length;
		        			String[] prods = new String[n];
		        			for(int i = 0; i < n; i++){
		        				prods[i] = productos[i].getNombre();
		        			}
		        			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, prods);
		        			spinprod.setAdapter(adapter2);
		        			
		            }
		        public void onNothingSelected(AdapterView<?> parent) {
		            
		        }
			});
			
			Button button = (Button)dialog.findViewById(R.id.declinar);
            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View declinar) {
                	dialog.dismiss();
                }
                });
            Button button2 = (Button)dialog.findViewById(R.id.confirmar);
            button2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View declinar) {
                	dialog.dismiss();
                }
                });
			
			Log.e("dialog", "Llego al dialog");
			dialog.show();
			Log.e("dialog", "show dialog");
			
            
		}
		return super.onOptionsItemSelected(item);
	}

	

}
