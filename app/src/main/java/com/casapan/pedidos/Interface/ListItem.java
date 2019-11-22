package com.casapan.pedidos.Interface;

public interface ListItem {
    boolean isHeader();
    String getNombre();
    String getId();
    String getCantidad();
    String getStock();
    String getObservacion();
    String getIdArticulo();
    String getIdLineaPedido();
    int getTorta();
    void setId(String id);
    void setNombre(String nombre);
    void setCantidad(String cantidad);
    void setStock(String stock);
    void setTorta(int torta);
    void setObservacion(String observacion);
    void setIdArticulo(String id);
    void setIdLineaPedido(String id);
}
