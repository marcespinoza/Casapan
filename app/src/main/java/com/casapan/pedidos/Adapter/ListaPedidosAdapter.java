package com.casapan.pedidos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Pojo.Pedido;
import com.casapan.pedidos.R;

import java.util.ArrayList;

public class ListaPedidosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pedido> listPedidos = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public ListaPedidosAdapter(ArrayList<Pedido> listPedidos) {
        this.listPedidos = listPedidos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
            return new PedidoHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PedidoHolder pedidoHolder = (PedidoHolder) holder;
            pedidoHolder.fecha.setText(listPedidos.get(position).getFecha());
            pedidoHolder.usuario.setText(listPedidos.get(position).getUsuario());
            pedidoHolder.observacion.setText(listPedidos.get(position).getObs());
    }

    @Override
    public int getItemCount() {
        return listPedidos.size();
    }

    public static class PedidoHolder extends RecyclerView.ViewHolder {

        TextView fecha, usuario, observacion;
        ImageButton descargarpdf;

        public PedidoHolder(@NonNull View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.fecha);
            usuario = itemView.findViewById(R.id.usuario);
            observacion = itemView.findViewById(R.id.observacion);
            descargarpdf = itemView.findViewById(R.id.descargarpdf);
        }
    }

}
