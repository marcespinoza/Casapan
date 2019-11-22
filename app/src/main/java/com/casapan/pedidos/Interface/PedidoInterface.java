package com.casapan.pedidos.Interface;

import android.graphics.Bitmap;

import java.util.ArrayList;

public interface PedidoInterface {

    interface Vista{
        void mostrarPedidos();
        void mostrarPdf(String id);
        void mostrarPedidoTorta(ArrayList<String>ptorta);
        void mostrarError(String mensaje);
    }

    interface Presentador{
        void getPedidos();
        void mostrarPedidos();
        void armarPedidoTorta(String idpedido, String[] params, Bitmap bitmaptorta);
        void generarPdf(String[] params, ArrayList<ListItem> pedidos);
        void mostrarPdf(String path);
        void actualizarPedidoTorta(String id);
        void actualizarPedido(String id, String usuario, ArrayList<ListItem> pedidos, String obs);
        void enviarPedidoTorta(ArrayList<String> ptorta);
        void mostrarError(String mensaje);
        void armarPedido(String usuario, ArrayList<ListItem> pedidos, String obs);
    }

    interface Modelo{
        void getPedidos();
        void obtenerPedidoTorta(String id);
        void updatePedido(String id, String usuario, ArrayList<ListItem> pedidos, String obs);
        void pedidoTorta(String idpedido, String[] params, Bitmap bitmaptorta);
        void generarPdf(String [] params, ArrayList<ListItem>pedidos);
        void pedido(String usuario, ArrayList<ListItem> pedidos, String obs);
    }

}
