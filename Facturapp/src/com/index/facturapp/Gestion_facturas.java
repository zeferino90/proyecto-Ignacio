package com.index.facturapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.index.facturapp.adapters.Adapter_liniaprod;
import com.index.facturapp.clasesextra.Factura;
import com.index.facturapp.clasesextra.LiniaProducto;
import com.index.facturapp.clasesextra.Producto;
import com.index.facturapp.dades.FacturaDB;

public class Gestion_facturas extends ListActivity {
	//Bundle bundle;
	private Factura fact;
	private Adapter_liniaprod adaptador;
	//private boolean nuevo;
	private ListActivity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.gestion_facturas);
		Bundle bundle = getIntent().getExtras();
		FacturaDB fdb = new FacturaDB(this);
		fact = fdb.getFactura(bundle.getInt("idfactura"));
		
		Log.e("chivato", "recuperando factura: " + String.valueOf(fact.getNumFact())+ "Pero hemos pedido factura: " + String.valueOf(bundle.getInt("idfactura")));
		//nuevo = bundle.getBoolean("nuevo");
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
			Log.e("chivato lp", "factura no nueva");
			FacturaDB fdb = new FacturaDB(this);
			List<LiniaProducto> liniaprod = fdb.getLiniasProducto(fact);
			adaptador = new Adapter_liniaprod(this, liniaprod, fact);
			Log.e("chivato lp", "factura no nueva2");
			setListAdapter(adaptador);
			Log.e("chivato lp", "factura no nueva3");
    		/*getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					final int pos = position;
					final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseContext());
					String[] items = {"Editar", "Eliminar", "Cancelar"};
					dialog.setTitle("Opciones");
					dialog.setItems(items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int witch) {
							if(witch == 0){
								final Dialog dialog3 = new Dialog(getBaseContext());
								dialog3.setContentView(R.layout.liniaproductdialog);
								dialog3.setTitle("Escoge tu producto");
								
								FacturaDB fdb = new FacturaDB(getBaseContext());
								final LiniaProducto lprod = adaptador.getItem(pos);
								List<String> categorias = fdb.getCategorias();
								Spinner spincat = (Spinner)dialog3.findViewById(R.id.spincategoria);
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, categorias); 
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
								final AlertDialog.Builder dialog2 = new AlertDialog.Builder(getBaseContext());
								String[] items = {"Si", "Cancelar"};
								dialog2.setTitle("Seguro que desea eliminar?");
								dialog2.setItems(items, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if(which == 0){
											LiniaProducto lprod = adaptador.getItem(pos);
											FacturaDB fdb = new FacturaDB(getBaseContext());
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
		*///}
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
		else if(id == R.id.exportar){
			//Habra que preguntar si por dropbox o por mail de momento por mail
			// create a new document
			 PdfDocument document = new PdfDocument();
			 
			 // crate a page description
			 int pageWidth = 595;
			 int pageHeigth = 842;
			 int pagenumber = 0;
			 PageInfo pageInfo = new PageInfo.Builder(pageWidth, pageHeigth, pagenumber).create();
			 // start a page
			 Page page = document.startPage(pageInfo);
			 // draw something on the page
			 View content = LayoutInflater.from(this).inflate(R.layout.plantilla_factura, null);
			 TextView prueva = (TextView) content.findViewById(R.id.prueva);
			 prueva.setText("Espero que funciones cabron");
			 content.draw(page.getCanvas());
			 
			 // finish the page
			 document.finishPage(page);
			 
			 // add more pages
			 File pdffile = new File(this.getFilesDir(), "pdfprueva");
			 
			 // write the document content
			 try {
				document.writeTo(new FileOutputStream(pdffile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
			 //close the document
			 document.close();
			 String[] to = {"mperesp1990@gmail.com"};
			 String[] cc = {"mpe1990@msn.com"}; 
			 enviar(to, cc, "prueva android", "Espero que leas esto por tu bien", pdffile);
			 Toast.makeText(this, "Enviando el mail", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}

	private void enviar(String[] to, String[] cc, String asunto, String mensaje, File file) {
	        Intent emailIntent = new Intent(Intent.ACTION_SEND);
	        emailIntent.setData(Uri.parse("mailto:"));
	        //String[] to = direccionesEmail;
	        //String[] cc = copias;
	        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
	        emailIntent.putExtra(Intent.EXTRA_CC, cc);
	        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
	        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
	        emailIntent.setType("message/rfc822");
	        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	        startActivity(Intent.createChooser(emailIntent, "Email "));
	    }

}
