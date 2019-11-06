package com.casapan.pedidos.Interface;

public interface ListItem {
    boolean isHeader();
    String getNombre();
    String getId();
    String mostrarCantidad();
    String mostrarStock();
    void setId(String id);
    void setNombre(String nombre);
    void setCantidad(String cantidad);
    void setStock(String stock);
}
