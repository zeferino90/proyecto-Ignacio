package com.index.facturapp.adapters;

import java.util.List;

import com.index.facturapp.R;
import com.index.facturapp.clasesextra.LiniaProducto;
import com.index.facturapp.dades.FacturaDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Adapter_liniaprod extends ArrayAdapter<LiniaProducto> {
	private Context context;
	private List<LiniaProducto> datos;
	
	public Adapter_liniaprod(Activity context, List<LiniaProducto> datos){
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

        LiniaProducto item = datos.get(position);
        if (item!= null) {
            
            TextView nomprod = (TextView) view.findViewById(R.id.nombreProd);
            TextView nprod = (TextView) view.findViewById(R.id.nProd);
            TextView precioUni = (TextView) view.findViewById(R.id.precioUni);
            TextView precioTotal = (TextView) view.findViewById(R.id.precioTotal);
            if ( nomprod != null ) {
            	
                // do whatever you want with your string and long
                nomprod.setText(item.getNombre());
                nprod.setText(Integer.toString(item.getCantidad()));
                precioUni.setText(Float.toString(item.getPrecio()));
                precioTotal.setText(Float.toString(item.getPrecio()* item.getCantidad()));
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						final int pos = position;
						final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
						String[] items = {"Editar", "Eliminar", "Cancelar"};
						dialog.setTitle("Opciones");
						dialog.setItems(items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int witch) {
								if(witch == 0){
									final Dialog dialog3 = new Dialog(getContext());
									dialog3.setContentView(R.layout.liniaproductdialog);
									dialog3.setTitle("Escoge tu producto");
									
									FacturaDB fdb = new FacturaDB(getContext());
									final LiniaProducto lprod = adaptador.getItem(pos);
									List<String> categorias = fdb.getCategorias();
									Spinner spincat = (Spinner)dialog3.findViewById(R.id.spincategoria);
									ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categorias); 
									spincat.setAdapter(adapter);
									int poscat = adapter.getPosition(fdb.getProducto(lprod.getNombre()).getCategoria().getCategoria());
									spincat.setSelection(poscat);
									spincat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								        public void onItemSelected(AdapterView<?> parent,
								                android.view.View v, int position, long id) {
								        			FacturaDB fdb = new FacturaDB(getApplicationContext());
								        			List<String> categorias = fdb.getCategorias();
								        			final List<Producto> productos = fdb.getProductoscat(fdb.getCategoria(categorias.get(position)).getId());
								        			Spinner spinprod = (Spinner)dialog3.findViewById(R.id.spinproducto);
								        			int n = productos.size();
								        			//Log.e("dberror", "El numero de productos es:" + n + "y la posicion es: " + position + "Y las categorias son: " + categorias.get(0) + categorias.get(1));
								        			String[] prods = new String[n];
								        			for(int i = 0; i < n; i++){
								        				prods[i] = productos.get(i).getNombre();
								        				Log.e("dberror", productos.get(i).getNombre());
								        			}
								        			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, prods);
								        			spinprod.setAdapter(adapter2);
								        			int posprod = adapter2.getPosition(lprod.getNombre());
								        			spinprod.setSelection(posprod);
								        			spinprod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								        				public void onItemSelected(AdapterView<?> parent,
								        		                android.view.View v, int position, long id){
								        						TextView precio = (TextView)dialog3.findViewById(R.id.precio);
								        						precio.setText(String.valueOf(productos.get(position).getPrecio()) + "Û");
								        						//poner formato a euro correctamente
								        				}
								        				public void onNothingSelected(AdapterView<?> parent) {
								        		            
								        		        }
								        			});
								        		
								        			
								            }
								        public void onNothingSelected(AdapterView<?> parent) {
								            
								        }
									});
									
									Button button = (Button)dialog3.findViewById(R.id.declinar);
						            button.setOnClickListener(new View.OnClickListener(){
						                public void onClick(View declinar) {
						                	dialog3.dismiss();
						                }
						                });
						            Button button2 = (Button)dialog3.findViewById(R.id.confirmar);
						            button2.setOnClickListener(new View.OnClickListener(){
						                public void onClick(View aceptar) {
						                	LiniaProducto prod = new LiniaProducto();
						                	EditText edi = (EditText)dialog3.findViewById(R.id.cantidad);
						                	prod.setCantidad(Integer.parseInt(edi.getText().toString()));
						                	//prod.setFactura(fact.getNumFact());
						                	prod.setFactura(fact.getNumFact());
						                	Spinner spinprod = (Spinner)dialog3.findViewById(R.id.spinproducto);
						                	prod.setNombre(spinprod.getSelectedItem().toString());
						                	TextView preu = (TextView)dialog3.findViewById(R.id.precio);
						                	CharSequence precio = preu.getText();
						                	precio.subSequence(0, precio.length());
						                	prod.setPrecio(Float.parseFloat(precio.subSequence(0, precio.length()-1).toString()));
						                	adaptador.add(prod);
						                	FacturaDB fdb = new FacturaDB(activity);
						                	fdb.createLiniaproducto(prod);
						                	dialog3.dismiss();
						                }
						                });
									dialog3.show();
								}
								else if (witch == 1){
									final AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
									String[] items = {"Si", "Cancelar"};
									dialog2.setTitle("Seguro que desea eliminar?");
									dialog2.setItems(items, new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											if(which == 0){
												LiniaProducto lprod = adaptador.getItem(pos);
												FacturaDB fdb = new FacturaDB(getContext());
												fdb.removeLiniaProducto(lprod);
											}
											else dialog.dismiss();
										}
									});
								}
								else dialog.dismiss();
							}
							
						});
						return false;
					}
				});
            }
         }

        return view;
	}
	public List<LiniaProducto> getLiniasProducto(){
		return this.datos;
	}
	
	
}
