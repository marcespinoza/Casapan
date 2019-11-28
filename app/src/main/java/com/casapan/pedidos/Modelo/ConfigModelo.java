package com.casapan.pedidos.Modelo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ajts.androidmads.library.ExcelToSQLite;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.casapan.pedidos.App.GlobalApplication;
import com.casapan.pedidos.Interface.ConfigInterface;
import com.casapan.pedidos.Util.Constants;

public class ConfigModelo implements ConfigInterface.Modelo {

    public ConfigInterface.Presentador ipresentador;
    SQLiteToExcel sqliteToExcel;
    Context context;

    public ConfigModelo(ConfigInterface.Presentador ipresentador) {
        this.ipresentador = ipresentador;
        context = GlobalApplication.getAppContext();
    }

    @Override
    public void enviarSucursal(String suc) {
        boolean b = Constants.getSPreferences(GlobalApplication.getAppContext()).setNombreSucursal(suc);
        ipresentador.sharedResponse(b);
    }

    @Override
    public void importarbd(String path) {
        ExcelToSQLite excelToSQLite = new ExcelToSQLite(context, "casapanpedidos.db", false);
        excelToSQLite.importFromFile(path, new ExcelToSQLite.ImportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String dbName) {
                ipresentador.reiniciarApp();
            }

            @Override
            public void onError(Exception e) {
                Log.i("Error importando",""+e.getMessage());
                ipresentador.reiniciarApp();
            }
        });
    }

    @Override
    public void exportarbd() {
        sqliteToExcel = new SQLiteToExcel(context, "casapanpedidos.db", Environment.getExternalStorageDirectory().getPath() + "/Casapan");
        sqliteToExcel.exportAllTables("casapandb.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onCompleted(String filePath) {
                ipresentador.mostrartoast();
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }
}
