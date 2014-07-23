package com.index.facturapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.index.facturapp.clasesextra.Categoria;
import com.index.facturapp.clasesextra.Cliente;
import com.index.facturapp.clasesextra.Factura;
import com.index.facturapp.clasesextra.LiniaProducto;
import com.index.facturapp.clasesextra.Producto;

public class FacturaDB extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "facturas.db";
	private String sqlCreate = "CREATE TABLE CLIENTE(dni VARCHAR(10) PRIMARY KEY, nombre VARCHAR(30), apellido1 VARCHAR(30), apellido2 VARCHAR(30), direccion VARCHAR(100), localidad VARCHAR(40))";
	private String sqlCreate2 =	"CREATE TABLE CATEGORIA (id INTEGER PRIMARY KEY, categoria VARCHAR(20))";
	private String sqlCreate3 = "CREATE TABLE PRODUCTO (nombre TEXT PRIMARY KEY, precio FLOAT(8, 2), idcategoria INTEGER)";
	private String sqlCreate4 = "CREATE TABLE FACTURAS (idFactura INTERGER PRIMARY KEY, fecha DATE, estado VARCHAR(12), cliente VARCHAR(10))";
	private String sqlCreate5 = "CREATE TABLE LINIAPRODUCTO (nombreProducto TEXT, idFactura INTEGER, cantidad INTEGER, precio FLOAT(8, 2), PRIMARY KEY(nombreProducto, idFactura))";
	
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
	
	public List<LiniaProducto> getLiniasProducto(Factura factura){
		List<LiniaProducto> productos = new ArrayList<LiniaProducto>();
		SQLiteDatabase db = this.getWritableDatabase();
		String[] campos = new String[] {"nombreProducto", "cantidad"};
		String[] args = new String[] {};
		args[0] = Integer.toString(factura.getNumFact());
		Cursor c = db.query("LINIAPRODUCTO", campos, "idFactura=?", args, null, null, null);
		if(c.moveToFirst()){
			int i = 0;
			do {
				LiniaProducto prod = new LiniaProducto();
				prod.setNombre(c.getString(c.getColumnIndex("nombreProducto")));
				prod.setCantidad(c.getInt(c.getColumnIndex("cantidad")));
				prod.setFactura(factura.getNumFact());
				prod.setPrecio(c.getFloat(c.getColumnIndex("precio")));
				productos.set(i, prod);
				i++;
			} while(c.moveToNext());
		}
		else Log.e("dberror", "no hay linias de producto para la factura " + factura.getNumFact());
		return productos;
	}
	
	public Factura[] getFacturas(){
		Factura[] facturas = new Factura[]{};
		int i = 0;
		SQLiteDatabase db = this.getWritableDatabase();
	    String selectQuery = "SELECT  * FROM Factura";
	 
	    Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            Factura fact = new Factura();
	            fact.setNumFact(c.getInt(c.getColumnIndex("idFactura")));
	            fact.setEstado(c.getString(c.getColumnIndex("estado")));
	            
	            fact.setCliente(this.getCliente(c.getString(c.getColumnIndex("cliente"))));
	 
	            // adding to todo list
	            facturas[i] = fact;
	            ++i;
	        } while (c.moveToNext());
	    }
	 
	    return facturas;
	}
	
	public String[] getCategorias(){
//		Categoria[] categorias = new Categoria[]{};
//		int i = 0;
//		SQLiteDatabase db = this.getWritableDatabase();
//	    String selectQuery = "SELECT  * FROM Categoria";
//	 
//	    Log.e("dberror", selectQuery);
//	    Cursor c = db.rawQuery(selectQuery, null);
//	 
//	    // looping through all rows and adding to list
//	    if (c.moveToFirst()) {
//	        do {
//	            Categoria cat = new Categoria();
//	            cat.setId(c.getInt(c.getColumnIndex("id")));
//	            cat.setCategoria(c.getString(c.getColumnIndex("categoria")));
//	 
//	            // adding to todo list
//	            categorias[i] = cat;
//	            ++i;
//	        } while (c.moveToNext());
//	    }
//	    String[] catego = new String[i];
//	    for(int j = 0; j < i; ++j){
//	    	catego[j] = categorias[j].getCategoria();
//	    }
		
	 
//	    return catego;
		String[] debug = {"madera", "pladur"};
		return debug;
	}
	
	public Cliente getCliente(String idcliente) {
		// TODO Auto-generated method stub
		Cliente cliente = new Cliente();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM cliente where dni = " + idcliente;
		
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	    if (c != null)
	        c.moveToFirst();
	    cliente.setDni(idcliente);
	    cliente.setApellido1(c.getString(c.getColumnIndex("apellido1")));
	    cliente.setApellido2(c.getString(c.getColumnIndex("apellido2")));
	    cliente.setDir(c.getString(c.getColumnIndex("direccion")));
	    cliente.setLocalidad(c.getString(c.getColumnIndex("localidad")));
	    cliente.setNombre(c.getString(c.getColumnIndex("nombre")));
	    
		return cliente;
	}
	
	public Producto getProducto(String nombre){
		Producto producto = new Producto();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM producto where nombre = " + nombre;
		
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	    if (c != null)
	        c.moveToFirst();
	    producto.setNombre(nombre);
	    producto.setPrecio(c.getFloat(c.getColumnIndex("precio")));
	    producto.setCategoria(this.getCategoria(c.getInt(c.getColumnIndex("id"))));
	    
	    
	    
		return producto;
	}
	
	public String[] getProductos(){
//		String[] productos = new String[]{};
//		SQLiteDatabase db = this.getWritableDatabase();
//		String selectQuery = "SELECT  * FROM producto";
//		int i = 0;
//		Log.e("dberror", selectQuery);
//	    Cursor c = db.rawQuery(selectQuery, null);
//	    if (c.moveToFirst()) {
//	        do {
//	            productos[i] = c.getString(c.getColumnIndex("nombre"));
//	            ++i;
//	        } while (c.moveToNext());
//	    }
//		return productos;
		
		String[] debug = {"caca", "culo"};
		return debug;
	}
	
	public Producto[] getProductoscat (String categoria){
//		Producto[] productos = new Producto[]{};
//		int i = 0;
//		SQLiteDatabase db = this.getWritableDatabase();
//		String selectQuery = "SELECT  * FROM producto where categoria = " + categoria;
//		
//		Log.e("dberror", selectQuery);
//	    Cursor c = db.rawQuery(selectQuery, null);
//	    if (c.moveToFirst()) {
//	        do {
//	        	productos[i].setNombre(c.getString(c.getColumnIndex("nombre")));
//	    	    productos[i].setPrecio(c.getFloat(c.getColumnIndex("precio")));
//	    	    productos[i].setCategoria(this.getCategoria(categoria));
//	            ++i;
//	        } while (c.moveToNext());
//	    }
//	    return productos;
		
		//----------------debug code--------------
		Producto[] productos = new Producto[2];
		if (categoria == "madera"){
				Categoria cate = new Categoria();
				Producto prod = new Producto();
				Producto prod2 = new Producto();
				cate.setCategoria(categoria);
				cate.setId(0);
				prod.setNombre("hola");
				prod.setCategoria(cate);
				float aux = 1;
				prod.setPrecio(aux);
				productos[0]= prod;
				prod2.setNombre("adios");
				prod2.setCategoria(cate);
				aux = 2;
				prod2.setPrecio(aux);
				productos[1]= prod2;
		}
		else {
			Categoria cate = new Categoria();
			Producto prod = new Producto();
			Producto prod2 = new Producto();
			cate.setCategoria(categoria);
			cate.setId(0);
			prod.setNombre("pepe");
			prod.setCategoria(cate);
			float aux = 3;
			prod.setPrecio(aux);
			productos[0]= prod;
			prod2.setNombre("manolo");
			prod2.setCategoria(cate);
			aux = 4;
			prod2.setPrecio(aux);
			productos[1]= prod2;
		}
		return productos;
	}
	
	public Categoria getCategoria(int idcat){
		Categoria categoria = new Categoria();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM categoria where id = " + idcat;
		
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	    if (c != null)
	        c.moveToFirst();
	    categoria.setId(idcat);
	    categoria.setCategoria(c.getString(c.getColumnIndex("categoria")));
		
		return categoria;
	}
	
	public Categoria getCategoria(String cat){
		Categoria categoria = new Categoria();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM categoria where categoria = " + cat;
		
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	    if (c != null)
	        c.moveToFirst();
	    categoria.setId(c.getInt(c.getColumnIndex("id")));
	    categoria.setCategoria(cat);
		
		return categoria;
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
	
	public void createLiniaproducto (LiniaProducto liniaprod){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nombreProducto", liniaprod.getNombre());
		values.put("idFactura", liniaprod.getFactura());
		values.put("cantidad", liniaprod.getCantidad());
		values.put("precio", liniaprod.getPrecio());
		
		db.insert("liniaproducto", null, values);
	}
	public void updateLiniaproducto (LiniaProducto liniaprod){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("cantidad", liniaprod.getCantidad());
	    values.put("precio", liniaprod.getPrecio());
	 
	    // updating row
	    db.update("liniaproducto", values, "nombreProducto" + " = ? and idFactura = ?", new String []{liniaprod.getNombre(), String.valueOf(liniaprod.getFactura())});
	}
	
	public void createFactura (Factura factura){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("idFactura", factura.getNumFact());
		values.put("estado", factura.getEstado());
		values.put("cliente", factura.getCliente().getDni());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
	    values.put("fecha", dateFormat.format(date));
		
		db.insert("factura", null, values);
	}
	
	public void updateFactura (Factura factura){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("estado", factura.getEstado());
		values.put("cliente", factura.getCliente().getDni());
	 
	    // updating row
	    db.update("factura", values, "nombreProducto" + " = ?", new String []{String.valueOf(factura.getNumFact())});
	}
}
