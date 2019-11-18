package com.casapan.pedidos.Database;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Pojo.Articulo;
import com.casapan.pedidos.Pojo.Categoria;
import com.casapan.pedidos.Pojo.ListaPedido;
import com.casapan.pedidos.Pojo.Pedido;
import com.casapan.pedidos.Util.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Casapanpedidos.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PEDIDO = "pedido";
    private static final String TABLE_ARTICULO = "articulo";
    private static final String TABLE_CATEGORIA = "categoria";
    private static final String TABLE_LINEA_PEDIDO = "linea_pedido";
    private static final String TABLE_TORTA = "torta";
    private static final String TABLE_USER = "usuario";
    private static final String TABLE_EXTRA = "extra";
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
            + TABLE_PEDIDO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "torta" + " INTEGER DEFAULT 0,"+
            USUARIO + " TEXT,"+
            FECHA + " TEXT ,"+
            OBS + " TEXT ,"+
            "local" + " TEXT ,"+
            "cliente" + " TEXT,"+
            "telefono" + " TEXT,"+
            "fecha_entrega" + " TEXT,"+
            "hora_entrega" + " TEXT,"+
            "kilo" + " TEXT,"+
            "bizcochuelo" + " TEXT,"+
            "relleno1" + " TEXT,"+
            "relleno2" + " TEXT,"+
            "textotorta" + " TEXT,"+
            "adorno" + " TEXT);";

    private static final String CREATE_TABLE_LINEA_PEDIDO = "CREATE TABLE "
            + TABLE_LINEA_PEDIDO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ "id_pedido" + " INTEGER, "+ ID_ARTICULO + " INTEGER,"+ "stock" +" INTEGER," + "cantidad" + " INTEGER );";

    private static final String CREATE_TABLE_CATEGORIA = "CREATE TABLE "
            + TABLE_CATEGORIA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ NOMBRE + " TEXT );";

    private static final String CREATE_TABLE_EXTRA = "CREATE TABLE "
            + TABLE_EXTRA +
            "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "id_pedido"+ " INTEGER ,"+
            "blanco" + " INTEGER ,"+
            "amarillo" + " INTEGER ,"+
            "rosado" + " INTEGER ,"+
            "lila" + " INTEGER ,"+
            "verde" + " INTEGER ,"+
            "celeste" + " INTEGER,"+
            "anaranjado" + " INTEGER ,"+
            "cereza" + " INTEGER ,"+
            "mani" + " INTEGER ,"+
            "chipchocolate" + " INTEGER ,"+
            "baniochocolate" + " INTEGER );";



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
        db.execSQL(CREATE_TABLE_EXTRA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_PEDIDO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_LINEA_PEDIDO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ARTICULO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CATEGORIA + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_EXTRA + "'");
        onCreate(db);
    }

    //----------INSERT-----------//

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

    public long insertarPedidoTorta (String suc, String [] params) {
        SQLiteDatabase db = this.getWritableDatabase();
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("torta", 1);
        contentValues.put("local", suc);
        contentValues.put("fecha", fecha);
        contentValues.put("cliente", params[0]);
        contentValues.put("telefono", params[1]);
        contentValues.put("fecha_entrega", params[2]);
        contentValues.put("hora_entrega", params[3]);
        contentValues.put("kilo", params[4]);
        contentValues.put("bizcochuelo", params[5]);
        contentValues.put("relleno1", params[6]);
        contentValues.put("relleno2", params[7]);
        contentValues.put("textotorta", params[19]);
        contentValues.put("adorno", params[20]);
        contentValues.put("usuario", params[21]);
        contentValues.put("observacion", "Cliente: "+params[0]);
        long id = db.insert("pedido", null, contentValues);
        insertarExtra(params, id);
        return id;
    }

    public long insertarExtra (String [] params, long idPedido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_pedido", idPedido);
        contentValues.put("blanco", params[8]);
        contentValues.put("amarillo", params[9]);
        contentValues.put("rosado", params[10]);
        contentValues.put("lila", params[11]);
        contentValues.put("verde", params[12]);
        contentValues.put("celeste", params[13]);
        contentValues.put("anaranjado", params[14]);
        contentValues.put("cereza", params[15]);
        contentValues.put("mani", params[16]);
        contentValues.put("chipchocolate", params[17]);
        contentValues.put("baniochocolate", params[18]);
        long id = db.insert("extra", null, contentValues);
        return id;
    }

    public long insertarPedido (String usuario, String obs) {
        SQLiteDatabase db = this.getWritableDatabase();
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
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

    //--------UPDATE----------//

    public boolean updateArticulo (String id, String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(DESCRIPCION_ARTICULO, nombre);
        db.update("articulo", contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public boolean updateCategoria (String id, String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(NOMBRE, nombre);
        db.update("categoria", contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public boolean updatePedido(String id, String nombre, String obs, ArrayList<ListItem> pedidos){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USUARIO, nombre);
        contentValues.put(OBS, obs);
        db.update("pedido", contentValues, "id = ? ", new String[] { id } );
        for(int i = 0; i < pedidos.size(); i++){
            ContentValues contentPedidos = new ContentValues();
            String idpedido = pedidos.get(i).getId();
            contentPedidos.put("cantidad", pedidos.get(i).getCantidad());
            contentPedidos.put("stock", pedidos.get(i).getStock());
            db.update("linea_pedido", contentPedidos, "id_pedido = ? and id = ?", new String[] { id, idpedido } );
        }
        return true;
    }

    //-----------DELETE---------------//

    public Integer borrarArticulo (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("articulo",
                "id = ? ",
                new String[] { id});
    }

    public Integer borrarPedido (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("pedido",
                "id = ? ",
                new String[] { id});
    }

    public Integer borrarLineaPedido (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("linea_pedido",
                "id_pedido = ? ",
                new String[] { id});
    }

    //----------GET--------------//

    public Cursor getPedido(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from pedido where id="+id+"", null );
        return res;
    }

    public ArrayList<Pedido> getPedidos() {
        ArrayList<Pedido> lPedidos = new ArrayList<Pedido>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select p.id, p.torta, p.usuario, strftime('%d-%m-%Y', p.fecha), p.observacion from pedido p order by date(fecha) desc", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Pedido pedido = new Pedido();
            pedido.setId(res.getString(0));
            pedido.setTorta(res.getInt(1));
            pedido.setUsuario(res.getString(2));
            pedido.setFecha(res.getString(3));
            pedido.setObservacion(res.getString(4));
            lPedidos.add(pedido);
            res.moveToNext();
        }
        return lPedidos;
    }

    public ArrayList<ListItem> getPedidosbyId(String id) {
        ArrayList<ListItem> lPedidos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select p.id, p.usuario, p.observacion, a.nombre, lp.id, lp.cantidad, lp.stock from pedido p inner join linea_pedido lp on p.id=lp.id_pedido inner join articulo a on a.id=lp.id_articulo where p.id="+id+" ", null );
        if(res.getCount()>0){
        res.moveToFirst();
        Pedido pedido = new Pedido();
        pedido.setId(res.getString(0));
        pedido.setNombre(res.getString(1));
        pedido.setObservacion(res.getString(2));
        lPedidos.add(pedido);
        while(res.isAfterLast() == false){
            ListaPedido lp =  new ListaPedido();
            lp.setId(res.getString(4));
            lp.setNombre(res.getString(3));
            lp.setCantidad(res.getString(5));
            lp.setStock(res.getString(6));
            lPedidos.add(lp);
            res.moveToNext();
        }
        }
        return lPedidos;
    }

    public ArrayList<String> getPedidoTortabyId(String id){
        ArrayList<String>ptorta = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select p.local, p.cliente, p.telefono, p.fecha_entrega, " +
                                    "p.hora_entrega, p.kilo, p.bizcochuelo, p.relleno1, p.relleno2, " +
                                    "p.textotorta, p.adorno, e.blanco, e.amarillo, e.rosado," +
                "e.lila, e.verde, e.celeste, e.anaranjado, e.cerezas, e.mani, e.chipchocolate, e.baniochocolate from pedido p inner join extra e on p.id=e.id_pedido" , null );

        if(res.moveToFirst()){
            for(int i = 0; i <=21; i++){
             ptorta.add(res.getString(i));
            }
        }return ptorta;
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
