package com.index.facturapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FacturaDB extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "facturas.db";
	private String sqlCreate = "CREATE TABLE CLIENTE(dni VARCHAR(10), nombre VARCHAR(30), apellido1 VARCHAR(30), apellido2 VARCHAR(30), direccion VARCHAR(100), localidad VARCHAR(40));" +
			"CREATE TABLE CATEGORIA (categoria VARCHAR(20));" +
			"CREATE TABLE PRODUCTO (nombre TEXT, precio FLOAT(8, 2), categoria VARCHAR(20))" +
			"CREATE TABLE FACTURAS (idFactura INTERGER, fecha DATE, estado VARCHAR(12), cliente VARCHAR(10));" +
			"CREATE TABLE LINIAPRODUCTO (nombreProducto TEXT, idFactura INTEGER)";
	
	public FacturaDB(Context context){
		super (context, DATABASE_NAME, null, 1);
	}
	
	public LiniaProducto[] getLiniasProducto(int idFactura){
		//aqui estabas
		LiniaProducto[] productos;
		return productos;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){ 
		db.execSQL(sqlCreate);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS LINIAPRODUCTO, FACTURAS, PRODUCTO, CATEGORIA, CLIENTE");
		db.execSQL(sqlCreate);
	}
	
	
}
