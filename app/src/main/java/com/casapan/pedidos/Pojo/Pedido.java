package com.casapan.pedidos.Pojo;

import com.casapan.pedidos.Interface.ListItem;

public class Pedido implements ListItem {

    String usuario;
    String fecha;
    String obs;
    String id;
    int torta;

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getNombre() {
        return usuario;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getCantidad() {
        return null;
    }

    @Override
    public String getStock() {
        return null;
    }

    @Override
    public String getObservacion() {
        return obs;
    }

    @Override
    public String getIdArticulo() {
        return null;
    }

    @Override
    public int getTorta() {
        return torta;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setNombre(String nombre) {
        this.usuario = nombre;
    }

    @Override
    public void setCantidad(String cantidad) {

    }

    @Override
    public void setStock(String stock) {

    }

    @Override
    public void setTorta(int torta) {
        this.torta = torta;
    }

    @Override
    public void setObservacion(String obs) {
        this.obs = obs;
    }

    @Override
    public void setIdArticulo(String id) {

    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
