package com.index.facturapp;

import com.index.facturapp.clasesextra.Categoria;
import com.index.facturapp.clasesextra.Cliente;
import com.index.facturapp.clasesextra.LiniaProducto;
import com.index.facturapp.clasesextra.Producto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FacturaDB extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "facturas.db";
	private String sqlCreate = "CREATE TABLE CLIENTE(dni VARCHAR(10) PRIMARY KEY, nombre VARCHAR(30), apellido1 VARCHAR(30), apellido2 VARCHAR(30), direccion VARCHAR(100), localidad VARCHAR(40))";
	private String sqlCreate2 =	"CREATE TABLE CATEGORIA (id INTEGER PRIMARY KEY, categoria VARCHAR(20))";
	private String sqlCreate3 = "CREATE TABLE PRODUCTO (nombre TEXT PRIMARY KEY, precio FLOAT(8, 2), idcategoria INTEGER)";
	private String sqlCreate4 = "CREATE TABLE FACTURAS (idFactura INTERGER PRIMARY KEY, fecha DATE, estado VARCHAR(12), cliente VARCHAR(10))";
	private String sqlCreate5 = "CREATE TABLE LINIAPRODUCTO (nombreProducto TEXT PRIMARY KEY, idFactura INTEGER, cantidad INTEGER)";
	
	public FacturaDB(Context context){
		super (context, DATABASE_NAME, null, 3);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){ 
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreate2);
		db.execSQL(sqlCreate3);
		db.execSQL(sqlCreate4);
		db.execSQL(sqlCreate5);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS LINIAPRODUCTO");
		db.execSQL("DROP TABLE IF EXISTS FACTURAS");
		db.execSQL("DROP TABLE IF EXISTS PRODUCTO");
		db.execSQL("DROP TABLE IF EXISTS CATEGORIA");
		db.execSQL("DROP TABLE IF EXISTS CLIENTE");
		onCreate(db);
	}
	
	public LiniaProducto[] getLiniasProducto(int idFactura){
		LiniaProducto[] productos = new LiniaProducto[]{};
		SQLiteDatabase db = this.getWritableDatabase();
		String[] campos = new String[] {"nombreProducto", "cantidad"};
		String[] args = new String[] {};
		args[0] = Integer.toString(idFactura);
		Cursor c = db.query("LINIAPRODUCTO", campos, "idFactura=?", args, null, null, null);
		if(c.moveToFirst()){
			int i = 0;
			do {
				productos[i].setNombre(c.getString(0));
				productos[i].setCantidad(c.getInt(1));
				productos[i].setFactura(idFactura);
				String[] campos2 = new String[] {"precio", "categoria"};
				String[] args2 = new String[] {c.getString(0)};
				Cursor p = db.query("PRODUCTO", campos2, "nombre=?", args2, null, null, null);
				if(c.moveToFirst()){
					productos[i].setPrecio(p.getFloat(0));
					productos[i].setCategoria(p.getString(1));
				}
				else Log.e("dberror", "no hay producto con ese nombre en factura " + idFactura);
				i++;
			} while(c.moveToNext());
		}
		else Log.e("dberror", "no hay linias de producto para la factura " + idFactura);
		return productos;
	}
	
	public void createCategoria (Categoria categoria){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("categoria", categoria.getCategoria());
		values.put("id", categoria.getId());
		
		db.insert("categoria", null, values);
	}
	
	public void updateCategoria (Categoria categoria){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("categoria", categoria.getCategoria());
	 
	    // updating row
	    db.update("categoria", values, "id" + " = ?",
	            new String[] { String.valueOf(categoria.getId()) });
	}
	
	public void createCliente (Cliente cliente){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("dni", cliente.getDni());
		values.put("nombre", cliente.getNombre());
		values.put("apellido1", cliente.getApellido1());
		values.put("apellido2", cliente.getApellido2());
		values.put("direccion", cliente.getDir());
		values.put("localidad", cliente.getLocalidad());
		
		db.insert("cliente", null, values);
	}
	
	public void updateCliente (Cliente cliente) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("nombre", cliente.getNombre());
		values.put("apellido1", cliente.getApellido1());
		values.put("apellido2", cliente.getApellido2());
		values.put("direccion", cliente.getDir());
		values.put("localidad", cliente.getLocalidad());
	 
	    // updating row
	    db.update("cliente", values, "dni" + " = ?",
	            new String[] { String.valueOf(cliente.getDni()) });
	}
	
	public void createProducto (Producto producto){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nombre", producto.getNombre());
		values.put("precio", producto.getPrecio());
		values.put("idcategoria", producto.getCategoria().getId());
		
		
		db.insert("producto", null, values);
	}
	
	public void updateProducto (Producto producto){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("precio", producto.getPrecio());
		values.put("idcategoria", producto.getCategoria().getId());
	 
	    // updating row
	    db.update("producto", values, "nombre" + " = ?", new String []{producto.getNombre()});
	}
}
