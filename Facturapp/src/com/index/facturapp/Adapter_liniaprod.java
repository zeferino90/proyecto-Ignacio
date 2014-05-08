package com.index.facturapp;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

public class Adapter_liniaprod extends ArrayAdapter<LiniaProducto> {
	private Context context;
	private LiniaProducto[] datos;
	
	Adapter_liniaprod(Activity context, LiniaProducto[] datos){
		super(context, R.layout.liniaprod);
        this.context = context;
        this.datos = datos;
	}
	
	
}
