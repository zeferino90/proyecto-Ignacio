package com.index.facturapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.index.facturapp.dades.FacturaDB;
import com.index.facturapp.clasesextra.Categoria;
import com.index.facturapp.clasesextra.Factura;
import com.index.facturapp.clasesextra.LiniaProducto;
import com.index.facturapp.clasesextra.Producto;
import com.index.facturapp.adapters.*;

public class Gestion_facturas extends ListActivity {
	//Bundle bundle;
	private Factura fact;
	private Adapter_liniaprod adaptador;
	private boolean nuevo;
	private ListActivity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.gestion_facturas);
		Bundle bundle = getIntent().getExtras();
		FacturaDB fdb = new FacturaDB(this);
		fact = fdb.getFactura(bundle.getInt("idfactura"));
		nuevo = bundle.getBoolean("nuevo");
		activity = this;
		
		setContentGestionFacturas();
		
		
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


	private void setContentGestionFacturas() {
		//Si no es nueva consultar base de datos
			//generar todas las linias segun los productos obtenidos de esa factura
		//sino generar linia vacia
		if (nuevo){
			//nuevo = true;
			//si cal falta poner el codigo para hacerla vacia
			List<LiniaProducto> liniaprod = new ArrayList<LiniaProducto>();
			adaptador = new Adapter_liniaprod(this, liniaprod);
    		setListAdapter(adaptador);
		}
		else{
			Log.e("chivato lp", "he llegado");
			FacturaDB fdb = new FacturaDB(this);
			List<LiniaProducto> liniaprod = fdb.getLiniasProducto(fact);
			//ListView main = (ListView)findViewById(R.layout.gestion_facturas); //esto puede petar
			adaptador = new Adapter_liniaprod(this, liniaprod);
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
			dialog.setContentView(R.layout.liniaproductdialog);
			dialog.setTitle("Escoge tu producto");
			
			FacturaDB fdb = new FacturaDB(this);
			List<String> categorias = fdb.getCategorias();
			Spinner spincat = (Spinner)dialog.findViewById(R.id.spincategoria);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias); 
			spincat.setAdapter(adapter);
			spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent,
		                android.view.View v, int position, long id) {
		        			FacturaDB fdb = new FacturaDB(getApplicationContext());
		        			List<String> categorias = fdb.getCategorias();
		        			final List<Producto> productos = fdb.getProductoscat(fdb.getCategoria(categorias.get(position)).getId());
		        			Spinner spinprod = (Spinner)dialog.findViewById(R.id.spinproducto);
		        			int n = productos.size();
		        			//Log.e("dberror", "El numero de productos es:" + n + "y la posicion es: " + position + "Y las categorias son: " + categorias.get(0) + categorias.get(1));
		        			String[] prods = new String[n];
		        			for(int i = 0; i < n; i++){
		        				prods[i] = productos.get(i).getNombre();
		        				Log.e("dberror", productos.get(i).getNombre());
		        			}
		        			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, prods);
		        			spinprod.setAdapter(adapter2);
		        			spinprod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        				public void onItemSelected(AdapterView<?> parent,
		        		                android.view.View v, int position, long id){
		        						TextView precio = (TextView)dialog.findViewById(R.id.precio);
		        						precio.setText(String.valueOf(productos.get(position).getPrecio()) + "�");
		        						//poner formato a euro correctamente
		        				}
		        				public void onNothingSelected(AdapterView<?> parent) {
		        		            
		        		        }
		        			});
		        		
		        			
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
                public void onClick(View aceptar) {
                	LiniaProducto prod = new LiniaProducto();
                	EditText edi = (EditText)dialog.findViewById(R.id.cantidad);
                	prod.setCantidad(Integer.parseInt(edi.getText().toString()));
                	//prod.setFactura(fact.getNumFact());
                	prod.setFactura(fact.getNumFact());
                	Spinner spinprod = (Spinner)dialog.findViewById(R.id.spinproducto);
                	prod.setNombre(spinprod.getSelectedItem().toString());
                	TextView preu = (TextView)dialog.findViewById(R.id.precio);
                	CharSequence precio = preu.getText();
                	precio.subSequence(0, precio.length());
                	prod.setPrecio(Float.parseFloat(precio.subSequence(0, precio.length()-1).toString()));
                	adaptador.add(prod);
                	FacturaDB fdb = new FacturaDB(activity);
                	fdb.createLiniaproducto(prod);
                	dialog.dismiss();
                }
                });
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	

}
