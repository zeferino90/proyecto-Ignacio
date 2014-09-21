package com.index.facturapp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
	}
	
}
