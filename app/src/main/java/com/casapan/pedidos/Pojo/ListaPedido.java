package com.casapan.pedidos.Pojo;

import com.casapan.pedidos.Interface.ListItem;

public class ListaPedido implements ListItem {


    String producto;
    String cantidad = "0";
    String stock = "0";
    String id;
    String id_articulo;


    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getNombre() {
        return producto;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCantidad() {
        return cantidad;
    }

    @Override
    public String getStock() {
        return stock;
    }

    @Override
    public String getObservacion() {
        return null;
    }

    @Override
    public String getIdArticulo() {
        return id_articulo;
    }

    @Override
    public String getIdLineaPedido() {
        return "";
    }

    @Override
    public int getTorta() {
        return 0;
    }

    @Override
    public void setId(String id) {
        this.id = id ;
    }

    @Override
    public void setNombre(String nombre) {
        this.producto=nombre;
    }

    @Override
    public void setCantidad(String cantidad) {
        this.cantidad =  cantidad;
    }

    @Override
    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public void setTorta(int torta) {

    }

    @Override
    public void setObservacion(String observacion) {

    }

    @Override
    public void setIdArticulo(String id_articulo) {
        this.id_articulo = id_articulo;
    }

    @Override
    public void setIdLineaPedido(String id) {

    }


}
