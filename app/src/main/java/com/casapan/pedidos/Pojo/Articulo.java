package com.casapan.pedidos.Pojo;

import com.casapan.pedidos.Interface.ListItem;

public class Articulo implements ListItem {

    String nombre;
    String id;
    String categoria;
    String idCategoria;
    String cantidad;
    String stock;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public boolean isHeader() {
        return false;
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
    public String mostrarCantidad() {
        return cantidad;
    }

    @Override
    public String mostrarStock() {
        return stock;
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
        this.cantidad = cantidad;
    }

    @Override
    public void setStock(String stock) {
        this.stock = stock;
    }

}
