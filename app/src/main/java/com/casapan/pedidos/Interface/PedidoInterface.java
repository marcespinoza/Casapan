package com.casapan.pedidos.Interface;

import java.util.ArrayList;

public interface PedidoInterface {

    interface Vista{
        void mostrarPedidos();
        void mostrarPdf(String id);
    }

    interface Presentador{
        void getPedidos();
        void mostrarPedidos();
        void armarPedidoTorta(String [] params);
        void generarPdf(String[] params, ArrayList<ListItem> pedidos);
        void mostrarPdf(String path);
    }

    interface Modelo{
        void getPedidos();
        void pedidoTorta(String [] params);
        void generarPdf(String [] params, ArrayList<ListItem>pedidos);
    }

}
