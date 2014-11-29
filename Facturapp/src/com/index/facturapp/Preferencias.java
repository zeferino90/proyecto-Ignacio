package com.index.facturapp;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.index.facturapp.dades.FacturaDB;

public class Preferencias extends Activity {

	private int IVA;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferencias);
		
		TextView catprod = (TextView) findViewById(R.id.cateprod);
		catprod.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(v.getContext(), Managecatprod.class);
				startActivity(intent);
			}
		});
		TextView clientes = (TextView) findViewById(R.id.clietes);
		clientes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(v.getContext(), ClientesAct.class);
				startActivity(intent);
			}
		});
		
		EditText iva = (EditText) findViewById(R.id.nIVA);
		final FacturaDB fdb = new FacturaDB(this);
		IVA = fdb.getIVA();
		iva.setText(String.valueOf(IVA));
		iva.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int nIVA = Integer.valueOf(v.getText().toString());
				fdb.setIVA(nIVA, IVA);
				IVA = nIVA;
				Toast.makeText(getApplicationContext(), "IVA actualizado", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		
		TextView cache = (TextView)findViewById(R.id.cache);
		cache.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File[] files = getFilesDir().listFiles();
				for(File file : files){
					file.delete();
				}
				Toast.makeText(getBaseContext(), "Se han limpiado los ficheros", Toast.LENGTH_SHORT).show();
			}
		});
		
		TextView copia = (TextView)findViewById(R.id.copia);
		copia.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FacturaDB fdb = new FacturaDB(getApplicationContext());
				String FileName = null;
				try {
					FileName = fdb.backupDatabase(getApplicationContext());
				} catch (IOException e) {
					e.printStackTrace();
				}
		        File dbFile = new File(FileName);
		        //String[] to = {"reformasimacia@hotmail.es"};
		        String[] to = {"mlespinosa1960@gmail.com"};
		        String[] cc = {};
				enviar(to, cc, "Copia de seguridad datos Facturapp", "", dbFile);
			}
		});
		
		TextView importar = (TextView) findViewById(R.id.importar);
		importar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FacturaDB fdb = new FacturaDB(getApplicationContext());
				File descargas = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/");
				File files[] = descargas.listFiles();
				String dbPath = new String();
				int j = 0;
				for(int i = 0; i < files.length; i++){
					if (files[i].getName().equals("DBFacturapp.factDB")){
						dbPath = files[i].getPath();
						j = i;
					}
				}
				try {
					fdb.importDatabase(dbPath, getApplicationContext());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				files[j].delete();
				Toast.makeText(getApplicationContext(), "Datos importados correctamente", Toast.LENGTH_SHORT).show();
			}
		});
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
}
