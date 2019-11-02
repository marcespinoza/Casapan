package com.casapan.pedidos.Pojo;

import com.casapan.pedidos.Interface.ListItem;

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

    @Override
    public String mostrarCantidad() {
        return null;
    }

    @Override
    public String mostrarStock() {
        return null;
    }

    @Override
    public void setID(String id) {

    }

    @Override
    public void setNombreArticulo(String nombre) {

    }

    @Override
    public void setCantidad(String cantidad) {

    }

    @Override
    public void setStock(String stock) {

    }


    @Override
    public boolean getchecked() {
        return false;
    }

    @Override
    public void setchecked(boolean b) {

    }


}
