package com.casapan.pedidos.Presentador;

import android.graphics.Bitmap;

import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.Interface.PedidoInterface;
import com.casapan.pedidos.Modelo.PedidoModelo;

import java.util.ArrayList;

public class PedidoPresentador implements PedidoInterface.Presentador {

    private PedidoInterface.Vista vista;
    private PedidoInterface.Modelo modelo;

    public PedidoPresentador(PedidoInterface.Vista vista) {
        this.vista = vista;
        modelo = new PedidoModelo(this);
    }


    @Override
    public void mostrarPedidos() {

    }

    @Override
    public void armarPedidoTorta(String idpedido, String[] params, Bitmap bitmaptorta) {
        modelo.pedidoTorta(idpedido, params, bitmaptorta);
    }

    @Override
    public void generarPdf(String[] params, ArrayList<ListItem> pedidos) {
        modelo.generarPdf(params, pedidos);
    }

    @Override
    public void mostrarPdf(String path) {
            vista.mostrarPdf(path);
    }

    @Override
    public void actualizarPedidoTorta(String id) {
        modelo.obtenerPedidoTorta(id);
    }

    @Override
    public void actualizarPedido(String id, String usuario, ArrayList<ListItem> pedidos, String obs) {
        modelo.updatePedido(id, usuario, pedidos, obs);
    }

    @Override
    public void enviarPedidoTorta(ArrayList<String> ptorta) {
        vista.mostrarPedidoTorta(ptorta);
    }

    @Override
    public void mostrarError(String mensaje) {
        vista.mostrarError(mensaje);
    }

    @Override
    public void armarPedido(String usuario, ArrayList<ListItem> pedidos, String obs) {
        modelo.pedido(usuario, pedidos, obs);
    }
}
