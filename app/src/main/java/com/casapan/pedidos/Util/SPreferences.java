package com.casapan.pedidos.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPreferences{

    protected final String SUCURSAL = "sucursal";

    private Context context;
    private SharedPreferences sp;

    public SPreferences(Context context){
        this.context = context;
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }


    //------sucursal------
    public boolean setNombreSucursal(String token){
        return sp.edit().putString(SUCURSAL, token).commit();
    }
    public String getNombreSucursal(){
        return sp.getString(SUCURSAL, "");
    }
    //-----------------



}
