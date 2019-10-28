package com.casapan.pedidos.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Adapter.Utils.ListItem;
import com.casapan.pedidos.Model.Articulo;
import com.casapan.pedidos.Model.Pedido;
import com.casapan.pedidos.R;

import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ListItem> aList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public PedidoAdapter(ArrayList<ListItem> aList) {
        this.aList = aList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_categoria, parent, false);
            return new PedidoAdapter.HeaderHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo_pedido, parent, false);
            return new PedidoAdapter.ArticuloHolder(layoutView);
        }
        else
            throw new RuntimeException("Could not inflate layout");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()== TYPE_ITEM){
        ArticuloHolder articuloHolder = (ArticuloHolder) holder;
            articuloHolder.descArticulo.setText(aList.get(position).mostrarNombre());
            articuloHolder.cantidad.setShowSoftInputOnFocus(false);
            articuloHolder.stock.setShowSoftInputOnFocus(false);
            articuloHolder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(articuloHolder.check.isChecked()){

                if(articuloHolder.cantidad.hasFocus()){
                    String cant = articuloHolder.cantidad.getText().toString();
                    int cantArt = Integer.valueOf(cant);
                    cantArt++;
                    articuloHolder.cantidad.setText(String.valueOf(cantArt));
                } else if(articuloHolder.stock.hasFocus()){
                    String cant = articuloHolder.stock.getText().toString();
                    int cantArt = Integer.valueOf(cant);
                    cantArt++;
                    articuloHolder.stock.setText(String.valueOf(cantArt));
                }
                }
            }
        });
            articuloHolder.quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(articuloHolder.check.isChecked()){
                    if(articuloHolder.cantidad.hasFocus()){
                        String cant = articuloHolder.cantidad.getText().toString();
                        int cantArt = Integer.valueOf(cant);
                        cantArt--;
                        if(cantArt>=0)
                            articuloHolder.cantidad.setText(String.valueOf(cantArt));
                    } else if(articuloHolder.stock.hasFocus()){
                        String cant = articuloHolder.stock.getText().toString();
                        int cantArt = Integer.valueOf(cant);
                        cantArt--;
                        if(cantArt>=0)
                        articuloHolder.stock.setText(String.valueOf(cantArt));
                    }
                }
            }
        });
            articuloHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    articuloHolder.cantidad.setEnabled(true);
                    articuloHolder.stock.setEnabled(true);
                }else{
                    articuloHolder.cantidad.setEnabled(false);
                    articuloHolder.stock.setEnabled(false);
                }
            }
        });
        }else{
            PedidoAdapter.HeaderHolder headerHolder = (PedidoAdapter.HeaderHolder) holder;
            headerHolder.nombreCategoria.setText(aList.get(position).mostrarNombre());
        }

    }

    @Override
    public int getItemCount() {
        return aList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(aList.get(position).isHeader()) {
            return TYPE_HEADER;
        }else
            return TYPE_ITEM;
    }


    public class ArticuloHolder extends RecyclerView.ViewHolder {
        public TextView descArticulo;
        public ImageButton agregar, quitar;
        EditText cantidad, stock;
        AppCompatCheckBox check;

        public ArticuloHolder(View view) {
            super(view);
            descArticulo = view.findViewById(R.id.descarticulo);
            agregar = view.findViewById(R.id.agregar);
            quitar = view.findViewById(R.id.quitar);
            cantidad = view.findViewById(R.id.cantidadart);
            stock =  view.findViewById(R.id.stock);
            check = view.findViewById(R.id.check);
        }
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {

        TextView nombreCategoria;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            nombreCategoria = itemView.findViewById(R.id.nombrecategoria);
        }
    }
    
}
