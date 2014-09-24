package com.index.facturapp.dades;

import java.text.ParseException;
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
	private String sqlCreate2 =	"CREATE TABLE CATEGORIA (id INTEGER PRIMARY KEY, categoria VARCHAR(30))";
	private String sqlCreate3 = "CREATE TABLE PRODUCTO (nombre TEXT PRIMARY KEY, precio FLOAT(8, 2), idcategoria VARCHAR(30))";
	private String sqlCreate4 = "CREATE TABLE FACTURAS (idFactura INTERGER PRIMARY KEY, fecha VARCHAR(60), estado VARCHAR(12), cliente VARCHAR(10), notas TEXT)";
	private String sqlCreate5 = "CREATE TABLE LINIAPRODUCTO (nombreProducto TEXT, idFactura INTEGER, cantidad INTEGER, precio FLOAT(8, 2), PRIMARY KEY(nombreProducto, idFactura))";
	private String sqlCreate6 = "INSERT INTO CATEGORIA (id, categoria) VALUES (0, 'Sin categoria')";
	private String sqlCreate7 = "CREATE TABLE IVA (valor INTEGER PRIMARY KEY)";
	private String sqlCreate8 = "INSERT INTO IVA (valor) VALUES (21)";
	private SQLiteDatabase db;
	
	public FacturaDB(Context context){
		super (context, DATABASE_NAME, null, 14);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){ 
		db.execSQL(sqlCreate);
		db.execSQL(sqlCreate2);
		db.execSQL(sqlCreate3);
		db.execSQL(sqlCreate4);
		db.execSQL(sqlCreate5);
		db.execSQL(sqlCreate6);
		db.execSQL(sqlCreate7);
		db.execSQL(sqlCreate8);
		this.db = db;
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS LINIAPRODUCTO");
		db.execSQL("DROP TABLE IF EXISTS FACTURAS");
		db.execSQL("DROP TABLE IF EXISTS PRODUCTO");
		db.execSQL("DROP TABLE IF EXISTS CATEGORIA");
		db.execSQL("DROP TABLE IF EXISTS CLIENTE");
		db.execSQL("DROP TABLE IF EXISTS IVA");
		onCreate(db);
	}
	
	public void openDB (){
		this.db = this.getWritableDatabase();
	}
	
	public void closeDB(){
		this.db.close();
	}
	
	public int getIVA(){
		int IVA = 0;
		this.db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM IVA";
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	    if (c.moveToFirst()) {
	    	IVA = c.getInt(c.getColumnIndex("valor"));
	    }
		return IVA;
	}
	
	public void setIVA(int nIVA, int oIVA){
		this.db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("valor", nIVA);
	 
	    // updating row
	    db.update("IVA", values, "valor" + " = ?",
	            new String[] { String.valueOf(oIVA) });
	    Log.e("dberror", "Iva actualizado");
	}
	
	public boolean existsLiniaProducto (LiniaProducto lprod){
		LiniaProducto aux = new LiniaProducto();
		this.db = this.getWritableDatabase();
		String[] campos = new String[]{"nombreProducto", "cantidad", "precio", "idFactura"};
		String[] args = {lprod.getNombre(), Integer.toString(lprod.getFactura())};
		Cursor c = db.query("LINIAPRODUCTO", campos, "nombreProducto = ? and idFactura = ?", args, null, null, null);
		return c.moveToFirst();
	}
	
	public List<LiniaProducto> getLiniasProducto(Factura factura){
		List<LiniaProducto> productos = new ArrayList<LiniaProducto>();
		this.db = this.getWritableDatabase();
		String[] campos = new String[] {"nombreProducto", "cantidad", "precio"};
		String[] args = {Integer.toString(factura.getNumFact())};
		Log.e("dberror", "Consultando Liniaprod factura: " + String.valueOf(factura.getNumFact()));
		Cursor c = db.query("LINIAPRODUCTO", campos, "idFactura=?", args, null, null, null);
		if(c.moveToFirst()){
			int i = 0;
			do {
				LiniaProducto prod = new LiniaProducto();
				prod.setNombre(c.getString(c.getColumnIndex("nombreProducto")));
				prod.setCantidad(c.getInt(c.getColumnIndex("cantidad")));
				prod.setFactura(factura.getNumFact());
				prod.setPrecio(c.getFloat(c.getColumnIndex("precio")));
				productos.add(i, prod);
				i++;
			} while(c.moveToNext());
		}
		else Log.e("dberror", "no hay linias de producto para la factura " + factura.getNumFact());
		
		return productos;
	}
	
	public List<String> getCategorias(){
		List<Categoria> categorias = new ArrayList<Categoria>();
		int i = 0;
		this.db = this.getWritableDatabase();
	    String selectQuery = "SELECT  * FROM Categoria";
	 
	    Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            Categoria cat = new Categoria();
	            cat.setId(c.getInt(c.getColumnIndex("id")));
	            cat.setCategoria(c.getString(c.getColumnIndex("categoria")));
	 
	            // adding to todo list
	            categorias.add(i, cat);
	            ++i;
	        } while (c.moveToNext());
	    }
	    List<String> catego = new ArrayList<String>();
	    int j = 0;
	    for(j = 0; j < i; ++j){
	    	catego.add(j, categorias.get(j).getCategoria());
	    }
		
	    
	    return catego;
		
	}
	
	public Cliente getCliente(String idcliente) {
		Cliente cliente = new Cliente();
		this.db = this.getWritableDatabase();
		String selectQuery = "SELECT  dni, nombre, apellido1, apellido2, direccion, localidad FROM cliente where dni = ?";
		String[] aux = {idcliente};
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, aux);
	    if (c.moveToFirst()){
	        cliente.setDni(idcliente);
	        cliente.setApellido1(c.getString(c.getColumnIndex("apellido1")));
	        cliente.setApellido2(c.getString(c.getColumnIndex("apellido2")));
	        cliente.setDir(c.getString(c.getColumnIndex("direccion")));
	        cliente.setLocalidad(c.getString(c.getColumnIndex("localidad")));
	        cliente.setNombre(c.getString(c.getColumnIndex("nombre")));
	        return cliente;
	    
	        
	    }
	    return null;
	}
	
	public Cliente getCliente(String nombre, String ape1, String ape2) {
		Cliente cliente = new Cliente();
		this.db = this.getWritableDatabase();
		String selectQuery = "SELECT  dni, direccion, localidad FROM cliente where nombre = ? AND apellido1 = ? AND apellido2 = ?";
		String[] aux = {nombre, ape1, ape2};
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, aux);
	    if (c.moveToFirst()){
	    cliente.setDni(c.getString(c.getColumnIndex("dni")));
	    cliente.setApellido1(ape1);
	    cliente.setApellido2(ape2);
	    cliente.setDir(c.getString(c.getColumnIndex("direccion")));
	    cliente.setLocalidad(c.getString(c.getColumnIndex("localidad")));
	    cliente.setNombre(nombre);
	    
		return cliente;
	    }
	    return null;
	}
	
	public Producto getProducto(String nombre){
		Producto producto = new Producto();
		this.db = this.getWritableDatabase();
		String selectQuery = "SELECT  nombre, precio, idcategoria FROM producto where nombre = ?";
		String[] aux = {nombre};
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, aux);
	    if (c != null)
	        c.moveToFirst();
	    producto.setNombre(nombre);
	    producto.setPrecio(c.getFloat(c.getColumnIndex("precio")));
	    producto.setCategoria(this.getCategoria(c.getInt(c.getColumnIndex("idcategoria"))));
	    
	    
	      
		return producto;
	}
	
	public List<String> getProductos(){
		List<String> productos = new ArrayList<String>();
		this.db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM producto";
		int i = 0;
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	    if (c.moveToFirst()) {
	        do {
	            productos.add(i, c.getString(c.getColumnIndex("nombre")));
	            ++i;
	        } while (c.moveToNext());
	    }
	      
		return productos;
	}
	
	public List<Producto> getProductoscat (int idcategoria){
		List<Producto> productos = new ArrayList<Producto>();
		int i = 0;
		this.db = this.getWritableDatabase();
		String selectQuery = "SELECT nombre, precio, idcategoria FROM producto where idcategoria = ?";
		String[] aux = {String.valueOf(idcategoria)};
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, aux);
	    if (c.moveToFirst()) {
	        do {
	        	//Producto prod = productos.get(i);
	        	Producto prod = new Producto();
	        	prod.setNombre(c.getString(c.getColumnIndex("nombre")));
	    	    prod.setPrecio(c.getFloat(c.getColumnIndex("precio")));
	    	    prod.setCategoria(this.getCategoria(idcategoria));
	    	    productos.add(i, prod);
	            ++i;
	        } while (c.moveToNext());
	    }
	      
	    return productos;
		
		/*//----------------debug code--------------
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
		return productos;*/
	}
	
	public Categoria getCategoria(int idcat){
		Categoria categoria = new Categoria();
		this.db = this.getWritableDatabase(); 
		String selectQuery = "SELECT  id, categoria FROM categoria where id = ?";
		String[] aux = {String.valueOf(idcat)};
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, aux);
	    if (c != null)
	        c.moveToFirst();
	    categoria.setId(idcat);
	    categoria.setCategoria(c.getString(c.getColumnIndex("categoria")));
	      
		return categoria;
	}
	
	public Categoria getCategoria(String cat){
		Categoria categoria = new Categoria();
		this.db = this.getWritableDatabase();
		String selectQuery = "SELECT  categoria, id FROM CATEGORIA where categoria = ?";
		
		String[] aux = {cat};
		Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, aux);
	    if (c != null)
	        c.moveToFirst();
	    categoria.setId(c.getInt(c.getColumnIndex("id")));
	    categoria.setCategoria(cat);
	      
		return categoria;
	}

	public void createCategoria (Categoria categoria){
		this.db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("categoria", categoria.getCategoria());
		values.put("id", categoria.getId());
		
		db.insert("categoria", null, values);
		  
	}
	
	public void updateCategoria (Categoria categoria){
		this.db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put("categoria", categoria.getCategoria());
	 
	    // updating row
	    db.update("categoria", values, "id" + " = ?",
	            new String[] { String.valueOf(categoria.getId()) });
	      
	}
	
	public void removeCategoria(String categoria){
		this.db = this.getWritableDatabase();
		Categoria catego = this.getCategoria(categoria);
		ContentValues nuevo = new ContentValues();
		nuevo.put("idcategoria", "0");
		String[] id ={String.valueOf(catego.getId())};
		//try{
			db.update("producto", nuevo, "idcategoria = ?", id );	
		/*}
		catch(Exception e){
			Log.e("dberror", "STACKTRACE");
			Log.e("DBerror", Log.getStackTraceString(e));
		}*/
		
		String[] cate ={categoria};
		db.delete("CATEGORIA", "categoria=?", cate);
		  
	}
	
	public void createCliente (Cliente cliente){
		this.db = this.getWritableDatabase();
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
		 
		this.db = this.getWritableDatabase();
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
	
	public void removeCliente(String idclient) {
		this.db = this.getWritableDatabase();
		String[] aux = {String.valueOf(idclient)};
		ContentValues values = new ContentValues();
		values.putNull("cliente");
		db.update("FACTURAS", values, "cliente = ?", aux);
		db.delete("CLIENTE", "dni=?", aux);
	}
	
	public List<String> getClientes() {
		List<String> clientes = new ArrayList<String>();
		int i = 0;
		this.db = this.getWritableDatabase();
	    String selectQuery = "SELECT * FROM cliente";
	 
	    Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            Cliente cliente = new Cliente();
	            cliente.setNombre(c.getString(c.getColumnIndex("nombre")));
	            cliente.setApellido1(c.getString(c.getColumnIndex("apellido1")));
	            cliente.setApellido2(c.getString(c.getColumnIndex("apellido2")));
	            cliente.setDni(c.getString(c.getColumnIndex("dni")));
	            cliente.setDir(c.getString(c.getColumnIndex("direccion")));
	            cliente.setLocalidad(c.getString(c.getColumnIndex("localidad")));
	        	String aux = new String();
	            aux = cliente.getNombre();
	            aux = aux.concat(" ");
	            aux = aux.concat(cliente.getApellido1());
	            aux = aux.concat(" ");
	            aux = aux.concat(cliente.getApellido2());
	        	clientes.add(i, aux);
	            i++;
	        } while (c.moveToNext());
	    }
		return clientes;
	}
	
	public List<Cliente> getClientes_object() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		int i = 0;
		this.db = this.getWritableDatabase();
	    String selectQuery = "SELECT * FROM cliente";
	 
	    Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            Cliente cliente = new Cliente();
	            cliente.setNombre(c.getString(c.getColumnIndex("nombre")));
	            cliente.setApellido1(c.getString(c.getColumnIndex("apellido1")));
	            cliente.setApellido2(c.getString(c.getColumnIndex("apellido2")));
	            cliente.setDni(c.getString(c.getColumnIndex("dni")));
	            cliente.setDir(c.getString(c.getColumnIndex("direccion")));
	            cliente.setLocalidad(c.getString(c.getColumnIndex("localidad")));
	        	
	        	clientes.add(i, cliente);
	            i++;
	        } while (c.moveToNext());
	    }
		return clientes;
	}
	
	public void createProducto (Producto producto){
		this.db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nombre", producto.getNombre());
		values.put("precio", producto.getPrecio());
		values.put("idcategoria", producto.getCategoria().getId());
		
		db.insert("producto", null, values);
		  
	}
	
	public void updateProducto (Producto producto){
		 
		this.db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put("precio", producto.getPrecio());
		values.put("idcategoria", producto.getCategoria().getId());
	 
	    // updating row
	    db.update("PRODUCTO", values, "nombre = ?", new String []{producto.getNombre()});
	      
	}
	
	public void removeProducto(String producto){
		this.db = this.getWritableDatabase();
		String[] prod ={producto};
		db.delete("PRODUCTO", "nombre=?", prod);
		  
	}
	
	public void createLiniaproducto (LiniaProducto liniaprod){
		this.db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nombreProducto", liniaprod.getNombre());
		values.put("idFactura", liniaprod.getFactura());
		values.put("cantidad", liniaprod.getCantidad());
		values.put("precio", liniaprod.getPrecio());
		
		db.insert("LINIAPRODUCTO", null, values);
		  
	}
	public void updateLiniaproducto (LiniaProducto liniaprod){
		 
		this.db = this.getWritableDatabase(); 
	    ContentValues values = new ContentValues();
	    values.put("cantidad", liniaprod.getCantidad());
	    values.put("precio", liniaprod.getPrecio());
	 
	    // updating row
	    db.update("LINIAPRODUCTO", values, "nombreProducto" + " = ? and idFactura = ?", new String []{liniaprod.getNombre(), String.valueOf(liniaprod.getFactura())});
	      
	}
	
	public void removeLiniaProducto(LiniaProducto lprod) {
		// TODO Auto-generated method stub
		this.db = this.getWritableDatabase();
		String[] lp ={String.valueOf(lprod.getFactura()), String.valueOf(lprod.getNombre())};
		db.delete("LINIAPRODUCTO", "idFactura=? AND nombreProducto = ?", lp);
	}
	
	public Factura getFactura(int idfactura) {
		Factura factura = new Factura();
		this.db = this.getWritableDatabase();
	    String selectQuery = "SELECT  idFactura, fecha, estado, cliente, notas FROM FACTURAS WHERE idFactura = ?";
	    String[] args = {String.valueOf(idfactura)};
	    Log.e("dberror", selectQuery  + " idfactura = " + String.valueOf(idfactura));
	    Cursor c = db.rawQuery(selectQuery, args);
	    if (c.moveToFirst()) {
	    	factura.setNumFact(idfactura);
	    	factura.setEstado(c.getString(c.getColumnIndex("estado")));
	    	factura.setNotas(c.getString(c.getColumnIndex("notas")));
	    	if (!c.isNull(c.getColumnIndex("cliente"))){
            	factura.setCliente(this.getCliente(c.getString(c.getColumnIndex("cliente"))));
            }
	    	Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
				date = dateFormat.parse(c.getString(c.getColumnIndex("fecha")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
            factura.setData(date);
            return factura;
	    }
		return null;
	}
	
	public List<Factura> getFacturas() throws ParseException{
		List<Factura> facturas = new ArrayList<Factura>();
		int i = 0;
		this.db = this.getWritableDatabase();
	    String selectQuery = "SELECT  * FROM FACTURAS";
	 
	    Log.e("dberror", selectQuery);
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            Factura fact = new Factura();
	            fact.setNumFact(c.getInt(c.getColumnIndex("idFactura")));
	            fact.setEstado(c.getString(c.getColumnIndex("estado")));
	            fact.setNotas(c.getString(c.getColumnIndex("notas")));
	            if (!c.isNull(c.getColumnIndex("cliente"))){
	            	fact.setCliente(this.getCliente(c.getString(c.getColumnIndex("cliente"))));
	            }
	            Date date = new Date();
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            date = dateFormat.parse(c.getString(c.getColumnIndex("fecha")));
	            fact.setData(date);
	 
	            // adding to todo list
	            facturas.add(i, fact);
	            ++i;
	        } while (c.moveToNext());
	    }
	 
	    return facturas;
	}
	
	public void createFactura (Factura factura){
		this.db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("idFactura", factura.getNumFact());
		values.put("estado", factura.getEstado());
		values.put("notas", factura.getNotas());
		if(factura.getCliente() != null){
			values.put("cliente", factura.getCliente().getDni());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	    values.put("fecha", dateFormat.format(factura.getData()));
		
		db.insert("FACTURAS", null, values);
		  
	}
	
	public void updateFactura (Factura factura){
		 
		this.db = this.getWritableDatabase(); 
	    ContentValues values = new ContentValues();
	    values.put("estado", factura.getEstado());
	    values.put("notas", factura.getNotas());
	    if(factura.getCliente()!= null)
		values.put("cliente", factura.getCliente().getDni());
	 
	    // updating row
	    db.update("FACTURAS", values, "idFactura" + " = ?", new String []{String.valueOf(factura.getNumFact())});
	      
	}
	
	public void removeFactura (Factura factura){
		this.db = this.getWritableDatabase();
		String[] fact ={String.valueOf(factura.getNumFact())};
		db.delete("FACTURAS", "idFactura=?", fact);
	}

	

	

	

	
}
