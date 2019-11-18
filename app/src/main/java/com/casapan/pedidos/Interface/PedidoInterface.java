package com.casapan.pedidos.Interface;

import java.util.ArrayList;

public interface PedidoInterface {

    interface Vista{
        void mostrarPedidos();
        void mostrarPdf(String id);
        void mostrarPedidoTorta(ArrayList<String>ptorta);
    }

    interface Presentador{
        void getPedidos();
        void mostrarPedidos();
        void armarPedidoTorta(String [] params);
        void generarPdf(String[] params, ArrayList<ListItem> pedidos);
        void mostrarPdf(String path);
        void actualizarPedidoTorta(String id);
        void enviarPedidoTorta(ArrayList<String> ptorta);
    }

    interface Modelo{
        void getPedidos();
        void obtenerPedidoTorta(String id);
        void pedidoTorta(String [] params);
        void generarPdf(String [] params, ArrayList<ListItem>pedidos);
    }

}
