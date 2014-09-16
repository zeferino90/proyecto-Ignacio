package com.index.facturapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.index.facturapp.clasesextra.Factura;
import com.index.facturapp.dades.FacturaDB;

public class Notas extends Activity {
	private Factura fact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notas);
		Bundle bundle = getIntent().getExtras();
		FacturaDB fdb = new FacturaDB(this);
		fact = fdb.getFactura(bundle.getInt("idfactura"));
		EditText nota = (EditText)findViewById(R.id.nota);
		nota.setText(fact.getNotas());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.Guardar) {
			EditText nota = (EditText)findViewById(R.id.nota);
			fact.setNotas(nota.getText().toString());
			FacturaDB fdb = new FacturaDB(this);
			fdb.updateFactura(fact);
			Toast.makeText(this, "Se ha guardado la explicacion", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		EditText nota = (EditText)findViewById(R.id.nota);
		fact.setNotas(nota.getText().toString());
		FacturaDB fdb = new FacturaDB(this);
		fdb.updateFactura(fact);
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		EditText nota = (EditText)findViewById(R.id.nota);
		fact.setNotas(nota.getText().toString());
		FacturaDB fdb = new FacturaDB(this);
		fdb.updateFactura(fact);
		super.onBackPressed();
	}
	
}
