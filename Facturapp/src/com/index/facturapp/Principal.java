package com.index.facturapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Principal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		
		getFacturas();
	}

	private void getFacturas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.anadir) {
			Intent intent = new Intent(this, Gestion_facturas.class);
			intent.putExtra("nuevo", true);
			//intent.putExtra("factura", factura); esto es para cuando no sea una nueva sino una de las que aprete del listado
		    startActivity(intent);
			return true;
		}
		else if (id == R.id.action_settings){
			Intent intent = new Intent(this, Preferencias.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
