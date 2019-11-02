package com.casapan.pedidos.Interface;

public interface ListItem {
    boolean isHeader();
    String mostrarNombre();
    String mostrarId();
    String mostrarCantidad();
    String mostrarStock();
    void setID(String id);
    void setNombreArticulo(String nombre);
    void setCantidad(String cantidad);
    void setStock(String stock);
    boolean getchecked();
    void setchecked(boolean c);
}
