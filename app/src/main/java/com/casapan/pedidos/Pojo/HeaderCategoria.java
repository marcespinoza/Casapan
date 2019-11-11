package com.casapan.pedidos.Pojo;

import com.casapan.pedidos.Interface.ListItem;

public class HeaderCategoria implements ListItem {

    String nombre;
    String id;


    @Override
    public boolean isHeader() {
        return true;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCantidad() {
        return "0";
    }

    @Override
    public String getStock() {
        return null;
    }

    @Override
    public String getObservacion() {
        return null;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void setCantidad(String cantidad) {

    }

    @Override
    public void setStock(String stock) {

    }

    @Override
    public void setObservacion(String observacion) {

    }


}
