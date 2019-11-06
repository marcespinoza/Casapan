package com.casapan.pedidos.Pojo;

import com.casapan.pedidos.Interface.ListItem;

public class ListaPedido implements ListItem {

    String sucursal;
    String fecha;
    String empleado;
    String producto;
    String cantidad = "0";
    String stock = "0";
    String comentario;
    boolean checked;
    String idarticulo;


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
        return idarticulo;
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
        this.idarticulo = id ;
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



}
