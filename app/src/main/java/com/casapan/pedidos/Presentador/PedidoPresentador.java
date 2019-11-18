package com.casapan.pedidos.Presentador;

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
    public void getPedidos() {

    }

    @Override
    public void mostrarPedidos() {

    }

    @Override
    public void armarPedidoTorta(String [] params) {
        modelo.pedidoTorta(params);
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
    public void enviarPedidoTorta(ArrayList<String> ptorta) {
        vista.mostrarPedidoTorta(ptorta);
    }
}
