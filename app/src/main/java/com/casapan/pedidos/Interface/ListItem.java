package com.casapan.pedidos.Interface;

public interface ListItem {
    boolean isHeader();
    String getNombre();
    String getId();
    String getCantidad();
    String getStock();
    String getObservacion();
    void setId(String id);
    void setNombre(String nombre);
    void setCantidad(String cantidad);
    void setStock(String stock);
    void setObservacion(String observacion);
}
