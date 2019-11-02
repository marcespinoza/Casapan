package com.casapan.pedidos.Database;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.casapan.pedidos.Model.Articulo;
import com.casapan.pedidos.Model.Categoria;
import com.casapan.pedidos.Model.Pedido;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Casapanpedidos.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PEDIDO = "pedido";
    private static final String TABLE_ARTICULO = "articulo";
    private static final String TABLE_CATEGORIA = "categoria";
    private static final String TABLE_LINEA_PEDIDO = "linea_pedido";
    private static final String TABLE_USER = "usuario";
    private static final String NOMBRE= "nombre";
    private static final String KEY_ID = "id";
    private static final String ID_ARTICULO = "id_articulo";
    private static final String DESCRIPCION_ARTICULO = "nombre";
    private static final String USUARIO = "usuario";
    private static final String FECHA = "fecha";
    private static final String OBS = "observacion";
    private static final String KEY_CATEGORIA = "idcategoria";
    private static final String CREATE_TABLE_ARTICULO = "CREATE TABLE "
            + TABLE_ARTICULO + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + DESCRIPCION_ARTICULO + " TEXT," +  KEY_CATEGORIA + " INTEGER);";

    private static final String CREATE_TABLE_PEDIDO = "CREATE TABLE "
            + TABLE_PEDIDO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ USUARIO + " TEXT,"+ FECHA + " TEXT ,"+ OBS + " TEXT );";

    private static final String CREATE_TABLE_LINEA_PEDIDO = "CREATE TABLE "
            + TABLE_LINEA_PEDIDO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ "id_pedido" + " INTEGER, "+ ID_ARTICULO + " TEXT,"+ "stock" +" INTEGER," + "cantidad" + " INTEGER );";

    private static final String CREATE_TABLE_CATEGORIA = "CREATE TABLE "
            + TABLE_CATEGORIA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ NOMBRE + " TEXT );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_ARTICULO);
        db.execSQL(CREATE_TABLE_CATEGORIA);
        db.execSQL(CREATE_TABLE_PEDIDO);
        db.execSQL(CREATE_TABLE_LINEA_PEDIDO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_PEDIDO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_LINEA_PEDIDO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ARTICULO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CATEGORIA + "'");
        onCreate(db);
    }

    public boolean insertarArticulo (String nombre, String categoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nombre);
        contentValues.put("idcategoria", categoria);
        long id = db.insert("articulo", null, contentValues);
        if(id==-1){
            return false;
        }else{
            return true;
        }
    }

    public long insertarPedido (String usuario, String obs) {
        SQLiteDatabase db = this.getWritableDatabase();
        String fecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario", usuario);
        contentValues.put("fecha", fecha);
        contentValues.put("observacion", obs);
        long id = db.insert("pedido", null, contentValues);
        return id;
    }

    public boolean insertarLineaPedido (int id_pedido, int id_articulo, int cant, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_pedido", id_pedido);
        contentValues.put("id_articulo", id_articulo);
        contentValues.put("cantidad", cant);
        contentValues.put("stock", stock);
        long id = db.insert("linea_pedido", null, contentValues);
        if(id==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertarCategoria (String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nombre);
        db.insert("categoria", null, contentValues);
        return true;
    }

    public Cursor getPedido(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from pedido where id="+id+"", null );
        return res;
    }

    public ArrayList<Pedido> getPedidos() {
        ArrayList<Pedido> lPedidos = new ArrayList<Pedido>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from pedido", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Pedido pedido = new Pedido();
            pedido.setUsuario(res.getString(1));
            pedido.setFecha(res.getString(2));
            pedido.setObs(res.getString(3));
            lPedidos.add(pedido);
            res.moveToNext();
        }
        return lPedidos;
    }

    public boolean updateArticulo (String id, String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(DESCRIPCION_ARTICULO, nombre);
        db.update("articulo", contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public Integer borrarArticulo (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("articulo",
                "id = ? ",
                new String[] { id});
    }

    public ArrayList<Articulo> getArticulosPorCategoria() {
        ArrayList<Articulo> lArticulos = new ArrayList<Articulo>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select a.id, a.nombre, c.id, c.nombre from articulo a inner join categoria c on a.idcategoria=c.id order by c.nombre", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Articulo articulo = new Articulo();
            articulo.setId(res.getString(0));
            articulo.setNombre(res.getString(1));
            articulo.setIdCategoria(res.getString(2));
            articulo.setCategoria(res.getString(3));
            lArticulos.add(articulo);
            res.moveToNext();
        }
        return lArticulos;
    }

    public ArrayList<Categoria> getCategorias() {
        ArrayList<Categoria> lCategoria = new ArrayList<Categoria>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from categoria", null );
        res.moveToFirst();
        Categoria art = new Categoria();
        art.setNombre("Seleccione");
        lCategoria.add(art);
        while(res.isAfterLast() == false){
            Categoria categoria = new Categoria();
            categoria.setId(res.getString(res.getColumnIndex(KEY_ID)));
            categoria.setNombre(res.getString(res.getColumnIndex(NOMBRE)));
            lCategoria.add(categoria);
            res.moveToNext();
        }
        return lCategoria;
    }
}
