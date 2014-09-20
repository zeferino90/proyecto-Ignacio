package com.index.facturapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
	private boolean factura;
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
	
	@Override
	protected void onResume() {
		FacturaDB fdb = new FacturaDB(this);
		fact = fdb.getFactura(fact.getNumFact());
		super.onResume();
	}

	private void setContentGestionFacturas() {
			Log.e("chivato lp", "factura no nueva");
			FacturaDB fdb = new FacturaDB(this);
			List<LiniaProducto> liniaprod = fdb.getLiniasProducto(fact);
			adaptador = new Adapter_liniaprod(this, liniaprod, fact);
			Log.e("chivato lp", "factura no nueva2");
			setListAdapter(adaptador);
			Log.e("chivato lp", "factura no nueva3");
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
		        						NumberFormat nformat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
		        						precio.setText(nformat.format(productos.get(position).getPrecio()));
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
                	NumberFormat nformat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
                	try {
						prod.setPrecio(nformat.parse(precio.toString()).floatValue());
					} catch (ParseException e) {
						e.printStackTrace();
					}
                	FacturaDB fdb = new FacturaDB(activity);
                	if(fdb.existsLiniaProducto(prod)){
                		Toast.makeText(activity, "Modifica el producto ya existente en la factura", Toast.LENGTH_LONG).show();
                	}
                	else{
	                	adaptador.add(prod);
	                	fdb.createLiniaproducto(prod);
	                	dialog.dismiss();
                	}
                	
                }
                });
			dialog.show();
		}
		else if(id == R.id.exportar){
			//Habra que preguntar si por dropbox o por mail de momento por mail
			final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			String[] items = {"Factura", "Pressupuesto"};
			dialog.setTitle("Escoge el formato");
			dialog.setItems(items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int which) {
					// create a new document
					PdfDocument document = new PdfDocument();
					boolean iva = true;
					if(which == 0){
						iva = true;
					}
					else iva = false;
					// crate a page description
					 int pageWidth = 595;
					 int pageHeigth = 842;
					 int pagenumber = 0;
					 int actualpage = 0;
					 PageInfo pageInfo = new PageInfo.Builder(pageWidth, pageHeigth, pagenumber).create();
					 // start a page
					 //Page page = document.startPage(pageInfo);
					 List<Page> pages = new ArrayList<Page>();
					 pages.add(actualpage, document.startPage(pageInfo));
					 // -----------------draw on the page------------------------------------
					 Canvas canvas = pages.get(actualpage).getCanvas();
					 Paint paint = new Paint();
					 paint.setTextSize(12);
					 float coordX = 0;//horizontal
					 float coordY = 0;//vertical
					 //Color color = new Color();
					 if(iva){
						 //cabeceraIgnacio+numfact
						 int col = Color.parseColor("#fefccd");
						 //canvas.drawText("prueba", coordX, coordY, paint);
						 int nfact = fact.getNumFact();
						 int n = nxifres(nfact);
						 coordY = 13;
						 coordX = 590 - (n+10*5);
						 canvas.drawText("Nº Fact: " + String.valueOf(nfact), coordX, coordY, paint);
						 paint.setStyle(Paint.Style.FILL);
						 paint.setColor(col);
						 canvas.drawRect(1, 1, 400, 30, paint);
						 paint.setStyle(Paint.Style.STROKE);
						 paint.setColor(Color.BLACK);
						 canvas.drawRect(1, 1, 400, 30, paint);
						 coordX = 100;
						 coordY = 13;
						 canvas.drawText("REFORMAS INTEGRALES", coordX, coordY, paint);
						 coordY = 23;
						 canvas.drawText("IGNACIO MACÍA NIF 38080988-A", coordX, coordY, paint);
						 coordY = 50;
						 paint.setTextSize(10);
						 canvas.drawText("Teléfono 637237412", coordX, coordY, paint);
						 coordX = 500;
						 paint.setColor(Color.RED);
						 canvas.drawText("PRESUPUESTO", coordX, coordY, paint);
						 //---------fin cabecera datos ignacio-------
					 }
					 //-----Pintar linias superiores i bloque cliente-------
					 Paint paint2 = new Paint();
					 paint.setColor(Color.BLACK);
					 coordY = 85;
					 coordX = 3;
					 paint2.setTextSize(10);
					 canvas.drawText("Cliente", coordX, coordY, paint2);
					 coordY = 50;
					 coordX = 500;
					 canvas.drawLine(0, coordY+2, coordX-2, coordY+2, paint);
					 canvas.drawLine(coordX-2, coordY+2, coordX-2, coordY-11, paint);
					 canvas.drawLine(coordX-2, coordY-11, coordX+96, coordY-11, paint);
					 coordY = 75;
					 canvas.drawLine(0, coordY+12, 95, coordY+12, paint);
					 canvas.drawLine(95, coordY+12, 95, coordY, paint);
					 canvas.drawLine(95, coordY, 95+300, coordY, paint);
					 canvas.drawLine(405, coordY+12, 497, coordY+12, paint);
					 canvas.drawLine(497, coordY+12, 497, coordY, paint);
					 canvas.drawLine(497, coordY, 595, coordY, paint);
					 coordX = 407;
					 coordY = 97;
					 canvas.drawText("Fecha", coordX, coordY, paint2);
					 coordX = 500;
					 SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
					 canvas.drawText(sdf.format(fact.getData()), coordX, coordY, paint2);
					 coordX = 3;
					 canvas.drawText("Nombre", coordX, coordY, paint2);
					 coordY = 107;
					 canvas.drawText("Dirección", coordX, coordY, paint2);
					 coordY = 117;
					 canvas.drawText("Localidad", coordX, coordY, paint2);
					 coordY = 127;
					 canvas.drawText("Teléfono", coordX, coordY, paint2);
					 coordX = 95;
					 coordY = 97;
					 if(fact.getCliente() != null){
						 String aux = fact.getCliente().getNombre();
						 aux = aux.concat(" ");
						 aux = aux.concat(fact.getCliente().getApellido1());
						 aux = aux.concat(" ");
						 aux = aux.concat(fact.getCliente().getApellido2());
						 canvas.drawText(aux, coordX, coordY, paint2);
						 coordY = 107;
						 canvas.drawText(fact.getCliente().getDir(), coordX, coordY, paint2);
						 coordY = 117;
						 canvas.drawText(fact.getCliente().getLocalidad(), coordX, coordY, paint2);
						 coordY = 127;
						 canvas.drawText("campotel", coordX, coordY, paint2);
					 }
					 //-----fin bloque cliente mas linia superior--------
					 //------Primera linia superior a las linias de producto------
					 coordY = 137;
					 paint.setStyle(Paint.Style.STROKE);
					 paint.setPathEffect(new DashPathEffect(new float[]{1, 2}, 0));
					 canvas.drawLine(0, coordY, 595, coordY, paint);
					 coordY = 149;
					 canvas.drawLine(0, coordY, 595, coordY, paint);
					 coordX = 25;
					 canvas.drawText("Cantidad", coordX, coordY-2, paint2);
					 coordX = 95;
					 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
					 coordX = 235;
					 canvas.drawText("Descripción", coordX, coordY-2, paint2);
					 coordX = 405;
					 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
					 coordX = 415;
					 canvas.drawText("Precio unitario", coordX, coordY-2, paint2);
					 coordX = 500;
					 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
					 coordX = 535;
					 canvas.drawText("TOTAL", coordX, coordY-2, paint2);
					 //---------Generacion de linias producto en la factura
					 
					 int i = 0;
					 float subtotal = 0;
					 List<LiniaProducto> lprod = adaptador.getLiniasProducto();
					 int nlprod = lprod.size();
					 while (i < nlprod){
						 if(coordY > 740){
							// finish the page
							 document.finishPage(pages.get(actualpage));
							 actualpage++;
							 pages.add(actualpage, document.startPage(pageInfo));
							 canvas = pages.get(actualpage).getCanvas();
							 coordY = 40;
							 canvas.drawLine(0, coordY, 595, coordY, paint);
							 coordY += 12;
							 canvas.drawLine(0, coordY, 595, coordY, paint);
							 coordX = 25;
							 canvas.drawText("Cantidad", coordX, coordY-2, paint2);
							 coordX = 95;
							 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
							 coordX = 235;
							 canvas.drawText("Descripción", coordX, coordY-2, paint2);
							 coordX = 405;
							 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
							 coordX = 415;
							 canvas.drawText("Precio unitario", coordX, coordY-2, paint2);
							 coordX = 500;
							 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
							 coordX = 535;
							 canvas.drawText("TOTAL", coordX, coordY-2, paint2);
						 } 
						 coordY += 12;
						 canvas.drawLine(0, coordY, 595, coordY, paint);
						 coordX = 95;
						 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
						 coordX = 405;
						 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
						 coordX = 500;
						 canvas.drawLine(coordX, coordY, coordX, coordY-12, paint);
						 coordX = 25;
						 int cant = lprod.get(i).getCantidad();
						 canvas.drawText(String.valueOf(cant), coordX, coordY-2, paint2);
						 coordX = 97;
						 canvas.drawText(lprod.get(i).getNombre(), coordX, coordY-2, paint2);
						 coordX = 415;
						 NumberFormat nformat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
						 float precio = lprod.get(i).getPrecio();
						 canvas.drawText(nformat.format(precio), coordX, coordY-2, paint2);
						 coordX = 535;
						 float lprodtotal = cant * precio;
						 
						 subtotal += lprodtotal;
						 canvas.drawText(nformat.format(lprodtotal), coordX, coordY-2, paint2);
						 i++;
					 }
					 //------fin bloque generacion linia producto------
					 //-----Bloque notas------------------
					 coordY += 12 *4;
					 
					 if(coordY > 600)
					 coordY = 741;
					 else {
						 canvas.drawLine(0, coordY, 595, coordY, paint);
						 coordY += 12;
						 canvas.drawLine(0, coordY, 595, coordY, paint);
						 coordX = 25;
						 canvas.drawText("Descripción del trabajo a realizar", coordX, coordY-2, paint2);
						 coordY +=12;
					 }
					 
					 String nota = fact.getNotas();
					 String[] saltos = nota.split("\n");
					 int indsaltos = 0;
					 while(indsaltos < saltos.length){
						 int j = 0;
						 //String nota = fact.getNotas();
						 int maxj = saltos[indsaltos].length();
						 String[] palabras = saltos[indsaltos].split(" ");
						 int indexpal = 0;
						 int index = 0;
						 while(j < maxj){
							 if(coordY > 740){
									// finish the page
									 document.finishPage(pages.get(actualpage));
									 actualpage++;
									 pages.add(actualpage, document.startPage(pageInfo));
									 canvas = pages.get(actualpage).getCanvas();
									 coordY = 40;
									 canvas.drawLine(0, coordY, 595, coordY, paint);
									 coordY += 12;
									 canvas.drawLine(0, coordY, 595, coordY, paint);
									 coordX = 25;
									 canvas.drawText("Descripción del trabajo a realizar", coordX, coordY-2, paint2);
									 coordY +=12;
								 }
							 int n = j;
							 int n2 = 0;
							 float stringwidth = 0;
							 while(stringwidth < pageWidth-75 && indexpal < palabras.length){
								 String aux = palabras[indexpal];
								 index = saltos[indsaltos].indexOf(aux) + aux.length();
								 if(index > j) {
									 String substring = saltos[indsaltos].substring(j, index);
									 n2 = substring.length();
									 if(n2 < pageWidth){
										 if(n2 < n){
											 n += aux.length() + 1;
										 }
										 else n = n2;
									 }
								 }
								 else n += aux.length() + 1;
								 stringwidth = paint2.measureText(saltos[indsaltos].substring(j, n));
								 indexpal++;
							 }
							 canvas.drawText(saltos[indsaltos].substring(j, n), coordX, coordY, paint2);
							 j = index;
							 coordY +=12;
						 }
						 indsaltos++;
					 }
					 
					 //-----fin bloque notas--------------
					 
					 /*coordY = 741;
					 int j = 0;
					 String nota = fact.getNotas();
					 int maxj = nota.length();
					 String[] palabras = nota.split(" ");
					 int indexpal = 0;
					 int index = 0;
					 while(j < maxj){
						 if(coordY > 740){
								// finish the page
								 document.finishPage(pages.get(actualpage));
								 actualpage++;
								 pages.add(actualpage, document.startPage(pageInfo));
								 canvas = pages.get(actualpage).getCanvas();
								 coordY = 40;
								 canvas.drawLine(0, coordY, 595, coordY, paint);
								 coordY += 12;
								 canvas.drawLine(0, coordY, 595, coordY, paint);
								 coordX = 25;
								 canvas.drawText("Descripción del Trabajo a realizar", coordX, coordY-2, paint2);
								 coordY +=12;
							 }
						 int n = j;
						 int n2 = 0;
						 float stringwidth = 0;
						 while(stringwidth < pageWidth-75 && indexpal < palabras.length){
							 String aux = palabras[indexpal];
							 index = nota.indexOf(aux) + aux.length();
							 if(index > j) {
								 String substring = nota.substring(j, index);
								 n2 = substring.length();
								 if(n2 < pageWidth){
									 if(n2 < n){
										 n += aux.length() + 1;
									 }
									 else n = n2;
								 }
							 }
							 else n += aux.length() + 1;
							 stringwidth = paint2.measureText(nota.substring(j, n));
							 indexpal++;
						 }
						 canvas.drawText(nota.substring(j, n), coordX, coordY, paint2);
						 j = index;
						 coordY +=12;
					 }
					 //-----fin bloque notas--------------
*/					 coordY = 740 + 12;
					 coordX = 415;
					 canvas.drawText("Subtotal", coordX, coordY-2, paint2);
					 coordX = 535;
					 NumberFormat nformat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
					 canvas.drawText(nformat.format(subtotal), coordX, coordY-2, paint2);
					 coordY +=12;
					 Paint paint3 = new Paint();
					 paint3.setStyle(Paint.Style.STROKE);
					 paint3.setColor(Color.BLACK);
					 canvas.drawLine(0, coordY+12, 95, coordY+12, paint3);
					 canvas.drawLine(95, coordY+12, 95, coordY, paint3);
					 canvas.drawLine(95, coordY, 295, coordY, paint3);
					 if(iva){
						 coordY += 12;
						 coordX = 415;
						 FacturaDB fdb = new FacturaDB(activity);
						 int niva = fdb.getIVA();
						 canvas.drawText("IVA " + String.valueOf(niva) + "%", coordX, coordY, paint2);
						 coordX = 535;
						 float precioIVA = (subtotal*niva)/ 100;
						 canvas.drawText(nformat.format(precioIVA), coordX, coordY, paint2);
						 coordY += 12;
						 coordX = 415;
						 canvas.drawText("TOTAL", coordX, coordY, paint2);
						 coordX = 535;
						 subtotal += precioIVA;
						 canvas.drawText(nformat.format(subtotal), coordX, coordY, paint2);
					 }
					 else{
						 coordY += 12;
						 coordX = 130;
						 canvas.drawText("IVA NO INCLUIDO", coordX, coordY, paint2);
						 coordY += 12;
						 coordX = 415;
						 canvas.drawText("TOTAL", coordX, coordY, paint2);
						 coordX = 535;
						 canvas.drawText(nformat.format(subtotal), coordX, coordY, paint2);
					 }
					 
					 // finish the page
					 document.finishPage(pages.get(actualpage));
					 File pdffile;
					 if(isExternalStorageWritable() && isExternalStorageReadable()){
						 pdffile = new File(activity.getExternalFilesDir(null), "pdfprueva_ext.pdf");
						 Toast.makeText(activity, "Guardado en SDcard", Toast.LENGTH_SHORT).show();
					 }
					 else {
						 pdffile = new File(activity.getFilesDir(), "pdfprueva.pdf");
						 Toast.makeText(activity, "Guardado en Interno", Toast.LENGTH_SHORT).show();
					 }
					// write the document content
					 FileOutputStream outputstream;
					try {
						outputstream = new FileOutputStream(pdffile);
						document.writeTo(outputstream);
						outputstream.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					 
					 String[] to = {"mperesp1990@gmail.com"};
					 String[] cc = {}; 
					 enviar(to, cc, "prueva android", "contrato notario", pdffile);
					 Toast.makeText(activity, "Enviando el mail", Toast.LENGTH_SHORT).show();
					 
					 //close the document
					 document.close();
				}
			});
			dialog.show();
		}
		else if(id == R.id.Explicacion){
			Intent intent = new Intent(activity, Notas.class);
			intent.putExtra("idfactura", fact.getNumFact());
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	private void enviar(String[] to, String[] cc, String asunto, String mensaje, File file) {
	        Intent emailIntent = new Intent(Intent.ACTION_SEND);
	        emailIntent.setData(Uri.parse("mailto:"));
	        //String[] to = direccionesEmail;
	        //String[] cc = copias;
	        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
	        if (cc!= null)
	        	emailIntent.putExtra(Intent.EXTRA_CC, cc);
	        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
	        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
	        emailIntent.setType("message/rfc822");
	        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	        startActivity(Intent.createChooser(emailIntent, "Email "));
	}
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	private int nxifres (int x){
		int aux = x;
		int i=0;
		while(aux != 0){
			aux = aux/10;
			i++;
		}
		return i;
	}

}
