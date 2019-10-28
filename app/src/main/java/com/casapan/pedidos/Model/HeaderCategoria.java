package com.casapan.pedidos.Model;

import com.casapan.pedidos.Adapter.Utils.ListItem;

public class HeaderCategoria implements ListItem {

    String nombre;
    String id;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

    @Override
    public String mostrarNombre() {
        return nombre;
    }

    @Override
    public String mostrarId() {
        return id;
    }


}
