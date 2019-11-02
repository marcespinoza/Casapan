package com.casapan.pedidos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casapan.pedidos.Interface.ListItem;
import com.casapan.pedidos.R;

import java.util.ArrayList;

public class ArticuloAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ListItem> listArticulo = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ArticuloAdapter(ArrayList<ListItem> listArticulo) {
        this.listArticulo = listArticulo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_categoria, parent, false);
            return new HeaderHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo, parent, false);
            return new ArticuloHolder(layoutView);
        }
        else
            throw new RuntimeException("Could not inflate layout");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()== TYPE_HEADER)
        {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.nombreCategoria.setText(listArticulo.get(position).mostrarNombre());
        }
        else
        {
            ArticuloHolder articuloHolder = (ArticuloHolder) holder;
            articuloHolder.nombreArticulo.setText(listArticulo.get(position).mostrarNombre());
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(listArticulo.get(position).isHeader()) {
            return TYPE_HEADER;
        }else
            return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listArticulo.size();
    }

    public void removeItem(int position) {
        listArticulo.remove(position);
        notifyItemRemoved(position);
    }


    public String getId(int position){
        return listArticulo.get(position).mostrarId();
    }

    public String getNombre(int position){
        return listArticulo.get(position).mostrarNombre();
    }

    public static class ArticuloHolder extends RecyclerView.ViewHolder {

        TextView nombreArticulo;

        public ArticuloHolder(@NonNull View itemView) {
            super(itemView);
            nombreArticulo = itemView.findViewById(R.id.nombrearticulo);
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
