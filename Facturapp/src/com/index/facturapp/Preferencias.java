package com.index.facturapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Preferencias extends Activity {

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
	}
	
}
