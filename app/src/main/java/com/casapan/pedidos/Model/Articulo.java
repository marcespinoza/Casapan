package com.casapan.pedidos.Model;

import com.casapan.pedidos.Adapter.Utils.ListItem;
import com.casapan.pedidos.Vista.FragmentArticulo;

public class Articulo implements ListItem {

    String nombre;
    String id;
    String categoria;
    String idCategoria;

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String mostrarNombre() {
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String mostrarId() {
        return id;
    }

}
