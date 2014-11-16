package com.index.facturapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

import com.index.facturapp.adapters.Adaptercatprod;
import com.index.facturapp.clasesextra.Producto;
import com.index.facturapp.dades.FacturaDB;

public class ProdsCat extends ListActivity {

	private static Adaptercatprod adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		String Categoria = bundle.getString("categoria");
		setTitle("Productos "+ Categoria);
		FacturaDB fdb = new FacturaDB(this);
		List<Producto> prodscat = fdb.getProductoscat(fdb.getCategoria(Categoria).getId());
		List<String> prodsname = new ArrayList<String>();
		for(int i = 0; i < prodscat.size(); i++){
			prodsname.add(prodscat.get(i).getNombre());
		}
		adapter = new Adaptercatprod(this , prodsname);
		setListAdapter(adapter);
	}
}
